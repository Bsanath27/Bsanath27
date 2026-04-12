---
title:  Scene Recreator (AI video transformation)
type: source
domain: ai-ml
created: 2026-04-12
updated: 2026-04-12
tags: [ai-ml, source]
---

## Source Info
- **Title**:  Scene Recreator (AI video transformation)
- **Type**: Article/Paper/Note
- **Domain**: ai-ml
- **Accessed**: 2026-04-12

## Content

# Insights from: Scene Recreator (AI video transformation)
## Date: 2026-04-12

### Problems Solved & Gotchas
- **Problem**: Kling API rate limiting.
  - **Solution**: Built a request queuing system with exponential backoff.
  - **Why it matters**: Prevents job failures during peak usage or batch processing.
  - **Gotcha**: Kling API is slow (1-2 min per video); the UI must handle long-lived "Pending" states gracefully.
- **Problem**: Video upload timeouts for large files.
  - **Solution**: Implemented chunked uploads to MinIO with resumable support.
  - **Why it matters**: Essential for handling 4K or high-bitrate source material without network failures.
  - **Gotcha**: Standard `multipart/form-data` uploads often fail on slow connections for files >50MB.
- **Problem**: Shot detection normalization bug (histogram-based).
  - **Solution**: Fixed a math error where Chi-Square distance was divided by 256 instead of 2, correctly normalizing the range.
  - **Why it matters**: Restored the ability to automatically detect scene cuts.
  - **Gotcha**: Always verify that your normalization matches the expected 0–1 range of your thresholds.

### Design Patterns & Architecture Decisions
- **Decision**: Separate services (Next.js + FastAPI) instead of a monolith.
  - **Trade-off**: More complex deployment (Docker Compose).
  - **Alternative**: Next.js API routes for everything.
  - **Lesson**: Heavy processing (FFmpeg, ML models) is better handled in Python (FastAPI) while keeping the UI snappy in Next.js.
- **Decision**: Local-first MVP with MinIO.
  - **Trade-off**: Requires local setup (Docker) for dev.
  - **Alternative**: Direct S3/Cloudinary.
  - **Lesson**: MinIO allows for fast local development without egress costs or internet dependency.
- **Decision**: Scene-based processing instead of frame-based.
  - **Trade-off**: May miss transitions if detection fails.
  - **Alternative**: Process every Nth frame globally.
  - **Lesson**: Scenes provide semantic boundaries that are crucial for maintaining character consistency.

### Performance Optimizations & Tricks
- **Optimization**: Inference time and data handling.
  - **Solution**: Keyframe sampling (3 keyframes per shot) instead of processing every frame.
  - **Metric**: Reduced total processing time by ~80% without losing visual context.
  - **Applies to**: AI video analysis, summarization, or style transfer.
- **Optimization**: UI responsiveness with many thumbnails.
  - **Solution**: Implemented virtual scrolling for the shots grid and an LRU cache for base64 thumbnails.
  - **Metric**: Maintained 60fps scroll even with hundreds of extracted scenes.
  - **Applies to**: Video editors, gallery apps, or asset managers.

### Tech Learnings (React Native, FastAPI, etc)
- **Tech**: Video Processing (FFmpeg/PySceneDetect)
  - **What works**: Using `PySceneDetect` for robust cut detection; FFmpeg for fast chunking.
  - **What doesn't**: Running FFmpeg synchronously in a web request (will timeout).
  - **Pro tip**: Offload all FFmpeg work to background workers (Celery/FastAPI BackgroundTasks).
- **Tech**: Face Detection (face-api.js)
  - **What works**: `TinyFaceDetector` for fast browser-side clustering.
  - **What doesn't**: Default settings often miss small or profile faces.
  - **Pro tip**: Increase `inputSize` (to 512+) and lower `scoreThreshold` (to 0.2) for better recall in video frames.

### Reusable Code/Patterns Worth Documenting
- **Component/Function**: `FrameExtractor` (Canvas-based)
  - **Use case**: Extracting thumbnails and keyframes from video in the browser without server roundtrips.
  - **Code location**: `frontend/src/services/frameExtractor.ts`
  - **Ports to**: Any web-based video editor or preview system.
- **Component/Function**: `ShotDetector` (Histogram/Pixel Hybrid)
  - **Use case**: Detecting scene cuts in raw video buffers.
  - **Code location**: `frontend/src/services/shotDetector.ts`
  - **Ports to**: Automated video summarizers or highlight generators.
- **Component/Function**: `CharacterClusteringEngine` (Cosine Similarity)
  - **Use case**: Grouping detected faces across frames into unique identity groups.
  - **Code location**: `frontend/src/services/identityClustering.ts`
  - **Ports to**: Security software, photo organizers, or character-driven AI pipelines.

## Related
- [[../topics/]] — Related topics
- [[../frameworks/]] — Related frameworks

## Archive
- Original: raw/archive/scene-recreator-insights-2026-04-12.md
