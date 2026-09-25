package cm.kfokam48.presence.repository;

import cm.kfokam48.presence.entity.Exercice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciceRepository extends JpaRepository<Exercice, Long> {
    Optional<Exercice> findBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);
    List<Exercice> findByEtudiantId(Long etudiantId);
    long countByEtudiantId(Long etudiantId);
}
