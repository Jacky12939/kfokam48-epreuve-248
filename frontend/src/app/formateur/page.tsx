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
    setLoading(true); setError(null);
    try { setSession(await api.ouvrirSession(titre, promotionId)); }
    catch (err) { setError(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur"); }
    finally { setLoading(false); }
  }
  async function chargerTableau() {
    setLoading(true); setError(null);
    try { setTableau(await api.tableau(promotionId)); }
    catch (err) { setError(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur"); }
    finally { setLoading(false); }
  }
  async function cloturer() {
    if (!session) return;
    setLoading(true); setError(null);
    try { await api.cloturerSession(session.id); setSession(null); alert("Session clôturée."); }
    catch (err) { setError(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur"); }
    finally { setLoading(false); }
  }

  return (
    <main className="max-w-6xl mx-auto px-6 py-10 space-y-8">
      <header>
        <h1 className="text-3xl font-bold tracking-tight">🎓 Espace Formateur</h1>
        <p className="text-slate-600 mt-1">Ouvrir une session et suivre les étudiants.</p>
      </header>

      <section className="rounded-2xl bg-white border border-slate-200 p-6 shadow-sm">
        <h2 className="text-lg font-semibold mb-4">Ouvrir une session</h2>
        <form onSubmit={ouvrirSession} className="grid md:grid-cols-[1fr_140px_auto] gap-3">
          <input type="text" value={titre} onChange={(e) => setTitre(e.target.value)} required placeholder="Titre du cours"
            className="px-4 py-2.5 rounded-xl border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500/40 focus:border-blue-500 transition" />
          <input type="number" value={promotionId} onChange={(e) => setPromotionId(Number(e.target.value))} required placeholder="Promo"
            className="px-4 py-2.5 rounded-xl border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500/40 focus:border-blue-500 transition" />
          <button disabled={loading} className="px-5 py-2.5 rounded-xl bg-blue-600 text-white font-medium hover:bg-blue-700 disabled:opacity-50 transition shadow-md shadow-blue-600/20">
            {loading ? "…" : "Ouvrir"}
          </button>
        </form>
        {session && (
          <div className="mt-5 p-5 rounded-xl bg-gradient-to-br from-emerald-50 to-teal-50 border border-emerald-200 flex items-center justify-between gap-4 flex-wrap">
            <div>
              <p className="text-xs uppercase font-semibold text-emerald-700 mb-1">Code de présence</p>
              <p className="text-4xl font-mono font-bold text-emerald-900 tracking-widest">{session.code}</p>
              <p className="text-xs text-slate-600 mt-1">Expire à {new Date(session.expirationAt).toLocaleTimeString()}</p>
            </div>
            <button onClick={cloturer} className="px-4 py-2 rounded-lg bg-red-600 text-white text-sm font-medium hover:bg-red-700 transition">Clôturer</button>
          </div>
        )}
      </section>

      <section className="rounded-2xl bg-white border border-slate-200 p-6 shadow-sm">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-semibold">Tableau de bord</h2>
          <button onClick={chargerTableau} disabled={loading} className="px-4 py-2 rounded-lg bg-slate-900 text-white text-sm font-medium hover:bg-slate-800 disabled:opacity-50 transition">
            {loading ? "…" : "Rafraîchir"}
          </button>
        </div>
        {tableau.length > 0 ? (
          <div className="overflow-x-auto -mx-2">
            <table className="w-full text-sm">
              <thead className="text-left text-xs uppercase text-slate-500 border-b">
                <tr>
                  <th className="py-3 px-2">Étudiant</th>
                  <th className="py-3 px-2">Présences</th>
                  <th className="py-3 px-2">Exercices</th>
                  <th className="py-3 px-2">Moyenne</th>
                  <th className="py-3 px-2">Relectures en attente</th>
                </tr>
              </thead>
              <tbody>
                {tableau.map((l) => (
                  <tr key={l.etudiantId} className="border-b border-slate-100 hover:bg-slate-50 transition">
                    <td className="py-3 px-2 font-medium">{l.nom}</td>
                    <td className="py-3 px-2">{l.presences}</td>
                    <td className="py-3 px-2">{l.exercicesDeposes}</td>
                    <td className="py-3 px-2">
                      {l.moyenne === null ? <span className="text-slate-400">—</span> : (
                        <span className="font-semibold text-slate-900">{l.moyenne.toFixed(2)}</span>
                      )}
                    </td>
                    <td className="py-3 px-2">
                      {l.relecturesEnAttente > 0 ? (
                        <span className="inline-block px-2 py-0.5 rounded-full text-xs font-semibold bg-amber-100 text-amber-800">{l.relecturesEnAttente}</span>
                      ) : <span className="text-slate-400">—</span>}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-slate-500 text-sm py-8 text-center">Aucune donnée. Cliquez sur Rafraîchir.</p>
        )}
      </section>

      {error && <div className="rounded-xl bg-red-50 border border-red-200 text-red-700 p-4 text-sm">{error}</div>}
    </main>
  );
}