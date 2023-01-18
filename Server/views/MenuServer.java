/* MenuServer.java */
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
 * Clase Init
 */
public class MenuServer extends JFrame implements ActionListener {
    private JButton btnUsuarios, btnServidores, btnSalir;

    /**
     * Constructor Clase Init
     */
    public MenuServer(){
        setTitle("Menu");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(Img.backgroundImage)));


        btnUsuarios = new JButton("Usuarios");
        btnUsuarios.setBounds(10,5,100,30);
        btnUsuarios.addActionListener(this);
        add(btnUsuarios);

        btnServidores = new JButton("Servidores");
        btnServidores.setBounds(10,40,100,30);
        add(btnServidores);
        btnServidores.addActionListener(this);

        btnSalir = new JButton("Salir");
        btnSalir.setBounds(10,75,100,30);
        btnSalir.addActionListener(this);
        add(btnSalir);


        setSize(244,189);
        setSize(245,190);
    }

    /**
     * Accion de los Botones
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnUsuarios){
            setVisible(false);
            new Users();
        }
        if (e.getSource() == btnServidores){
            setVisible(false);
            new Servers();
        }
        if (e.getSource() == btnSalir){
            if (ToDNS.ofLine()){
                System.exit(0);
            }
        }
    }
}
