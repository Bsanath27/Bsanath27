import { Viewer, Ion, createWorldTerrainAsync, Color, Cartesian3, Cartesian2, LabelStyle, VerticalOrigin } from 'cesium';
import 'cesium/Build/Cesium/Widgets/widgets.css';

// Set your Cesium Ion access token here
// Get a free token at https://cesium.com/ion/signup
// For now, using the default token (limited functionality)
Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJlYWE1OWUxNy1mMWZiLTQzYjYtYTQ0OS1kMWFjYmFkNjc5YzciLCJpZCI6NTc3MzMsImlhdCI6MTYyNzg0NTE4Mn0.XcKpgANiY19MC4bdFUXMVEBToBmqS8kuYpUlxJHYZxk';

async function initializeCesium() {
  try {
    // Create the Cesium Viewer
    const viewer = new Viewer('cesiumContainer', {
      // Use Cesium World Terrain for 3D terrain
      terrainProvider: await createWorldTerrainAsync(),
      
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

    // Enable depth testing for better terrain rendering
    viewer.scene.globe.depthTestAgainstTerrain = true;

    // Set initial camera position (centered on India)
    viewer.camera.setView({
      destination: Cartesian3.fromDegrees(78.9629, 20.5937, 3000000), // India center
      orientation: {
        heading: 0.0,
        pitch: -0.5,
        roll: 0.0
      }
    });

    // Optional: Add a custom imagery provider (satellite imagery)
    // This uses Bing Maps imagery by default
    
    console.log('✅ Cesium initialized successfully!');
    console.log('🌍 3D Terrain Map is ready');
    console.log('📍 Camera centered on India');
    
    // Example: Add a marker/entity to demonstrate capabilities
    viewer.entities.add({
      name: 'Example Location',
      position: Cartesian3.fromDegrees(77.5946, 12.9716, 1000), // Bangalore
      point: {
        pixelSize: 10,
        color: Color.RED,
        outlineColor: Color.WHITE,
        outlineWidth: 2
      },
      label: {
        text: 'Bangalore',
        font: '14pt sans-serif',
        style: LabelStyle.FILL_AND_OUTLINE,
        outlineWidth: 2,
        verticalOrigin: VerticalOrigin.BOTTOM,
        pixelOffset: new Cartesian2(0, -9)
      }
    });

    return viewer;
  } catch (error) {
    console.error('❌ Error initializing Cesium:', error);
    throw error;
  }
}

// Initialize when DOM is ready
initializeCesium();
