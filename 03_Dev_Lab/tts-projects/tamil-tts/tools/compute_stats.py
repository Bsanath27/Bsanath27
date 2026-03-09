import os
import wave
from pathlib import Path
from tools.config import DATA_DIR

def analyze_audio_stats(base_dir):
    base_path = Path(base_dir)
    if not base_path.exists():
        print(f"Directory {base_dir} does not exist.")
        return

    print(f"\n--- Detailed Stats for {base_path.name} ---")
    header = f"{'Speaker':<15} | {'Files':>5} | {'Total':>8} | {'Avg':>5} | {'Min':>5} | {'Max':>5}"
    print(header)
    print("-" * len(header))
    
    for speaker_dir in sorted([d for d in base_path.iterdir() if d.is_dir()]):
        speaker_id = speaker_dir.name
        durations = []
        
        for wav_file in speaker_dir.glob("*.wav"):
            try:
                with wave.open(str(wav_file), 'rb') as w:
                    frames = w.getnframes()
                    sr = w.getframerate()
                    durations.append(frames / sr)
            except Exception:
                continue
                
        if len(durations) > 0:
            total_duration_sec = sum(durations)
            avg_duration = total_duration_sec / len(durations)
            min_duration = min(durations)
            max_duration = max(durations)
            
            total_str = f"{int(total_duration_sec // 60)}m {int(total_duration_sec % 60):02d}s"
            print(f"{speaker_id:<15} | {len(durations):>5} | {total_str:>8} | {avg_duration:4.1f}s | {min_duration:4.1f}s | {max_duration:4.1f}s")

if __name__ == "__main__":
    analyze_audio_stats(os.path.join(DATA_DIR, "normalized_audio/ta_in_female"))
    analyze_audio_stats(os.path.join(DATA_DIR, "normalized_audio/ta_in_male"))
