import logging
import os
import subprocess
import sys
import threading
import time

import requests

from mt4.libs.util import get_log_directory, read_config

sys.path.append(os.path.abspath(os.path.dirname(__file__)))

BASE_INSTALLATION_DIRECTORY = os.path.abspath(os.path.join(os.path.dirname(__file__)))
LOGGING_DIRECTORY = r"{}\main.log".format(str(get_log_directory()))

logging.basicConfig(filename=LOGGING_DIRECTORY, level=logging.INFO, format="%(asctime)s - %(message)s")

config_values = read_config(BASE_INSTALLATION_DIRECTORY)


def run_script(path):
    """Runs the given script in a new thread"""
    return subprocess.Popen([sys.executable, path])

def send_healthcheck(name: str):
    """Sends a health check to bluebell"""
    headers = {"fp-api_token": config_values["api_token"]}
    params = {
        "systemName": name
    }

    response = requests.post(config_values["api_endpoint"], headers=headers, params=params, timeout=30)
    if response.status_code == 200:
        logging.info(f"Successfully sent health check for: {name}")
        return True
    else:
        logging.error(f"Failed to send health check for {name}. Status: {response.status_code}, Response: {response.text}")
        return False

def ping_bluebell():
    """Sends the health check pings"""
    while True:
        try:
            send_healthcheck("account_data_watcher")
            send_healthcheck("market_price_watcher")
        except Exception as e:
            logging.error(f"Health check failed: {e}", exc_info=True)

        time.sleep(3 * 60 * 60) # runs every 3 hours

def main():
    account_data_watcher = run_script(os.path.join("mt4", "account", "account_data_watcher.py"))
    market_price_watcher = run_script(os.path.join("mt4", "price", "market_price_watcher.py"))

    thread = threading.Thread(target=ping_bluebell, daemon=True)
    thread.start()

    account_data_watcher.wait()
    market_price_watcher.wait()

if __name__ == "__main__":
    main()