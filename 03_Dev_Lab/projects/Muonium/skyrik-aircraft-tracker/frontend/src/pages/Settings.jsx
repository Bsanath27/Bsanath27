import { useAircraft } from '../context/AircraftContext';

export default function Settings() {
  const { settings, updateSettings } = useAircraft();

  const handleChange = (key, value) => {
    updateSettings({...settings, [key]: value});
  };

  return (
    <div className="p-8 max-w-2xl">
      <h1 className="text-3xl font-bold mb-8">Settings</h1>

      <div className="bg-gray-800 rounded-lg p-8 space-y-6">
        <div>
          <label className="block text-gray-400 mb-2">Poll Interval (seconds)</label>
          <select
            value={settings.pollInterval}
            onChange={(e) => handleChange('pollInterval', parseInt(e.target.value))}
            className="w-full bg-gray-700 text-white p-2 rounded"
          >
            <option value={10}>10 seconds</option>
            <option value={30}>30 seconds</option>
            <option value={45}>45 seconds</option>
            <option value={60}>60 seconds</option>
          </select>
        </div>

        <div>
          <label className="block text-gray-400 mb-2">Data Retention (days)</label>
          <select
            value={settings.dataRetention}
            onChange={(e) => handleChange('dataRetention', parseInt(e.target.value))}
            className="w-full bg-gray-700 text-white p-2 rounded"
          >
            <option value={1}>1 day</option>
            <option value={7}>7 days</option>
            <option value={14}>14 days</option>
            <option value={30}>30 days</option>
          </select>
        </div>

        <div>
          <label className="block text-gray-400 mb-2">Map Style</label>
          <select
            value={settings.mapStyle}
            onChange={(e) => handleChange('mapStyle', e.target.value)}
            className="w-full bg-gray-700 text-white p-2 rounded"
          >
            <option value="osm">OpenStreetMap</option>
            <option value="dark">Dark</option>
            <option value="satellite">Satellite</option>
          </select>
        </div>

        <div>
          <label className="block text-gray-400 mb-2">Marker Size: {settings.markerSize}</label>
          <input
            type="range"
            min="10"
            max="50"
            value={settings.markerSize}
            onChange={(e) => handleChange('markerSize', parseInt(e.target.value))}
            className="w-full"
          />
        </div>

        <div>
          <label className="flex items-center gap-2 text-gray-400">
            <input
              type="checkbox"
              checked={settings.autoRefresh}
              onChange={(e) => handleChange('autoRefresh', e.target.checked)}
            />
            Auto-refresh data
          </label>
        </div>

        <div>
          <label className="flex items-center gap-2 text-gray-400">
            <input
              type="checkbox"
              checked={settings.showTrails !== false}
              onChange={(e) => handleChange('showTrails', e.target.checked)}
            />
            Show flight trails
          </label>
        </div>

        <button className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 rounded font-semibold">
          Save Settings
        </button>
      </div>
    </div>
  );
}
