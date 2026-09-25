package cm.kfokam48.presence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SessionRequest(
    @NotBlank(message = "Le titre est obligatoire") String titre,
    @NotNull(message = "L'identifiant de promotion est obligatoire") Long promotionId
) {}
