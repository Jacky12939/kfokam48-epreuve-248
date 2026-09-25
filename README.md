# KFOKAM48 — Plateforme de présence et relecture

## Frontend choisi : Next.js

Routage par fichiers + séparation nette des 3 espaces (formateur / étudiant / relecteur).

## Démarrage (3 commandes)

1. `docker compose up -d` — PostgreSQL
2. `cd backend && ./mvnw spring-boot:run` — Backend (port 8080)
3. `cd frontend && npm install && npm run dev` — Frontend (port 3000)

Ouvrir : http://localhost:3000

Données de démo chargées au démarrage : 2 promotions, 4 étudiants.

## Prérequis

- Java 17+
- Node 18+
- Docker + Docker Compose

## API

- Contrat : api/contrat.yaml (OpenAPI 3.0)
- Base : PostgreSQL 16 (Docker)

## Tests

`cd backend && ./mvnw test`

- PresenceConcurrencyTest : race condition #21
- SessionControllerIT : POST /api/sessions (nominal + 400 + 404)

## Analyse

- CDC : docs/CAHIER_DES_CHARGES.md
- Diagrammes : docs/diagrammes/ (Mermaid)
- Journal : docs/JOURNAL.md
