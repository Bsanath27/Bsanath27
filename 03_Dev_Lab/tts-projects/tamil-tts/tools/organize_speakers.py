import os
import shutil
from pathlib import Path
import time

def organize_by_speaker(base_dir):
    """
    Groups perfectly sliced .wav files into subfolders based on their speaker ID.
    e.g. taf_00008_123.wav goes into taf_00008/
    """
    base_path = Path(base_dir)
    if not base_path.exists():
        print(f"Directory {base_dir} does not exist.")
        return

    start_time = time.time()
    count = 0
    speakers = set()

    print(f"Organizing files in {base_dir}...")
    
    for wav_file in base_path.glob("*.wav"):
        parts = wav_file.stem.split('_')
        # Expecting format like taf_00008_...
        if len(parts) >= 2:
            speaker_id = f"{parts[0]}_{parts[1]}"
            speakers.add(speaker_id)
            
            # Create speaker directory
            speaker_dir = base_path / speaker_id
            speaker_dir.mkdir(exist_ok=True)
            
            # Move file
            shutil.move(str(wav_file), str(speaker_dir / wav_file.name))
            count += 1

    elapsed = time.time() - start_time
    print(f"Moved {count} files into {len(speakers)} speaker folders in {elapsed:.2f} seconds.")

if __name__ == "__main__":
    organize_by_speaker("data/ta_in_female_unzipped")
    organize_by_speaker("data/ta_in_male_unzipped")
    print("Done organizing all speakers!")
