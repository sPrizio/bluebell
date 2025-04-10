#!/bin/bash

# Ensure a valid argument is provided
if [[ "$#" -ne 1 ]]; then
    echo "Missing argument. Requires one of: dev, staging, prod"
    exit 1
fi

ENV_TYPE="$1"

# Validate the argument using a regex match
if ! [[ "$ENV_TYPE" =~ ^(dev|staging|prod)$ ]]; then
    echo "Error: Invalid environment type. Allowed values: dev, staging, prod."
    exit 1
fi

SOURCE_FILE=".env.${ENV_TYPE}"
DEST_FILE=".env"

# Check if the source file exists
if [[ ! -f "$SOURCE_FILE" ]]; then
    echo "Error: Source file '$SOURCE_FILE' not found."
    exit 1
fi

# Copy the contents to .env, creating or overwriting it
cp "$SOURCE_FILE" "$DEST_FILE"

echo "Successfully copied $SOURCE_FILE to $DEST_FILE."
