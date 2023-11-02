package messager.db;

import messager.entities.CommandDialog;

import java.util.List;
import java.util.stream.Collectors;

public class CommandDialogService extends DAO<CommandDialog> {

    public CommandDialogService() {
        super(CommandDialog.class);
    }

    public void add(CommandDialog dialog) {
        save(dialog);
    }

    public List<CommandDialog> getDialogsFor(long userId) {
        return selectAll()
                .stream()
                .filter(dialog -> dialog.hasUser(userId))
                .collect(Collectors.toList());
    }

}
