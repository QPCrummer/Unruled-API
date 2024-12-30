package mc.recraftors.unruled_api.utils;

public interface GameruleAccessor<T> {
    void unruled_setAdapter(IGameruleAdapter<T> adapter);
    void unruled_setValidator(IGameruleValidator<T> validator);

    IGameruleAdapter<T> unruled_getAdapter();
    IGameruleValidator<T> unruled_getValidator();
}
