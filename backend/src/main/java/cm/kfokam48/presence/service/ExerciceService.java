package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.ExerciceRequest;
import cm.kfokam48.presence.dto.ExerciceResponse;
import cm.kfokam48.presence.entity.Exercice;
import cm.kfokam48.presence.exception.ExerciceDejaDeposeException;
import cm.kfokam48.presence.exception.LienInvalideException;
import cm.kfokam48.presence.exception.SessionInconnueException;
import cm.kfokam48.presence.repository.ExerciceRepository;
import cm.kfokam48.presence.repository.SessionRepository;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciceService {

    private final ExerciceRepository exerciceRepository;
    private final SessionRepository sessionRepository;

    public ExerciceService(ExerciceRepository exerciceRepository,
                           SessionRepository sessionRepository) {
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public ExerciceResponse deposer(ExerciceRequest request) {
        // RG - lien valide
        if (!estLienValide(request.lien())) {
            throw new LienInvalideException();
        }

        // Session doit exister
        sessionRepository.findById(request.sessionId())
            .orElseThrow(() -> new SessionInconnueException(request.sessionId()));

        // 409 si déjà déposé
        exerciceRepository.findBySessionIdAndEtudiantId(request.sessionId(), request.etudiantId())
            .ifPresent(e -> { throw new ExerciceDejaDeposeException(); });

        Exercice exercice = new Exercice(
            request.sessionId(),
            request.etudiantId(),
            request.lien(),
            "DEPOSE"
        );

        Exercice saved = exerciceRepository.save(exercice);
        return new ExerciceResponse(saved.getId(), saved.getStatut());
    }

    private boolean estLienValide(String lien) {
        if (lien == null || lien.isBlank()) return false;
        try {
            URI uri = new URI(lien);
            String scheme = uri.getScheme();
            return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
