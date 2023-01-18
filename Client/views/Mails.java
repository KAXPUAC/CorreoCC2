/* Mails.java */
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
public class Mails extends JFrame implements ActionListener {
    private JTable dtMails;
    private JScrollPane scrollMails;
    private JPanel dtPanel;
    private JButton btnRegresar, btnActualizar;
    /**
     * Constructor Menu
     */
    public Mails(){
        setTitle("Correos");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));

        loadMails();

        btnRegresar = new JButton("Menu");
        btnRegresar.setBounds(465,5,130,30);
        btnRegresar.addActionListener(this);
        add(btnRegresar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(465,40,130,30);
        btnActualizar.addActionListener(this);
        add(btnActualizar);

        setSize(599,349);
        setSize(600,350);
    }

    /**
     * Inicializa los Mails
     */
    public void loadMails(){
        try {
            Server.mensajeError = "";
            if(ToServer.listMails()){
                createTable();
            }else {
                JOptionPane.showMessageDialog(null,Server.mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
                createTable();
            }
            Server.mensajeError = "";
        }catch (Exception ex){
            System.out.println("Error");
            JOptionPane.showMessageDialog(null,ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Llena tabla ce Mails
     */
    private void createTable(){
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Contacto");
            modelo.addColumn("Asunto");
            modelo.addColumn("Mensaje");

            for (int i = 0; i < Perfil.listMails.size(); i++) {
                String[] mailInbox = Perfil.listMails.get(i).split("concatCC2");
                modelo.addRow(new Object[]{ mailInbox[0], mailInbox[1] , mailInbox[2] });
            }
            dtMails = new JTable();
            dtMails.setModel(modelo);
            scrollMails = new JScrollPane(dtMails);

            dtPanel = new JPanel(new BorderLayout());
            dtPanel.add(scrollMails);
            dtPanel.setBackground(Color.white);
            dtPanel.setBounds(10,10,455,220);
            add(dtPanel);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

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
        if (e.getSource() == btnActualizar){
            loadMails();
        }
    }
}
