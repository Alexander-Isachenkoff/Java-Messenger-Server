package messager.server;

import messager.client.ClientXML;
import messager.db.DialogService;
import messager.db.MessagesService;
import messager.db.UserService;
import messager.entities.Dialog;
import messager.entities.TextMessage;
import messager.entities.User;
import messager.requests.*;
import messager.response.*;
import messager.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MessengerServer {

    private final UserService userService = new UserService();
    private final DialogService dialogService = new DialogService();
    private final MessagesService messagesService = new MessagesService();
    private final Server server;

    public MessengerServer() {
        ServerBuilder builder = new ServerBuilder();
        builder.addClass(AddMessageRequest.class, this::onMessageRecieved);
        builder.addClass(SignInRequest.class, this::onSignIn);
        builder.addClass(SignUpRequest.class, this::onSignUp);
        builder.addClass(DialogsListRequest.class, this::onDialogsList);
        builder.addClass(MessagesRequest.class, this::onMessagesRequest);
        builder.addClass(UsersListRequest.class, this::onUsersRequest);
        builder.addClass(AddDialogRequest.class, this::onAddDialogRequest);
        builder.addClass(DeleteDialogRequest.class, this::onDeleteDialogRequest);
        server = builder.build();
        server.setConsumer((response, address) -> {
            if (response != null) {
                ClientXML client = new ClientXML();
                client.post(response, address);
            }
        });
    }

    private MessagesResponse onMessagesRequest(MessagesRequest request) {
        List<TextMessage> messages = messagesService.getMessages(request.getDialogId());
        return new MessagesResponse(messages);
    }

    private DialogsListResponse onDialogsList(DialogsListRequest request) {
        List<Dialog> dialogs = dialogService.getDialogsFor(request.getUserId());
        return new DialogsListResponse(dialogs);
    }

    private Object onMessageRecieved(AddMessageRequest request) {
        User userFrom = userService.findById(request.getUserId()).get();
        Dialog dialog = dialogService.findById(request.getDialogId()).get();
        TextMessage textMessage = new TextMessage(
                userFrom,
                request.getText(),
                request.getDateTime(),
                dialog
        );
        messagesService.add(textMessage);
        return null;
    }

    private SignUpResponse onSignUp(SignUpRequest request) {
        Optional<User> optionalUser = userService.getRegisteredUsers().stream()
                .filter(u -> u.getName().equals(request.getUserName()))
                .findFirst();
        SignUpResponse response;
        if (optionalUser.isPresent()) {
            response = new SignUpResponse(null, SignUpResponse.SignUpStatus.USER_ALREADY_EXISTS);
        } else {
            String encodedImage = null;
            if (request.getEncodedImage() != null) {
                BufferedImage image = ImageUtils.decodeImage(request.getEncodedImage());
                image = ImageUtils.cropImageAtCenter(image);
                image = ImageUtils.scaleImage(image, 48, 48);
                encodedImage = ImageUtils.encodeImage(image, request.getImageFormat());
            }
            User user = new User(request.getUserName(), request.getPassword(), encodedImage);
            response = new SignUpResponse(user, SignUpResponse.SignUpStatus.OK);
            userService.register(user);
        }

        return response;
    }

    private SignInResponse onSignIn(SignInRequest signInRequest) {
        String userName = signInRequest.getUserName();
        String password = signInRequest.getPassword();
        Optional<User> optionalUser = userService.getRegisteredUsers().stream()
                .filter(user -> user.getName().equals(userName))
                .findFirst();
        SignInResponse response;
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPassword().equals(password)) {
                response = new SignInResponse(optionalUser.get(), SignInResponse.SignInStatus.OK);
            } else {
                response = new SignInResponse(null, SignInResponse.SignInStatus.WRONG_PASSWORD);
            }
        } else {
            response = new SignInResponse(null, SignInResponse.SignInStatus.USER_NOT_FOUND);
        }

        return response;
    }

    private UsersListResponse onUsersRequest(UsersListRequest request) {
        List<User> availableUsers = userService.getUsersWithoutDialog(request.getUserId());
        return new UsersListResponse(availableUsers);
    }

    private AddDialogResponse onAddDialogRequest(AddDialogRequest request) {
        Dialog dialog = new Dialog(null, Arrays.asList(request.getUserFrom(), request.getUserTo()));
        dialogService.add(dialog);
        return new AddDialogResponse(dialog);
    }

    private Void onDeleteDialogRequest(DeleteDialogRequest request) {
        dialogService.findById(request.getDialogId()).ifPresent(dialogService::delete);
        return null;
    }

    public void start() {
        server.start();
    }

}
