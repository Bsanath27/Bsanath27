import os
import wave
from pathlib import Path
from pydub import AudioSegment
from pydub.silence import split_on_silence
from faster_whisper import WhisperModel

def segment_and_transcribe(speaker_id, input_dir, output_dir, metadata_in):
    wavs_in = Path(input_dir) / "wavs"
    wavs_out = Path(output_dir) / "wavs_segmented"
    wavs_out.mkdir(parents=True, exist_ok=True)
    
    # Initialize Whisper for re-transcription if needed
    print("Loading Whisper model for segmentation re-transcription...")
    model = WhisperModel("base", device="cpu", compute_type="int8")
    
    with open(metadata_in, 'r', encoding='utf-8') as f:
        lines = f.readlines()
        
    final_metadata = []
    
    print(f"Segmenting {len(lines)} files for {speaker_id}...")
    
    for line in lines:
        filename, raw_transcript = line.strip().split('|')
        wav_path = wavs_in / filename
        
        audio = AudioSegment.from_file(str(wav_path))
        duration = len(audio) / 1000.0
        
        if duration <= 8.0:
            # Keep as is if > 3s (or keep all if Task 1 already filtered < 0.5s)
            # Following "3-8s" rule:
            if duration >= 3.0:
                shutil_name = f"seg_{filename}"
                audio.export(str(wavs_out / shutil_name), format="wav")
                final_metadata.append(f"{shutil_name}|{raw_transcript}")
            else:
                # Clip too short for "3-8s" rule, skipping as per Task 2
                # print(f"  [SKIP] {filename} too short ({duration:.2fs})")
                pass
        else:
            # Split clip > 8s
            print(f"  [SPLITTING] {filename} ({duration:.2f}s)")
            # Try splitting on silence
            chunks = split_on_silence(
                audio, 
                min_silence_len=400, 
                silence_thresh=audio.dBFS-16, 
                keep_silence=200
            )
            
            # If splitting failed to produce parts, or still too long, we might need manual split
            # But for automation, we'll just use the chunks
            
            current_chunk = AudioSegment.empty()
            chunk_idx = 0
            
            for i, chunk in enumerate(chunks):
                if len(current_chunk) + len(chunk) < 8000:
                    current_chunk += chunk
                else:
                    # Export current_chunk and start new one
                    if len(current_chunk) >= 500: # Min 0.5s
                        c_name = f"split_{chunk_idx}_{filename}"
                        c_path = str(wavs_out / c_name)
                        current_chunk.export(c_path, format="wav")
                        
                        # Re-transcribe with Whisper
                        segments, _ = model.transcribe(c_path, language="ta")
                        new_text = "".join([s.text for s in segments]).strip()
                        if new_text:
                            final_metadata.append(f"{c_name}|{new_text}")
                        chunk_idx += 1
                    current_chunk = chunk
            
            # Handle last chunk
            if len(current_chunk) >= 500:
                c_name = f"split_{chunk_idx}_{filename}"
                c_path = str(wavs_out / c_name)
                current_chunk.export(c_path, format="wav")
                segments, _ = model.transcribe(c_path, language="ta")
                new_text = "".join([s.text for s in segments]).strip()
                if new_text:
                    final_metadata.append(f"{c_name}|{new_text}")

    # Write final metadata
    segmented_metadata_path = Path(output_dir) / "metadata_segmented.csv"
    with open(segmented_metadata_path, 'w', encoding='utf-8') as f:
        f.write("\n".join(final_metadata))
        
    print(f"\nSegmentation complete.")
    print(f"Original clips: {len(lines)}")
    print(f"Segmented clips: {len(final_metadata)}")
    print(f"Metadata saved to: {segmented_metadata_path}")

if __name__ == "__main__":
    segment_and_transcribe(
        "taf_04125",
        "dataset/taf_04125",
        "dataset/taf_04125",
        "dataset/taf_04125/metadata.csv"
    )
