package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.SessionRequest;
import cm.kfokam48.presence.dto.SessionResponse;
import cm.kfokam48.presence.entity.Session;
import cm.kfokam48.presence.exception.PromotionInconnueException;
import cm.kfokam48.presence.repository.PromotionRepository;
import cm.kfokam48.presence.repository.SessionRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {

    private static final int DUREE_VALIDITE_MINUTES = 15;
    private final SessionRepository sessionRepository;
    private final PromotionRepository promotionRepository;
    private final SecureRandom random = new SecureRandom();

    public SessionService(SessionRepository sessionRepository,
                          PromotionRepository promotionRepository) {
        this.sessionRepository = sessionRepository;
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public SessionResponse ouvrir(SessionRequest request) {
        promotionRepository.findById(request.promotionId())
            .orElseThrow(() -> new PromotionInconnueException(request.promotionId()));

        LocalDateTime ouverture = LocalDateTime.now();
        LocalDateTime expiration = ouverture.plusMinutes(DUREE_VALIDITE_MINUTES);

        Session session = new Session();
        session.setTitre(request.titre());
        session.setCode(genererCode());
        session.setPromotionId(request.promotionId());
        session.setOuvertureAt(ouverture);
        session.setExpirationAt(expiration);

        Session saved = sessionRepository.save(session);

        return new SessionResponse(
            saved.getId(),
            saved.getCode(),
            saved.getOuvertureAt(),
            saved.getExpirationAt()
        );
    }

    private String genererCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
