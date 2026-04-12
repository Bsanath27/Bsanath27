---
title:  Skyrik (helicopter booking platform)
type: source
domain: real-time-systems
created: 2026-04-12
updated: 2026-04-12
tags: [real-time-systems, source]
---

## Source Info
- **Title**:  Skyrik (helicopter booking platform)
- **Type**: Article/Paper/Note
- **Domain**: real-time-systems
- **Accessed**: 2026-04-12

## Content

# Insights from: Skyrik (helicopter booking platform)
## Date: 2026-04-12

### Problems Solved & Gotchas
- **Problem**: Battery drain from constant WebSocket polling on mobile.
  - **Solution**: Implemented message batching and adjusted heartbeat intervals to reduce active radio time.
  - **Why it matters**: Improves device longevity and user satisfaction during long tracking sessions.
  - **Gotcha**: Mobile OS background execution limits can kill WebSockets silently; needs a robust reconnection strategy.
- **Problem**: Real-time geospatial tracking accuracy issues.
  - **Solution**: Implemented a `GeospatialCache` with smart invalidation and kalman-filtering principles for smoothing.
  - **Why it matters**: Prevents "jumping" icons on the map and provides a professional feel.
  - **Gotcha**: GPS noise at low speeds can cause "drifting" even when stationary.
- **Problem**: State management complexity in React Native.
  - **Solution**: Moved from simple Context to a more robust state management approach for complex booking flows.
  - **Why it matters**: Prevents unnecessary re-renders and logic spaghetti.
  - **Gotcha**: React Context is not a replacement for a dedicated state manager in highly interactive apps.

### Design Patterns & Architecture Decisions
- **Decision**: Chose REST + WebSocket hybrid over pure WebSocket.
  - **Trade-off**: Slightly more complex client-side logic (handling two communication styles).
  - **Alternative**: Pure WebSocket would simplify logic but increase server cost and complexity for simple metadata fetches.
  - **Lesson**: Use the right tool for the job—WebSockets for live updates, REST for transactional data.
- **Decision**: Built geospatial index in PostgreSQL (PostGIS) instead of an external service.
  - **Trade-off**: Managed database scaling vs. managed service API latency.
  - **Alternative**: Elasticsearch or a dedicated geospatial service.
  - **Lesson**: PostgreSQL's geospatial capabilities are powerful enough for most MVPs, reducing architectural footprint.
- **Decision**: Cache-first strategy for offline booking.
  - **Trade-off**: Handling complex synchronization/conflict resolution.
  - **Alternative**: Online-only booking (simpler but brittle in low-connectivity helipads).
  - **Lesson**: Mobile users expect offline resilience; `OfflineBookingQueue` is essential for UX.

### Performance Optimizations & Tricks
- **Optimization**: Geospatial query latency.
  - **Solution**: Implemented proper GIST indexing in PostGIS.
  - **Metric**: Reduced query time from 500ms to 50ms.
  - **Applies to**: Any project with radius-based searches or live asset tracking.
- **Optimization**: WebSocket overhead.
  - **Solution**: Implemented server-side message batching.
  - **Metric**: Reduced perceived latency by ~200ms and decreased packet count.
  - **Applies to**: High-frequency data streaming apps.
- **Optimization**: React Native UI responsiveness.
  - **Solution**: Heavy use of `useMemo` and `memo` for expensive map/carousel components.
  - **Metric**: Maintained stable 60fps even with 100+ moving map entities.
  - **Applies to**: Any React Native app with heavy visualization.

### Tech Learnings (React Native, FastAPI, etc)
- **Tech**: FastAPI
  - **What works**: Rapid API development and automatic documentation.
  - **What doesn't**: Standard background tasks are unreliable for long-running processes at scale.
  - **Pro tip**: Use Celery for any task that takes longer than a few seconds or needs persistence.
- **Tech**: React Native
  - **What works**: Cross-platform code sharing for UI logic.
  - **What doesn't**: High-frequency state updates can choke the bridge if not handled carefully.
  - **Pro tip**: Keep the bridge light; process heavy animations/transforms natively or via Reanimated.

### Reusable Code/Patterns Worth Documenting
- **Component/Function**: `GeospatialCache`
  - **Use case**: Caching location data with distance-based invalidation.
  - **Code location**: `skyrik-kotlin/app/src/main/kotlin/.../data/GeospatialCache.kt`
  - **Ports to**: Delivery apps, social mapping, or fleet management.
- **Component/Function**: `DynamicPricingEngine`
  - **Use case**: Calculating real-time prices based on supply/demand/distance.
  - **Code location**: `backend/services/pricing_engine.py`
  - **Ports to**: Any marketplace or booking system with fluctuating demand.
- **Component/Function**: `OfflineBookingQueue`
  - **Use case**: Queuing actions for background sync when connection is restored.
  - **Code location**: `skyrik-kotlin/.../data/sync/OfflineBookingQueue.kt`
  - **Ports to**: CRM tools, task trackers, or any "field" application.

## Archive
- Original: raw/archive/skyrik-insights-2026-04-12.md
