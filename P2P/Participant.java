package P2P;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Participant {
    //    private int myPort;
//    private String myIP;
    private DatagramSocket socket;
    private GUI_client parentGUI;
    private Status status;
    private boolean loggedIn = true;
    private int tcpSocketPort;
    private String tcpSocketIp;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setParentGUI(GUI_client parentGUI) {
        this.parentGUI = parentGUI;
    }

    public void setMyPort(int myPort) throws SocketException {
        if (this.status.getPort() == myPort) {
            return;
        }
        this.status.setPort(myPort);
        createSocket(myPort);
    }

    public void setMyIP(String myIP) {
        this.status.setIP(myIP);
    }

    Participant(String myIP, int myPort, String username) throws SocketException {
        this.status = new Status(myIP, myPort, username);
        createSocket(myPort);

    }

    private void createSocket(int myPort) throws SocketException {
        this.socket = new DatagramSocket(myPort);

        new Thread(new Runnable() {
            public void run() {
                try {
                    startReceiving();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendData(String distIP, int destinationPort, String msg) {
        try {
            InetAddress IPAddress = InetAddress.getByName(distIP);

            byte[] sendData;
            // reading data and store it in bytes form
            sendData = msg.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, destinationPort);
            // send data to the server socket through the sendPacket
            socket.send(sendPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReceiving() throws IOException {

        byte[] receiveData = new byte[1024];

        while (true) {
            // storing received data from client socket in receivePacket socket
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String sentence = new String(receivePacket.getData());
            parentGUI.getStatus().setText(String.format("FROM %s,%s, USING (UDP): %s\n",
                    String.valueOf(receivePacket.getAddress()), String.valueOf(receivePacket.getPort()), sentence));
//            sentence = myIP + ":" + myPort + " SAYS: " + sentence;
            sentence = this.status.getIP() + ":" + this.status.getPort() + " SAYS: " + sentence;
            parentGUI.print(sentence, Color.RED);
        }
    }

    public void getDataFromServer(String serverIP, int serverPort, String msg) throws ConnectException, UnknownHostException, IOException {
        String sentence;
        InetAddress IPAddress = InetAddress.getByName(serverIP);
        Socket clientSocket = new Socket(IPAddress, serverPort);
        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer =
                new BufferedReader(new
                        InputStreamReader(clientSocket.getInputStream()));
        if(msg == "allClients"){
            tcpSocketPort = clientSocket.getLocalPort();
            tcpSocketIp = String.valueOf(clientSocket.getInetAddress());
            status.setTcpSocketIp(tcpSocketIp);
            status.setTcpSocketPort(tcpSocketPort);
            sentence = new Gson().toJson(this.status);
            // this will send data to the server
            outToServer.writeBytes(sentence + '\n');
            // received data from server side
            new Thread(new Runnable() {
                public void run() {
                    while (loggedIn) {
                        String userList;
                        try {
                            userList = inFromServer.readLine();
                            if (userList != null) {
                                Type collectionType = new TypeToken<ArrayList<Status>>() {
                                }.getType();
                                ArrayList<Status> users = new Gson().fromJson(userList, collectionType);
                                parentGUI.setActiveSet(users);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }).start();
        }
        else if(msg == "logOut"){
            sentence = msg+":"+tcpSocketPort+":"+tcpSocketIp;
            outToServer.writeBytes(sentence + '\n');
            parentGUI.setActiveSet(new ArrayList<Status>());
            clientSocket.close();
        }



    }

}

