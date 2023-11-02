package messager.db;

import messager.entities.Dialog;

public class DialogService extends DAO<Dialog> {
    public DialogService() {
        super(Dialog.class);
    }
}
