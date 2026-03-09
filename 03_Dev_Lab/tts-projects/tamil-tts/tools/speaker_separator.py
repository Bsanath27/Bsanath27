"""
Speaker Separator
-----------------
This script takes an audio file, performs speaker diarization using pyannote.audio,
and then extracts and combines all audio segments belonging to the same speaker
into a single output file. 

Works on both Google Colab (CUDA) and local Apple Silicon (MPS) or CPU.

Prerequisites:
    pip install pyannote.audio pydub torch torchaudio

You also need ffmpeg installed on your system (for pydub).
Local Mac (Homebrew): brew install ffmpeg
Colab: !apt-get install ffmpeg

Usage:
    python speaker-seperator.py --input audio.wav --output_dir ./output --hf_token "YOUR_HUGGINGFACE_TOKEN"
"""

import os
import argparse
import torch
from pyannote.audio import Pipeline
from pydub import AudioSegment

def get_device():
    """Detect and return the optimal PyTorch device."""
    if torch.cuda.is_available():
        return torch.device("cuda")
    elif torch.backends.mps.is_available():
        return torch.device("mps")
    else:
        return torch.device("cpu")

def process_audio(input_file, output_dir, hf_token):
    # 1. Setup Device
    device = get_device()
    print(f"[INFO] Using device: {device}")

    if not os.path.exists(input_file):
        print(f"[ERROR] Input file not found: {input_file}")
        return

    # Create output directory
    os.makedirs(output_dir, exist_ok=True)
    base_name = os.path.splitext(os.path.basename(input_file))[0]

    # 2. Load Diarization Pipeline
    print(f"[INFO] Loading pyannote.audio pipeline... (This might take a moment to download models)")
    try:
        pipeline = Pipeline.from_pretrained(
            "pyannote/speaker-diarization-3.1",
            use_auth_token=hf_token
        )
        # Send pipeline to device (GPU/MPS)
        pipeline.to(device)
    except Exception as e:
        print(f"[ERROR] Failed to load pyannote pipeline. Make sure your HF token is valid and you have accepted the user conditions on Hugging Face.")
        print(f"Details: {e}")
        return

    # 3. Run Diarization
    print(f"[INFO] Running diarization on {input_file}...")
    try:
        diarization = pipeline(input_file)
    except Exception as e:
        print(f"[ERROR] Diarization failed: {e}")
        return

    # 4. Group Segments by Speaker
    # Dictionary to hold list of (start_time, end_time) for each speaker
    speaker_segments = {}
    
    print("[INFO] Analyzing speaker turns...")
    for turn, _, speaker in diarization.itertracks(yield_label=True):
        if speaker not in speaker_segments:
            speaker_segments[speaker] = []
        speaker_segments[speaker].append((turn.start, turn.end))

    # 5. Extract and Combine Audio for Each Speaker
    print(f"[INFO] Loading full audio file for slicing using pydub...")
    try:
        audio = AudioSegment.from_file(input_file)
    except Exception as e:
        print(f"[ERROR] Failed to load audio with pydub (Ensure ffmpeg is installed): {e}")
        return

    print(f"[INFO] Found {len(speaker_segments)} unique speakers. Combining audio...")
    for speaker_id, segments in speaker_segments.items():
        print(f"  -> Processing {speaker_id} ({len(segments)} segments)...")
        
        # Start with an empty audio segment
        combined_audio = AudioSegment.empty()
        
        for start_time, end_time in segments:
            # pydub works in milliseconds
            start_ms = int(start_time * 1000)
            end_ms = int(end_time * 1000)
            
            # Slice the original audio
            segment_audio = audio[start_ms:end_ms]
            
            # Append to the combined audio for this speaker
            combined_audio += segment_audio
            
        # Export the combined audio
        output_filename = f"{base_name}_{speaker_id}.wav"
        output_path = os.path.join(output_dir, output_filename)
        
        combined_audio.export(output_path, format="wav")
        print(f"  -> Saved combined audio to: {output_path}")

    print("[INFO] Processing complete!")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Separate speakers from an audio file and combine each speaker's audio.")
    parser.add_argument("--input", "-i", type=str, required=True, help="Path to input audio file")
    parser.add_argument("--output_dir", "-o", type=str, default="./output_speakers", help="Directory to save combined speaker files")
    parser.add_argument("--hf_token", "-t", type=str, help="Hugging Face access token (required for pyannote)")
    
    args = parser.parse_args()
    
    # Check for token in args first, then environment variable
    token = args.hf_token or os.environ.get("HF_TOKEN")
    
    if not token:
        print("[ERROR] Hugging Face token is required.")
        print("Please provide it via --hf_token or set the HF_TOKEN environment variable.")
        print("You can get a token from https://huggingface.co/settings/tokens")
        print("Make sure you have accepted the conditions for pyannote/speaker-diarization-3.1")
    else:
        process_audio(args.input, args.output_dir, token)
