#!/usr/bin/env bash
set -euo pipefail
# Simple smoke test for demo: starts both servers (if needed) and verifies demo pages

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
echo "Running smoke demo checks from $ROOT_DIR"

cd "$ROOT_DIR"

echo "Starting helipads server (background if not running)"
make -C helipads run || true
echo "Starting tn-pincodes server (background if not running)"
make -C tn-pincodes run || true

sleep 1

echo "Checking helipads intel pages..."
curl -fsS -I http://localhost:8001/skyrik_helipad_intel_3d.html >/dev/null && echo "OK: helipads 3d" || (echo "FAIL: helipads 3d"; exit 1)
curl -fsS -I http://localhost:8001/skyrik_helipad_intel_map.html >/dev/null && echo "OK: helipads map" || (echo "FAIL: helipads map"; exit 1)

echo "Checking tn-pincodes map..."
curl -fsS -I http://localhost:8000/tamilnadu_pincode_map.html >/dev/null && echo "OK: tn-pincodes map" || (echo "FAIL: tn-pincodes map"; exit 1)

echo "Smoke demo checks passed"

echo "(Servers are left running; stop them with 'make stop' at repo root)"
