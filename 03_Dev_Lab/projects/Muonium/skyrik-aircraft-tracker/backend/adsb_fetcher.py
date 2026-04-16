"""Fetch real ADS-B data from OpenSky Network and integrate with local data."""
import sqlite3
import requests
import time
from datetime import datetime
from config import Config

class ADSBFetcher:
    """Fetch real aircraft data from OpenSky Network API"""

    # India bounding box (roughly)
    INDIA_BOUNDS = {
        "min_lat": 8.0,
        "max_lat": 35.0,
        "min_lon": 68.0,
        "max_lon": 97.0
    }

    OPENSKY_URL = "https://opensky-api.org/api/1.0/states/all"

    def __init__(self, db_path=Config.DATABASE_PATH):
        self.db_path = db_path
        self.session = requests.Session()

    def fetch_india_aircraft(self):
        """Fetch aircraft over India from OpenSky Network"""
        try:
            params = {
                "lamin": self.INDIA_BOUNDS["min_lat"],
                "lamax": self.INDIA_BOUNDS["max_lat"],
                "lomin": self.INDIA_BOUNDS["min_lon"],
                "lomax": self.INDIA_BOUNDS["max_lon"]
            }

            response = self.session.get(self.OPENSKY_URL, params=params, timeout=10)
            response.raise_for_status()

            data = response.json()
            aircraft = []

            if data.get("states"):
                for state in data["states"]:
                    # OpenSky API returns: [icao24, callsign, origin_country, time_position,
                    # last_contact, longitude, latitude, baro_altitude, on_ground, velocity,
                    # true_track, vertical_rate, sensors, geo_altitude, squawk, spi, position_source]

                    if state[5] is None or state[6] is None:  # No lat/lon
                        continue

                    aircraft.append({
                        "icao": state[0] or f"UNKNOWN-{int(time.time())}",
                        "callsign": (state[1] or "").strip(),
                        "latitude": state[6],
                        "longitude": state[5],
                        "altitude": int(state[7] or 0),
                        "speed_knots": int((state[9] or 0) * 1.94384),  # Convert m/s to knots
                        "heading": int(state[10] or 0),
                        "aircraft_type": state[2] or "Unknown",
                        "is_live": True
                    })

            return aircraft

        except Exception as e:
            print(f"Error fetching ADS-B data: {e}")
            return []

    def insert_aircraft(self, aircraft_list):
        """Insert or update aircraft in database"""
        try:
            conn = sqlite3.connect(self.db_path)
            cursor = conn.cursor()
            now = datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S")

            for aircraft in aircraft_list:
                cursor.execute("""
                    INSERT OR REPLACE INTO aircraft
                    (icao_address, callsign, latitude, longitude, altitude, speed_knots,
                     heading, aircraft_type, last_update, created_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, COALESCE(
                        (SELECT created_at FROM aircraft WHERE icao_address = ?), ?
                    ))
                """, (
                    aircraft["icao"],
                    aircraft["callsign"],
                    aircraft["latitude"],
                    aircraft["longitude"],
                    aircraft["altitude"],
                    aircraft["speed_knots"],
                    aircraft["heading"],
                    aircraft["aircraft_type"],
                    now,
                    aircraft["icao"],
                    now
                ))

            conn.commit()
            conn.close()
            return True

        except Exception as e:
            print(f"Error inserting aircraft: {e}")
            return False

    def sync_live_data(self):
        """Fetch and sync live data with database"""
        aircraft = self.fetch_india_aircraft()
        if aircraft:
            return self.insert_aircraft(aircraft)
        return False


def start_live_tracking(db_path=Config.DATABASE_PATH, interval=30):
    """Start background live tracking service"""
    fetcher = ADSBFetcher(db_path)

    print(f"Starting live ADS-B tracking (fetching every {interval}s)...")

    while True:
        try:
            success = fetcher.sync_live_data()
            if success:
                print(f"✓ Live data synced at {datetime.now().strftime('%H:%M:%S')}")
            else:
                print(f"⚠ Sync failed at {datetime.now().strftime('%H:%M:%S')}")
        except Exception as e:
            print(f"✗ Error in live tracking: {e}")

        time.sleep(interval)


if __name__ == "__main__":
    start_live_tracking()
