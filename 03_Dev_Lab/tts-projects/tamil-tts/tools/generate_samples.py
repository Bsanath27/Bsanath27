import os
import torch
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

def generate_samples():
    root_path = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts"
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    output_dir = os.path.join(root_path, "samples")
    os.makedirs(output_dir, exist_ok=True)
    
    # Use an existing audio for speaker cloning reference
    speaker_wav = os.path.join(root_path, "dataset/taf_04125/wavs/taf_04125_00033069500.wav")

    print("Loading model for inference...")
    config = XttsConfig()
    config.load_json(os.path.join(xtts_checkpoint, "config.json"))
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=xtts_checkpoint, eval=True)
    
    device = "mps" if torch.backends.mps.is_available() else "cpu"
    model.to(device)

    sentences = [
        "வாழ்க்கையில் வெற்றி பெற கடின உழைப்பு அவசியம்.",
        "தமிழ் மொழி மிகவும் பழமையான மற்றும் அழகான மொழி.",
        "இந்த ஆடியோ கோப்பு தானாகவே உருவாக்கப்பட்டது.",
        "கல்வியால் மட்டுமே ஒரு சமூகத்தை மாற்ற முடியும்.",
        "இன்று வானிலை மிகவும் நன்றாக இருக்கிறது."
    ]

    print(f"Generating {len(sentences)} samples...")
    for i, text in enumerate(sentences):
        outputs = model.synthesize(
            text,
            config,
            speaker_wav=speaker_wav,
            language="ta"
        )
        out_path = os.path.join(output_dir, f"sample_{i+1}.wav")
        # synthesize returns a dict with 'wav'
        import scipy.io.wavfile
        scipy.io.wavfile.write(out_path, 24000, outputs['wav'])
        print(f"Saved: {out_path}")

if __name__ == "__main__":
    generate_samples()
