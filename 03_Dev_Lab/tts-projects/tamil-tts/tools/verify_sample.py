import os
from faster_whisper import WhisperModel

def verify():
    model_size = "base"  # Small and fast for verification
    # Run on CPU for compatibility since I don't know the GPU state for sure
    model = WhisperModel(model_size, device="cpu", compute_type="int8")

    samples = [
        {
            "audio": "data/normalized_audio/ta_in_female/taf_02345/taf_02345_00348037167.wav",
            "text": "ஆஸ்த்ரேலியப் பெண்ணுக்கு முப்பத்தி மூன்று ஆண்டுகளுக்குப் பின்னர் இந்தியா இழப்பீடு வழங்கியது"
        },
        {
            "audio": "data/normalized_audio/ta_in_female/taf_07049/taf_07049_00155837462.wav",
            "text": "ஸ்ரீரங்கம் கோவிலில் வெடிகுண்டு மிரட்டல்"
        },
        {
            "audio": "data/normalized_audio/ta_in_female/taf_09705/taf_09705_01218130267.wav",
            "text": "உங்களுடைய உணவுக் கட்டுப்பாட்டைச் சொன்னால் மற்றவர்களுக்கும் உதவியாக இருக்கும்"
        }
    ]

    for sample in samples:
        audio_path = sample["audio"]
        target_text = sample["text"]

        if not os.path.exists(audio_path):
            print(f"File not found: {audio_path}")
            continue

        print(f"\nVerifying: {audio_path}")
        print(f"Target: {target_text}")

        segments, info = model.transcribe(audio_path, beam_size=5, language="ta")

        full_text = ""
        for segment in segments:
            full_text += segment.text

        print(f"Whisper: {full_text.strip()}")
        # Basic check for intersection or high similarity
        # Since STT might not be 100% exact due to spelling variations or punctuation
        # we just want to see if it's "right".

if __name__ == "__main__":
    verify()
