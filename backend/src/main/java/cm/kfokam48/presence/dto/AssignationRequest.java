package cm.kfokam48.presence.dto;

import jakarta.validation.constraints.NotNull;

public record AssignationRequest(
    @NotNull(message = "L'identifiant d'exercice est obligatoire") Long exerciceId
) {}
