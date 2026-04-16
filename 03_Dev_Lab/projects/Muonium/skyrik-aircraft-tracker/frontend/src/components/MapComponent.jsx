import { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useAircraft } from '../context/AircraftContext';

export default function MapComponent({ onMarkerClick }) {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const markersRef = useRef({});
  const { aircraft, settings } = useAircraft();

  useEffect(() => {
    if (!mapRef.current) return;

    // Initialize map
    const map = L.map(mapRef.current).setView([20, 0], 2);

    // Add tile layer
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
      maxZoom: 19,
    }).addTo(map);

    mapInstanceRef.current = map;

    return () => {
      map.remove();
    };
  }, []);

  // Update markers when aircraft data changes
  useEffect(() => {
    if (!mapInstanceRef.current) return;

    const map = mapInstanceRef.current;

    // Remove old markers
    Object.values(markersRef.current).forEach(marker => map.removeLayer(marker));
    markersRef.current = {};

    // Add new markers
    aircraft.forEach(plane => {
      if (!plane.latitude || !plane.longitude) return;

      const color = plane.altitude < 10000 ? 'green' : plane.altitude < 30000 ? 'orange' : 'red';

      const marker = L.circleMarker([plane.latitude, plane.longitude], {
        radius: settings.markerSize / 10,
        fillColor: color,
        color: '#fff',
        weight: 2,
        opacity: 1,
        fillOpacity: 0.8
      }).addTo(map);

      marker.bindPopup(`
        <div class="text-sm">
          <strong>${plane.callsign || 'Unknown'}</strong><br/>
          Altitude: ${plane.altitude} ft<br/>
          Speed: ${plane.speed_knots} knots<br/>
          Type: ${plane.aircraft_type || 'N/A'}
        </div>
      `);

      marker.on('click', () => onMarkerClick(plane));

      markersRef.current[plane.icao_address] = marker;
    });
  }, [aircraft, settings.markerSize, onMarkerClick]);

  return <div ref={mapRef} className="w-full h-full" />;
}
