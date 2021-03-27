package P2P;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Participant {
    private int myPort;
    private DatagramSocket socket;

    Participant(int myPort) throws SocketException {

        this.myPort = myPort;
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

    public void sendData(int destinationPort, String msg) {
        try {
            // getting the IP of the server using DNS application protocol to get it
            InetAddress IPAddress = InetAddress.getByName("localhost");

            byte[] sendData;
            // reading data and store it in bytes form
            sendData = msg.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, destinationPort);
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
            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            String sentence = new String(receivePacket.getData());

            System.out.printf("FROM %s USING (UDP): %s", String.valueOf(receivePacket.getPort()), sentence);

        }

    }
}
