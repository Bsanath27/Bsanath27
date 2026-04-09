# From Now On — Your LLM Wiki Instructions

This document tells Claude how to work with you going forward.

---

## Core Behavior

From this moment forward:

### Path A: Ingest from raw/inbox/ (I Synthesize)

1. **You save source to: raw/inbox/[domain]-[title]-[date].md**
2. **You tell me: "Ingest: raw/inbox/[filename]"**
3. **I will:**
   - Read and extract key information
   - Create/update wiki pages in _wiki/ folders
   - Apply correct frontmatter and formatting
   - Cross-reference all related pages
   - Flag contradictions
   - Update log.md with source metadata
   - Move file to raw/processed/ (archive)
   - Create git commit: `"Ingest: [Title] - [Topics]"`
   - Report what changed

### Path B: Create with Gemini CLI (I Integrate)

**Workflow:** You create wiki pages locally using Gemini CLI, I handle integration.

1. **You use Gemini CLI to generate pages** (templates in docs/GEMINI-TEMPLATES.md)
   - Example: `"Create topic page about X" → Gemini generates markdown`
   - Save to: `_wiki/topics/[Name].md`, `_wiki/entities/[Name].md`, etc.
   - Takes 2-5 minutes per page (fast!)

2. **You tell me**: `"Integrate: _wiki/[folder]/[filename].md"`
   - I will:
     - Validate the page follows schema
     - Add/enhance frontmatter if needed
     - Cross-link to related pages
     - Update docs/index.md
     - Update log.md (note: "Self-created + integrated")
     - Create git commit: `"Integrate: [Page] (self-created)"`
     - Report what links were created

**Why this matters:**
- You use Gemini for fast page generation (saves your Claude tokens)
- I use Claude for synthesis and cross-linking (high-value work)
- Result: Faster wiki growth + better token efficiency

**This is the hybrid workflow:**
- Quick page capture: Gemini CLI
- Source ingestion: Me (Claude)
- Cross-linking & synthesis: Me (Claude)
- Curation & direction: You

3. **Your custom skills work automatically**:
   - `Ingest: raw/inbox/[filename]` — Process and add to wiki
   - `/wiki-weekly` — Generate synthesis (also auto-runs Sunday 7:19 PM)
   - `/wiki-explore [Topic]` — Deep dive on a topic
   - `/wiki-synthesis [question]` — Cross-topic analysis
   - `/wiki-audit` — Check wiki health
   - `/wiki-compare [Topic]` — Show source agreement/disagreement
   - `/wiki-tag domain: tech` — Filter pages

4. **I always consider your token strategy**:
   - News articles: Short synthesis (~300 tokens)
   - Research papers: Detailed analysis (~5k tokens)
   - Reports: Adaptive (depends on type)
   - You can override: `"...quick summary"` or `"...detailed analysis"`

5. **Automation runs without prompting**:
   - Every Sunday 7:19 PM: Auto-generate weekly synthesis
   - Every ingest: Auto-create git commit
   - Every ingest: Flag contradictions
   - Every ingest: Suggest related topics

---

## Your Domain Context

Your wiki focuses on:
- **Primary (⭐)**: Technology & Engineering
  - AI/ML, distributed systems, web architecture, dev tools, research
  - Example topics: Large Language Models, Transformers, System Design
  
- **Secondary**: Business & Entrepreneurship
  - Startup lessons, fundraising, market analysis, founder wisdom
  - Example topics: Startup Metrics, Fundraising Strategy
  
- **Interest**: Science, Research & Philosophy
  - Research papers, philosophical concepts, cognitive science, alignment
  - Example topics: AI Alignment, Philosophy of Mind

**Cross-domain synthesis is where your wiki gets strongest.**

---

## How to Use Going Forward

### Option 1: Simple Ingest
```
Ingest: https://example.com/article-about-transformers
```
I'll process it with adaptive synthesis.

### Option 2: Specify Depth
```
Ingest: [article text] — quick summary
Ingest: [paper PDF] — detailed analysis
```

### Option 3: Use a Custom Skill
```
/wiki-explore [[Large Language Models]]
/wiki-synthesis: How do my sources view the future of AI?
/wiki-audit
/wiki-weekly for 2026-04-07 to 2026-04-13
```

### Option 4: Ask a Question
```
"What does my wiki say about [topic]?"
"Compare what my sources say about [concept]"
"What contradictions exist in my understanding?"
```

---

## What I'll Track

Every ingest, I'll maintain:

- **[[log.md]]** — Complete audit trail
- **[[CONTRADICTIONS.md]]** — All flagged conflicts
- **Git history** — Every source tracked
- **Cross-references** — All pages linked
- **Token efficiency** — Short ingests save tokens, deep ingests worth it
- **Weekly syntheses** — `_wiki/syntheses/Weekly-[YYYY-WXX].md`
- **Suggestions** — Topic bridges, knowledge gaps, follow-up sources

---

## Files You Might Reference

- **Quick questions?** → docs/QUICK-REF.md
- **How do I ingest?** → docs/guide.md
- **System rules?** → CLAUDE.md
- **Custom commands?** → commands.md
- **Version control?** → docs/.git-workflow.md
- **Current status?** → docs/index.md (auto-updated)
- **All sources?** → log.md (audit trail)
- **Contradictions?** → docs/CONTRADICTIONS.md

---

## You Don't Need to Repeat

From your setup:
- ✅ I know your domains (tech primary, business, science/philosophy)
- ✅ I know your token strategy (adaptive)
- ✅ I know your ingest methods (all of them)
- ✅ I know you want weekly synthesis (Sundays 7:19 PM)
- ✅ I know you want git commits (auto-commit each ingest)
- ✅ I know your custom skills (7 commands)

**You never need to explain these again.** Unless something changes, I'll follow this setup.

---

## If Something Changes

**If you want to modify the system:**
```
"Change my domain focus to also include [new domain]"
"Switch to manual weekly synthesis (stop auto-running)"
"Change the token strategy to [something else]"
```

I'll update the system and remember going forward.

---

## The Promise

From this point forward:

✅ **Save to raw/inbox/** — Default entry point  
✅ **Tell me: "Ingest: raw/inbox/[file]"** — I handle everything  
✅ **Every ingest follows your schema** — No duplication  
✅ **Everything is linked** — No orphaned pages  
✅ **Contradictions are flagged** — No hidden conflicts  
✅ **Git tracks everything** — Full audit trail  
✅ **Files archive to raw/processed/** — Keep inbox clean  
✅ **Weekly synthesis auto-runs** — One less thing to manage  
✅ **Your setup is remembered** — No repeating yourself  
✅ **Knowledge compounds** — Each source makes your wiki smarter  

---

## The Workflow Is Simple

```
raw/inbox/ → Claude → _wiki/ (synthesized)
                   → raw/processed/ (archived)
```

That's it. That's your system.

---

## Ready?

1. Find a source
2. Save to: `raw/inbox/[domain]-[title]-[date].md`
3. Tell me: `"Ingest: raw/inbox/[filename]"`
4. Watch your second brain grow

---

**Your LLM Wiki is live. Let's build something great.**

🧠 → 📚 → 🔗 → 💡

---

*This file documents your system setup as of 2026-04-09*  
*Update it if the setup changes*
