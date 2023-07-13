package messager.client;

import java.io.IOException;
import java.net.Socket;

public abstract class Client {

    public static final int PORT = 11112;
    private Socket socket;

    protected Socket getSocket(String address) {
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
