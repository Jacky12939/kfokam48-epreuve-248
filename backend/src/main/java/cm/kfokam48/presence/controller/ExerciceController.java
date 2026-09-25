package cm.kfokam48.presence.controller;

import cm.kfokam48.presence.dto.ExerciceRequest;
import cm.kfokam48.presence.dto.ExerciceResponse;
import cm.kfokam48.presence.service.ExerciceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exercices")
public class ExerciceController {

    private final ExerciceService exerciceService;

    public ExerciceController(ExerciceService exerciceService) {
        this.exerciceService = exerciceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciceResponse deposer(@Valid @RequestBody ExerciceRequest request) {
        return exerciceService.deposer(request);
    }
}
