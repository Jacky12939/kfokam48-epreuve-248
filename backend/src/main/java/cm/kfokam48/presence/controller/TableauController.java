package cm.kfokam48.presence.controller;

import cm.kfokam48.presence.dto.TableauLigneResponse;
import cm.kfokam48.presence.service.TableauService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tableau")
public class TableauController {

    private final TableauService tableauService;

    public TableauController(TableauService tableauService) {
        this.tableauService = tableauService;
    }

    @GetMapping
    public List<TableauLigneResponse> tableau(@RequestParam Long promotionId) {
        return tableauService.construire(promotionId);
    }
}
