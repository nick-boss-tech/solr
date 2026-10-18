#!/bin/bash
# usage: sweep-run.sh <worktree> <log dir> <run name> <gradle args...>
# Runs one Gradle build under this host's lock. Host environment comes from ~/.pool-env.
set -u
W=$1; L=$2; name=$3; shift 3
POOL_HOST=$(cat "$HOME/.pool-host" 2>/dev/null)
[ -n "$POOL_HOST" ] || { echo "missing ~/.pool-host" >&2; exit 3; }
[ -f "$HOME/.pool-env" ] && . "$HOME/.pool-env"
LOCK=/workspace/tools/.gradle-$POOL_HOST.lock
mkdir -p "$L"
exec 9>"$LOCK"
if ! flock -n 9; then
  echo "lock held on $LOCK; build not started" > "$L/$name.log"
  exit 2
fi
{ echo "pre $(date -u +%FT%TZ) | $(df -h / | tail -1 | awk '{print $4" free"}') | $(df -i / | tail -1 | awk '{print $4" ifree"}')"; } > "$L/$name.headroom.log"
cd "$W" || exit 1
touch "$L/$name.start"
./gradlew "$@" > "$L/$name.log" 2>&1
rc=$?
echo "rc=$rc end $(date -u +%FT%TZ) | $(df -h / | tail -1 | awk '{print $4" free"}') | $(df -i / | tail -1 | awk '{print $4" ifree"}')" >> "$L/$name.headroom.log"
mkdir -p "$L/$name.xml"
find "$W" -path '*/build/test-results/test/*.xml' -newer "$L/$name.start" -exec cp {} "$L/$name.xml/" \;
echo "rc=$rc" >> "$L/$name.log"
exit $rc
