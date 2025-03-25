@echo off
setlocal

set ROOT_FOLDER=%~dp0..
cd /d %ROOT_FOLDER% || exit

git fetch -p
for /f "tokens=1" %%A in ('git branch -vv ^| findstr ": gone"') do git branch -D %%A