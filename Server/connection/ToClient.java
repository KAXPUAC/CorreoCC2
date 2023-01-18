/* ToClient.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
package connection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import paths.*;
import db.*;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase ToClient
 */
public class ToClient extends Thread {
    private CRUDMail crudDb = new CRUDMail();
    private String userClient = "";
    /**
     * Constructor ToClient
     */
    public ToClient() throws IOException {
        Server.socketServer = new ServerSocket(Server.portServeClient);
        Server.connectClient = new Socket();
        System.out.println("Esperando Cliente");
        Server.connectClient = Server.socketServer.accept();
        System.out.println("Cliente en linea ");
        run();
    }

    /**
     * Cierra la conexion con el cliente
     */
    public static void closeToClient(){
        try {
            Server.socketServer.close();
        }catch (Exception ex){
            System.out.println("Fin de la conexion");
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Run del Thread
     */
    public void run(){
        try {
            Server.flujoDatosSalidaCliente = new DataOutputStream(Server.connectClient.getOutputStream());
            //System.out.println("Conectado al Servidor, direccion: " + Server.connectClient.getRemoteSocketAddress());

            Server.flujoDatosEntradaCliente = new DataInputStream(Server.connectClient.getInputStream());
            String commanClient;
            while (true){
                commanClient = Server.flujoDatosEntradaCliente.readUTF();
                System.out.println("CLIENTE: " + commanClient);
                String response = verificarRespuesta(commanClient);
                Server.flujoDatosSalidaCliente.writeUTF(response);
                System.out.println("SERVER: " + response);
            }
        }catch (Exception e){
            closeToClient();
            try {
                new ToClient();
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * Analiza el comando y toma acciones, retorna una string response
     * @param inCommandServer
     * @return
     */
    private String verificarRespuesta(String inCommandClien){
        String respuesta = "INVALID COMMAND ERROR";
        try {
            String[] clientComman = inCommandClien.split(" ");
            switch (clientComman[0]){
                case "LOGIN":
                    if (clientComman.length == 3){
                        respuesta = (!CRUDMail.checkUer(clientComman[1])) ? "LOGIN ERROR 101" : (!CRUDMail.checkPass(clientComman[1],clientComman[2])) ? "LOGIN ERROR 102" : "OK LOGIN";
                        if (respuesta == "OK LOGIN"){
                            userClient = new String(clientComman[1]);
                        }
                    }
                    break;
                case "CLIST":
                    if (clientComman.length == 2){
                        ArrayList<String> contactos = CRUDMail.contactsUser(userClient);
                        if (contactos.size() > 0){
                            String lastResponse = "";
                            for (int i = 0; i < contactos.size(); i++){
                                if (i == contactos.size() -1){
                                    lastResponse = contactos.get(i);
                                }else {
                                    Server.flujoDatosSalidaCliente.writeUTF("OK CLIST " + contactos.get(i));
                                }
                            }
                            respuesta = "OK CLIST " + lastResponse + " *";
                        }else {
                            respuesta = "CLIST ERROR 103";
                        }
                    }
                    break;
                case "GETNEWMAILS":
                    if (clientComman.length == 2){
                        ArrayList<String> mails = CRUDMail.mailImbox(clientComman[1]);
                        if (mails.size() > 0){
                            String lastResponse = "";
                            for (int i = 0; i < mails.size(); i++){
                                if (i == mails.size() -1){
                                    lastResponse = mails.get(i);
                                }else {
                                    Server.flujoDatosSalidaCliente.writeUTF("OK GETNEWMAILS " + mails.get(i));
                                }
                            }
                            respuesta = "OK GETNEWMAILS " + lastResponse + " *";
                        }else {
                            respuesta = "OK GETNEWMAILS NOMAILS";
                        }
                    }
                    break;
                case "NEWCONT":
                    if (clientComman.length == 2){
                        String[] contacto = clientComman[1].split("@");
                        if (contacto[1].toUpperCase().contains("DISOURCE")){
                            boolean creado = CRUDMail.createContact(userClient,clientComman[1],contacto[1]);
                            respuesta = creado ? "OK NEWCONT " + clientComman[1] : "NEWCONT ERROR 109 " + clientComman[1];
                        }else {
                            // Enviar a otro Server
                            respuesta = "NEWCONT ERROR 110 " + clientComman[1];
                        }
                    }
                    break;
                case "NOOP":
                    respuesta = "OK NOOP";
                    break;
                case "SEND":
                    boolean sendMails = false;
                    String messageSend = "";
                    String subjectMailSend = "";
                    ArrayList<String> para = new ArrayList<String>();
                    while (!sendMails){
                        String commanClient = Server.flujoDatosEntradaCliente.readUTF();
                        if (commanClient.contains("END SEND MAIL")){
                            sendMails = true;
                        }else {
                            String[] actions = commanClient.split(" ");
                            switch (actions[1]){
                                case "TO":
                                    para.add(actions[2]);
                                    break;
                                case "SUBJECT":
                                    for (int i = 2; i < actions.length; i++){
                                        subjectMailSend = subjectMailSend + " " + actions[i];
                                    }
                                    break;
                                case "BODY":
                                    for (int i = 2; i < actions.length; i++){
                                        messageSend = messageSend + " " + actions[i];
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    boolean enviado = false;
                    respuesta = "OK SEND MAIL";
                    for (int i = 0; i < para.size(); i++){
                        enviado = CRUDMail.sendEmail(para.get(i), messageSend, subjectMailSend, userClient);
                        if (!enviado){
                            Server.ipServeServe = CRUDMail.searchServerinDB(para.get(i).split("@")[1].toUpperCase());
                            if (!Server.ipServeServe.isEmpty()){
                                if(OutServer.initOutServer()){
                                    if(OutServer.checkUser(para.get(i))){
                                        if(OutServer.sendMail(para.get(i),subjectMailSend, messageSend, userClient)){
                                            respuesta = "OK SEND MAIL";
                                        }else {
                                            respuesta = Server.mensajeError;
                                        }
                                    }else {
                                        respuesta = "SEND ERROR 104 " + para.get(i);
                                    }
                                    OutServer.closeOutServer();
                                }
                            }else {
                                respuesta = "SEND ERROR 105 " + para.get(i);
                            }
                            break;
                        }
                    }
                    break;
                case "LOGOUT":
                    respuesta = "OK LOGOUT";
                    break;
                default:
                    respuesta = "INVALID COMMAND ERROR";
                    break;
            }
            return respuesta;
        }catch (Exception ex){
            return ex.getMessage();
        }

    }
}
