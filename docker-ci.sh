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

echo "Loading and exporting environment variables..."
set -a
source "$ENV_FILE"
set +a

IMAGE_NAME="ghcr.io/sprizio/bluebell/bluebell"
echo "Building image $IMAGE_NAME:latest with SPRING_PROFILE=$SPRING_PROFILE and FLOWER_PROFILE=$FLOWER_PROFILE"
docker compose --env-file "$ENV_FILE" -t "$IMAGE_NAME:$TAG" build
