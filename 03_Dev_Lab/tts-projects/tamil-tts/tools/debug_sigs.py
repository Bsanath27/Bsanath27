import os
import torch
import json
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts

def debug():
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    config = XttsConfig()
    config.load_json(os.path.join(xtts_checkpoint, "config.json"))
    
    # Init model
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=xtts_checkpoint, eval=False)
    
    device = "mps" if torch.backends.mps.is_available() else "cpu"
    model.to(device)
    
    # Find a wav file
    wav_file = os.path.join(xtts_checkpoint, "speakers_xtts.pth") # Not a wav, but let's see if we can find any
    # Better: use one from our dataset
    wav_file = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts/dataset/taf_04125/standard/wavs/noise_split_1_taf_04125_01944315112.wav"
    
    print(f"Testing get_conditioning_latents with: {wav_file}")
    try:
        latents = model.get_conditioning_latents(audio_path=wav_file)
        print("Success with keyword arg!")
    except Exception as e:
        print(f"Failed with keyword arg: {e}")
        
    try:
        latents = model.get_conditioning_latents(wav_file)
        print("Success with positional arg!")
    except Exception as e:
        print(f"Failed with positional arg: {e}")

    # Check if 'wav' is somehow expected?
    try:
        import librosa
        wav_data, _ = librosa.load(wav_file, sr=22050)
        wav_tensor = torch.FloatTensor(wav_data).unsqueeze(0)
        latents = model.get_conditioning_latents(audio_path=None, wav=wav_tensor)
        print("Success with hidden 'wav' argument?!")
    except Exception as e:
        print(f"Failed with hidden 'wav' argument: {e}")

if __name__ == "__main__":
    debug()
