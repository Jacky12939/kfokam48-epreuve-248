package cm.kfokam48.presence.repository;

import cm.kfokam48.presence.entity.Relecture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelectureRepository extends JpaRepository<Relecture, Long> {
    Optional<Relecture> findByExerciceId(Long exerciceId);
    List<Relecture> findByExerciceIdIn(List<Long> exerciceIds);
    long countByRelecteurIdAndRenduAtIsNull(Long relecteurId);
}
