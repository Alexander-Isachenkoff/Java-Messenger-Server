package messager.debug;

import messager.entities.User;
import messager.server.MessagerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class MainTest {

    @BeforeEach
    void setUp() {
        try {
            Files.delete(new File("messenger.sqlite").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void main() {
        MessagerServer server = new MessagerServer();

        server.registerUser(new User("me", "123"));
        server.registerUser(new User("he", "1234"));
        server.registerUser(new User("she", "12345"));
        server.registerUser(new User("they", "123456"));

        server.start();
    }

}