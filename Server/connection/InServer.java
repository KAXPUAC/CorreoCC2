/* InServer.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
package connection;
/* Paquetes Kevin Axpuac */
import paths.*;
import db.*;
/* Clases Java */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase InServer
 * Para conexion con otro Server como cliente
 */
public class InServer extends Thread {
    //private CRUDMail crudDb = new CRUDMail();
    /**
     * Constructor
     */
    public InServer() throws IOException {
        /* Inicializo el Server to Server */
        Server.socketServerServer = new ServerSocket(Server.portServeServe);
        Server.conectServerServer = new Socket();
    }
    /**
     * Cierra la conexion con el server
     */
    public static void closeInServer(){
        try {
            Server.socketServerServer.close();
        }catch (Exception ex){
            System.out.println("Fin de la conexion");
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Run de Thread
     */
    public void run(){
        try {
            System.out.println("Esperando Servidor");
            Server.conectServerServer = Server.socketServerServer.accept();
            System.out.println("Servidor en linea");

            Server.flujoDatosSalidaServer = new DataOutputStream(Server.conectServerServer.getOutputStream());
            Server.flujoDatosEntradaServer = new DataInputStream(Server.conectServerServer.getInputStream());
            String commandServer;
            while (true) {
                commandServer = Server.flujoDatosEntradaServer.readUTF();
                System.out.println("IN SERVER: " + commandServer);
                String response = verificarRespuesta(commandServer);
                Server.flujoDatosSalidaServer.writeUTF(response);
                System.out.println("OUT SERVER: " + response);
            }
        }catch (Exception e){
            closeInServer();
            try {
                InServer inServer = new InServer();
                inServer.start();
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
    private String verificarRespuesta(String inCommandServer){
        String respuesta = "INVALID COMMAND ERROR";
        try {
            String[] serverComands = inCommandServer.split(" ");
            switch (serverComands[0]){
                case "SEND":
                    boolean sendMails = false;
                    String messageSend = "";
                    String subjectMailSend = "";
                    String userClient = "";
                    ArrayList<String> para = new ArrayList<String>();
                    while (!sendMails){
                        String commanServidor = Server.flujoDatosEntradaServer.readUTF();
                        if (commanServidor.contains("END SEND MAIL")){
                            sendMails = true;
                        }else {
                            String[] actions = commanServidor.split(" ");
                            if (actions.length >= 3){
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
                                    case "FROM":
                                        userClient = actions[2];
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    if (messageSend.isEmpty()){
                        respuesta = "SEND ERROR 204";
                        break;
                    }
                    if (userClient.isEmpty()){
                        respuesta = "SEND ERROR 202";
                        break;
                    }
                    if (subjectMailSend.isEmpty()){
                        respuesta = "SEND ERROR 203";
                        break;
                    }
                    boolean enviado = false;
                    respuesta = "OK SEND MAIL";
                    for (int i = 0; i < para.size(); i++){
                        enviado = CRUDMail.sendEmail(para.get(i), messageSend, subjectMailSend, userClient);
                        if (!enviado){
                            respuesta = "SEND ERROR 201 " + para.get(i);
                            break;
                        }
                    }
                    break;
                case "CHECK":
                    if (serverComands.length == 3){
                        switch (serverComands[1]){
                            case "CONTACT":
                                if (serverComands[2].toUpperCase().contains(Server.nameServer)){
                                    if (CRUDMail.checkUer(serverComands[2])){
                                        respuesta = "OK CHECK CONTACT";
                                    }else {
                                        respuesta = "CHECK ERROR 205";
                                    }
                                }else {
                                    respuesta = "CHECK ERROR 206";
                                }
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
            return respuesta;
        }catch (Exception ex){
            return ex.getMessage();
        }
    }
}
