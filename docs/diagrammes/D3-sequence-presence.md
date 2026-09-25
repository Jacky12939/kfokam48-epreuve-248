# D3 — Séquence : marquer sa présence

Cas nominal + cas d'erreur conformes au contrat d'API.

```mermaid
sequenceDiagram
  actor E as Étudiant
  participant F as Front
  participant API as PresenceController
  participant S as PresenceService
  participant DB as Base

  E->>F: saisit le code
  F->>API: POST /api/presences
  API->>S: enregistrer(code, etudiantId)

  alt code inconnu
    S-->>API: CodeInconnuException
    API-->>F: 400 CODE_INCONNU
  else code expiré (RG1)
    S-->>API: CodeExpireException
    API-->>F: 410 CODE_EXPIRE
  else déjà présent (RG2)
    S-->>API: DejaPresentException
    API-->>F: 409 DEJA_PRESENT
  else trop d'erreurs (RG4)
    S-->>API: BlocageActifException
    API-->>F: 429 TROP_D_ERREURS
  else cas nominal
    S->>DB: INSERT presence
    DB-->>S: ok
    S-->>API: Presence
    API-->>F: 201 id, sessionId, etudiantId, source
  end
