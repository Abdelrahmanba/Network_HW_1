package P2P;

import java.net.SocketException;

public class Demo2 {
    public static void main(String[] args) throws SocketException, InterruptedException {
        // creating the server that will run on 6789 port
        Server server = new Server("localhost",6789);

        // every participant logs in his status (ip, port, and username) will be saved
        Participant client1 = new Participant("localhost",8000,"maysam");
        Participant client2 = new Participant("localhost",8001,"abed");
        Participant client3 = new Participant("localhost",8002,"mohammad");

        server.addUser(client1.getStatus());
        server.addUser(client2.getStatus());
        server.addUser(client3.getStatus());

        // client 1 requests list of users using getDataFromServer method which is implemented in TCP
        // this request mostly when the GUI is initialized
        String msg1FromC1 = "{'type_of_request':'AllUsers'}";
        client1.getDataFromServer(msg1FromC1); // you can pass the textarea which is specified for the list of users set the text

        // client 2 requests a single user using getDataFromServer method which is implemented in TCP
        // this request mostly when a user is double clicked in the onlineusers list and its ip,port is put to the remote ip/port in the GUI
        String msg2FromC1 = "{'type_of_request':'SingleUser', 'username':'maysam'}";
        client2.getDataFromServer(msg2FromC1); // pass JTextArea as a param to getDataFromServer method to set the resulting text


    }
}
