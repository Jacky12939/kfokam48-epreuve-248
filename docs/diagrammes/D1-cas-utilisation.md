# D1 — Diagramme de cas d'utilisation

Acteurs : Formateur, Étudiant, Relecteur (étudiant assigné).

```mermaid
graph LR
  F[Formateur] --> UC1[Ouvrir une session]
  F --> UC2[Voir le tableau de bord]
  F --> UC3[Ajouter une présence manuelle]
  F --> UC4[Clôturer une session]

  E[Étudiant] --> UC5[Marquer sa présence avec un code]
  E --> UC6[Déposer le lien de son exercice]
  E --> UC7[Voir sa note et son commentaire]

  R[Relecteur] --> UC8[Consulter un exercice assigné]
  R --> UC9[Noter et commenter]
  R --> UC10[Corriger tant que la session est ouverte]
Traçabilité
UC1 → EF1, RG1

UC2 → EF6

UC3 → EF9, RG11

UC4 → RG12

UC5 → EF2, RG1, RG2, RG4, RG8

UC6 → EF3, RG9, RG10

UC7 → EF8

UC8 → EF4, RG3, RG6, RG7

UC9 → EF5, RG5

UC10 → EF7, RG12
