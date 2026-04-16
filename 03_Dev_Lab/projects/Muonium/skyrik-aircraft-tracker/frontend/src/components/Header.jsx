import { Link } from 'react-router-dom';
import { Plane, Radar } from 'lucide-react';
import Navigation from './Navigation';

export default function Header() {
  return (
    <header className="bg-gradient-to-r from-slate-900 via-blue-900 to-slate-900 border-b border-blue-800 p-6 shadow-xl sticky top-0 z-40">
      <div className="flex items-center justify-between max-w-7xl mx-auto">
        {/* Logo & Title */}
        <Link to="/" className="flex items-center gap-3 group">
          <div className="relative">
            <div className="absolute inset-0 bg-blue-600 rounded-lg blur opacity-75 group-hover:opacity-100 transition duration-300"></div>
            <div className="relative bg-blue-600 p-2 rounded-lg">
              <Radar className="w-6 h-6 text-white" />
            </div>
          </div>
          <div>
            <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-400 to-cyan-400 bg-clip-text text-transparent">
              Skyrik
            </h1>
            <p className="text-xs text-blue-200 font-semibold">Aircraft Tracker</p>
          </div>
        </Link>

        {/* Navigation */}
        <Navigation />

        {/* Status Indicator */}
        <div className="flex items-center gap-2 px-4 py-2 bg-green-500/10 border border-green-500/30 rounded-full">
          <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse"></div>
          <span className="text-sm text-green-300 font-semibold">System Online</span>
        </div>
      </div>
    </header>
  );
}
