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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelectureService {

    private static final int NB_RELECTEURS = 2;

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

    /**
     * v2 (issue #22) : assigne DEUX relecteurs distincts parmi les présents, ≠ déposant.
     * Si moins de 3 présents (déposant inclus), un seul relecteur est assigné (RG3 + RG7).
     */
    @Transactional
    public AssignationResponse assigner(AssignationRequest request) {
        Exercice exercice = exerciceRepository.findById(request.exerciceId())
            .orElseThrow(() -> new ExerciceInconnuException(request.exerciceId()));

        if (relectureRepository.countByExerciceId(exercice.getId()) > 0) {
            throw new RelectureDejaAssigneeException();
        }

        List<Presence> presences = presenceRepository.findBySessionId(exercice.getSessionId());
        List<Long> candidats = presences.stream()
            .map(Presence::getEtudiantId)
            .filter(id -> !id.equals(exercice.getEtudiantId()))
            .distinct()
            .collect(Collectors.toCollection(ArrayList::new));

        if (candidats.isEmpty()) {
            throw new AucunRelecteurDisponibleException();
        }

        Collections.shuffle(candidats, random);
        int nbAAssignee = Math.min(NB_RELECTEURS, candidats.size());
        List<Long> selection = candidats.subList(0, nbAAssignee);

        List<Long> relecteurIds = new ArrayList<>();
        for (Long relecteurId : selection) {
            Relecture r = relectureRepository.save(new Relecture(exercice.getId(), relecteurId));
            relecteurIds.add(r.getRelecteurId());
        }

        return new AssignationResponse(exercice.getId(), relecteurIds);
    }

    @Transactional
    public RelectureResponse rendre(Long relectureId, RelectureRequest request) {
        if (request.note() == null || request.note() < 0 || request.note() > 20) {
            throw new NoteInvalideException();
        }

        Relecture relecture = relectureRepository.findById(relectureId)
            .orElseThrow(() -> new RelectureInconnueException(relectureId));

        Exercice exercice = exerciceRepository.findById(relecture.getExerciceId())
            .orElseThrow(() -> new ExerciceInconnuException(relecture.getExerciceId()));

        if (relecture.getRelecteurId().equals(exercice.getEtudiantId())) {
            throw new AutoRelectureException();
        }

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
