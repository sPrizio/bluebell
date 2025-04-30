import logging
import os
import re
import time

import requests
from watchdog.events import FileSystemEventHandler
from watchdog.observers import Observer

from mt4.libs.util import get_log_directory, read_config

BASE_INSTALLATION_DIRECTORY = os.path.abspath(os.path.join(os.path.dirname(__file__)))
LOGGING_DIRECTORY = r"{}\market_price_watcher.log".format(str(get_log_directory()))

logging.basicConfig(filename=LOGGING_DIRECTORY, level=logging.INFO, format="%(asctime)s - %(message)s")

config_values = read_config(BASE_INSTALLATION_DIRECTORY)

WATCH_DIRECTORY = config_values["watch_directory"]


class MetaTrader4DataWatcher(FileSystemEventHandler):
    """Handles new .csv files detected in the directory."""
    def on_created(self, event):
        """Event that is triggered when a new file is added to the watch directory."""
        if event.is_directory:
            logging.info(f"Directory {os.path.basename(event.src_path)} added to watch directory, nothing to do.")
            return

        if event.src_path.endswith(".csv"):
            logging.info(f"New mt4 .csv file {os.path.basename(event.src_path)} detected. Beginning file upload...")
            process_file(event.src_path)
        else:
            logging.info(f"Non .csv file {os.path.basename(event.src_path)} detected! Ignoring file.")

def process_file(file_path):
    """Function to handle CSV upload to bluebell api."""
    try:
        logging.info(f"Processing file: {os.path.basename(file_path)}")

        if upload_file(file_path):
            logging.info(f"Finished processing: {os.path.basename(file_path)}")

            # Move file to "processed" folder
            processed_dir = os.path.join(WATCH_DIRECTORY, "processed")
            os.makedirs(processed_dir, exist_ok=True)
            os.rename(file_path, os.path.join(processed_dir, os.path.basename(file_path)))

            logging.info(f"Moved {os.path.basename(file_path)} to {processed_dir}")

            cleanup_files("processed")
        else:
            logging.error(f"Failed to upload file {os.path.basename(file_path)}")

            # Move file to "failed" folder
            failed_dir = os.path.join(WATCH_DIRECTORY, "failed")
            os.makedirs(failed_dir, exist_ok=True)
            os.rename(file_path, os.path.join(failed_dir, os.path.basename(file_path)))

            cleanup_files("failed")

    except Exception as e:
        logging.error(f"Error processing {file_path}: {e}")

def start_watcher():
    """Starts the directory watcher."""
    logging.info(f"Watching directory: {WATCH_DIRECTORY}")

    event_handler = MetaTrader4DataWatcher()
    observer = Observer()
    observer.schedule(event_handler, WATCH_DIRECTORY, recursive=False)

    observer.start()

    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()

    observer.join()

def is_valid_filename(file_path):
    """Validates if the file name follows the expected format."""
    file_name, _ = os.path.splitext(os.path.basename(file_path))
    pattern = r"^MarketData_[^_]+_\d{8}_\d{6}_.+$"

    if re.match(pattern, file_name):
        parts = file_name.split("_", maxsplit=4)
        if len(parts) == 5:
            return True

    logging.warning(f"Invalid file name format: {file_name}")
    return False

def upload_file(file_path):
    """Uploads a file to bluebell api."""
    try:
        if not is_valid_filename(file_path):
            return False

        logging.info(f"Uploading file: {file_path}")

        file_name, _ = os.path.splitext(os.path.basename(file_path))
        parts = file_name.split("_", maxsplit=4)
        params = {
            "symbol": parts[1],
            "priceInterval": parts[4],
        }

        with open(file_path, "rb") as file:
            files = {"file": (os.path.basename(file_path), file, "text/csv")}
            headers = {"fp-api_token": config_values["api_token"]}
            response = requests.post(config_values["api_endpoint"], headers=headers, params=params, files=files, timeout=30)

            if response.status_code == 200:
                logging.info(f"Successfully uploaded: {file_path}")
                return True
            else:
                logging.error(f"Failed to upload {file_path}. Status: {response.status_code}, Response: {response.text}")
                return False

    except requests.exceptions.RequestException as e:
        logging.error(f"Error uploading {file_path}: {e}")
        return False

    finally:
        time.sleep(2)

def cleanup_files(directory):
    """Cleanup the files after x amount of time in the given directory."""
    now = time.time()
    working_dir = os.path.join(WATCH_DIRECTORY, directory)
    for file in os.listdir(working_dir):
        file_path = os.path.join(working_dir, file)
        if os.path.isfile(file_path):
            file_age = now - os.path.getmtime(file_path)
            if file_age > config_values["cleanup_threshold"]:
                os.remove(file_path)
                logging.info(f"Cleaned up old file: {os.path.basename(file_path)}")

if __name__ == "__main__":
    start_watcher()
