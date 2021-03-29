package P2P;

import javax.swing.*;

public class Demo {
    public static void main(String[] args) {
        //client 1
        createGUI(6789);
        //client 2
        createGUI(9876);
    }

    private static void createGUI(int sourcePort) {
        GUI g = new GUI(sourcePort);
        JFrame frame2 = new JFrame("GUI");
        frame2.setContentPane(g.getHome());
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.pack();
        frame2.setVisible(true);
    }
}
