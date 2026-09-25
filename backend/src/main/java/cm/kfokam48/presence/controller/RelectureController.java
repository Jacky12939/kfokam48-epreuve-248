package cm.kfokam48.presence.controller;

import cm.kfokam48.presence.dto.AssignationRequest;
import cm.kfokam48.presence.dto.AssignationResponse;
import cm.kfokam48.presence.dto.RelectureRequest;
import cm.kfokam48.presence.dto.RelectureResponse;
import cm.kfokam48.presence.service.RelectureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relectures")
public class RelectureController {

    private final RelectureService relectureService;

    public RelectureController(RelectureService relectureService) {
        this.relectureService = relectureService;
    }

    @PostMapping("/assigner")
    @ResponseStatus(HttpStatus.CREATED)
    public AssignationResponse assigner(@Valid @RequestBody AssignationRequest request) {
        return relectureService.assigner(request);
    }

    @PostMapping("/{id}")
    public RelectureResponse rendre(@PathVariable Long id,
                                     @Valid @RequestBody RelectureRequest request) {
        return relectureService.rendre(id, request);
    }
}
