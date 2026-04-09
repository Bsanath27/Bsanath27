/**
 * Terrain Utilities
 * 
 * Helper functions for terrain data management and visualization
 */

import { createWorldTerrainAsync, Cartesian3, Color, Cartesian2, LabelStyle, VerticalOrigin } from 'cesium';
import terrainConfig from '../config/terrainConfig.js';

/**
 * Initialize terrain provider with configuration
 * @returns {Promise<TerrainProvider>} Configured terrain provider
 */
export async function initializeTerrainProvider() {
  try {
    const terrain = await createWorldTerrainAsync({
      requestVertexNormals: terrainConfig.provider.requestVertexNormals,
      requestWaterMask: terrainConfig.provider.requestWaterMask
    });
    
    console.log('✅ Terrain provider initialized:', terrainConfig.provider.type);
    console.log('   - Vertex normals:', terrainConfig.provider.requestVertexNormals);
    console.log('   - Water mask:', terrainConfig.provider.requestWaterMask);
    
    return terrain;
  } catch (error) {
    console.error('❌ Failed to initialize terrain provider:', error);
    throw error;
  }
}

/**
 * Apply terrain rendering settings to viewer
 * @param {Viewer} viewer - Cesium viewer instance
 * @param {number} exaggeration - Terrain height exaggeration multiplier
 */
export function applyTerrainSettings(viewer, exaggeration = terrainConfig.exaggeration.default) {
  // Enable depth testing for proper terrain occlusion
  viewer.scene.globe.depthTestAgainstTerrain = terrainConfig.rendering.depthTestAgainstTerrain;
  
  // Set terrain exaggeration
  viewer.scene.verticalExaggeration = exaggeration;
  
  // Enable lighting
  viewer.scene.globe.enableLighting = terrainConfig.rendering.enableLighting;
  
  // Configure fog
  if (terrainConfig.rendering.fog.enabled) {
    viewer.scene.fog.enabled = true;
    viewer.scene.fog.density = terrainConfig.rendering.fog.density;
  }
  
  console.log('✅ Terrain rendering settings applied:');
  console.log('   - Depth testing:', terrainConfig.rendering.depthTestAgainstTerrain);
  console.log('   - Exaggeration:', exaggeration + 'x');
  console.log('   - Lighting:', terrainConfig.rendering.enableLighting);
  console.log('   - Fog:', terrainConfig.rendering.fog.enabled);
}

/**
 * Fly to a preset terrain location
 * @param {Viewer} viewer - Cesium viewer instance
 * @param {string} locationKey - Key from terrainConfig.presetLocations
 */
export function flyToPresetLocation(viewer, locationKey) {
  const location = terrainConfig.presetLocations[locationKey];
  
  if (!location) {
    console.error(`❌ Unknown location: ${locationKey}`);
    return;
  }
  
  console.log(`🌍 Flying to: ${location.name}`);
  console.log(`   ${location.description}`);
  
  viewer.camera.flyTo({
    destination: Cartesian3.fromDegrees(
      location.position.longitude,
      location.position.latitude,
      location.position.height
    ),
    orientation: location.orientation,
    duration: 3
  });
}

/**
 * Add terrain markers for all preset locations
 * @param {Viewer} viewer - Cesium viewer instance
 */
export function addTerrainMarkers(viewer) {
  const locations = terrainConfig.presetLocations;
  let markerCount = 0;
  
  for (const [key, location] of Object.entries(locations)) {
    if (location.marker) {
      viewer.entities.add({
        name: location.marker.label,
        position: Cartesian3.fromDegrees(
          location.marker.longitude,
          location.marker.latitude,
          location.marker.height
        ),
        point: {
          pixelSize: 10,
          color: Color.CYAN,
          outlineColor: Color.WHITE,
          outlineWidth: 2
        },
        label: {
          text: location.marker.label,
          font: '14pt sans-serif',
          style: LabelStyle.FILL_AND_OUTLINE,
          outlineWidth: 2,
          verticalOrigin: VerticalOrigin.BOTTOM,
          pixelOffset: new Cartesian2(0, -9),
          fillColor: Color.WHITE,
          showBackground: true,
          backgroundColor: new Color(0, 0, 0, 0.7),
          backgroundPadding: new Cartesian2(8, 4)
        }
      });
      markerCount++;
    }
  }
  
  console.log(`✅ Added ${markerCount} terrain markers`);
}

/**
 * Get available preset locations
 * @returns {Object} Map of location keys to location data
 */
export function getPresetLocations() {
  return terrainConfig.presetLocations;
}

/**
 * Get available exaggeration presets
 * @returns {Object} Map of exaggeration preset names to multipliers
 */
export function getExaggerationPresets() {
  return terrainConfig.exaggeration;
}

/**
 * Sample terrain height at a specific location
 * @param {Viewer} viewer - Cesium viewer instance
 * @param {number} longitude - Longitude in degrees
 * @param {number} latitude - Latitude in degrees
 * @returns {Promise<number>} Height in meters above sea level
 */
export async function sampleTerrainHeight(viewer, longitude, latitude) {
  const positions = [Cartesian3.fromDegrees(longitude, latitude)];
  const updatedPositions = await viewer.scene.sampleHeightMostDetailed(positions);
  
  if (updatedPositions && updatedPositions.length > 0) {
    const cartographic = viewer.scene.globe.ellipsoid.cartesianToCartographic(updatedPositions[0]);
    return cartographic.height;
  }
  
  return 0;
}

export default {
  initializeTerrainProvider,
  applyTerrainSettings,
  flyToPresetLocation,
  addTerrainMarkers,
  getPresetLocations,
  getExaggerationPresets,
  sampleTerrainHeight
};
