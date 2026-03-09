import os
import torch
import scipy.io.wavfile
import json
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts, XttsArgs, XttsAudioConfig
from TTS.config.shared_configs import BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig

# PyTorch 2.6 security fixes
try:
    torch.serialization.add_safe_globals([
        XttsConfig, XttsArgs, XttsAudioConfig,
        BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig
    ])
except Exception:
    pass

def generate_samples():
    root_path = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts"
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    gpt_checkpoint = os.path.join(root_path, "training/full_run/gpt_final.pth")
    vocab_path = os.path.join(root_path, "training/model_assets/vocab.json")
    output_dir = os.path.join(root_path, "30epoch-samples")
    os.makedirs(output_dir, exist_ok=True)
    
    # Use an existing audio for speaker cloning reference
    speaker_wav = os.path.join(root_path, "dataset/taf_04125/wavs/taf_04125_00033069500.wav")

    print("Loading config...")
    config = XttsConfig()
    config.load_json(os.path.join(xtts_checkpoint, "config.json"))
    
    # Update vocab
    config.model_args.tokenizer_file = vocab_path
    with open(vocab_path, 'r') as f:
        v = json.load(f)
        new_vocab_size = len(v["model"]["vocab"])
    config.model_args.gpt_number_text_tokens = new_vocab_size

    # Force add 'ta' to supported languages
    if "ta" not in config.languages:
        config.languages.append("ta")

    print("Initializing model...")
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=xtts_checkpoint, eval=True)
    
    print(f"Loading 30-epoch weights from {gpt_checkpoint}...")
    saved_sd = torch.load(gpt_checkpoint, map_location="cpu")
    
    # Heuristic mapping for XTTS v2 gpt weights
    model_sd = model.gpt.state_dict()
    new_sd = {}
    
    for k, v in saved_sd.items():
        if k in model_sd:
            new_sd[k] = v
        else:
            # Map GPT layers
            k_map = k.replace("gpt.h.", "gpt_inference.transformer.h.")
            k_map = k_map.replace("gpt.ln_f.", "gpt_inference.transformer.ln_f.")
            
            # Map embeddings and heads
            k_map = k_map.replace("text_embedding.", "gpt_inference.transformer.wte.")
            k_map = k_map.replace("text_pos_embedding.", "gpt_inference.pos_embedding.")
            k_map = k_map.replace("final_norm.", "gpt_inference.final_norm.")
            k_map = k_map.replace("text_head.", "gpt_inference.lm_head.0.")
            k_map = k_map.replace("mel_head.", "gpt_inference.lm_head.1.")
            
            # If still not found, try adding gpt_inference prefix
            if k_map not in model_sd:
                k_map = "gpt_inference." + k_map
            
            if k_map in model_sd:
                new_sd[k_map] = v
                
    missing, unexpected = model.gpt.load_state_dict(new_sd, strict=False)
    if missing:
        print(f"Missing keys: {len(missing)}")
    if unexpected:
        print(f"Unexpected keys: {len(unexpected)}")

    device = "mps" if torch.backends.mps.is_available() else "cpu"
    print(f"Using device: {device}")
    model.to(device)

    sentences = [
        "வாழைப்பழம் வழுக்கிச் சென்று மழையில் விழுந்தது.",
        "தம்பி, சீக்கிரம் கடைக்குப் போய் மளிகைச் சாமான்கள் வாங்கிட்டு வா.",
        "செயற்கை நுண்ணறிவுத் தொழில்நுட்பம் உலகத்தை அதிவேகமாக மாற்றிக் கொண்டிருக்கிறது",
        "நாளைக்கு மழை பெய்யுமா என்று உனக்குத் தெரியுமா? நாம் வெளியே செல்லலாமா?",
        "யாதும் ஊரே யாவரும் கேளிர் எனப் புறநானூறு போதிக்கிறது.",
        [
            "தமிழ் மொழி உலகின் மிகத் தொன்மையான மொழிகளில் ஒன்று.",
            "இது திராவிட மொழிக் குடும்பத்தின் ஒரு அங்கமாகும்.",
            "சங்க இலக்கியங்கள் முதல் நவீன கால இலக்கியங்கள் வரை தமிழ் மொழி ஒரு நீண்ட வரலாற்றைக் கொண்டுள்ளது.",
            "திருவள்ளுவரின் திருக்குறள் உலகப் பொதுமறையாகப் போற்றப்படுகிறது.",
            "தமிழகத்தின் கலை, கலாச்சாரம், கட்டிடக்கலை மற்றும் இசை ஆகியவை உலகப் புகழ் பெற்றவை.",
            "குறிப்பாக, தஞ்சைப் பெரிய கோயில் தமிழர்களின் கட்டிடக்கலைத் திறமைக்கு ஒரு சிறந்த சான்றாகும்.",
            "பாரதியார், பாரதிதாசன் போன்ற கவிஞர்கள் தமிழர்களின் விடுதலை உணர்வைத் தூண்டியவர்கள்.",
            "இன்று கணினித் தமிழும் மின்னாளுமையும் வளர்ந்து வரும் நிலையில், தமிழ் மொழி உலக அரங்கில் தனது தனித்துவத்தைத் தொடர்ந்து நிலைநாட்டி வருகிறது."
        ]
    ]

    print(f"Generating samples in {output_dir}...")
    for i, item in enumerate(sentences):
        try:
            # Handle both single strings and lists of strings for long samples
            if isinstance(item, str):
                chunks = [item]
            else:
                chunks = item
                
            combined_wav = []
            for chunk in chunks:
                outputs = model.synthesize(
                    chunk,
                    config,
                    speaker_wav=speaker_wav,
                    language="hi" # Use 'hi' as proxy for Tamil training
                )
                combined_wav.append(outputs['wav'])
            
            import numpy as np
            final_wav = np.concatenate(combined_wav)
            
            out_path = os.path.join(output_dir, f"sample_{i+1}.wav")
            scipy.io.wavfile.write(out_path, 24000, final_wav)
            print(f"Saved: {out_path}")
        except Exception as e:
            print(f"Failed for sample {i+1}: {e}")

if __name__ == "__main__":
    generate_samples()
