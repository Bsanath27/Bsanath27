# Development Guide

## Getting Started

1. **Install dependencies**: `npm install`
2. **Start dev server**: `npm run dev`
3. **Build for production**: `npm run build`

## Development Workflow

### Running Locally

```bash
npm run dev
```

Visit `http://localhost:3000` in your browser. The application will hot-reload on file changes.

### Code Structure

**src/main.js**: Main application entry point
- Initializes Cesium viewer
- Configures terrain and imagery
- Sets camera position
- Adds example entities

**index.html**: HTML template
- Cesium container div
- Imports main.js module
- Basic styling

**vite.config.js**: Build configuration
- Cesium plugin setup
- Dev server settings
- Build output configuration

## Adding Features

### 1. Load GeoJSON Data

```javascript
import { GeoJsonDataSource, Color } from 'cesium';

const dataSource = await GeoJsonDataSource.load('/data/boundaries.geojson', {
  stroke: Color.YELLOW,
  fill: Color.YELLOW.withAlpha(0.3),
  strokeWidth: 2
});

viewer.dataSources.add(dataSource);
```

### 2. Add Custom Markers

```javascript
viewer.entities.add({
  position: Cartesian3.fromDegrees(longitude, latitude, height),
  point: {
    pixelSize: 10,
    color: Color.RED
  },
  label: {
    text: 'My Location',
    font: '14pt sans-serif'
  }
});
```

### 3. Handle Click Events

```javascript
const handler = new ScreenSpaceEventHandler(viewer.scene.canvas);

handler.setInputAction((click) => {
  const pickedObject = viewer.scene.pick(click.position);
  if (pickedObject?.id) {
    console.log('Clicked:', pickedObject.id.name);
  }
}, ScreenSpaceEventType.LEFT_CLICK);
```

## Performance Tips

1. **Enable Request Render Mode**: Already configured in main.js
2. **Use Terrain Depth Testing**: Enabled for better occlusion
3. **Optimize GeoJSON**: Simplify complex geometries if needed
4. **Lazy Load Data**: Load data only when needed

## Debugging

### Enable Cesium Inspector

```javascript
viewer.extend(viewerCesiumInspectorMixin);
```

### Console Logging

Check browser console for Cesium logs:
- ✅ Success messages
- ❌ Error messages
- ℹ️ Info about loaded assets

## Common Tasks

### Change Initial View

Edit camera settings in `src/main.js`:

```javascript
viewer.camera.setView({
  destination: Cartesian3.fromDegrees(lng, lat, height),
  orientation: {
    heading: 0.0,
    pitch: -0.5,
    roll: 0.0
  }
});
```

### Switch Imagery Provider

```javascript
viewer.imageryLayers.addImageryProvider(
  new OpenStreetMapImageryProvider({
    url: 'https://a.tile.openstreetmap.org/'
  })
);
```

### Add Custom Terrain

```javascript
viewer.terrainProvider = await CesiumTerrainProvider.fromUrl(
  'https://your-terrain-server.com/terrain'
);
```

## Deployment

### Build Optimization

The production build is automatically optimized with:
- Minification
- Tree-shaking
- Code splitting
- Source maps

### Deploy to Static Hosting

After running `npm run build`, deploy the `dist/` folder to:
- Netlify
- Vercel
- GitHub Pages
- AWS S3
- Any static file hosting

## Resources

- [Cesium Tutorials](https://cesium.com/learn/cesiumjs-learn/)
- [Sandcastle Examples](https://sandcastle.cesium.com/)
- [Vite Documentation](https://vitejs.dev/)
