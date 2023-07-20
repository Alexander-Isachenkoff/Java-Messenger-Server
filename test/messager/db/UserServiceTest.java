package messager.db;

import messager.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService();
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

}