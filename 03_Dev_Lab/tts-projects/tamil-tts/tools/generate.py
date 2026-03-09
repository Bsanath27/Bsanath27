import os
import torch
import scipy.io.wavfile
import json
import argparse
import numpy as np
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import Xtts
from tools.config import (
    ROOT_PATH, VOCAB_PATH, DEFAULT_XTTS_CHECKPOINT,
    DEVICE, LANGUAGE_PROXY, get_path
)

def generate(checkpoint_path, output_dir, speaker_wav=None, sentences=None):
    os.makedirs(output_dir, exist_ok=True)
    
    # Standard reference speaker if none provided
    if not speaker_wav:
        speaker_wav = get_path("dataset/taf_04125/wavs/taf_04125_00033069500.wav")

    print(f"Loading config from {DEFAULT_XTTS_CHECKPOINT}...")
    config = XttsConfig()
    config.load_json(os.path.join(DEFAULT_XTTS_CHECKPOINT, "config.json"))
    
    # Update vocab
    config.model_args.tokenizer_file = VOCAB_PATH
    with open(VOCAB_PATH, 'r') as f:
        new_vocab_size = len(json.load(f)["model"]["vocab"])
    config.model_args.gpt_number_text_tokens = new_vocab_size

    # Force add 'ta' to supported languages (though we use 'hi' proxy)
    if "ta" not in config.languages:
        config.languages.append("ta")

    print("Initializing model...")
    model = Xtts.init_from_config(config)
    model.load_checkpoint(config, checkpoint_dir=DEFAULT_XTTS_CHECKPOINT, eval=True)
    
    if checkpoint_path and os.path.exists(checkpoint_path):
        print(f"Loading fine-tuned weights from {checkpoint_path}...")
        saved_sd = torch.load(checkpoint_path, map_location="cpu")
        
        # Heuristic mapping for inference
        model_sd = model.gpt.state_dict()
        new_sd = {}
        for k, v in saved_sd.items():
            if k in model_sd:
                new_sd[k] = v
            else:
                # Map GPT layers to inference model
                k_map = k.replace("gpt.h.", "gpt_inference.transformer.h.")
                k_map = k_map.replace("gpt.ln_f.", "gpt_inference.transformer.ln_f.")
                k_map = k_map.replace("text_embedding.", "gpt_inference.transformer.wte.")
                k_map = k_map.replace("text_pos_embedding.", "gpt_inference.pos_embedding.")
                k_map = k_map.replace("final_norm.", "gpt_inference.final_norm.")
                k_map = k_map.replace("text_head.", "gpt_inference.lm_head.0.")
                k_map = k_map.replace("mel_head.", "gpt_inference.lm_head.1.")
                
                if k_map not in model_sd:
                    k_map = "gpt_inference." + k_map
                
                if k_map in model_sd:
                    new_sd[k_map] = v
                    
        model.gpt.load_state_dict(new_sd, strict=False)

    print(f"Using device: {DEVICE}")
    model.to(DEVICE)

    if not sentences:
        sentences = [
            "வாழைப்பழம் வழுக்கிச் சென்று மழையில் விழுந்தது.",
            "தம்பி, சீக்கிரம் கடைக்குப் போய் மளிகைச் சாமான்கள் வாங்கிட்டு வா.",
            "செயற்கை நுண்ணறிவுத் தொழில்நுட்பம் உலகத்தை அதிவேகமாக மாற்றிக் கொண்டிருக்கிறது",
        ]

    print(f"Generating samples in {output_dir}...")
    for i, item in enumerate(sentences):
        try:
            # Handle both single strings and lists of strings for long samples
            chunks = [item] if isinstance(item, str) else item
                
            combined_wav = []
            for chunk in chunks:
                outputs = model.synthesize(
                    chunk,
                    config,
                    speaker_wav=speaker_wav,
                    language=LANGUAGE_PROXY 
                )
                combined_wav.append(outputs['wav'])
            
            final_wav = np.concatenate(combined_wav)
            out_path = os.path.join(output_dir, f"sample_{i+1}.wav")
            scipy.io.wavfile.write(out_path, 24000, final_wav)
            print(f"Saved: {out_path}")
        except Exception as e:
            print(f"Failed for sample {i+1}: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Unified XTTS Sample Generator")
    parser.add_argument("--checkpoint", type=str, help="Path to GPT checkpoint pth file")
    parser.add_argument("--output", type=str, default="samples_output", help="Output directory")
    parser.add_argument("--speaker_wav", type=str, help="Reference speaker wav")
    
    args = parser.parse_args()
    
    output_dir = get_path(args.output)
    generate(args.checkpoint, output_dir, args.speaker_wav)
