package P2P;

import P2P.Search.SearchByUsername;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    //******************************************************************************************************************
    private ArrayList<Status> listOfUsers = new ArrayList<>();

    public void addUser(Status status) {
        listOfUsers.add(status);
    }

    public ArrayList<Status> getListOfUsers() {
        return listOfUsers;
    }

    public Status getUserByUsername(String username) {
        return new SearchByUsername(listOfUsers, username).search();
    }

    //******************************************************************************************************************
    private String myIP;
    private int myPort;

    public Server(String myIP, int myPort) {
        this.myIP = myIP;
        this.myPort = myPort;

        // new thread to start receiving TCP requests.
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

    //******************************************************************************************************************
    // TODO:1- here you have to connect the TCP PORT, IP with this method
    // TODO:2- replace BufferedReader, DataOutputStream with inputs from GUI
    public void startReceiving() {
        String clientMessage;
        String responseMessage = null;

        try {
            // creating a listening socket of type ServerSocket and specifying the port number it's gonna use
            ServerSocket welcomeSocket = new ServerSocket(myPort);

            while (true) {
                // accepting different client sockets
                Socket connectionSocket = welcomeSocket.accept();
                // handle the input and output stream of the socket to the inFromClient and outToClient objects
                BufferedReader inFromClient =
                        new BufferedReader(new
                                InputStreamReader(connectionSocket.getInputStream()));

                DataOutputStream outToClient =
                        new DataOutputStream(connectionSocket.getOutputStream());
                // reading the data
                clientMessage = inFromClient.readLine();
                // processing the data
//                capitalizedSentence = clientSentence.toUpperCase() + '\n';

                // it would be in json format, so we convert it to json object
                JsonObject fromJSON = new Gson().fromJson(clientMessage, JsonObject.class);
                String typeOfRequest = fromJSON.get("type_of_request").toString().replaceAll("\"", "");

                if (typeOfRequest.equals("AllUsers")) {
                    responseMessage = new Gson().toJson(getListOfUsers())+ "\n";

                } else if (typeOfRequest.equals("SingleUser")) {
                    String username = fromJSON.get("username").toString().replaceAll("\"", "");
                    responseMessage = new Gson().toJson(getUserByUsername(username))+ "\n";
                }

                // returning the processed data and directing
                outToClient.writeBytes(responseMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
