/* SendMail.java */
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
public class SendMail extends JFrame implements ActionListener {
    private JButton btnEnviarCorreo, btnCancelar;
    private JLabel lblCorreo, lblAsunto, lblBody;
    private JTextField txtCorreo, txtAsunto;
    private JTextArea txtBody;

    /**
     * Constructor Menu
     */
    public SendMail(){
        setTitle("Enviar Correo");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));


        /**
         * Para enviar
         */
        lblCorreo = new JLabel("Para");
        lblCorreo.setForeground(Color.white);
        lblCorreo.setBounds(10,5,150,20);
        add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(75,5,190,20);
        add(txtCorreo);
        /**
         * Asunto
         */
        lblAsunto = new JLabel("Asunto");
        lblAsunto.setBounds(10,40,150,20);
        lblAsunto.setForeground(Color.white);
        add(lblAsunto);

        txtAsunto = new JTextField();
        txtAsunto.setBounds(75,40,190,20);
        add(txtAsunto);
        /**
         * Mensaje
         */
        lblBody = new JLabel("Mensaje");
        lblBody.setForeground(Color.white);
        lblBody.setBounds(10,75,150,20);
        add(lblBody);

        txtBody = new JTextArea();
        txtBody.setBounds(75,75,190,150);
        txtBody.setLineWrap(true);
        txtBody.setWrapStyleWord(true);
        add(txtBody);

        /**
         * Boton Enviar
         */
        btnEnviarCorreo = new JButton("Enviar Correo");
        btnEnviarCorreo.setBounds(10,260,150,30);
        btnEnviarCorreo.addActionListener(this);
        add(btnEnviarCorreo);

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
        if (e.getSource() == btnCancelar){
            new Menu();
            setVisible(false);
        }
        if (e.getSource() == btnEnviarCorreo){
            if (enviarCorreo()){
                setVisible(false);
                new Menu();
            }
        }
    }

    /**
     * Envia el correo al servidor
     * @return
     */
    private boolean enviarCorreo(){
        String para = txtCorreo.getText();
        String asunto = txtAsunto.getText();
        String mensaje = txtBody.getText();
        if (!para.trim().isEmpty() && !para.trim().equals("")){
            if (!asunto.trim().isEmpty() && !asunto.trim().equals("")){
                if (!mensaje.trim().isEmpty()  && !mensaje.trim().equals("")){
                    Server.mensajeError = "";
                    if (ToServer.sendMail(para,asunto, mensaje)){
                        JOptionPane.showMessageDialog (null, "Correo enviado", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                        Server.mensajeError = "";
                        return true;
                    }else {
                        JOptionPane.showMessageDialog(null,Server.mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
                        Server.mensajeError = "";
                        return false;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"Debe escribir un mensaje para enviar", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }else {
                JOptionPane.showMessageDialog(null,"Debe escribir un asunto para enviar", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }else{
            JOptionPane.showMessageDialog(null,"Debe escribir un destinatario", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
