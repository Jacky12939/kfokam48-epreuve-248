package cm.kfokam48.presence.exception;

public class SessionClotureeException extends RuntimeException {
    public SessionClotureeException() {
        super("La session est clôturée, la note n'est plus modifiable (RG12).");
    }
}
