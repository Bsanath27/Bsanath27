import { useState } from 'react';
import { searchAircraft } from '../services/api';
import { useNavigate } from 'react-router-dom';

export default function Search() {
  const navigate = useNavigate();
  const [filters, setFilters] = useState({
    callsign: '',
    type: '',
    altitude_min: '',
    altitude_max: '',
    speed_min: '',
    speed_max: ''
  });
  const [results, setResults] = useState([]);
  const [searched, setSearched] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    const data = await searchAircraft(filters);
    setResults(data);
    setSearched(true);
  };

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Search Aircraft</h1>

      <form onSubmit={handleSearch} className="bg-gray-800 p-6 rounded-lg mb-8">
        <div className="grid grid-cols-2 gap-4 mb-6">
          <input
            type="text"
            placeholder="Callsign"
            value={filters.callsign}
            onChange={(e) => setFilters({...filters, callsign: e.target.value})}
            className="bg-gray-700 text-white p-2 rounded"
          />
          <input
            type="text"
            placeholder="Aircraft Type"
            value={filters.type}
            onChange={(e) => setFilters({...filters, type: e.target.value})}
            className="bg-gray-700 text-white p-2 rounded"
          />
          <input
            type="number"
            placeholder="Min Altitude"
            value={filters.altitude_min}
            onChange={(e) => setFilters({...filters, altitude_min: e.target.value})}
            className="bg-gray-700 text-white p-2 rounded"
          />
          <input
            type="number"
            placeholder="Max Altitude"
            value={filters.altitude_max}
            onChange={(e) => setFilters({...filters, altitude_max: e.target.value})}
            className="bg-gray-700 text-white p-2 rounded"
          />
        </div>
        <button
          type="submit"
          className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded font-semibold"
        >
          Search
        </button>
      </form>

      {searched && (
        <div>
          <h2 className="text-2xl font-bold mb-4">Results ({results.length})</h2>
          <div className="overflow-x-auto">
            <table className="w-full bg-gray-800 rounded">
              <thead className="border-b border-gray-700">
                <tr>
                  <th className="p-4 text-left">Callsign</th>
                  <th className="p-4 text-left">Type</th>
                  <th className="p-4 text-left">Altitude</th>
                  <th className="p-4 text-left">Speed</th>
                  <th className="p-4 text-left">Action</th>
                </tr>
              </thead>
              <tbody>
                {results.map(aircraft => (
                  <tr key={aircraft.icao_address} className="border-b border-gray-700 hover:bg-gray-700">
                    <td className="p-4">{aircraft.callsign}</td>
                    <td className="p-4">{aircraft.aircraft_type}</td>
                    <td className="p-4">{aircraft.altitude} ft</td>
                    <td className="p-4">{aircraft.speed_knots} kts</td>
                    <td className="p-4">
                      <button
                        onClick={() => navigate(`/aircraft/${aircraft.icao_address}`)}
                        className="text-blue-400 hover:text-blue-300"
                      >
                        View
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
