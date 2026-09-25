package cm.kfokam48.presence.dto;

import java.time.LocalDateTime;

public record RelectureResponse(
    Long id,
    Long exerciceId,
    Integer note,
    String commentaire,
    LocalDateTime renduAt
) {}
