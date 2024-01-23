package mc.recraftors.unruled_api.utils;

public class EncapsulatedException extends RuntimeException {
    public final Exception exception;

    public EncapsulatedException(Exception e) {
        super(e);
        this.exception = e;
    }

    public EncapsulatedException(String s, Exception e) {
        super(s, e);
        this.exception = e;
    }
}
