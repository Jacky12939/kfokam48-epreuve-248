package cm.kfokam48.presence.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Requête de présence.
 * - Cas étudiant : { code, etudiantId } → source par défaut ETUDIANT
 * - Cas formateur : { sessionId, etudiantId, source: "FORMATEUR" } → ajout manuel (RG11)
 */
public record PresenceRequest(
    String code,
    Long sessionId,
    @NotNull(message = "L'identifiant étudiant est obligatoire") Long etudiantId,
    String source
) {
    public String sourceEffective() {
        return (source == null || source.isBlank()) ? "ETUDIANT" : source;
    }
}
