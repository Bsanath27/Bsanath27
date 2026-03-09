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

def test_model():
    root_path = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts"
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    smoke_checkpoint = os.path.join(root_path, "training/smoke/smoke_gpt_model.pth")
    output_dir = os.path.join(root_path, "samples_tested")
    os.makedirs(output_dir, exist_ok=True)
    
    # Use an existing audio for speaker cloning reference
    speaker_wav = os.path.join(root_path, "dataset/taf_04125/wavs/taf_04125_00033069500.wav")

    print("Loading base model...")
    config = XttsConfig()
    config.load_json(os.path.join(xtts_checkpoint, "config.json"))
    
    # Force add 'ta' to supported languages if not there
    if "ta" not in config.languages:
        print("Adding 'ta' to supported languages...")
        config.languages.append("ta")

    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=xtts_checkpoint, eval=True)
    
    # Load the smoke trained weights into GPT
    if os.path.exists(smoke_checkpoint):
        print(f"Loading smoke trained weights from {smoke_checkpoint}...")
        # Since we only saved the gpt state_dict
        model.gpt.load_state_dict(torch.load(smoke_checkpoint, map_location="cpu"))
    else:
        print("WARN: Smoke checkpoint not found, using base model weights.")

    device = "mps" if torch.backends.mps.is_available() else "cpu"
    print(f"Using device: {device}")
    model.to(device)

    sentences = [
        "வணக்கம், இது சோதனைக் குரல்.", # Hello, this is a test voice.
        "இந்த மாடல் இப்போது தமிழ்ப் பேசுகிறது." # This model is now speaking Tamil.
    ]

    print(f"Generating {len(sentences)} test samples...")
    for i, text in enumerate(sentences):
        try:
            outputs = model.synthesize(
                text,
                config,
                speaker_wav=speaker_wav,
                language="ta"
            )
            out_path = os.path.join(output_dir, f"test_sample_{i+1}.wav")
            scipy.io.wavfile.write(out_path, 24000, outputs['wav'])
            print(f"Saved: {out_path}")
        except Exception as e:
            print(f"Failed to generate for '{text}': {e}")

if __name__ == "__main__":
    test_model()
