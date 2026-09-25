import Link from "next/link";

const espaces = [
  { href: "/formateur", titre: "Formateur", desc: "Ouvrir une session, consulter le tableau de bord, ajouter une présence.", emoji: "🎓", gradient: "from-blue-500 to-indigo-500" },
  { href: "/etudiant", titre: "Étudiant", desc: "Marquer sa présence avec un code, déposer le lien de son exercice.", emoji: "📚", gradient: "from-emerald-500 to-teal-500" },
  { href: "/relecteur", titre: "Relecteur", desc: "Consulter un exercice assigné, noter et commenter.", emoji: "✍️", gradient: "from-purple-500 to-fuchsia-500" },
];

export default function Home() {
  return (
    <main className="max-w-6xl mx-auto px-6 py-16">
      <section className="text-center mb-16">
        <span className="inline-block px-3 py-1 rounded-full text-xs font-semibold bg-blue-100 text-blue-700 mb-4">
          Épreuve finale fullstack
        </span>
        <h1 className="text-4xl md:text-5xl font-bold tracking-tight mb-4 bg-gradient-to-r from-slate-900 to-slate-600 bg-clip-text text-transparent">
          Présence & Relecture
        </h1>
        <p className="text-lg text-slate-600 max-w-2xl mx-auto">
          Choisissez votre espace. Aucune authentification n&apos;est requise.
        </p>
      </section>
      <div className="grid md:grid-cols-3 gap-6">
        {espaces.map((e) => (
          <Link key={e.href} href={e.href} className="group relative overflow-hidden rounded-2xl bg-white border border-slate-200 p-6 hover:shadow-xl hover:-translate-y-1 transition-all duration-200">
            <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${e.gradient} grid place-items-center text-2xl mb-4 shadow-lg`}>
              {e.emoji}
            </div>
            <h2 className="text-xl font-semibold mb-2">{e.titre}</h2>
            <p className="text-sm text-slate-600 leading-relaxed">{e.desc}</p>
            <span className="inline-block mt-4 text-sm font-medium text-blue-600 group-hover:translate-x-1 transition-transform">Accéder →</span>
          </Link>
        ))}
      </div>
    </main>
  );
}
