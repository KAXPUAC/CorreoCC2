/* LoadClient.java */
/**
 ** Hecho por: Kevin Axpuac
 ** Carnet: 15006597
 ** Seccion: AN
 **/
import java.io.IOException;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import views.*;
import paths.*;
import connection.*;
import java.util.regex.*;
/**
 * Clase principal que hara uso el cliente
 */

public class LoadClient extends JFrame implements ActionListener {
    private JTextField txtIpServer;
    private JLabel lblIpServer;
    private JButton btnBuscarServer, btnCancelar;
    private String regexIp = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
    /**
     * Constructor de LoadClient
     */
    public LoadClient(){
        /* Titulo de Ventana */
        setTitle("Load Client");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());

        String pathImgBackground = (new File (".").getAbsolutePath());
        pathImgBackground = pathImgBackground.substring(0,pathImgBackground.length() - 1);
        pathImgBackground = pathImgBackground + "img/background.jpg";
        Img.backgroundImage = pathImgBackground;

        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));

        String fullPath = (new File (".").getAbsolutePath());
        fullPath = fullPath.substring(0,fullPath.length() - 1);

        Img.iconContacts = fullPath + "img/contacts.png";
        Img.iconSendMail = fullPath + "img/sendMail.png";
        Img.iconInbox = fullPath + "img/inboxMail.png";

        /**
         * Ip
         */
        lblIpServer = new JLabel("IP Server");
        lblIpServer.setForeground(Color.white);
        lblIpServer.setBounds(10,5,150,20);
        add(lblIpServer);

        txtIpServer = new JTextField();
        txtIpServer.setBounds(75,5,150,20);
        add(txtIpServer);
        /**
         * End Ip
         */
        btnBuscarServer = new JButton("Conectar");
        btnBuscarServer.setBounds(10,80,100,30);
        btnBuscarServer.addActionListener(this);
        add(btnBuscarServer);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(120,80,100,30);
        add(btnCancelar);
        btnCancelar.addActionListener(this);

        setSize(244,189);
        setSize(245,190);
    }

    /**
     * Acciones de botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBuscarServer){
            if (txtIpServer.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Por favor ingrese un valor.", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                try {
                    if (!txtIpServer.getText().isEmpty()){
                        if (Pattern.matches(regexIp,txtIpServer.getText())){
                            System.out.println("Iniciando Cliente");
                            /* Asigna ip de Servidor */
                            Server.ipServe = txtIpServer.getText();
                            new ToServer();
                            System.out.println("Cliente iniciado");
                            new Login();
                            setVisible(false);
                        }else {
                            JOptionPane.showMessageDialog(null, "La ip ingresada no es valida.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Ingrese una ip para iniciar.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }catch (IOException ex){
                    System.out.println("No se pudo crear la conexion.");
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al conectar al servidor.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == btnCancelar){
            System.exit(0);
        }
    }

    /**
     * Inicializador
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        new LoadClient();
    }
}
