import json
import os
from pathlib import Path

def extend_vocab():
    root_path = "/Users/sanathbs/03_Dev_Lab/tts-projects/tamil-tts"
    metadata_path = os.path.join(root_path, "dataset/taf_04125/standard/metadata.csv")
    xtts_checkpoint = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"
    vocab_path = os.path.join(xtts_checkpoint, "vocab.json")
    
    # 1. Extract all unique characters from metadata
    tamil_chars = set()
    with open(metadata_path, 'r', encoding='utf-8') as f:
        for line in f:
            parts = line.strip().split('|')
            if len(parts) >= 2:
                transcript = parts[1]
                for char in transcript:
                    # Filter for Tamil unicode range (roughly 0B80–0BFF) 
                    # plus punctuation we might want
                    if '\u0b80' <= char <= '\u0bff' or char in '.,?! ':
                        tamil_chars.add(char)
    
    print(f"Extracted {len(tamil_chars)} unique characters/punctuation from Tamil dataset.")

    # 2. Load original vocab
    with open(vocab_path, 'r', encoding='utf-8') as f:
        full_vocab = json.load(f)
    
    # XTTS v2 vocab is in model -> vocab
    vocab = full_vocab["model"]["vocab"]
    
    # 3. Add missing characters
    added_count = 0
    # Find the max ID across both vocab and added_tokens
    max_vocab_id = max(vocab.values())
    max_added_id = max([t["id"] for t in full_vocab.get("added_tokens", [])]) if full_vocab.get("added_tokens") else 0
    max_idx = max(max_vocab_id, max_added_id)
    
    # Sorting chars for consistency
    for char in sorted(list(tamil_chars)):
        if char not in vocab:
            max_idx += 1
            vocab[char] = max_idx
            added_count += 1
            
    # 4. Save new vocab to the training directory
    out_dir = os.path.join(root_path, "training/model_assets")
    os.makedirs(out_dir, exist_ok=True)
    new_vocab_path = os.path.join(out_dir, "vocab.json")
    
    with open(new_vocab_path, 'w', encoding='utf-8') as f:
        json.dump(full_vocab, f, ensure_ascii=False, indent=4)
        
    print(f"Added {added_count} characters. New vocab size: {len(vocab)}.")
    print(f"New vocab saved to {new_vocab_path}")

if __name__ == "__main__":
    extend_vocab()
