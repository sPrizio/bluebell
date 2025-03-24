@echo off
setlocal enabledelayedexpansion

set ROOT_FOLDER=%~dp0..
cd /d %ROOT_FOLDER% || exit

if "%1"=="" (
    echo Usage: %~nx0 [dev^|staging^|prod]
    exit /b 1
)

set "env_name=%1"

if /i "%env_name%" neq "dev" if /i "%env_name%" neq "staging" if /i "%env_name%" neq "prod" (
    echo Invalid argument. Allowed values: dev, staging, prod
    exit /b 1
)

set "src_file=config\.env.%env_name%"
set "dest_file=.env"

if not exist "%src_file%" (
    echo Error: Source file %src_file% does not exist.
    exit /b 1
)

copy /Y "%src_file%" "%dest_file%" >nul

echo .env file has been updated with %src_file%
