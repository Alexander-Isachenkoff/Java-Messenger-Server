package debug;

import messager.db.MessagesService;
import messager.db.PersonalDialogService;
import messager.db.UserService;
import messager.entities.PersonalDialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.server.MessengerServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

class MainTest {

    private final UserService userService = new UserService();
    private final PersonalDialogService personalDialogService = new PersonalDialogService();
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
            File file = new File("messenger.sqlite");
            if (file.exists()) {
                Files.delete(file.toPath());
            }
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

        PersonalDialog dialog = new PersonalDialog(users[0], users[1]);
        personalDialogService.save(dialog);

        messagesService.add(new TextMessage(dialog, users[1], "Привет!", LocalDateTime.now().toString()));

        server.start();
    }

}