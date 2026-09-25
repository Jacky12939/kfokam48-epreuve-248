package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.AssignationRequest;
import cm.kfokam48.presence.dto.AssignationResponse;
import cm.kfokam48.presence.entity.Exercice;
import cm.kfokam48.presence.entity.Presence;
import cm.kfokam48.presence.entity.Relecture;
import cm.kfokam48.presence.exception.AucunRelecteurDisponibleException;
import cm.kfokam48.presence.exception.ExerciceInconnuException;
import cm.kfokam48.presence.exception.RelectureDejaAssigneeException;
import cm.kfokam48.presence.repository.ExerciceRepository;
import cm.kfokam48.presence.repository.PresenceRepository;
import cm.kfokam48.presence.repository.RelectureRepository;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelectureService {

    private final RelectureRepository relectureRepository;
    private final ExerciceRepository exerciceRepository;
    private final PresenceRepository presenceRepository;
    private final SecureRandom random = new SecureRandom();

    public RelectureService(RelectureRepository relectureRepository,
                            ExerciceRepository exerciceRepository,
                            PresenceRepository presenceRepository) {
        this.relectureRepository = relectureRepository;
        this.exerciceRepository = exerciceRepository;
        this.presenceRepository = presenceRepository;
    }

    @Transactional
    public AssignationResponse assigner(AssignationRequest request) {
        Exercice exercice = exerciceRepository.findById(request.exerciceId())
            .orElseThrow(() -> new ExerciceInconnuException(request.exerciceId()));

        // RG6 : un seul relecteur
        relectureRepository.findByExerciceId(exercice.getId())
            .ifPresent(r -> { throw new RelectureDejaAssigneeException(); });

        // RG7 : parmi les présents, hors déposant (RG3)
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
}
