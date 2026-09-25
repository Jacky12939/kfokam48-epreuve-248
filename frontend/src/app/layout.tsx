import type { Metadata } from "next";
import { Inter } from "next/font/google";
import Link from "next/link";
import "./globals.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "KFOKAM48 — Présence & Relecture",
  description: "Plateforme de gestion de présence et de relecture par les pairs",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="fr">
      <body className={`${inter.className} bg-gradient-to-br from-slate-50 via-white to-blue-50 min-h-screen text-slate-900 antialiased`}>
        <header className="sticky top-0 z-50 backdrop-blur-lg bg-white/80 border-b border-slate-200">
          <div className="max-w-6xl mx-auto px-6 h-16 flex items-center justify-between">
            <Link href="/" className="flex items-center gap-2 font-bold text-lg">
              <span className="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-600 to-indigo-600 text-white grid place-items-center text-sm">KF</span>
              KFOKAM48
            </Link>
            <nav className="flex gap-1 text-sm">
              <Link href="/formateur" className="px-3 py-2 rounded-lg hover:bg-slate-100 transition">Formateur</Link>
              <Link href="/etudiant" className="px-3 py-2 rounded-lg hover:bg-slate-100 transition">Étudiant</Link>
              <Link href="/relecteur" className="px-3 py-2 rounded-lg hover:bg-slate-100 transition">Relecteur</Link>
            </nav>
          </div>
        </header>
        {children}
        <footer className="text-center text-xs text-slate-500 py-8">
          Épreuve finale fullstack · KFOKAM48 · 2026
        </footer>
      </body>
    </html>
  );
}
