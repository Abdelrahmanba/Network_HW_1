package P2P;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) throws SocketException {

        Participant client1 = new Participant(9876);
        Participant client2 = new Participant(6789);

        client1.sendData(9876,"hello Abed!\n");
        client2.sendData(6789,"Hello Maysam!, How are you!\n");
    }

}
