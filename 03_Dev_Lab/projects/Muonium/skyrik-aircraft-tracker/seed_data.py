#!/usr/bin/env python3
"""Seed the database with sample aircraft data for demo purposes."""
import sqlite3
from datetime import datetime
import random

DATABASE_PATH = "data/aircraft.db"

# Sample aircraft data
SAMPLE_AIRCRAFT = [
    # Commercial Planes
    {"icao": "A4B205", "callsign": "BA747", "aircraft_type": "Boeing 747", "latitude": 51.5074, "longitude": -0.1278, "altitude": 35000, "speed_knots": 450, "heading": 90},
    {"icao": "A4B206", "callsign": "AA380", "aircraft_type": "Airbus A380", "latitude": 48.8566, "longitude": 2.3522, "altitude": 38000, "speed_knots": 460, "heading": 180},
    {"icao": "A4B207", "callsign": "UA777", "aircraft_type": "Boeing 777", "latitude": 40.7128, "longitude": -74.0060, "altitude": 32000, "speed_knots": 480, "heading": 270},
    {"icao": "A4B208", "callsign": "DL330", "aircraft_type": "Airbus A330", "latitude": 33.9425, "longitude": -118.4081, "altitude": 30000, "speed_knots": 470, "heading": 45},
    {"icao": "A4B209", "callsign": "UA688", "aircraft_type": "Boeing 787", "latitude": 37.7749, "longitude": -122.4194, "altitude": 36000, "speed_knots": 490, "heading": 135},

    # Regional/Smaller Planes
    {"icao": "A4B210", "callsign": "SKW6587", "aircraft_type": "Bombardier Q400", "latitude": 47.6062, "longitude": -122.3321, "altitude": 18000, "speed_knots": 250, "heading": 225},
    {"icao": "A4B211", "callsign": "ASA1234", "aircraft_type": "Boeing 737", "latitude": 58.2975, "longitude": -134.4197, "altitude": 25000, "speed_knots": 400, "heading": 315},

    # Helicopters
    {"icao": "A4B212", "callsign": "NYPD1", "aircraft_type": "Bell 407", "latitude": 40.7580, "longitude": -73.9855, "altitude": 1500, "speed_knots": 120, "heading": 0},
    {"icao": "A4B213", "callsign": "LAA1", "aircraft_type": "Airbus H135", "latitude": 34.0522, "longitude": -118.2437, "altitude": 2000, "speed_knots": 110, "heading": 90},
    {"icao": "A4B214", "callsign": "CHC1", "aircraft_type": "Sikorsky S-76", "latitude": 34.7465, "longitude": -92.2896, "altitude": 1800, "speed_knots": 100, "heading": 180},
    {"icao": "A4B215", "callsign": "NEWSCOPTER", "aircraft_type": "Robinson R66", "latitude": 35.0896, "longitude": -106.6055, "altitude": 2500, "speed_knots": 80, "heading": 270},
    {"icao": "A4B216", "callsign": "MEDIC1", "aircraft_type": "Bell 429", "latitude": 39.7392, "longitude": -104.9903, "altitude": 3000, "speed_knots": 130, "heading": 45},

    # More commercial aircraft
    {"icao": "A4B217", "callsign": "FDX1234", "aircraft_type": "Boeing 767", "latitude": 42.3601, "longitude": -71.0589, "altitude": 28000, "speed_knots": 420, "heading": 120},
    {"icao": "A4B218", "callsign": "UPS1234", "aircraft_type": "Airbus A300", "latitude": 33.7490, "longitude": -84.3880, "altitude": 29000, "speed_knots": 430, "heading": 210},
    {"icao": "A4B219", "callsign": "DAL456", "aircraft_type": "Boeing 757", "latitude": 25.7617, "longitude": -80.1918, "altitude": 31000, "speed_knots": 440, "heading": 300},
    {"icao": "A4B220", "callsign": "AAL789", "aircraft_type": "Airbus A321", "latitude": 28.4294, "longitude": -81.3089, "altitude": 27000, "speed_knots": 410, "heading": 60},
]

def seed_database():
    """Insert sample aircraft data into the database."""
    conn = sqlite3.connect(DATABASE_PATH)
    cursor = conn.cursor()

    # Get current timestamp
    now = datetime.utcnow().strftime("%Y-%m-%d %H:%M:%S")

    try:
        for aircraft in SAMPLE_AIRCRAFT:
            # Add some randomization to make data more realistic
            altitude = aircraft["altitude"] + random.randint(-1000, 1000)
            speed = aircraft["speed_knots"] + random.randint(-20, 20)
            latitude = aircraft["latitude"] + random.uniform(-0.5, 0.5)
            longitude = aircraft["longitude"] + random.uniform(-0.5, 0.5)
            heading = aircraft["heading"] + random.randint(-20, 20)
            heading = heading % 360

            cursor.execute("""
                INSERT OR REPLACE INTO aircraft
                (icao_address, callsign, latitude, longitude, altitude, speed_knots, heading, aircraft_type, last_update, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                aircraft["icao"],
                aircraft["callsign"],
                latitude,
                longitude,
                altitude,
                speed,
                heading,
                aircraft["aircraft_type"],
                now,
                now
            ))

        conn.commit()

        # Show statistics
        cursor.execute("SELECT COUNT(*) FROM aircraft")
        count = cursor.fetchone()[0]

        cursor.execute("SELECT aircraft_type, COUNT(*) as count FROM aircraft GROUP BY aircraft_type")
        types = cursor.fetchall()

        print(f"✓ Database seeded with {count} aircraft")
        print("\nAircraft by type:")
        for atype, cnt in types:
            print(f"  {atype}: {cnt}")

    except Exception as e:
        print(f"Error seeding database: {e}")
    finally:
        conn.close()

if __name__ == "__main__":
    seed_database()
