package cm.kfokam48.presence.exception;

public class NoteInvalideException extends RuntimeException {
    public NoteInvalideException() {
        super("La note doit être un entier entre 0 et 20.");
    }
}
