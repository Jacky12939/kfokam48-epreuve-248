package cm.kfokam48.presence.exception;

public class TropDErreursException extends RuntimeException {
    public TropDErreursException() {
        super("Trop de tentatives. Réessayez dans 2 minutes.");
    }
}
