package cm.kfokam48.presence.dto;

public record TableauLigneResponse(
    Long etudiantId,
    String nom,
    long presences,
    long exercicesDeposes,
    Double moyenne,
    long relecturesEnAttente
) {}
