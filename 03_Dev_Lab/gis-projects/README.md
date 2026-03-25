# GIS Projects

This workspace contains small GIS utilities, source data and generated maps used for helipad locations and Indian pincode boundaries.

## Structure

- `helipads/` — example helipad datasets and generated HTML maps (CSV, JSON, KML, and HTML outputs).
  - Example files: [helipads/skyrik_helipads_india_20260321_1656.csv](helipads/skyrik_helipads_india_20260321_1656.csv), [helipads/skyrik_helipad_intel_map.html](helipads/skyrik_helipad_intel_map.html)
- `tn-pincodes/` — scripts, raw/state GeoJSONs and generated maps for Tamil Nadu pincodes.
  - Key files: [tn-pincodes/requirements.txt](tn-pincodes/requirements.txt), [tn-pincodes/Makefile](tn-pincodes/Makefile), [tn-pincodes/tamilnadu_pincodes.geojson](tn-pincodes/tamilnadu_pincodes.geojson)
  - Tools: [tn-pincodes/tools/convert_geojson.py](tn-pincodes/tools/convert_geojson.py), [tn-pincodes/tools/extract_tn.py](tn-pincodes/tools/extract_tn.py)
- `tn-pincodes/data_raw/India-Pincode-Boundary-Data/` — original per-state GeoJSONs and converted outputs.

## Data formats

- GeoJSON (.geojson)
- CSV (.csv)
- KML (.kml)
- Static map HTML (.html)

## Quick start

1. Create a Python virtual environment and install dependencies for the pincode tools:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r tn-pincodes/requirements.txt
```

2. Convert or process GeoJSONs using the scripts in `tn-pincodes/tools/`.

Example: extract Tamil Nadu pincodes (uses files under `tn-pincodes/data_raw`):

```bash
python3 tn-pincodes/tools/extract_tn.py
```

3. Open any generated HTML maps in a browser, e.g.:

```bash
open tn-pincodes/tamilnadu_pincode_map.html
open helipads/skyrik_helipad_intel_map.html
```

## Notes

- The repository contains both raw data and generated outputs — if you intend to re-run conversions, prefer working with files under `tn-pincodes/data_raw` and the `tools/` scripts.
- If you want, I can also add a CONTRIBUTING or developer README with exact command examples for each tool.

---
Generated automatically to summarise the current workspace contents.
