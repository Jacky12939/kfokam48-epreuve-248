package cm.kfokam48.presence.exception;

public class CodeExpireException extends RuntimeException {
    public CodeExpireException() {
        super("Le code de présence a expiré.");
    }
}
