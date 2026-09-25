package cm.kfokam48.presence.service;

import cm.kfokam48.presence.dto.PresenceRequest;
import cm.kfokam48.presence.dto.PresenceResponse;
import cm.kfokam48.presence.entity.Presence;
import cm.kfokam48.presence.entity.Session;
import cm.kfokam48.presence.exception.CodeExpireException;
import cm.kfokam48.presence.exception.CodeInconnuException;
import cm.kfokam48.presence.exception.DejaPresentException;
import cm.kfokam48.presence.exception.SessionInconnueException;
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
        String source = request.sourceEffective();
        Session session;

        if ("FORMATEUR".equals(source)) {
            // RG11 : ajout manuel par le formateur — sessionId obligatoire, pas de code
            if (request.sessionId() == null) {
                throw new CodeInconnuException("sessionId obligatoire pour un ajout FORMATEUR");
            }
            session = sessionRepository.findById(request.sessionId())
                .orElseThrow(() -> new SessionInconnueException(request.sessionId()));
        } else {
            // Cas étudiant : code obligatoire + vérification expiration (RG1)
            if (request.code() == null || request.code().isBlank()) {
                throw new CodeInconnuException("Le code est obligatoire.");
            }
            session = sessionRepository.findByCode(request.code())
                .orElseThrow(() -> new CodeInconnuException(request.code()));

            if (LocalDateTime.now().isAfter(session.getExpirationAt())) {
                throw new CodeExpireException();
            }
        }

        // RG2 : unicité (session, étudiant)
        presenceRepository.findBySessionIdAndEtudiantId(session.getId(), request.etudiantId())
            .ifPresent(p -> { throw new DejaPresentException(); });

        Presence presence = new Presence();
        presence.setSessionId(session.getId());
        presence.setEtudiantId(request.etudiantId());
        presence.setSource(source);

        Presence saved = presenceRepository.save(presence);
        return new PresenceResponse(
            saved.getId(),
            saved.getSessionId(),
            saved.getEtudiantId(),
            saved.getSource()
        );
    }
}
