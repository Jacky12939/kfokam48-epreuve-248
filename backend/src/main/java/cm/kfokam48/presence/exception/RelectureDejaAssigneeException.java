package cm.kfokam48.presence.exception;

public class RelectureDejaAssigneeException extends RuntimeException {
    public RelectureDejaAssigneeException() {
        super("Une relecture est déjà assignée à cet exercice (RG6).");
    }
}
