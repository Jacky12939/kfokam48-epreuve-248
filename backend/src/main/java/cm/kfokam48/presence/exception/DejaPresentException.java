package cm.kfokam48.presence.exception;

public class DejaPresentException extends RuntimeException {
    public DejaPresentException() {
        super("Cet étudiant a déjà marqué sa présence à cette session.");
    }
}
