import os
import wave
import shutil
from pathlib import Path

def inspect_and_filter(speaker_id, raw_audio_dir, line_index_path, output_dir):
    wavs_out = Path(output_dir) / "wavs"
    wavs_out.mkdir(parents=True, exist_ok=True)
    
    valid_clips = []
    total_duration = 0
    
    # Read line_index
    with open(line_index_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    print(f"Inspecting {len(lines)} files for speaker {speaker_id}...")
    
    for line in lines:
        parts = line.strip().split('\t')
        if len(parts) < 2:
            continue
        
        file_id, transcript = parts[0], parts[1]
        wav_path = Path(raw_audio_dir) / f"{file_id}.wav"
        
        if not wav_path.exists():
            print(f"  [MISSING] {wav_path}")
            continue
            
        try:
            with wave.open(str(wav_path), 'rb') as w:
                frames = w.getnframes()
                sr = w.getframerate()
                duration = frames / sr
                
                if 0.5 <= duration <= 15.0:
                    shutil.copy2(str(wav_path), str(wavs_out / f"{file_id}.wav"))
                    valid_clips.append(f"{file_id}.wav|{transcript}")
                    total_duration += duration
                else:
                    print(f"  [FILTERED] {file_id}.wav ({duration:.2f}s)")
        except Exception as e:
            print(f"  [ERROR] {file_id}.wav: {e}")
            
    # Write metadata
    metadata_path = Path(output_dir) / "metadata.csv"
    with open(metadata_path, 'w', encoding='utf-8') as f:
        f.write("\n".join(valid_clips))
        
    print(f"\n--- Inspection Results for {speaker_id} ---")
    print(f"Total clips analyzed: {len(lines)}")
    print(f"Clips accepted: {len(valid_clips)}")
    print(f"Total duration: {total_duration/60:.2f} minutes")
    print(f"Metadata saved to: {metadata_path}")

if __name__ == "__main__":
    inspect_and_filter(
        "taf_04125",
        "data/normalized_audio/ta_in_female/taf_04125",
        "dataset/taf_04125/metadata/line_index_raw.tsv",
        "dataset/taf_04125"
    )
