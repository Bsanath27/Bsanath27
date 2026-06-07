# Wiki Health Report — 2026-06-07

> **STILL EMPTY:** `wiki/`, `raw/inbox/`, `_index.md`, `_log.md`, `sources-index.md` do not exist on disk.
> This is the second consecutive audit (previous: 2026-05-31) finding zero wiki content — the bootstrap
> action items from last week were not carried out.

## Stale Pages (>30 days)
None — `wiki/` directory does not exist; no pages to check.

## Orphan Pages (no incoming links)
None — no wiki pages exist.

## Cross-link Gaps (0 related links)
None — `_index.md` does not exist; no page list to check.

## Contradictions
### Resolved this week
None

### New findings
None — no wiki content exists to scan.

## Inbox Backlog (>7 days unprocessed)
Clear — `raw/inbox/` does not exist (nothing to ingest).

## Action Items (top 3 priorities)
1. **Wiki has been empty for two straight audit cycles.** Last week's plan (create `_index.md`, stub pages, seed `CONTRADICTIONS.md`) was not executed and the claimed directory skeleton never persisted (git doesn't track empty dirs). Either populate the wiki this week or pause this audit until there's content — running it weekly against nothing is wasted motion.
2. Create `wiki/_index.md` plus at least one real stub page with `title:`, `updated:`, and `related:`/`see_also:` frontmatter so future audits have something to check against.
3. Drop a real source into `raw/inbox/` and run it through the ingest pipeline end-to-end — still unvalidated after two cycles.
