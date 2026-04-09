# LLM Wiki — Custom Commands

Patterns you paste into Claude to interact with your wiki. These are prompts, not slash commands.

---

## 🚀 Quick Cheat Sheet

**Adding Knowledge:**
```
Ingest: raw/inbox/tech-article-2026-04-09.md
Ingest: https://example.com/article
```

**Exploring:**
```
Explore: [[Large Language Models]]
Synthesis: How do my sources view AI safety?
Compare: [[Machine Learning]] across sources
```

**Maintenance:**
```
Audit: Check wiki health
Weekly: Generate synthesis (auto-runs Sundays 7:19 PM)
```

**Querying:**
```
Filter: domain:tech
Filter: tag:ai-ml source_count:>=3
Export: [[Topic Name]] as summary
```

---

## Full Command Reference

### **INGEST** — Add source to wiki

**Pattern:** `Ingest: [URL | file path | pasted text]`

Copy-paste examples:
- `Ingest: https://example.com/article-about-ai`
- `Ingest: raw/inbox/tech-transformers-2026-04-09.md`
- `Ingest: [pasted article text] — quick summary` (news articles)
- `Ingest: [pasted paper] — detailed analysis` (research papers)

**What happens:**
- Extract key information
- Create/update pages in `_wiki/topics/`, `_wiki/entities/`, `_wiki/frameworks/`
- Cross-reference all related pages
- Update `log.md`
- Auto-commit to git
- Report changes

---

### **EXPLORE** — Deep dive on a topic

**Pattern:** `Explore: [[Topic Name]]`

Copy-paste examples:
- `Explore: [[Machine Learning]]`
- `Explore: [[AI Safety]] with all sources`
- `Explore: [[Cesium.js]] and related entities`

**What happens:**
- Show definition & key concepts
- List all sources mentioning it
- Display related topics/entities
- Show how understanding evolved
- Flag contradictions
- Suggest follow-up sources

---

### **SYNTHESIS** — Cross-topic analysis

**Pattern:** `Synthesis: [Your question]`

Copy-paste examples:
- `Synthesis: How do my sources view the future of AI safety?`
- `Synthesis: What's the relationship between X and Y?`
- `Synthesis: What do sources agree/disagree on about [[Topic]]?`
- `Synthesis: Connect these three concepts: A, B, C`

**What happens:**
- Synthesize across relevant pages
- Update pages with new connections
- Flag contradictions
- Return synthesis + updated pages

---

### **COMPARE** — See source agreement/disagreement

**Pattern:** `Compare: [[Topic]]`

Copy-paste examples:
- `Compare: [[Large Language Models]] across all sources`
- `Compare: [[AI Alignment]] — show consensus vs disagreements`

**What happens:**
- List all sources covering the topic
- Show consensus points
- Highlight disagreements
- Explain nuances
- Assess source quality
- Show your current synthesis

---

### **WEEKLY** — Generate synthesis report

**Pattern:** `Weekly: [for week of DATE to DATE]` (or just `Weekly:`)

Copy-paste examples:
- `Weekly: for week of 2026-04-07 to 2026-04-13`
- `Weekly:` (auto-uses current/last week)

**What happens:**
- Review all ingests from the week
- Summarize by domain (tech, business, science, philosophy)
- List new topics/entities created
- Flag contradictions found
- Suggest follow-up areas
- Create file: `_wiki/syntheses/Weekly-[YYYY-WXX].md`

*Also runs automatically every Sunday 7:19 PM*

---

### **AUDIT** — Check wiki health

**Pattern:** `Audit:`

Copy-paste: `Audit:`

**What happens:**
- Identify orphaned pages (few cross-references)
- Flag outdated information
- Find merge opportunities
- Check contradiction status
- Suggest missing links
- Report overall health score

---

### **FILTER** — Query by metadata

**Pattern:** `Filter: [criteria]`

Copy-paste examples:
- `Filter: domain:tech`
- `Filter: tag:ai-ml created:2026-03`
- `Filter: source_count:>=3`
- `Filter: tag:ai-ml AND domain:tech`

**Options:**
- `domain:` (tech, business, science, philosophy)
- `tag:` (any tag in frontmatter)
- `created:` (YYYY-MM-DD or YYYY-MM)
- `updated:` (date range)
- `source_count:` (>= N)

---

### **EXPORT** — Generate summary export

**Pattern:** `Export: [[Topic]] as [format]`

Copy-paste examples:
- `Export: [[Machine Learning]] as summary`
- `Export: [[AI Alignment]] as outline with sources`
- `Export: [[Tech Topic]] as list`

**Formats:** full-page, summary, outline, list

---

---

## Workflow Chains

**After finding an article:**
```
Ingest: [URL]
→ Wait a day or two for more sources
→ Weekly: (auto-runs Sunday, or run manually)
```

**Deep understanding:**
```
Explore: [[Topic]]
→ Synthesis: [Follow-up question based on exploration]
→ Compare: [[Related Topic]] (see connections)
→ Audit: (ensure everything is linked)
```

**Regular maintenance:**
```
Weekly: (end of each week)
→ Audit: (monthly or as needed)
→ Filter: domain:tech (browse by domain)
```

---

## Pro Tips

- **Batch ingests** — Add 3-5 sources before running Weekly
- **Token efficiency** — Use "quick summary" for news, "detailed" for papers
- **Exploration chains** — Explore → Synthesis → Compare → Audit
- **Auto-runs** — Weekly synthesis every Sunday 7:19 PM (no action needed)
- **Always git tracked** — Every ingest/update creates a commit

---

## How Commands Work

All commands follow the LLM Wiki schema in `CLAUDE.md`:
- Pages created/updated in proper `_wiki/` folders
- Frontmatter applied automatically
- Cross-references maintained
- `log.md` updated automatically
- Contradictions flagged
- Git commits created automatically

**You just paste the pattern → Claude handles the rest.**

---

## See Also

- `CLAUDE.md` — System schema and rules
- `FROM-NOW-ON.md` — How Claude works with your wiki
- `log.md` — Complete ingest history
- `docs/index.md` — Wiki navigation hub
