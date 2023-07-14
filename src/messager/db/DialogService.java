package messager.db;

import messager.entities.Dialog;

public class DialogService extends DialogDAO {

    public void add(Dialog dialog) {
        save(dialog);
    }

}
