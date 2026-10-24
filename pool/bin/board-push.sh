#!/bin/bash
# board-push.sh: commit files to a board branch on top of its current remote tip, then push.
# No checkout, no rebase: the commit is built with a temporary index on the fetched tip.
#
# usage: board-push.sh [--branch <name>] [--dry-run] -m "<commit message>" <repo-path>=<local-file> ...
# example: board-push.sh -m "SOLR-18541: claim vm2 leftovers" claims/vm2-x.md=/tmp/vm2-x.md
#
# Default branch: coordination-ui. Author: Nick Shanin, no trailers. The token is read from
# GITHUB_TOKEN in /workspace/.env (the shell's copy can be stale) and is never printed.
# Exit 0 pushed (or dry-run built); 1 push raced 4 times; 2 usage; 6 no token.
set -euo pipefail

REPO=${BOARD_REPO:-/workspace/solr}
BRANCH=coordination-ui
MSG=""
DRY=0
while [ $# -gt 0 ]; do
  case "$1" in
    --branch) BRANCH=$2; shift 2 ;;
    --dry-run) DRY=1; shift ;;
    -m) MSG=$2; shift 2 ;;
    *) break ;;
  esac
done
[ -n "$MSG" ] && [ $# -ge 1 ] || { echo "usage: board-push.sh [--branch <name>] [--dry-run] -m <message> <repo-path>=<local-file>..." >&2; exit 2; }
for pair in "$@"; do
  [ "${pair#*=}" != "$pair" ] || { echo "board-push: expected <repo-path>=<local-file>, got: $pair" >&2; exit 2; }
  [ -f "${pair#*=}" ] || { echo "board-push: no such file: ${pair#*=}" >&2; exit 2; }
done

BOARD_TOKEN=$(grep -m1 '^GITHUB_TOKEN=' /workspace/.env | cut -d= -f2- | tr -d '"\r')
[ -n "$BOARD_TOKEN" ] || { echo "board-push: no GITHUB_TOKEN in /workspace/.env" >&2; exit 6; }
export BOARD_TOKEN
CRED='credential.helper=!f() { echo username=x-access-token; echo "password=$BOARD_TOKEN"; }; f'
ID=(-c user.name="Nick Shanin" -c user.email="nick.boss.us@gmail.com")

cd "$REPO"
IDX=$(mktemp -u)
trap 'rm -f "$IDX"' EXIT

for attempt in 1 2 3 4; do
  timeout 100 git -c "$CRED" fetch -q origin "$BRANCH"
  TIP=$(git rev-parse FETCH_HEAD)
  GIT_INDEX_FILE=$IDX git read-tree "$TIP"
  for pair in "$@"; do
    p=${pair%%=*}; f=${pair#*=}
    GIT_INDEX_FILE=$IDX git update-index --add --cacheinfo "100644,$(git hash-object -w -- "$f"),$p"
  done
  TREE=$(GIT_INDEX_FILE=$IDX git write-tree)
  C=$(git "${ID[@]}" commit-tree "$TREE" -p "$TIP" -m "$MSG")
  if [ "$DRY" -eq 1 ]; then
    echo "dry run: $C on top of $(git rev-parse --short "$TIP") (not pushed)"
    git show --stat --format= "$C"
    exit 0
  fi
  if timeout 100 git -c "$CRED" push -q origin "$C:refs/heads/$BRANCH" 2>/dev/null; then
    echo "pushed $(git rev-parse --short "$C") to $BRANCH"
    exit 0
  fi
  echo "board-push: push raced, retrying ($attempt)" >&2
done
exit 1
