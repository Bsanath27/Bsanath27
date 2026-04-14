---
type: summary
date: 2026-04-13
project: projects-wiki
tags:
  - documentation
  - obsidian
  - wiki
---

# Documentation Format Setup

> Established consistent summary and commands format with bidirectional wikilinks for the projects-wiki vault.

## Context
The vault lacked a standardized format for session summaries and command references. Navigation between commands and their context was manual and inconsistent.

## What Changed
- Created `docs/commands.md` as a single command reference file, sectioned by tool
- Created `docs/summaries/` directory for topic-based session summaries
- Added format rules to CLAUDE.md enforcing frontmatter, TL;DR blockquotes, and mutual wikilinks
- Added a stop hook that reminds to update docs when code changes happen without doc updates

## Key Learnings
- Bidirectional wikilinks (summary → commands, commands → summary) make Obsidian graph view useful
- Stop hooks can enforce documentation habits without blocking workflow (exit 2 = warn, not block)
- Frontmatter tags enable Obsidian Dataview queries across all summaries

## Commands
- `chmod +x ~/.claude/hooks/stop-docs-reminder.sh` → [[commands#Shell|make hook executable]]

## See Also
- [[commands#Claude Code]]
