package messager.db;

import messager.entities.Dialog;
import messager.entities.TextMessage;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesService extends DAO<TextMessage> {
    public MessagesService() {
        super(TextMessage.class);
    }

    public void add(TextMessage message) {
        save(message);
    }

    public List<TextMessage> getMessages(Dialog dialog) {
        List<TextMessage> messages = selectAll().stream()
                .filter(message -> message.getDialog().getId() == dialog.getId())
                .collect(Collectors.toList());
        return messages;
    }

}
