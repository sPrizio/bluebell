#!/bin/bash

ROOT_FOLDER=$(realpath "$0" | xargs dirname | xargs dirname)
cd "$ROOT_FOLDER" || exit

git fetch -p
git branch -vv | grep ': gone' | awk '{print $1}' #| xargs git branch -D