import { useState } from 'react';
import { useAircraft } from '../context/AircraftContext';
import MapComponent from '../components/MapComponent';
import Sidebar from '../components/Sidebar';
import { Card, CardContent } from '../components/ui/Card';
import { Badge } from '../components/ui/Badge';
import { Plane, Helicopter, Activity, AlertCircle } from 'lucide-react';

export default function LiveMap() {
  const { aircraft } = useAircraft();
  const [selectedAircraft, setSelectedAircraft] = useState(null);

  const helicopters = aircraft.filter(a =>
    a.aircraft_type?.toLowerCase().includes('bell') ||
    a.aircraft_type?.toLowerCase().includes('robinson') ||
    a.aircraft_type?.toLowerCase().includes('sikorsky') ||
    a.aircraft_type?.toLowerCase().includes('h135') ||
    a.aircraft_type?.toLowerCase().includes('dhruv')
  );

  const maxAltitude = aircraft.length > 0 ? Math.max(...aircraft.map(a => a.altitude || 0)) : 0;
  const maxSpeed = aircraft.length > 0 ? Math.max(...aircraft.map(a => a.speed_knots || 0)) : 0;

  return (
    <div className="flex h-full bg-gradient-to-br from-slate-900 to-slate-800">
      <div className="flex-1 flex flex-col">
        {/* Premium Header */}
        <div className="bg-gradient-to-r from-blue-600 to-blue-700 border-b border-blue-800 px-6 py-4 shadow-lg">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-3">
              <Activity className="w-8 h-8 text-white" />
              <div>
                <h1 className="text-2xl font-bold text-white">Aircraft Tracking</h1>
                <p className="text-blue-100 text-sm">Real-time helicopter & aircraft monitoring</p>
              </div>
            </div>
            <Badge variant="success">Live</Badge>
          </div>

          {/* Stats Grid */}
          <div className="grid grid-cols-4 gap-3">
            <Card className="bg-blue-500 border-blue-400 shadow-lg">
              <CardContent className="p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-blue-100 text-xs font-semibold">Total Aircraft</p>
                    <p className="text-white text-2xl font-bold">{aircraft.length}</p>
                  </div>
                  <Plane className="w-10 h-10 text-blue-200 opacity-50" />
                </div>
              </CardContent>
            </Card>

            <Card className="bg-purple-500 border-purple-400 shadow-lg">
              <CardContent className="p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-purple-100 text-xs font-semibold">Helicopters</p>
                    <p className="text-white text-2xl font-bold">{helicopters.length}</p>
                  </div>
                  <Helicopter className="w-10 h-10 text-purple-200 opacity-50" />
                </div>
              </CardContent>
            </Card>

            <Card className="bg-amber-500 border-amber-400 shadow-lg">
              <CardContent className="p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-amber-100 text-xs font-semibold">Max Altitude</p>
                    <p className="text-white text-2xl font-bold">{maxAltitude}</p>
                    <p className="text-amber-100 text-xs">ft</p>
                  </div>
                  <AlertCircle className="w-10 h-10 text-amber-200 opacity-50" />
                </div>
              </CardContent>
            </Card>

            <Card className="bg-green-500 border-green-400 shadow-lg">
              <CardContent className="p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-green-100 text-xs font-semibold">Max Speed</p>
                    <p className="text-white text-2xl font-bold">{maxSpeed}</p>
                    <p className="text-green-100 text-xs">knots</p>
                  </div>
                  <Activity className="w-10 h-10 text-green-200 opacity-50" />
                </div>
              </CardContent>
            </Card>
          </div>
        </div>

        {/* Map Container */}
        <div className="flex-1 relative overflow-hidden rounded-xl m-4 shadow-2xl">
          <MapComponent onMarkerClick={setSelectedAircraft} />
        </div>
      </div>

      {/* Sidebar */}
      <Sidebar onSelectAircraft={setSelectedAircraft} />
    </div>
  );
}
