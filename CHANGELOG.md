# Changelog

## [1.0.0] - 2026-09-25

### Added
- POST /api/sessions : ouvrir une session avec code à 15 min (EF1)
- POST /api/presences : marquer sa présence (EF2)
- POST /api/exercices : déposer un exercice (EF3)
- POST /api/relectures/assigner : assigner des relecteurs (EF4)
- POST /api/relectures/{id} : rendre une relecture (EF5)
- GET /api/tableau : tableau formateur (EF6)
- GET /api/exercices/{id} : consultation sans relecteur (EF8)
- POST /api/sessions/{id}/cloturer : clôture (EF7)
- 3 écrans frontend (F2) + couche API centralisée (F3)

### Changed (v2, enveloppe étape 3)
- **Breaking (#22)** : double relecture par exercice, note = moyenne, note provisoire si une seule relecture rendue
- Contrat API v2.0.0 : GET /api/exercices/{id} expose `noteProvisoire:boolean`
- Migration V2 : DROP UNIQUE(exercice_id), ADD UNIQUE(exercice_id, relecteur_id)

### Fixed
- **#21** — Race condition sur POST /api/presences : 2 appels simultanés renvoyaient 500 au lieu de 409
  - Cause : findBy + save non atomique
  - Fix : `saveAndFlush` + catch `DataIntegrityViolationException` → `DejaPresentException`

### Removed (sacrifice de périmètre v2)
- Affichage du nom des relecteurs (RG15)
