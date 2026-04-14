---
type: skill-guide
updated: 2026-04-13
tags:
  - skills
  - wiki
  - commands
---

# Wiki Commands

> Text patterns you paste into Claude to interact with your knowledge base. These are prompts, not slash commands.

## Commands

| Pattern | What It Does |
|---------|-------------|
| `Ingest: [URL or file path]` | Add source to wiki — extracts info, creates/updates pages, cross-references |
| `Ingest: [pasted text] — quick summary` | Ingest pasted content (news articles) |
| `Ingest: [pasted text] — detailed analysis` | Ingest with deep analysis (research papers) |
| `Explore: [[Topic]]` | Deep dive — definition, sources, related topics, contradictions |
| `Synthesis: [question]` | Cross-topic analysis — synthesize across pages, flag contradictions |
| `Compare: [[Topic]]` | Source agreement/disagreement — consensus, disputes, nuances |
| `Weekly:` | Generate weekly synthesis report (auto-runs Sundays 7:19 PM) |
| `Audit:` | Wiki health check — orphans, outdated info, missing links |
| `Filter: [criteria]` | Query by metadata — `domain:tech`, `tag:ai-ml`, `created:2026-03` |
| `Export: [[Topic]] as [format]` | Generate export — formats: full-page, summary, outline, list |

## TLDR Workflow (Gemini + Claude)

| Pattern | What It Does |
|---------|-------------|
| `Process: raw/inbox/[file]-SUMMARY.md` | Process Gemini summary into wiki |
| `Summarize: raw/inbox/[file].md` | Quick summary only |
| `Batch process: raw/inbox/` | Process all files in inbox |

**Flow:** Save raw source → Gemini summarizes → Claude integrates into wiki → raw/inbox/ stays clean.

## Workflow Chains

**After finding an article:**
```
Ingest: [URL] → wait for more sources → Weekly:
```

**Deep understanding:**
```
Explore: [[Topic]] → Synthesis: [follow-up] → Compare: [[Related Topic]] → Audit:
```

**Regular maintenance:**
```
Weekly: → Audit: → Filter: domain:tech
```

## See Also

- [[commands]] — Quick reference table
- [[nlm-search-source]] — Source discovery before ingest
- [[firecrawl]] — Web scraping for source extraction
