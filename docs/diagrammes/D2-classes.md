# D2 — Modèle de données (v2 après enveloppe)

Correspond aux migrations Flyway V1 + V2.

```mermaid
erDiagram
  PROMOTION ||--o{ ETUDIANT : contient
  PROMOTION ||--o{ SESSION : organise
  SESSION ||--o{ PRESENCE : enregistre
  SESSION ||--o{ EXERCICE : recoit
  ETUDIANT ||--o{ PRESENCE : marque
  ETUDIANT ||--o{ EXERCICE : depose
  EXERCICE ||--o{ RELECTURE : fait_l_objet
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

```
## Contraintes (v2)
presence(session_id, etudiant_id) : UNIQUE (RG2)

exercice(session_id, etudiant_id) : UNIQUE

relecture(exercice_id, relecteur_id) : UNIQUE — au plus un rendu par relecteur et par exercice. exercice_id n'est PLUS unique seul (v2).

relecture : jusqu'à 2 lignes par exercice (RG6 v2)

note : entier 0-20 (RG5)

source : ETUDIANT | FORMATEUR (RG11)

statut : DEPOSE | EN_ATTENTE | RELU
