package P2P;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private String myIP;
    private int myPort;
    private ArrayList<Status> listOfUsers = new ArrayList<>();
    private ArrayList<Socket> socketsList = new ArrayList<>();
    private GUI_server gui;

    public void setGui(GUI_server gui) {
        this.gui = gui;
    }

    public void addUser(Status status) {
        listOfUsers.add(status);
    }

    public void clearListOfUsers() {
        this.listOfUsers.clear();
    }

    public ArrayList<Status> getListOfUsers() {
        return listOfUsers;
    }


    public Server(String myIP, int myPort)  {
        this.myIP = myIP;
        this.myPort = myPort;

        // new thread to start receiving TCP requests.
        new Thread(new Runnable() {
            public void run() {
                try {
                    startReceiving();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(gui.getMainPanel(), "Port Already in Use", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    public void startReceiving() throws IOException {
        String clientMessage;
//        String responseMessage = null;

            // creating a listening socket of type ServerSocket and specifying the port number it's gonna use
            ServerSocket welcomeSocket = new ServerSocket(myPort);

            while (true) {
                // accepting different client sockets
                Socket connectionSocket = welcomeSocket.accept();

                // handle the input and output stream of the socket to the inFromClient and outToClient objects
                BufferedReader inFromClient =
                        new BufferedReader(new
                                InputStreamReader(connectionSocket.getInputStream()));
                clientMessage = inFromClient.readLine();
                if(clientMessage.split(":")[0].equals("logOut")){
                    int socketPort = Integer.parseInt(clientMessage.split(":")[1]);
                    String socketIP = clientMessage.split(":")[2];

                    for(int i = 0; i < listOfUsers.size(); i++){
                        if((listOfUsers.get(i).getTcpSocketIp()).equals(socketIP) && +listOfUsers.get(i).getTcpSocketPort() == socketPort){
                            listOfUsers.remove(i);
                            socketsList.remove(i);
                        }
                    }

                }
                else {
                    Status status = new Gson().fromJson(clientMessage, Status.class);
                    addUser(status);
                    socketsList.add(connectionSocket);

                }
                gui.setActiveSet(listOfUsers);
                updateSocketList();


            }

    }

    public void updateSocketList() throws IOException {
        for(Socket s: socketsList){
            DataOutputStream outToClient = new DataOutputStream(s.getOutputStream());
            String responseMessage = new Gson().toJson(getListOfUsers())+ "\n";
            outToClient.writeBytes(responseMessage);
        }

    }








}
