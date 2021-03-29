package P2P;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Participant {
    private int myPort;
    private String myIP;

    public void setParentGUI(GUI parentGUI) {
        this.parentGUI = parentGUI;
    }

    private GUI parentGUI;


    public void setMyPort(int myPort) throws SocketException {
        if(this.myPort==myPort){
            return;
        }
        this.myPort = myPort;
        createSocket(myPort);

    }

    public void setMyIP(String myIP)  {
        this.myIP = myIP;
    }


    private DatagramSocket socket;

    Participant(int myPort,String myIP) throws SocketException {

        this.myPort = myPort;
        this.myIP = myIP;


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
            InetAddress IPAddress = InetAddress.getByName(myIP);

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
            sentence = myIP + ":" + myPort + " SAYS: " + sentence;
            parentGUI.print(sentence, Color.RED);

//            System.out.printf("FROM %s,%s, USING (UDP): %s\n",  String.valueOf(receivePacket.getAddress() ),String.valueOf(receivePacket.getPort()), sentence);

        }

    }
}
