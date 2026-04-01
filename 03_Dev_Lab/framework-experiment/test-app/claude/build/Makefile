# SkyRide Android App — Makefile
# Usage: Run these targets from the project root (test-app/claude/build/)
# In Android Studio: Tools > Terminal, then `make <target>`

GRADLEW := ./gradlew
APP_MODULE := :app
BUILD_DIR := app/build

.PHONY: help setup build build-debug build-release bundle apk-path download download-release \
        install install-release run uninstall reinstall \
        test test-unit test-report lint lint-report check check-colors \
        clean clean-all stop deps deps-updates sync \
        devices logcat screenshot open

# ─── Default ──────────────────────────────────────────────────────────────────

help: ## Show this help message
	@echo ""
	@echo "  SkyRide Android — Makefile targets"
	@echo ""
	@awk 'BEGIN {FS = ":.*##"} /^[a-zA-Z_-]+:.*##/ { printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)
	@echo ""

# ─── Setup ────────────────────────────────────────────────────────────────────

setup: ## First-time setup: make gradlew executable and check local.properties
	@echo "→ Making gradlew executable..."
	chmod +x gradlew
	@echo "→ Checking local.properties..."
	@if [ ! -f local.properties ]; then \
		echo "sdk.dir=$$ANDROID_HOME" > local.properties; \
		echo "MAPS_API_KEY=YOUR_MAPS_API_KEY_HERE" >> local.properties; \
		echo "  ⚠  Created local.properties — add your MAPS_API_KEY before building"; \
	else \
		echo "  ✓ local.properties exists"; \
	fi
	@echo "→ Setup complete."

# ─── Build ────────────────────────────────────────────────────────────────────

build: ## Assemble debug APK
	$(GRADLEW) $(APP_MODULE):assembleDebug

build-debug: ## Assemble debug APK (explicit)
	$(GRADLEW) $(APP_MODULE):assembleDebug

build-release: ## Assemble release APK (requires signing config)
	$(GRADLEW) $(APP_MODULE):assembleRelease

bundle: ## Build release AAB (for Play Store)
	$(GRADLEW) $(APP_MODULE):bundleRelease

apk-path: ## Print the path to the debug APK
	@find $(BUILD_DIR)/outputs/apk/debug -name "*.apk" 2>/dev/null || echo "No APK found — run 'make build' first"

download: build ## Build debug APK and copy it to ~/Downloads/SkyRide-debug.apk
	@APK=$$(find $(BUILD_DIR)/outputs/apk/debug -name "*.apk" | head -1); \
	if [ -z "$$APK" ]; then echo "✗ APK not found after build"; exit 1; fi; \
	cp "$$APK" ~/Downloads/SkyRide-debug.apk; \
	echo "✓ APK saved to ~/Downloads/SkyRide-debug.apk"; \
	open ~/Downloads

download-release: build-release ## Build release APK and copy it to ~/Downloads/SkyRide-release.apk
	@APK=$$(find $(BUILD_DIR)/outputs/apk/release -name "*.apk" | head -1); \
	if [ -z "$$APK" ]; then echo "✗ APK not found after build"; exit 1; fi; \
	cp "$$APK" ~/Downloads/SkyRide-release.apk; \
	echo "✓ APK saved to ~/Downloads/SkyRide-release.apk"; \
	open ~/Downloads

# ─── Install & Run ────────────────────────────────────────────────────────────

install: build ## Build debug APK and install on connected device/emulator
	$(GRADLEW) $(APP_MODULE):installDebug

install-release: build-release ## Build release APK and install on connected device/emulator
	$(GRADLEW) $(APP_MODULE):installRelease

run: install ## Install and launch the app on connected device
	adb shell am start -n com.skyride.app/.MainActivity

uninstall: ## Uninstall the app from connected device
	adb uninstall com.skyride.app || true

reinstall: uninstall install ## Uninstall then reinstall (clean install)

# ─── Test ─────────────────────────────────────────────────────────────────────

test: ## Run all unit tests across all modules
	$(GRADLEW) test

test-unit: ## Run unit tests for a specific module (e.g. make test-unit MODULE=:feature:booking)
	$(GRADLEW) $(MODULE):test

test-report: ## Open the last test report in browser
	@REPORT=$$(find . -path "*/test-results/test/*.xml" | head -1 | xargs dirname); \
	HTML=$$(find . -path "*/reports/tests/test/index.html" | head -1); \
	if [ -n "$$HTML" ]; then open "$$HTML"; else echo "No test report found — run 'make test' first"; fi

# ─── Code Quality ─────────────────────────────────────────────────────────────

lint: ## Run Android Lint on all modules
	$(GRADLEW) lint

lint-report: lint ## Run lint and open the HTML report
	@open $$(find . -name "lint-results*.html" | head -1) 2>/dev/null || echo "Lint report not found"

check: lint test ## Run lint + all unit tests

check-colors: ## Verify no Color(0xFF) literals exist outside ColorPalette.kt (design rule)
	@echo "→ Checking for hardcoded colors outside ColorPalette.kt..."
	@VIOLATIONS=$$(grep -rn "Color(0x" --include="*.kt" . \
		| grep -v "ColorPalette.kt" \
		| grep -v "^Binary"); \
	if [ -z "$$VIOLATIONS" ]; then \
		echo "  ✓ No hardcoded colors found outside ColorPalette.kt"; \
	else \
		echo "  ✗ Hardcoded color violations found:"; \
		echo "$$VIOLATIONS"; \
		exit 1; \
	fi

# ─── Clean ────────────────────────────────────────────────────────────────────

clean: ## Clean build outputs
	$(GRADLEW) clean

clean-all: clean ## Clean build outputs + Gradle caches
	rm -rf ~/.gradle/caches/build-cache
	@echo "→ Gradle build cache cleared"

stop: ## Stop the Gradle daemon
	$(GRADLEW) --stop

# ─── Dependencies ─────────────────────────────────────────────────────────────

deps: ## Print dependency tree for the app module
	$(GRADLEW) $(APP_MODULE):dependencies

deps-updates: ## Check for dependency updates (requires gradle-versions-plugin)
	$(GRADLEW) dependencyUpdates

sync: ## Sync Gradle (equivalent to Android Studio's "Sync Project")
	$(GRADLEW) $(APP_MODULE):generateDebugSources

# ─── Utilities ────────────────────────────────────────────────────────────────

devices: ## List connected ADB devices and emulators
	adb devices -l

logcat: ## Tail filtered logcat for SkyRide only
	adb logcat -s SkyRide:V AndroidRuntime:E System.err:W

screenshot: ## Take a screenshot from connected device and save to ./screenshots/
	@mkdir -p screenshots
	@FILE="screenshots/skyride_$$(date +%Y%m%d_%H%M%S).png"; \
	adb exec-out screencap -p > "$$FILE" && echo "  ✓ Saved: $$FILE"

open: ## Open this project in Android Studio (macOS)
	@open -a "Android Studio" . 2>/dev/null || \
	studio . 2>/dev/null || \
	echo "Could not open Android Studio — open the build/ folder manually"
