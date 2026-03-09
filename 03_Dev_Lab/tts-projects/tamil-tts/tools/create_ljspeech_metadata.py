import os
from pathlib import Path

def create_ljspeech_metadata(input_metadata, output_file):
    with open(input_metadata, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        
    ljspeech_lines = []
    for line in lines:
        parts = line.strip().split('|')
        if len(parts) < 2:
            continue
        
        # Original: filename.wav|transcript|speaker_id
        # LJSpeech: id|transcript|normalized_transcript
        
        filename = parts[0]
        id_base = filename.replace(".wav", "")
        transcript = parts[1]
        
        ljspeech_lines.append(f"{id_base}|{transcript}|{transcript}")
        
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("\n".join(ljspeech_lines))
        
    print(f"Standard LJSpeech metadata created at {output_file}")

if __name__ == "__main__":
    create_ljspeech_metadata(
        "dataset/taf_04125/metadata.csv",
        "dataset/taf_04125/standard/metadata.csv"
    )
