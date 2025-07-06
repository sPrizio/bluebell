#!/bin/bash

set -euo pipefail

ROOT_FOLDER=$(realpath "$0" | xargs dirname | xargs dirname)
cd "$ROOT_FOLDER" || exit

git fetch -p
git branch -vv | grep ': gone]' | awk '{print $1}' | xargs -r git branch -D

remote_tags=$(git ls-remote --tags origin | awk '{print $2}' | sed 's#refs/tags/##' | sed 's/\^{}//')

local_tags=$(git tag)

for tag in $local_tags; do
  if ! grep -Fxq "$tag" <<< "$remote_tags"; then
    git tag -d "$tag"
  fi
done