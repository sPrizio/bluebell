#!/bin/bash
set -e

ENV=${1:-prod}
ENV_FILE=".env.$ENV"

if [ ! -f "$ENV_FILE" ]; then
  echo "$ENV_FILE not found, generating default version..."
  touch "$ENV_FILE"
fi

echo "Using environment file: $ENV_FILE"
export SPRING_PROFILE=$ENV
export FLOWER_PROFILE=$ENV

echo "Loading and exporting environment variables..."
set -a
source "$ENV_FILE"
set +a

echo "Running docker compose build with SPRING_PROFILE=$SPRING_PROFILE and FLOWER_PROFILE=$FLOWER_PROFILE"
docker compose --env-file "$ENV_FILE" build
