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
    setMsg(null); setErr(null); setLoading(true);
    try { setExercice(await api.consulterExercice(exerciceId)); }
    catch (e) { setErr(e instanceof ApiError ? `${e.code} — ${e.message}` : "Erreur"); }
    finally { setLoading(false); }
  }
  async function assigner() {
    setMsg(null); setErr(null); setLoading(true);
    try { const r = await api.assignerRelecteur(exerciceId); setMsg(`✅ Relecture assignée. ID = ${r.id}`); }
    catch (e) { setErr(e instanceof ApiError ? `${e.code} — ${e.message}` : "Erreur"); }
    finally { setLoading(false); }
  }
  async function rendre(e: React.FormEvent) {
    e.preventDefault();
    setMsg(null); setErr(null); setLoading(true);
    try { const r = await api.rendreRelecture(relectureId, note, commentaire); setMsg(`✅ Relecture rendue. Note ${r.note}/20.`); }
    catch (e) { setErr(e instanceof ApiError ? `${e.code} — ${e.message}` : "Erreur"); }
    finally { setLoading(false); }
  }

  const inputCls = "w-full px-4 py-2.5 rounded-xl border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500/40 focus:border-blue-500 transition";

  return (
    <main className="max-w-3xl mx-auto px-6 py-10 space-y-8">
      <header>
        <h1 className="text-3xl font-bold tracking-tight">✍️ Espace Relecteur</h1>
        <p className="text-slate-600 mt-1">Noter et commenter un exercice assigné.</p>
      </header>

      <section className="rounded-2xl bg-white border border-slate-200 p-6 shadow-sm space-y-4">
        <h2 className="text-lg font-semibold">Charger un exercice</h2>
        <div className="flex gap-3 flex-wrap">
          <input type="number" value={exerciceId} onChange={(e) => setExerciceId(Number(e.target.value))} className="w-32 px-4 py-2.5 rounded-xl border border-slate-300 focus:outline-none focus:ring-2 focus:ring-blue-500/40" />
          <button onClick={chargerExercice} disabled={loading} className="px-4 py-2.5 rounded-xl bg-slate-900 text-white text-sm font-medium hover:bg-slate-800 disabled:opacity-50 transition">Charger</button>
          <button onClick={assigner} disabled={loading} className="px-4 py-2.5 rounded-xl bg-purple-600 text-white text-sm font-medium hover:bg-purple-700 disabled:opacity-50 transition shadow-md shadow-purple-600/20">M&apos;assigner</button>
        </div>
        {exercice && (
          <div className="p-4 rounded-xl bg-slate-50 border border-slate-200 text-sm space-y-1">
            <p className="font-semibold">Exercice #{exercice.id}</p>
            <p>Lien : <a href={exercice.lien} target="_blank" rel="noreferrer" className="text-blue-600 underline break-all">{exercice.lien}</a></p>
            <p>Statut : <span className="font-medium">{exercice.statut}</span></p>
            {exercice.note !== null && (
              <p className="text-emerald-700 font-medium">
                Note : {exercice.note}/20 {exercice.noteProvisoire && <span className="text-amber-700">(provisoire)</span>}
              </p>
            )}
          </div>
        )}
      </section>

      <section className="rounded-2xl bg-white border border-slate-200 p-6 shadow-sm">
        <h2 className="text-lg font-semibold mb-4">Rendre une relecture</h2>
        <form onSubmit={rendre} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">ID de relecture</label>
            <input type="number" value={relectureId} onChange={(e) => setRelectureId(Number(e.target.value))} required className={`${inputCls} w-32`} />
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Note (0-20)</label>
            <input type="number" min={0} max={20} value={note} onChange={(e) => setNote(Number(e.target.value))} required className={`${inputCls} w-32`} />
          </div>
          <div>
            <label className="block text-sm font-medium mb-2">Commentaire</label>
            <textarea value={commentaire} onChange={(e) => setCommentaire(e.target.value)} required rows={4} className={inputCls} />
          </div>
          <button disabled={loading} className="w-full px-5 py-2.5 rounded-xl bg-emerald-600 text-white font-medium hover:bg-emerald-700 disabled:opacity-50 transition shadow-md shadow-emerald-600/20">
            {loading ? "…" : "Envoyer la relecture"}
          </button>
        </form>
      </section>

      {msg && <div className="rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 p-4 text-sm">{msg}</div>}
      {err && <div className="rounded-xl bg-red-50 border border-red-200 text-red-700 p-4 text-sm">{err}</div>}
    </main>
  );
}