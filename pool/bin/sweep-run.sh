#!/bin/bash
# usage: sweep-run.sh <worktree> <log dir> <run name> <gradle args...>
# Runs one Gradle build under this host's lock, writes <name>.log, <name>.headroom.log and copies new test XML to <name>.xml/.
set -u
W=$1; L=$2; name=$3; shift 3
LOCK=/workspace/tools/.gradle-$(hostname -s).lock
mkdir -p "$L"
{ echo "pre $(date -u +%FT%TZ) | $(df -h / | tail -1 | awk '{print $4" free"}') | $(df -i / | tail -1 | awk '{print $4" ifree"}')"; } > "$L/$name.headroom.log"
cd "$W" || exit 1
flock -n "$LOCK" ./gradlew "$@" > "$L/$name.log" 2>&1
rc=$?
if [ "$rc" -eq 1 ] && ! grep -q "BUILD" "$L/$name.log"; then
  echo "lock held on $LOCK; build not started" > "$L/$name.log"
  echo "rc=lock-held" >> "$L/$name.headroom.log"
  exit 2
fi
echo "rc=$rc end $(date -u +%FT%TZ) | $(df -h / | tail -1 | awk '{print $4" free"}') | $(df -i / | tail -1 | awk '{print $4" ifree"}')" >> "$L/$name.headroom.log"
mkdir -p "$L/$name.xml"
find "$W" -path '*/build/test-results/test/*.xml' -newer "$L/$name.headroom.log" -exec cp {} "$L/$name.xml/" \;
echo "rc=$rc" >> "$L/$name.log"
exit $rc
