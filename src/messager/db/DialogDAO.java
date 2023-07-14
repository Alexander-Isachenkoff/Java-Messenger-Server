package messager.db;

import messager.entities.Dialog;
import messager.entities.User;
import org.hibernate.Session;

import java.util.List;
import java.util.stream.Collectors;

public class DialogDAO extends DAO<Dialog> {

    DialogDAO() {
        super(Dialog.class);
    }

    public List<Dialog> getDialogsFor(long userId) {
        Session session = openSession();
        List<Dialog> list = session.createQuery("From " + tClass.getSimpleName(), Dialog.class).list()
                .stream()
                .filter(dialog -> dialog.getUsers().stream().map(User::getId).anyMatch(aLong -> aLong == userId))
                .collect(Collectors.toList());
        session.close();
        return list;
    }

}
