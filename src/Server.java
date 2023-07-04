import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    public static final int PORT = 11111;
    private final Unmarshaller unmarshaller;
    private ServerSocket serverSocket;

    private Consumer<TextMessage> onMessageAccepted = m -> {
    };

    public Server() {
        try {
            JAXBContext context = JAXBContext.newInstance(TextMessage.class);
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

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
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
                continue;
            }
            onMessageAccepted.accept(message);
        }
    }

    public void setOnMessageAccepted(Consumer<TextMessage> onMessageAccepted) {
        this.onMessageAccepted = onMessageAccepted;
    }

    private TextMessage getMessage() throws IOException, JAXBException {
        Socket incoming = serverSocket.accept();

        try (ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream())) {
            return (TextMessage) unmarshaller.unmarshal(ois);
        }
    }

}