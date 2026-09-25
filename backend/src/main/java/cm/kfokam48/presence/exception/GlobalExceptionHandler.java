package cm.kfokam48.presence.exception;

import cm.kfokam48.presence.dto.ErrorResponse;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PromotionInconnueException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePromotionInconnue(PromotionInconnueException ex) {
        return new ErrorResponse("PROMOTION_INCONNUE", ex.getMessage());
    }

    @ExceptionHandler(SessionInconnueException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSessionInconnue(SessionInconnueException ex) {
        return new ErrorResponse("SESSION_INCONNUE", ex.getMessage());
    }

    @ExceptionHandler(CodeInconnuException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCodeInconnu(CodeInconnuException ex) {
        return new ErrorResponse("CODE_INCONNU", ex.getMessage());
    }

    @ExceptionHandler(CodeExpireException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ErrorResponse handleCodeExpire(CodeExpireException ex) {
        return new ErrorResponse("CODE_EXPIRE", ex.getMessage());
    }

    @ExceptionHandler(DejaPresentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDejaPresent(DejaPresentException ex) {
        return new ErrorResponse("DEJA_PRESENT", ex.getMessage());
    }

    @ExceptionHandler(SessionCloseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSessionClose(SessionCloseException ex) {
        return new ErrorResponse("SESSION_CLOTUREE", ex.getMessage());
    }

    @ExceptionHandler(TropDErreursException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleTropDErreurs(TropDErreursException ex) {
        return new ErrorResponse("TROP_D_ERREURS", ex.getMessage());
    }

    @ExceptionHandler(LienInvalideException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLienInvalide(LienInvalideException ex) {
        return new ErrorResponse("LIEN_INVALIDE", ex.getMessage());
    }

    @ExceptionHandler(ExerciceDejaDeposeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleExerciceDejaDepose(ExerciceDejaDeposeException ex) {
        return new ErrorResponse("EXERCICE_DEJA_DEPOSE", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + " : " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return new ErrorResponse("CHAMP_MANQUANT", msg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex) {
        return new ErrorResponse("ERREUR_INTERNE", "Une erreur interne est survenue.");
    }
}
