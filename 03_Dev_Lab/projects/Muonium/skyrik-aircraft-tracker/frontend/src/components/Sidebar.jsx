import { useAircraft } from '../context/AircraftContext';
import { useNavigate } from 'react-router-dom';

export default function Sidebar({ onSelectAircraft }) {
  const { aircraft } = useAircraft();
  const navigate = useNavigate();

  const handleClick = (plane) => {
    onSelectAircraft(plane);
    navigate(`/aircraft/${plane.icao_address}`);
  };

  return (
    <div className="w-80 bg-gray-800 border-l border-gray-700 overflow-auto">
      <div className="p-4">
        <h2 className="text-lg font-bold mb-4">Aircraft ({aircraft.length})</h2>
        <div className="space-y-2">
          {aircraft.slice(0, 20).map(plane => (
            <div
              key={plane.icao_address}
              onClick={() => handleClick(plane)}
              className="p-3 bg-gray-700 rounded cursor-pointer hover:bg-gray-600 transition"
            >
              <div className="font-semibold text-blue-400">{plane.callsign || 'N/A'}</div>
              <div className="text-xs text-gray-400">
                {plane.altitude} ft • {plane.speed_knots} knots
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
