# D4 — États d'un exercice (bonus)

```mermaid
stateDiagram-v2
  [*] --> DEPOSE : étudiant dépose le lien
  DEPOSE --> EN_ATTENTE : relecteur assigné (RG7)
  EN_ATTENTE --> RELU : relecture rendue (EF5)
  RELU --> EN_ATTENTE : correction avant clôture (RG12, Q10)
  EN_ATTENTE --> [*] : session clôturée sans relecture
  RELU --> [*] : session clôturée
