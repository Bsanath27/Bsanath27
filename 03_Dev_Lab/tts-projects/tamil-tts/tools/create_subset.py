from pathlib import Path

def create_subset(train_path, output_dir, size=150):
    with open(train_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        
    subset_lines = lines[:size]
    
    out_path = Path(output_dir)
    out_path.mkdir(parents=True, exist_ok=True)
    
    with open(out_path / "train_subset.txt", "w", encoding="utf-8") as f:
        f.writelines(subset_lines)
        
    print(f"Subset of {len(subset_lines)} samples created at {out_path / 'train_subset.txt'}")

if __name__ == "__main__":
    create_subset("dataset/taf_04125/train.txt", "dataset/taf_04125")
