package cm.kfokam48.presence.dto;

import java.time.LocalDateTime;

/**
 * Vue publique d'un exercice pour son auteur.
 * Q8 : la note et le commentaire sont visibles, mais PAS l'identité du relecteur.
 */
public record ExerciceDetailResponse(
    Long id,
    Long sessionId,
    Long etudiantId,
    String lien,
    String statut,
    Integer note,
    String commentaire,
    LocalDateTime renduAt
) {}
