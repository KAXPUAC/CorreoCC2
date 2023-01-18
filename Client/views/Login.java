/* Login.java */
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
/**
 * Clase Login
 */
public class Login extends JFrame implements ActionListener {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JLabel lblEmail, lblPassword;
    private JButton btnIniciar, btnCancelar;
    /**
     * Constructor de Login
     */
    public Login(){
        setTitle("Login");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());

        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));

        lblEmail = new JLabel("Correo");
        lblEmail.setForeground(Color.white);
        lblEmail.setBounds(10,5,150,20);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(75,5,150,20);
        add(txtEmail);


        lblPassword = new JLabel("Passord");
        lblPassword.setForeground(Color.white);
        lblPassword.setBounds(10,40,150,20);
        add(lblPassword);


        txtPassword = new JPasswordField();
        txtPassword.setBounds(75,40,150,20);
        add(txtPassword);


        btnIniciar = new JButton("Log In");
        btnIniciar.setBounds(10,80,100,30);
        btnIniciar.addActionListener(this);
        add(btnIniciar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(120,80,100,30);
        add(btnCancelar);
        btnCancelar.addActionListener(this);

        setSize(244,189);
        setSize(245,190);
    }

    /**
     * Aciones a tomar por los botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIniciar){
            logIn();
        }
        if (e.getSource() == btnCancelar){
            System.exit(0);
        }
    }

    /**
     * Fucion para Login
     */
    public void logIn(){
        Server.mensajeError = "";
        if(ToServer.loginCommandSend("LOGIN " + txtEmail.getText().toUpperCase() + " " + txtPassword.getText().toUpperCase())){
            setVisible(false);
            Perfil.email = txtEmail.getText().toUpperCase();
            new Menu();
        }else {
            JOptionPane.showMessageDialog(null,Server.mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
        }
        Server.mensajeError = "";
    }
}
