import { useEffect, useState } from 'react';
import { fetchStatistics } from '../services/api';
import { Pie, Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, BarElement, Title, Tooltip, Legend, ArcElement } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, BarElement, Title, Tooltip, Legend, ArcElement);

export default function Statistics() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    const loadStats = async () => {
      const data = await fetchStatistics();
      setStats(data);
    };
    loadStats();
  }, []);

  if (!stats) return <div className="p-8">Loading...</div>;

  const typeData = {
    labels: Object.keys(stats.by_type),
    datasets: [{
      label: 'Aircraft by Type',
      data: Object.values(stats.by_type),
      backgroundColor: ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'],
    }]
  };

  const altitudeData = {
    labels: Object.keys(stats.altitude_bands),
    datasets: [{
      label: 'Aircraft by Altitude',
      data: Object.values(stats.altitude_bands),
      backgroundColor: ['#10b981', '#f59e0b', '#ef4444'],
    }]
  };

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-8">Statistics</h1>

      <div className="grid grid-cols-3 gap-4 mb-8">
        <div className="bg-gray-800 p-6 rounded-lg">
          <div className="text-gray-400">Total Aircraft</div>
          <div className="text-4xl font-bold text-blue-400">{stats.total}</div>
        </div>
        <div className="bg-gray-800 p-6 rounded-lg">
          <div className="text-gray-400">Max Altitude</div>
          <div className="text-4xl font-bold text-green-400">{stats.max_altitude} ft</div>
        </div>
        <div className="bg-gray-800 p-6 rounded-lg">
          <div className="text-gray-400">Max Speed</div>
          <div className="text-4xl font-bold text-yellow-400">{stats.max_speed} kts</div>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-8">
        <div className="bg-gray-800 p-6 rounded-lg">
          <h2 className="text-xl font-bold mb-4">Aircraft by Type</h2>
          <Pie data={typeData} />
        </div>
        <div className="bg-gray-800 p-6 rounded-lg">
          <h2 className="text-xl font-bold mb-4">Aircraft by Altitude</h2>
          <Bar data={altitudeData} />
        </div>
      </div>
    </div>
  );
}
