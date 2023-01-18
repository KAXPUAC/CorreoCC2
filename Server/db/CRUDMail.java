/* CRUD.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
package db;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.*;
/**
 * Clase CRUD
 */
public class CRUDMail {
    //ConexionMySQL db = new ConexionMySQL();
    private String pathDB = "jdbc:mysql://localhost:3306/";
    private String user = "root";
    private String password = "Al354497553";
    private String name = "Mail";
    //public ConexionMySQL db = new ConexionMySQL(pathDB, user, password, name);
    private static ConexionMySQL db;// = new ConexionMySQL();

    public CRUDMail(){
        db = new ConexionMySQL();
        //db = new ConexionMySQL(pathDB, user, password, name);
    }

    /**
     * Crea un ussuario nuevo en la DB
     * @param firsName
     * @param lastName
     * @param mail
     * @param password
     * @return
     */
    public static boolean createUser(String firsName, String lastName, String mail, String password){
        try{
            String query = "INSERT INTO USERS(FIRSTNAME,LASTNAME,EMAIL,PASS,STATE) VALUES('";
            query = query.concat(firsName);
            query = query.concat("','");
            query = query.concat(lastName);
            query = query.concat("','");
            query = query.concat(mail).concat("@disource.com").toUpperCase();
            query = query.concat("',");
            query = query.concat("SHA2('");
            query = query.concat(password);
            query = query.concat("', 512)");
            query = query.concat(", 1)");
            return db.executeNonQuery(query);
        }catch (Exception ex){
            System.out.println("Error: Contacto");
            return false;
        }
    }
    /**
     * Metodo que valida usuario
     * @param user
     * @return
     */
    public static boolean checkUer(String user){
        try {
            boolean existe = false;
            /**
             * Select de Contrasena
             */
            String query = "SELECT * FROM USERS WHERE EMAIL = '" + user.toUpperCase() + "'";
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                if (user.toUpperCase().contains(res.getString("EMAIL"))){
                    existe = true;
                }
            }
            return existe;
        }catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return false;
        }

    }

    /**
     * Metodo que valida contrasena
     * @param user
     * @param pass
     * @return
     */
    public static boolean checkPass(String user, String pass){
        try {
            boolean existe = false;
            /**
             * Select de contrasena
             */
            String query2 = "SELECT SHA2('"+ pass.toUpperCase() +"', 512) PASS FROM DUAL";
            ResultSet resPass = db.executeQuerySting(query2);
            String passUser = "";
            while (resPass.next()){
                passUser = resPass.getString("PASS");
            }
            /**
             * Select de usuario y contrasena
             */
            String query = "SELECT * FROM USERS WHERE EMAIL = '" + user.toUpperCase() + "' AND PASS = ";
            query = query.concat("SHA2('");
            query = query.concat(pass.toUpperCase());
            query = query.concat("', 512)");
            /**
             * Compara contrasena
             */
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                if (passUser.contains(res.getString("PASS"))){
                    existe = true;
                }
            }
            return existe;
        }catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return false;
        }

    }

    /**
     * Retorna la lista de correos en imbox
     * @return
     */
    public static ArrayList<String> mailImbox(String userName){
        ArrayList<String> mails = new ArrayList<String>();
        try {
            String query = "SELECT ASUNTO, BODY, DE FROM CORREO WHERE PARA = '"+ userName +"'";
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                String mail = res.getString("DE") + " \"" + res.getString("ASUNTO") + "\"" + " \"" + res.getString("BODY") + "\"";
                mails.add(mail);
            }
            return mails;
        }catch (Exception ex){
            return mails;
        }
    }

    /**
     * Devuelve el listado de servidores
     * @return
     */
    public static ArrayList<String> serverDB(){
        ArrayList<String> servers = new ArrayList<String>();
        try {
            String query = "SELECT IDSERVER, NAMESERVER, IPSERVER, STATESERVER FROM SERVIDOR";
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                String servidor = res.getString("NAMESERVER") + "'" + res.getString("IPSERVER");
                servers.add(servidor);
            }
            return servers;
        }catch (Exception ex){
            System.out.println("Error");
            return servers;
        }
    }
    /**
     * Guarda correo enviado
     * @param para
     * @param body
     * @param asunto
     * @return
     */
    public static boolean sendEmail(String para, String body, String asunto, String de){
        try {
            boolean enviado = false;
            String dbEmail = "";
            String idServer = "";
            String query = "SELECT IDSERVER FROM SERVIDOR WHERE NAMESERVER = '" + para.split("@")[1].toUpperCase() + "'";
            //*********
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                idServer = res.getString("IDSERVER");
            }

            query = "SELECT EMAIL FROM EMAILS WHERE EMAIL = '" + para.toUpperCase() + "'";
            res = db.executeQuerySting(query);
            while (res.next()){
                dbEmail = res.getString("EMAIL");
            }
            if (!dbEmail.isEmpty()){
                if (!idServer.trim().isEmpty()){
                    query = "INSERT INTO CORREO(ASUNTO,BODY,PARA,DE,SERVE) ";
                    query = query + "  VALUES('" + asunto.toUpperCase() + "','" + body.toUpperCase() + "', '" + para.toUpperCase() + "','" + de.toUpperCase() + "'," + idServer + ")";
                    enviado = db.executeNonQuery(query);
                }
            }
            return enviado;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * Ingresa un nuevo Servidor a la DB
     * @param nameServer
     * @param ipServer
     * @return
     */
    public static boolean createServer(String nameServer, String ipServer){
        boolean created = false;
        try {
            String servidor = searchServerinDB(nameServer);
            if (servidor.isEmpty()){
                String query = "INSERT INTO SERVIDOR(NAMESERVER, IPSERVER, STATESERVER) VALUES('"+nameServer.toUpperCase()+"','"+ipServer+"',1)";
                created = db.executeNonQuery(query);
            }
            return created;
        }catch (Exception ex){
            return false;
        }
    }
    /**
     * Metodo que devuelve los contactos del usuario
     * @param userName
     * @return
     */
    public static ArrayList<String> contactsUser(String userName){
        ArrayList<String> contacts = new ArrayList<String>();
        try {
            String query = "SELECT A.EMAIL FROM CONTACT A, USERS B WHERE A.IDUSUARIOPERSONAL = B.IDUSER AND B.EMAIL = '" + userName + "'";
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                contacts.add(res.getString("EMAIL"));
            }
            return contacts;
        }catch (Exception ex){
            return contacts;
        }
    }

    /**
     * Devulve el listado de usuarios en la DB
     * @return
     */
    public static ArrayList<Usuarios> userServer(){
        ArrayList<Usuarios> usuariosServidor = new ArrayList<Usuarios>();
        try {
            String query = "SELECT FIRSTNAME, LASTNAME, EMAIL, PASS FROM USERS";
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                Usuarios users = new Usuarios();
                users.Usuario = res.getString("EMAIL");
                users.Nombre = res.getString("FIRSTNAME");
                users.Apellido = res.getString("LASTNAME");
                users.Pass = res.getString("PASS");
                usuariosServidor.add(users);
            }
            return usuariosServidor;
        }catch (Exception ex){
            return usuariosServidor;
        }
    }
    /**
     * Busca un servidor en la DB
     * @return
     */
    public static String searchServerinDB(String serve){
        String ip = "";
        try {
            String query = "SELECT * FROM SERVIDOR WHERE NAMESERVER = '" + serve.toUpperCase() + "'";
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                ip = res.getString("IPSERVER");
            }
            return ip;
        }catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return ip;
        }
    }
    /**
     * Elimina un usuario en la DB
     * @return
     */
    public static boolean deleteUser(){
        try {
            return  true;
        }catch (Exception ex){
            System.out.println("Error: Contacto");
            return false;
        }
    }
    /**
     * Agrega un contacto
     * @return
     */
    public static boolean createContact(String userName, String contact, String nameServer){
        try {
            boolean resultInsert = false;
            String idUser = "";
            String idServer = "";
            String query = "SELECT IDUSER FROM USERS WHERE EMAIL = '" + userName.toUpperCase() + "'";
            ResultSet res = db.executeQuerySting(query);
            while (res.next()){
                idUser = res.getString("IDUSER");
            }
            query = "SELECT IDSERVER FROM SERVIDOR WHERE NAMESERVER = '" + nameServer.toUpperCase() + "'";
            res = db.executeQuerySting(query);
            while (res.next()){
                idServer = res.getString("IDSERVER");
            }
            if (idServer.trim().isEmpty() || idUser.trim().isEmpty()){
                return resultInsert;
            }
            query = "INSERT INTO CONTACT (IDUSUARIOPERSONAL,EMAIL,IDSERVER) VALUES (" + idUser + ",'" + contact.toUpperCase() + "'," + idServer +")";
            resultInsert = db.executeNonQuery(query);
            return resultInsert;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * Crea un usuario en la Base de Datos
     * @param email
     * @param firstName
     * @param lastName
     * @param pass
     * @return
     */
    public static boolean createNewUser(String email, String firstName, String lastName, String pass ){
        boolean createdUser = false;
        try {
            if (!checkUer(email)){
                boolean repetido = false;
                String query = "SELECT EMAIL FROM EMAILS";

                ResultSet res = db.executeQuerySting(query);
                while (res.next()){
                    if (email.toUpperCase().contains(res.getString("EMAIL"))){
                        repetido = true;
                    }
                }
                if (!repetido){
                    query = "INSERT INTO EMAILS VALUES('"+email.toUpperCase()+"')";
                    if (db.executeNonQuery(query)){
                        query = "INSERT INTO USERS (FIRSTNAME,LASTNAME,EMAIL,PASS,STATE) VALUES ('"+firstName.toUpperCase()+"','"+lastName.toUpperCase()+"', '"+email.toUpperCase()+"',SHA2('"+pass+"', 512),0)";
                        createdUser = db.executeNonQuery(query);
                    }
                }
            }
            return createdUser;
        }catch (Exception ex){
            return false;
        }
    }
}
