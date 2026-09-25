package cm.kfokam48.presence.exception;

public class ExerciceDejaDeposeException extends RuntimeException {
    public ExerciceDejaDeposeException() {
        super("Un exercice a déjà été déposé pour cette session.");
    }
}
