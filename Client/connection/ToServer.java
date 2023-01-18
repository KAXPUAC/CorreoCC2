/* ToServer.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
package connection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;
import paths.*;
import models.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase ToServer
 */

public class ToServer {
    private static String respuestaServidor;
    /**
     * Constructor ToServer
     */
    public ToServer() throws IOException {
        Server.sc = new Socket(Server.ipServe, Server.portServe);
        /* Objeto par recibir datos */
        Server.flujoDatosSalida = new DataOutputStream(Server.sc.getOutputStream());
        /* Objeto para enviar datos */
        Server.flujoDatosEntrada = new DataInputStream(Server.sc.getInputStream());
    }
    /**
     * Cierra la conexion con el Servidor
     */
    public static void closeToServer(){
        try {
            Server.sc.close();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Utilizado para enviar commando al Servidor
     * @param comando
     */
    public static void sendCommand(String comando){
        try{
            printCommandSend(comando);
            Server.flujoDatosSalida.writeUTF(comando);
            respuestaServidor = verificarRespuesta();
            printResponseServer(respuestaServidor);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Metodo que envia correo electronico
     * @param to
     * @param subject
     * @param body
     * @return
     */
    public static boolean sendMail(String to, String subject, String body){
        try {
            boolean enviado = false;
            /* Inicio a evniar correo */
            String comando = "SEND MAIL";
            Server.flujoDatosSalida.writeUTF(comando);
            if (to.contains(",")){
                String[] mails = to.split(",");
                for (int i = 0; i < mails.length; i++){
                    if (i == mails.length - 1){
                        comando = "MAIL TO " + mails[i].trim() + " *";
                    }else {
                        comando = "MAIL TO " + mails[i].trim();
                    }
                    Server.flujoDatosSalida.writeUTF(comando);
                    printCommandSend(comando);
                }
            }else {
                comando = "MAIL TO " + to.trim() + " *";
                Server.flujoDatosSalida.writeUTF(comando);
                printCommandSend(comando);
            }
            /* Envio el Asunto*/
            comando = "MAIL SUBJECT " + subject;
            Server.flujoDatosSalida.writeUTF(comando);
            printCommandSend(comando);
            /*Envio el mensaje*/
            comando = "MAIL BODY " + body;
            Server.flujoDatosSalida.writeUTF(comando);
            printCommandSend(comando);
            /* Finalizo enviar el correo */
            comando = "END SEND MAIL";
            Server.flujoDatosSalida.writeUTF(comando);
            printCommandSend(comando);
            /* Finalizo enviar el correo */
            String respuestaServidor = Server.flujoDatosEntrada.readUTF();
            switch (respuestaServidor){
                case "OK SEND MAIL":
                    Server.mensajeError = "Correo enviado";
                    enviado = true;
                    break;
                default:
                    if (respuestaServidor.contains("SEND ERROR 104")){
                        Server.mensajeError = "unknown contact";
                        enviado = false;
                    }else if (respuestaServidor.contains("SEND ERROR 105")){
                        Server.mensajeError = "unknown server";
                        enviado = false;
                    }else if (respuestaServidor.contains("SEND ERROR 106")){
                        Server.mensajeError = "no recipient(s)";
                        enviado = false;
                    }else if (respuestaServidor.contains("SEND ERROR 107")){
                        Server.mensajeError = "no subject";
                        enviado = false;
                    }else if (respuestaServidor.contains("SEND ERROR 108")){
                        Server.mensajeError = "no body";
                        enviado = false;
                    }
                    break;
            }
            printResponseServer(respuestaServidor);
            setTimeout(() -> noOperation(), 20000);
            return enviado;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * Metodo para anadir un contacto.
     * @param contacto
     * @return
     */
    public static boolean createContact(String contacto){
        try {
            boolean creado = false;
            String comando = "NEWCONT " + contacto;
            Server.flujoDatosSalida.writeUTF(comando);
            printCommandSend(comando);
            String respuestaServidor = Server.flujoDatosEntrada.readUTF();
            if (respuestaServidor.contains("OK NEWCONT " + contacto)){
                creado = true;
            }else if (respuestaServidor.contains("NEWCONT ERROR 109")){
                Server.mensajeError = "contact not found";
                creado = false;
            }else if (respuestaServidor.contains("NEWCONT ERROR 110")){
                Server.mensajeError = "server not found";
                creado = false;
            }else {
                Server.mensajeError = respuestaServidor;
                creado = false;
            }
            printResponseServer(respuestaServidor);
            setTimeout(() -> noOperation(), 20000);
            return creado;
        }catch (Exception ex){
            return false;
        }
    }
    /**
     * Carga los Contactos de un usuario
     * @return
     */
    public static boolean listContacts(){
        try {
            boolean loadaContacts = false;
            String comando = "CLIST " + Perfil.email;
            printCommandSend(comando);
            Server.flujoDatosSalida.writeUTF(comando);
            String respuestaServidor = "";
            boolean lastContact = false;
            Perfil.listContact = new ArrayList<String>();
             while (!lastContact){
                 respuestaServidor = Server.flujoDatosEntrada.readUTF();
                 printResponseServer(respuestaServidor);

                 switch (respuestaServidor){
                     case "CLIST ERROR 103":
                         loadaContacts = false;
                         Server.mensajeError = "no contacts found";
                         lastContact = true;
                         break;
                     default:
                         String[] serverResponse = respuestaServidor.split(" ");
                         switch (serverResponse[0] + serverResponse [1]){
                             case "OKCLIST":
                                 if (serverResponse.length == 4){
                                     Perfil.listContact.add(serverResponse[2]);
                                     lastContact = true;
                                     loadaContacts = true;
                                 }else {
                                     loadaContacts = true;
                                     Perfil.listContact.add(serverResponse[2]);
                                 }
                                 break;
                             default:
                                 loadaContacts = true;
                                 lastContact = true;
                                 break;
                         }
                 }
             }
            setTimeout(() -> noOperation(), 20000);
            return loadaContacts;
        }catch (Exception ex){
            Server.mensajeError = ex.getMessage();
            System.out.println(ex.getMessage());
            return false;
        }
    }
    /**
     * Carga los emails recibidos
     * @return
     */
    public static boolean listMails(){
        try {
            boolean loadMails = false;
            String comando = "GETNEWMAILS " + Perfil.email;
            printCommandSend(comando);
            Server.flujoDatosSalida.writeUTF(comando);
            String respuestaServidor = "";
            boolean lastMail = false;
            Perfil.listMails = new ArrayList<String>();
            while (!lastMail){
                respuestaServidor = Server.flujoDatosEntrada.readUTF();
                printResponseServer(respuestaServidor);
                switch (respuestaServidor){
                    case "OK GETNEWMAILS NOMAILS":
                        loadMails = false;
                        Server.mensajeError = "No mails found";
                        lastMail = true;
                        break;
                    default:
                        String[] serverResponse = respuestaServidor.split(" ");
                        String[] mailImbox = respuestaServidor.split("\"");
                        switch (serverResponse[0] + serverResponse [1]){
                            case "OKGETNEWMAILS":
                                if (mailImbox.length == 5){
                                    String mail = serverResponse[2] + "concatCC2" + mailImbox[1] + "concatCC2" + mailImbox[3];
                                    Perfil.listMails.add(mail);
                                    lastMail = true;
                                    loadMails = true;
                                }
                                else {
                                    if (mailImbox.length == 4){
                                        String mail = serverResponse[2] + "concatCC2" + mailImbox[1] + "concatCC2" + mailImbox[3];
                                        Perfil.listMails.add(mail);
                                        loadMails = true;
                                    }
                                }
                                break;
                            default:
                                loadMails = true;
                                lastMail = true;
                                break;
                        }
                }
            }
            setTimeout(() -> noOperation(), 20000);
            return loadMails;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * Utilizado para hacer un logOut del server
     * @return
     */
    public static boolean logOut(){
        String comando = "LOGOUT";
        boolean salir = false;
        try {
            printCommandSend(comando);
            Server.flujoDatosSalida.writeUTF(comando);
            String respuestaServidor = Server.flujoDatosEntrada.readUTF();
            printResponseServer(respuestaServidor);
            switch (respuestaServidor){
                case "OK LOGOUT":
                    closeToServer();
                    salir = true;
                    break;
                default:
                    Server.mensajeError = "Error LOGOUT";
                    salir = false;
                    break;
            }
            return salir;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * Loguea en el servidor
     * @param comando
     * @return
     */
    public static boolean loginCommandSend(String comando){
        try{
            boolean loged = false;
            printCommandSend(comando);
            Server.flujoDatosSalida.writeUTF(comando);
            String respuestaServidor = Server.flujoDatosEntrada.readUTF();
            switch (respuestaServidor){
                case "LOGIN ERROR 102":
                    loged = false;
                    Server.mensajeError = "invalid password";
                    break;
                case "LOGIN ERROR 101":
                    loged = false;
                    Server.mensajeError = "unknown user";
                    break;
                case "OK LOGIN":
                    loged = true;
                    break;
                default:
                    loged = false;
                    Server.mensajeError = respuestaServidor;
                    break;
            }
            printResponseServer(respuestaServidor);
            setTimeout(() -> noOperation(), 20000);
            return loged;
        }catch (Exception ex){
            Server.mensajeError = ex.getMessage();
            System.out.println(ex.getMessage());
            return false;
        }
    }
    /**
     *Imprime el commando a Enviar al Servidor
     * @param responseServer
     */
    private static void printCommandSend(String commandSendServer){
        System.out.println("Enviar: " + commandSendServer);
    }
    /**
     * Imprime la respuesta del Servidor
     * @param responseServe
     */
    private static void printResponseServer(String responseServe){
        System.out.println("Respuesta: " + responseServe);
    }

    /**
     * Analiza la respuesta del servidor
     */
    private static String verificarRespuesta(){
        try{
            String response = Server.flujoDatosEntrada.readUTF();
            return response;
        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    /**
     * Envia comando NOOP al Servidor
     */
    public static void noOperation(){
        String comando = "NOOP";
        try {
            printCommandSend(comando);
            Server.flujoDatosSalida.writeUTF(comando);
            String respuestaServidor = Server.flujoDatosEntrada.readUTF();
            printResponseServer(respuestaServidor);
            switch (respuestaServidor){
                case "OK NOOP":
                    break;
                default:
                    break;
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /**
     * TimeOut para ejecutar tarea
     * setTimeout(() -> System.out.println("String"), 20000);
     * setTimeout(() -> noOperation(), 20000);
     * @param runnable
     * @param delay
     */
    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

}

