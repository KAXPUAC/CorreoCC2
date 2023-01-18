/* AddServer.java */
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
//Pattern
//Matcher
/**
 * Clase AddServer
 */
public class AddServer extends JFrame implements ActionListener {
    private JButton btnGuardar, btnCancelar;
    private JLabel lblIp, lblNombre;
    private JTextField txtIp, txtNombre;
    private String mensajeError = "";
    /**
     *Constructor de Clase
     */
    public AddServer(){
        setTitle("Nuevo Servidor");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));


        /**
         * Nombre servidor
         */
        lblNombre = new JLabel("Nombre");
        lblNombre.setForeground(Color.white);
        lblNombre.setBounds(10,5,150,20);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(75,5,150,20);
        add(txtNombre);
        /**
         * Ip Servidor
         */
        lblIp = new JLabel("Ip Servidor");
        lblIp.setBounds(10,40,150,20);
        lblIp.setForeground(Color.white);
        add(lblIp);

        txtIp = new JTextField();
        txtIp.setBounds(75,40,150,20);
        add(txtIp);
        /**
         * Boton Guardar
         */
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(10,80,100,30);
        btnGuardar.addActionListener(this);
        add(btnGuardar);
        /**
         * Boton Cancelar
         */
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(120,80,100,30);
        btnCancelar.addActionListener(this);
        add(btnCancelar);

        setSize(244,189);
        setSize(245,190);
    }
    /**
     * Accion de botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGuardar){
            if (addServ()){
                JOptionPane.showMessageDialog (null, "Servidor agregado", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
                new Servers();
            }
        }
        if (e.getSource() == btnCancelar){
            setVisible(false);
            new Servers();
        }
    }

    /**
     * Agrega un nuevo servidor a la DB
     * @return
     */
    public boolean addServ(){
        boolean agreado = false;
        try {
            if (validaDatos()){
                agreado = CRUDMail.createServer(txtNombre.getText(),txtIp.getText());
                if (!agreado){
                    JOptionPane.showMessageDialog(null,"Servidor no agregado", "Error", JOptionPane.ERROR_MESSAGE);
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
        String regex = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        if (!txtIp.getText().isEmpty()){
            if (!txtNombre.getText().isEmpty()){
                if (Pattern.matches(regex,txtIp.getText())){
                    validos = true;
                }else {
                    mensajeError = "La ip ingresada no es valida";
                }
            }
            else {
                mensajeError = "El nombre del servidor no puede ir vacio";
            }
        }else {
            mensajeError = "El campo IP es requerido";
        }
        return validos;
    }
}
