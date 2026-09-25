"use client";

import { useState } from "react";
import { api, ApiError, type SessionResponse, type TableauLigne } from "@/lib/api";

export default function FormateurPage() {
  const [titre, setTitre] = useState("");
  const [promotionId, setPromotionId] = useState(1);
  const [session, setSession] = useState<SessionResponse | null>(null);
  const [tableau, setTableau] = useState<TableauLigne[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function ouvrirSession(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const s = await api.ouvrirSession(titre, promotionId);
      setSession(s);
    } catch (err) {
      setError(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  async function chargerTableau() {
    setLoading(true);
    setError(null);
    try {
      const data = await api.tableau(promotionId);
      setTableau(data);
    } catch (err) {
      setError(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  async function cloturer() {
    if (!session) return;
    setLoading(true);
    setError(null);
    try {
      await api.cloturerSession(session.id);
      alert("Session clôturée.");
      setSession(null);
    } catch (err) {
      setError(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur inconnue");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-4xl mx-auto space-y-8">
        <h1 className="text-3xl font-bold">Espace Formateur</h1>

        <section className="bg-white border rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">Ouvrir une session</h2>
          <form onSubmit={ouvrirSession} className="flex flex-wrap gap-3 items-end">
            <div className="flex-1 min-w-[200px]">
              <label className="block text-sm mb-1">Titre</label>
              <input
                type="text"
                value={titre}
                onChange={(e) => setTitre(e.target.value)}
                required
                className="w-full border rounded px-3 py-2"
                placeholder="Cours Java"
              />
            </div>
            <div className="w-40">
              <label className="block text-sm mb-1">Promotion ID</label>
              <input
                type="number"
                value={promotionId}
                onChange={(e) => setPromotionId(Number(e.target.value))}
                required
                className="w-full border rounded px-3 py-2"
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
            >
              {loading ? "…" : "Ouvrir"}
            </button>
          </form>

          {session && (
            <div className="mt-4 p-4 bg-green-50 border border-green-200 rounded">
              <p className="text-sm text-slate-600">Code de présence :</p>
              <p className="text-3xl font-mono font-bold">{session.code}</p>
              <p className="text-xs text-slate-500 mt-1">
                Expire à {new Date(session.expirationAt).toLocaleTimeString()}
              </p>
              <button
                onClick={cloturer}
                className="mt-3 text-sm text-red-600 underline"
              >
                Clôturer la session
              </button>
            </div>
          )}
        </section>

        <section className="bg-white border rounded-lg p-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-semibold">Tableau de bord</h2>
            <button
              onClick={chargerTableau}
              disabled={loading}
              className="bg-slate-700 text-white px-4 py-2 rounded hover:bg-slate-800 disabled:opacity-50"
            >
              {loading ? "…" : "Rafraîchir"}
            </button>
          </div>

          {tableau.length > 0 ? (
            <table className="w-full text-left text-sm border-collapse">
              <thead>
                <tr className="border-b">
                  <th className="py-2">Étudiant</th>
                  <th className="py-2">Présences</th>
                  <th className="py-2">Exercices</th>
                  <th className="py-2">Moyenne</th>
                  <th className="py-2">Relectures en attente</th>
                </tr>
              </thead>
              <tbody>
                {tableau.map((l) => (
                  <tr key={l.etudiantId} className="border-b">
                    <td className="py-2">{l.nom}</td>
                    <td className="py-2">{l.presences}</td>
                    <td className="py-2">{l.exercicesDeposes}</td>
                    <td className="py-2">
                      {l.moyenne === null ? "—" : l.moyenne.toFixed(2)}
                    </td>
                    <td className="py-2">{l.relecturesEnAttente}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p className="text-slate-500 text-sm">Aucune donnée. Cliquez sur Rafraîchir.</p>
          )}
        </section>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 p-3 rounded text-sm">
            {error}
          </div>
        )}
      </div>
    </main>
  );
}
