---
type: skill-guide
plugin: claude-mem@thedotmack
updated: 2026-04-13
tags:
  - skills
  - claude-mem
  - memory
  - planning
---

# Claude-Mem

> Cross-session memory, planning, code exploration, and knowledge management.

## Commands

| Command | Category | What It Does |
|---------|----------|-------------|
| `/claude-mem:make-plan` | Planning | Phased implementation plan with discovery and verification |
| `/claude-mem:do` | Execution | Execute plan using parallel subagents for independent tasks |
| `/claude-mem:smart-explore` | Code Analysis | Token-efficient structural code search using tree-sitter AST |
| `/claude-mem:knowledge-agent` | Knowledge | Build and query AI-powered knowledge bases from observations |
| `/claude-mem:timeline-report` | Knowledge | Generate "Journey Into [Project]" narrative from git history |
| `/claude-mem:mem-search` | Memory | Search persistent cross-session memory database |
| `/claude-mem:version-bump` | Release | Automated semantic versioning and release workflow |

## When to Use

- **make-plan** over superpowers:writing-plans when you need phased discovery (explore → plan → execute)
- **smart-explore** over grep/glob when you need structural understanding (AST-level, not text-level)
- **mem-search** when you need context from previous sessions that isn't in git

## See Also

- [[commands]] — Quick reference table
- [[superpowers]] — Alternative planning and execution workflows
