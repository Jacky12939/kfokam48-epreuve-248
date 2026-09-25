# Cahier des charges — Plateforme de présence et de relecture KFOKAM48

**Auteur :** Jacky — KF48-248
**Version :** 1
**Frontend choisi :** Next.js — parce que le routage par fichiers et le rendu serveur simplifient la séparation des 3 espaces (formateur / étudiant / relecteur).

---

## 1. Contexte et objectif

La direction KFOKAM48 veut outiller ses formateurs pour :
- ouvrir une session de cours et générer un code de présence à durée limitée ;
- permettre aux étudiants de marquer leur présence avec ce code ;
- permettre aux étudiants de déposer le lien de leur exercice ;
- assigner chaque exercice à un relecteur pair qui le note et le commente ;
- offrir au formateur un tableau de bord agrégé (présences, exercices déposés, moyenne, relectures en attente).

Objectif : remplacer le pointage manuel et la relecture informelle par un flux numérique traçable.

---

## 2. Acteurs et rôles

| Acteur | Ce qu'il peut faire |
|---|---|
| Formateur | Ouvrir une session, voir son code, consulter le tableau, ajouter une présence manuelle, clôturer la session |
| Étudiant | Marquer sa présence avec un code, déposer le lien de son exercice, voir sa note et son commentaire |
| Relecteur (étudiant assigné) | Consulter l'exercice assigné, le noter (0-20), commenter, corriger tant que la session est ouverte |

---

## 3. Périmètre

**Inclus :**
- Sessions avec code de présence expirant 15 min après ouverture.
- Pointage étudiant par code, blocage 2 min après 5 erreurs.
- Dépôt d'un lien d'exercice par session.
- Assignation automatique d'un relecteur unique parmi les présents.
- Notation (0-20 entier) + commentaire.
- Tableau de bord formateur.

**Exclu :**
- Authentification par mot de passe (Q1 : l'étudiant choisit son nom dans une liste).
- Upload de fichier — on ne stocke qu'un lien.
- Notifications email.
- Gestion administrative des promotions.
- Statistiques avancées, paiement, export PDF.

---

## 4. Exigences fonctionnelles

| Réf | Exigence | Critère d'acceptation | Priorité |
|---|---|---|---|
| EF1 | Le formateur ouvre une session et obtient un code | POST /api/sessions retourne 201 avec { id, code, ouvertureAt, expirationAt } | Must |
| EF2 | L'étudiant marque sa présence avec un code | Code valide → ma présence apparaît dans le tableau du formateur (201) | Must |
| EF3 | L'étudiant dépose le lien de son exercice | POST /api/exercices retourne 201 avec { id, statut:"DEPOSE" } | Must |
| EF4 | Le système assigne un relecteur unique | POST /api/relectures/assigner crée une relecture pour un étudiant présent ≠ déposant | Must |
| EF5 | Le relecteur note et commente | POST /api/relectures/{id} retourne 200 avec la note enregistrée | Must |
| EF6 | Le formateur voit le tableau | GET /api/tableau?promotionId= retourne les stats par étudiant | Must |
| EF7 | Le relecteur peut corriger tant que la session est ouverte | Deuxième POST /api/relectures/{id} écrase la note (200) | Should |
| EF8 | L'étudiant relu voit sa note sans le nom du relecteur | GET /api/exercices/{id} retourne { note, commentaire } sans relecteurId | Should |
| EF9 | Le formateur ajoute une présence manuellement | POST /api/presences avec source:"FORMATEUR" → 201 | Should |

---

## 5. Exigences non fonctionnelles

| Réf | Exigence | Comment on la vérifie |
|---|---|---|
| ENF1 | Temps de réponse < 500 ms en charge normale | Test manuel sur 50 requêtes consécutives |
| ENF2 | Utilisable sur mobile (responsive) | Test Chrome DevTools en mode iPhone |
| ENF3 | Toutes les erreurs respectent le format imposé | Test d'intégration sur chaque endpoint |
| ENF4 | Volumétrie cible : 500 étudiants, 20 promotions, 50 sessions | Données de démo chargées au démarrage |
| ENF5 | Aucune stack trace exposée au client | Test d'intégration sur cas d'erreur |

---

## 6. Règles de gestion

| Réf | Règle | Source |
|---|---|---|
| RG1 | Le code de présence expire 15 minutes après l'ouverture de la session | Q2 |
| RG2 | Un étudiant ne peut marquer qu'une seule présence par session | Q5 (implicite) |
| RG3 | Un étudiant ne peut jamais relire son propre exercice | Q5 |
| RG4 | Au bout de 5 erreurs de code, l'étudiant est bloqué 2 minutes | Q4 |
| RG5 | La note est un entier de 0 à 20 | Q9 |
| RG6 | Un seul relecteur par exercice | Q6 |
| RG7 | Le relecteur est choisi au hasard parmi les étudiants présents | Q7 |
| RG8 | Impossible de marquer sa présence après la fin de la session | Q3 |
| RG9 | Le dépôt d'exercice reste possible jusqu'à la clôture par le formateur | Q12 |
| RG10 | Le lien d'exercice est remplaçable tant que personne n'a commencé la relecture | Q13 |
| RG11 | Une présence ajoutée par le formateur porte source = "FORMATEUR" | Q14 |
| RG12 | Une relecture est corrigeable tant que la session n'est pas clôturée | Q10 (voir section 7) |

---

## 7. Zones d'ombre, hypothèses et contradictions tranchées

| Point | Réponse client (Qx) ou hypothèse | Décision retenue | Pourquoi |
|---|---|---|---|
| Contradiction Q10 / Q15 | Q10 : corrigeable tant que la session est ouverte. Q15 : définitive une fois envoyée. | Q10 retenu | Q11 décrit un usage réel (relectures en retard), Q15 est une intention. L'usage prime sur l'intention. La note reste modifiable jusqu'à clôture. |
| Trou non vu : étudiant seul présent | Q7 dit "relecteur choisi parmi les présents". Q5 interdit l'auto-relecture. | Si aucun autre étudiant n'est présent, l'exercice reste EN_ATTENTE et apparaît signalé dans le tableau du formateur. | Sinon le système se contredit : on ne peut pas assigner un relecteur qui n'existe pas. |
| Moyenne d'un étudiant sans relecture rendue | Non couvert par le client | La note vaut null (pas 0), et l'étudiant est exclu du calcul de moyenne tant que sa relecture est en attente. | 0 serait injuste ; null exprime "pas encore évalué". |
| Correction après clôture | Q10 implique non | Refusée avec 409 SESSION_CLOTUREE | Sans cela, Q10 n'a plus de sens. |
| Relecture assignée à un étudiant qui n'a pas déposé | Non couvert | Autorisé : le relecteur n'a pas besoin d'avoir déposé son propre exercice. | Q7 ne l'exige pas, et cela évite de bloquer le flux. |

---

## 8. Contraintes techniques

**Backend (imposé) :** Java 17+, Maven, wrapper mvnw commité, Spring Boot, contrat api/contrat.yaml respecté à la lettre, séparation controller/service/repository, DTO (jamais d'entité JPA en JSON), @RestControllerAdvice, Flyway, 2 tests (1 unitaire + 1 intégration).

**Frontend (au choix) :** Next.js, 3 écrans (formateur / étudiant / relecteur), couche API dédiée, états de chargement et d'erreur, aucune règle métier dupliquée.

**Démarrage :** 3 commandes maximum documentées dans le README, testées depuis un clone vierge, avec données de démonstration.

---

## 9. Livrables

- Dépôt GitHub public kfokam48-epreuve-248 avec historique Git lisible.
- Backend Spring Boot complet (contrôleurs, services, repositories, DTO, migrations Flyway).
- Frontend Next.js avec 3 écrans.
- Contrat API complété (api/contrat.yaml).
- Cahier des charges (ce document) + 3 diagrammes Mermaid (+1 bonus).
- Backlog en issues GitHub avec critères d'acceptation.
- Tests unitaire et d'intégration.
- README d'installation + CHANGELOG + JOURNAL.
- SOUMISSION.md téléversé sur la plateforme.

---

## 10. Démarche prévue

1. Analyse : CDC, diagrammes, issues, contrat → [JALON] analyse.
2. v0.1 : uniquement les issues Must (backend + frontend minimal + tests) → [JALON] v0.1.
3. Enveloppe (demandée au surveillant) : bug + évolution, issues ouvertes, migration Flyway versionnée, contrat mis à jour, docs corrigés.
4. v1.0 : frontend complet, CHANGELOG, README testé depuis un clone vierge, backlog trié → [JALON] v1.0.
5. Soumission : hash final, SOUMISSION.md, téléversement avant 18h00.

**Definition of Done d'un ticket :** code mergé sur main, issue fermée par un commit Closes #N, tests passants, contrat API respecté, aucune règle métier dupliquée côté front.
