package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.PresenceRequest;
import cm.kfokam48.presence.dto.PresenceResponse;
import cm.kfokam48.presence.entity.Presence;
import cm.kfokam48.presence.entity.Session;
import cm.kfokam48.presence.exception.CodeExpireException;
import cm.kfokam48.presence.exception.CodeInconnuException;
import cm.kfokam48.presence.exception.DejaPresentException;
import cm.kfokam48.presence.repository.PresenceRepository;
import cm.kfokam48.presence.repository.SessionRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;

    public PresenceService(PresenceRepository presenceRepository,
                           SessionRepository sessionRepository) {
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public PresenceResponse marquer(PresenceRequest request) {
        // RG1 - code inconnu
        Session session = sessionRepository.findByCode(request.code())
            .orElseThrow(() -> new CodeInconnuException(request.code()));

        // RG1 - code expiré (15 min après ouverture)
        if (LocalDateTime.now().isAfter(session.getExpirationAt())) {
            throw new CodeExpireException();
        }

        // RG2 - déjà présent
        presenceRepository.findBySessionIdAndEtudiantId(session.getId(), request.etudiantId())
            .ifPresent(p -> { throw new DejaPresentException(); });

        Presence presence = new Presence();
        presence.setSessionId(session.getId());
        presence.setEtudiantId(request.etudiantId());
        presence.setSource("ETUDIANT");

        Presence saved = presenceRepository.save(presence);

        return new PresenceResponse(
            saved.getId(),
            saved.getSessionId(),
            saved.getEtudiantId(),
            saved.getSource()
        );
    }
}
