package P2P;

import javax.swing.*;
import java.awt.*;
import java.net.SocketException;

public class Demo {
    public static void main(String[] args) throws SocketException {
        //server
        GUI_server server = new GUI_server();
        JFrame frame = new JFrame("Server TCP");
        frame.setContentPane(server.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600,400));
        frame.pack();
        frame.setVisible(true);


        //client 1
        createGUI_client(6789, 9876);
        //client 2
        createGUI_client(9876, 6789);
    }

    private static void createGUI_client(int sourcePort, int distPort) {
        GUI_client g = new GUI_client(sourcePort, distPort);
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(g.getHome());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
