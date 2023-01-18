/* OutServer.java */
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
/**
 * Clase OutServer
 */
public class OutServer {
    private static String respuestaInServer;
    /**
     * Constructor
     */
    public OutServer() { //throws IOException {
//        Server.socketOutServe = new Socket(Server.ipServeServe, Server.portServeServe);
//        /* Objeto par recibir datos */
//        Server.flujoDatosSalidaOutServe = new DataOutputStream(Server.socketOutServe.getOutputStream());
//        /* Objeto para enviar datos */
//        Server.flujoDatosEntradaOutServe = new DataInputStream(Server.socketOutServe.getInputStream());
    }

    /**
     * Inicializa las variables para conexion con el Server
     * @return
     */
    public static boolean initOutServer() {
        try {
            Server.socketOutServe = new Socket(Server.ipServeServe, Server.portServeServe);
            /* Objeto par recibir datos */
            Server.flujoDatosSalidaOutServe = new DataOutputStream(Server.socketOutServe.getOutputStream());
            /* Objeto para enviar datos */
            Server.flujoDatosEntradaOutServe = new DataInputStream(Server.socketOutServe.getInputStream());
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * Cierra la conexion el el Server
     */
    public static void closeOutServer(){
        try {
            Server.socketOutServe.close();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Utilizado para enviar un commando al Server
     * @param comando
     */
    public static void sendCommand(String comando){
        try{
            printCommandSend(comando);
            Server.flujoDatosSalidaOutServe.writeUTF(comando);
            respuestaInServer = verificarRespuesta();
            printResponseServer(respuestaInServer);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Analiza la respuesta del servidor
     */
    private static String verificarRespuesta(){
        try{
            String response = Server.flujoDatosEntradaOutServe.readUTF();
            return response;
        }catch (Exception ex){
            return ex.getMessage();
        }
    }
    /**
     *Imprime el commando a Enviar al Servidor
     * @param responseServer
     */
    private static void printCommandSend(String commandSendServer){
        System.out.println("OUT SERVER: " + commandSendServer);
    }
    /**
     * Imprime la respuesta del Servidor
     * @param responseServe
     */
    private static void printResponseServer(String responseServe){
        System.out.println("IN SERVER: " + responseServe);
    }

    /**
     * Verifica si el usuario existe
     * @return
     */
    public static boolean checkUser(String contact){
        try {
            boolean existUser = false;
            String comando = "CHECK CONTACT " + contact;
            Server.flujoDatosSalidaOutServe.writeUTF(comando);

            String respuestaServidor = Server.flujoDatosEntradaOutServe.readUTF();
            printResponseServer(respuestaServidor);
            switch (respuestaServidor){
                case "CHECK ERROR 205":
                    Server.mensajeError = "unknown contact";
                    break;
                case "CHECK ERROR 206":
                    Server.mensajeError = "not this server";
                    break;
                default:
                    existUser = true;
                    break;
            }
            return existUser;
        }catch (Exception ex){
            return false;
        }
    }
    /**
     * Envia un correo a otro Server
     * @param to
     * @param subject
     * @param body
     * @return
     */
    public static boolean sendMail(String to, String subject, String body, String de){
        try {
            boolean enviado = false;
            /* Inicio a evniar correo */
            String comando = "SEND MAIL " + to;
            Server.flujoDatosSalidaOutServe.writeUTF(comando);
            /* Contacto que envia el correo */
            comando = "MAIL FROM " + de;
            Server.flujoDatosSalidaOutServe.writeUTF(comando);
            /* Envio el Asunto*/
            comando = "MAIL SUBJECT " + subject;
            Server.flujoDatosSalidaOutServe.writeUTF(comando);
            printCommandSend(comando);
            /*Envio el mensaje*/
            comando = "MAIL BODY " + body;
            Server.flujoDatosSalidaOutServe.writeUTF(comando);
            printCommandSend(comando);
            /* Finalizo enviar el correo */
            comando = "END SEND MAIL";
            Server.flujoDatosSalidaOutServe.writeUTF(comando);
            printCommandSend(comando);
            /* Finalizo enviar el correo */
            String respuestaServidor = Server.flujoDatosEntradaOutServe.readUTF();
            switch (respuestaServidor){
                case "OK SEND MAIL":
                    Server.mensajeError = "Correo enviado";
                    enviado = true;
                    break;
                default:
                    if (respuestaServidor.contains("SEND ERROR 201")){
                        Server.mensajeError = "unknown contact";
                        enviado = false;
                    }else if (respuestaServidor.contains("SEND ERROR 202")){
                        Server.mensajeError = "no sender (from)";
                        enviado = false;
                    }else if (respuestaServidor.contains("SEND ERROR 203")){
                        Server.mensajeError = "no subject";
                        enviado = false;
                    }else if (respuestaServidor.contains("SEND ERROR 201")){
                        Server.mensajeError = "no body";
                        enviado = false;
                    }
                    break;
            }
            printResponseServer(respuestaServidor);
            return enviado;
        }catch (Exception ex){
            return false;
        }
    }


}
