/* ToDNS.java */
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
public class ToDNS {
    /**
     * Constructor
     */
    public ToDNS() throws IOException {
        Server.socketDNS = new Socket(Server.ipDNS, Server.portServeDNS);
        /* Objeto par recibir datos */
        Server.flujoDatosSalidaDNS = new DataOutputStream(Server.socketDNS.getOutputStream());
        /* Objeto para enviar datos */
        Server.flujoDatosEntradaDNS = new DataInputStream(Server.socketDNS.getInputStream());
        onLine();
        ipTable();
    }

    /**
     * Envia al DNS que el Servidor esta en Linea
     * @return
     */
    public static boolean onLine(){
        try {
            boolean onLine = false;
            String ipLocal = " " + Server.socketDNS.getLocalSocketAddress();
            ipLocal = ipLocal.trim().split(":")[0].split("/")[1];
            String comando = "ONLINE " + Server.nameServer + " " + ipLocal;
            Server.flujoDatosSalidaDNS.writeUTF(comando);
            printCommandSend(comando);
            String respuestaServidor = Server.flujoDatosEntradaDNS.readUTF();
            printResponseServer(respuestaServidor);
            switch (respuestaServidor){
                case "OK ONLINE DISOURCE":
                    Server.mensajeError = "unknown contact";
                    onLine = true;
                    break;
                case "ONLINE ERROR 301":
                    Server.mensajeError = "ip invalido";
                    break;
                default:
                    break;
            }
            return onLine;
        }catch (Exception ex){
            return false;
        }
    }
    /**
     * Envia al DNS que el Servidor esa fuera de Linea
     * @return
     */
    public static boolean ofLine(){
        try {
            boolean ofLine = false;
            String comando = "OFFLINE DISOURCE";
            Server.flujoDatosSalidaDNS.writeUTF(comando);
            printCommandSend(comando);
            String respuestaServidor = Server.flujoDatosEntradaDNS.readUTF();
            printResponseServer(respuestaServidor);
            switch (respuestaServidor){
                case "OK OFFLINE DISOURCE":
                    Server.mensajeError = "unknown contact";
                    ofLine = true;
                case "OFFLINE ERROR 302":
                    Server.mensajeError = "no server found";
                    break;
                default:
                    break;
            }
            return ofLine;
        }catch (Exception ex){
            return false;
        }
    }
    /**
     * Trae las ip y nombres de los servidores en linea
     * @return
     */
    public static boolean ipTable(){
        try {
            boolean loadIpTable = false;
            String comando = "GETIPTABLE";
            Server.flujoDatosSalidaDNS.writeUTF(comando);
            printCommandSend(comando);
            String respuestaDNS = "";
            boolean lastServer = false;
            while (!lastServer){
                respuestaDNS  = Server.flujoDatosEntradaDNS.readUTF();
                printResponseServer(respuestaDNS);
                switch (respuestaDNS){
                    case "GETIPTABLE ERROR 303":
                        loadIpTable = false;
                        lastServer = true;
                        Server.mensajeError = "no server found";
                        break;
                    default:
                        String[] responseDNS = respuestaDNS.split(" ");
                        if (responseDNS.length >= 4){
                            if (respuestaDNS.contains("*")){
                                lastServer = true;
                                loadIpTable = true;
                                CRUDMail.createServer(responseDNS[2],responseDNS[3]);
                            }else {
                                loadIpTable = true;
                                CRUDMail.createServer(responseDNS[2],responseDNS[3]);
                            }
                        }
                        break;
                }
            }
            return loadIpTable;
        }catch (Exception ex){
            return false;
        }
    }
    /**
     *Imprime el commando a Enviar al Servidor
     * @param responseServer
     */
    private static void printCommandSend(String commandSendServer){
        System.out.println("OUT DNS: " + commandSendServer);
    }
    /**
     * Imprime la respuesta del Servidor
     * @param responseServe
     */
    private static void printResponseServer(String responseServe){
        System.out.println("IN DNS: " + responseServe);
    }
}
