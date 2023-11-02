package messager;

import messager.server.MessengerServer;

public class Main {

    public static void main(String[] args) {
        MessengerServer server = new MessengerServer();
        server.start();
    }

}