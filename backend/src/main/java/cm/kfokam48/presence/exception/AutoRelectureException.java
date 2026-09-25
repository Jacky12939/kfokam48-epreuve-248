package cm.kfokam48.presence.exception;

public class AutoRelectureException extends RuntimeException {
    public AutoRelectureException() {
        super("Interdiction de relire son propre exercice (RG3).");
    }
}
