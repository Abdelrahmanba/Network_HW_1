package P2P;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class GUI {
    //GUI
    private JTextPane chat;
    private JTextArea msg;
    private JTextField status;
    private JPanel home;
    private JComboBox availableInterfaces;
    private JTextField sourceIP;
    private JTextField sourcePort;
    private JTextField distIP;
    private JTextField distPort;
    private JButton sendButton;

    //vars
    int sourcePortVar;
    String sourceIPVar = null;
    int distPortVar;
    Participant client;

    //constants
    private static final String IPV4_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    public GUI(int sPort,int dPort) {
        //init source port
        sourcePortVar = sPort;
        distPortVar = dPort;

        //** listeners init **
        //update source ip
        sourceIP.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!validateIPField(sourceIP))
                    return;
                sourceIPVar = sourceIP.getText();
                client.setMyIP(sourceIPVar);
            }
        });
        //update dist ip
        distIP.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!validateIPField(distIP))
                    return;
            }
        });
        //update source port
        sourcePort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!validatePortField(sourcePort))
                    return;
                sourcePortVar = Integer.parseInt(sourcePort.getText());

                try {
                    client.setMyPort(sourcePortVar);
                } catch (SocketException socketException) {
                    JOptionPane.showMessageDialog(home, "Port Taken,Try another one", "Error", JOptionPane.ERROR_MESSAGE);
                    sourcePort.requestFocusInWindow();
                    return;
                }

            }
        });
        //update dist port
        distPort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!validatePortField(distPort))
                    return;

            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendData(Integer.parseInt(distPort.getText()), msg.getText());
                String sentence = "ME:" + msg.getText();
                print(sentence,Color.BLUE);
            }
        });
    }

    public JPanel getHome() {
        return home;
    }

    public JTextField getStatus() { return status; }

    private void createUIComponents() {
        //Available interfaces init
        InetAddress localhost = null;
        try {
            availableInterfaces = new JComboBox();
            localhost = InetAddress.getLocalHost();
            sourceIPVar = localhost.getHostAddress();
            String availableHostsItem = localhost.getHostName() + ": " + localhost.getHostAddress();
            availableInterfaces.addItem(availableHostsItem);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //init source ip/port default value
        sourceIP = new JTextField(sourceIPVar);
        sourcePort = new JTextField(String.valueOf(sourcePortVar));
        distIP = new JTextField(sourceIPVar);
        distPort = new JTextField(String.valueOf(distPortVar));
        //init default client
        try {
            client = new Participant(sourcePortVar, sourceIPVar);
            client.setParentGUI(this);
        } catch (SocketException e) {
            JOptionPane.showMessageDialog(home, "Port Taken,Try another one", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void print(String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        StyledDocument doc = chat.getStyledDocument();

        try
        {
            doc.insertString(doc.getLength(), msg + "\n",aset);
        }
        catch(Exception e) { System.out.println(e);
        }
    }
    private boolean validatePortField(JTextField portField) {
        try {
            Integer.parseInt(portField.getText());
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(home, "Invalid Input format", "Error", JOptionPane.ERROR_MESSAGE);
            portField.requestFocusInWindow();
            return false;
        }
        return true;
    }

    private boolean validateIPField(JTextField ipField) {
        if (!ipField.getText().matches(IPV4_PATTERN)) {
            JOptionPane.showMessageDialog(home, "Invalid Input format use IPv4 format", "Error", JOptionPane.ERROR_MESSAGE);
            ipField.requestFocusInWindow();
            return false;
        }
        return true;
    }
}
