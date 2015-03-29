#!/usr/bin/env bash
for i in {1..102}; do
  echo "Downloading $i"
  USER="apache"
  REPO="spark"
  curl -H "Authorization: token $1" "https://api.github.com/repos/$USER/$REPO/commits?per_page=100&page=$i" > /tmp/github/commits$i.json
done
