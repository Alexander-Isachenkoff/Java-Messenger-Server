package messager.db;

import messager.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserService extends DAO<User> {

    public UserService() {
        super(User.class);
    }

    public List<User> getUsersWithoutDialog(long userId) {
        List<Long> usersIds = new DialogService().getDialogsFor(userId)
                .stream()
                .flatMap(dialog -> dialog.getUsers().stream())
                .map(User::getId)
                .collect(Collectors.toList());
        return selectAll().stream()
                .filter(user -> !usersIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

}
