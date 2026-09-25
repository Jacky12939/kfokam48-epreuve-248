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
    setPresenceMsg(null); setPresenceErr(null); setLoading(true);
    try { const p = await api.marquerPresence(code, etudiantId); setPresenceMsg(`✅ Présence marquée (session ${p.sessionId}).`); }
    catch (err) { setPresenceErr(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur"); }
    finally { setLoading(false); }
  }
  async function deposerExercice(e: React.FormEvent) {
    e.preventDefault();
    setExerciceMsg(null); setExerciceErr(null); setLoading(true);
    try { const r = await api.deposerExercice(sessionId, etudiantId, lien); setExerciceMsg(`✅ Exercice déposé (id ${r.id}).`); }
    catch (err) { setExerciceErr(err instanceof ApiError ? `${err.code} — ${err.message}` : "Erreur"); }
    finally { setLoading(false); }
  }

  const inputCls = "w-full px-4 py-2.5 rounded-xl border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500/40 focus:border-blue-500 transition";

  return (
    <main className="max-w-3xl mx-auto px-6 py-10 space-y-8">
      <header>
        <h1 className="text-3xl font-bold tracking-tight">📚 Espace Étudiant</h1>
        <p className="text-slate-600 mt-1">Marquez votre présence et déposez votre exercice.</p>
      </header>

      <section className="rounded-2xl bg-white border border-slate-200 p-6 shadow-sm">
        <label className="block text-sm font-medium mb-2">Mon identifiant étudiant</label>
        <input type="number" value={etudiantId} onChange={(e) => setEtudiantId(Number(e.target.value))}
          className="w-32 px-4 py-2.5 rounded-xl border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500/40 focus:border-blue-500 transition" />
      </section>

      <section className="rounded-2xl bg-white border border-slate-200 p-6 shadow-sm">
        <h2 className="text-lg font-semibold mb-4">🕐 Marquer ma présence</h2>
        <form onSubmit={marquerPresence} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">Code de présence</label>
            <input type="text" value={code} onChange={(e) => setCode(e.target.value)} required placeholder="123456"
              className={`${inputCls} font-mono text-lg tracking-widest text-center`} />
          </div>
          <button disabled={loading} className="w-full px-5 py-2.5 rounded-xl bg-emerald-600 text-white font-medium hover:bg-emerald-700 disabled:opacity-50 transition shadow-md shadow-emerald-600/20">
            {loading ? "…" : "Marquer ma présence"}
          </button>
        </form>
        {presenceMsg && <div className="mt-4 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm">{presenceMsg}</div>}
        {presenceErr && <div className="mt-4 p-3 rounded-xl bg-red-50 border border-red-200 text-red-700 text-sm">{presenceErr}</div>}
      </section>

      <section className="rounded-2xl bg-white border border-slate-200 p-6 shadow-sm">
        <h2 className="text-lg font-semibold mb-4">📎 Déposer mon exercice</h2>
        <form onSubmit={deposerExercice} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">ID de session</label>
            <input type="number" value={sessionId} onChange={(e) => setSessionId(Number(e.target.value))} required className={`${inputCls} w-32`} />
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Lien de l&apos;exercice</label>
            <input type="url" value={lien} onChange={(e) => setLien(e.target.value)} required placeholder="https://github.com/..." className={inputCls} />
          </div>
          <button disabled={loading} className="w-full px-5 py-2.5 rounded-xl bg-blue-600 text-white font-medium hover:bg-blue-700 disabled:opacity-50 transition shadow-md shadow-blue-600/20">
            {loading ? "…" : "Déposer l'exercice"}
          </button>
        </form>
        {exerciceMsg && <div className="mt-4 p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm">{exerciceMsg}</div>}
        {exerciceErr && <div className="mt-4 p-3 rounded-xl bg-red-50 border border-red-200 text-red-700 text-sm">{exerciceErr}</div>}
      </section>
    </main>
  );
}