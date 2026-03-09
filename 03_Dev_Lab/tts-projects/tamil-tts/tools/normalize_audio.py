import os
import subprocess
from pathlib import Path
from tools.config import DATA_DIR

def normalize_speaker_audio(input_dir, output_dir, target_sr=22050):
    in_path = Path(input_dir)
    out_base_path = Path(output_dir)

    if not in_path.exists():
        print(f"[{input_dir}] does not exist. Skipping.")
        return

    out_base_path.mkdir(parents=True, exist_ok=True)

    for speaker_dir in in_path.iterdir():
        if speaker_dir.is_dir():
            speaker_out_dir = out_base_path / speaker_dir.name
            speaker_out_dir.mkdir(parents=True, exist_ok=True)

            print(f"Normalizing speaker: {speaker_dir.name}...")
            count = 0
            
            for wav_file in speaker_dir.glob("*.wav"):
                out_file = speaker_out_dir / wav_file.name
                
                cmd = [
                    "ffmpeg", "-y", "-loglevel", "error",
                    "-i", str(wav_file),
                    "-ar", str(target_sr),
                    "-ac", "1",
                    "-c:a", "pcm_s16le",
                    str(out_file)
                ]
                
                try:
                    subprocess.run(cmd, check=True)
                    count += 1
                except subprocess.CalledProcessError as e:
                    print(f"Failed to normalize {wav_file.name}: {e}")

            print(f"  -> Normalized {count} files for {speaker_dir.name}")

if __name__ == "__main__":
    print("Starting audio normalization for TTS...")
    normalize_speaker_audio(os.path.join(DATA_DIR, "ta_in_female_unzipped"), os.path.join(DATA_DIR, "normalized_audio/ta_in_female"))
    normalize_speaker_audio(os.path.join(DATA_DIR, "ta_in_male_unzipped"), os.path.join(DATA_DIR, "normalized_audio/ta_in_male"))
    print("Done normalization.")
