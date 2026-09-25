package cm.kfokam48.presence.dto;

import java.util.List;

public record AssignationResponse(
    Long exerciceId,
    List<Long> relecteurs
) {}
