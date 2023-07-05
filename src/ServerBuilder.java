import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ServerBuilder {

    private final Map<Class, Consumer> map = new HashMap<>();

    public <T> void addClass(Class<T> aClass, Consumer<T> onAccepted) {
        map.put(aClass, onAccepted);
    }

    public Server build() {
        return new Server(map);
    }

}
