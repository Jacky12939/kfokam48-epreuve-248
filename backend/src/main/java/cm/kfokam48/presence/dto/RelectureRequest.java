package cm.kfokam48.presence.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RelectureRequest(
    @NotNull(message = "La note est obligatoire")
    @Min(value = 0, message = "La note doit être entre 0 et 20")
    @Max(value = 20, message = "La note doit être entre 0 et 20")
    Integer note,

    @NotNull(message = "Le commentaire est obligatoire")
    @Size(max = 2000, message = "Le commentaire ne doit pas dépasser 2000 caractères")
    String commentaire
) {}
