package messager.db;

import messager.entities.Dialog;

import java.util.List;
import java.util.stream.Collectors;

public class DialogService extends DAO<Dialog> {

    public DialogService() {
        super(Dialog.class);
    }

    public void add(Dialog dialog) {
        save(dialog);
    }

    public List<Dialog> getDialogsFor(long userId) {
        return selectAll()
                .stream()
                .filter(dialog -> dialog.hasUser(userId))
                .collect(Collectors.toList());
    }

}
