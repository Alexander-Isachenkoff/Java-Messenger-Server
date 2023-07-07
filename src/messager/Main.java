package messager;

import messager.entities.User;
import messager.server.MessagerServer;

public class Main {

    public static void main(String[] args) {
        MessagerServer server = new MessagerServer();

        server.registerUser(new User("me", "123"));
        server.registerUser(new User("he", "1234"));
        server.registerUser(new User("she", "12345"));
        server.registerUser(new User("they", "123456"));

        server.start();
    }

}