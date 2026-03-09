import os
import subprocess
from pathlib import Path

def augment(speaker_id, input_dir, output_dir, metadata_in):
    wavs_in = Path(input_dir) / "wavs_segmented"
    wavs_out = Path(output_dir) / "wavs_augmented"
    wavs_out.mkdir(parents=True, exist_ok=True)
    
    with open(metadata_in, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        
    augmented_metadata = []
    
    print(f"Augmenting {len(lines)} clips to ~3 hours...")
    
    for line in lines:
        filename, transcript = line.strip().split('|')
        base_name = filename.replace(".wav", "")
        in_path = wavs_in / filename
        
        # 0. Original
        shutil_name = f"orig_{filename}"
        subprocess.run(["ffmpeg", "-y", "-i", str(in_path), "-ar", "22050", str(wavs_out / shutil_name)], capture_output=True)
        augmented_metadata.append(f"{shutil_name}|{transcript}")
        
        # 1. Speed 0.95x
        s095_name = f"spd095_{filename}"
        subprocess.run(["ffmpeg", "-y", "-i", str(in_path), "-af", "atempo=0.95", "-ar", "22050", str(wavs_out / s095_name)], capture_output=True)
        augmented_metadata.append(f"{s095_name}|{transcript}")
        
        # 2. Speed 1.05x
        s105_name = f"spd105_{filename}"
        subprocess.run(["ffmpeg", "-y", "-i", str(in_path), "-af", "atempo=1.05", "-ar", "22050", str(wavs_out / s105_name)], capture_output=True)
        augmented_metadata.append(f"{s105_name}|{transcript}")
        
        # 3. Pitch +1 semitone (1.05946 rate change then fix tempo)
        p1_name = f"pchP1_{filename}"
        subprocess.run(["ffmpeg", "-y", "-i", str(in_path), "-af", "asetrate=22050*1.059,atempo=0.944", "-ar", "22050", str(wavs_out / p1_name)], capture_output=True)
        augmented_metadata.append(f"{p1_name}|{transcript}")
        
        # 4. Pitch -1 semitone (0.94387 rate change then fix tempo)
        pM1_name = f"pchM1_{filename}"
        subprocess.run(["ffmpeg", "-y", "-i", str(in_path), "-af", "asetrate=22050*0.944,atempo=1.059", "-ar", "22050", str(wavs_out / pM1_name)], capture_output=True)
        augmented_metadata.append(f"{pM1_name}|{transcript}")
        
        # 5. Noise (Add very light white noise)
        n_name = f"noise_{filename}"
        # SNR 25-30dB is roughly 0.05 volume for white noise relative to full scale speech
        subprocess.run(["ffmpeg", "-y", "-i", str(in_path), "-filter_complex", "anoisesrc=d=5:c=white:a=0.01[n];[0:a][n]amix=inputs=2:duration=shortest", "-ar", "22050", str(wavs_out / n_name)], capture_output=True)
        augmented_metadata.append(f"{n_name}|{transcript}")
        
        # 6. Reverb (Mild room reverb using aecho)
        r_name = f"reverb_{filename}"
        subprocess.run(["ffmpeg", "-y", "-i", str(in_path), "-af", "aecho=0.8:0.88:60:0.4", "-ar", "22050", str(wavs_out / r_name)], capture_output=True)
        augmented_metadata.append(f"{r_name}|{transcript}")

    # Write final metadata
    final_metadata_path = Path(output_dir) / "metadata.csv"
    with open(final_metadata_path, 'w', encoding='utf-8') as f:
        f.write("\n".join(augmented_metadata))
        
    print(f"\nAugmentation complete.")
    print(f"Total clips: {len(augmented_metadata)}")
    print(f"Metadata saved to: {final_metadata_path}")

if __name__ == "__main__":
    augment(
        "taf_04125",
        "dataset/taf_04125",
        "dataset/taf_04125",
        "dataset/taf_04125/metadata_segmented.csv"
    )
