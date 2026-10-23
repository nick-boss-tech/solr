#!/bin/bash
# usage: bg-steps.sh <job> <worktree> <log dir> <steps file>
# Runs each step through sweep-run.sh under the host lock, in order. Start it detached:
#   setsid nohup bash pool/bin/bg-steps.sh <job> <worktree> <log dir> <steps file> >/dev/null 2>&1 </dev/null &
# Steps file: one step per line, "<label>|<gradle args>" or "<label>|<gradle args>|continue".
# Without continue, a failing step stops the job. Writes <log dir>/<job>/{pid,steps.txt,steps.tsv,state}.
set -u
set -f
JOB=$1; W=$2; L=$3; STEPS=$4
D=$L/$JOB
mkdir -p "$D"
echo $$ > "$D/pid"
cp "$STEPS" "$D/steps.txt"
: > "$D/steps.tsv"
rm -f "$D/state"
while IFS='|' read -r label args cont; do
  [ -n "$label" ] || continue
  start=$(date -u +%FT%TZ)
  bash "$(dirname "$0")/sweep-run.sh" "$W" "$D" "$label" $args
  rc=$?
  printf '%s\t%s\t%s\t%s\n' "$label" "$rc" "$start" "$(date -u +%FT%TZ)" >> "$D/steps.tsv"
  # Inode safety refusal is a hard stop, even for an ordinary-failure continue step.
  if [ "$rc" -eq 4 ] || { [ "$rc" -ne 0 ] && [ "${cont:-}" != continue ]; }; then
    echo "stopped at $label rc=$rc" > "$D/state"
    exit "$rc"
  fi
done < "$STEPS"
echo "done" > "$D/state"
