package server;

import client.ClientXML;
import entities.TextMessage;
import entities.User;
import requests.SignInRequest;
import requests.SignInResponse;
import requests.SignUpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessagerServer {

    private final Server server;
    private final List<User> registeredUsers = new ArrayList<>();
    private final List<User> connectedUsers = new ArrayList<>();

    public MessagerServer() {
        ServerBuilder builder = new ServerBuilder();
        builder.addClass(TextMessage.class, this::onMessageRecieved);
        builder.addClass(SignInRequest.class, this::onSignIn);
        builder.addClass(SignUpRequest.class, this::onSignUp);
        server = builder.build();
    }

    private void onMessageRecieved(TextMessage m) {
        System.out.println(m.getUser().getName() + ": " + m.getMessage());
    }

    private void onSignUp(SignUpRequest signUpRequest) {
        User user = signUpRequest.getUser();
        Optional<User> optionalUser = registeredUsers.stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findFirst();
        if (optionalUser.isPresent()) {
            System.out.println("Пользователь " + user.getName() + " уже зарегистрирован!");
        } else {
            registeredUsers.add(user);
            System.out.println("Зарегистрирован " + user.getName());
        }
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
