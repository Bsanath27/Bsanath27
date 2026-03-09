import json
import os
from pathlib import Path
from tools.config import ROOT_PATH, DATA_DIR, VOCAB_PATH, ASSETS_PATH, get_path

def extend_vocab():
    metadata_path = get_path("dataset/taf_04125/standard/metadata.csv")
    
    # 1. Extract all unique characters from metadata
    tamil_chars = set()
    with open(metadata_path, 'r', encoding='utf-8') as f:
        for line in f:
            parts = line.strip().split('|')
            if len(parts) >= 2:
                transcript = parts[1]
                for char in transcript:
                    if '\u0b80' <= char <= '\u0bff' or char in '.,?! ':
                        tamil_chars.add(char)
    
    print(f"Extracted {len(tamil_chars)} unique characters/punctuation from Tamil dataset.")

    # 2. Load original vocab from standard path
    # Using the default XTTS v2 vocab.json as base
    base_xtts_vocab = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2/vocab.json"
    with open(base_xtts_vocab, 'r', encoding='utf-8') as f:
        full_vocab = json.load(f)
    
    vocab = full_vocab["model"]["vocab"]
    
    # 3. Add missing characters
    added_count = 0
    max_vocab_id = max(vocab.values())
    max_added_id = max([t["id"] for t in full_vocab.get("added_tokens", [])]) if full_vocab.get("added_tokens") else 0
    max_idx = max(max_vocab_id, max_added_id)
    
    for char in sorted(list(tamil_chars)):
        if char not in vocab:
            max_idx += 1
            vocab[char] = max_idx
            added_count += 1
            
    # 4. Save new vocab to the training directory
    os.makedirs(ASSETS_PATH, exist_ok=True)
    new_vocab_path = os.path.join(ASSETS_PATH, "vocab.json")
    
    with open(new_vocab_path, 'w', encoding='utf-8') as f:
        json.dump(full_vocab, f, ensure_ascii=False, indent=4)
        
    print(f"Added {added_count} characters. New vocab size: {len(vocab)}.")
    print(f"New vocab saved to {new_vocab_path}")

if __name__ == "__main__":
    extend_vocab()
