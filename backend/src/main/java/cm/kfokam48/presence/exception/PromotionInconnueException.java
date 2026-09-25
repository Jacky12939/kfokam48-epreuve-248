package cm.kfokam48.presence.exception;

public class PromotionInconnueException extends RuntimeException {
    public PromotionInconnueException(Long id) {
        super("Promotion inconnue : " + id);
    }
}
