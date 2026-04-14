---
type: reference
updated: 2026-04-13
tags:
  - commands
  - reference
---

# Commands

> Quick-scan reference. Every command links to its detailed guide in `docs/skills/`.

---

## Skills — Slash Commands

### Research & Discovery

| Command | Purpose | Guide |
|---------|---------|-------|
| `/nlm-search-source` | Search NotebookLM → filter → store in raw/ → ingest | [[nlm-search-source]] |
| `/firecrawl-search` | Web search with full page content extraction | [[firecrawl]] |
| `/firecrawl-scrape` | Extract clean markdown from any URL | [[firecrawl]] |
| `/firecrawl-crawl` | Bulk extract content from an entire website | [[firecrawl]] |
| `/firecrawl-map` | Discover and list all URLs on a website | [[firecrawl]] |
| `/firecrawl-agent` | AI-powered autonomous data extraction | [[firecrawl]] |
| `/firecrawl-interact` | Control a live browser session on any page | [[firecrawl]] |
| `/defuddle` | Extract clean markdown from web pages | [[obsidian-tools]] |

### Planning & Design

| Command | Purpose | Guide |
|---------|---------|-------|
| `/superpowers:brainstorming` | Explore ideas before starting | [[superpowers]] |
| `/superpowers:writing-plans` | Create detailed step-by-step plan | [[superpowers]] |
| `/claude-mem:make-plan` | Phased planning with discovery | [[claude-mem]] |

### Development

| Command | Purpose | Guide |
|---------|---------|-------|
| `/superpowers:test-driven-development` | TDD: write test → code → refactor | [[superpowers]] |
| `/superpowers:using-git-worktrees` | Isolated feature branches | [[superpowers]] |
| `/superpowers:executing-plans` | Execute plan with checkpoints | [[superpowers]] |
| `/superpowers:subagent-driven-development` | Execute plans with independent subagents | [[superpowers]] |
| `/claude-mem:do` | Execute plan using parallel subagents | [[claude-mem]] |

### Quality & Review

| Command | Purpose | Guide |
|---------|---------|-------|
| `/superpowers:requesting-code-review` | Request feedback before merge | [[superpowers]] |
| `/superpowers:receiving-code-review` | Implement review comments | [[superpowers]] |
| `/superpowers:verification-before-completion` | Verify work is truly done | [[superpowers]] |

### Debugging

| Command | Purpose | Guide |
|---------|---------|-------|
| `/superpowers:systematic-debugging` | Methodical root-cause analysis | [[superpowers]] |
| `/autoresearch:autoresearch:debug` | Autonomous bug-hunting loop | [[autoresearch]] |
| `/autoresearch:autoresearch:fix` | Iterative fix loops | [[autoresearch]] |

### Autonomous Workflows

| Command                               | Purpose                            | Guide            |
| ------------------------------------- | ---------------------------------- | ---------------- |
| `/autoresearch:autoresearch`          | Goal-directed iteration            | [[autoresearch]] |
| `/autoresearch:autoresearch:learn`    | Auto-documentation generator       | [[autoresearch]] |
| `/autoresearch:autoresearch:security` | STRIDE + OWASP audit               | [[autoresearch]] |
| `/autoresearch:autoresearch:scenario` | Scenario-driven use case generator | [[autoresearch]] |
| `/autoresearch:autoresearch:ship`     | Universal shipping workflow        | [[autoresearch]] |
| `/autoresearch:autoresearch:reason`   | Multi-agent adversarial refinement | [[autoresearch]] |
| `/autoresearch:autoresearch:predict`  | Multi-persona swarm prediction     | [[autoresearch]] |
| `/autoresearch:autoresearch:plan`     | Interactive scope/metric wizard    | [[autoresearch]] |

### Code Analysis

| Command | Purpose | Guide |
|---------|---------|-------|
| `/codex:rescue` | Delegate investigation or fix to Codex | [[codex]] |
| `/codex:setup` | Configure Codex CLI | [[codex]] |
| `/claude-mem:smart-explore` | Token-efficient AST-based code search | [[claude-mem]] |

### Knowledge & Documentation

| Command | Purpose | Guide |
|---------|---------|-------|
| `/claude-mem:knowledge-agent` | Build AI knowledge bases | [[claude-mem]] |
| `/claude-mem:timeline-report` | Generate project development narrative | [[claude-mem]] |
| `/claude-mem:mem-search` | Query cross-session memory | [[claude-mem]] |
| `/claude-mem:version-bump` | Semantic versioning automation | [[claude-mem]] |
| `/superpowers:writing-skills` | Create reusable skill docs | [[superpowers]] |

### Obsidian & Vault

| Command | Purpose | Guide |
|---------|---------|-------|
| `/obsidian-cli` | Read, create, search vault notes | [[obsidian-tools]] |
| `/obsidian-markdown` | Create OFM with wikilinks, embeds, callouts | [[obsidian-tools]] |
| `/obsidian-bases` | Create/edit Obsidian database views | [[obsidian-tools]] |
| `/json-canvas` | Create knowledge graph canvases | [[obsidian-tools]] |
| `/daily` | Open/create daily note | [[obsidian-tools]] |
| `/vault-setup` | Interactive vault configurator | [[obsidian-tools]] |

### Design

| Command | Purpose | Guide |
|---------|---------|-------|
| `/figma` (MCP) | Autonomous wireframe generation and design | [[figma]] |

### Operations

| Command | Purpose | Guide |
|---------|---------|-------|
| `/superpowers:finishing-a-development-branch` | Integrate, merge, ship | [[superpowers]] |
| `/superpowers:dispatching-parallel-agents` | Run independent tasks in parallel | [[superpowers]] |
| `/schedule` | Create cron-scheduled tasks | [[built-in-tools]] |
| `/loop` | Run commands on recurring interval | [[built-in-tools]] |
| `/graphify` | Generate interactive knowledge graphs | [[graphify]] |
| `/graphify query "question"` | Query relationships in graphs | [[graphify]] |
| `/tldr` | Save conversation summary to vault | [[built-in-tools]] |
| `/simplify` | Review changed code for quality | [[built-in-tools]] |

---

## Wiki Commands — Text Patterns

> Paste these patterns into Claude to interact with your knowledge base.

| Pattern | Purpose | Guide |
|---------|---------|-------|
| `Ingest: [URL or file path]` | Add source to wiki | [[wiki-commands]] |
| `Ingest: [pasted text] — quick summary` | Ingest pasted content | [[wiki-commands]] |
| `Explore: [[Topic]]` | Deep dive on a topic | [[wiki-commands]] |
| `Synthesis: [question]` | Cross-topic analysis | [[wiki-commands]] |
| `Compare: [[Topic]]` | See agreement/disagreement across sources | [[wiki-commands]] |
| `Weekly:` | Generate weekly synthesis report | [[wiki-commands]] |
| `Audit:` | Check wiki health | [[wiki-commands]] |
| `Filter: [criteria]` | Query by metadata (domain, tag, date) | [[wiki-commands]] |
| `Export: [[Topic]] as [format]` | Generate summary export | [[wiki-commands]] |
| `Process: raw/inbox/[file]-SUMMARY.md` | TLDR workflow integration | [[wiki-commands]] |
| `Batch process: raw/inbox/` | Process all files in inbox | [[wiki-commands]] |

---

## CLI Commands — Shell

### NotebookLM

| Command | Purpose | Guide |
|---------|---------|-------|
| `nlm notebook create "[Title]"` | Create new notebook | [[nlm-search-source]] |
| `nlm notebook list` | List all notebooks | [[nlm-search-source]] |
| `nlm notebook delete <id> --confirm` | Delete notebook | [[nlm-search-source]] |
| `nlm notebook query <id> "question"` | Chat with sources | [[nlm-search-source]] |
| `nlm source list <notebook-id>` | List sources in notebook | [[nlm-search-source]] |
| `nlm source content <source-id>` | Extract raw text | [[nlm-search-source]] |
| `nlm source add <id> --url "..."` | Add URL source | [[nlm-search-source]] |
| `nlm research start "query" --mode fast` | Web search (30s) | [[nlm-search-source]] |
| `nlm research start "query" --mode deep` | Deep research (5min) | [[nlm-search-source]] |
| `nlm research status <notebook-id>` | Check research progress | [[nlm-search-source]] |
| `nlm research import <notebook-id> <task-id>` | Import discovered sources | [[nlm-search-source]] |

### System

| Command | Purpose |
|---------|---------|
| `kill $(lsof -ti :3000)` | Kill process on specific port |
| `kill -9 $(lsof -t -i)` | Kill all port-bound processes |
| `netstat -tulnp` | View all open ports |
| `rm -f ~/.git/objects/pack/tmp_pack_*` | Remove temp git pack files |

---

## Workflow Chains

### Add a Feature
```
/superpowers:brainstorming → /superpowers:writing-plans → /superpowers:test-driven-development → /superpowers:requesting-code-review → /superpowers:finishing-a-development-branch
```

### Debug a Bug
```
/superpowers:systematic-debugging → /autoresearch:autoresearch:debug → /superpowers:test-driven-development → /autoresearch:autoresearch:fix
```

### Research → Wiki
```
/nlm-search-source → Ingest: raw/[topic]/ → Weekly: → Audit:
```

### Analyze Codebase
```
/graphify . → /claude-mem:smart-explore → /claude-mem:knowledge-agent → /claude-mem:mem-search
```

---

**See Also:** [[_wiki/skills]] | [[CLAUDE.md]]
