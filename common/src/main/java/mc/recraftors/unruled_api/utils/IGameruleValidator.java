package mc.recraftors.unruled_api.utils;

@FunctionalInterface
public interface IGameruleValidator <T> {
    boolean validate(T t);

    static <U> boolean alwaysTrue(U u) {
        return true;
    }

    default IGameruleValidator<T> and(IGameruleValidator<T> other) {
        return t -> this.validate(t) && other.validate(t);
    }

    default IGameruleValidator<T> or(IGameruleValidator<T> other) {
        return t -> this.validate(t) || other.validate(t);
    }

    default IGameruleValidator<T> xor(IGameruleValidator<T> other) {
        return t -> this.validate(t) ^ other.validate(t);
    }

    default IGameruleValidator<T> not() {
        return t -> !this.validate(t);
    }
}
