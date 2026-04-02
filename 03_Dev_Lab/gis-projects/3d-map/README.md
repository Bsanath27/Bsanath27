# 3D Terrain Map with Cesium.js

An interactive 3D terrain visualization application using Cesium.js for rendering satellite imagery and geographic boundaries.

## 🌍 Features

- **3D Globe Rendering**: Interactive globe with realistic terrain
- **Satellite Imagery**: High-resolution satellite imagery overlay
- **Terrain Elevation**: World terrain with accurate elevation data
- **GeoJSON Support**: Ready for pincode boundary overlays
- **Performance Optimized**: Request render mode for efficient rendering
- **Modern Build Tools**: Vite for fast development and optimized builds

## 📋 Prerequisites

- **Node.js** (v18 or higher recommended)
- **npm** or **yarn** package manager

## 🚀 Quick Start

### 1. Install Dependencies

```bash
npm install
```

### 2. Start Development Server

```bash
npm run dev
```

The application will automatically open in your browser at `http://localhost:3000`

### 3. Build for Production

```bash
npm run build
```

The optimized build will be generated in the `dist/` directory.

### 4. Preview Production Build

```bash
npm run preview
```

## 📁 Project Structure

```
3d-map/
├── src/                    # Source files
│   └── main.js            # Main application entry point
├── public/                # Static assets
├── data/                  # Data files (GeoJSON, etc.)
├── dist/                  # Build output (generated)
├── index.html             # HTML entry point
├── vite.config.js         # Vite configuration
├── package.json           # Project dependencies
└── README.md              # This file
```

## 🔑 Cesium Ion Access Token

The application currently uses a default Cesium Ion access token with limited functionality. For production use or access to premium features:

1. Create a free account at [Cesium Ion](https://cesium.com/ion/signup)
2. Get your access token from the dashboard
3. Replace the token in `src/main.js`:

```javascript
Ion.defaultAccessToken = 'YOUR_TOKEN_HERE';
```

## 🛠️ Technology Stack

- **Cesium.js** (v1.114.0): 3D geospatial visualization library
- **Vite** (v5.0.11): Next-generation frontend build tool
- **vite-plugin-cesium**: Vite plugin for Cesium integration

## 📦 Available Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server with hot reload |
| `npm run build` | Build optimized production bundle |
| `npm run preview` | Preview production build locally |

## 🎯 Next Steps

### Phase 1: Terrain and Imagery (Current)
- ✅ Basic 3D globe with terrain
- ✅ Satellite imagery overlay
- ✅ Example location marker
- ⏳ Custom terrain data sources

### Phase 2: Data Integration
- ⏳ Load pincode boundary GeoJSON files
- ⏳ Style and render boundaries on terrain
- ⏳ Add interactivity (hover, click)

### Phase 3: Advanced Features
- ⏳ Search and navigation
- ⏳ Data filtering and visualization
- ⏳ Performance optimization for large datasets

## 📖 Cesium Resources

- [Cesium Documentation](https://cesium.com/learn/)
- [Cesium Sandcastle](https://sandcastle.cesium.com/) - Interactive examples
- [Cesium Forum](https://community.cesium.com/)
- [API Reference](https://cesium.com/learn/cesiumjs/ref-doc/)

## 🐛 Troubleshooting

### Port 3000 already in use
Change the port in `vite.config.js`:
```javascript
server: {
  port: 3001
}
```

### Build errors
Clear the cache and reinstall:
```bash
rm -rf node_modules package-lock.json
npm install
```

### Cesium assets not loading
Make sure the `vite-plugin-cesium` is properly configured in `vite.config.js`

## 📄 License

Apache-2.0

## 🤝 Contributing

This is an internal project. For questions or issues, please contact the development team.

---

**Built with ❤️ using Cesium.js**
