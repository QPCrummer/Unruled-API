package mc.recraftors.unruled_api.utils;

@FunctionalInterface
public interface IGameruleValidator <T> {
    boolean validate(T t);

    static <U> boolean alwaysTrue(U u) {
        return true;
    }
}
