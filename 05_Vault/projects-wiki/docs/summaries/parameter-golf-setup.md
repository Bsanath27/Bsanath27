---
type: summary
date: 2026-04-14
project: parameter-golf
tags: [parameter-golf, quantization, transformers, competition, openai]
---

> Bootstrapped the Parameter Golf submission workspace: private docs folder, central config file, full technique analysis, and a synthesized wiki entry — ready to start experimenting.

## Context

OpenAI's Parameter Golf challenge (March 18 – April 30, 2026): train the best LM that fits in 16MB and trains in ≤10 min on 8×H100. Scored by BPB on FineWeb val set. Current SOTA is 1.1147 BPB.

## What Changed

**In `openai/parameter-golf` (local fork):**
- Added `docs/` to `.gitignore` — private scratch space, not committed
- Created `config.py` — single file with every tunable variable (hyperparams, optimizer, quantization, advanced techniques). Has a `to_env_string()` helper to build torchrun launch commands.
- Created `docs/roadmap.md` — 6-phase plan with per-day timeline to submission
- Created `docs/knowledge-required.md` — 9 knowledge domains with learning order
- Created `docs/techniques-analysis.md` — all 22+ techniques from the leaderboard with BPB impact, originating PR, notes
- Created `docs/winning-techniques.md` — tier 1/2/3 breakdown, what to try next, negative results to avoid

**In projects-wiki vault:**
- Created `wiki/parameter-golf.md` — synthesized knowledge page (SOTA stack, quantization frontier, Muon optimizer, submission requirements)
- Updated `projects/current.md` with active project entry
- Updated `wiki/_index.md` and `wiki/_log.md`

## Key Learnings

- **Quantization is the core lever**: Int6 Full GPTQ + LZMA lets you pack ~70M effective params into 16MB vs ~50M with the int8 baseline
- **Sliding window eval is free**: −0.019 BPB with zero training changes — must always be on
- **Techniques interact non-linearly**: TTT helped in one stack but was neutral/negative in the GPTQ stack (25 failed attempts documented in PR #670)
- **The Muon optimizer** is uniquely compatible with quantization (arXiv:2509.23106) — 8-bit Muon achieves parity with FP32 at 62% less optimizer state memory
- **First experiments to run**: `WARMDOWN_ITERS=3500` and `MLP_MULT=3` — both proven, not yet combined in your own fork

## Commands

- `python3 config.py` → print default torchrun env-var string
- `SEED=42 NUM_LAYERS=11 MLP_MULT=3 WARMDOWN_ITERS=3500 torchrun --standalone --nproc_per_node=1 train_gpt.py` → first experiment baseline

## See Also

- [[wiki/parameter-golf]] — full wiki knowledge page
- [[projects/current]] — active project status
- [[docs/skills/firecrawl]] — firecrawl used for research (RunPod article, arXiv:2509.23106)
- [[commands]] — command reference
