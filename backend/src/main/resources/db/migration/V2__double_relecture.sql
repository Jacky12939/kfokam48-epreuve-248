-- V2 (issue #22) : passage d'un seul relecteur à DEUX relecteurs par exercice.
-- Ne modifie PAS V1. Doit survivre aux données existantes.

-- 1. Supprimer la contrainte UNIQUE sur exercice_id seul.
ALTER TABLE relecture DROP CONSTRAINT IF EXISTS relecture_exercice_id_key;

-- 2. Ajouter une contrainte UNIQUE sur (exercice_id, relecteur_id) :
--    au plus un rendu par relecteur et par exercice.
ALTER TABLE relecture
  ADD CONSTRAINT uq_relecture_exercice_relecteur UNIQUE (exercice_id, relecteur_id);

-- 3. Index pour accélérer la recherche des relectures d'un exercice.
CREATE INDEX IF NOT EXISTS idx_relecture_exercice ON relecture(exercice_id);
