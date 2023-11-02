package messager.db;

import messager.entities.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserService extends DAO<User> {

    public UserService() {
        super(User.class);
    }

    public List<User> getUsersWithoutDialog(long userId) {
        List<Long> usersIds = new PersonalDialogService().getDialogsFor(userId)
                .stream()
                .flatMap(dialog -> Stream.of(dialog.getUser1(), dialog.getUser2()))
                .map(User::getId)
                .collect(Collectors.toList());
        return selectAll().stream()
                .filter(user -> !usersIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

}
