import { createContext, useContext, useState, useEffect } from 'react';
import { initializeSocket, closeSocket, fetchAircraft } from '../services/api';

const AircraftContext = createContext();

export function AircraftProvider({ children }) {
  const [aircraft, setAircraft] = useState([]);
  const [selectedAircraft, setSelectedAircraft] = useState(null);
  const [loading, setLoading] = useState(false);
  const [settings, setSettings] = useState({
    pollInterval: 10,
    dataRetention: 7,
    mapStyle: 'osm',
    markerSize: 30,
    autoRefresh: true,
    showTrails: true
  });

  useEffect(() => {
    // Load settings from localStorage
    const saved = localStorage.getItem('aircraftTrackerSettings');
    if (saved) {
      setSettings(JSON.parse(saved));
    }

    // Fetch initial aircraft data
    const loadInitialData = async () => {
      setLoading(true);
      const data = await fetchAircraft(100);
      setAircraft(data);
      setLoading(false);
    };
    loadInitialData();

    // Initialize WebSocket for live updates
    const socket = initializeSocket((newAircraft) => {
      setAircraft(newAircraft);
    });

    // Set up polling interval for periodic updates
    const pollInterval = setInterval(async () => {
      const data = await fetchAircraft(100);
      setAircraft(data);
    }, 10000); // Poll every 10 seconds

    return () => {
      closeSocket();
      clearInterval(pollInterval);
    };
  }, []);

  const updateSettings = (newSettings) => {
    setSettings(newSettings);
    localStorage.setItem('aircraftTrackerSettings', JSON.stringify(newSettings));
  };

  return (
    <AircraftContext.Provider value={{
      aircraft,
      setAircraft,
      selectedAircraft,
      setSelectedAircraft,
      loading,
      setLoading,
      settings,
      updateSettings
    }}>
      {children}
    </AircraftContext.Provider>
  );
}

export function useAircraft() {
  const context = useContext(AircraftContext);
  if (!context) {
    throw new Error('useAircraft must be used within AircraftProvider');
  }
  return context;
}
