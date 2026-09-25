package cm.kfokam48.presence.exception;

public class SessionInconnueException extends RuntimeException {
    public SessionInconnueException(Long id) {
        super("Session inconnue : " + id);
    }
}
