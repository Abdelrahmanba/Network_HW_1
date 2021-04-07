package P2P;

import javax.swing.*;

public class Demo {
    public static void main(String[] args) {
        //client 1
        createGUI(6789, 9876);
        //client 2
        createGUI(9876, 6789);
    }

    private static void createGUI(int sourcePort, int distPort) {
        GUI g = new GUI(sourcePort, distPort);
        JFrame frame2 = new JFrame("GUI");
        frame2.setContentPane(g.getHome());
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.pack();
        frame2.setVisible(true);
    }
}
