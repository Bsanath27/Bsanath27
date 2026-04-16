// frontend/src/utils/helicopterUtils.js

/**
 * Detect if aircraft is a helicopter based on aircraft type
 */
export function isHelicopter(aircraft) {
  if (!aircraft || !aircraft.aircraft_type) return false;
  const type = aircraft.aircraft_type.toLowerCase();
  const helicopterKeywords = [
    'helicopter', 'heli', 'h135', 'h145', 'h125', 'h225',
    'bell', 'sikorsky', 's-76', 'ah-64', 'uh-60', 'uh-1',
    'airbus h', 'robinson', 'eurocopter', 'augusta'
  ];
  return helicopterKeywords.some(keyword => type.includes(keyword));
}

/**
 * Check if helicopter is operating at low altitude (tactical/emergency)
 */
export function isLowAltitudeHelicopter(aircraft) {
  return isHelicopter(aircraft) && aircraft.altitude < 2000;
}

/**
 * Get display name for aircraft type
 */
export function getAircraftDisplayName(aircraft) {
  if (isHelicopter(aircraft)) {
    return `🚁 ${aircraft.aircraft_type}`;
  }
  return `✈️ ${aircraft.aircraft_type}`;
}
