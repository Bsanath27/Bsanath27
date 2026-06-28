# Wiki Health Report — 2026-06-28

> **STILL EMPTY:** `wiki/`, `raw/inbox/`, `_index.md`, `_log.md`, `sources-index.md` do not exist on disk.
> This is the **fourth consecutive audit** (previous: 2026-05-31, 2026-06-07, 2026-06-21) finding zero wiki content.
> Each prior report recommended bootstrapping content or pausing the audit. Neither has happened.

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
1. **Four straight empty audits.** This routine has produced zero signal every week it's run. Either seed real content now (`wiki/_index.md`, at least one page with `title:`/`updated:`/`related:` frontmatter, a populated `raw/inbox/`) or pause this scheduled job until content exists.
2. Verify the wiki isn't actually maintained in a different repo/path — confirmed via filesystem search this run that no `wiki/`, `raw/`, or `_index.md` exists anywhere under the repo root, so this isn't a path typo in the audit itself.
3. Assign an owner and date for seeding the wiki; otherwise expect a fifth empty audit next week.
