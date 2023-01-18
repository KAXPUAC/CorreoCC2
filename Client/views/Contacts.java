/* Contacts.java */
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
import connection.*;
import models.*;
import javax.swing.table.DefaultTableModel;
/**
 * Clase Login
 */
public class Contacts extends JFrame implements ActionListener {

    private JTable dtContacts;
    private JScrollPane scrollContacts;
    private JPanel dtPanel;
    private JButton btnRegresar, btnAddContact;

/**
 * Constructor Menu
 */
    public Contacts(){
        setTitle("Contactos");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));

        loadContacts();

        btnRegresar = new JButton("Menu");
        btnRegresar.setBounds(465,5,130,30);
        btnRegresar.addActionListener(this);
        add(btnRegresar);

        btnAddContact = new JButton("Nuevo Contacto");
        btnAddContact.setBounds(465,40,130,30);
        btnAddContact.addActionListener(this);
        add(btnAddContact);

        //setSize(acho,altura);
        setSize(599,349);
        setSize(600,350);
    }

    /**
     * Carga los Contactos
     */
    public void loadContacts(){
        try {
            Server.mensajeError = "";
            if(ToServer.listContacts()){
                createTable();
            }else {
                JOptionPane.showMessageDialog(null,Server.mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
                createTable();
            }
            Server.mensajeError = "";
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea la tabla de Contactos
     */
    public void createTable(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Contacto");
        modelo.addColumn("Servidor");

        for (int i = 0; i < Perfil.listContact.size(); i++) {
            String[] contacto = Perfil.listContact.get(i).split("@");
            modelo.addRow(new Object[]{contacto[0], contacto[1]});
        }
        dtContacts = new JTable();
        dtContacts.setModel(modelo);

        scrollContacts = new JScrollPane(dtContacts);

        dtPanel = new JPanel(new BorderLayout());
        dtPanel.add(scrollContacts);
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
            new Menu();
        }
        if (e.getSource() == btnAddContact){
            setVisible(false);
            new AddContact();
        }
    }
}
