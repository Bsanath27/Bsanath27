import { Link } from 'react-router-dom';
import { Plane } from 'lucide-react';
import Navigation from './Navigation';

export default function Header() {
  return (
    <header className="bg-gray-800 border-b border-gray-700 p-4">
      <div className="flex items-center justify-between">
        <Link to="/" className="flex items-center gap-2">
          <Plane className="w-8 h-8 text-blue-500" />
          <h1 className="text-2xl font-bold">Skyrik Aircraft Tracker</h1>
        </Link>
        <Navigation />
      </div>
    </header>
  );
}
