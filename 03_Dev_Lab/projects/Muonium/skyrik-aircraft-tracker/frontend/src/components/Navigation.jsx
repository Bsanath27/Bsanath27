import { Link, useLocation } from 'react-router-dom';
import { Map, Search, BarChart3, Settings } from 'lucide-react';

export default function Navigation() {
  const location = useLocation();

  const isActive = (path) => location.pathname === path ? 'text-blue-500' : 'text-gray-400 hover:text-white';

  return (
    <nav className="flex gap-6">
      <Link to="/" className={`flex items-center gap-2 ${isActive('/')}`}>
        <Map className="w-5 h-5" />
        <span className="hidden sm:inline">Live Map</span>
      </Link>
      <Link to="/search" className={`flex items-center gap-2 ${isActive('/search')}`}>
        <Search className="w-5 h-5" />
        <span className="hidden sm:inline">Search</span>
      </Link>
      <Link to="/statistics" className={`flex items-center gap-2 ${isActive('/statistics')}`}>
        <BarChart3 className="w-5 h-5" />
        <span className="hidden sm:inline">Statistics</span>
      </Link>
      <Link to="/settings" className={`flex items-center gap-2 ${isActive('/settings')}`}>
        <Settings className="w-5 h-5" />
        <span className="hidden sm:inline">Settings</span>
      </Link>
    </nav>
  );
}
