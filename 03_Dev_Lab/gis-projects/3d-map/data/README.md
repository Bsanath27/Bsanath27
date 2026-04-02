# Data Directory

This directory is for storing GeoJSON and other data files:

## Structure

```
data/
├── pincodes/           # Pincode boundary GeoJSON files
├── terrain/            # Custom terrain data (if any)
└── imagery/            # Custom imagery tiles (if any)
```

## GeoJSON Format

Pincode boundary files should follow this structure:

```json
{
  "type": "FeatureCollection",
  "features": [
    {
      "type": "Feature",
      "properties": {
        "pincode": "560001",
        "area_name": "Example Area",
        "state": "Karnataka"
      },
      "geometry": {
        "type": "Polygon",
        "coordinates": [[[lng, lat], [lng, lat], ...]]
      }
    }
  ]
}
```

## Loading Data in Application

```javascript
import { GeoJsonDataSource } from 'cesium';

const dataSource = await GeoJsonDataSource.load('./data/pincodes/bangalore.geojson', {
  stroke: Color.RED,
  fill: Color.RED.withAlpha(0.3),
  strokeWidth: 2
});

viewer.dataSources.add(dataSource);
```
