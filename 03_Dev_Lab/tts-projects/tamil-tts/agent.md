# agent.md
## Title
Antigravity TTS Preprocess & XTTS Training Playbook — single-speaker pipeline

## Purpose
Automate converting a single-speaker subset of OpenSLR Tamil into a ready-to-train XTTS dataset, run a smoke-train, and produce artifacts for full training and Hugging Face publishing.

## Assumptions (agent must verify)
- Input: `/data/openSLR_tamil/<speaker_id>/wavs/*.wav` and `/data/openSLR_tamil/<speaker_id>/line_index.txt`
- The dataset is **single-speaker** folder selected by the user (agent must confirm).
- Host has `ffmpeg`, `sox`, `git`, `python3`, `conda` (or equivalent) installed.
- GPU is available (if not, fall back to CPU with warnings).
- User wants target audio format: WAV, mono, 22050 Hz.

## Top-level goals
1. Verify speaker folder and compute stats.
2. Normalize audio (resample, mono, loudness).
3. Remove / quarantine bad clips.
4. Forced-align transcripts (if transcripts present); otherwise generate via Whisper and then align.
5. Segment into short clips (3–12s).
6. Produce `metadata.csv`, `train.txt`, `val.txt`.
7. Run smoke-train (small subset).
8. If smoke-train OK, prepare full training config and start full train OR upload prepared dataset to HF dataset repo for later training.
9. Produce inference artifacts and HF publish package.

## Agent workflow (step-by-step with commands)

### 0. Basic env & utilities
```bash
# create working dirs
WORK=/workspace/tts_project
RAW=$WORK/raw
NORM=$WORK/normalized
WAVS=$NORM/wavs
METADATA=$WORK/metadata
mkdir -p $RAW $WAVS $METADATA

# ensure required tools
command -v ffmpeg || (echo "ffmpeg missing" && exit 1)
command -v git || (echo "git missing" && exit 1)