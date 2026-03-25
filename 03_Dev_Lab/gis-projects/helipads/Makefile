# Makefile for running helipads static maps (macOS / Linux)

PORT ?= 8001
HTML ?= skyrik_helipads_map.html
PIDFILE := .server.pid
URL := http://localhost:$(PORT)/$(HTML)

.PHONY: help serve run stop open live

help:
	@echo "Helipads Makefile - targets:"
	@echo "  serve   - Run a foreground Python HTTP server on $(PORT)"
	@echo "  run     - Start Python HTTP server in background (writes PID to $(PIDFILE))"
	@echo "  stop    - Stop background server started by 'make run'"
	@echo "  open    - Open $(HTML) in default browser"
	@echo "  live    - Run live-server via npx (if installed) for live reload"

serve:
	@echo "Starting foreground server on port $(PORT) — visit $(URL)"
	python3 -m http.server $(PORT)

run:
	@if [ -f $(PIDFILE) ]; then \
		pid=$$(cat $(PIDFILE)); \
		if kill -0 $$pid >/dev/null 2>&1; then \
			echo "Server appears to be running (PID $$pid). Use 'make stop' first."; exit 1; \
		else \
			echo "Removing stale PID file (PID $$pid not running)."; rm -f $(PIDFILE); \
		fi; \
	fi
	@nohup python3 -m http.server $(PORT) > server.log 2>&1 & echo $$! > $(PIDFILE)
	@sleep 1
	@echo "Server started in background at $(URL) (logs → server.log)"

stop:
	@if [ -f $(PIDFILE) ]; then \
		pid=$$(cat $(PIDFILE)); \
		if kill -0 $$pid >/dev/null 2>&1; then \
			kill $$pid 2>/dev/null || true; echo "Stopped server ($$pid)."; \
		else \
			echo "No process $$pid — removing stale PID file."; \
		fi; \
		rm -f $(PIDFILE); \
	else \
		echo "No server PID file ($(PIDFILE)) found."; \
	fi

open:
	@echo "Opening $(URL) in default browser..."
	@open $(URL)

live:
	@if command -v npx >/dev/null 2>&1; then \
		npx live-server --port=$(PORT) --open=$(HTML); \
	else \
		echo "npx/live-server not found. Install with 'npm i -g live-server' or use 'make serve'"; exit 1; \
	fi

# Specific intel targets: open both intel 3d and intel_map HTMLs
INTEL_3D := skyrik_helipad_intel_3d.html
INTEL_MAP := skyrik_helipad_intel_map.html

.PHONY: intel intel-open intel-serve

# Start background server (if not running) and open both intel pages
intel: run
	@$(MAKE) intel-open

intel-open:
	@echo "Opening intel pages on port $(PORT)..."
	@open http://localhost:$(PORT)/$(INTEL_3D)
	@open http://localhost:$(PORT)/$(INTEL_MAP)

# Foreground serve and open both pages
intel-serve: serve
	@open http://localhost:$(PORT)/$(INTEL_3D)
	@open http://localhost:$(PORT)/$(INTEL_MAP)
