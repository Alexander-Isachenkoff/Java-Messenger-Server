package messager.client;

import java.io.IOException;
import java.net.Socket;

public abstract class Client implements Poster {

    public String address;
    public static final int PORT = 11112;
    private Socket socket;

    public Client(String address) {
        this.address = address;
    }

    protected Socket getSocket() {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(address, PORT);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return socket;
    }

}
