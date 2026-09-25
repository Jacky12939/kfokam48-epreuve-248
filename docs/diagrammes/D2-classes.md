# D2 — Modèle de données

Ce diagramme correspond aux migrations Flyway V1 et suivantes.

```mermaid
erDiagram
  PROMOTION ||--o{ ETUDIANT : contient
  PROMOTION ||--o{ SESSION : organise
  SESSION ||--o{ PRESENCE : enregistre
  SESSION ||--o{ EXERCICE : recoit
  ETUDIANT ||--o{ PRESENCE : marque
  ETUDIANT ||--o{ EXERCICE : depose
  EXERCICE ||--o| RELECTURE : fait_l_objet
  ETUDIANT ||--o{ RELECTURE : effectue

  PROMOTION {
    long id
    string nom
  }
  ETUDIANT {
    long id
    string nom
    long promotion_id
  }
  SESSION {
    long id
    string titre
    string code
    long promotion_id
    timestamp ouverture_at
    timestamp expiration_at
    timestamp cloture_at
  }
  PRESENCE {
    long id
    long session_id
    long etudiant_id
    string source
  }
  EXERCICE {
    long id
    long session_id
    long etudiant_id
    string lien
    string statut
  }
  RELECTURE {
    long id
    long exercice_id
    long relecteur_id
    int note
    text commentaire
    timestamp rendu_at
  }
Contraintes
presence(session_id, etudiant_id) : unique — RG2

exercice(session_id, etudiant_id) : unique

relecture.exercice_id : unique — RG6

note : entier entre 0 et 20 — RG5

source : ETUDIANT ou FORMATEUR — RG11

statut : DEPOSE, EN_ATTENTE, RELU
