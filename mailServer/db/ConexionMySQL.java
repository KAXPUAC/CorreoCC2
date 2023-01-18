/* ConexionMySQL.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Sección: AN
 **/
package db;
import java.sql.*;
import java.lang.reflect.*;
import java.util.Hashtable;

import java.io.File;

/**
 * Clase ConexionMySQL
 */
public class ConexionMySQL {
    /**
     *  @param cnx Connection to DB
     */
    private static Connection cnx = null;
    private static String urlDB, userDB, passwordDB, nameDB;
    private Hashtable<String,ResultSet> RSHash;
    /**
     * Abre una conexion hacia la Base de Datos
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static Connection openConnection() throws SQLException,  ClassNotFoundException {
        if (cnx == null) {
            try {

                String driverJar = (new File (".").getAbsolutePath());
                driverJar = driverJar.substring(0,driverJar.length() - 1);
                driverJar = driverJar + "mysql-connector-java-8.0.18.jar";
                //System.out.println(driverJar);

                //DriverDB.appendPath(driverJar);
                Class.forName("com.mysql.cj.jdbc.Driver");

                cnx = DriverManager.getConnection(urlDB + nameDB, userDB, passwordDB);
                //System.out.println("Conexion Exitosa");
            } catch (SQLException ex) {
                System.out.println("Error: Conexion 1");
                throw new SQLException(ex);
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: Conexion 2");
                throw new ClassCastException(ex.getMessage());
            } catch (Exception e){
                System.out.println("Error: Conexion");
                System.out.println(e.getMessage());
            }
        }
        return cnx;
    }
    /**
     * Cierra la conexion de la db
     * @throws SQLException
     */
    private static void closeConnection() throws SQLException {
        try {
            if (cnx != null) {
                cnx.close();
                cnx = null;
            }
        }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Constructor de ConexionMySQL
     */
    public ConexionMySQL(){
        urlDB = "jdbc:mysql://cc2-instance-1.c6v4bv6mx5lv.us-east-1.rds.amazonaws.com:3306/";
        nameDB = "MAIL";
        userDB = "admin";
        passwordDB = "yCSZEvxll9vpCVPG6fbq";
        RSHash = new Hashtable<String,ResultSet>();
        //cnx = openConnection();
    }
    /**
     * Constructor de ConexionMySQL recibiendo los datos
     */
    public ConexionMySQL(String pathDB, String user, String password, String name){
        urlDB = pathDB;
        nameDB = name;
        userDB = user;
        passwordDB = password;
        RSHash = new Hashtable<String,ResultSet>();
        //cnx = openConnection();
    }

    /**
     Es utilizado para realizar operaciones sql que regresan un set de resultados asiciados a una consulta, el set de resultados se guarda en la estructura default de sets de resultados. Por ejemplo un select a la base de datos
     @param	q	representa la consulta sql que se desea realizar.
     @return boolean	si el query se ejecuto con exito el valor de retorno es true, en caso contrario es false
     **/
    public boolean executeQuery(String q) throws SQLException {
        Statement stm = null;
        boolean status = false;
        try{
            cnx = openConnection();
            stm = cnx.createStatement();
        }catch(Exception e){
            String error_msg = "There was an error when creating the statement, make sure the connection to the database has been established";
            System.out.println(error_msg);
        }
        try{
            ResultSet rs = stm.executeQuery(q);
            while (rs.next()){
                System.out.println("Email");
                System.out.println(rs.getString("Email"));
            }
            RSHash.put("default",rs);
            status = true;
        }catch(Exception e){
            String error_msg = "There was an error when processing the query, aditional information below:\n"+e.getMessage();
            System.out.println(error_msg);
        }
        closeConnection();
        return status;
    }
    /**
     Es utilizado para realizar operaciones sql que regresan un set de resultados asociados a una consulta. Por ejemplo un select a la base de datos
     @param	q	representa la consulta sql que se desea realizar.
     @param  resultSetName indica el nombre del set de resultados con el cual se desea trabajar.
     @return	boolean	si el query fue ejecutado exitosamente se regresa true, en caso contario false sera el valor de retorno.
     **/
    public boolean executeQuery(String q,String resultSetName) throws SQLException {
        Statement stm = null;
        boolean status = false;
        try{
            cnx = openConnection();
            stm = cnx.createStatement();
        }catch(Exception e){
            String error_msg = "There was an error when creating the statement, make sure the connection to the database has been established";
            System.out.println(error_msg);
        }
        try{
            RSHash.put(resultSetName,stm.executeQuery(q));
            status = true;
        }catch(Exception e){
            String error_msg = "There was an error when processing the query, aditional information below:\n"+e.getMessage();
            System.out.println(error_msg);
        }
        closeConnection();
        return status;
    }

    /**
     Ejecuta una sentencia sql sobre la base de datos, pero esta no regresa algun set de resultados.
     @param	q	sentencia sql a realizar.
     @return	boolean	el valor de retorno sera false si la base de datos no a sido inicializada o la sentencia sql tenga algun error sintactico
     **/
    public boolean executeNonQuery(String q) throws SQLException {
        Statement stm = null;
        boolean status = false;
        try{
            cnx = openConnection();
            stm = cnx.createStatement();
        }catch(Exception e){
            String error_msg = "There was an error when creating the statement, make sure the connection to the database has been established";
            System.out.println(error_msg);
        }
        try {
            stm.executeUpdate(q);
            status = true;
        }catch(Exception e){
            String error_msg = "There was an error when processing the query, aditional information below:\n"+e.getMessage();
            System.out.println(error_msg);
        }
        return status;
    }
    /**
     *
     * @param args
     */
//    public static void main(String[] args){
//        try {
//            //ConexionMySQL db = new ConexionMySQL();
//            String pathDB = "jdbc:mysql://localhost:3306/";
//            String user = "root";
//            String password = "Al354497553";
//            String name = "Mail";
//            ConexionMySQL db = new ConexionMySQL(pathDB, user, password, name);
//            String query = "SELECT * FROM USERS";
//            boolean consulta = db.executeQuery(query);
//            if (consulta){
//                System.out.println("Consulta realizada Correctamente");
//            }
//            else{
//                System.out.println("Error en la consulta");
//            }
//            query = "INSERT INTO USERS(FIRSTNAME,LASTNAME,EMAIL,PASS) VALUES('KEVIN','JUAREZ','KEVINJUAREZ@MAIL.COM','JUAREZ')";
//            consulta = db.executeNonQuery(query);
//            if (consulta){
//                System.out.println("Usuario Creado");
//                consulta = db.executeQuery("SELECT * FROM USERS");
//            }else {
//                System.out.println("Error en crear usuario");
//            }
////            cnx = openConnection();
////            PreparedStatement ps;
////            ResultSet res;
////            ps = cnx.prepareStatement("SELECT * FROM USERS");
////            res = ps.executeQuery();
////            if (res.next()){
////                System.out.println("Email");
////                System.out.println(res.getString("Email"));
////            }else {
////                System.out.println("No hay datos");
////            }
////            //con.close();
////            closeConnection();
////            for (int i = 0; i < 10; i++){
////                System.out.println("*******************************");
////                cnx = openConnection();
////                ps = cnx.prepareStatement("SELECT * FROM USERS");
////                res = ps.executeQuery();
////                while (res.next()){
////                    System.out.println("Email");
////                    System.out.println(res.getString("Email"));
////                }
////                closeConnection();
////                System.out.println("*******************************");
////            }
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }

}
