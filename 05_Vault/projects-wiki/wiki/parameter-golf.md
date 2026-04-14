# Parameter Golf — OpenAI Model Craft Challenge

**Summary**: OpenAI's competition to train the best language model that fits in 16MB and trains in under 10 minutes on 8×H100 GPUs, scored by bits-per-byte (BPB) on the FineWeb validation set. Reveals the frontier of parameter-constrained ML: quantization, architecture search, and compression as competitive disciplines.

**Sources**: [[raw/parameter-golf/roadmap|roadmap]], [[raw/parameter-golf/knowledge-required|knowledge-required]], [[raw/parameter-golf/techniques-analysis|techniques-analysis]], [[raw/parameter-golf/winning-techniques|winning-techniques]], openai/parameter-golf GitHub, RunPod blog (2026-03-25), arXiv:2509.23106

**Last updated**: 2026-04-14

---

## Introduction

Parameter Golf is OpenAI's L(N) optimization challenge: minimize validation loss given a fixed parameter budget (~50–70M effective params), unconstrained by data, compute steps, or architecture. The challenge runs **March 18 – April 30, 2026**, with $1M in compute grants from OpenAI.

**The metric**: Bits-per-byte (BPB) on the FineWeb validation set (first 50k documents). BPB is tokenizer-agnostic — it normalizes for vocabulary size, so you can't game it by shrinking your vocab.

**The constraint**: Artifact ≤ 16,000,000 bytes (decimal), measured as `code_bytes + lzma(model_bytes)`. No external downloads or network calls during evaluation.

---

## Scoring Formula

```
artifact_bytes = len(train_gpt.py source) + len(compressed_model_bytes)
BPB = cross_entropy_bits × (tokens / bytes_in_validation_set)
```

BPB collapses tokenizer differences — a model with a tiny vocab and one with a large vocab compete on equal footing.

---

## Winning Techniques (as of 2026-04-14)

### Current SOTA: 1.1147 BPB (abaybektursun, 2026-03-25)

The top entry stacks 11 validated techniques. No single trick — it's iterative compounding:

| Technique | BPB Gain | Category |
|-----------|---------|----------|
| Full Hessian GPTQ (AR self-gen calib) | −0.008 | Quantization |
| XSA on all 11 layers | −0.005 | Architecture |
| BigramHash 3072 × dim=112 | −0.005 | Embedding |
| Sliding Window Eval (stride=64) | −0.019 | Evaluation |
| MLP 3× expansion (1536) + Int6 QAT | −0.008 | Arch + Quant |
| EMA(0.997) + SWA(every 50) | −0.003 | Optimizer |
| Parallel Muon + warmdown 4000 | −0.003 | Optimizer |
| Partial RoPE (16/64 dims) | −0.002 | Positional |
| LeakyReLU² activation | −0.002 | Activation |
| LZMA preset=9 (vs zlib) | −0.001 | Compression |
| SmearGate, U-Net skips, LN scale | −0.002 | Architecture |

### Leaderboard Progression

```
Naive Baseline:  1.2244 BPB  (March 18)
SOTA today:      1.1147 BPB  (March 25)
Delta in 7 days: −0.1097 BPB
```

---

## Architecture: The Converged Stack

After 5+ weeks of community iteration, the winning architecture has converged to:

| Component | Value | Notes |
|-----------|-------|-------|
| Layers | 11 | (baseline was 9) |
| Model dim | 512 | Unchanged |
| MLP hidden | 1536 (3×) | Up from 1024 (2×) |
| Heads | 8Q / 4KV (GQA) | Unchanged |
| Activation | LeakyReLU(0.5)² | Replaced GELU |
| Quantization | Int6 Full GPTQ | Up from int8 |
| Compression | LZMA preset=9 | Up from zlib |
| Embeddings | FP16 tied | Saves ~10% budget |
| Positional | Partial RoPE (16/64) | Partial helps |
| Evaluation | Sliding window, stride=64 | Key free gain |

---

## The Quantization Frontier

Quantization is the **highest-leverage dimension** because it directly controls how many parameters you can fit:

| Scheme | Effective params | SOTA BPB |
|--------|----------------|---------|
| Int8 + zlib | ~50M | 1.22 |
| Int6 STE QAT + zstd | ~65M | 1.16 |
| Int6 Full GPTQ + LZMA | ~70M | 1.115 (current SOTA) |
| Ternary (1.57 bits) + 2h training | ~100M | 1.123 |
| Binary (1 bit) + 2h training | ~200M | 1.124 |

Binary/ternary quantization shows that with enough parameters, lower precision can win — the barrier is fitting the training within 10 minutes.

---

## Open Frontiers (Unexplored)

From the README's "Requests for PRs" — no winning implementation yet:

| Technique | Why promising |
|-----------|--------------|
| JEPA (Joint Embedding Predictive Architecture) | Self-supervised learning without next-token prediction |
| Text diffusion | Masked-predict instead of autoregressive |
| H-net tokenization | Hierarchical subword; better compression ratio |
| State-space models (Mamba) | Linear complexity; may help at eval time |
| E2E TTT | Model adapts itself during evaluation |
| Megakernels | Custom CUDA ops to fit 8192 seq_len in 10 min |

---

## The Muon Optimizer (Key Primitive)

All competitive runs use **Muon** for matrix parameter updates:
- Based on Newton-Schulz orthogonalization: `G → G / ‖G‖ → orthogonalized`
- Much faster gradient flow than Adam for matrix-shaped params
- Paper: arXiv:2509.23106 — "Effective Quantization of Muon Optimizer States"
  - 8-bit Muon achieves parity with FP32 Muon
  - 62% reduction in optimizer state footprint
  - Uniquely compatible with simple linear quantization (unlike AdamW)

**Parallel Muon** (PR #399): distributes Newton-Schulz across GPUs, ~10% speedup → more steps in 600s → better BPB.

---

## Submission Requirements

For a record submission (`records/track_10min_16mb/`):
1. Beat current SOTA by ≥ 0.005 BPB
2. Prove statistical significance: p < 0.01 via Welch's t-test (typically 3 seeds)
3. Run in ≤ 10 min on 8×H100 SXM

Required files per submission:
- `README.md` — architecture table, changes, lineage, run command
- `submission.json` — author, val_bpb, seeds, hardware, delta
- `train_seed<N>.log` × 3 — full stdout from each seed
- `train_gpt.py` — must compile and run from the records folder

Non-record submissions (`records/track_non_record_16mb/`): lower bar, accept interesting ideas, negative results, 4h runs.

---

## Methodology: How the Top Team Works

1. Identify a technique from a prior PR or paper
2. Implement on a 1×H100, run WITHOUT 600s cap (`MAX_WALLCLOCK_SECONDS=0`)
3. Compare BPB vs same step count as SOTA (not same wall-clock time)
4. If promising: add to stack, run full 600s on 1×H100
5. If still promising: 3-seed run on 8×H100 SXM with 600s cap
6. Log everything: every failed experiment goes in a "negative results PR"

**Key discipline**: ablate every technique in isolation before stacking. Techniques interact non-linearly (e.g., TTT helps standalone but hurts in the GPTQ stack).

---

## Conclusion

Parameter Golf is the cleanest benchmark for parameter-efficient language model research. It forces creativity at the intersection of quantization, architecture, and systems engineering. The SOTA at 1.1147 BPB was achieved through 11 stacked improvements over 7 days of community effort. The remaining open frontier — binary quantization at 10-minute scale, long context eval, JEPA-style training — will likely require algorithmic breakthroughs, not just engineering.

## Raw Sources

| File | What it contains |
|------|-----------------|
| [[raw/parameter-golf/roadmap\|roadmap.md]] | 6-phase submission plan with per-day timeline, April 30 deadline |
| [[raw/parameter-golf/knowledge-required\|knowledge-required.md]] | 9 knowledge domains: transformers, quantization, Muon, DDP, TTT, compression |
| [[raw/parameter-golf/techniques-analysis\|techniques-analysis.md]] | Every technique from all 22+ leaderboard submissions with BPB impact |
| [[raw/parameter-golf/winning-techniques\|winning-techniques.md]] | Tier analysis, what to try next, negative results to skip |

## Related pages

- [[projects/current]] — active project tracking (Parameter Golf listed here)
- [[docs/summaries/parameter-golf-setup]] — session that built the submission workspace
- [[_metrics]] — velocity dashboard
- [[_index]] — wiki table of contents
