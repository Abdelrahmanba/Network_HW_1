package P2P;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class GUI extends JFrame{
    private JTextArea textArea1;
    private JTextField textField1;
    private JButton logInButton;
    private JButton logOutButton;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox1;
    private JTextField textField4;
    private JButton sendButton;
    private JButton testButtonButton;
    private JTextField textField5;
    private JPanel mainPanel;
    private JTextArea textArea2;
    private JTextArea textArea3;

    GUI(){
        super("Maysam M. Mousa 11819202");
        this.setContentPane(mainPanel);

        textArea3.setText("Type your text here");
        textArea3.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                textArea3.setText("");
            }
            public void focusLost(FocusEvent e) {
                textArea3.setText("Type your text here");
            }
        });

    }

    public static void main(String[] args) {
        GUI obj = new GUI();
        obj.setVisible(true);
        obj.setDefaultCloseOperation(EXIT_ON_CLOSE);
        obj.setSize(900, 500);
    }
}
