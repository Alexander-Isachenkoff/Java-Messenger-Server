package messager.server;

import lombok.SneakyThrows;
import messager.requests.Request;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.BiConsumer;

public class Server {

    public static final int PORT = 11111;
    private final Unmarshaller unmarshaller;
    private ServerSocket serverSocket;

    private BiConsumer<Request, String> onAccepted = (o, s) -> {};

    public Server() {
        try {
            JAXBContext context = JAXBContext.newInstance(Request.class);
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Готов!\n");

        while (true) {
            Socket incoming = serverSocket.accept();
            Object object;
            try (ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream())) {
                object = unmarshaller.unmarshal(ois);
            }
            onAccepted.accept((Request) object, incoming.getInetAddress().getHostAddress());
        }
    }

    public void setOnAccepted(BiConsumer<Request, String> onAccepted) {
        this.onAccepted = onAccepted;
    }

}