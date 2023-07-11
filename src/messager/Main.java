package messager;

import messager.server.MessagerServer;

public class Main {

    public static void main(String[] args) {
        MessagerServer server = new MessagerServer();
        server.start();
    }

}