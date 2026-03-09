import random
from pathlib import Path

def split_data(metadata_path, output_dir, train_ratio=0.95):
    with open(metadata_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    random.shuffle(lines)
    
    num_train = int(len(lines) * train_ratio)
    train_lines = lines[:num_train]
    val_lines = lines[num_train:]
    
    out_path = Path(output_dir)
    
    # XTTS usually expects format: path/to/wav|transcript|speaker_id
    # But for single speaker, path|transcript is fine. 
    # I'll add the speaker_id 'taf_04125' as the 3rd field for compatibility.
    
    with open(out_path / "train.txt", "w", encoding="utf-8") as f:
        for line in train_lines:
            f.write(f"wavs_augmented/{line.strip()}|taf_04125\n")
            
    with open(out_path / "val.txt", "w", encoding="utf-8") as f:
        for line in val_lines:
            f.write(f"wavs_augmented/{line.strip()}|taf_04125\n")
            
    print(f"Split complete. Train: {len(train_lines)}, Val: {len(val_lines)}")

if __name__ == "__main__":
    split_data("dataset/taf_04125/metadata.csv", "dataset/taf_04125")
