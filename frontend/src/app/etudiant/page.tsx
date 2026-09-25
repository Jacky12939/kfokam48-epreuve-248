"use client";

import { useState } from "react";
import { api, ApiError } from "@/lib/api";

export default function EtudiantPage() {
  const [code, setCode] = useState("");
  const [etudiantId, setEtudiantId] = useState(1);
  const [presenceMsg, setPresenceMsg] = useState<string | null>(null);
  const [presenceErr, setPresenceErr] = useState<string | null>(null);

  const [sessionId, setSessionId] = useState(1);
  const [lien, setLien] = useState("");
  const [exerciceMsg, setExerciceMsg] = useState<string | null>(null);
  const [exerciceErr, setExerciceErr] = useState<string | null>(null);

  const [loading, setLoading] = useState(false);

  async function marquerPresence(e: React.FormEvent) {
    e.preventDefault();
    setPresenceMsg(null);
    setPresenceErr(null);
    setLoading(true);
    try {
      const p = await api.marquerPresence(code, etudiantId);
      setPresenceMsg(`Présence marquée (session ${p.sessionId}, source ${p.source}).`);
    } catch (err) {
      setPresenceErr(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  async function deposerExercice(e: React.FormEvent) {
    e.preventDefault();
    setExerciceMsg(null);
    setExerciceErr(null);
    setLoading(true);
    try {
      const r = await api.deposerExercice(sessionId, etudiantId, lien);
      setExerciceMsg(`Exercice déposé (id ${r.id}, statut ${r.statut}).`);
    } catch (err) {
      setExerciceErr(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-2xl mx-auto space-y-8">
        <h1 className="text-3xl font-bold">Espace Étudiant</h1>

        <section className="bg-white border rounded-lg p-6">
          <label className="block text-sm mb-1">Mon identifiant étudiant</label>
          <input
            type="number"
            value={etudiantId}
            onChange={(e) => setEtudiantId(Number(e.target.value))}
            className="border rounded px-3 py-2 w-32"
          />
        </section>

        <section className="bg-white border rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">Marquer ma présence</h2>
          <form onSubmit={marquerPresence} className="space-y-3">
            <div>
              <label className="block text-sm mb-1">Code de présence</label>
              <input
                type="text"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                required
                className="border rounded px-3 py-2 w-full font-mono"
                placeholder="123456"
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 disabled:opacity-50"
            >
              {loading ? "…" : "Marquer"}
            </button>
          </form>
          {presenceMsg && (
            <p className="mt-3 text-sm text-green-700 bg-green-50 p-2 rounded">{presenceMsg}</p>
          )}
          {presenceErr && (
            <p className="mt-3 text-sm text-red-700 bg-red-50 p-2 rounded">{presenceErr}</p>
          )}
        </section>

        <section className="bg-white border rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">Déposer mon exercice</h2>
          <form onSubmit={deposerExercice} className="space-y-3">
            <div>
              <label className="block text-sm mb-1">ID de session</label>
              <input
                type="number"
                value={sessionId}
                onChange={(e) => setSessionId(Number(e.target.value))}
                required
                className="border rounded px-3 py-2 w-32"
              />
            </div>
            <div>
              <label className="block text-sm mb-1">Lien de l&apos;exercice</label>
              <input
                type="url"
                value={lien}
                onChange={(e) => setLien(e.target.value)}
                required
                className="border rounded px-3 py-2 w-full"
                placeholder="https://github.com/..."
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? "…" : "Déposer"}
            </button>
          </form>
          {exerciceMsg && (
            <p className="mt-3 text-sm text-green-700 bg-green-50 p-2 rounded">{exerciceMsg}</p>
          )}
          {exerciceErr && (
            <p className="mt-3 text-sm text-red-700 bg-red-50 p-2 rounded">{exerciceErr}</p>
          )}
        </section>
      </div>
    </main>
  );
}
