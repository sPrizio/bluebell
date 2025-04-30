import configparser
from pathlib import Path

def get_root_project_directory() -> Path:
    """Returns the root directory"""
    current_path = Path(__file__).resolve()
    for parent in current_path.parents:
        if (parent / "main.py").exists():
            return parent

    raise FileNotFoundError("Could not find project root with 'main.py'")

def get_log_directory() -> Path:
    """Returns the log directory"""
    log_dir = get_root_project_directory() / "log"
    log_dir.mkdir(parents=True, exist_ok=True)
    return log_dir

def read_config(config_path: str) -> dict:
    """Reads a config file into a dictionary"""
    path = r"{}\config.ini".format(config_path)
    config = configparser.ConfigParser()
    config.read(path)

    return {
        "watch_directory": config.get("settings", "watch_directory", fallback="unknown"),
        "cleanup_threshold": int(config.get("settings", "cleanup_threshold", fallback="600000")),
        "api_endpoint": config.get("api", "api_endpoint", fallback="unknown"),
        "api_token": config.get("api", "api_token", fallback="unknown"),
        "user": config.get("api", "user", fallback="unknown"),
    }