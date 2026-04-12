---
title:  How Passive Radar Works
type: source
domain: real-time-systems
created: 2026-04-12
updated: 2026-04-12
tags: [real-time-systems, source]
---

## Source Info
- **Title**:  How Passive Radar Works
- **Type**: Article/Paper/Note
- **Domain**: real-time-systems
- **Accessed**: 2026-04-12

## Content

# Summary: How Passive Radar Works

## Metadata
- **Source**: https://www.passiveradar.com/how-passive-radar-works/
- **Type**: article
- **Domain**: Real-time-systems
- **Date Read**: 2026-04-12
- **Confidence Level**: High
- **Read Time**: 15 min

## Summary

### Main Argument / Core Claim
Passive radar is an accessible and covert radar system that operates by listening to "broadcasts of opportunity" (e.g., FM radio, digital TV) rather than using a dedicated transmitter. By measuring the Doppler shift and time delay of signals reflected off objects compared to direct signals, it can determine an object's position and velocity.

### Supporting Evidence / Key Points
- **Bistatic Principles**: Unlike traditional monostatic radar, passive radar is bistatic, meaning the transmitter (illuminator) and receiver are in different locations.
- **Ellipse Delay Surfaces**: A measured time delay in a bistatic system corresponds to an ellipse where the transmitter and receiver are the foci; multiple intersections from different transmitters are used to triangulate a 3D position.
- **Accessibility**: Passive radar is significantly cheaper and legally simpler than active radar because it requires no transmitter hardware or broadcast licenses.

### Limitations / Caveats
- **Assumes**: Availability of third-party "illuminators of opportunity" in the area; high computational power to extract weak echoes (60-80 dB lower than direct signals).
- **Doesn't Cover**: Specific privacy constraints or detailed multi-target tracking algorithms.
- **Uncertainty**: Range resolution is limited by the bandwidth of available broadcasts (e.g., FM vs. DTV), often resulting in hundreds of meters of error compared to single-meter precision in active systems.

### Contradictions & Connections
- **[[Extends]]**: Basic radar principles like the Doppler effect and signal delay, but applies them to a bistatic, passive context.
- **[[Related to]]**: [[Real-Time Geospatial Data]], [[Real-Time Visualization Pipeline]], Software-Defined Radio (SDR), and signal processing.

### Key Quotes / Evidence
> "The result is a radar system with no transmitter, no expensive hardware, and no need for a broadcast license, unlike traditional, or 'monostatic' radar."

> "A well-designed passive radar system fuses all of these [FM stations, TV transmitters] to build a coherent picture of what's moving overhead."

### Personal Notes / Why This Matters
This article provides a foundational understanding of how passive radar can be built using affordable modern technology like SDRs. It’s particularly relevant for projects requiring low-cost, covert, or legally simple real-time tracking of aerial objects.

### Decision / Action
- **Keep in Wiki**: Yes
- **Tag for Synthesis**: Yes
- **Archive**: Yes

## Related
- [[../topics/]] — Related topics
- [[../entities/]] — People, companies, products
- [[../frameworks/]] — Patterns and methodologies

## Archive
- Original: raw/archive/2026-04-12-real-time-systems-how-passive-radar-works.md
