.PHONY: help install dev backend frontend build clean stop test

# Default target
help:
	@echo "Skyrik Aircraft Tracker - Available Commands:"
	@echo ""
	@echo "Setup & Installation:"
	@echo "  make install          - Install all dependencies (backend + frontend)"
	@echo "  make install-backend  - Install Python dependencies"
	@echo "  make install-frontend - Install Node dependencies"
	@echo ""
	@echo "Development:"
	@echo "  make dev              - Start both backend and frontend (parallel)"
	@echo "  make backend          - Start Flask backend only (port 5000)"
	@echo "  make frontend         - Start React dev server only (port 5173)"
	@echo ""
	@echo "Production:"
	@echo "  make build            - Build frontend for production"
	@echo "  make build-backend    - Compile backend (check syntax)"
	@echo ""
	@echo "Utilities:"
	@echo "  make test             - Test backend health endpoint"
	@echo "  make clean            - Clean build artifacts and cache"
	@echo "  make stop             - Stop all running processes"
	@echo "  make lint             - Lint Python backend"

# Install all dependencies
install: install-backend install-frontend

# Install backend dependencies
install-backend:
	@echo "Installing Python backend dependencies..."
	cd backend && pip install -r requirements.txt

# Install frontend dependencies
install-frontend:
	@echo "Installing Node frontend dependencies..."
	cd frontend && npm install

# Start both services in parallel
dev: backend-dev frontend-dev

# Start backend in background
backend-dev:
	@echo "Starting Flask backend on http://127.0.0.1:5000..."
	cd backend && python3 run.py &

# Start frontend in background
frontend-dev:
	@echo "Starting React frontend on http://localhost:5173..."
	cd frontend && npm run dev &

# Start backend only
backend:
	@echo "Starting Flask backend on http://127.0.0.1:5000..."
	cd backend && python3 run.py

# Start frontend only
frontend:
	@echo "Starting React frontend on http://localhost:5173..."
	cd frontend && npm run dev

# Build backend (syntax check)
build-backend:
	@echo "Checking backend Python syntax..."
	python3 -m py_compile backend/app.py backend/config.py backend/models.py backend/routes.py
	@echo "Backend syntax OK ✓"

# Build frontend for production
build-frontend: install-frontend
	@echo "Building React app for production..."
	cd frontend && npm run build
	@echo "Build complete: frontend/dist/"

# Full production build
build: build-backend build-frontend
	@echo "Production build complete! ✓"

# Clean build artifacts and cache
clean:
	@echo "Cleaning build artifacts..."
	rm -rf frontend/dist
	rm -rf frontend/node_modules/.vite
	rm -f backend/__pycache__/*.pyc
	find . -type d -name '__pycache__' -exec rm -rf {} + 2>/dev/null || true
	@echo "Clean complete ✓"

# Stop all running processes
stop:
	@echo "Stopping all services..."
	pkill -f "python3.*run.py" || true
	pkill -f "npm run dev" || true
	pkill -f "node.*vite" || true
	@echo "Services stopped ✓"

# Test backend health endpoint
test:
	@echo "Testing backend health endpoint..."
	@sleep 2
	@curl -s http://127.0.0.1:5000/api/health | python3 -m json.tool || echo "Backend not running. Start with: make backend"

# Lint backend Python code
lint:
	@echo "Linting Python backend..."
	python3 -m py_compile backend/app.py backend/config.py backend/models.py backend/routes.py
	@echo "No syntax errors found ✓"

# Quick start for development
quick-start: install dev
	@echo ""
	@echo "=========================================="
	@echo "Skyrik Aircraft Tracker is starting..."
	@echo "=========================================="
	@echo ""
	@echo "Frontend:  http://localhost:5173"
	@echo "Backend:   http://127.0.0.1:5000"
	@echo ""
	@echo "Press Ctrl+C to stop"

# Show project structure
info:
	@echo "Skyrik Aircraft Tracker - Project Structure:"
	@echo ""
	@echo "Backend (Flask):"
	@ls -lh backend/*.py | awk '{print "  " $$NF " (" $$5 ")"}'
	@echo ""
	@echo "Frontend (React):"
	@find frontend/src -type f \( -name "*.jsx" -o -name "*.js" \) | sort | awk '{print "  " $$0}'
