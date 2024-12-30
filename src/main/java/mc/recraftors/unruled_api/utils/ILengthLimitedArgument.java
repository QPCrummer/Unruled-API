package mc.recraftors.unruled_api.utils;

public interface ILengthLimitedArgument {
    default void unruled_setMaxLength(int length) {}

    default int unruled_getMaxLength() {
        return 0;
    }
}
