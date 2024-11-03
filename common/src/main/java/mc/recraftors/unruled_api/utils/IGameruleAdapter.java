package mc.recraftors.unruled_api.utils;

import java.util.Optional;

@FunctionalInterface
public interface IGameruleAdapter <T> {
    Optional<T> adapt(T t);
}
