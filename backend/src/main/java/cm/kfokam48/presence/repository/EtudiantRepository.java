package cm.kfokam48.presence.repository;

import cm.kfokam48.presence.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
}
