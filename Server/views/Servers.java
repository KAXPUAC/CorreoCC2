/* Servers.java */
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

/**
 * Clase Servers
 */
public class Servers extends JFrame implements ActionListener {
    private JButton btnRegresar, btnActualizar, btnAgregar;
    private JTable dtServers;
    private JScrollPane scrollServer;
    private JPanel dtPanel;
    ArrayList<String> serversdt;

    /**
     * Constructor de clase Servers
     */
    public Servers(){
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

        btnAgregar = new  JButton("Agregar Servidor");
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
        serversdt = CRUDMail.serverDB();
        createTable();
    }

    /**
     * Crea la tabla
     */
    public void createTable(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Servidor");
        modelo.addColumn("Ip");

        for (int i = 0; i < serversdt.size(); i++) {
            String[] serv = serversdt.get(i).split("'");
            modelo.addRow(new Object[]{serv[0], serv[1]});
        }

        dtServers = new JTable();
        dtServers.setModel(modelo);
        scrollServer = new JScrollPane(dtServers);

        dtPanel = new JPanel(new BorderLayout());
        dtPanel.add(scrollServer);
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
            new AddServer();
        }
        if (e.getSource() == btnActualizar){
            loadServers();
        }
    }
}
