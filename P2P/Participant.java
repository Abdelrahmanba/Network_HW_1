package P2P;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Participant {
    //    private int myPort;
//    private String myIP;
    private DatagramSocket socket;
    private GUI parentGUI;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setParentGUI(GUI parentGUI) {
        this.parentGUI = parentGUI;
    }

    public void setMyPort(int myPort) throws SocketException {
        if (this.status.getPort() == myPort) {
            return;
        }
//        this.myPort = myPort;
        this.status.setPort(myPort);
        createSocket(myPort);
    }

    public void setMyIP(String myIP) {
//        this.myIP = myIP;
        this.status.setIP(myIP);
    }

    Participant(String myIP, int myPort, String username) throws SocketException {

//        this.myPort = myPort;
//        this.myIP = myIP;
        status = new Status(myIP, myPort, username);

        createSocket(myPort);

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

    private void createSocket(int myPort) throws SocketException {
        this.socket = new DatagramSocket(myPort);
    }

    public void sendData(int destinationPort, String msg) {
        try {
            // getting the IP
//            InetAddress IPAddress = InetAddress.getByName(myIP);
            InetAddress IPAddress = InetAddress.getByName(this.status.getIP());

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

    //******************************************************************************************************************
    public void getDataFromServer(String msg) {
        String sentence;
        String modifiedSentence;
        // TODO: 4- replace the input stream with this function parameter 'msg'
        try {
            // the data is gonna be read through inFormUser object
            // creating a TCP socket of type Socket and specifying the IP and port number of the server
            Socket clientSocket = new Socket("localhost", 6789);
            // controlling the input and output stream of the TCP socket
            DataOutputStream outToServer =
                    new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
            // reading data
            sentence = msg;
            // this will send data to the server
            outToServer.writeBytes(sentence + '\n');
            // received data from server side
            modifiedSentence = inFromServer.readLine();
            // print the result
            System.out.println("FROM SERVER USING (TCP): " + modifiedSentence);
            // closing the socket

            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
