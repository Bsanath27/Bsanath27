# LLM Wiki System Rules & Schema

## Philosophy
This is a persistent, curated knowledge base maintained by Claude. Unlike RAG systems that re-derive knowledge on every query, this wiki is incrementally built and kept current. The human curates sources and asks questions. Claude synthesizes, integrates, and maintains all files.

## Core Principles
1. **Write Once, Synthesize Continuously**: Information is integrated once, then refined with each new source
2. **Cross-Reference Everything**: Entities link to topics, topics link to sources, sources link to contradictions
3. **Flag Contradictions**: When new data conflicts with existing claims, flag it prominently with [[Contradiction]]
4. **Maintain, Don't Repeat**: The LLM updates existing files rather than creating redundant pages
5. **Human Curation, Machine Maintenance**: You source and direct; I synthesize, file, and maintain consistency

---

## Root Folder Restriction

**ONLY these items allowed at root:**
- `_wiki/` — Knowledge base folder
- `docs/` — Documentation folder
- `skills/` — Skill documentation folder
- `raw/` — Staging area folder
- `commands.md` — Main command reference

**NO .md files at root.** All documentation must be in `docs/` folder.

Cannot create anything in root except the 4 folders above and commands.md.

---

## Folder Structure
```
obsidian-vault/
├── commands.md                # Main command reference (ROOT ONLY)
├── docs/                      # ALL documentation & guides
│   ├── CLAUDE.md             # This file - system rules
│   ├── FROM-NOW-ON.md        # How Claude works with this vault
│   ├── skills.md             # Quick skill command reference
│   ├── log.md                # Ingest journal
│   ├── GRAPHIFY-INDEX.md     # Knowledge graph guide
│   ├── IMPLEMENTATION-GUIDE.md
│   ├── MEMORY.md
│   ├── index.md              # Hub - links to all sections
│   ├── guide.md              # Practical use cases
│   ├── CONTRADICTIONS.md     # Contradiction tracker
│   └── daily/                # Daily notes storage
├── skills/                    # Skill documentation
│   ├── superpowers.md
│   ├── claude-mem.md
│   ├── autoresearch.md
│   ├── codex.md
│   ├── obsidian.md
│   ├── graphify.md
│   ├── daily.md
│   ├── schedule.md
│   ├── loop.md
│   └── _STRUCTURE.md         # Template for new skills
├── _wiki/                    # Knowledge base
│   ├── entities/             # People, organizations, products
│   ├── topics/               # Concepts, domains, themes
│   ├── frameworks/           # Methods, models, systems
│   └── sources/              # Raw source metadata
└── raw/                      # Staging area (flat structure)
    ├── [domain]-[title]-[date].md   # All new sources (flat)
    ├── .processed/           # (Hidden) Archived after ingest
    │   └── [old-files]
    ├── .archived/            # (Hidden) Very old files (90+ days)
    │   └── [archived-files]
    └── .gitignore            # Ignore .processed/ folder
```

---

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

### Log Page (docs/log.md)
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
```

### Index Page (docs/index.md)
```
# Wiki Index

## Quick Navigation
- [[log.md]] — View all ingested sources
- [[guide.md]] — How to use this system

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

---

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

---

## Special Tags
- `[[Contradiction]]` — Flag conflicting claims
- `#processed` — Source has been integrated
- `#pending` — Source waiting for integration
- `#archived` — Source set aside (reason noted)

---

## Connection Maintenance Rules

When filling stub pages or updating existing pages, maintain meaningful links:

### When Adding Content to a Stub Page
1. **Search for existing references** — Grep the wiki for mentions of this topic/entity
2. **Find parent topics** — What broader concept does this belong to?
3. **Find child concepts** — What specific examples or subtopics exist?
4. **Update backlinks** — If a parent/related page mentions this, add bidirectional link
5. **Add to "Related" sections** — Include relevant entities, topics, frameworks

### Example: Filling `autoresearch.md`
- Found in: `skills/` folder → links to [[Investigation]], [[Knowledge Synthesis]]
- Connected to: Projects that use research → [[Skyrik]], [[Scene Recreator]]
- Updated: Any pages that reference "automated research" now link here

### Connection Update Checklist
- [ ] Does this page link to its parent topic?
- [ ] Do related pages link back here?
- [ ] Are all mentioned entities linked?
- [ ] Are frameworks/patterns referenced?
- [ ] Is there a bidirectional connection to at least 2-3 other pages?

### Rules
- **No orphaned pages** — Every page should have at least 2 incoming links
- **Bidirectional links** — If A links to B, B should link to A (unless one-directional makes sense)
- **Update source references** — When linking to external docs/projects, add [[Link]] in Related section
- **Don't over-link** — Only link if the connection is semantically meaningful, not forced

---

## Version Control
This wiki is a git repo. Each major integration is committed:
- Commit message: "Ingest: [Source Title] - [Topics/Entities Added]"

---

## Documentation Preferences

**Format:** Straight-to-point, concise, no fluff
- Skip introductions and philosophical preambles
- Start with the essential information
- Summaries should be 1-3 sentences max
- Generated text: markdown with headers, lists, code blocks only
- Long docs: break into bullet points, not paragraphs
- All new docs: SHORT (no lengthy explanations)

---

## File Routing Rules (TLDR Auto-Save)

When saving notes, TLDR checks context and routes to:

| Context | Save Location | Example |
|---------|---------------|---------|
| Testing, Playwright, test failures | `projects/[project]/` | `projects/mujoco-muwtin/2026-04-11-test-failures.md` |
| Skills, commands, workflows | `skills/` | `skills/superpowers.md` |
| Learning, research, patterns | `research/` or `_wiki/topics/` | `research/token-efficiency.md` |
| Daily standup, session notes | `docs/daily/YYYY-MM-DD.md` | `docs/daily/2026-04-11-standup.md` |
| Project bugs, issues | `projects/[project]/issues/` | `projects/mujoco-muwtin/issues/loading-overlay-timeout.md` |
| Code patterns, architecture | `_wiki/frameworks/` | `_wiki/frameworks/playwright-setup.md` |
| General notes | `docs/daily/` | `docs/daily/2026-04-11-notes.md` |

**How TLDR decides:**
1. Detect keywords in conversation (testing, skill, learning, project)
2. Check if project name mentioned
3. Route according to table above
4. Ask user for confirmation if ambiguous

---

## Raw Folder (Staging Area)

**Purpose:** Flat inbox for sources before processing to _wiki/

### Structure
```
raw/
├── [domain]-[title]-YYYY-MM-DD.md      # All new sources (FLAT)
├── .processed/                         # (Hidden) Archived after ingest
│   └── [processed-files]
├── .processed/.archived/               # (Hidden) Very old (90+ days)
│   └── [old-files]
└── .gitignore                          # Ignore .processed/ folder
```

### Naming Convention
- Format: `[domain]-[title]-YYYY-MM-DD.md`
- Example: `tech-transformers-2026-04-11.md`
- Example: `business-funding-2026-04-10.md`

### Workflow
1. **Save** source to: `raw/[domain]-[title]-[date].md`
2. **Quick log** (optional): `/tldr-raw:summarize [file]` → adds to docs/log.md
3. **Full ingest**: `/tldr-raw:ingest [file]` → creates wiki pages, archives source
4. **Auto-cleanup**: Move 90+ day old files to `.processed/.archived/`

### /tldr-raw Skill Commands
- `/tldr-raw:summarize [file]` — Quick summary to log (no processing)
- `/tldr-raw:ingest [file]` — Full process: extract → wiki pages → archive
- `/tldr-raw:convert [file] to [type]` — Specify type: entity/topic/framework/source
- `/tldr-raw:move [file] to [dest]` — Organize: processed or archived

**See:** [[../skills/tldr-raw]]

---

## Skill Registry

See detailed documentation in **`skills/`** — Quick ref in **`docs/skills.md`**

### Raw Processing Skill ✨ NEW
- [[../skills/tldr-raw|tldr-raw]] — Process raw/ sources into wiki

### Installed Skills (5)
- [[../skills/superpowers|superpowers]] — TDD, planning, debugging, code review, git worktrees
- [[../skills/claude-mem|claude-mem]] — Cross-session memory, planning, execution, reports
- [[../skills/codex|codex]] — Code analysis & rescue agent
- [[../skills/obsidian|obsidian]] — Vault integration, markdown, knowledge graphs
- [[../skills/autoresearch|autoresearch]] — Autonomous debugging, audits, scenarios

### Built-in Skills
- [[../skills/graphify|graphify]] — Knowledge graph generation
- [[../skills/daily|daily]] — Daily note creation
- [[../skills/schedule|schedule]] — Scheduled tasks
- [[../skills/loop|loop]] — Recurring commands

---

## Using This Vault

### Quick Commands
See **`commands.md`** (root) for simplified reference with links to detailed docs.

**Most-used:**
- `/superpowers:test-driven-development` — Write test-driven code
- `/claude-mem:make-plan` — Create implementation plans
- `/graphify` — Generate knowledge graphs
- `/autoresearch:autoresearch:debug` — Auto-debug issues

### Full Documentation
- Quick ref: `docs/skills.md`
- Skill details: `skills/[skill-name].md`
- Tool details: `_wiki/tools/[tool-name].md`

---

## Graphify

This vault uses graphify for knowledge graph generation.

**Generated outputs:**
- **Interactive Graph** → `graphify-out/graph.html` (open in browser)
- **Analysis Report** → `graphify-out/GRAPH_REPORT.md` (god nodes, communities, insights)
- **Query API** → `graphify-out/graph.json` (persistent, queryable)
- **Vault Index** → `docs/GRAPHIFY-INDEX.md`

**Rules:**
- Before answering wiki or architecture questions, read graphify-out/GRAPH_REPORT.md for god nodes and community structure
- Use the interactive graph.html to visualize connections between concepts
- After modifying wiki files, regenerate with: `/graphify .`
- Query specific relationships with: `/graphify query "your question"`

---

**Last Updated:** 2026-04-11  
**Maintained By:** Claude (you direct, I synthesize)
