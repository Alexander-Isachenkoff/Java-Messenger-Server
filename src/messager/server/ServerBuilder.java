package messager.server;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ServerBuilder {

    private final Map<Class, Function> map = new HashMap<>();

    public <T, R> void addClass(Class<T> aClass, Function<T, R> onAccepted) {
        map.put(aClass, onAccepted);
    }

    public Server build() {
        return new Server(map);
    }

}
