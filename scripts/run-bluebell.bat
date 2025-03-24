@echo off
setlocal

set ROOT_FOLDER=%~dp0..
cd /d %ROOT_FOLDER% || exit

:: Check for required env argument
if "%~1"=="" (
    echo No environment specified
    echo Usage: %~nx0 {dev^|staging^|prod} [build]
    exit /b 1
)

set ENV=%1
set ENV_FILE=.env.%ENV%

:: Check if the .env file exists
if not exist "%ENV_FILE%" (
    echo %ENV_FILE% not found. This file is required for running Docker
    exit /b 1
)

set FORCE_BUILD=%2
if "%FORCE_BUILD%"=="" set FORCE_BUILD=false

echo Using %ENV_FILE%
:: Run Docker Compose with or without --build based on FORCE_BUILD
if "%FORCE_BUILD%"=="true" (
    echo Rebuilding Docker images...
    docker compose --env-file %ENV_FILE% up --build
) else (
    echo Running Docker based on previous build...
    docker compose --env-file %ENV_FILE% up
)
