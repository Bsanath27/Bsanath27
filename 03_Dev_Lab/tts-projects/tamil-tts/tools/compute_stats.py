import os
import wave
from pathlib import Path

def analyze_audio_stats(base_dir):
    """
    Computes per-speaker stats:
    - Total duration
    - Number of files
    - Shortest, longest, and average duration
    """
    base_path = Path(base_dir)
    if not base_path.exists():
        print(f"Directory {base_dir} does not exist.")
        return

    print(f"\n--- Detailed Stats for {base_path.name} ---")
    header = f"{'Speaker':<15} | {'Files':>5} | {'Total':>8} | {'Avg':>5} | {'Min':>5} | {'Max':>5}"
    print(header)
    print("-" * len(header))
    
    # Iterate through each speaker folder
    for speaker_dir in sorted([d for d in base_path.iterdir() if d.is_dir()]):
        speaker_id = speaker_dir.name
        total_frames = 0
        file_count = 0
        sample_rate = None
        durations = []
        
        for wav_file in speaker_dir.glob("*.wav"):
            try:
                with wave.open(str(wav_file), 'rb') as w:
                    frames = w.getnframes()
                    sr = w.getframerate()
                    
                    duration = frames / sr
                    durations.append(duration)
                    
                    if sample_rate is None:
                        sample_rate = sr
                    
                    total_frames += frames
                    file_count += 1
            except Exception:
                continue
                
        if file_count > 0:
            total_duration_sec = sum(durations)
            avg_duration = total_duration_sec / file_count
            min_duration = min(durations)
            max_duration = max(durations)
            
            total_str = f"{int(total_duration_sec // 60)}m {int(total_duration_sec % 60):02d}s"
            print(f"{speaker_id:<15} | {file_count:>5} | {total_str:>8} | {avg_duration:4.1f}s | {min_duration:4.1f}s | {max_duration:4.1f}s")

if __name__ == "__main__":
    # We analyze the normalized audio since that's the final output
    analyze_audio_stats("data/normalized_audio/ta_in_female")
    analyze_audio_stats("data/normalized_audio/ta_in_male")
