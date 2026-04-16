import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AircraftProvider } from './context/AircraftContext';
import Header from './components/Header';
import LiveMap from './pages/LiveMap';
import AircraftDetail from './pages/AircraftDetail';
import Search from './pages/Search';
import Statistics from './pages/Statistics';
import Settings from './pages/Settings';

export default function App() {
  return (
    <Router>
      <AircraftProvider>
        <div className="h-screen flex flex-col bg-gray-900 text-white">
          <Header />
          <div className="flex-1 overflow-auto">
            <Routes>
              <Route path="/" element={<LiveMap />} />
              <Route path="/aircraft/:icao" element={<AircraftDetail />} />
              <Route path="/search" element={<Search />} />
              <Route path="/statistics" element={<Statistics />} />
              <Route path="/settings" element={<Settings />} />
            </Routes>
          </div>
        </div>
      </AircraftProvider>
    </Router>
  );
}
