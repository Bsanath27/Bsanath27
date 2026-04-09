# LLM Wiki System Rules & Schema

## Philosophy
This is a persistent, curated knowledge base maintained by Claude. Unlike RAG systems that re-derive knowledge on every query, this wiki is incrementally built and kept current. The human curates sources and asks questions. Claude synthesizes, integrates, and maintains all files.

## Core Principles
1. **Write Once, Synthesize Continuously**: Information is integrated once, then refined with each new source
2. **Cross-Reference Everything**: Entities link to topics, topics link to sources, sources link to contradictions
3. **Flag Contradictions**: When new data conflicts with existing claims, flag it prominently with [[Contradiction]]
4. **Maintain, Don't Repeat**: The LLM updates existing files rather than creating redundant pages
5. **Human Curation, Machine Maintenance**: You source and direct; I synthesize, file, and maintain consistency

## Folder Structure
```
obsidian-vault/
├── CLAUDE.md                 # This file - system rules (agent instructions)
├── log.md                    # Ingest journal (audit trail, logs)
├── FROM-NOW-ON.md           # How Claude works with this system
├── commands.md              # All custom commands reference
├── docs/                    # Documentation & guides
│   ├── guide.md             # Practical use cases & workflows
│   ├── index.md             # Hub - links to all major sections
│   ├── CONTRADICTIONS.md    # Contradiction tracker
│   └── [other guides...]    # See docs/ folder
├── _wiki/
│   ├── entities/            # People, organizations, products
│   ├── topics/              # Concepts, domains, themes
│   ├── frameworks/          # Methods, models, systems
│   └── sources/             # Raw source metadata & summaries
└── raw/                     # Clipped articles, PDFs (not indexed)
    ├── inbox/               # Default entry point (new sources)
    ├── processed/           # Archived sources
    └── assets/              # Downloaded images
```

## File Schema

### Entity Pages (entities/)
```yaml
---
title: [Entity Name]
type: person | organization | product | place
tags: [relevant-tags]
created: YYYY-MM-DD
updated: YYYY-MM-DD
sources: N
related: [links to related entities]
---

## Overview
[2-3 sentence description]

## Key Facts
- Fact 1
- Fact 2
- etc.

## Work / Contributions
[Major work or involvement in topics]

## Related
- [[Topic 1]]
- [[Entity 2]]

## Contradictions
[If conflicts exist with other records]
```

### Topic Pages (topics/)
```yaml
---
title: [Topic Name]
type: concept | domain | theme
tags: [relevant-tags]
created: YYYY-MM-DD
updated: YYYY-MM-DD
source_count: N
entities: [relevant entities]
---

## Definition
[Clear, concise explanation]

## Key Ideas
- Idea 1
- Idea 2

## Applications
[Where and how this is used]

## Related Topics
- [[Related Topic 1]]
- [[Related Topic 2]]

## Key Entities Involved
- [[Entity 1]]
- [[Entity 2]]

## Sources
- [[Source Title 1]] (date, relevance)
- [[Source Title 2]] (date, relevance)

## Evolution
[How understanding of this topic has changed with new sources]

## Contradictions
[If conflicting views exist]
```

### Framework Pages (frameworks/)
```yaml
---
title: [Framework Name]
type: method | model | system | process
created: YYYY-MM-DD
updated: YYYY-MM-DD
applications: [where used]
sources: N
---

## Overview
[What is this and why does it exist?]

## Steps / Components
1. Step 1
2. Step 2

## When to Use
[Conditions and contexts]

## Strengths
- +
- +

## Weaknesses / Limitations
- -
- -

## Examples
- [[Entity/Topic Example]]
- [[Entity/Topic Example]]

## Related Frameworks
- [[Framework 1]]

## See Also
- [[Topic]]
- [[Entity]]
```

### Source Pages (sources/)
```yaml
---
title: [Source Title]
type: article | book | video | report | conversation
source_url: [URL or path]
date: YYYY-MM-DD
author: [Name]
domain: [general field: "business", "tech", "science", etc.]
relevance_score: 1-5 (1=low, 5=high)
key_entities: [entities mentioned]
key_topics: [topics covered]
processed: YYYY-MM-DD
---

## Source Info
- **URL**: [URL]
- **Author**: [Name]
- **Date**: YYYY-MM-DD
- **Type**: article/book/etc

## Summary
[2-3 paragraphs synthesizing main points]

## Key Takeaways
- Takeaway 1
- Takeaway 2

## Relevant Entities Mentioned
- [[Entity 1]]
- [[Entity 2]]

## Relevant Topics Covered
- [[Topic 1]]
- [[Topic 2]]

## Contradictions or Notable Disagreements
[If this source contradicts existing wiki knowledge]

## Integration Notes
[How this was integrated: what was added, what was updated, what was revised]
```

### Log Page (log.md)
```
# Ingest Journal

## Format
| Date | Title | Type | Relevance | Status | Topics/Entities Added |
|------|-------|------|-----------|--------|------------------------|
| YYYY-MM-DD | [Title] | article/book/etc | 1-5 | integrated | [[Topic]], [[Entity]] |

## Entry Details (for complex ingests)
### [Date] - [Title]
- **Type**: article/book/report
- **Source**: [URL]
- **Relevance**: Why this was ingested
- **New Pages Created**: [[Entity]], [[Topic]]
- **Pages Updated**: [[Topic]], [[Framework]]
- **Contradictions Found**: [If any]
- **Status**: integrated | pending | archived

---
```

### Index Page (docs/index.md)
```
# Wiki Index

## Quick Navigation
- log.md — View all ingested sources
- docs/guide.md — How to use this system

## Topics
[Auto-generated or manually maintained list]
- [[Topic 1]] (N sources)
- [[Topic 2]] (N sources)

## Entities
- [[Entity 1]]
- [[Entity 2]]

## Frameworks & Methods
- [[Framework 1]]
- [[Framework 2]]

## By Domain
### Business
- [[Topic]]

### Technology
- [[Topic]]

### [Other Domains]

## Recent Ingests
[Last 5-10 sources from log.md]

## Unresolved Contradictions
[All flagged contradictions across wiki]

## Orphaned Pages
[Pages with few cross-references - candidates for deletion]
```

### Guide Page (docs/guide.md)
```
# How to Use This Wiki

## Common Workflows

### 1. Ingest a New Source
1. Add raw content to `/raw/` folder or share with Claude
2. Claude reads, extracts key info, creates/updates pages
3. Claude updates log.md with source metadata
4. Cross-references are automatically created

### 2. Ask a Synthesis Question
Example: "What's the relationship between X and Y across all sources?"
- Claude synthesizes across existing pages
- Updates topic pages with new connections
- Flags any contradictions found
- Returns answer + updates summary

### 3. Explore a Topic
1. Open topic page
2. Follow [[linked entities]]
3. Review "Sources" section
4. Check "Evolution" to see how understanding changed

### 4. Track Disagreements
Search for [[Contradiction]] tags to find conflicting views.

### 5. Maintain the Wiki
- Regular: Claude updates cross-references as new sources arrive
- Occasional: Review orphaned pages, merge related topics
- Manual override: You can edit any page, Claude respects changes

## Obsidian Tips
- Use graph view to see knowledge density
- Use backlinks pane to explore connections
- Use Dataview plugin to query by tags/metadata
- Use search to find contradictions or entities

## When to Ingest
- New sources with high relevance (score 4-5)
- Contradictions to existing understanding
- Novel perspectives on known topics
- Don't ingest: tangential, low-relevance, or duplicative content

## Output Format
After each ingest, Claude provides:
1. Summary of what was added/updated
2. New pages created (if any)
3. Pages modified (if any)
4. Contradictions found (if any)
5. Link to updated log.md
```

## Ingest Process (Claude's Responsibility)

When you provide a source, I will:

1. **Read & Extract**: Understand main points, entities, claims
2. **Match & Integrate**: 
   - Check if entity pages exist → update them
   - Check if topic pages exist → add source, update synthesis
   - Create new pages only if truly novel
3. **Cross-Reference**: Link all relevant [[entities]] and [[topics]]
4. **Flag Issues**:
   - New contradictions → note in [[Contradiction]]
   - Conflicting data → explicitly mark and explain
5. **Update Log**: Add source to log.md with metadata
6. **Report**: Show you exactly what changed

## Special Tags
- `[[Contradiction]]` — Flag conflicting claims
- `#processed` — Source has been integrated
- `#pending` — Source waiting for integration
- `#archived` — Source set aside (reason noted)

## Version Control
This wiki is a git repo. Each major integration is committed:
- Commit message: "Ingest: [Source Title] - [Topics/Entities Added]"

---

**Last Updated**: CLAUDE.md created as system schema
**Maintained By**: Claude (you direct, I synthesize)
