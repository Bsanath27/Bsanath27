import os
import torch
from torch.utils.data import DataLoader
import pandas as pd
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts
import traceback
from tools.config import (
    DATA_DIR, VOCAB_PATH, DEFAULT_XTTS_CHECKPOINT,
    DVAE_PATH, MEL_NORMS_PATH, TRAINING_DIR,
    DEVICE, LANGUAGE_PROXY
)
from tools.train_xtts_full import XTTSDataset, collate_fn

def smoke_test():
    output_path = os.path.join(TRAINING_DIR, "smoke_test")
    os.makedirs(output_path, exist_ok=True)

    print("--- XTTS Pipeline Smoke Test ---")
    
    # 1. Load Config
    config = XttsConfig()
    config.load_json(os.path.join(DEFAULT_XTTS_CHECKPOINT, "config.json"))
    config.model_args.tokenizer_file = VOCAB_PATH
    
    # 2. Init Model
    print(f"Initializing Model on {DEVICE}...")
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=DEFAULT_XTTS_CHECKPOINT, eval=False)
    model.to(DEVICE)

    # 3. Load Minimal Samples (First 4 rows of metadata)
    metadata_path = os.path.join(DATA_DIR, "multispeaker_metadata.csv")
    if not os.path.exists(metadata_path):
        print(f"ERROR: Metadata not found at {metadata_path}")
        return

    df = pd.read_csv(metadata_path, sep="|", header=None, names=["audio_file", "text", "speaker_name"])
    samples = df.head(4).to_dict('records')
    
    train_dataset = XTTSDataset(samples, model, config)
    train_loader = DataLoader(train_dataset, batch_size=2, shuffle=False, collate_fn=collate_fn)

    # 4. Minimal Optimizer & Components
    optimizer = torch.optim.AdamW(model.gpt.parameters(), lr=1e-6)
    
    from TTS.tts.layers.xtts.dvae import DiscreteVAE
    from TTS.tts.layers.tortoise.arch_utils import TorchMelSpectrogram
    
    dvae = DiscreteVAE(
        channels=80, normalization=None, positional_dims=1, num_tokens=1024,
        codebook_dim=512, hidden_dim=512, num_resnet_blocks=3, kernel_size=3,
        num_layers=2, use_transposed_convs=False,
    ).to(DEVICE)
    dvae.load_state_dict(torch.load(DVAE_PATH, map_location=DEVICE), strict=False)
    dvae.eval()
    
    torch_mel_spectrogram_dvae = TorchMelSpectrogram(mel_norm_file=MEL_NORMS_PATH, sampling_rate=22050).to(DEVICE)
    torch_mel_spectrogram_cond = TorchMelSpectrogram(
        filter_length=4096, hop_length=1024, win_length=4096,
        sampling_rate=22050, mel_fmin=0, mel_fmax=8000,
        n_mel_channels=80, mel_norm_file=MEL_NORMS_PATH,
    ).to(DEVICE)

    # 5. Single Real Training Step
    print("Executing single training step...")
    model.train()
    
    for i, batch in enumerate(train_loader):
        if i >= 1: break # Only 1 step for smoke test
        
        try:
            text_input = batch["text_tokens"].to(DEVICE)
            audio_input = batch["audio"].to(DEVICE)
            
            with torch.no_grad():
                mel_spec = torch_mel_spectrogram_dvae(audio_input)
                audio_codes = dvae.get_codebook_indices(mel_spec)
                cond_mels = torch_mel_spectrogram_cond(audio_input)
                cond_latents = model.gpt.get_style_emb(cond_mels).transpose(1, 2)
            
            optimizer.zero_grad()
            loss_text, loss_mel, _ = model.gpt(
                text_input, batch["text_lengths"].to(DEVICE),
                audio_codes, batch["audio_lengths"].to(DEVICE),
                cond_mels, None, None, cond_latents
            )
            
            loss = (loss_text * 0.01) + (loss_mel * 1.0)
            loss.backward()
            optimizer.step()
            
            print(f"Step SUCCESS. Loss: {loss.item():.4f}")
            
        except Exception as e:
            print(f"Step FAILED: {e}")
            print(traceback.format_exc())
            return

    print("Smoke test PASSED.")

if __name__ == "__main__":
    smoke_test()
