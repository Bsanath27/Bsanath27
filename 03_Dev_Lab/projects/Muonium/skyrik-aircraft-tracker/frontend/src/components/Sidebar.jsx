import { useAircraft } from '../context/AircraftContext';
import { useNavigate } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from './ui/Card';
import { Badge } from './ui/Badge';
import { ChevronRight, Helicopter, Plane } from 'lucide-react';

export default function Sidebar({ onSelectAircraft }) {
  const { aircraft } = useAircraft();
  const navigate = useNavigate();

  const handleClick = (plane) => {
    onSelectAircraft(plane);
    navigate(`/aircraft/${plane.icao_address}`);
  };

  const isHelicopter = (type) => {
    const heliKeywords = ['bell', 'robinson', 'sikorsky', 'h135', 'dhruv', 'helicopter'];
    return heliKeywords.some(k => type?.toLowerCase().includes(k));
  };

  return (
    <div className="w-96 bg-gradient-to-b from-slate-800 to-slate-900 border-l border-slate-700 overflow-hidden flex flex-col shadow-xl">
      {/* Header */}
      <CardHeader className="bg-slate-700 border-b border-slate-600 p-4">
        <CardTitle className="text-lg text-white flex items-center gap-2">
          <span>Aircraft List</span>
          <Badge variant="primary">{aircraft.length}</Badge>
        </CardTitle>
      </CardHeader>

      {/* Aircraft List */}
      <div className="flex-1 overflow-y-auto p-3 space-y-2">
        {aircraft.slice(0, 25).map(plane => (
          <Card
            key={plane.icao_address}
            onClick={() => handleClick(plane)}
            className="bg-slate-700 border-slate-600 hover:bg-slate-600 cursor-pointer transition-all duration-200 group hover:shadow-lg hover:border-blue-500"
          >
            <CardContent className="p-3">
              <div className="flex items-start justify-between gap-2">
                <div className="flex-1 min-w-0">
                  {/* Callsign & Type */}
                  <div className="flex items-center gap-2 mb-2">
                    {isHelicopter(plane.aircraft_type) ? (
                      <Helicopter className="w-4 h-4 text-purple-400 flex-shrink-0" />
                    ) : (
                      <Plane className="w-4 h-4 text-blue-400 flex-shrink-0" />
                    )}
                    <span className="font-semibold text-white truncate group-hover:text-blue-400 transition">
                      {plane.callsign || 'Unknown'}
                    </span>
                  </div>

                  {/* Aircraft Type */}
                  <p className="text-xs text-slate-400 mb-2 truncate">
                    {plane.aircraft_type || 'N/A'}
                  </p>

                  {/* Stats Row */}
                  <div className="grid grid-cols-3 gap-2">
                    <div>
                      <p className="text-xs text-slate-400">Altitude</p>
                      <p className="text-sm font-semibold text-green-400">
                        {plane.altitude} ft
                      </p>
                    </div>
                    <div>
                      <p className="text-xs text-slate-400">Speed</p>
                      <p className="text-sm font-semibold text-amber-400">
                        {plane.speed_knots} kts
                      </p>
                    </div>
                    <div>
                      <p className="text-xs text-slate-400">Heading</p>
                      <p className="text-sm font-semibold text-blue-400">
                        {plane.heading || '—'}°
                      </p>
                    </div>
                  </div>
                </div>

                {/* Arrow Indicator */}
                <ChevronRight className="w-4 h-4 text-slate-500 flex-shrink-0 group-hover:text-blue-400 group-hover:translate-x-1 transition" />
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Footer */}
      <div className="bg-slate-700 border-t border-slate-600 p-3 text-center text-xs text-slate-400">
        Showing {Math.min(25, aircraft.length)} of {aircraft.length} aircraft
      </div>
    </div>
  );
}
