package messager.server;

import messager.requests.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ServerBuilder {

    private final Map<Class<? extends Request>, Function> map = new HashMap<>();

    public <T extends Request, R> void addClass(Class<T> aClass, Function<T, R> onAccepted) {
        map.put(aClass, onAccepted);
    }

    public Server build() {
        return new Server(map);
    }

}
