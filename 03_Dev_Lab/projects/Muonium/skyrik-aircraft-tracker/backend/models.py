import sqlite3
from datetime import datetime, timedelta
from config import Config

class AircraftModel:
    def __init__(self, db_path=Config.DATABASE_PATH):
        self.db_path = db_path

    def get_all_aircraft(self, limit=100):
        """Get all current aircraft."""
        conn = sqlite3.connect(self.db_path)
        conn.row_factory = sqlite3.Row
        cursor = conn.cursor()

        cursor.execute("""
            SELECT * FROM aircraft
            ORDER BY last_update DESC
            LIMIT ?
        """, (limit,))

        aircraft = [dict(row) for row in cursor.fetchall()]
        conn.close()
        return aircraft

    def get_aircraft_by_icao(self, icao_address):
        """Get single aircraft details."""
        conn = sqlite3.connect(self.db_path)
        conn.row_factory = sqlite3.Row
        cursor = conn.cursor()

        cursor.execute("""
            SELECT * FROM aircraft
            WHERE icao_address = ?
        """, (icao_address,))

        row = cursor.fetchone()
        conn.close()

        return dict(row) if row else None

    def search_aircraft(self, callsign=None, aircraft_type=None,
                       altitude_min=None, altitude_max=None,
                       speed_min=None, speed_max=None):
        """Search aircraft with filters."""
        conn = sqlite3.connect(self.db_path)
        conn.row_factory = sqlite3.Row
        cursor = conn.cursor()

        query = "SELECT * FROM aircraft WHERE 1=1"
        params = []

        if callsign:
            query += " AND callsign LIKE ?"
            params.append(f"%{callsign}%")

        if aircraft_type:
            query += " AND aircraft_type = ?"
            params.append(aircraft_type)

        if altitude_min is not None:
            query += " AND altitude >= ?"
            params.append(altitude_min)

        if altitude_max is not None:
            query += " AND altitude <= ?"
            params.append(altitude_max)

        if speed_min is not None:
            query += " AND speed_knots >= ?"
            params.append(speed_min)

        if speed_max is not None:
            query += " AND speed_knots <= ?"
            params.append(speed_max)

        cursor.execute(query, params)
        aircraft = [dict(row) for row in cursor.fetchall()]
        conn.close()

        return aircraft

    def get_statistics(self):
        """Get statistics for dashboard."""
        conn = sqlite3.connect(self.db_path)
        cursor = conn.cursor()

        # Total aircraft
        cursor.execute("SELECT COUNT(*) FROM aircraft")
        total = cursor.fetchone()[0]

        # Max altitude
        cursor.execute("SELECT MAX(altitude) FROM aircraft")
        max_altitude = cursor.fetchone()[0] or 0

        # Max speed
        cursor.execute("SELECT MAX(speed_knots) FROM aircraft")
        max_speed = cursor.fetchone()[0] or 0

        # Aircraft by type
        cursor.execute("""
            SELECT aircraft_type, COUNT(*) as count
            FROM aircraft
            GROUP BY aircraft_type
        """)
        by_type = {row[0]: row[1] for row in cursor.fetchall()}

        # Altitude bands
        cursor.execute("""
            SELECT
                CASE
                    WHEN altitude < 10000 THEN 'Low (< 10k ft)'
                    WHEN altitude < 30000 THEN 'Medium (10-30k ft)'
                    ELSE 'High (> 30k ft)'
                END as band,
                COUNT(*) as count
            FROM aircraft
            GROUP BY band
        """)
        altitude_bands = {row[0]: row[1] for row in cursor.fetchall()}

        conn.close()

        return {
            "total": total,
            "max_altitude": max_altitude,
            "max_speed": max_speed,
            "by_type": by_type,
            "altitude_bands": altitude_bands
        }
