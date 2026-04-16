// frontend/src/services/mapUtils.js
import L from 'leaflet';

export function getAltitudeColor(altitude) {
  if (altitude < 10000) return '#22c55e'; // green
  if (altitude < 30000) return '#f59e0b'; // orange
  return '#ef4444'; // red
}

export function isHelicopter(aircraftType) {
  if (!aircraftType) return false;
  const type = aircraftType.toLowerCase();
  const helicopterKeywords = [
    'helicopter', 'heli', 'h135', 'h145', 'h125', 'h225',
    'bell', 'sikorsky', 's-76', 'ah-64', 'uh-60', 'uh-1',
    'airbus h', 'robinson', 'eurocopter', 'augusta'
  ];
  return helicopterKeywords.some(keyword => type.includes(keyword));
}

export function getHelicopterCategory(aircraftType) {
  const type = aircraftType?.toLowerCase() || '';
  if (type.includes('bell') || type.includes('robinson')) {
    return 'civil';
  }
  if (type.includes('ah-64') || type.includes('uh-60') || type.includes('uh-1')) {
    return 'military';
  }
  if (type.includes('airbus h')) {
    return 'commercial';
  }
  return 'other';
}

export function getHelicopterColor(category) {
  const colors = {
    civil: '#06b6d4',      // cyan
    military: '#8b5cf6',   // purple
    commercial: '#ec4899', // pink
    other: '#6366f1'       // indigo
  };
  return colors[category] || colors.other;
}

export function createCustomMarker(aircraft, markerSize = 30) {
  const heli = isHelicopter(aircraft.aircraft_type);

  let color = getAltitudeColor(aircraft.altitude);

  // Override color for helicopters based on category
  if (heli) {
    const category = getHelicopterCategory(aircraft.aircraft_type);
    color = getHelicopterColor(category);
  }

  const rotation = aircraft.heading || 0;
  const svg = `
    <svg width="${markerSize}" height="${markerSize}" viewBox="0 0 24 24" fill="none"
         xmlns="http://www.w3.org/2000/svg"
         style="transform: rotate(${rotation}deg); filter: drop-shadow(0 0 3px rgba(0,0,0,0.4));">
      <circle cx="12" cy="12" r="10" fill="${color}" stroke="white" stroke-width="2"/>
      <text x="12" y="14" font-size="8" font-weight="bold" fill="white" text-anchor="middle">
        ${heli ? 'H' : 'A'}
      </text>
    </svg>
  `;

  return L.divIcon({
    html: svg,
    className: 'aircraft-marker',
    iconSize: [markerSize, markerSize],
    iconAnchor: [markerSize / 2, markerSize / 2],
    popupAnchor: [0, -markerSize / 2]
  });
}
