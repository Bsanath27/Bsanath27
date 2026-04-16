import { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useAircraft } from '../context/AircraftContext';
import { createCustomMarker, getAltitudeColor } from '../services/mapUtils';

export default function MapComponent({ onMarkerClick }) {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const markersRef = useRef({});
  const trailsRef = useRef({}); // Store flight path trails
  const maxTrailPoints = 30; // Maximum points per trail
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

  // Update markers and trails when aircraft data changes
  useEffect(() => {
    if (!mapInstanceRef.current) return;

    const map = mapInstanceRef.current;

    // Update trails for aircraft (if enabled)
    if (settings.showTrails !== false) {
      aircraft.forEach(plane => {
        if (!plane.latitude || !plane.longitude) return;

        const trailId = plane.icao_address;
        let trail = trailsRef.current[trailId];

        if (!trail) {
          // Create new trail
          const trailColor = getAltitudeColor(plane.altitude);
          trail = {
            points: [[plane.latitude, plane.longitude]],
            polyline: L.polyline(
              [[plane.latitude, plane.longitude]],
              {
                color: trailColor,
                weight: 2,
                opacity: 0.4,
                dashArray: '5, 5',
                lineCap: 'round'
              }
            ).addTo(map)
          };
          trailsRef.current[trailId] = trail;
        } else {
          // Add point to existing trail
          trail.points.push([plane.latitude, plane.longitude]);
          if (trail.points.length > maxTrailPoints) {
            trail.points.shift();
          }
          trail.polyline.setLatLngs(trail.points);

          // Update trail color based on current altitude
          const trailColor = getAltitudeColor(plane.altitude);
          trail.polyline.setStyle({ color: trailColor });
        }
      });

      // Remove trails for aircraft no longer tracked
      Object.keys(trailsRef.current).forEach(trailId => {
        if (!aircraft.find(a => a.icao_address === trailId)) {
          map.removeLayer(trailsRef.current[trailId].polyline);
          delete trailsRef.current[trailId];
        }
      });
    }

    // Remove old markers
    Object.values(markersRef.current).forEach(marker => map.removeLayer(marker));
    markersRef.current = {};

    // Add new markers with custom icons
    aircraft.forEach(plane => {
      if (!plane.latitude || !plane.longitude) return;

      const icon = createCustomMarker(plane, settings.markerSize);

      const marker = L.marker([plane.latitude, plane.longitude], { icon }).addTo(map);

      // Enhanced popup with more details
      const popupContent = `
        <div style="font-size: 12px; width: 200px;">
          <div style="font-weight: bold; color: #3b82f6; margin-bottom: 8px;">
            ${plane.callsign || 'Unknown'}
          </div>
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 8px; font-size: 11px;">
            <div>
              <div style="color: #999; font-size: 9px;">Type</div>
              <div>${plane.aircraft_type || 'N/A'}</div>
            </div>
            <div>
              <div style="color: #999; font-size: 9px;">ICAO</div>
              <div style="font-family: monospace; font-size: 10px;">${plane.icao_address}</div>
            </div>
            <div>
              <div style="color: #999; font-size: 9px;">Altitude</div>
              <div style="color: #22c55e; font-weight: bold;">${plane.altitude} ft</div>
            </div>
            <div>
              <div style="color: #999; font-size: 9px;">Speed</div>
              <div style="color: #f59e0b; font-weight: bold;">${plane.speed_knots} kts</div>
            </div>
            <div>
              <div style="color: #999; font-size: 9px;">Heading</div>
              <div>${plane.heading || 'N/A'}°</div>
            </div>
            <div>
              <div style="color: #999; font-size: 9px;">Latitude</div>
              <div style="font-size: 10px;">${plane.latitude.toFixed(4)}</div>
            </div>
          </div>
        </div>
      `;

      marker.bindPopup(popupContent);
      marker.on('click', () => onMarkerClick(plane));

      markersRef.current[plane.icao_address] = marker;
    });
  }, [aircraft, settings.markerSize, settings.showTrails, onMarkerClick]);

  return <div ref={mapRef} className="w-full h-full" />;
}
