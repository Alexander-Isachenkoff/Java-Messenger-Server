package messager.db;

import messager.entities.PersonalDialog;

import java.util.List;
import java.util.stream.Collectors;

public class PersonalDialogService extends DAO<PersonalDialog> {

    public PersonalDialogService() {
        super(PersonalDialog.class);
    }

    public List<PersonalDialog> getDialogsFor(long userId) {
        return selectAll().stream()
                .filter(dialog -> dialog.getUser1().getId() == userId || dialog.getUser2().getId() == userId)
                .collect(Collectors.toList());
    }

}
