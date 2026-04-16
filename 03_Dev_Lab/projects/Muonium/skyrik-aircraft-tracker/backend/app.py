from flask import Flask
from flask_cors import CORS
from flask_socketio import SocketIO, emit
import threading
import time
from config import Config
from routes import api
from models import AircraftModel

app = Flask(__name__)
app.config.from_object(Config)

# Enable CORS for all routes
CORS(app,
     resources={r"/*": {"origins": ["http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:3000"]}},
     supports_credentials=True,
     methods=["GET", "POST", "PUT", "DELETE", "OPTIONS"])

socketio = SocketIO(app,
                   cors_allowed_origins=["http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:3000"],
                   async_mode='threading',
                   ping_timeout=60,
                   ping_interval=25)

# Register blueprints
app.register_blueprint(api)

# Initialize model
aircraft_model = AircraftModel()

# WebSocket events
@socketio.on('connect')
def handle_connect():
    print('Client connected')
    emit('response', {'data': 'Connected to server'})

@socketio.on('disconnect')
def handle_disconnect():
    print('Client disconnected')

@socketio.on('request_update')
def handle_request():
    """Send live aircraft data on request."""
    aircraft = aircraft_model.get_all_aircraft()
    emit('aircraft_update', {'aircraft': aircraft}, broadcast=True)

# Background thread for broadcasting
def broadcast_thread():
    """Broadcast aircraft data every N seconds."""
    print(f"Broadcast thread started. Broadcasting every {Config.POLL_INTERVAL}s")
    while True:
        try:
            with app.app_context():
                aircraft = aircraft_model.get_all_aircraft()
                if aircraft:
                    socketio.emit('aircraft_update', {'aircraft': aircraft}, broadcast=True, skip_sid=None)
                    print(f"Broadcast: {len(aircraft)} aircraft to all clients")
        except Exception as e:
            print(f"Broadcast error: {e}")

        time.sleep(Config.POLL_INTERVAL)

if __name__ == '__main__':
    # Start broadcast thread
    thread = threading.Thread(target=broadcast_thread, daemon=True)
    thread.start()

    # Run server
    socketio.run(app, debug=True, host='127.0.0.1', port=5000, allow_unsafe_werkzeug=True)
