package messager.server;

import messager.client.ClientXML;
import messager.db.*;
import messager.entities.Dialog;
import messager.entities.PersonalDialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.Request;
import messager.requests.StringList;
import messager.requests.TransferableObject;
import messager.response.*;
import messager.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessengerServer {

    private final UserService userService = new UserService();
    private final DialogService dialogService = new DialogService();
    private final PersonalDialogService personalDialogService = new PersonalDialogService();
    private final MessagesService messagesService = new MessagesService();
    private final Server server = new Server();

    public MessengerServer() {
        server.setOnAccepted((request, address) -> {
            Optional<Object> optionalResult = processRequest(request);
            optionalResult.ifPresent(result -> {
                ClientXML client = new ClientXML();
                client.post(result, address);
            });
        });
    }

    private Optional<Object> processRequest(Request request) {
        try {
            Method method = this.getClass().getMethod(request.getFunction(), TransferableObject.class);
            Object result = method.invoke(this, request.getTransferableObject());
            return Optional.ofNullable(result);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    public CheckAccessResponse checkServerAccess(TransferableObject params) {
        return new CheckAccessResponse();
    }

    @SuppressWarnings("unused")
    public SignInResponse signIn(TransferableObject params) {
        String login = params.getString("login");
        String password = params.getString("password");
        Optional<User> optionalUser = userService.selectAll().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
        SignInResponse response;
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPasswordHash() == password.hashCode()) {
                response = new SignInResponse(optionalUser.get(), SignInResponse.SignInStatus.OK);
            } else {
                response = new SignInResponse(null, SignInResponse.SignInStatus.WRONG_PASSWORD);
            }
        } else {
            response = new SignInResponse(null, SignInResponse.SignInStatus.USER_NOT_FOUND);
        }

        return response;
    }

    @SuppressWarnings("unused")
    public Void readMessages(TransferableObject params) {
        StringList messagesId = params.getStringList("messagesId");
        messagesService.readMessages(messagesId, params.getInt("userId"));
        return null;
    }

    @SuppressWarnings("unused")
    public MessagesList getMessages(TransferableObject params) {
        List<TextMessage> messages = messagesService.getMessages(params.getInt("dialogId"));
        if (params.getBoolean("unreadOnly")) {
            messages = messages.stream()
                    .filter(message -> message.getReadBy().stream().map(User::getId).noneMatch(aLong -> aLong == params.getInt("userId")))
                    .collect(Collectors.toList());
        }
        return new MessagesList(messages);
    }

    @SuppressWarnings("unused")
    public MessagesList getOldMessages(TransferableObject params) {
        int dialogId = params.getInt("dialogId");
        List<TextMessage> messages = messagesService.getMessages(dialogId);

        int userId = params.getInt("userId");
        messages = messages.stream()
                .filter(message -> message.getReadBy().stream().map(User::getId).anyMatch(id -> id == userId))
                .collect(Collectors.toList());

        return new MessagesList(messages);
    }

    @SuppressWarnings("unused")
    public PersonalDialogsResponse getPersonalDialogs(TransferableObject params) {
        List<PersonalDialog> dialogs = personalDialogService.getDialogsFor(params.getInt("userId"));
        return new PersonalDialogsResponse(dialogs);
    }

    @SuppressWarnings("unused")
    public Object addMessage(TransferableObject params) {
        User userFrom = userService.findById(params.getInt("userId")).get();
        Dialog dialog = dialogService.findById(params.getInt("dialogId")).get();
        TextMessage textMessage = new TextMessage(
                dialog,
                userFrom,
                params.getString("text"),
                params.getString("dateTime")
        );
        messagesService.add(textMessage);
        return null;
    }

    @SuppressWarnings("unused")
    public SignUpResponse signUp(TransferableObject params) {
        Optional<User> optionalUser = userService.selectAll().stream()
                .filter(u -> u.getLogin().equals(params.getString("login")))
                .findFirst();
        SignUpResponse response;
        if (optionalUser.isPresent()) {
            response = new SignUpResponse(null, SignUpResponse.SignUpStatus.USER_ALREADY_EXISTS);
        } else {
            String encodedImage = null;
            if (params.getString("encodedImage") != null) {
                BufferedImage image = ImageUtils.decodeImage(params.getString("encodedImage"));
                image = ImageUtils.cropImageAtCenter(image);
                image = ImageUtils.scaleImage(image, 48, 48);
                encodedImage = ImageUtils.encodeImage(image, params.getString("imageFormat"));
            }
            User user = new User(params.getString("login"), params.getString("userName"), params.getString("password"), encodedImage);
            response = new SignUpResponse(user, SignUpResponse.SignUpStatus.OK);
            userService.save(user);
        }

        return response;
    }

    @SuppressWarnings("unused")
    public UsersList usersList(TransferableObject params) {
        List<User> availableUsers = userService.getUsersWithoutDialog(params.getInt("userId"));
        return new UsersList(availableUsers);
    }

    @SuppressWarnings("unused")
    public PersonalDialog addDialog(TransferableObject params) {
        User userFrom = userService.findById(params.getInt("userFromId")).get();
        User userTo = userService.findById(params.getInt("userToId")).get();
        PersonalDialog dialog = new PersonalDialog(userFrom, userTo);
        personalDialogService.save(dialog);
        return dialog;
    }

    @SuppressWarnings("unused")
    public Void deleteDialog(TransferableObject params) {
        dialogService.findById(params.getInt("dialogId")).ifPresent(dialogService::delete);
        return null;
    }

    @SuppressWarnings("unused")
    public TextMessage getLastMessage(TransferableObject params) {
        Optional<TextMessage> messageOptional = messagesService.getMessages(params.getInt("dialogId"))
                .stream()
                .max(Comparator.comparing(m -> LocalDateTime.parse(m.getDateTime())));
        return messageOptional.orElse(new TextMessage());
    }

    @SuppressWarnings("unused")
    public XmlBoolean isReadMessage(TransferableObject params) {
        TextMessage message = messagesService.findById(params.getInt("messageId")).get();
        boolean b = message.getReadBy().stream()
                .anyMatch(user -> user.getId() != message.getUserFrom().getId());
        return new XmlBoolean(b);
    }

    @SuppressWarnings("unused")
    public MessagesList getReadMessages(TransferableObject params) {
        List<TextMessage> messages = messagesService.getReadMessages(params.getInt("dialogId"), params.getInt("userId"));
        return new MessagesList(messages);
    }

    @SuppressWarnings("unused")
    public Object updateAccepted(TransferableObject params) {
        StringList messagesId = params.getStringList("messagesId");
        int userId = params.getInt("userId");
        messagesService.deleteStatesWhere(messagesId.getList().stream().map(Long::parseLong).collect(Collectors.toList()), userId);
        return null;
    }

    @SuppressWarnings("unused")
    public Object editUser(TransferableObject params) {
        int id = params.getInt("userId");
        User user = userService.findById(id).get();
        user.name = params.getString("userName");
        user.encodedImage = params.getString("encodedImage");
        userService.update(user);
        return new EditUserResponse(EditUserResponse.Status.OK);
    }

    public void start() {
        server.start();
    }

}
