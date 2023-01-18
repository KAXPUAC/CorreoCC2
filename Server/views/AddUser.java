/* AddUser.java */
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
import java.util.ArrayList;
import java.util.List;
import db.*;
import java.util.regex.*;
/**
 * Clase AddServer
 */
public class AddUser extends JFrame implements ActionListener {
    /**
     * Variables
     */
    private JButton btnGuardar, btnCancelar;
    private JLabel lblNombre, lblApellido, lblCorreo, lblPass;
    private JTextField txtNombre, txtApellido, txtCorreo;
    private String mensajeError = "";
    private JPasswordField txtPass;
    /**
     *Constructor de Clase
     */
    public AddUser(){
        setTitle("Nuevo Usuario");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));


        /**
         * Nombre
         */
        lblNombre = new JLabel("Nombre");
        lblNombre.setForeground(Color.white);
        lblNombre.setBounds(10,5,150,20);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(75,5,190,20);
        add(txtNombre);
        /**
         * Apellido
         */
        lblApellido = new JLabel("Apellido");
        lblApellido.setBounds(10,40,150,20);
        lblApellido.setForeground(Color.white);
        add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setBounds(75,40,190,20);
        add(txtApellido);
        /**
         * Correo
         */
        lblCorreo = new JLabel("Correo");
        lblCorreo.setForeground(Color.white);
        lblCorreo.setBounds(10,75,150,20);
        add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(75,75,190,20);
        add(txtCorreo);
        /**
         * Pass
         */
        lblPass = new JLabel("Clave");
        lblPass.setForeground(Color.white);
        lblPass.setBounds(10,110,150,20);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(75,110,190,20);
        add(txtPass);

        /**
         * Boton Enviar
         */
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(10,260,150,30);
        btnGuardar.addActionListener(this);
        add(btnGuardar);

        /**
         * Boton Cancelar
         */
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(165,260,100,30);
        btnCancelar.addActionListener(this);
        add(btnCancelar);



        setSize(299,349);
        setSize(300,350);
    }
    /**
     * Accion de botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGuardar){
            if (addUsuario()){
                JOptionPane.showMessageDialog (null, "Usuario agregado", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new Users();
            }
        }
        if (e.getSource() == btnCancelar){
            setVisible(false);
            new MenuServer();
        }
    }
    /**
     * Agrega un nuevo usuario a la DB
     * @return
     */
    public boolean addUsuario(){
        boolean agreado = false;
        try {
            if (validaDatos()){
                agreado = CRUDMail.createNewUser(txtCorreo.getText(),txtNombre.getText(), txtApellido.getText(), txtPass.getText());
                if (!agreado){
                    JOptionPane.showMessageDialog(null,"Usuario no agregado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(null,mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
            }
            return agreado;
        }catch (Exception ex){
            return true;
        }
    }
    /**
     * Valida los datos
     * @return
     */
    public boolean validaDatos(){
        boolean validos = false;
        mensajeError = "";
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+Server.nameServer;
        if (!txtNombre.getText().isEmpty()){
            if (!txtApellido.getText().isEmpty()){
                if (!txtCorreo.getText().isEmpty()){
                    if (!txtPass.getText().isEmpty()){
                        if (Pattern.matches(regex,txtCorreo.getText().toUpperCase())){
                            validos = true;
                        }else {
                            mensajeError = "El correo ingresado no es valido";
                        }
                    }else {
                        mensajeError = "El campo Pass es requerido.";
                    }
                }else {
                    mensajeError = "El campo Correo es requerido.";
                }
            }
            else {
                mensajeError = "El campo Apellido es requerido";
            }
        }else {
            mensajeError = "El campo Nombre es requerido";
        }
        return validos;
    }
}
