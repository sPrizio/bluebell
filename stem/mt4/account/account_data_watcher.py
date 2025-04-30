import csv
import json
import logging
import os
import re
import time
from dataclasses import dataclass, asdict
from datetime import datetime
from enum import Enum
from typing import Optional, List

import requests
from watchdog.events import FileSystemEventHandler
from watchdog.observers import Observer

from mt4.libs.util import get_log_directory, read_config

BASE_INSTALLATION_DIRECTORY = os.path.abspath(os.path.join(os.path.dirname(__file__)))
LOGGING_DIRECTORY = r"{}\account_data_watcher.log".format(str(get_log_directory()))

logging.basicConfig(filename=LOGGING_DIRECTORY, level=logging.INFO, format="%(asctime)s - %(message)s")

config_values = read_config(BASE_INSTALLATION_DIRECTORY)

WATCH_DIRECTORY = config_values["watch_directory"]

HEADERS : dict = {"fp-api_token": config_values["api_token"]}


class TradeType(Enum):
    """Defines a trade type enum"""
    BUY = "BUY"
    SELL = "SELL"
    UNKNOWN = "UNKNOWN"

class TransactionType(Enum):
    """Defines a transaction type enum"""
    DEPOSIT = "DEPOSIT"
    WITHDRAWAL = "WITHDRAWAL"

@dataclass
class MT4CsvRow:
    id: str
    open_time: datetime
    type: TradeType
    lot_size: float
    symbol: str
    open_price: float
    stop_loss: float
    take_profit: float
    close_time: datetime
    close_price: float
    net_profit: float
    description: str

    @classmethod
    def from_csv_row(cls, row: dict) -> "MT4CsvRow":
        return cls(
            id=row["Ticket"],
            open_time=datetime.strptime(row["Open Time"], "%Y.%m.%d %H:%M:%S"),
            type=TradeType[row["Type"].upper()],
            lot_size=float(row["Lots"]),
            symbol=row["Symbol"],
            open_price=float(row["Open Price"]),
            stop_loss=float(row["Stop Loss"]),
            take_profit=float(row["Take Profit"]),
            close_time=datetime.strptime(row["Close Time"], "%Y.%m.%d %H:%M:%S"),
            close_price=float(row["Close Price"]),
            net_profit=float(row["Net Profit"]),
            description=row["Comment"]
        )

@dataclass
class Trade:
    """Defines a trade as expected by bluebell"""
    trade_id: str
    trade_platform: str
    product: str
    trade_type: str
    trade_open_time: datetime
    lot_size: float
    open_price: float
    net_profit: Optional[float] = None
    close_price: Optional[float] = None
    trade_close_time: Optional[datetime] = None
    stop_loss: Optional[float] = None
    take_profit: Optional[float] = None

@dataclass
class Transaction:
    """Defines a transaction as expected by bluebell"""
    transaction_type: str
    transaction_date: datetime
    name: str
    amount: float
    transaction_status: str

@dataclass
class AccountRequest:
    """Defines the api request that will be sent to the bluebell api"""
    user_identifier: str
    account_number: int
    trades: List[Trade]
    transactions: List[Transaction]

def to_camel_case(snake_str):
    """Converts a string to camelCase"""
    parts = snake_str.split('_')
    return parts[0] + ''.join(word.capitalize() for word in parts[1:])

def dict_keys_to_camel_case(obj):
    """Converts dict keys to camelCase"""
    if isinstance(obj, dict):
        return {to_camel_case(k): dict_keys_to_camel_case(v) for k, v in obj.items()}
    elif isinstance(obj, list):
        return [dict_keys_to_camel_case(i) for i in obj]
    else:
        return obj

class MetaTrader4AccountDataWatcher(FileSystemEventHandler):
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
    """Computes the request object from the CSV file identified within the watched directory"""
    try:
        logging.info(f"Processing file: {os.path.basename(file_path)}")

        if not perform_api_call(read_file(file_path), file_path):
            logging.error(f"Failed to read file {os.path.basename(file_path)}")

            # Move file to "failed" folder
            failed_dir = os.path.join(WATCH_DIRECTORY, "failed")
            os.makedirs(failed_dir, exist_ok=True)
            os.rename(file_path, os.path.join(failed_dir, os.path.basename(file_path)))

            cleanup_files("failed")
        else:
            # Move file to "processed" folder
            processed_dir = os.path.join(WATCH_DIRECTORY, "processed")
            os.makedirs(processed_dir, exist_ok=True)
            os.rename(file_path, os.path.join(processed_dir, os.path.basename(file_path)))

            logging.info(f"Moved {os.path.basename(file_path)} to {processed_dir}")
            logging.info(f"Finished processing: {os.path.basename(file_path)}")
            cleanup_files("processed")
    except Exception as e:
        logging.error(f"Error processing {file_path}: {e}")

def perform_api_call(req: AccountRequest, file_path: str) -> bool:
    """Sends the data to the bluebell api"""
    if req is None:
        return False

    base_dict = asdict(req)
    formatted_dict = dict_keys_to_camel_case(base_dict)
    json_data = json.dumps(formatted_dict, default=str)

    HEADERS["Content-Type"] = "application/json"
    response = requests.post(config_values["api_endpoint"], headers=HEADERS, data=json_data, timeout=30)

    if response.status_code == 200:
        logging.info(f"Successfully uploaded: {file_path}")
        return True
    else:
        logging.error(f"Failed to upload {file_path}. Status: {response.status_code}, Response: {response.text}")
        return False

def start_watcher():
    """Starts the directory watcher."""
    logging.info(f"Watching directory: {WATCH_DIRECTORY}")

    event_handler = MetaTrader4AccountDataWatcher()
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
    file_name, ext = os.path.splitext(os.path.basename(file_path))
    pattern = r"^AccountTradingData_\d+$"

    if ext.lower() != ".csv":
        logging.error(f"Invalid file extension: {ext}")
        return False

    if re.match(pattern, file_name):
        parts = file_name.split("_", maxsplit=4)
        if len(parts) > 1:
            return True

    logging.error(f"Invalid file name format: {file_name}")
    return False

def read_file(file_path) -> AccountRequest | None:
    """Reads the file and returns it as a struct."""
    try:
        if not is_valid_filename(file_path):
            return None

        file_name, _ = os.path.splitext(os.path.basename(file_path))
        parts = file_name.split("_", maxsplit=4)
        account_number = int(parts[1])

        with open(file_path, "r") as file:
            reader = csv.DictReader(file, delimiter=";")
            rows = [MT4CsvRow.from_csv_row(row) for row in reader]

            return AccountRequest(
                user_identifier=config_values["user"],
                account_number=account_number,
                trades=[tr for row in rows if (tr := process_row_as_trade(row)) is not None],
                transactions=[tx for row in rows if (tx := process_row_as_transaction(row)) is not None],
            )

    except Exception as e:
        logging.error(f"Error reading file {file_path}: {e}", exc_info=True)
        return None

def process_row_as_trade(row: MT4CsvRow) -> Trade | None:
    """Processes a mt4 csv row as a trade"""
    if row.type == TradeType.UNKNOWN:
        return None

    return Trade(
        trade_id=row.id,
        product=row.symbol,
        trade_type=row.type.value,
        trade_open_time=row.open_time,
        lot_size=row.lot_size,
        open_price=row.open_price,
        net_profit=row.net_profit,
        close_price=row.close_price,
        trade_close_time=row.close_time,
        stop_loss=row.stop_loss,
        take_profit=row.take_profit,
        trade_platform="METATRADER4"
    )

def process_row_as_transaction(row: MT4CsvRow) -> Transaction | None:
    """Processes a mt4 csv row as a transaction"""
    if row.type != TradeType.UNKNOWN:
        return None

    tr_type = TransactionType.DEPOSIT if row.net_profit > 0 else TransactionType.WITHDRAWAL
    def_str = "Deposit" if tr_type == TransactionType.DEPOSIT else "Withdrawal"
    return Transaction(
        transaction_type=tr_type.value,
        transaction_date=row.close_time,
        name=generate_transaction_name(row.description, def_str),
        amount=row.net_profit,
        transaction_status="COMPLETED"
    )

def generate_transaction_name(comment: str, def_str: str) -> str:
    """Generates a transaction name from an arbitrary mt4 description"""
    match = re.search(r"\[(.*?)]", comment)
    return match.group(1).strip() if match else def_str

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