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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MessengerServer {

    private final UserService userService = new UserService();
    private final DialogService dialogService = new DialogService();
    private final MessagesService messagesService = new MessagesService();
    private final Server server;
    private final List<User> connectedUsers = new ArrayList<>();

    public MessengerServer() {
        ServerBuilder builder = new ServerBuilder();
        builder.addClass(TextMessage.class, this::onMessageRecieved);
        builder.addClass(SignInRequest.class, this::onSignIn);
        builder.addClass(SignUpRequest.class, this::onSignUp);
        builder.addClass(DialogsListRequest.class, this::onDialogsList);
        builder.addClass(MessagesRequest.class, this::onMessagesRequest);
        builder.addClass(UsersListRequest.class, this::onUsersRequest);
        builder.addClass(AddDialogRequest.class, this::onAddDialogRequest);
        server = builder.build();
    }

    private void onMessagesRequest(MessagesRequest request) {
        List<TextMessage> messages = messagesService.getMessages(request.getDialog());
        new ClientXML("127.0.0.1").post(new MessagesResponse(messages));
    }

    private void onDialogsList(DialogsListRequest request) {
        List<Dialog> dialogs = dialogService.getDialogsFor(request.getUser());
        DialogsListResponse response = new DialogsListResponse(dialogs);
        new ClientXML("127.0.0.1").post(response);
    }

    private void onMessageRecieved(TextMessage message) {
        messagesService.add(message);
    }

    private void onSignUp(SignUpRequest signUpRequest) {
        User user = signUpRequest.getUser();
        Optional<User> optionalUser = userService.getRegisteredUsers().stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findFirst();
        SignUpResponse response;
        if (optionalUser.isPresent()) {
            response = SignUpResponse.USER_ALREADY_EXISTS;
        } else {
            response = SignUpResponse.OK;
            userService.register(user);
        }

        new ClientXML("127.0.0.1").post(response);
    }

    private void onSignIn(SignInRequest signInRequest) {
        String userName = signInRequest.getUserName();
        String password = signInRequest.getPassword();
        Optional<User> optionalUser = userService.getRegisteredUsers().stream()
                .filter(user -> user.getName().equals(userName))
                .findFirst();
        SignInResponse response;
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPassword().equals(password)) {
                connectedUsers.add(optionalUser.get());
                response = new SignInResponse(optionalUser.get(), SignInResponse.SignInStatus.OK);
            } else {
                response = new SignInResponse(null, SignInResponse.SignInStatus.WRONG_PASSWORD);
            }
        } else {
            response = new SignInResponse(null, SignInResponse.SignInStatus.USER_NOT_FOUND);
        }

        new ClientXML("127.0.0.1").post(response);
    }

    private void onUsersRequest(UsersListRequest request) {
        List<User> registeredUsers = userService.getRegisteredUsers();
        UsersListResponse response = new UsersListResponse(registeredUsers);
        new ClientXML("127.0.0.1").post(response);
    }

    private void onAddDialogRequest(AddDialogRequest request) {
        Dialog dialog = new Dialog(null, Arrays.asList(request.getUserFrom(), request.getUserTo()));
        dialogService.add(dialog);
        AddDialogResponse response = new AddDialogResponse(dialog);
        new ClientXML("127.0.0.1").post(response);
    }

    public void start() {
        server.start();
    }

}
