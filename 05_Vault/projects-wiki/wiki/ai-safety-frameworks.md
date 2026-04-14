# AI Safety Frameworks

**Summary**: Authoritative frameworks for evaluating, classifying, and managing AI risk — covering capability assessment, safety indices, international standards, and regulatory alignment. Practical reference for building safety-aware AI systems.

**Sources**: [[raw/ai-safety-frameworks/README|ai-safety-frameworks/README]], FLI AI Safety Index 2025, DeepMind Frontier Safety Framework, ISO 42001, Anthropic RSP & Agent Capabilities

**Last updated**: 2026-04-14

---

## Introduction

AI safety frameworks are formal structures used by researchers, regulators, and AI companies to assess, classify, and mitigate risk from advanced AI systems. The field has matured significantly in 2024–2026, with multiple converging standards across government, industry, and academia.

Four sources were kept from a filtered set of 8 (filtered criteria: published within 24 months, substantive technical depth):

| Framework | Type | Published | Relevance |
|-----------|------|-----------|-----------|
| FLI AI Safety Index 2025 | Academic Report | June 2025 | 5/5 |
| DeepMind Frontier Safety Framework | Technical White Paper | Sept 2025 | 5/5 |
| ISO 42001 Compliance Guide | Published Standard | Jan 2026 | 4/5 |
| Anthropic RSP & Agent Capabilities | Academic Paper | Nov 2024 | 4/5 |

---

## Explanation

### 1. FLI AI Safety Index 2025
**What it is**: The Future of Life Institute's annual evaluation of AI safety practices across 7 major AI companies (OpenAI, Anthropic, DeepMind, Meta AI, xAI, Mistral, Apple).

**Key dimensions evaluated**:
- Risk assessment processes
- Capability evaluations before deployment
- Red-teaming and adversarial testing
- Incident response procedures
- Government / external oversight cooperation

**Why it matters**: First systematic cross-company comparison of safety practices. Exposes gaps between stated policies and actual deployment behavior.

---

### 2. DeepMind Frontier Safety Framework (Sept 2025)
**What it is**: Google DeepMind's internal framework for "critical capability assessment" — defining thresholds at which models require escalating safety measures.

**Key concepts**:
- **Critical capabilities**: autonomy, self-replication, persuasion, cyberoffense, CBRN uplift
- **Safety levels**: similar to biosafety level model (SL1–SL4)
- **Deployment gates**: each safety level unlocks only after passing evaluations
- **External audits**: commitment to third-party verification before SL3+ deployment

**Why it matters**: Most operationalized framework published publicly. Shows how to translate abstract safety concerns into testable capability thresholds.

---

### 3. ISO 42001 — AI Management Systems Standard (Jan 2026)
**What it is**: The only current international standard for AI management systems. Published by ISO/IEC.

**Key requirements**:
- Risk-based approach to AI system lifecycle
- Documented AI impact assessments
- Traceability and explainability requirements
- Supplier and third-party AI governance
- Continuous improvement obligations

**Why it matters**: Will likely become a compliance baseline for enterprise AI deployment, similar to ISO 27001 for information security. Early adoption creates competitive advantage.

---

### 4. Anthropic RSP + Agent Capabilities (Nov 2024)
**What it is**: Anthropic's Responsible Scaling Policy (RSP) paired with Berkeley CS294/194 course material on measuring agent capabilities.

**Key concepts from RSP**:
- **AI Safety Levels (ASL)**: ASL-1 to ASL-4, each triggering mandatory safety interventions
- **Capability evaluations**: uplift testing for CBRN, cyberoffense, autonomous replication
- **Commitment to pause**: at ASL-3, deployment pauses until safety measures are verified

**Key concepts from agent capabilities framework**:
- Capability benchmarks: task complexity, tool use, multi-step reasoning
- Distinction between capability (can do X) and propensity (will do X)
- Evaluations that test worst-case behavior, not average behavior

**Why it matters**: Most concrete published example of operationalizing safety constraints at the model level, not just the deployment level.

---

## Methodology

The research was collected via NotebookLM web research (fast mode, query: "AI safety frameworks"), filtered to: (1) published within 24 months, (2) substantive technical frameworks (not political statements or Wikipedia), (3) representing diverse authoritative types (academic, industry, standards body).

---

## Conclusion

The AI safety framework landscape has converged around four pillars: **capability thresholds** (DeepMind, Anthropic RSP), **cross-company benchmarking** (FLI Index), **compliance standards** (ISO 42001), and **agent-specific evaluation** (RSP + Berkeley). For a builder, the most actionable framework is ISO 42001 (compliance baseline) + Anthropic RSP-style capability gates (safety levels tied to deployment decisions).

## Related pages

- [[sources-index]] — source mapping
- [[_index]] — wiki table of contents
- [[projects/current]] — where safety-aware AI projects are tracked
