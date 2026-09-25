import Link from "next/link";

export default function Home() {
  return (
    <main className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-3xl mx-auto">
        <h1 className="text-3xl font-bold mb-2">KFOKAM48 — Présence & Relecture</h1>
        <p className="text-slate-600 mb-8">
          Choisissez votre espace. Aucune authentification (Q1).
        </p>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Link
            href="/formateur"
            className="bg-white border rounded-lg p-6 hover:shadow-md transition"
          >
            <h2 className="text-xl font-semibold mb-1">Formateur</h2>
            <p className="text-sm text-slate-600">
              Ouvrir une session, voir le tableau de bord, ajouter une présence.
            </p>
          </Link>
          <Link
            href="/etudiant"
            className="bg-white border rounded-lg p-6 hover:shadow-md transition"
          >
            <h2 className="text-xl font-semibold mb-1">Étudiant</h2>
            <p className="text-sm text-slate-600">
              Marquer sa présence avec un code, déposer un exercice.
            </p>
          </Link>
          <Link
            href="/relecteur"
            className="bg-white border rounded-lg p-6 hover:shadow-md transition"
          >
            <h2 className="text-xl font-semibold mb-1">Relecteur</h2>
            <p className="text-sm text-slate-600">
              Noter et commenter un exercice assigné.
            </p>
          </Link>
        </div>
      </div>
    </main>
  );
}
