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

def test_hindi_proxy():
    root_path = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts"
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    smoke_checkpoint = os.path.join(root_path, "training/smoke/smoke_gpt_model.pth")
    output_dir = os.path.join(root_path, "samples_tested")
    os.makedirs(output_dir, exist_ok=True)
    
    speaker_wav = os.path.join(root_path, "dataset/taf_04125/wavs/taf_04125_00033069500.wav")

    print("Loading base model...")
    config = XttsConfig()
    config.load_json(os.path.join(xtts_checkpoint, "config.json"))
    
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=xtts_checkpoint, eval=True)
    
    if os.path.exists(smoke_checkpoint):
        print(f"Loading smoke trained weights...")
        model.gpt.load_state_dict(torch.load(smoke_checkpoint, map_location="cpu"), strict=False)

    device = "mps" if torch.backends.mps.is_available() else "cpu"
    model.to(device)

    # Use 'hi' as proxy - we use Romanized Tamil which often maps better than English
    sentences = [
        "Vanakkam, idhu oru sodhanai.", # Welcome, this is a test.
        "Tamil mozhi adhisayamana oru mozhi." # Tamil language is a wonderful language.
    ]

    print(f"Generating phonetic Tamil test samples using 'hi' proxy...")
    for i, text in enumerate(sentences):
        try:
            outputs = model.synthesize(
                text,
                config,
                speaker_wav=speaker_wav,
                language="hi"
            )
            out_path = os.path.join(output_dir, f"test_phonetic_tamil_{i+1}.wav")
            scipy.io.wavfile.write(out_path, 24000, outputs['wav'])
            print(f"Saved: {out_path}")
        except Exception as e:
            print(f"Failed: {e}")

if __name__ == "__main__":
    test_hindi_proxy()
