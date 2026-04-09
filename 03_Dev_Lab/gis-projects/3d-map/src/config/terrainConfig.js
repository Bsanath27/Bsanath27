/**
 * Terrain Configuration Module
 * 
 * Configures elevation data pipeline for 3D terrain rendering over India.
 * 
 * Data Source: Cesium World Terrain
 * - Global coverage with high-resolution elevation data
 * - Based on SRTM, ASTER GDEM, and other sources
 * - Delivered via Cesium Ion (free tier available)
 * - Resolution: ~30m in most areas, ~90m globally
 * - Format: Quantized-mesh for efficient streaming
 */

export const terrainConfig = {
  /**
   * Terrain provider settings
   */
  provider: {
    // Using Cesium World Terrain (free via Cesium Ion)
    type: 'cesium-world-terrain',
    
    // Enable request vertex normals for better lighting
    requestVertexNormals: true,
    
    // Enable request water mask for water bodies
    requestWaterMask: true
  },

  /**
   * Terrain exaggeration settings
   * Multiplier applied to terrain heights for visual effect
   */
  exaggeration: {
    default: 1.0,      // 1.0 = realistic heights
    dramatic: 2.0,     // 2x elevation for mountainous areas
    subtle: 1.5,       // 1.5x for gentle emphasis
    extreme: 3.0       // 3x for extreme visualization
  },

  /**
   * Rendering settings
   */
  rendering: {
    // Enable depth testing for proper occlusion
    depthTestAgainstTerrain: true,
    
    // Enable lighting for terrain (shows shadows)
    enableLighting: true,
    
    // Fog settings for atmosphere
    fog: {
      enabled: true,
      density: 0.0002
    }
  },

  /**
   * Notable terrain locations in India
   * Great for testing and demonstration
   */
  presetLocations: {
    himalayas: {
      name: 'Himalayas',
      description: 'Highest mountain range in the world',
      position: {
        longitude: 77.5730,
        latitude: 28.7041,
        height: 50000
      },
      orientation: {
        heading: 0.0,
        pitch: -0.4,
        roll: 0.0
      },
      marker: {
        longitude: 77.5730,
        latitude: 28.7041,
        height: 8849,
        label: 'Mt. Everest Region'
      }
    },
    westernGhats: {
      name: 'Western Ghats',
      description: 'Mountain range along western coast of India',
      position: {
        longitude: 76.5222,
        latitude: 12.9141,
        height: 100000
      },
      orientation: {
        heading: 0.0,
        pitch: -0.5,
        roll: 0.0
      },
      marker: {
        longitude: 76.5222,
        latitude: 12.9141,
        height: 2695,
        label: 'Anamudi Peak'
      }
    },
    easternGhats: {
      name: 'Eastern Ghats',
      description: 'Discontinuous mountain range along eastern coast',
      position: {
        longitude: 78.8718,
        latitude: 18.2551,
        height: 150000
      },
      orientation: {
        heading: 0.0,
        pitch: -0.5,
        roll: 0.0
      },
      marker: {
        longitude: 78.8718,
        latitude: 18.2551,
        height: 1680,
        label: 'Arma Konda'
      }
    },
    deccanPlateau: {
      name: 'Deccan Plateau',
      description: 'Large plateau in southern India',
      position: {
        longitude: 77.5946,
        latitude: 12.9716,
        height: 200000
      },
      orientation: {
        heading: 0.0,
        pitch: -0.6,
        roll: 0.0
      },
      marker: {
        longitude: 77.5946,
        latitude: 12.9716,
        height: 600,
        label: 'Bangalore (Deccan Plateau)'
      }
    },
    gangesPlain: {
      name: 'Indo-Gangetic Plain',
      description: 'Vast alluvial plain of northern India',
      position: {
        longitude: 80.9462,
        latitude: 26.8467,
        height: 150000
      },
      orientation: {
        heading: 0.0,
        pitch: -0.5,
        roll: 0.0
      },
      marker: {
        longitude: 80.9462,
        latitude: 26.8467,
        height: 100,
        label: 'Lucknow (Gangetic Plain)'
      }
    }
  }
};

export default terrainConfig;
