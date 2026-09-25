// Couche API centralisée (F3) — aucun fetch ailleurs dans l'app.

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export class ApiError extends Error {
  code: string;
  status: number;
  constructor(code: string, message: string, status: number) {
    super(message);
    this.code = code;
    this.status = status;
  }
}

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options?.headers ?? {}),
    },
    cache: "no-store",
  });

  if (!res.ok) {
    let code = "ERREUR_INCONNUE";
    let message = `Erreur ${res.status}`;
    try {
      const body = await res.json();
      if (body && typeof body === "object" && "code" in body) {
        code = body.code;
        message = body.message;
      }
    } catch {
      // corps non-JSON
    }
    throw new ApiError(code, message, res.status);
  }

  if (res.status === 204) return undefined as T;
  return (await res.json()) as T;
}

// ============ Types ============
export type SessionResponse = {
  id: number;
  code: string;
  ouvertureAt: string;
  expirationAt: string;
};

export type PresenceResponse = {
  id: number;
  sessionId: number;
  etudiantId: number;
  source: string;
};

export type ExerciceResponse = {
  id: number;
  statut: string;
};

export type ExerciceDetail = {
  id: number;
  sessionId: number;
  etudiantId: number;
  lien: string;
  statut: string;
  note: number | null;
  commentaire: string | null;
  renduAt: string | null;
};

export type TableauLigne = {
  etudiantId: number;
  nom: string;
  presences: number;
  exercicesDeposes: number;
  moyenne: number | null;
  relecturesEnAttente: number;
};

export type AssignationResponse = {
  id: number;
  exerciceId: number;
  relecteurId: number;
};

export type RelectureResponse = {
  id: number;
  exerciceId: number;
  note: number;
  commentaire: string;
  renduAt: string;
};

// ============ Endpoints ============
export const api = {
  ouvrirSession: (titre: string, promotionId: number) =>
    request<SessionResponse>("/api/sessions", {
      method: "POST",
      body: JSON.stringify({ titre, promotionId }),
    }),

  cloturerSession: (sessionId: number) =>
    request<{ id: number; titre: string; clotureAt: string }>(
      `/api/sessions/${sessionId}/cloturer`,
      { method: "POST" }
    ),

  marquerPresence: (code: string, etudiantId: number) =>
    request<PresenceResponse>("/api/presences", {
      method: "POST",
      body: JSON.stringify({ code, etudiantId }),
    }),

  presenceManuelle: (sessionId: number, etudiantId: number) =>
    request<PresenceResponse>("/api/presences", {
      method: "POST",
      body: JSON.stringify({ sessionId, etudiantId, source: "FORMATEUR" }),
    }),

  deposerExercice: (sessionId: number, etudiantId: number, lien: string) =>
    request<ExerciceResponse>("/api/exercices", {
      method: "POST",
      body: JSON.stringify({ sessionId, etudiantId, lien }),
    }),

  consulterExercice: (id: number) =>
    request<ExerciceDetail>(`/api/exercices/${id}`),

  assignerRelecteur: (exerciceId: number) =>
    request<AssignationResponse>("/api/relectures/assigner", {
      method: "POST",
      body: JSON.stringify({ exerciceId }),
    }),

  rendreRelecture: (id: number, note: number, commentaire: string) =>
    request<RelectureResponse>(`/api/relectures/${id}`, {
      method: "POST",
      body: JSON.stringify({ note, commentaire }),
    }),

  tableau: (promotionId: number) =>
    request<TableauLigne[]>(`/api/tableau?promotionId=${promotionId}`),
};
