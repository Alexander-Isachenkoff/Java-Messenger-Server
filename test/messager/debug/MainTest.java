package messager.debug;

import messager.db.DialogService;
import messager.db.UserService;
import messager.entities.Dialog;
import messager.entities.User;
import messager.server.MessengerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

class MainTest {

    private final UserService userService = new UserService();
    private final DialogService dialogService = new DialogService();

    private final User[] users = new User[]{
            new User("me", "111"),
            new User("he", "222"),
            new User("she", "333"),
            new User("they", "444")
    };

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
        MessengerServer server = new MessengerServer();

        for (User user : users) {
            userService.register(user);
        }

        dialogService.add(new Dialog(Arrays.asList(users[0], users[1])));

        server.start();
    }

}