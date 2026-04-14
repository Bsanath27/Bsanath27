---
type: skill-guide
plugin: built-in
updated: 2026-04-13
tags:
  - skills
  - graphify
  - knowledge-graph
  - visualization
---

# Graphify

> Generate interactive knowledge graphs from code, docs, images, and research.

## Commands

| Command | What It Does |
|---------|-------------|
| `/graphify .` | Analyze current directory |
| `/graphify /path/to/folder` | Analyze specific folder |
| `/graphify query "question"` | Query relationships in generated graphs |

## What It Reads

- Code files (classes, functions, imports)
- Markdown and documentation
- PDFs and research papers
- Images and screenshots (vision-based)
- Whiteboard photos and diagrams

## Output

```
graphify-out/
  graph.html     # Interactive visualization
  GRAPH_REPORT.md # Analysis and patterns
  graph.json     # Queryable data
```

## Configuration

Create `.graphifyignore` in project root to exclude folders (same syntax as `.gitignore`).

## See Also

- [[commands]] — Quick reference table
- [[claude-mem]] — smart-explore for AST-based code search
