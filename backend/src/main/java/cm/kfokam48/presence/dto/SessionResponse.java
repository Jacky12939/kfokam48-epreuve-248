package cm.kfokam48.presence.dto;

import java.time.LocalDateTime;

public record SessionResponse(
    Long id,
    String code,
    LocalDateTime ouvertureAt,
    LocalDateTime expirationAt
) {}
