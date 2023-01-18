/* Server.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
package paths;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
/**
 * Clase Img
 */
public class Server {
    public static String nameServer = "DISOURCE";
    /**
     * MENSAJES DE ERROR Y SUCCES
     */
    public static String mensajeError;
    public static String mensajeSuccess;
    /**
     * Concetar con otro servidor como Servidor
     */
    public static int portServeServe = 1500;
    public static ServerSocket socketServerServer;
    public static Socket conectServerServer;
    public static DataInputStream flujoDatosEntradaServer;
    public static DataOutputStream flujoDatosSalidaServer;
    /**
     * Conectar con otro Servidor como Cliente
     * Se utiliza la misma variable portServeServe
     */
    public static String ipServeServe = "localhost";
    public static Socket socketOutServe;
    public static DataInputStream flujoDatosEntradaOutServe;
    public static DataOutputStream flujoDatosSalidaOutServe;
    /**
     * Conectar Con cliente
     */
    public static int portServeClient = 1400;
    public static String ipServe = "localhost";
    public static ServerSocket socketServer;
    public static Socket connectClient;
    public static DataInputStream flujoDatosEntradaCliente;
    public static DataOutputStream flujoDatosSalidaCliente;
    /**
     * Conectar con DNS como Cliente
     */
    public static int portServeDNS = 1200;
    public static String ipDNS = "localhost";
    public static Socket socketDNS;
    public static DataInputStream flujoDatosEntradaDNS;
    public static DataOutputStream flujoDatosSalidaDNS;
}
