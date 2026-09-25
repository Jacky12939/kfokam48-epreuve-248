package cm.kfokam48.presence.repository;

import cm.kfokam48.presence.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
