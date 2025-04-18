@echo off
setlocal enabledelayedexpansion

:: Ensure an argument is provided
if "%~1"=="" (
    echo Missing argument. Requires one of: dev, staging, prod
    exit /b 1
)

set "ENV_TYPE=%~1"

:: Validate the argument
if /I not "%ENV_TYPE%"=="dev" if /I not "%ENV_TYPE%"=="staging" if /I not "%ENV_TYPE%"=="prod" (
    echo Error: Invalid environment type. Allowed values: dev, staging, prod.
    exit /b 1
)

set "SOURCE_FILE=.env.%ENV_TYPE%"
set "DEST_FILE=.env"

:: Check if the source file exists
if not exist "%SOURCE_FILE%" (
    echo Error: Source file '%SOURCE_FILE%' not found.
    exit /b 1
)

:: Copy the contents to .env, creating or overwriting it
copy /Y "%SOURCE_FILE%" "%DEST_FILE%" >nul

echo Successfully copied %SOURCE_FILE% to %DEST_FILE%.
