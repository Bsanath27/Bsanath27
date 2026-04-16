import axios from 'axios';
import io from 'socket.io-client';

const API_URL = 'http://127.0.0.1:5000/api';
let socket = null;

// REST API calls
export const fetchAircraft = async (limit = 100) => {
  try {
    const response = await axios.get(`${API_URL}/aircraft?limit=${limit}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching aircraft:', error);
    return [];
  }
};

export const fetchAircraftDetail = async (icao) => {
  try {
    const response = await axios.get(`${API_URL}/aircraft/${icao}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching aircraft detail:', error);
    return null;
  }
};

export const searchAircraft = async (filters) => {
  try {
    const params = new URLSearchParams();
    Object.keys(filters).forEach(key => {
      if (filters[key] !== null && filters[key] !== undefined) {
        params.append(key, filters[key]);
      }
    });
    const response = await axios.get(`${API_URL}/search?${params}`);
    return response.data;
  } catch (error) {
    console.error('Error searching aircraft:', error);
    return [];
  }
};

export const fetchStatistics = async () => {
  try {
    const response = await axios.get(`${API_URL}/statistics`);
    return response.data;
  } catch (error) {
    console.error('Error fetching statistics:', error);
    return null;
  }
};

// WebSocket connection
export const initializeSocket = (onAircraftUpdate) => {
  socket = io('http://127.0.0.1:5000', {
    reconnection: true,
    reconnectionDelay: 1000,
    reconnectionDelayMax: 5000,
    reconnectionAttempts: 5
  });

  socket.on('connect', () => {
    console.log('Connected to server');
  });

  socket.on('aircraft_update', (data) => {
    onAircraftUpdate(data.aircraft);
  });

  socket.on('disconnect', () => {
    console.log('Disconnected from server');
  });

  socket.on('error', (error) => {
    console.error('Socket error:', error);
  });

  return socket;
};

export const closeSocket = () => {
  if (socket) {
    socket.disconnect();
  }
};

export const requestUpdate = () => {
  if (socket && socket.connected) {
    socket.emit('request_update');
  }
};
