import os
import torch
import torch.nn as nn
from torch.utils.data import Dataset, DataLoader
from tqdm import tqdm
import json
import librosa
import numpy as np
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts
import warnings
import pandas as pd
import traceback
from tools.config import (
    ROOT_PATH, DATA_DIR, VOCAB_PATH, DEFAULT_XTTS_CHECKPOINT,
    DVAE_PATH, MEL_NORMS_PATH, PHASE2_DIR, PHASE1_WEIGHTS,
    DEVICE, LANGUAGE_PROXY, get_path
)

warnings.filterwarnings("ignore", category=UserWarning)

class XTTSDataset(Dataset):
    def __init__(self, samples, model, config):
        self.samples = samples
        self.model = model
        self.config = config
        
    def __len__(self):
        return len(self.samples)
        
    def __getitem__(self, idx):
        sample = self.samples[idx]
        audio_path = sample["audio_file"]
        text = sample["text"]
        
        # Base dir for audio is ROOT/data
        full_audio_path = os.path.join(DATA_DIR, audio_path)
        
        audio, _ = librosa.load(full_audio_path, sr=24000)
        audio = torch.FloatTensor(audio).unsqueeze(0)
        
        # Tokenize Text using proxy language
        text_tokens = torch.LongTensor(self.model.tokenizer.encode(text, lang=LANGUAGE_PROXY))
        
        return {
            "audio": audio,
            "text_tokens": text_tokens,
            "text": text,
            "audio_path": audio_path
        }

def collate_fn(batch):
    # Padding for DataLoader
    audio = [b["audio"] for b in batch]
    text_tokens = [b["text_tokens"] for b in batch]
    
    audio_lengths = torch.LongTensor([a.shape[1] for a in audio])
    text_lengths = torch.LongTensor([t.shape[0] for t in text_tokens])
    
    max_audio_len = max(audio_lengths)
    max_text_len = max(text_lengths)
    
    padded_audio = torch.stack([torch.nn.functional.pad(a, (0, max_audio_len - a.shape[1])) for a in audio]).squeeze(1)
    padded_text = torch.stack([torch.nn.functional.pad(t, (0, max_text_len - t.shape[0])) for t in text_tokens])
    
    return {
        "audio": padded_audio,
        "audio_lengths": audio_lengths,
        "text_tokens": padded_text,
        "text_lengths": text_lengths,
        "audio_paths": [b["audio_path"] for b in batch]
    }

def train_manual():
    output_path = PHASE2_DIR
    os.makedirs(output_path, exist_ok=True)

    # 1. Load Config & Extend
    config = XttsConfig()
    config.load_json(os.path.join(DEFAULT_XTTS_CHECKPOINT, "config.json"))
    config.model_args.tokenizer_file = VOCAB_PATH
    
    with open(VOCAB_PATH, 'r') as f:
        new_vocab_size = len(json.load(f)["model"]["vocab"])
    config.model_args.gpt_number_text_tokens = new_vocab_size

    # 2. Init Model
    print("Initializing Model...")
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=DEFAULT_XTTS_CHECKPOINT, eval=False)
    
    print(f"Using device: {DEVICE}")
    model.to(DEVICE)

    # 3. Load Multi-Speaker Samples
    print("Loading multi-speaker samples...")
    metadata_path = os.path.join(DATA_DIR, "multispeaker_metadata.csv")
    df = pd.read_csv(metadata_path, sep="|", header=None, names=["audio_file", "text", "speaker_name"])
    
    samples = df.to_dict('records')
    
    train_dataset = XTTSDataset(samples, model, config)
    print(f"Dataset size: {len(train_dataset)}")
    batch_size = 2
    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True, collate_fn=collate_fn, num_workers=0)
    print(f"Loader size: {len(train_loader)}")

    # 4. Optimization
    optimizer = torch.optim.AdamW(model.gpt.parameters(), lr=2e-7, weight_decay=0.01)
    
    # Language ID for Hindi proxy (hi) is required for some parts of GPT forward
    hi_lang_id = 6680 
    print(f"Using language ID for '{LANGUAGE_PROXY}': {hi_lang_id}")

    # Standard XTTS auxiliary components
    from TTS.tts.layers.xtts.dvae import DiscreteVAE
    from TTS.tts.layers.tortoise.arch_utils import TorchMelSpectrogram
    
    print("Loading DVAE...")
    dvae = DiscreteVAE(
        channels=80, normalization=None, positional_dims=1, num_tokens=1024,
        codebook_dim=512, hidden_dim=512, num_resnet_blocks=3, kernel_size=3,
        num_layers=2, use_transposed_convs=False,
    ).to(DEVICE)
    
    dvae_checkpoint = torch.load(DVAE_PATH, map_location=DEVICE)
    dvae.load_state_dict(dvae_checkpoint, strict=False)
    dvae.eval()
    
    torch_mel_spectrogram_dvae = TorchMelSpectrogram(
        mel_norm_file=MEL_NORMS_PATH, sampling_rate=22050
    ).to(DEVICE)
    
    torch_mel_spectrogram_cond = TorchMelSpectrogram(
        filter_length=4096, hop_length=1024, win_length=4096,
        sampling_rate=22050, mel_fmin=0, mel_fmax=8000,
        n_mel_channels=80, mel_norm_file=MEL_NORMS_PATH,
    ).to(DEVICE)

    # Standard XTTS loss weights
    gpt_loss_text_ce_weight = 0.01
    gpt_loss_mel_ce_weight = 1.0
    accumulation_steps = 64 
    
    # 5. Load Weights (Phase 1 or latest Checkpoint)
    start_epoch = 0
    checkpoints = sorted([f for f in os.listdir(output_path) if f.startswith("gpt_epoch_") and f.endswith(".pth")], 
                         key=lambda x: int(x.split("_")[2].split(".")[0]))
    
    if checkpoints:
        latest_ckpt = checkpoints[-1]
        start_epoch = int(latest_ckpt.split("_")[2].split(".")[0])
        print(f"Resuming from Phase 2 checkpoint: {latest_ckpt} (Starting at Epoch {start_epoch + 1})", flush=True)
        model.gpt.load_state_dict(torch.load(os.path.join(output_path, latest_ckpt), map_location=DEVICE))
    elif os.path.exists(PHASE1_WEIGHTS):
        print(f"Initializing Phase 2 with Phase 1 weights: {PHASE1_WEIGHTS}", flush=True)
        model.gpt.load_state_dict(torch.load(PHASE1_WEIGHTS, map_location=DEVICE))
    else:
        print("Starting training from base model...", flush=True)

    print(f"Starting Training for {30 - start_epoch} remaining epochs...")
    model.train()
    
    # 6. Training Loop with Resource Cleanup Fix
    log_path = os.path.join(output_path, "manual_log.txt")
    with open(log_path, "a") as log_file:
        for epoch in range(start_epoch, 30):
            epoch_loss = 0
            optimizer.zero_grad()
            pbar = tqdm(train_loader, desc=f"Epoch {epoch+1}")
            
            for i, batch in enumerate(pbar):
                try:
                    text_input = batch["text_tokens"].to(DEVICE)
                    audio_input = batch["audio"].to(DEVICE)
                    
                    # 1. Get discrete audio codes
                    with torch.no_grad():
                        mel_spec = torch_mel_spectrogram_dvae(audio_input)
                        audio_codes = dvae.get_codebook_indices(mel_spec)
                    
                    # 2. Fix Conditioning Bug: 
                    # Use all audio in batch for conditioning, ensuring per-sample style match
                    with torch.no_grad():
                        cond_mels = torch_mel_spectrogram_cond(audio_input)
                        cond_latents = model.gpt.get_style_emb(cond_mels).transpose(1, 2)
                    
                    if i == 0:
                        print(f"DEBUG Batch 0 - audio_input: {audio_input.shape}, cond_latents: {cond_latents.shape}")

                    # 3. GPT Forward
                    loss_text, loss_mel, _ = model.gpt(
                        text_input,
                        batch["text_lengths"].to(DEVICE),
                        audio_codes,
                        batch["audio_lengths"].to(DEVICE),
                        cond_mels,
                        None,
                        None,
                        cond_latents
                    )
                    
                    # 4. Weighted Loss
                    loss = (loss_text * gpt_loss_text_ce_weight) + (loss_mel * gpt_loss_mel_ce_weight)
                    loss = loss / accumulation_steps
                    
                    if torch.isnan(loss):
                        print(f"NaN loss at batch {i}")
                        continue
                        
                    loss.backward()
                    
                    if (i + 1) % accumulation_steps == 0 or (i + 1) == len(train_loader):
                        optimizer.step()
                        optimizer.zero_grad()
                        if DEVICE == "mps":
                            torch.mps.empty_cache()
                    
                    epoch_loss += loss.item() * accumulation_steps
                    pbar.set_postfix({"loss": (loss.item() * accumulation_steps)})
                    
                except Exception as e:
                    err_msg = f"Error in batch {i}: {e}\n{traceback.format_exc()}\n"
                    print(err_msg, flush=True)
                    log_file.write(err_msg)
                    log_file.flush()
                    continue
                    
            avg_loss = epoch_loss / max(1, len(train_loader))
            msg = f"Epoch {epoch+1} - Avg Loss: {avg_loss}\n"
            print(msg, flush=True)
            log_file.write(msg)
            log_file.flush()
            
            # Save and Rotate Checkpoints
            ckpt_path = os.path.join(output_path, f"gpt_epoch_{epoch+1}.pth")
            torch.save(model.gpt.state_dict(), ckpt_path)
            
            checkpoints = sorted([f for f in os.listdir(output_path) if f.startswith("gpt_epoch_") and f.endswith(".pth")], 
                                 key=lambda x: int(x.split("_")[2].split(".")[0]))
            if len(checkpoints) > 2:
                for old_ckpt in checkpoints[:-2]:
                    try:
                        os.remove(os.path.join(output_path, old_ckpt))
                    except OSError as remove_err:
                        print(f"Warning: Could not remove old checkpoint {old_ckpt}: {remove_err}")

    # Final save
    final_ckpt_path = os.path.join(output_path, "gpt_final.pth")
    torch.save(model.gpt.state_dict(), final_ckpt_path)
    print(f"Saved final checkpoint: {final_ckpt_path}")

if __name__ == "__main__":
    train_manual()
