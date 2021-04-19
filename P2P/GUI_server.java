package P2P;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

public class GUI_server {
    private JButton startListeningButton;
    private JTextField port;
    private JList activeUsers;
    private JPanel mainPanel;
    private JLabel status;
    private Server server;
    private int portVar = -1;

    final private String ip = "localhost";

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setActiveSet(ArrayList<Status> ulist) {
        DefaultListModel model = new DefaultListModel();
        activeUsers.setModel(model);
        for (Status s : ulist){
            int pos = activeUsers.getModel().getSize();
            model.add(pos, s.getIP() + ":" + s.getPort() + ":(" + s.getUsername() + ")");
            activeUsers.setModel(model);
        }
    }

    public GUI_server() throws SocketException {
        DefaultListModel model = new DefaultListModel();
        GUI_server g = this;

        activeUsers.setModel(model);
        startListeningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(server!=null){
                    server.clearListOfUsers();
                    try {
                        server.updateSocketList();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
                server = new Server(ip, Integer.parseInt(port.getText()));
                server.setGui(g);
                System.gc();
                status.setText("Status: Listening on Port " + Integer.parseInt(port.getText()));
                setActiveSet();
            }
        });
    }

    void setActiveSet() {
        DefaultListModel model = new DefaultListModel();
        int pos = activeUsers.getModel().getSize();
        for (Status s : server.getListOfUsers())
            model.add(pos, s.getIP() + ":" + s.getPort());
        activeUsers.setModel(model);
    }


    private boolean validatePortField(JTextField portField) {
        try {
            int port = Integer.parseInt(portField.getText());
            if (port >= 65536) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(mainPanel, "Invalid Input format", "Error", JOptionPane.ERROR_MESSAGE);
            portField.requestFocusInWindow();
            return false;
        }
        return true;
    }


}
