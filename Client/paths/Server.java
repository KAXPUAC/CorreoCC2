/* Server.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
package paths;
import java.net.Socket;

import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.io.DataInputStream;
/**
 * Clase Img
 */
public class Server {
    /**
     * Host de Servidor
     */
    public static int portServe = 1400;
    /**
     * Ip de Servidor
     */
    public static String ipServe = "localhost";
    /**
     * Socket de conexion con el server
     */
    public static Socket sc;
    /**
     * Datos recibidos del Servidor
     */
    public static DataInputStream flujoDatosEntrada;
    /**
     *Datos enviados al Servidor
     */
    public static DataOutputStream flujoDatosSalida;
    /**
     * Mensaje Error
     */
    public static String mensajeError;
    /**
     * Mensaje Exito
     */
    public static String mensajeSuccess;
}
