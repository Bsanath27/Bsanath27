import os
from pathlib import Path

class Config:
    # Database
    PROJECT_ROOT = Path(__file__).parent.parent
    DATABASE_PATH = os.getenv("DATABASE_PATH", str(PROJECT_ROOT / "data" / "aircraft.db"))

    # Flask
    SECRET_KEY = os.getenv("SECRET_KEY", "dev-secret-key")
    CORS_ORIGINS = ["http://localhost:5173", "http://localhost:3000"]

    # WebSocket
    SOCKETIO_ASYNC_MODE = "threading"

    # API
    POLL_INTERVAL = 10  # seconds between broadcasts
    MAX_HISTORY = 100   # max aircraft per broadcast
