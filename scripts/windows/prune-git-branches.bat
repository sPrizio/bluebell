@echo off
setlocal

set ROOT_FOLDER=%~dp0..
cd /d %ROOT_FOLDER% || exit

git fetch -p
for /f "tokens=1" %%A in ('git branch -vv ^| findstr ": gone"') do git branch -D %%A

git ls-remote --tags origin > remote_tags.tmp

for /f %%T in ('git tag') do (
    findstr /C:"refs/tags/%%T" remote_tags.tmp >nul
    if errorlevel 1 (
        git tag -d %%T
    )
)

del remote_tags.tmp