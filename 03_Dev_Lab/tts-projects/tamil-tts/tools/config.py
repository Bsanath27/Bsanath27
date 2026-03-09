import os
import torch
from pathlib import Path
from TTS.tts.configs.xtts_config import XttsConfig
from TTS.tts.models.xtts import XttsArgs, XttsAudioConfig
from TTS.config.shared_configs import BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig

# 1. Path Detection
# Get the root directory of the project (parent of 'tools')
ROOT_PATH = str(Path(__file__).parent.parent.absolute())

# Standard XTTS v2 checkpoint location on this Mac
DEFAULT_XTTS_CHECKPOINT = "/Users/sanathbs/Library/Application Support/tts/tts_models--multilingual--multi-dataset--xtts_v2"

# 2. Device Selection
DEVICE = "mps" if torch.backends.mps.is_available() else "cpu"

# 3. Model Constants
LANGUAGE_PROXY = "hi" # Hindi is used as the proxy language for Tamil UTF-8 support

# 4. PyTorch 2.6 Security Compatibility
def setup_torch_security():
    """Adds XTTS classes to safe globals for torch.load compatibility."""
    try:
        torch.serialization.add_safe_globals([
            XttsConfig, XttsArgs, XttsAudioConfig,
            BaseDatasetConfig, BaseAudioConfig, BaseTrainingConfig
        ])
    except Exception:
        pass

def get_path(*args):
    """Generates an absolute path relative to the project root."""
    return os.path.join(ROOT_PATH, *args)

# 5. Asset Paths
ASSETS_PATH = get_path("training/model_assets")
VOCAB_PATH = os.path.join(ASSETS_PATH, "vocab.json")
DVAE_PATH = os.path.join(ASSETS_PATH, "dvae.pth")
MEL_NORMS_PATH = os.path.join(ASSETS_PATH, "mel_norms.pth")
DATA_DIR = get_path("data")
TRAINING_DIR = get_path("training")
PHASE2_DIR = os.path.join(TRAINING_DIR, "multispeaker_phase2")
PHASE1_WEIGHTS = os.path.join(TRAINING_DIR, "full_run/gpt_final.pth")

setup_torch_security()
