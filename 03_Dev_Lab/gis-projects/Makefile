.PHONY: help helipads-intel tn-pincodes serve stop

help:
	@echo "Usage: make <target>"
	@echo "Targets:"
	@echo "  helipads-intel   Open helipads intel HTML files"
	@echo "  tn-pincodes      Serve tamilnadu pincode map"
	@echo "  serve            Start simple http server on 8000"
	@echo "  stop             Stop servers started by this Makefile"

helipads-intel:
	@open helipads/skyrik_helipad_intel_3d.html helipads/skyrik_helipad_intel_map.html

tn-pincodes:
	@python3 -m http.server 8001 --directory tn-pincodes & echo $$! > tn-pincodes/.server.pid; sleep 1; open http://localhost:8001/tamilnadu_pincode_map.html

serve:
	@python3 -m http.server 8000 & echo $$! > .server.pid; sleep 1; echo "serving on http://localhost:8000"

stop:
	@if [ -f .server.pid ]; then kill `cat .server.pid` 2>/dev/null || true; rm -f .server.pid; fi
	@if [ -f tn-pincodes/.server.pid ]; then kill `cat tn-pincodes/.server.pid` 2>/dev/null || true; rm -f tn-pincodes/.server.pid; fi
