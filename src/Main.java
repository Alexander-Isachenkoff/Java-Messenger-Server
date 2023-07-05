import entities.TextMessage;
import entities.User;

public class Main {

    public static void main(String[] args) {
        ServerBuilder builder = new ServerBuilder();
        builder.addClass(TextMessage.class, m -> System.out.println(m.getUser().getName() + ": " + m.getMessage()));
        builder.addClass(User.class, user -> System.out.println("Подключен " + user.getName()));
        Server server = builder.build();
        server.start();
    }

}