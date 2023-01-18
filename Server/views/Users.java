/* Users.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
package views;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import paths.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import db.*;
import models.*;

/**
 * Clase Users
 */
public class Users  extends JFrame implements ActionListener {
    private JButton btnRegresar, btnActualizar, btnAgregar;
    private JTable dtUsers;
    private JScrollPane scrollUsers;
    private JPanel dtPanel;
    ArrayList<Usuarios> usersdt;

    /**
     * Constructor de clase Users
     */
    public Users(){

        setTitle("Contactos");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));

        btnRegresar = new JButton("Menu");
        btnRegresar.setBounds(465,5,130,30);
        btnRegresar.addActionListener(this);
        add(btnRegresar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(465,40,130,30);
        btnActualizar.addActionListener(this);
        add(btnActualizar);

        btnAgregar = new  JButton("Agregar Usuario");
        btnAgregar.setBounds(465,75,130,30);
        btnAgregar.addActionListener(this);
        add(btnAgregar);

        //createTable();
        loadServers();

        //setSize(acho,altura);
        setSize(599,349);
        setSize(600,350);
    }
    /**
     * Carga los datos
     */
    public void loadServers(){
        usersdt = CRUDMail.userServer();
        createTable();
    }
    /**
     * Crea la tabla
     */
    public void createTable(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Correo");
        modelo.addColumn("Password");
        /**
         * Modificar
         */
        for (int i = 0; i < usersdt.size(); i++) {
            Usuarios users = usersdt.get(i);
            modelo.addRow(new Object[]{ users.getNombre(), users.getApellido(), users.getUsuario(), users.getPass() });
        }
        dtUsers = new JTable();
        dtUsers.setModel(modelo);
        scrollUsers = new JScrollPane(dtUsers);

        dtPanel = new JPanel(new BorderLayout());
        dtPanel.add(scrollUsers);
        dtPanel.setBackground(Color.white);
        dtPanel.setBounds(10,10,455,220);
        add(dtPanel);
    }
    /**
     * Accion de Botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegresar){
            setVisible(false);
            new MenuServer();
        }
        if (e.getSource() == btnAgregar){
            setVisible(false);
            new AddUser();
        }
        if (e.getSource() == btnActualizar){
            loadServers();
        }
    }
}
