# Your LLM Wiki — Personal Knowledge Base

Welcome to your second brain. This is a persistent, AI-maintained knowledge base that grows smarter with every source you add.

## 🚀 Quick Start

### Your Setup
- **Domains**: Tech/Engineering (primary), Business, Science/Research, Philosophy
- **Strategy**: Adaptive token use (short for news, detailed for research)
- **Entry Point**: raw/inbox/ (default for all knowledge)
- **Automation**: Weekly synthesis, contradiction alerts, topic suggestions

### Your First Ingest

**Method 1: Article/Text (Easiest)**
1. Copy article or paste text
2. Save to: `raw/inbox/[domain]-[title]-date.md`
3. Tell Claude: `"Ingest: raw/inbox/[filename]"`

**Method 2: PDF/Document**
1. Save PDF to: `raw/inbox/[filename].pdf`
2. Tell Claude: `"Ingest: raw/inbox/[filename].pdf"`

**Method 3: Web Clipper**
1. Use Obsidian Web Clipper
2. Save to: `raw/inbox/[filename].md`
3. Tell Claude: `"Ingest: raw/inbox/[filename]"`

**Method 4: Use Gemini (Skip raw/)**
1. Use docs/GEMINI-TEMPLATES.md
2. Generate page with Gemini
3. Save to: `_wiki/topics/[Name].md`
4. Tell Claude: `"Integrate: _wiki/topics/[Name].md"`

### Browse Your Wiki
- **index.md** — Navigation hub (start here)
- **../log.md** — All sources you've ingested
- **guide.md** — Personalized workflows with your domains + examples
- **../CLAUDE.md** — System schema and rules
- **../commands.md** — Your 7 custom commands
- **CONTRADICTIONS.md** — Track conflicting views

### Example Pages (Sample Ingest)
See how the system works:
- **Topics**: [[Real-Time Geospatial Data]], [[WebGL & WebGPU]]
- **Entities**: [[Cesium.js]], [[Mapbox]]
- **Frameworks**: [[Real-Time Visualization Pipeline]]
- **Sources**: [[Sample Ingest - Real-Time Geospatial Data]]

---

## 📚 Core Files

| File | Purpose |
|------|---------|
| [[CLAUDE.md]] | System rules, page schemas, definitions |
| [[log.md]] | Ingest journal (audit trail of all sources) |
| [[index.md]] | Navigation & quick links |
| [[guide.md]] | Practical workflows with examples |
| `_wiki/` | Main wiki folder (organized by type) |
| `raw/` | Raw sources (articles, PDFs, clips) |

## 🗂️ Folder Structure

```
obsidian-vault/
├── CLAUDE.md              ← System rules
├── log.md                 ← Source journal
├── index.md               ← Navigation hub
├── guide.md               ← How-to guide
├── _wiki/
│   ├── topics/            ← Concepts, domains, themes
│   ├── entities/          ← People, orgs, products
│   ├── frameworks/        ← Methods, models, systems
│   └── sources/           ← Source metadata & summaries
└── raw/                   ← Raw clipped content (optional)
```

---

## 🎯 How It Works

### The Core Idea
Unlike traditional search engines or RAG systems, this wiki is **continuously synthesized**:

1. **You curate sources** — Find interesting articles, papers, reports
2. **You ask questions** — Direct the analysis ("How does X relate to Y?")
3. **Claude synthesizes** — Reads sources, creates pages, maintains cross-references
4. **Knowledge compounds** — Each new source strengthens existing understanding

The human's job: curate and ask good questions.  
Claude's job: synthesize, organize, and maintain consistency.

### Example: Multi-Source Synthesis

**You add 3 sources:**
1. Article on flight tracking systems
2. Paper on real-time architecture
3. Case study of Cesium.js implementation

**Claude will:**
- Create pages for each topic
- Link [[Real-Time Tracking]] ← → [[Architecture]]
- Update [[Cesium.js]] with implementation details
- Flag if any sources contradict each other
- Update [[log.md]] with all 3 sources

**The wiki compounds** — not just 3 separate pages, but an interconnected synthesis.

---

## 🔄 Common Workflows

### Ingest a Source
```
"Ingest this for my wiki: [article/URL/file]"
→ Claude creates/updates pages, links everything, updates log.md
```

### Explore a Topic
```
Open [[Topic Name]] → See all sources, related topics, entities, evolution
```

### Synthesis Question
```
"How do [Concept A] and [Concept B] relate? Show me what my sources say."
→ Claude synthesizes across pages, updates understanding
```

### Find Contradictions
```
"Show me contradictions in my wiki"
→ Claude lists all [[Contradiction]] flags, explains conflicts
```

### Compare Sources
```
"Compare what my sources say about [Topic]"
→ Claude shows consensus, disagreements, nuances
```

See **[[guide.md]]** for detailed workflow examples with screenshots/mockups.

---

## ✨ Why This Works

**The Problem with Normal Knowledge Bases:**
- You store raw documents
- You re-search them every time you have a question
- Knowledge doesn't compound
- Contradictions aren't flagged
- Cross-references are manual

**The LLM Wiki Approach:**
- Information is synthesized once, then refined
- Cross-references are automatic
- Contradictions are flagged explicitly
- The wiki gets **richer and faster** with each source
- Maintenance is effortless (Claude does it)

The tedious part of maintaining a knowledge base — updating cross-references, keeping summaries current, noting conflicts — is something LLMs excel at and humans hate. This system lets the AI do what it's good at.

---

## 📋 File Schemas

Every page has a specific schema (defined in [[CLAUDE.md]]):

- **Topic Pages** — Definition, key ideas, applications, related topics, sources, evolution
- **Entity Pages** — Overview, key facts, work/contributions, related entities
- **Framework Pages** — Overview, steps/components, when to use, strengths/weaknesses, examples
- **Source Pages** — Source info, summary, key takeaways, relevant entities/topics, integration notes

Don't worry about these — Claude uses them automatically when creating pages.

---

## 🎓 Learn More

- **System Rules & Schemas** → [[CLAUDE.md]]
- **Practical Workflows** → [[guide.md]]
- **See Example Pages** → [[index.md#Recently Updated]]
- **Track Your Sources** → [[log.md]]

---

## 💡 Pro Tips

### Obsidian Features
- **Graph View** (`Ctrl/Cmd + Shift + G`) — Visualize connections
- **Backlinks** — See what links to current page
- **Search** → Find contradictions with `[[Contradiction]]`
- **Daily Notes** — Pair with daily reflections

### Best Practices
- Ingest high-relevance sources (4-5 score)
- Ask synthesis questions to deepen understanding
- Review contradictions regularly
- Use graph view to spot orphaned pages
- Let Claude do all the maintenance (cross-refs, updating, etc.)

### Don't
- Try to manually update cross-references (Claude handles it)
- Ingest everything (prioritize quality)
- Create duplicate pages (ask Claude to merge instead)
- Ignore contradictions (they're insights!)

---

## 🚀 Your Next Step

**Ready to build your knowledge base?**

1. Find an article, paper, transcript, or interesting link
2. Share it with Claude: `"Ingest this: [source]"`
3. Watch your wiki grow
4. Ask questions. Explore connections. Synthesize across sources.

**The more you add, the smarter it gets.**

---

## 📞 Need Help?

- **How do I...?** → See [[guide.md]]
- **What's the system?** → Read [[CLAUDE.md]]
- **What's been ingested?** → Check [[log.md]]
- **Where do I start?** → Read [[index.md]]

---

**Built for you by Claude. Maintained by Claude. Directed by you.**

Let's build something great.
