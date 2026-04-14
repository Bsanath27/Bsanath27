# LLM Wiki

A personal knowledge base maintained by Claude Code.

Based on Andrej Karpathy's LLM Wiki pattern.

## Purpose

This wiki is a structured, interlinked knowledge base for all my projects and AI knowledge.

Claude maintains the wiki. The human curates sources, asks questions, and guides the analysis.

## Folder structure

```
raw/               -- source documents (immutable -- never modify these)
wiki/              -- markdown pages maintained by Claude
  index.md         -- table of contents for the entire wiki
  log.md           -- append-only record of all operations
  sources-index.md -- maps raw files to wiki pages they feed into
  metrics.md       -- learning velocity + building velocity dashboard
projects/          -- project tracking and shipping status
daily.md           -- single table: daily work, learning, shipping log
```

## Ingest workflow

When the user adds a new source to `raw/` and asks you to ingest it:

1. Read the full source document
2. Discuss key takeaways with the user before writing anything
3. Create ONE canonical page in `wiki/` for the source (e.g., `wiki/ai-robotics.md`)
4. Identify which existing wiki pages this source relates to (read wiki/index.md first)
5. Update related concept pages to link to the new source using `[[canonical-page]]`
6. Create new concept pages ONLY if the source bridges topics in a novel way (no ghost pages)
7. Update `wiki/sources-index.md`: map raw/file → [all wiki pages it feeds into]
8. Update `wiki/index.md` if new concept pages were created
9. Append an entry to `wiki/log.md`: date + source name + which pages were created/updated
10. Update `wiki/metrics.md` if the source relates to learning
11. Update `projects/current.md` if it relates to active work

**Key principle**: Be selective about connections. Link only where meaningful, not mechanically.

## Page format

Every wiki page should follow this structure (adapt as needed):

```markdown
# Page Title

**Summary**: Concise overview. Length depends on topic complexity.

**Sources**: List of raw source files this page draws from.

**Last updated**: Date of most recent update.

---

## Introduction
Concise intro — what is this?

## Methodology (if relevant)
How was this approach developed or tested?

## Views/Perspectives (if relevant)
Different approaches or perspectives on this topic.

## Explanation
Key insights and details. Use tables, charts, or visualizations for data.

## Conclusion
Outcome-focused takeaway — why does this matter?

## Related pages

- [[related-concept-1]]
- [[related-concept-2]]
```

### Summary Style Guide

Summaries must be:
- **Concise and to the point** — no over-explanation
- **Outcome-focused** — what can you do with this knowledge?
- **Flexible length** — 1-3 sentences depending on topic complexity
- **Specific** — "AI systems for robotics" not "about AI"
- **Use visualizations** — tables, charts, graphs for numbers/data
- **No ghost pages** — only create pages with real substance

Examples:
- ✅ "Core patterns for building AI-powered applications"
- ❌ "AI patterns"
- ✅ [Data table] "Learning velocity metrics: concepts/week, projects/month"
- ❌ "Learning and metrics"

## Citation rules

- Every factual claim should reference its source file
- Use the format (source: filename.pdf) after the claim
- If two sources disagree, note the contradiction explicitly
- If a claim has no source, mark it as needing verification

## Question answering

When the user asks a question:

1. Read `wiki/index.md` first to find relevant pages
2. Read those pages and synthesize an answer
3. Cite specific wiki pages in your response
4. If the answer is not in the wiki, say so clearly
5. If the answer is valuable, offer to save it as a new wiki page

Good answers should be filed back into the wiki so they compound over time.

## Lint

When the user asks you to lint or audit the wiki:

- Check for contradictions between pages
- Find orphan pages (no inbound links from other pages)
- Identify concepts mentioned in pages that lack their own page
- Flag claims that may be outdated based on newer sources
- Check that all pages follow the page format above
- Report findings as a numbered list with suggested fixes

## Rules

- Never modify anything in the `raw/` folder
- Always update `wiki/index.md`, `wiki/log.md`, and `wiki/sources-index.md` after changes
- Keep page names lowercase with hyphens (e.g. `machine-learning.md`)
- Write in clear, plain language
- **Summaries: concise, outcome-focused. Length varies by topic (1-3 sentences).**
- **No ghost pages** — only create pages with real substance
- **No over-explanation** — use tables/charts for data, keep text concise
- **No date-based file naming** — use topic/purpose-based names instead (e.g., `daily.md` not `daily-2026-04-13.md`)
- When a source relates to multiple topics, create ONE canonical page and link from all related pages
- When adding sources, be selective about connections (meaningful links, not mechanical)
- Update `daily.md` with learning/building progress after ingesting
- Update `wiki/metrics.md` when ingesting learning-related sources
- Update `projects/current.md` when ingesting sources related to active work
- When uncertain about how to categorize something, ask the user

# Documentation — Commands, Summaries & Skills

## File Locations
- **Commands:** `commands.md` (root — single file, the canonical command reference)
- **Summaries:** `docs/summaries/<topic>.md` (kebab-case, one file per session/topic)
- **Skill guides:** `docs/skills/<tool>.md` (one file per tool/plugin group)

## Raw Folder — Default for All Inputs
**All web clips, articles, files, and external content go into `raw/` first.**
- Browser clips → `raw/inbox/[topic].md`
- Downloaded files → `raw/inbox/[filename]`
- Research sources → `raw/[topic]/` (via nlm-search-source workflow)
- Never put external content directly into `wiki/` or `docs/`

## Before Writing or Updating Docs
1. **Read existing files first.** Read `commands.md` (root) and scan `docs/summaries/` before creating anything.
2. **Find connections.** When creating a new summary, check existing summaries for related topics — add mutual [[wikilinks]] in See Also on both sides.
3. **Append, don't rewrite.** Add new commands to existing sections in `commands.md`. Add new H2 sections alphabetically. Never restructure what's already there.

## commands.md Format (root)
```
---
type: reference
updated: YYYY-MM-DD
tags: [commands, reference]
---
```
- **Three sections:** Skills (slash commands) | Wiki Commands (text patterns) | CLI Commands
- **Within Skills:** group by phase (Research, Planning, Development, Quality, etc.)
- **Table columns:** `Command | Purpose | Guide` — Guide column always links to `[[docs/skills/tool-name]]`
- **No duplicate commands** — if a command moves phases, update in place
- **Update `updated` date** in frontmatter when adding entries
- **New tool group:** create a new H3 under the matching phase, add a `docs/skills/<tool>.md` page

## docs/skills/<tool>.md Format
```
---
type: skill-guide
plugin: plugin-name
updated: YYYY-MM-DD
tags: [skills, tool-name]
---
```
- One file per tool/plugin group (e.g., `superpowers.md`, `firecrawl.md`)
- Sections: Commands table → When to Use → Workflows → See Also
- Commands table: `Command | What It Does` (no Context column — that lives in commands.md)
- Always include `[[commands]]` in See Also

## docs/summaries/<topic>.md Format
```
---
type: summary
date: YYYY-MM-DD
project: project-name
tags: [kebab-case, tags]
---
```
- TL;DR blockquote directly under H1
- Sections: Context → What Changed → Key Learnings → Commands → See Also
- Omit empty sections
- Commands section: `command` → `[[commands#Section|description]]`
- See Also: mutual — if you link [[B]], add [[this-file]] to B's See Also

## Connection Integrity
1. Every new command in `commands.md` → Guide column links to `docs/skills/<tool>.md`
2. Every `docs/skills/<tool>.md` → See Also links back to `[[commands]]`
3. Every `docs/summaries/` entry with commands → both directions maintained
4. Scan existing summaries for tag overlaps when creating new ones

## When to Update
After sessions where you: used non-trivial commands, made architecture decisions, solved a non-obvious problem, or set up/configured a new tool.
