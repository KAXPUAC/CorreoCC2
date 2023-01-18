/* AddContact.java */
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
/**
 * Clase Login
 */
public class AddContact extends JFrame implements ActionListener {
    private JTextField txtContact;
    private JLabel lblContact;
    private JButton btnAgregar, btnCancelar;

    /**
     * Constructor AddContact
     */
    public AddContact(){
        setTitle("Nuevo Contacto");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));

        /**
         * Contacto
         */
        lblContact = new JLabel("Nuevo Contacto");
        lblContact.setForeground(Color.white);
        lblContact.setBounds(10,5,150,20);
        add(lblContact);

        txtContact = new JTextField();
        txtContact.setBounds(10,40,150,20);
        add(txtContact);
        /**
         * Botones
         */
        btnAgregar = new JButton("Agregar Contacto");
        btnAgregar.setBounds(10,80,100,30);
        btnAgregar.addActionListener(this);
        add(btnAgregar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(120,80,100,30);
        add(btnCancelar);
        btnCancelar.addActionListener(this);

        setSize(244,189);
        setSize(245,190);
    }

    /**
     * Accion de botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAgregar){
            if (addContact()){
                setVisible(false);
                new Contacts();
            }
        }
        if (e.getSource() == btnCancelar){
            setVisible(false);
            new Menu();
        }
    }

    /**
     * Envia el correo al servidor
     * @return
     */
    private boolean addContact(){
        String contact = txtContact.getText();
        if (!contact.trim().isEmpty() && !contact.trim().equals("")){
            if (contact.trim().contains("@")){
                try {
                    Server.mensajeError = "";
                    boolean con = ToServer.createContact(contact);
                    if (!con){
                        JOptionPane.showMessageDialog(null,Server.mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
                    }else {
                        JOptionPane.showMessageDialog (null, "Contacto creado", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                    }
                    Server.mensajeError = "";
                    return con;
                } catch (java.lang.Exception exception) {
                    exception.printStackTrace();
                }
                //return ToServer.sendMail(para,asunto, mensaje);
                return true;
            }else {
                JOptionPane.showMessageDialog(null,"Usuario no valido", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }else{
            JOptionPane.showMessageDialog(null,"Debe ingresar un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
