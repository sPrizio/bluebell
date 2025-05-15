#!/bin/bash
set -e

ENV=${1:-prod}
ENV_FILE=".env.$ENV"
TAG=${2:-latest}

if [ ! -f "$ENV_FILE" ]; then
  echo "$ENV_FILE not found, generating default version..."
  touch "$ENV_FILE"
fi

echo "Using environment file: $ENV_FILE"
export SPRING_PROFILE=$ENV
export FLOWER_PROFILE=$ENV
export TAG=$TAG

echo "Loading and exporting environment variables..."
set -a
source "$ENV_FILE"
set +a

echo "Building docker image with SPRING_PROFILE=$SPRING_PROFILE and FLOWER_PROFILE=$FLOWER_PROFILE for version $TAG"
docker compose --env-file "$ENV_FILE" build
