package messager.db;

import messager.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private final UserService service = new UserService();

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
        service.deleteAll();
    }

    @Test
    void register() {
        User user = new User("user1", "123456");
        service.save(user);
        Long id = user.getId();

        Optional<User> optionalUser = service.findById(id);
        assertTrue(optionalUser.isPresent());
        assertEquals(user, optionalUser.get());
    }

    @Test
    void selectAll() {
        for (User user : users) {
            service.save(user);
        }
        assertEquals(Arrays.asList(users), service.selectAll());
    }

}