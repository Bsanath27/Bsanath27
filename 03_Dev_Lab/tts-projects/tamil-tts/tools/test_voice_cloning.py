import os
import torch
import scipy.io.wavfile
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts, XttsArgs, XttsAudioConfig
from TTS.config.shared_configs import BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig

# PyTorch 2.6 security fixes
try:
    torch.serialization.add_safe_globals([
        XttsConfig, XttsArgs, XttsAudioConfig, 
        BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig
    ])
except AttributeError:
    pass

def test_cloning():
    root_path = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts"
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    output_dir = os.path.join(root_path, "samples_tested")
    os.makedirs(output_dir, exist_ok=True)
    
    # Tamil speaker reference
    speaker_wav = os.path.join(root_path, "dataset/taf_04125/wavs/taf_04125_00033069500.wav")

    print("Loading base model...")
    config = XttsConfig()
    config.load_json(os.path.join(xtts_checkpoint, "config.json"))
    
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=xtts_checkpoint, eval=True)
    
    # Load the smoke trained weights into GPT
    smoke_checkpoint = os.path.join(root_path, "training/smoke/smoke_gpt_model.pth")
    if os.path.exists(smoke_checkpoint):
        print(f"Loading smoke trained weights from {smoke_checkpoint}...")
        model.gpt.load_state_dict(torch.load(smoke_checkpoint, map_location="cpu"), strict=False)
    else:
        print("WARN: Smoke checkpoint not found, using base model weights.")

    device = "mps" if torch.backends.mps.is_available() else "cpu"
    print(f"Using device: {device}")
    model.to(device)

    # Use a supported language (English) to test if the speaker's identity is cloned
    sentences = [
        "This is a test of the Tamil speaker voice cloning in English.",
        "The audio pipeline is now verified and operational."
    ]

    print(f"Generating {len(sentences)} test samples in English using Tamil speaker voice...")
    for i, text in enumerate(sentences):
        try:
            outputs = model.synthesize(
                text,
                config,
                speaker_wav=speaker_wav,
                language="en"
            )
            out_path = os.path.join(output_dir, f"test_voice_clone_{i+1}.wav")
            scipy.io.wavfile.write(out_path, 24000, outputs['wav'])
            print(f"Saved: {out_path}")
        except Exception as e:
            print(f"Failed for '{text}': {e}")

if __name__ == "__main__":
    test_cloning()
