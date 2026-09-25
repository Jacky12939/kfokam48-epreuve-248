"use client";

import { useState } from "react";
import { api, ApiError, type ExerciceDetail } from "@/lib/api";

export default function RelecteurPage() {
  const [exerciceId, setExerciceId] = useState(1);
  const [exercice, setExercice] = useState<ExerciceDetail | null>(null);

  const [relectureId, setRelectureId] = useState(1);
  const [note, setNote] = useState(15);
  const [commentaire, setCommentaire] = useState("");

  const [msg, setMsg] = useState<string | null>(null);
  const [err, setErr] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function chargerExercice() {
    setMsg(null);
    setErr(null);
    setLoading(true);
    try {
      const ex = await api.consulterExercice(exerciceId);
      setExercice(ex);
    } catch (e) {
      setErr(e instanceof ApiError ? `${e.code} — ${e.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  async function assigner() {
    setMsg(null);
    setErr(null);
    setLoading(true);
    try {
      const r = await api.assignerRelecteur(exerciceId);
      setRelectureId(r.id);
      setMsg(`Relecture assignée. ID de relecture = ${r.id}`);
    } catch (e) {
      setErr(e instanceof ApiError ? `${e.code} — ${e.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  async function rendre(e: React.FormEvent) {
    e.preventDefault();
    setMsg(null);
    setErr(null);
    setLoading(true);
    try {
      const r = await api.rendreRelecture(relectureId, note, commentaire);
      setMsg(`Relecture rendue. Note ${r.note}/20.`);
    } catch (e) {
      setErr(e instanceof ApiError ? `${e.code} — ${e.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-2xl mx-auto space-y-8">
        <h1 className="text-3xl font-bold">Espace Relecteur</h1>

        <section className="bg-white border rounded-lg p-6 space-y-3">
          <h2 className="text-xl font-semibold">Charger un exercice</h2>
          <div className="flex gap-2 flex-wrap">
            <input
              type="number"
              value={exerciceId}
              onChange={(e) => setExerciceId(Number(e.target.value))}
              className="border rounded px-3 py-2 w-32"
            />
            <button
              onClick={chargerExercice}
              disabled={loading}
              className="bg-slate-700 text-white px-4 py-2 rounded disabled:opacity-50"
            >
              Charger
            </button>
            <button
              onClick={assigner}
              disabled={loading}
              className="bg-purple-600 text-white px-4 py-2 rounded disabled:opacity-50"
            >
              M&apos;assigner comme relecteur
            </button>
          </div>

          {exercice && (
            <div className="mt-3 p-3 bg-slate-50 rounded text-sm">
              <p>Exercice #{exercice.id}</p>
              <p>
                Lien :{" "}
                <a
                  href={exercice.lien}
                  className="text-blue-600 underline"
                  target="_blank"
                  rel="noreferrer"
                >
                  {exercice.lien}
                </a>
              </p>
              <p>Statut : {exercice.statut}</p>
              {exercice.note !== null && (
                <p className="text-green-700">Note déjà rendue : {exercice.note}/20</p>
              )}
            </div>
          )}
        </section>

        <section className="bg-white border rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">Rendre une relecture</h2>
          <form onSubmit={rendre} className="space-y-3">
            <div>
              <label className="block text-sm mb-1">ID de relecture</label>
              <input
                type="number"
                value={relectureId}
                onChange={(e) => setRelectureId(Number(e.target.value))}
                required
                className="border rounded px-3 py-2 w-32"
              />
            </div>
            <div>
              <label className="block text-sm mb-1">Note (0-20)</label>
              <input
                type="number"
                min={0}
                max={20}
                value={note}
                onChange={(e) => setNote(Number(e.target.value))}
                required
                className="border rounded px-3 py-2 w-32"
              />
            </div>
            <div>
              <label className="block text-sm mb-1">Commentaire</label>
              <textarea
                value={commentaire}
                onChange={(e) => setCommentaire(e.target.value)}
                required
                rows={3}
                className="border rounded px-3 py-2 w-full"
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="bg-green-600 text-white px-4 py-2 rounded disabled:opacity-50"
            >
              {loading ? "…" : "Envoyer"}
            </button>
          </form>
        </section>

        {msg && (
          <p className="bg-green-50 border border-green-200 text-green-800 p-3 rounded text-sm">
            {msg}
          </p>
        )}
        {err && (
          <p className="bg-red-50 border border-red-200 text-red-800 p-3 rounded text-sm">
            {err}
          </p>
        )}
      </div>
    </main>
  );
}
