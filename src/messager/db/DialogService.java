package messager.db;

import messager.entities.Dialog;
import messager.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class DialogService extends DAO<Dialog> {

    public DialogService() {
        super(Dialog.class);
    }

    public void add(Dialog dialog) {
        save(dialog);
    }

    public List<Dialog> getDialogsFor(User user) {
        List<Dialog> dialogs = selectAll().stream()
                .filter(dialog -> dialog.getUsers().contains(user))
                .collect(Collectors.toList());
        return dialogs;
    }

}
