package messager.db;

import messager.entities.User;

import java.util.Optional;

public class UserService {

    private DAO<User> dao = new DAO<User>() {
        @Override
        protected Class<User> getLoadingClass() {
            return User.class;
        }
    };

    public void register(User user) {
        dao.save(user);
    }

    public Optional<User> findById(Long id) {
        return dao.findById(id);
    }

    public void deleteAll() {
        dao.deleteAll();
    }

}
