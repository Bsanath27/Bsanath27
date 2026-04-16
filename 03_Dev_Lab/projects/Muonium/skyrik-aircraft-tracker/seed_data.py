#!/usr/bin/env python3
"""Seed the database with sample aircraft data for demo purposes."""
import sqlite3
from datetime import datetime
import random

DATABASE_PATH = "data/aircraft.db"

# Sample aircraft data - India focused with helicopters
SAMPLE_AIRCRAFT = [
    # Indian Helicopters - Military (Indian Air Force)
    {"icao": "VT4001", "callsign": "IAF001", "aircraft_type": "HAL Dhruv", "latitude": 28.7041, "longitude": 77.1025, "altitude": 3500, "speed_knots": 110, "heading": 45},
    {"icao": "VT4002", "callsign": "IAF002", "aircraft_type": "HAL Dhruv", "latitude": 28.6139, "longitude": 77.2090, "altitude": 3000, "speed_knots": 105, "heading": 120},

    # Indian Helicopters - Coast Guard & Navy
    {"icao": "VT5001", "callsign": "ICG-HELI01", "aircraft_type": "Sikorsky S-76", "latitude": 19.0176, "longitude": 72.8479, "altitude": 1200, "speed_knots": 95, "heading": 180},
    {"icao": "VT5002", "callsign": "INS-MEDIC", "aircraft_type": "Airbus H135", "latitude": 13.1939, "longitude": 80.2815, "altitude": 1500, "speed_knots": 100, "heading": 270},

    # Indian Helicopters - Medical/Emergency Services
    {"icao": "VT6001", "callsign": "MEDEVAC-DEL", "aircraft_type": "Bell 407", "latitude": 28.5355, "longitude": 77.1910, "altitude": 1800, "speed_knots": 115, "heading": 90},
    {"icao": "VT6002", "callsign": "MEDEVAC-BLR", "aircraft_type": "Robinson R66", "latitude": 12.9716, "longitude": 77.5946, "altitude": 1200, "speed_knots": 85, "heading": 45},
    {"icao": "VT6003", "callsign": "FLYCARE-MUM", "aircraft_type": "Airbus H135", "latitude": 19.0876, "longitude": 72.8691, "altitude": 1400, "speed_knots": 105, "heading": 135},

    # Indian Helicopters - Police & Law Enforcement
    {"icao": "VT7001", "callsign": "POLICE-1", "aircraft_type": "Sikorsky S-76", "latitude": 28.6329, "longitude": 77.2197, "altitude": 1600, "speed_knots": 100, "heading": 180},
    {"icao": "VT7002", "callsign": "POLICE-MUM", "aircraft_type": "Bell 407", "latitude": 19.0760, "longitude": 72.8777, "altitude": 1400, "speed_knots": 110, "heading": 225},

    # Indian Helicopters - Commercial Operators (Pawan Hans, Helicopter Air Taxi)
    {"icao": "VT8001", "callsign": "PH-001", "aircraft_type": "HAL Dhruv", "latitude": 22.5726, "longitude": 88.3639, "altitude": 2000, "speed_knots": 100, "heading": 0},
    {"icao": "VT8002", "callsign": "PUSHPAK-1", "aircraft_type": "Robinson R66", "latitude": 17.3850, "longitude": 78.4867, "altitude": 1300, "speed_knots": 80, "heading": 90},

    # Indian Helicopters - News & Media
    {"icao": "VT9001", "callsign": "NEWS-HELI-DEL", "aircraft_type": "Robinson R66", "latitude": 28.7041, "longitude": 77.0573, "altitude": 2200, "speed_knots": 95, "heading": 270},
    {"icao": "VT9002", "callsign": "NEWS-HELI-BLR", "aircraft_type": "Bell 407", "latitude": 13.1939, "longitude": 77.6245, "altitude": 1800, "speed_knots": 105, "heading": 315},

    # International Commercial Aircraft in India (for context)
    {"icao": "A4B205", "callsign": "AI101", "aircraft_type": "Boeing 787", "latitude": 28.5721, "longitude": 77.1200, "altitude": 35000, "speed_knots": 450, "heading": 90},
    {"icao": "A4B206", "callsign": "SG123", "aircraft_type": "Airbus A380", "latitude": 19.0880, "longitude": 72.8678, "altitude": 38000, "speed_knots": 460, "heading": 180},
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
