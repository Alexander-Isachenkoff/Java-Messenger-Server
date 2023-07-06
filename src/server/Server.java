package server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.function.Consumer;

public class Server {

    public static final int PORT = 11111;
    private final Unmarshaller unmarshaller;
    private ServerSocket serverSocket;

    private final Map<Class, Consumer> map;

    public Server(Map<Class, Consumer> classMap) {
        this.map = classMap;
        try {
            JAXBContext context = JAXBContext.newInstance(classMap.keySet().toArray(new Class[0]));
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
            Object object;
            try {
                object = getObject();
            } catch (IOException | JAXBException e) {
                e.printStackTrace();
                continue;
            }
            Consumer consumer = map.get(object.getClass());
            if (consumer != null) {
                consumer.accept(object);
            }
        }
    }

    private Object getObject() throws IOException, JAXBException {
        Socket incoming = serverSocket.accept();

        Object object;
        try (ObjectInputStream ois = new ObjectInputStream(incoming.getInputStream())) {
            object = unmarshaller.unmarshal(ois);
        }

        return object;
    }

}