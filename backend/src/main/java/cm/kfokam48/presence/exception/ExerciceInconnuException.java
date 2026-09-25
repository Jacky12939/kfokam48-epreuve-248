package cm.kfokam48.presence.exception;

public class ExerciceInconnuException extends RuntimeException {
    public ExerciceInconnuException(Long id) {
        super("Exercice inconnu : " + id);
    }
}
