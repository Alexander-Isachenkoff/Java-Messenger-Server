import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    public static final String ADDRESS = "127.0.0.1";
    public static final int PORT = 11111;
    private ServerSocket serverSocket;

    private Consumer<TextMessage> messageConsumer = m -> {
    };

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Готов!\n");

        while (true) {
            TextMessage message;
            try {
                message = getMessage();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                continue;
            }
            messageConsumer.accept(message);
        }
    }

    public void setOnMessageAccepted(Consumer<TextMessage> onMessageAccepted) {
        this.messageConsumer = onMessageAccepted;
    }

    private TextMessage getMessage() throws IOException, ClassNotFoundException {
        TextMessage textMessage;
        Socket incoming = serverSocket.accept();
        ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream());
        textMessage = (TextMessage) ois.readObject();
        ois.close();
        return textMessage;
    }

}