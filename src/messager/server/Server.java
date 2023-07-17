package messager.server;

import messager.requests.Request;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Server {

    public static final int PORT = 11111;
    private final Unmarshaller unmarshaller;
    private ServerSocket serverSocket;

    private final Map<Class<? extends Request>, Function> map;
    private BiConsumer<Object, String> consumer = (o, s) -> {
    };

    public Server(Map<Class<? extends Request>, Function> classMap) {
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
            Function classConsumer = map.get(object.getClass());
            if (classConsumer != null) {
                Object result = classConsumer.apply(object);
                consumer.accept(result, serverSocket.getInetAddress().getHostAddress());
            }
        }
    }

    public void setConsumer(BiConsumer<Object, String> consumer) {
        this.consumer = consumer;
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