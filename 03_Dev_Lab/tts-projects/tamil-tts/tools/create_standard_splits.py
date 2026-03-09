import random
from pathlib import Path

def create_standard_splits(metadata_path, output_dir, train_size=150, val_size=10):
    with open(metadata_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    random.seed(42)
    random.shuffle(lines)
    
    train_lines = lines[:train_size]
    val_lines = lines[train_size:train_size+val_size]
    
    out_path = Path(output_dir)
    out_path.mkdir(parents=True, exist_ok=True)
    
    with open(out_path / "metadata_train.csv", "w", encoding="utf-8") as f:
        f.writelines(train_lines)
            
    with open(out_path / "metadata_val.csv", "w", encoding="utf-8") as f:
        f.writelines(val_lines)
            
    print(f"Standard splits created. Train: {len(train_lines)}, Val: {len(val_lines)}")

if __name__ == "__main__":
    create_standard_splits(
        "dataset/taf_04125/standard/metadata.csv",
        "dataset/taf_04125/standard"
    )
