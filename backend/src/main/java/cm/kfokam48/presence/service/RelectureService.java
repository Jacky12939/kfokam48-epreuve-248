package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.AssignationRequest;
import cm.kfokam48.presence.dto.AssignationResponse;
import cm.kfokam48.presence.dto.RelectureRequest;
import cm.kfokam48.presence.dto.RelectureResponse;
import cm.kfokam48.presence.entity.Exercice;
import cm.kfokam48.presence.entity.Presence;
import cm.kfokam48.presence.entity.Relecture;
import cm.kfokam48.presence.entity.Session;
import cm.kfokam48.presence.exception.AucunRelecteurDisponibleException;
import cm.kfokam48.presence.exception.AutoRelectureException;
import cm.kfokam48.presence.exception.ExerciceInconnuException;
import cm.kfokam48.presence.exception.NoteInvalideException;
import cm.kfokam48.presence.exception.RelectureDejaAssigneeException;
import cm.kfokam48.presence.exception.RelectureInconnueException;
import cm.kfokam48.presence.exception.SessionClotureeException;
import cm.kfokam48.presence.repository.ExerciceRepository;
import cm.kfokam48.presence.repository.PresenceRepository;
import cm.kfokam48.presence.repository.RelectureRepository;
import cm.kfokam48.presence.repository.SessionRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelectureService {

    private final RelectureRepository relectureRepository;
    private final ExerciceRepository exerciceRepository;
    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;
    private final SecureRandom random = new SecureRandom();

    public RelectureService(RelectureRepository relectureRepository,
                            ExerciceRepository exerciceRepository,
                            PresenceRepository presenceRepository,
                            SessionRepository sessionRepository) {
        this.relectureRepository = relectureRepository;
        this.exerciceRepository = exerciceRepository;
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public AssignationResponse assigner(AssignationRequest request) {
        Exercice exercice = exerciceRepository.findById(request.exerciceId())
            .orElseThrow(() -> new ExerciceInconnuException(request.exerciceId()));

        relectureRepository.findByExerciceId(exercice.getId())
            .ifPresent(r -> { throw new RelectureDejaAssigneeException(); });

        List<Presence> presences = presenceRepository.findBySessionId(exercice.getSessionId());
        List<Long> candidats = presences.stream()
            .map(Presence::getEtudiantId)
            .filter(id -> !id.equals(exercice.getEtudiantId()))
            .distinct()
            .collect(Collectors.toList());

        if (candidats.isEmpty()) {
            throw new AucunRelecteurDisponibleException();
        }

        Long relecteurId = candidats.get(random.nextInt(candidats.size()));
        Relecture relecture = new Relecture(exercice.getId(), relecteurId);
        Relecture saved = relectureRepository.save(relecture);
        return new AssignationResponse(saved.getId(), saved.getExerciceId(), saved.getRelecteurId());
    }

    @Transactional
    public RelectureResponse rendre(Long relectureId, RelectureRequest request) {
        // RG5 : note entière 0-20 (double vérification car la validation pourrait être contournée)
        if (request.note() == null || request.note() < 0 || request.note() > 20) {
            throw new NoteInvalideException();
        }

        Relecture relecture = relectureRepository.findById(relectureId)
            .orElseThrow(() -> new RelectureInconnueException(relectureId));

        Exercice exercice = exerciceRepository.findById(relecture.getExerciceId())
            .orElseThrow(() -> new ExerciceInconnuException(relecture.getExerciceId()));

        // RG3 : pas d'auto-relecture
        if (relecture.getRelecteurId().equals(exercice.getEtudiantId())) {
            throw new AutoRelectureException();
        }

        // RG12 : corrigeable tant que la session n'est pas clôturée
        Session session = sessionRepository.findById(exercice.getSessionId())
            .orElseThrow(() -> new ExerciceInconnuException(exercice.getId()));
        if (session.getClotureAt() != null) {
            throw new SessionClotureeException();
        }

        relecture.setNote(request.note());
        relecture.setCommentaire(request.commentaire());
        relecture.setRenduAt(LocalDateTime.now());

        Relecture saved = relectureRepository.save(relecture);
        return new RelectureResponse(
            saved.getId(),
            saved.getExerciceId(),
            saved.getNote(),
            saved.getCommentaire(),
            saved.getRenduAt()
        );
    }
}
