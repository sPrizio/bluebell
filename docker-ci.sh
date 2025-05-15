#!/bin/bash
set -e

ENV=${1:-prod}
ENV_FILE=".env.$ENV"

if [ ! -f "$ENV_FILE" ]; then
  echo "$ENV_FILE not found, generating default version..."
  touch "$ENV_FILE"
fi

echo "Using environment file: $ENV_FILE"
echo "Running docker compose build only"
docker compose --env-file "$ENV_FILE" build
