package messager.db;

import messager.entities.CommandDialog;
import messager.entities.PersonalDialog;
import messager.entities.TextMessage;
import messager.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagesServiceTest {

    private final UserService userService = new UserService();
    private final CommandDialogService commandDialogService = new CommandDialogService();
    private final PersonalDialogService personalDialogService = new PersonalDialogService();
    private final MessagesService messagesService = new MessagesService();

    private final User[] users = {
            new User("user1", "111"),
            new User("user2", "222"),
            new User("user3", "333")
    };

    @BeforeAll
    static void setUp() {
        new File("messenger.sqlite").delete();
    }

    @BeforeEach
    void reset() {
        commandDialogService.deleteAll();
        personalDialogService.deleteAll();
        userService.deleteAll();

        for (User user : users) {
            userService.save(user);
        }
    }

    @Test
    void save_read_Messages() {
        CommandDialog commandDialog = new CommandDialog();
        commandDialog.getUsers().add(users[0]);
        commandDialog.getUsers().add(users[1]);
        commandDialog.getUsers().add(users[2]);
        commandDialogService.save(commandDialog);

        PersonalDialog personalDialog = new PersonalDialog(users[0], users[1]);
        personalDialogService.save(personalDialog);

        TextMessage[] commandMessages = {
                new TextMessage(commandDialog, users[0], "Привет", LocalDateTime.now().toString()),
                new TextMessage(commandDialog, users[1], "Hello", LocalDateTime.now().toString()),
                new TextMessage(commandDialog, users[2], "Hi", LocalDateTime.now().toString()),
        };
        for (TextMessage message : commandMessages) {
            messagesService.save(message);
        }

        TextMessage[] personalMessages = {
                new TextMessage(personalDialog, users[0], "Привет 1", LocalDateTime.now().toString()),
                new TextMessage(personalDialog, users[1], "Hello 1", LocalDateTime.now().toString())
        };
        for (TextMessage message : personalMessages) {
            messagesService.save(message);
        }

        List<TextMessage> loadCommandMessages = messagesService.getMessages(commandDialog.getId());

        assertEquals(commandMessages[0].getDialog(), loadCommandMessages.get(0).getDialog());
        assertArrayEquals(commandMessages, loadCommandMessages.toArray());
    }

}