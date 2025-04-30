# 🐍 Watcher Runner

This project runs two directory-watching scripts and periodically sends health check API calls. It is designed to run indefinitely as a background process on Windows or Linux.

## 📦 Project Structure

```
stem/
├── mt4/                     # Main module
│   ├── account/             # Watches and upload acount transactions & trades
│   │   ├── account_data_watcher.py
│   │   ├── config.ini
│   │   └── README.md
│   ├── libs/               # Shared libraries/utilities
│   │   └── util.py
│   └── price/              # Watches and uploads market price data
│       ├── config.ini
│       ├── market_price_watcher.py
│       └── README.md
├── config.ini              # main.py configuration
├── main.py                 # main program entry point
├── README.md               # What you're currently reading
└── requirements.txt        # Python dependencies
```

## 🚀 Getting Started

### 1. Create installation directory and copy files

```bash
cd <installation_directory> # example : cd bluebell
mkdir stem
cd stem
# copy contents of stem into your newly created 'stem directory'
```

### 2. Create and Activate a Virtual Environment

**Linux/macOS:**
```bash
python3 -m venv venv
source venv/bin/activate
```

**Windows:**
```cmd
python -m venv venv
venv\Scripts\activate
```

### 3. Install Dependencies

```bash
pip install -r requirements.txt
```

## 🔧 Building an Executable with PyInstaller

### 4. Install PyInstaller (if not installed globally)

```bash
pip install pyinstaller
```

### 5. Build the Executable

```bash
pyinstaller --onefile main.py
```

This will create a standalone executable in the `dist/` folder:
- On **Windows**: `dist/main.exe`
- On **Linux**: `dist/main`

### 6. Run the Executable

```bash
./dist/main        # Linux
dist\main.exe      # Windows
```

## ⚙️ Deploying as a Background Process

- **Windows**: Use Task Scheduler or `nssm` to run `main.exe` at startup.
- **Linux**: Create a `systemd` service pointing to the compiled executable.

## 📝 Notes

- The executable includes Python and all dependencies — no Python installation is needed on the target machine.
- To regenerate `requirements.txt` after installing new packages:

```bash
pip freeze > requirements.txt
```
