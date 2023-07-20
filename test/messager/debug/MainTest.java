package messager.debug;

import messager.db.DialogService;
import messager.db.MessagesService;
import messager.db.UserService;
import messager.entities.Dialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.server.MessengerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;

class MainTest {

    private final UserService userService = new UserService();
    private final DialogService dialogService = new DialogService();
    private final MessagesService messagesService = new MessagesService();

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
    void mainTest() {
        MessengerServer server = new MessengerServer();

        for (User user : users) {
            userService.save(user);
        }

        Dialog dialog = new Dialog(Arrays.asList(users[0], users[1]));
        dialogService.add(dialog);

        messagesService.add(new TextMessage(users[1], "Привет!", LocalDateTime.now().toString(), dialog));

        server.start();
    }

}