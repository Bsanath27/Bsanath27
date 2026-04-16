import { useAircraft } from '../context/AircraftContext';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { Settings as SettingsIcon, Sliders, Eye, Zap } from 'lucide-react';

export default function Settings() {
  const { settings, updateSettings } = useAircraft();

  const handleChange = (key, value) => {
    updateSettings({...settings, [key]: value});
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 to-slate-800 p-8">
      <div className="max-w-4xl mx-auto">
        {/* Header */}
        <div className="mb-8">
          <div className="flex items-center gap-3 mb-2">
            <SettingsIcon className="w-8 h-8 text-blue-400" />
            <h1 className="text-4xl font-bold text-white">Settings</h1>
          </div>
          <p className="text-slate-400">Configure your tracking experience</p>
        </div>

        {/* Settings Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Data Refresh Settings */}
          <Card className="bg-slate-700 border-slate-600 shadow-lg hover:shadow-xl transition">
            <CardHeader className="bg-gradient-to-r from-blue-600 to-blue-700 text-white">
              <CardTitle className="flex items-center gap-2">
                <Zap className="w-5 h-5" />
                Data Refresh
              </CardTitle>
              <CardDescription className="text-blue-100">Control how often data updates</CardDescription>
            </CardHeader>
            <CardContent className="p-6 space-y-6">
              <div>
                <label className="block text-white font-semibold mb-3">Poll Interval</label>
                <div className="flex items-center gap-4">
                  <select
                    value={settings.pollInterval}
                    onChange={(e) => handleChange('pollInterval', parseInt(e.target.value))}
                    className="flex-1 bg-slate-600 text-white p-3 rounded-lg border border-slate-500 hover:border-blue-500 transition focus:outline-none focus:border-blue-400"
                  >
                    <option value={10}>10 seconds</option>
                    <option value={30}>30 seconds</option>
                    <option value={45}>45 seconds</option>
                    <option value={60}>60 seconds</option>
                  </select>
                  <span className="text-slate-300 font-semibold">{settings.pollInterval}s</span>
                </div>
              </div>

              <div>
                <label className="flex items-center gap-3 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={settings.autoRefresh}
                    onChange={(e) => handleChange('autoRefresh', e.target.checked)}
                    className="w-5 h-5 rounded bg-slate-600 border-slate-500 cursor-pointer"
                  />
                  <span className="text-white font-semibold">Auto-refresh data</span>
                </label>
                <p className="text-slate-400 text-sm ml-8 mt-1">Automatically fetch new aircraft data</p>
              </div>
            </CardContent>
          </Card>

          {/* Data Retention Settings */}
          <Card className="bg-slate-700 border-slate-600 shadow-lg hover:shadow-xl transition">
            <CardHeader className="bg-gradient-to-r from-amber-600 to-amber-700 text-white">
              <CardTitle className="flex items-center gap-2">
                <Sliders className="w-5 h-5" />
                Data Management
              </CardTitle>
              <CardDescription className="text-amber-100">Manage stored aircraft history</CardDescription>
            </CardHeader>
            <CardContent className="p-6 space-y-6">
              <div>
                <label className="block text-white font-semibold mb-3">Data Retention Period</label>
                <select
                  value={settings.dataRetention}
                  onChange={(e) => handleChange('dataRetention', parseInt(e.target.value))}
                  className="w-full bg-slate-600 text-white p-3 rounded-lg border border-slate-500 hover:border-amber-500 transition focus:outline-none focus:border-amber-400"
                >
                  <option value={1}>1 day</option>
                  <option value={7}>7 days</option>
                  <option value={14}>14 days</option>
                  <option value={30}>30 days</option>
                </select>
              </div>

              <div>
                <label className="block text-white font-semibold mb-3">Map Style</label>
                <select
                  value={settings.mapStyle}
                  onChange={(e) => handleChange('mapStyle', e.target.value)}
                  className="w-full bg-slate-600 text-white p-3 rounded-lg border border-slate-500 hover:border-amber-500 transition focus:outline-none focus:border-amber-400"
                >
                  <option value="osm">OpenStreetMap</option>
                  <option value="dark">Dark Mode</option>
                  <option value="satellite">Satellite</option>
                </select>
              </div>
            </CardContent>
          </Card>

          {/* Visualization Settings */}
          <Card className="bg-slate-700 border-slate-600 shadow-lg hover:shadow-xl transition">
            <CardHeader className="bg-gradient-to-r from-purple-600 to-purple-700 text-white">
              <CardTitle className="flex items-center gap-2">
                <Eye className="w-5 h-5" />
                Visualization
              </CardTitle>
              <CardDescription className="text-purple-100">Customize map display</CardDescription>
            </CardHeader>
            <CardContent className="p-6 space-y-6">
              <div>
                <label className="block text-white font-semibold mb-3">
                  Marker Size: <span className="text-purple-400 ml-2">{settings.markerSize}px</span>
                </label>
                <input
                  type="range"
                  min="10"
                  max="50"
                  value={settings.markerSize}
                  onChange={(e) => handleChange('markerSize', parseInt(e.target.value))}
                  className="w-full h-2 bg-slate-600 rounded-lg appearance-none cursor-pointer accent-purple-500"
                />
                <div className="flex justify-between text-xs text-slate-400 mt-2">
                  <span>Small</span>
                  <span>Large</span>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Flight Trails Settings */}
          <Card className="bg-slate-700 border-slate-600 shadow-lg hover:shadow-xl transition">
            <CardHeader className="bg-gradient-to-r from-green-600 to-green-700 text-white">
              <CardTitle className="flex items-center gap-2">
                <Eye className="w-5 h-5" />
                Flight Trails
              </CardTitle>
              <CardDescription className="text-green-100">Track aircraft movement paths</CardDescription>
            </CardHeader>
            <CardContent className="p-6">
              <label className="flex items-center gap-3 cursor-pointer">
                <input
                  type="checkbox"
                  checked={settings.showTrails !== false}
                  onChange={(e) => handleChange('showTrails', e.target.checked)}
                  className="w-5 h-5 rounded bg-slate-600 border-slate-500 cursor-pointer accent-green-500"
                />
                <div>
                  <span className="text-white font-semibold block">Show flight trails</span>
                  <p className="text-slate-400 text-sm">Display dashed lines showing aircraft paths</p>
                </div>
              </label>
            </CardContent>
          </Card>
        </div>

        {/* Save Button */}
        <div className="flex justify-end gap-4">
          <Button variant="outline" size="lg">
            Reset to Defaults
          </Button>
          <Button variant="default" size="lg" className="bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800">
            Save Changes
          </Button>
        </div>
      </div>
    </div>
  );
}
