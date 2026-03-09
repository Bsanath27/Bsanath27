import os
import pandas as pd
from tools.config import DATA_DIR, get_path

# Paths
NORMALIZED_DIR = os.path.join(DATA_DIR, "normalized_audio")
FEMALE_TSV = os.path.join(DATA_DIR, "line_index_female.tsv")
MALE_TSV = os.path.join(DATA_DIR, "line_index_male.tsv")
OUTPUT_CSV = os.path.join(DATA_DIR, "multispeaker_metadata.csv")

def prepare_metadata():
    print("Preparing multi-speaker metadata...")
    
    # Load TSVs
    female_df = pd.read_csv(FEMALE_TSV, sep="\t", header=None, names=["audio_filename", "text"])
    male_df = pd.read_csv(MALE_TSV, sep="\t", header=None, names=["audio_filename", "text"])
    
    # Add speaker labels and full paths
    female_metadata = []
    for _, row in female_df.iterrows():
        audio_id = row['audio_filename']
        speaker_id = audio_id.split('_')[1]
        rel_path = os.path.join("normalized_audio", "ta_in_female", f"taf_{speaker_id}", f"{audio_id}.wav")
        female_metadata.append([rel_path, row['text'], f"female_{speaker_id}"])
    
    male_metadata = []
    for _, row in male_df.iterrows():
        audio_id = row['audio_filename']
        speaker_id = audio_id.split('_')[1]
        rel_path = os.path.join("normalized_audio", "ta_in_male", f"tag_{speaker_id}", f"{audio_id}.wav")
        male_metadata.append([rel_path, row['text'], f"male_{speaker_id}"])
    
    # Combine and save
    combined_metadata = female_metadata + male_metadata
    metadata_df = pd.DataFrame(combined_metadata, columns=["audio_file", "text", "speaker_name"])
    
    # Verify file existence and text validity
    final_rows = []
    missing_files = 0
    invalid_text = 0
    for _, row in metadata_df.iterrows():
        text = row['text']
        if pd.isna(text) or not isinstance(text, str) or len(text.strip()) == 0:
            invalid_text += 1
            continue
            
        full_path = os.path.join(DATA_DIR, row['audio_file'])
        if os.path.exists(full_path):
            final_rows.append(row)
        else:
            missing_files += 1
    
    final_df = pd.DataFrame(final_rows)
    final_df.to_csv(OUTPUT_CSV, sep="|", index=False, header=False)
    
    print(f"Metadata saved to {OUTPUT_CSV}")
    print(f"Total valid samples: {len(final_df)}")
    print(f"Missing files: {missing_files}")
    print(f"Invalid/Empty text rows skipped: {invalid_text}")

if __name__ == "__main__":
    prepare_metadata()
