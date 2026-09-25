package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.ExerciceDetailResponse;
import cm.kfokam48.presence.dto.ExerciceRequest;
import cm.kfokam48.presence.dto.ExerciceResponse;
import cm.kfokam48.presence.entity.Exercice;
import cm.kfokam48.presence.entity.Relecture;
import cm.kfokam48.presence.exception.ExerciceDejaDeposeException;
import cm.kfokam48.presence.exception.ExerciceInconnuException;
import cm.kfokam48.presence.exception.LienInvalideException;
import cm.kfokam48.presence.exception.SessionInconnueException;
import cm.kfokam48.presence.repository.ExerciceRepository;
import cm.kfokam48.presence.repository.RelectureRepository;
import cm.kfokam48.presence.repository.SessionRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciceService {

    private final ExerciceRepository exerciceRepository;
    private final SessionRepository sessionRepository;
    private final RelectureRepository relectureRepository;

    public ExerciceService(ExerciceRepository exerciceRepository,
                           SessionRepository sessionRepository,
                           RelectureRepository relectureRepository) {
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
        this.relectureRepository = relectureRepository;
    }

    @Transactional
    public ExerciceResponse deposer(ExerciceRequest request) {
        if (!estLienValide(request.lien())) {
            throw new LienInvalideException();
        }
        sessionRepository.findById(request.sessionId())
            .orElseThrow(() -> new SessionInconnueException(request.sessionId()));
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

    @Transactional(readOnly = true)
    public ExerciceDetailResponse consulter(Long exerciceId) {
        Exercice exercice = exerciceRepository.findById(exerciceId)
            .orElseThrow(() -> new ExerciceInconnuException(exerciceId));

        Optional<Relecture> relectureOpt = relectureRepository.findByExerciceId(exerciceId);

        Integer note = null;
        String commentaire = null;
        java.time.LocalDateTime renduAt = null;

        if (relectureOpt.isPresent()) {
            Relecture r = relectureOpt.get();
            if (r.getRenduAt() != null) {
                note = r.getNote();
                commentaire = r.getCommentaire();
                renduAt = r.getRenduAt();
            }
        }

        // Q8 : on ne renvoie PAS relecteurId
        return new ExerciceDetailResponse(
            exercice.getId(),
            exercice.getSessionId(),
            exercice.getEtudiantId(),
            exercice.getLien(),
            exercice.getStatut(),
            note,
            commentaire,
            renduAt
        );
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
