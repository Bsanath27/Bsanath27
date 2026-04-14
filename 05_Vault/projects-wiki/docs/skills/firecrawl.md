---
type: skill-guide
plugin: firecrawl (MCP server)
updated: 2026-04-13
tags:
  - skills
  - firecrawl
  - web-scraping
  - mcp
---

# Firecrawl

> Web scraping, search, crawling, and page interaction via MCP server.

**Setup:** MCP server configured in `~/.claude/mcp.json` with API key.

## Commands

| Command | What It Does |
|---------|-------------|
| `/firecrawl-search` | Web search with full page content extraction |
| `/firecrawl-scrape` | Extract clean markdown from any URL (handles JavaScript rendering) |
| `/firecrawl-crawl` | Bulk extract content from an entire website or section |
| `/firecrawl-map` | Discover and list all URLs on a website, with optional search |
| `/firecrawl-agent` | AI-powered autonomous data extraction that navigates complex sites |
| `/firecrawl-interact` | Control a live browser session on any page |
| `/firecrawl-download` | Download entire website as local files (markdown, screenshots) |

## Build Integration Skills

These integrate Firecrawl into your own product code:

| Command | What It Does |
|---------|-------------|
| `/firecrawl-build-scrape` | Integrate `/scrape` endpoint into product code |
| `/firecrawl-build-search` | Integrate `/search` endpoint into product code |
| `/firecrawl-build-interact` | Integrate `/interact` endpoint into product code |
| `/firecrawl-build-onboarding` | Get Firecrawl credentials and SDK setup into a project |

## When to Use

- **scrape** — single page, need clean markdown
- **crawl** — entire site or section, bulk extraction
- **search** — find pages by query, get content
- **map** — sitemap discovery, URL enumeration
- **agent** — complex multi-step navigation (login flows, dynamic content)
- **interact** — manual browser control for debugging or testing

## See Also

- [[commands]] — Quick reference table
- [[defuddle]] — Lighter alternative for simple web page extraction
- [[nlm-search-source]] — Research source discovery workflow
- [[docs/summaries/parameter-golf-setup]] — example of firecrawl used for competition research
