package cm.kfokam48.presence.dto;

import java.time.LocalDateTime;

public record SessionClotureResponse(
    Long id,
    String titre,
    LocalDateTime clotureAt
) {}
