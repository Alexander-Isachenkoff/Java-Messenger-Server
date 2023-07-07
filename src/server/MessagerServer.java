package server;

import client.ClientXML;
import entities.TextMessage;
import entities.User;
import requests.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessagerServer {

    private final Server server;
    private final List<User> registeredUsers = new ArrayList<>();
    private final List<User> connectedUsers = new ArrayList<>();

    private final List<TextMessage> messages = new ArrayList<>();

    public MessagerServer() {
        ServerBuilder builder = new ServerBuilder();
        builder.addClass(TextMessage.class, this::onMessageRecieved);
        builder.addClass(SignInRequest.class, this::onSignIn);
        builder.addClass(SignUpRequest.class, this::onSignUp);
        builder.addClass(UsersListRequest.class, this::onUsersList);
        builder.addClass(MessagesRequest.class, this::onMessagesRequest);
        server = builder.build();
    }

    private void onMessagesRequest(MessagesRequest request) {
        String recipientName = request.getUser().getName();
        List<TextMessage> messagesForUser = messages.stream()
                .filter(message -> message.getUserTo().getName().equals(recipientName))
                .collect(Collectors.toList());
        new ClientXML("127.0.0.1").post(new MessagesResponse(messagesForUser));
    }

    private void onUsersList(UsersListRequest request) {
        UsersListResponse response = new UsersListResponse(registeredUsers);
        new ClientXML("127.0.0.1").post(response);
    }

    private void onMessageRecieved(TextMessage message) {
        messages.add(message);
    }

    private void onSignUp(SignUpRequest signUpRequest) {
        User user = signUpRequest.getUser();
        Optional<User> optionalUser = registeredUsers.stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findFirst();
        SignUpResponse response;
        if (optionalUser.isPresent()) {
            response = SignUpResponse.USER_ALREADY_EXISTS;
        } else {
            response = SignUpResponse.OK;
            registerUser(user);
        }

        new ClientXML("127.0.0.1").post(response);
    }

    public void registerUser(User user) {
        registeredUsers.add(user);
    }

    private void onSignIn(SignInRequest signInRequest) {
        User user = signInRequest.getUser();
        Optional<User> optionalUser = registeredUsers.stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findFirst();
        SignInResponse response;
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getPassword().equals(user.getPassword())) {
                connectedUsers.add(user);
                response = SignInResponse.OK;
            } else {
                response = SignInResponse.WRONG_PASSWORD;
            }
        } else {
            response = SignInResponse.USER_NOT_FOUND;
        }

        new ClientXML("127.0.0.1").post(response);
    }

    public void start() {
        server.start();
    }

}
