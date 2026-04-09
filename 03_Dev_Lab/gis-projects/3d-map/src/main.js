/**
 * 3D Terrain Map - Main Entry Point
 * 
 * Initializes Cesium.js viewer with configured terrain elevation pipeline
 * for visualizing India's geographical features including:
 * - Himalayas (world's highest mountain range)
 * - Western Ghats (biodiversity hotspot)
 * - Eastern Ghats (discontinuous mountain range)
 * - Deccan Plateau (ancient volcanic plateau)
 * - Indo-Gangetic Plain (fertile alluvial plain)
 * 
 * Terrain Data: Cesium World Terrain (SRTM-based global elevation data)
 * Resolution: ~30m in India, ~90m globally
 * Data Provider: Cesium Ion (free tier)
 */

import { Viewer, Ion, Cartesian3 } from 'cesium';
import 'cesium/Build/Cesium/Widgets/widgets.css';
import { 
  initializeTerrainProvider, 
  applyTerrainSettings, 
  addTerrainMarkers,
  flyToPresetLocation,
  getExaggerationPresets
} from './utils/terrainUtils.js';
import terrainConfig from './config/terrainConfig.js';

// Cesium Ion access token for terrain and imagery data
// Get a free token at https://cesium.com/ion/signup
Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJlYWE1OWUxNy1mMWZiLTQzYjYtYTQ0OS1kMWFjYmFkNjc5YzciLCJpZCI6NTc3MzMsImlhdCI6MTYyNzg0NTE4Mn0.XcKpgANiY19MC4bdFUXMVEBToBmqS8kuYpUlxJHYZxk';

/**
 * Initialize Cesium viewer with terrain elevation pipeline
 */
async function initializeCesium() {
  try {
    console.log('🚀 Initializing 3D Terrain Map...');
    console.log('📊 Terrain Data Source:', terrainConfig.provider.type);
    
    // Step 1: Initialize terrain provider with configuration
    const terrainProvider = await initializeTerrainProvider();
    
    // Step 2: Create Cesium Viewer with terrain
    const viewer = new Viewer('cesiumContainer', {
      terrainProvider: terrainProvider,
      
      // UI Configuration
      timeline: false,
      animation: false,
      baseLayerPicker: true,
      geocoder: true,
      homeButton: true,
      navigationHelpButton: true,
      sceneModePicker: true,
      
      // Performance settings
      requestRenderMode: true,
      maximumRenderTimeChange: Infinity
    });

    // Step 3: Apply terrain rendering settings
    // Using default 1.0x exaggeration (realistic heights)
    // Change to 'dramatic', 'subtle', or 'extreme' for different effects
    applyTerrainSettings(viewer, terrainConfig.exaggeration.default);

    // Step 4: Set initial camera position (Western Ghats for dramatic terrain)
    const initialLocation = terrainConfig.presetLocations.westernGhats;
    viewer.camera.setView({
      destination: Cartesian3.fromDegrees(
        initialLocation.position.longitude,
        initialLocation.position.latitude,
        initialLocation.position.height
      ),
      orientation: initialLocation.orientation
    });

    // Step 5: Add terrain markers for notable locations
    addTerrainMarkers(viewer);
    
    console.log('✅ Cesium initialized successfully!');
    console.log('🌍 3D Terrain Map is ready');
    console.log('📍 Camera positioned at:', initialLocation.name);
    console.log('');
    console.log('🗺️  Available terrain locations:');
    Object.entries(terrainConfig.presetLocations).forEach(([key, loc]) => {
      console.log(`   - ${loc.name}: ${loc.description}`);
    });
    console.log('');
    console.log('💡 Try these commands in console:');
    console.log('   window.flyToHimalayas()    - Fly to Himalayas');
    console.log('   window.flyToWesternGhats() - Fly to Western Ghats');
    console.log('   window.setExaggeration(2.0) - Set terrain exaggeration to 2x');
    
    // Expose useful functions to window for interactive exploration
    window.viewer = viewer;
    window.flyToHimalayas = () => flyToPresetLocation(viewer, 'himalayas');
    window.flyToWesternGhats = () => flyToPresetLocation(viewer, 'westernGhats');
    window.flyToEasternGhats = () => flyToPresetLocation(viewer, 'easternGhats');
    window.flyToDeccanPlateau = () => flyToPresetLocation(viewer, 'deccanPlateau');
    window.flyToGangesPlain = () => flyToPresetLocation(viewer, 'gangesPlain');
    window.setExaggeration = (value) => {
      viewer.scene.verticalExaggeration = value;
      console.log(`✅ Terrain exaggeration set to ${value}x`);
    };
    window.getExaggerationPresets = () => {
      const presets = getExaggerationPresets();
      console.log('Available exaggeration presets:', presets);
      return presets;
    };

    return viewer;
  } catch (error) {
    console.error('❌ Error initializing Cesium:', error);
    console.error('Please check:');
    console.error('1. Internet connection (terrain data requires network)');
    console.error('2. Cesium Ion token validity');
    console.error('3. Browser console for detailed error messages');
    throw error;
  }
}

// Initialize when DOM is ready
initializeCesium();
