package P2P;

public class Status {
    private String IP;
    private int port;
    private String username;

    public Status(String IP, int port, String username) {
        this.IP = IP;
        this.port = port;
        this.username = username;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
