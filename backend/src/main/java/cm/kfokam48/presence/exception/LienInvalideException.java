package cm.kfokam48.presence.exception;

public class LienInvalideException extends RuntimeException {
    public LienInvalideException() {
        super("Le lien fourni est invalide.");
    }
}
