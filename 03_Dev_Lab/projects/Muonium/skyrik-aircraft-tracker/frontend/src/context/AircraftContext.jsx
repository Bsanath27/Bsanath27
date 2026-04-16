import { createContext, useContext, useState, useEffect } from 'react';
import { initializeSocket, closeSocket } from '../services/api';

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
    autoRefresh: true
  });

  useEffect(() => {
    // Load settings from localStorage
    const saved = localStorage.getItem('aircraftTrackerSettings');
    if (saved) {
      setSettings(JSON.parse(saved));
    }

    // Initialize WebSocket
    const socket = initializeSocket((newAircraft) => {
      setAircraft(newAircraft);
    });

    return () => {
      closeSocket();
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
