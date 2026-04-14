---
type: skill-guide
plugin: nlm-search-source (custom skill)
updated: 2026-04-13
tags:
  - skills
  - nlm
  - notebooklm
  - research
---

# NotebookLM Search Source

> Search NotebookLM → filter to high-quality sources → store in raw/ → trigger wiki ingest.

**Skill location:** `~/.claude/skills/nlm-search-source/SKILL.md`

## Quick Workflow

```
1. Define: topic + filter criteria (domain, type, recency, target count)
2. Create: nlm notebook + run research (30s fast / 5min deep)
3. Extract: list all sources + download each to evaluate
4. Filter: apply YES/NO criteria, rank by relevance
5. Store: save filtered set to raw/[topic]/ with sources.json + README
6. Ingest: run wiki ingest workflow on raw/[topic]/
7. Cleanup: delete NotebookLM notebook (optional)
```

## CLI Commands

| Command | Purpose |
|---------|---------|
| `nlm notebook create "[Title]"` | Create new notebook |
| `nlm notebook list` | List all notebooks |
| `nlm research start "query" --mode fast` | Web search (30s) |
| `nlm research start "query" --mode deep` | Deep research (5min) |
| `nlm research status <notebook-id>` | Check progress |
| `nlm research import <notebook-id> <task-id>` | Import discovered sources |
| `nlm source list <notebook-id>` | List sources |
| `nlm source content <source-id>` | Extract raw text |
| `nlm notebook delete <id> --confirm` | Cleanup |

## Output Structure

```
raw/[topic]/
  sources.json    # Metadata index (required)
  README.md       # Filtering decisions
  [source-1].md   # Source content
  [source-2].md
```

## Filtering Criteria

- Matches topic specifically (not tangentially)
- Within recency window (default: 24 months)
- Clear authorship (not anonymous)
- Substantive content (not just headline)
- Not paywalled, not Wikipedia, not duplicate

## See Also

- [[commands]] — Quick reference table
- [[firecrawl]] — Alternative web content extraction
- [[wiki-commands]] — Ingest workflow for after source collection
- [[_wiki/skills/nlm-search-source]] — Extended guide with full examples
