package cm.kfokam48.presence.controller;

import cm.kfokam48.presence.dto.PresenceRequest;
import cm.kfokam48.presence.dto.PresenceResponse;
import cm.kfokam48.presence.service.PresenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/presences")
public class PresenceController {

    private final PresenceService presenceService;

    public PresenceController(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PresenceResponse marquer(@Valid @RequestBody PresenceRequest request) {
        return presenceService.marquer(request);
    }
}
