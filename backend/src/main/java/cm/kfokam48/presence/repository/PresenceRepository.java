package cm.kfokam48.presence.repository;

import cm.kfokam48.presence.entity.Presence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    Optional<Presence> findBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);
    long countBySessionId(Long sessionId);
    List<Presence> findBySessionId(Long sessionId);
    long countByEtudiantId(Long etudiantId);
}
