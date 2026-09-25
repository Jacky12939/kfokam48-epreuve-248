CREATE TABLE promotion (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(100) NOT NULL
);

CREATE TABLE etudiant (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  promotion_id BIGINT NOT NULL REFERENCES promotion(id)
);

CREATE TABLE session (
  id BIGSERIAL PRIMARY KEY,
  titre VARCHAR(200) NOT NULL,
  code VARCHAR(10) NOT NULL,
  promotion_id BIGINT NOT NULL REFERENCES promotion(id),
  ouverture_at TIMESTAMP NOT NULL,
  expiration_at TIMESTAMP NOT NULL,
  cloture_at TIMESTAMP
);

CREATE TABLE presence (
  id BIGSERIAL PRIMARY KEY,
  session_id BIGINT NOT NULL REFERENCES session(id),
  etudiant_id BIGINT NOT NULL REFERENCES etudiant(id),
  source VARCHAR(20) NOT NULL,
  CONSTRAINT uq_presence_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE exercice (
  id BIGSERIAL PRIMARY KEY,
  session_id BIGINT NOT NULL REFERENCES session(id),
  etudiant_id BIGINT NOT NULL REFERENCES etudiant(id),
  lien VARCHAR(500) NOT NULL,
  statut VARCHAR(30) NOT NULL,
  CONSTRAINT uq_exercice_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE relecture (
  id BIGSERIAL PRIMARY KEY,
  exercice_id BIGINT NOT NULL UNIQUE REFERENCES exercice(id),
  relecteur_id BIGINT NOT NULL REFERENCES etudiant(id),
  note INT,
  commentaire TEXT,
  rendu_at TIMESTAMP
);
