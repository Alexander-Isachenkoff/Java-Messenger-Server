public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        server.setOnMessageAccepted(m -> System.out.println(m.getName() + ": " + m.getMessage()));
        server.start();
    }

}