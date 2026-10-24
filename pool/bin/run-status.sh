#!/bin/bash
# usage: run-status.sh <log dir> <run name>
# Prints one line: not-started, queued, running, died, or done rc=N. Never waits.
set -u
L=$1; name=$2
POOL_HOST=$(sed -n 's/^- Host id: `\([^`]*\)`.*/\1/p' /workspace/AGENTS.md | head -n1)
LOCK=/workspace/tools/.gradle-$POOL_HOST.lock
if [ -f "$L/$name.log" ] && tail -n1 "$L/$name.log" | grep -q '^rc='; then
  echo "done $(tail -n1 "$L/$name.log")"
elif [ -f "$L/$name.log" ] && grep -q 'build not started' "$L/$name.log"; then
  echo "not-started (lock wait timed out)"
elif [ -f "$L/$name.start" ]; then
  if flock -n "$LOCK" true 2>/dev/null; then echo died; else echo running; fi
elif [ -f "$L/$name.log" ]; then
  echo queued
else
  echo not-started
fi
