import os
import torch
import torch.nn as nn
from torch.utils.data import Dataset, DataLoader
from tqdm import tqdm
import json
import librosa
import numpy as np
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts, XttsArgs, XttsAudioConfig
from TTS.config.shared_configs import BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig
from TTS.tts.datasets import load_tts_samples
import warnings
warnings.filterwarnings("ignore", category=UserWarning)

# PyTorch 2.6 security fixes
try:
    torch.serialization.add_safe_globals([
        XttsConfig, XttsArgs, XttsAudioConfig, 
        BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig
    ])
except Exception:
    pass

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
        
        # 1. Load Audio
        # Base dir for audio is /Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts/data
        base_data_dir = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts/data"
        full_audio_path = os.path.join(base_data_dir, audio_path)
        
        audio, _ = librosa.load(full_audio_path, sr=24000)
        audio = torch.FloatTensor(audio).unsqueeze(0)
        
        # 2. Tokenize Text
        # We use 'hi' (Hindi) as a proxy language for the tokenizer preprocessor 
        # since it handles UTF-8 correctly and 'ta' is not natively supported.
        text_tokens = torch.LongTensor(self.model.tokenizer.encode(text, lang="hi"))
        
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
    
    # Simple zero padding
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
    root_path = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts"
    DATA_DIR = os.path.join(root_path, "data")
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    assets_path = os.path.join(root_path, "training/model_assets")
    output_path = os.path.join(root_path, "training/multispeaker_phase2")
    os.makedirs(output_path, exist_ok=True)

    # 1. Load Config & Extend (Already extended vocab exists)
    new_vocab_path = os.path.join(assets_path, "vocab.json")
    config = XttsConfig()
    config.load_json(os.path.join(xtts_checkpoint, "config.json"))
    config.model_args.tokenizer_file = new_vocab_path
    
    with open(new_vocab_path, 'r') as f:
        new_vocab_size = len(json.load(f)["model"]["vocab"])
    config.model_args.gpt_number_text_tokens = new_vocab_size

    # 2. Init Model
    print("Initializing Model...")
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=xtts_checkpoint, eval=False)
    
    device = "mps" if torch.backends.mps.is_available() else "cpu"
    print(f"Using device: {device}")
    model.to(device)

    # 3. Load Multi-Speaker Samples
    print("Loading multi-speaker samples...")
    import pandas as pd
    metadata_path = os.path.join(DATA_DIR, "multispeaker_metadata.csv")
    df = pd.read_csv(metadata_path, sep="|", header=None, names=["audio_file", "text", "speaker_name"])
    
    # Convert to list of dicts for XTTSDataset
    samples = df.to_dict('records')
    
    train_dataset = XTTSDataset(samples, model, config)
    print(f"Dataset size: {len(train_dataset)}")
    batch_size = 2 # Increased batch size for more data and stability
    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True, collate_fn=collate_fn, num_workers=0)
    print(f"Loader size: {len(train_loader)}")

    # 4. Optimization
    # We only fine-tune the GPT part for XTTS
    optimizer = torch.optim.AdamW(model.gpt.parameters(), lr=2e-7, weight_decay=0.01)
    
    # Get language ID for 'hi' (Hindi proxy) - Verified as 6680 from vocab.json
    hi_lang_id = 6680
    print(f"Using hardcoded language ID for 'hi': {hi_lang_id}")

    print(f"Starting Manual High-Fidelity Training for 250 epochs...")
    model.train()
    
    # Init DVAE for code extraction (required for XTTS fine-tuning)
    from TTS.tts.layers.xtts.dvae import DiscreteVAE
    from TTS.tts.layers.tortoise.arch_utils import TorchMelSpectrogram
    
    print("Loading DVAE...")
    dvae = DiscreteVAE(
        channels=80,
        normalization=None,
        positional_dims=1,
        num_tokens=1024,
        codebook_dim=512,
        hidden_dim=512,
        num_resnet_blocks=3,
        kernel_size=3,
        num_layers=2,
        use_transposed_convs=False,
    ).to(device)
    
    dvae_checkpoint_path = os.path.join(root_path, "training/model_assets/dvae.pth")
    mel_norm_file = os.path.join(root_path, "training/model_assets/mel_norms.pth")
    
    dvae_checkpoint = torch.load(dvae_checkpoint_path, map_location=device)
    dvae.load_state_dict(dvae_checkpoint, strict=False)
    dvae.eval()
    
    torch_mel_spectrogram_dvae = TorchMelSpectrogram(
        mel_norm_file=mel_norm_file, 
        sampling_rate=22050
    ).to(device)
    
    # Conditioning mel extractor (XTTS v2 uses 4096 window)
    torch_mel_spectrogram_cond = TorchMelSpectrogram(
        filter_length=4096,
        hop_length=1024,
        win_length=4096,
        sampling_rate=22050,
        mel_fmin=0,
        mel_fmax=8000,
        n_mel_channels=80,
        mel_norm_file=mel_norm_file,
    ).to(device)

    log_file = open(os.path.join(output_path, "manual_log.txt"), "w")
    
    # Standard XTTS loss weights
    gpt_loss_text_ce_weight = 0.01
    gpt_loss_mel_ce_weight = 1.0
    
    accumulation_steps = 64 
    
    # 5. Load PRETRAINED Weights from Phase 1 if starting new
    phase1_weights = os.path.join(root_path, "training/full_run/gpt_final.pth")
    
    start_epoch = 0
    checkpoints = sorted([f for f in os.listdir(output_path) if f.startswith("gpt_epoch_") and f.endswith(".pth")], 
                         key=lambda x: int(x.split("_")[2].split(".")[0]))
    
    if checkpoints:
        latest_ckpt = checkpoints[-1]
        start_epoch = int(latest_ckpt.split("_")[2].split(".")[0])
        print(f"Resuming from Phase 2 checkpoint: {latest_ckpt} (Starting at Epoch {start_epoch + 1})", flush=True)
        model.gpt.load_state_dict(torch.load(os.path.join(output_path, latest_ckpt), map_location=device))
    elif os.path.exists(phase1_weights):
        print(f"Initializing Phase 2 with Phase 1 (30-epoch) weights: {phase1_weights}", flush=True)
        model.gpt.load_state_dict(torch.load(phase1_weights, map_location=device))
    else:
        print("No fine-tuned weights found. Starting training from base model...", flush=True)

    for epoch in range(start_epoch, 30):
        epoch_loss = 0
        optimizer.zero_grad()
        pbar = tqdm(train_loader, desc=f"Epoch {epoch+1}")
        for i, batch in enumerate(pbar):
            
            try:
                text_input = batch["text_tokens"].to(device)
                audio_input = batch["audio"].to(device)
                
                # 1. Get discrete audio codes using DVAE
                with torch.no_grad():
                    # Resample to 22050 if needed (our dataset is already 22050)
                    mel_spec = torch_mel_spectrogram_dvae(audio_input)
                    audio_codes = dvae.get_codebook_indices(mel_spec) # (B, T_codes)
                
                # 2. Prepare conditioning (using first audio in batch)
                ref_audio = audio_input[0:1]
                # XTTS v2 conditioning expects (B, 1, 80, T) or (B, 80, T)
                with torch.no_grad():
                    cond_mels = torch_mel_spectrogram_cond(ref_audio)
                    # Broadcast to match batch size
                    cond_mels = cond_mels.repeat(audio_input.shape[0], 1, 1, 1)
                    
                # Manually compute cond_latents to verify shape
                with torch.no_grad():
                    cond_latents = model.gpt.get_style_emb(cond_mels).transpose(1, 2)
                
                if i == 0:
                    print(f"DEBUG Batch 0 shapes:", flush=True)
                    print(f"  text_input: {text_input.shape}", flush=True)
                    print(f"  audio_input: {audio_input.shape}", flush=True)
                    print(f"  audio_codes: {audio_codes.shape}", flush=True)
                    print(f"  cond_mels: {cond_mels.shape}", flush=True)
                    print(f"  cond_latents: {cond_latents.shape}", flush=True)

                # 3. GPT Forward Pass (returns losses)
                # Pass arguments positionally to absolute adherence to signature:
                # (text_inputs, text_lengths, audio_codes, wav_lengths, cond_mels, cond_idxs, cond_lens, cond_latents, ...)
                loss_text, loss_mel, _ = model.gpt(
                    text_input,                                                    # text_inputs
                    batch["text_lengths"].to(device),                              # text_lengths
                    audio_codes,                                                   # audio_codes
                    batch["audio_lengths"].to(device),                             # wav_lengths
                    cond_mels,                                                     # cond_mels (5th)
                    None,                                                          # cond_idxs (6th)
                    None,                                                          # cond_lens (7th)
                    cond_latents                                                   # cond_latents (8th)
                )
                
                # 4. Weighted Loss
                loss = (loss_text * gpt_loss_text_ce_weight) + (loss_mel * gpt_loss_mel_ce_weight)
                loss = loss / accumulation_steps # Scale loss for accumulation
                
                if torch.isnan(loss):
                    print(f"NaN loss at batch {i}", flush=True)
                    continue
                    
                loss.backward()
                
                if (i + 1) % accumulation_steps == 0 or (i + 1) == len(train_loader):
                    optimizer.step()
                    optimizer.zero_grad()
                    if device == "mps":
                        torch.mps.empty_cache()
                
                epoch_loss += loss.item() * accumulation_steps
                pbar.set_postfix({"loss": (loss.item() * accumulation_steps)})
                
            except Exception as e:
                import traceback
                err_msg = f"Error in batch {i}: {e}\n{traceback.format_exc()}\n"
                print(err_msg, flush=True)
                log_file.write(err_msg)
                log_file.flush()
                continue
                
        if len(train_loader) == 0:
            avg_loss = 0
            print("Warning: train_loader is empty!", flush=True)
        else:
            avg_loss = epoch_loss / len(train_loader)
        
        msg = f"Epoch {epoch+1} - Avg Loss: {avg_loss}\n"
        print(msg, flush=True)
        log_file.write(msg)
        log_file.flush()
        
        # Save checkpoint every epoch
        ckpt_path = os.path.join(output_path, f"gpt_epoch_{epoch+1}.pth")
        torch.save(model.gpt.state_dict(), ckpt_path)
        print(f"Saved checkpoint: {ckpt_path}", flush=True)
        
        # Keep only the last 2 checkpoints to save disk space while ensuring safety (1.6GB each)
        checkpoints = sorted([f for f in os.listdir(output_path) if f.startswith("gpt_epoch_") and f.endswith(".pth")], 
                             key=lambda x: int(x.split("_")[2].split(".")[0]))
        if len(checkpoints) > 2:
            for old_ckpt in checkpoints[:-2]: # Keep last two
                old_path = os.path.join(output_path, old_ckpt)
                try:
                    os.remove(old_path)
                    print(f"Removed old checkpoint to free space: {old_ckpt}", flush=True)
                except:
                    pass

    # Final save
    final_ckpt_path = os.path.join(output_path, "gpt_final.pth")
    torch.save(model.gpt.state_dict(), final_ckpt_path)
    print(f"Saved final checkpoint: {final_ckpt_path}", flush=True)
    
    log_file.close()

if __name__ == "__main__":
    train_manual()
