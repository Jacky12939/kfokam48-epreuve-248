package cm.kfokam48.presence.controller;

import cm.kfokam48.presence.dto.SessionRequest;
import cm.kfokam48.presence.dto.SessionResponse;
import cm.kfokam48.presence.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponse ouvrir(@Valid @RequestBody SessionRequest request) {
        return sessionService.ouvrir(request);
    }
}
