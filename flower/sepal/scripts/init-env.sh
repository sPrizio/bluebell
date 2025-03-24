#!/bin/bash

set -e

ROOT_FOLDER=$(realpath "$0" | xargs dirname | xargs dirname)
cd "$ROOT_FOLDER" || exit

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 [dev|staging|prod]"
    exit 1
fi

env_name=$1
case "$env_name" in
    dev|staging|prod)
        src_file="config/.env.$env_name"
        dest_file=".env"
        ;;
    *)
        echo "Invalid argument. Allowed values: dev, staging, prod"
        exit 1
        ;;
esac

if [ ! -f "$src_file" ]; then
    echo "Error: Source file $src_file does not exist."
    exit 1
fi

cp "$src_file" "$dest_file"

echo ".env file has been updated with $src_file"
