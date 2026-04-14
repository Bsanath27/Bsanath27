---
type: skill-guide
plugin: superpowers@claude-plugins-official
updated: 2026-04-13
tags:
  - skills
  - superpowers
  - development
---

# Superpowers

> Full development lifecycle — planning, TDD, review, debugging, shipping.

## Commands

| Command | Phase | What It Does |
|---------|-------|-------------|
| `/superpowers:brainstorming` | Planning | Explore ideas, requirements, and trade-offs before starting work |
| `/superpowers:writing-plans` | Planning | Create detailed multi-step implementation plan with decision points |
| `/superpowers:test-driven-development` | Development | Write test first → implement → refactor cycle |
| `/superpowers:using-git-worktrees` | Development | Isolated feature branches for parallel work |
| `/superpowers:executing-plans` | Development | Execute a written plan with checkpoints |
| `/superpowers:subagent-driven-development` | Development | Execute plans with independent parallel subagents |
| `/superpowers:requesting-code-review` | Quality | Request feedback on completed work before merging |
| `/superpowers:receiving-code-review` | Quality | Implement review feedback systematically |
| `/superpowers:verification-before-completion` | Quality | Verify work is truly done before claiming completion |
| `/superpowers:systematic-debugging` | Debugging | Methodical root-cause analysis and fix verification |
| `/superpowers:finishing-a-development-branch` | Operations | Integrate, merge, and ship complete features |
| `/superpowers:dispatching-parallel-agents` | Operations | Run 2+ independent tasks in parallel with coordination |
| `/superpowers:writing-skills` | Documentation | Create and test reusable process documentation (skills) |

## Common Workflows

**Adding a feature:**
```
brainstorming → writing-plans → test-driven-development → requesting-code-review → finishing-a-development-branch
```

**Debugging:**
```
systematic-debugging → test-driven-development (add regression test) → verification-before-completion
```

## See Also

- [[commands]] — Quick reference table
- [[autoresearch]] — Autonomous alternatives for debugging and fixing
- [[claude-mem]] — Planning and execution alternatives
