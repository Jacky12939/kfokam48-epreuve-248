package cm.kfokam48.presence.exception;

public class SessionCloseException extends RuntimeException {
    public SessionCloseException() {
        super("La session est terminée.");
    }
}
