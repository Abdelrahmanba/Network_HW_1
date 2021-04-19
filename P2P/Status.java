package P2P;

import java.util.Objects;

public class Status {
    private String IP;
    private int port;
    private String username;
    private int tcpSocketPort;
    private String tcpSocketIp;



    public Status(String IP, int port, String username) {
        this.IP = IP;
        this.port = port;
        this.username = username;

    }

    public String getTcpSocketIp() {
        return tcpSocketIp;
    }

    public void setTcpSocketIp(String tcpSocketIp) {
        this.tcpSocketIp = tcpSocketIp;
    }

    public int getTcpSocketPort() {
        return tcpSocketPort;
    }

    public void setTcpSocketPort(int tcpSocketPort) {
        this.tcpSocketPort = tcpSocketPort;
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

    @Override
    public String toString() {
        return IP + ":" + port + ":(" + username+ ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return port == status.port &&
                Objects.equals(IP, status.IP) &&
                Objects.equals(username, status.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IP, port, username, tcpSocketPort, tcpSocketIp);
    }
}

