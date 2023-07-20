package messager.db;

import messager.entities.Dialog;
import messager.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DialogServiceTest {

    private final UserService userService = new UserService();
    private final DialogService dialogService = new DialogService();

    private final User[] users = new User[]{
            new User("user1", "111"),
            new User("user2", "222"),
            new User("user3", "333")
    };

    @BeforeEach
    void setUp() {
        userService.deleteAll();
        dialogService.deleteAll();

        for (User user : users) {
            userService.save(user);
        }
    }

    @Test
    void add() {
        // setup
        Dialog dialog = new Dialog();
        dialog.getUsers().add(users[0]);
        dialog.getUsers().add(users[1]);

        // act
        dialogService.save(dialog);
        List<Dialog> dialogs = dialogService.selectAll();

        // verify
        assertEquals(1, dialogs.size());
        Dialog dialog1 = dialogs.get(0);
        List<User> users1 = dialog1.getUsers();
        assertEquals(2, users1.size());
        assertEquals(users[0], users1.get(0));
        assertEquals(users[1], users1.get(1));
    }

}