#!/bin/bash

ROOT_FOLDER=$(realpath "$0" | xargs dirname | xargs dirname)
cd "$ROOT_FOLDER" || exit

# Check for required env argument
if [ -z "$1" ]; then
    echo "No environment specified"
    echo "Usage: $0 {dev|staging|prod}"
    exit 1
fi

ENV="$1"
ENV_FILE=".env.$ENV"

# Check if the .env file exists
if [ ! -f "$ENV_FILE" ]; then
    echo "$ENV_FILE file not found. This file is required for running Docker"
    exit 1
fi

FORCE_BUILD="${2:-false}"

# Run Docker Compose with or without --build based on FORCE_BUILD
echo "Using $ENV_FILE"
if [ "$FORCE_BUILD" == "true" ]; then
    echo "Rebuilding Docker images..."
    docker compose --env-file "$ENV_FILE" up --build -d
else
    echo "Running Docker based on previous build..."
    docker compose --env-file "$ENV_FILE" up -d
fi
