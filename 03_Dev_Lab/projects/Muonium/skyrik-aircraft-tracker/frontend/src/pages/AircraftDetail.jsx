import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchAircraftDetail } from '../services/api';
import { ArrowLeft } from 'lucide-react';

export default function AircraftDetail() {
  const { icao } = useParams();
  const navigate = useNavigate();
  const [aircraft, setAircraft] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadAircraft = async () => {
      setLoading(true);
      const data = await fetchAircraftDetail(icao);
      setAircraft(data);
      setLoading(false);
    };
    loadAircraft();
  }, [icao]);

  if (loading) return <div className="p-8">Loading...</div>;
  if (!aircraft) return <div className="p-8">Aircraft not found</div>;

  return (
    <div className="p-8">
      <button
        onClick={() => navigate('/')}
        className="flex items-center gap-2 mb-6 text-blue-500 hover:text-blue-400"
      >
        <ArrowLeft className="w-5 h-5" />
        Back to Map
      </button>

      <div className="bg-gray-800 rounded-lg p-8 max-w-2xl">
        <h1 className="text-3xl font-bold mb-6 text-blue-400">{aircraft.callsign || 'Unknown'}</h1>

        <div className="grid grid-cols-2 gap-6">
          <div>
            <div className="text-gray-400">ICAO Address</div>
            <div className="text-xl font-semibold">{aircraft.icao_address}</div>
          </div>
          <div>
            <div className="text-gray-400">Aircraft Type</div>
            <div className="text-xl font-semibold">{aircraft.aircraft_type || 'N/A'}</div>
          </div>
          <div>
            <div className="text-gray-400">Altitude</div>
            <div className="text-2xl font-bold text-green-400">{aircraft.altitude} ft</div>
          </div>
          <div>
            <div className="text-gray-400">Speed</div>
            <div className="text-2xl font-bold text-blue-400">{aircraft.speed_knots} knots</div>
          </div>
          <div>
            <div className="text-gray-400">Heading</div>
            <div className="text-2xl font-bold">{aircraft.heading}°</div>
          </div>
          <div>
            <div className="text-gray-400">Position</div>
            <div className="text-sm font-mono">{aircraft.latitude.toFixed(4)}, {aircraft.longitude.toFixed(4)}</div>
          </div>
          <div className="col-span-2">
            <div className="text-gray-400">Last Update</div>
            <div className="text-sm">{aircraft.last_update}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
