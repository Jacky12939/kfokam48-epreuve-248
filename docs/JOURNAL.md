# Journal de bord — KFOKAM48

## Étape 1 — Analyse et conception

**Fait :** cahier des charges complet (9 EF, 12 RG, 10 sections), 4 diagrammes Mermaid (D1 cas d'usage, D2 classes, D3 séquence, D4 états en bonus), 9 issues créées sur GitHub, contrat API complété (5 opérations imposées + 3 complémentaires), commit [JALON] analyse poussé.

**Bloqué :** ~15 min sur la contradiction Q10 / Q15, tranchée en faveur de Q10 (l'usage réel décrit par Q11 prime sur l'intention de Q15). Décision documentée en section 7 du CDC.

**IA :** m'a proposé une vingtaine d'issues, j'en ai retenu 9. Les autres étaient des tâches techniques ("créer le contrôleur X"), pas des résultats utilisateur. Vérifié en relisant chaque titre : est-ce que le client le comprendrait ?

## Étape 3 — Enveloppe (bug #21 + changement #22)

**Bug #21 — Race condition sur POST /api/presences.** Deux appels simultanés pour le même (session, étudiant) renvoyaient une 500 au lieu de 409. Issue ouverte AVANT toute correction. Test TDD `PresenceConcurrencyTest` écrit en premier : 1 succès + 3 DataIntegrityViolationException (rouge). Correction : `saveAndFlush()` + catch `DataIntegrityViolationException` → `DejaPresentException`. Test vert. Branche `fix/race-presence`, commit référençant #21.

**Changement #22 — Double relecture + note provisoire.** Casse RG6. Traité dans une branche SÉPARÉE `feat/double-relecture`. Ordre respecté : CDC v2 en premier (commit `docs: mise à jour CDC et diagrammes suite à l'enveloppe`), puis contrat API v2, puis migration V2 (jamais V1 modifiée), puis code. Décision : fallback 1 seul relecteur si < 3 présents (§7).

**Sacrifice de périmètre assumé.** Le double relecture est un Must tardif → je retire du périmètre **l'affichage du nom des relecteurs, y compris pour le formateur** (RG15). Un périmètre réduit et assumé vaut mieux qu'un périmètre non tenu.

**Bloqué :** 8 min sur `BindException port 8080` (instance fantôme) → `pkill -f spring-boot`. 5 min sur heredoc qui cassait à cause des backticks Mermaid → bascule sur nano.

**IA :** m'a proposé 6 variantes pour le calcul de moyenne. J'ai retenu moyenne arithmétique arrondie à 2 décimales, avec `noteProvisoire = (nb rendues < nb assignées)`. Vérifié manuellement : 14 puis 18 → 16.0.
