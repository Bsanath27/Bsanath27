# Top-level Makefile to run subprojects (`tn-pincodes` and `helipads`)

.PHONY: help tn-help helipads-help \
	 tn-serve tn-run tn-stop tn-open tn-live \
 	 helipads-serve helipads-run helipads-stop helipads-open helipads-live
.PHONY: helipads-intel

help:
	@echo "Usage: make [target]"
	@echo "Available groups: tn-pincodes, helipads"
	@echo "Run 'make tn-help' or 'make helipads-help' for subproject targets."

stop:
	@$(MAKE) helipads-stop || true
	@$(MAKE) tn-stop || true

tn-help:
	@$(MAKE) -C tn-pincodes help

helipads-help:
	@$(MAKE) -C helipads help

helipads-intel:
	@$(MAKE) -C helipads intel

# tn-pincodes delegation
tn-serve:
	@$(MAKE) -C tn-pincodes serve

tn-run:
	@$(MAKE) -C tn-pincodes run

tn-stop:
	@$(MAKE) -C tn-pincodes stop

tn-open:
	@$(MAKE) -C tn-pincodes open

tn-live:
	@$(MAKE) -C tn-pincodes live

# helipads delegation
helipads-serve:
	@$(MAKE) -C helipads serve

helipads-run:
	@$(MAKE) -C helipads run

helipads-stop:
	@$(MAKE) -C helipads stop

helipads-open:
	@$(MAKE) -C helipads open

helipads-live:
	@$(MAKE) -C helipads live
