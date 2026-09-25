package cm.kfokam48.presence.exception;

public class RelectureInconnueException extends RuntimeException {
    public RelectureInconnueException(Long id) {
        super("Relecture inconnue : " + id);
    }
}
