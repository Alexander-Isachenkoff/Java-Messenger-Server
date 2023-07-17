package messager.db;

import messager.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private final DAO<User> dao = DAO.of(User.class);

    public void register(User user) {
        dao.save(user);
    }

    public Optional<User> findById(Long id) {
        return dao.findById(id);
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public List<User> getUsersWithoutDialog(long userId) {
        List<Long> usersIds = new DialogService().getDialogsFor(userId).stream()
                .flatMap(dialog -> dialog.getUsers().stream())
                .map(User::getId)
                .collect(Collectors.toList());
        return dao.selectAll().stream()
                .filter(user -> !usersIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getRegisteredUsers() {
        return dao.selectAll();
    }
}
