package cm.kfokam48.presence.repository;

import cm.kfokam48.presence.entity.Session;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByCode(String code);
    List<Session> findByPromotionId(Long promotionId);
}
