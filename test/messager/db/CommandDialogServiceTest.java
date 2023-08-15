package messager.db;

import messager.entities.CommandDialog;
import messager.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandDialogServiceTest {

    private final UserService userService = new UserService();
    private final CommandDialogService commandDialogService = new CommandDialogService();

    private final User[] users = {
            new User("user1", "test user - user1", "111"),
            new User("user2", "test user - user2", "222"),
            new User("user3", "test user - user3", "333")
    };

    @BeforeAll
    static void setUp() throws IOException {
        HibernateUtil.getSessionFactory().close();
        Files.deleteIfExists(new File("messenger.sqlite").toPath());
    }

    @BeforeEach
    void reset() {
        userService.deleteAll();
        commandDialogService.deleteAll();

        for (User user : users) {
            userService.save(user);
        }
    }

    @Test
    void add() {
        // setup
        CommandDialog dialog = new CommandDialog();
        dialog.getUsers().add(users[0]);
        dialog.getUsers().add(users[1]);

        // act
        commandDialogService.save(dialog);
        List<CommandDialog> dialogs = commandDialogService.selectAll();

        // verify
        assertEquals(1, dialogs.size());
        CommandDialog dialog1 = dialogs.get(0);
        List<User> users1 = dialog1.getUsers();
        assertEquals(2, users1.size());
        assertEquals(users[0], users1.get(0));
        assertEquals(users[1], users1.get(1));
    }

}