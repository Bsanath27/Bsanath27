# LLM Wiki — Table of Contents

**Summary**: Master index of all pages in this wiki. Updated as new pages are created.

**Last updated**: 2026-04-14

---

## Using This Wiki

This wiki follows [Andrej Karpathy's LLM pattern](https://github.com/karpathy/LLMs.txt) — a structured, interlinked knowledge base maintained by Claude Code.

- **Raw sources** are stored in `/raw/` (immutable)
- **Wiki pages** live in `/wiki/` (curated and linked)
- **Operations** are logged in `/wiki/log.md`

## How to Add Content

1. Place source documents in `raw/`
2. Ask Claude to ingest: "Ingest [source name]"
3. Claude will create summary page + concept pages + cross-links
4. Review and refine

## Pages by Category

### Core Operations
- [[sources-index]] — Maps raw sources to wiki pages they feed into
- [[_metrics]] — Learning velocity + building velocity dashboard
- [[_log]] — Append-only changelog of all wiki updates

### Topics
- [[parameter-golf]] — OpenAI's 16MB LM challenge: quantization, architecture, compression techniques

### Projects & Tracking
- [[projects/current.md]] — Active projects and shipping status
- [[daily.md]] — Daily work log (learning + building + next)

---

**Need to add a source?** Place files in `/raw/` and ask me to ingest them.

**Want to ask a question?** I'll search this wiki and cite relevant pages in my answer.
