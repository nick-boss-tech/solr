#!/bin/bash
# usage: gate-preflight.sh <owner/repo URL> <full commit SHA> <log dir>
# Runs tidy on a clean clone of the head before a gate job is claimed.
# Exit 0: tidy leaves the tree unchanged. Exit 3: tidy changes files, so the gate would be GATE INVALID.
# Exit 2: fetch failed. Writes preflight.log and, on exit 3, tidy.patch to the log dir.
set -u
URL=$1; SHA=$2; L=$3
mkdir -p "$L"
agents_value() { sed -n "s/^- $1: \`\([^\`]*\)\`.*/\1/p" /workspace/AGENTS.md 2>/dev/null | head -n1; }
POOL_HOST=$(agents_value 'Host id')
[ -n "$POOL_HOST" ] || { echo "missing Host id line in /workspace/AGENTS.md" > "$L/preflight.log"; exit 2; }
JAVA_HOME=$(agents_value JAVA_HOME); GRADLE_USER_HOME=$(agents_value GRADLE_USER_HOME)
export JAVA_HOME GRADLE_USER_HOME PATH="$JAVA_HOME/bin:$PATH"
unset NODE_ENV
AUTH=$(set -a; . /workspace/.env; set +a; printf 'x-access-token:%s' "$GITHUB_TOKEN" | base64 -w0)
W=$(mktemp -d /workspace/tmp/preflight.XXXXXX)
trap 'rm -rf "$W"' EXIT
cd "$W" && git init -q && git -c http.extraheader="AUTHORIZATION: basic $AUTH" fetch -q --depth=1 "$URL" "$SHA" && git checkout -q FETCH_HEAD || { echo "fetch failed for $SHA" > "$L/preflight.log"; exit 2; }
exec 9>"/workspace/tools/.gradle-$POOL_HOST.lock"
flock 9
./gradlew --no-daemon tidy > "$L/preflight-tidy.log" 2>&1 || { echo "tidy task failed, see preflight-tidy.log" > "$L/preflight.log"; exit 2; }
if [ -n "$(git status --porcelain)" ]; then
  git diff > "$L/tidy.patch"
  echo "tidy changed files at $SHA: $(git status --porcelain | wc -l) file(s). Gate would be GATE INVALID." > "$L/preflight.log"
  exit 3
fi
echo "tidy clean at $SHA" > "$L/preflight.log"
exit 0
