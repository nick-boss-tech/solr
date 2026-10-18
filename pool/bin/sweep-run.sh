#!/bin/bash
# usage: sweep-run.sh <worktree> <log dir> <run name> <gradle args...>
# Runs one Gradle build under this host's lock. Host id, JAVA_HOME and GRADLE_USER_HOME are read from /workspace/AGENTS.md.
set -u
W=$1; L=$2; name=$3; shift 3
agents_value() { sed -n "s/^- $1: \`\([^\`]*\)\`.*/\1/p" /workspace/AGENTS.md 2>/dev/null | head -n1; }
POOL_HOST=$(agents_value 'Host id')
[ -n "$POOL_HOST" ] || { echo "missing Host id line in /workspace/AGENTS.md" >&2; exit 3; }
JAVA_HOME=$(agents_value JAVA_HOME); JAVA_HOME=${JAVA_HOME:-/workspace/jdk/jdk-21.0.12.1+1}
GRADLE_USER_HOME=$(agents_value GRADLE_USER_HOME); GRADLE_USER_HOME=${GRADLE_USER_HOME:-/workspace/gradle-home}
export JAVA_HOME GRADLE_USER_HOME PATH="$JAVA_HOME/bin:$PATH"
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
