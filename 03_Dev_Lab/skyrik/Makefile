.PHONY: install start lan ios android clear-cache data-setup clean

# ==========================================
# SKYRIK DEVELOPMENT MAKEFILE
# ==========================================

# 📦 Install all dependencies (Node + Python)
install:
	@echo "Installing frontend dependencies..."
	cd skyrik-react && pnpm install
	@echo "Installing Python data dependencies..."
	pip3 install -r requirements.txt

# 🚀 Run the Expo development server with tunnel (Remote Access)
start:
	cd skyrik-react && npx expo start --tunnel

# 🌐 Run the Expo development server natively on local network
lan:
	cd skyrik-react && npx expo start

# 📱 Run iOS Simulator
ios:
	cd skyrik-react && npx expo start --ios

# 🤖 Run Android Emulator
android:
	cd skyrik-react && npx expo start --android

# 🧹 Clear Metro Bundler Cache
clear-cache:
	cd skyrik-react && npx expo start --clear

# 🐍 Run local Python data engineering pipelines
data-setup:
	@echo "Running Data Pipeline..."
	python3 skyrik-react/data/clean_data.py
	python3 skyrik-react/data/align_keys.py
	@echo "Data successfully sanitized and integrated!"

# ☢️ Complete Nuclear Reset (rm nodes and reinstall)
clean:
	@echo "Nuking caches and node_modules..."
	cd skyrik-react && rm -rf node_modules .expo
	cd skyrik-react && pnpm install
	@echo "Repository fresh."
