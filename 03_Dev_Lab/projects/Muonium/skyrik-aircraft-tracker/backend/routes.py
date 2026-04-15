from flask import Blueprint, jsonify, request
from models import AircraftModel

api = Blueprint('api', __name__, url_prefix='/api')
aircraft_model = AircraftModel()

@api.route('/aircraft', methods=['GET'])
def get_aircraft():
    """Get all aircraft."""
    limit = request.args.get('limit', 100, type=int)
    aircraft = aircraft_model.get_all_aircraft(limit)
    return jsonify(aircraft)

@api.route('/aircraft/<icao>', methods=['GET'])
def get_aircraft_detail(icao):
    """Get single aircraft details."""
    aircraft = aircraft_model.get_aircraft_by_icao(icao)
    if not aircraft:
        return jsonify({"error": "Aircraft not found"}), 404
    return jsonify(aircraft)

@api.route('/search', methods=['GET'])
def search():
    """Search aircraft with filters."""
    callsign = request.args.get('callsign')
    aircraft_type = request.args.get('type')
    altitude_min = request.args.get('altitude_min', type=int)
    altitude_max = request.args.get('altitude_max', type=int)
    speed_min = request.args.get('speed_min', type=int)
    speed_max = request.args.get('speed_max', type=int)

    results = aircraft_model.search_aircraft(
        callsign=callsign,
        aircraft_type=aircraft_type,
        altitude_min=altitude_min,
        altitude_max=altitude_max,
        speed_min=speed_min,
        speed_max=speed_max
    )
    return jsonify(results)

@api.route('/statistics', methods=['GET'])
def get_stats():
    """Get statistics."""
    stats = aircraft_model.get_statistics()
    return jsonify(stats)
