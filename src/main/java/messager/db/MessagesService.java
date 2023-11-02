package messager.db;

import messager.entities.MessageState;
import messager.entities.TextMessage;
import messager.requests.StringList;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesService extends DAO<TextMessage> {

    private final UserService userService = new UserService();
    private final DAO<MessageState> messageStateDAO = new DAO<>(MessageState.class);

    public MessagesService() {
        super(TextMessage.class);
    }

    public void add(TextMessage message) {
        save(message);
    }

    public List<TextMessage> getMessages(long dialogId) {
        return selectAll().stream()
                .filter(message -> message.getDialog().getId() == dialogId)
                .collect(Collectors.toList());
    }

    public void readMessages(StringList messagesId, int userId) {
        for (int i = 0; i < messagesId.size(); i++) {
            int messageId = messagesId.getInt(i);
            readMessage(userId, messageId);
        }
    }

    private void readMessage(int userId, int messageId) {
        TextMessage message = findById(messageId).get();
        message.getReadBy().add(userService.findById(userId).get());
        messageStateDAO.save(new MessageState(message, message.getUserFrom()));
        update(message);
    }

    public List<TextMessage> getReadMessages(int dialogId, int userId) {
        List<TextMessage> messages = messageStateDAO.selectAll().stream()
                .filter(messageState -> messageState.getUser().getId() == userId)
                .map(MessageState::getTextMessage)
                .filter(textMessage -> textMessage.getDialog().getId() == dialogId)
                .collect(Collectors.toList());
        return messages;
    }

    public void deleteStatesWhere(List<Long> messagesId, int userId) {
        for (MessageState state : messageStateDAO.selectAll()) {
            if (messagesId.contains(state.getTextMessage().getId()) && state.getUser().getId() == userId) {
                messageStateDAO.delete(state);
            }
        }
    }

}
