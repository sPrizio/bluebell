### MT4 Data Watcher

This middleware utilizes python to watch a specific directory for .csv files. When a file is detected, the script will validate the file and, once validated, will attempt to upload the file to
the ***bluebell*** api. Upon successful uploading, the file(s) will be moved to the `\processed` directory. If any failures occur, files will be moved to the `\failed` directory.

This script is also equipped to clean up after itself.

#### Installation
When installing this data watcher, the `config.ini` needs to be updated with the relevant values. Follow the file for hints on what info to utilize. The entire package can be copied
to any directory, ideally one with administrator privileges and once configured to watch a directory, will begin processing. The OS needs to have a valid installation of python 3.
A requirements file is included in the script if needed.
