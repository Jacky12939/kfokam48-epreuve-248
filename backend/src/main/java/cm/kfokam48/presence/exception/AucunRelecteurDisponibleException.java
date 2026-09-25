package cm.kfokam48.presence.exception;

public class AucunRelecteurDisponibleException extends RuntimeException {
    public AucunRelecteurDisponibleException() {
        super("Aucun relecteur disponible parmi les étudiants présents (RG7).");
    }
}
