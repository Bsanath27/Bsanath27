# Skyrik Aviation 🚁✨
**The Pinnacle of Elite Aerial Mobility and Helipad Logistics**

![Skyrik Framework](https://img.shields.io/badge/Platform-React_Native-000000?style=for-the-badge&logo=react)
![Expo](https://img.shields.io/badge/Build-Expo_SDK_54-D4AF37?style=for-the-badge&logo=expo)
![TypeScript](https://img.shields.io/badge/Language-TypeScript-3178C6?style=for-the-badge&logo=typescript)
![Supabase](https://img.shields.io/badge/Database-Supabase-3ECF8E?style=for-the-badge&logo=supabase)
![Python](https://img.shields.io/badge/Data_Pipeline-Python_3-3776AB?style=for-the-badge&logo=python)

---

## 🦅 Overview

**Skyrik** is a next-generation luxury aviation and logistics command center. Designed with an uncompromising "Stealth Obsidian & Champagne Gold" absolute premium aesthetic, Skyrik enables seamless booking of civilian, corporate, and private helicopter charters across India. 

The platform features an ultra-optimized MapBox/Google Maps integration with real-time rendering of **274+ licensed Indian helipads**, dynamic route calculation, and real-time live flight radar tracking.

---

## 🎨 Design Philosophy : "Champagne Boutique"

Skyrik ditches the traditional sterile blue-and-white mobility app aesthetic. Instead, it fully embraces a **Midnight Bronze (#1A1612)** dark mode intertwined with **Champagne Gold (#D4AF37)** accents and sophisticated **Glassmorphism (Matte Paper)** overlays. Every component is designed to feel like holding an exclusive Platinum card.

---

## ⚙️ Tech Stack Architecture

Our monorepo is efficiently structured into two main disciplines: Frontend React Native architecture and an overarching Data Engineering pipeline.

- **Frontend Core**: React Native + Expo Router v3
- **Mapping Engine**: `react-native-maps` plotting highly-optimized local JSON data synced with Supabase.
- **State Management**: Zustand / Local React Contexts
- **Style Engine**: Native `StyleSheet` strictly tied to our `/constants/theme.ts` token system.
- **Data Engineering**: Python scripts leveraging `pandas` and `kagglehub` to download, sanitize, and format global helipad open data into highly optimized JSON assets.

---

## 🧠 Project Layout

```text
📦 Skyrik/
├── 📂 skyrik-react/               # Core Mobile Application
│   ├── 📂 app/                    # Expo Router View Controllers
│   ├── 📂 components/             # Reusable UI & Feature components
│   ├── 📂 constants/              # Global Theme Tokens & Styles
│   ├── 📂 lib/                    # Supabase Configurations
│   └── 📂 data/                   # JSON data and Python formatting scripts
├── 📄 Makefile                   # Simple Command Orchestration
├── 📄 package.json               # Root monorepo entrypoint
└── 📄 requirements.txt           # Python Data Engineering dependencies
```

---

## 🚀 Quick Start (via Makefile)

We have engineered a highly efficient Makefile to get your development environment running instantly.

### 1. Prerequisites
Ensure you have `Node.js` (v20+), `pnpm`, and `Python 3` installed on your machine.

### 2. Setup Dependencies
Automatically install both NPM and Python data engineering dependencies:
```bash
make install
```

### 3. Launch Development Server
Boot up the Expo server. 
- **Local Network (Wi-Fi)**:
```bash
make lan
```
- **Remote Access (Tunnel)**:
```bash
make start
```

### 4. Direct OS Deployment
Run immediately on your target simulators:
```bash
make ios
# or
make android
```

---

## 🐍 Data Engineering Pipeline

To update the 274 map markers, our pipeline fetches fresh data from Kaggle, cleans null entries (like missing cities), and aligns longitude/latitude keys. 

Ensure your `requirements.txt` is installed, then run:

```bash
make data-setup
```

*(This automatically triggers the alignment and cleaning phases inside `skyrik-react/data/`.)*

---

## 🔒 Environment Variables

The Expo project relies on the following configurations in `.env`:
```env
EXPO_PUBLIC_SUPABASE_URL=your_remote_supabase_project_url
EXPO_PUBLIC_SUPABASE_ANON_KEY=your_remote_supabase_anon_key
```
*Note: If no network or DB is detected, Skyrik operates via `mockSupabase.ts` fallback—loading the local JSON helipad data directly into memory.*

---

**Skyrik Aviations.** *Elevate your mobility.* 🥂
