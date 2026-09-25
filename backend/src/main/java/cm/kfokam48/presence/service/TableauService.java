package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.TableauLigneResponse;
import cm.kfokam48.presence.entity.Etudiant;
import cm.kfokam48.presence.entity.Exercice;
import cm.kfokam48.presence.entity.Relecture;
import cm.kfokam48.presence.exception.PromotionInconnueException;
import cm.kfokam48.presence.repository.EtudiantRepository;
import cm.kfokam48.presence.repository.ExerciceRepository;
import cm.kfokam48.presence.repository.PresenceRepository;
import cm.kfokam48.presence.repository.PromotionRepository;
import cm.kfokam48.presence.repository.RelectureRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableauService {

    private final PromotionRepository promotionRepository;
    private final EtudiantRepository etudiantRepository;
    private final PresenceRepository presenceRepository;
    private final ExerciceRepository exerciceRepository;
    private final RelectureRepository relectureRepository;

    public TableauService(PromotionRepository promotionRepository,
                          EtudiantRepository etudiantRepository,
                          PresenceRepository presenceRepository,
                          ExerciceRepository exerciceRepository,
                          RelectureRepository relectureRepository) {
        this.promotionRepository = promotionRepository;
        this.etudiantRepository = etudiantRepository;
        this.presenceRepository = presenceRepository;
        this.exerciceRepository = exerciceRepository;
        this.relectureRepository = relectureRepository;
    }

    @Transactional(readOnly = true)
    public List<TableauLigneResponse> construire(Long promotionId) {
        promotionRepository.findById(promotionId)
            .orElseThrow(() -> new PromotionInconnueException(promotionId));

        List<Etudiant> etudiants = etudiantRepository.findByPromotionId(promotionId);
        List<TableauLigneResponse> lignes = new ArrayList<>();

        for (Etudiant e : etudiants) {
            long nbPresences = presenceRepository.countByEtudiantId(e.getId());
            long nbExercices = exerciceRepository.countByEtudiantId(e.getId());
            long nbRelecturesAttente = relectureRepository
                .countByRelecteurIdAndRenduAtIsNull(e.getId());

            // Moyenne : on prend les exercices de l'étudiant, puis les relectures rendues
            List<Exercice> exercices = exerciceRepository.findByEtudiantId(e.getId());
            List<Long> exerciceIds = exercices.stream()
                .map(Exercice::getId).collect(Collectors.toList());

            Double moyenne = null;
            if (!exerciceIds.isEmpty()) {
                List<Relecture> relectures = relectureRepository.findByExerciceIdIn(exerciceIds);
                List<Integer> notes = relectures.stream()
                    .filter(r -> r.getRenduAt() != null && r.getNote() != null)
                    .map(Relecture::getNote)
                    .collect(Collectors.toList());
                if (!notes.isEmpty()) {
                    moyenne = notes.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                    // arrondi à 2 décimales
                    moyenne = Math.round(moyenne * 100.0) / 100.0;
                }
            }

            lignes.add(new TableauLigneResponse(
                e.getId(), e.getNom(), nbPresences, nbExercices, moyenne, nbRelecturesAttente
            ));
        }

        return lignes;
    }
}
