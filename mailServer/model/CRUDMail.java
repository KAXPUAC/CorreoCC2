/* CRUD.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Sección: AN
 **/
package model;
import db.*;

/**
 * Clase CRUD
 */
public class CRUDMail {
    //ConexionMySQL db = new ConexionMySQL();
    private String pathDB = "jdbc:mysql://localhost:3306/";
    private String user = "root";
    private String password = "Al354497553";
    private String name = "Mail";
    public ConexionMySQL db = new ConexionMySQL(pathDB, user, password, name);

    public CRUDMail(){
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
    public boolean createUser(String firsName, String lastName, String mail, String password){
        try{
            String query;
            query = "INSERT INTO USERS(FIRSTNAME,LASTNAME,EMAIL,PASS) VALUES('"+firsName+"','"+lastName+"','"+mail+"','"+password+"')";
            return db.executeNonQuery(query);
        }catch (Exception ex){
            System.out.println("Error: Contacto");
            return false;
        }
    }

    /**
     * Elimina un usuario en la DB
     * @return
     */
    public boolean deleteUser(){
        try {
            return  true;
        }catch (Exception ex){
            System.out.println("Error: Contacto");
            return false;
        }
    }

    public boolean createContact(){
        try {
            return  true;
        }catch (Exception ex){
            System.out.println("Error: Contacto");
            return false;
        }
    }

    public boolean deleteContact(){
        try {
            return  true;
        }catch (Exception ex){
            System.out.println("Error: Contacto");
            return false;
        }
    }
    public static void main(String args[]){
        try {
            CRUDMail crud = new CRUDMail();
            boolean creado = crud.createUser("Manfredy","Axpuac","manfjaxpu@mail.com","prueba");
            if (creado){
                System.out.println("Usuario Creado");
            }else {
                System.out.println("Error al crear el usuario");
            }
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
