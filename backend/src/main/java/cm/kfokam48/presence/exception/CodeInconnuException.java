package cm.kfokam48.presence.exception;

public class CodeInconnuException extends RuntimeException {
    public CodeInconnuException(String code) {
        super("Le code de présence est inconnu.");
    }
}
