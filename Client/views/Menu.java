/* Menu.java */
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
public class Menu extends JFrame implements ActionListener {
    private JTextField txtIpServer;
    private JLabel lblIpServer;
    private JButton btnContactos, btnCorreos, btnSendMail, btnExit;
    /**
     * Constructor Menu
     */
    public Menu(){
        setTitle("Menu");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));

        btnCorreos = new JButton("Correos");
        btnCorreos.setBounds(10,5,150,30);
        btnCorreos.addActionListener(this);
        add(btnCorreos);

        btnSendMail = new JButton("Enviar Correo");
        btnSendMail.setBounds(10,40,150,30);
        btnSendMail.addActionListener(this);
        add(btnSendMail);

        btnContactos = new JButton("Contactos");
        btnContactos.setBounds(10,75,150,30);
        btnContactos.addActionListener(this);
        add(btnContactos);
//        ImageIcon iconContact = new ImageIcon(Img.iconContacts);
//        btnContactos.setIcon(new ImageIcon(iconContact.getImage().getScaledInstance(btnContactos.getWidth(),btnContactos.getHeight(), Image.SCALE_SMOOTH)));

        btnExit = new JButton("Salir");
        btnExit.setBounds(10,110,150,30);
        btnExit.addActionListener(this);
        add(btnExit);



        setSize(244,189);
        setSize(245,190);
    }

    /**
     * Accion de botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        try {
            Server.mensajeError = "";
            if (e.getSource() == btnContactos){
                setVisible(false);
                new Contacts();
            }
            if (e.getSource() == btnCorreos){
                setVisible(false);
                new Mails();
            }
            if (e.getSource() == btnSendMail){
                setVisible(false);
                new SendMail();
            }
            if (e.getSource() == btnExit){
                if (ToServer.logOut()){
                    System.exit(0);
                }else {
                    JOptionPane.showMessageDialog(null,Server.mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            Server.mensajeError = "";
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
