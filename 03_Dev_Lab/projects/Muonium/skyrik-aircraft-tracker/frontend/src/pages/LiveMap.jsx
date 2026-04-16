import { useState } from 'react';
import { useAircraft } from '../context/AircraftContext';
import MapComponent from '../components/MapComponent';
import Sidebar from '../components/Sidebar';
import StatCard from '../components/StatCard';
import { Plane, Gauge, Zap } from 'lucide-react';

export default function LiveMap() {
  const { aircraft } = useAircraft();
  const [selectedAircraft, setSelectedAircraft] = useState(null);

  const maxAltitude = aircraft.length > 0 ? Math.max(...aircraft.map(a => a.altitude || 0)) : 0;
  const maxSpeed = aircraft.length > 0 ? Math.max(...aircraft.map(a => a.speed_knots || 0)) : 0;

  return (
    <div className="flex h-full">
      <div className="flex-1 flex flex-col">
        {/* Stats bar */}
        <div className="bg-gray-800 border-b border-gray-700 p-4">
          <div className="grid grid-cols-3 gap-4">
            <StatCard label="Aircraft" value={aircraft.length} icon={Plane} />
            <StatCard label="Max Altitude" value={`${maxAltitude} ft`} icon={Gauge} />
            <StatCard label="Max Speed" value={`${maxSpeed} kts`} icon={Zap} />
          </div>
        </div>

        {/* Map */}
        <div className="flex-1">
          <MapComponent onMarkerClick={setSelectedAircraft} />
        </div>
      </div>

      {/* Sidebar */}
      <Sidebar onSelectAircraft={setSelectedAircraft} />
    </div>
  );
}
