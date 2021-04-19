package P2P;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class GUI_client {
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
    private JTextField userName;
    private JButton logInButton;
    private JButton logOutButton;
    private JTextField serverIp;
    private JTextField serverPort;
    private JList activeUsers;
    private JPanel onlineList;
    String selectedUser;
    private  ArrayList<Status> activeUsersList =new ArrayList<>();

    //vars
    int sourcePortVar;
    String sourceIPVar = null;
    int distPortVar;
    Participant client;
    Status s;

    //constants
    private static final String IPV4_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    public GUI_client(int sPort, int dPort)  {
        //init source port
        try {
            sourceIPVar = String.valueOf(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        sourcePortVar = sPort;
        distPortVar = dPort;
        sourceIP.setText(sourceIPVar);
        sourcePort.setText(String.valueOf(sourcePortVar));
        activeUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sendButton.setEnabled(false);

        //init default client
        try {
            client = new Participant(sourceIPVar, sourcePortVar, "User");
            client.setParentGUI(this);

        } catch (SocketException e) {
            JOptionPane.showMessageDialog(home, "Port Taken,Try another one", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.sendData(distIP.getText(), Integer.parseInt(distPort.getText()), msg.getText());
                    String sentence = "ME:" + msg.getText();
                    print(sentence, Color.BLUE);
                } catch (NumberFormatException ec) {
                    JOptionPane.showMessageDialog(home, "Empty Fields", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }
        });
        serverIp.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!validateIPField(serverIp))
                    return;
            }
        });
        serverPort.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (!validatePortField(serverPort))
                    return;

            }
        });
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    if (!userName.getText().isEmpty())
                        s = new Status(sourceIPVar, sourcePortVar, userName.getText());
                    else
                        s = new Status(sourceIPVar, sourcePortVar,"User");

                    client.setStatus(s);
                    client.getDataFromServer(serverIp.getText(), Integer.parseInt(serverPort.getText()), "allClients");
                    serverIp.setEnabled(false);
                    serverPort.setEnabled(false);
                    logInButton.setEnabled(false);
                    userName.setEnabled(false);
                    sendButton.setEnabled(true);
                    logOutButton.setEnabled(true);

                } catch (ConnectException connectException) {
                    JOptionPane.showMessageDialog(home, "Server Offline", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException connectException) {
                    JOptionPane.showMessageDialog(home, "Empty IP", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException connectException) {
                    JOptionPane.showMessageDialog(home, "Empty Port No", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverIp.setEnabled(true);
                serverPort.setEnabled(true);
                logInButton.setEnabled(true);
                sendButton.setEnabled(false);
                logOutButton.setEnabled(false);
                userName.setEnabled(true);
                try {
                    client.getDataFromServer(serverIp.getText(), Integer.parseInt(serverPort.getText()), "logOut");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            }
        });
        activeUsers.addListSelectionListener(e -> {
            try {
                selectedUser = (String) activeUsers.getSelectedValue();
                String[] data = selectedUser.split(":");
                distIP.setText(data[0]);
                distPort.setText(data[1]);
            } catch (Exception exception) {
                distIP.setText("");
                distPort.setText("");
            }
        });
    }



    private void createUIComponents() {
        //Available interfaces init
        try {
            availableInterfaces = new JComboBox();
            String availableHostsItem =  InetAddress.getLocalHost().toString();
            availableInterfaces.addItem(availableHostsItem);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void setActiveSet(ArrayList<Status> ulist) {
        ArrayList<Status> logout = activeUsersList;
        for(Status s:ulist ){
            for(int i = 0; i <logout.size();i++){
                if(logout.get(i).equals(s)){
                    logout.remove(i);
                }
            }
        }
        for(Status s: logout){
            print(s + " Logged Out",Color.darkGray);
        }

        try {
            DefaultListModel model = new DefaultListModel();
            activeUsers.setModel(model);
//            model.get
            for (Status s : ulist) {
                int pos = activeUsers.getModel().getSize();
                model.add(pos, s);
                activeUsers.setModel(model);
            }
            activeUsersList = ulist;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void print(String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        StyledDocument doc = chat.getStyledDocument();

        try {
            doc.insertString(doc.getLength(), msg + "\n", aset);
        } catch (Exception e) {
            e.printStackTrace();
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
        if (!ipField.getText().matches(IPV4_PATTERN)&&!ipField.getText().equals("localhost")) {
            JOptionPane.showMessageDialog(home, "Invalid Input format use IPv4 format", "Error", JOptionPane.ERROR_MESSAGE);
            ipField.requestFocusInWindow();
            return false;
        }
        return true;
    }

    public JPanel getHome() {
        return home;
    }

    public JTextField getStatus() {
        return status;
    }
}
