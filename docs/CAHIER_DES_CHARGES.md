# Cahier des charges — Plateforme de présence et de relecture KFOKAM48

**Auteur :** Jacky — KF48-248
**Version :** 2 (mise à jour suite enveloppe étape 3, issue #22)
**Frontend choisi :** Next.js — routage par fichiers, séparation nette des 3 espaces.

---

## 1. Contexte et objectif

Outiller la formation KFOKAM48 pour :
- ouvrir une session et générer un code de présence à durée limitée ;
- marquer sa présence avec ce code ;
- déposer le lien d'un exercice ;
- **assigner chaque exercice à DEUX relecteurs pairs** (v2, issue #22) ;
- **calculer une moyenne des deux notes ; marquer la note provisoire si un seul a rendu** (v2) ;
- offrir au formateur un tableau de bord agrégé.

---

## 2. Acteurs et rôles

| Acteur | Ce qu'il peut faire |
|---|---|
| Formateur | Ouvrir / clôturer une session, voir le tableau, ajouter une présence manuelle |
| Étudiant | Marquer sa présence, déposer son exercice, voir sa note (définitive ou provisoire) |
| Relecteur | Consulter un exercice assigné, noter, commenter, corriger tant que la session n'est pas clôturée |

---

## 3. Périmètre

**Inclus :**
- Sessions avec code de présence expirant 15 min après ouverture.
- Pointage étudiant par code, blocage 2 min après 5 erreurs.
- Dépôt d'un lien d'exercice par session.
- **Assignation automatique de DEUX relecteurs distincts parmi les présents (v2)**.
- **Note = moyenne des deux relectures, ou note provisoire si une seule rendue (v2)**.
- Tableau de bord formateur.

**Exclu (inchangé) :**
- Authentification par mot de passe, upload de fichier, notifications email.
- Gestion administrative des promotions, statistiques avancées, paiement, export PDF.

**Exclu v2 — sacrifice assumé après issue #22 (voir section 7) :**
- **L'affichage du nom des relecteurs, même pour le formateur.** L'anonymat s'étend à tout le monde ; le formateur ne voit que le nombre de relectures rendues.

---

## 4. Exigences fonctionnelles

| Réf | Exigence | Critère d'acceptation | Priorité |
|---|---|---|---|
| EF1 | Le formateur ouvre une session et obtient un code | POST /api/sessions retourne 201 avec { id, code, ouvertureAt, expirationAt } | Must |
| EF2 | L'étudiant marque sa présence avec un code | Code valide → 201 ; code inconnu → 400 ; expiré → 410 ; déjà présent → 409 | Must |
| EF3 | L'étudiant dépose le lien de son exercice | POST /api/exercices retourne 201 avec { id, statut } | Must |
| EF4 | **Deux relecteurs distincts sont assignés** | POST /api/relectures/assigner retourne les 2 relecteurs, distincts, ≠ déposant | Must |
| EF5 | Les relecteurs notent et commentent | POST /api/relectures/{id} retourne 200 | Must |
| EF6 | Le formateur voit le tableau | GET /api/tableau?promotionId= retourne les stats | Must |
| EF7 | **La note retenue est la moyenne des deux** | GET /api/exercices/{id} retourne moyenne + noteProvisoire | Must |
| EF8 | L'étudiant relu voit sa note (sans nom de relecteur) | GET /api/exercices/{id} ne contient aucun relecteurId | Should |
| EF9 | Le formateur ajoute une présence manuellement | POST /api/presences avec source: "FORMATEUR" | Should |

---

## 5. Exigences non fonctionnelles

| Réf | Exigence | Comment on la vérifie |
|---|---|---|
| ENF1 | Temps de réponse < 500 ms en charge normale | Test manuel sur 50 requêtes |
| ENF2 | Utilisable sur mobile (responsive) | Test Chrome DevTools |
| ENF3 | Toutes les erreurs respectent le format imposé | Test d'intégration par endpoint |
| ENF4 | Volumétrie : 500 étudiants, 20 promotions, 50 sessions | Données de démo chargées au démarrage |
| ENF5 | Aucune stack trace exposée au client | Test d'intégration sur cas d'erreur |
| ENF6 | **La migration V2 doit réussir sur une base déjà peuplée** | Test manuel : données V1 → migration → V2 → succès |

---

## 6. Règles de gestion

| Réf | Règle | Source |
|---|---|---|
| RG1 | Le code de présence expire 15 minutes après l'ouverture | Q2 |
| RG2 | Un étudiant ne peut marquer qu'une seule présence par session | Q5 (implicite) |
| RG3 | Un étudiant ne peut jamais relire son propre exercice | Q5 |
| RG4 | Au bout de 5 erreurs de code, l'étudiant est bloqué 2 minutes | Q4 |
| RG5 | La note est un entier de 0 à 20 | Q9 |
| **RG6** | **Chaque exercice est assigné à DEUX relecteurs distincts** | **Enveloppe #22 (remplace Q6)** |
| RG7 | Les relecteurs sont choisis au hasard parmi les étudiants présents | Q7 |
| RG8 | Impossible de marquer sa présence après la fin de la session | Q3 |
| RG9 | Le dépôt d'exercice reste possible jusqu'à la clôture | Q12 |
| RG10 | Le lien d'exercice est remplaçable tant que personne n'a relu | Q13 |
| RG11 | Une présence ajoutée par le formateur porte source = "FORMATEUR" | Q14 |
| RG12 | Une relecture est corrigeable tant que la session n'est pas clôturée | Q10 |
| **RG13** | **La note finale = moyenne(arrondie 2 déc.) des 2 notes rendues** | **Enveloppe #22** |
| **RG14** | **Si un seul relecteur a rendu, la note affichée est provisoire** | **Enveloppe #22** |
| **RG15** | **Le nom des relecteurs n'est jamais exposé, à personne** | **Sacrifice assumé (voir §7)** |

---

## 7. Zones d'ombre, hypothèses et contradictions tranchées

### 7.1 Décisions v1 (rappel)

| Point | Décision | Pourquoi |
|---|---|---|
| Q10 vs Q15 | Q10 (corrigeable jusqu'à clôture) | Usage réel > intention |
| Étudiant seul présent | Exercice reste EN_ATTENTE | Auto-relecture interdite |
| Moyenne sans relecture | null (pas 0) | "Pas encore évalué" ≠ "0/20" |

### 7.2 Décisions v2 — conséquences de l'enveloppe #22

| Point | Décision | Pourquoi |
|---|---|---|
| **Double relecture** | 2 relecteurs distincts, tirés au hasard parmi les présents, tous ≠ déposant | Enveloppe |
| **Note provisoire** | Champ booléen `noteProvisoire` exposé par l'API ; moyenne provisoire = note du seul rendu | Enveloppe |
| **Cas dégénéré : moins de 3 présents** | Si moins de 3 présents (déposant inclus), **un seul relecteur est assigné** et la note est provisoire jusqu'à clôture | Contrainte RG3 + RG7 : impossible de trouver 2 relecteurs ≠ déposant avec < 3 présents. Décision documentée. |
| **Sacrifice de périmètre** | On **retire du périmètre** l'affichage des noms de relecteurs, y compris pour le formateur (RG15) | Libère la bande passante pour le nouveau Must. Un périmètre réduit et assumé vaut mieux qu'un périmètre non tenu (règle de l'enveloppe). |
| **Migration en place** | On ajoute une migration **V2**, jamais modifiée V1. La base existante doit survivre | Enveloppe + B5 |

---

## 8. Contraintes techniques

**Backend (imposé) :** Java 17+, Maven, `mvnw`, Spring Boot, contrat `api/contrat.yaml` respecté, couches controller/service/repository, DTO, `@RestControllerAdvice`, Flyway, 2 tests (1 unitaire + 1 intégration).

**Frontend :** Next.js, 3 écrans, couche API dédiée, états loading/error, aucune règle métier dupliquée.

**Démarrage :** `docker compose up` + `./mvnw spring-boot:run` + `npm run dev`, testés depuis un clone vierge, données de démo.

---

## 9. Livrables

- Dépôt GitHub public `kfokam48-epreuve-248`.
- Backend Spring Boot + migrations Flyway (V1 + V2).
- Frontend Next.js.
- Contrat API complété (v2).
- CDC v2 + 4 diagrammes Mermaid.
- Backlog en issues GitHub (10 issues, dont #22).
- Tests unitaires + intégration.
- README + CHANGELOG + JOURNAL.
- SOUMISSION.md.

---

## 10. Démarche prévue

1. Analyse (v1) → `[JALON] analyse`
2. v0.1 (Must + Should) → `[JALON] v0.1`
3. Enveloppe : bug #21 corrigé (test TDD), changement #22 (double relecture) traité en **branche séparée** avec migration V2, contrat API et CDC mis à jour
4. v1.0 → `[JALON] v1.0`
5. Soumission

**Definition of Done :** code mergé sur main, issue fermée par `Closes #N`, tests passants, contrat API respecté, docs à jour.
