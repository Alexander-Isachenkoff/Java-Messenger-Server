package messager.db;

import messager.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private final UserService service = new UserService();

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
        service.deleteAll();
    }

    @Test
    void register() {
        User user = new User("user1", "test user - user1", "123456");
        service.save(user);
        long id = user.getId();

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