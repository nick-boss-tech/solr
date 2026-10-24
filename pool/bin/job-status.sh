#!/bin/bash
# usage: job-status.sh <log dir> <job>
# Prints the job state, each finished step, and the current step's run status. Never waits.
set -u
D=$1/$2
[ -d "$D" ] || { echo "no such job: $2"; exit 1; }
if [ -f "$D/state" ]; then
  state=$(cat "$D/state")
elif kill -0 "$(cat "$D/pid" 2>/dev/null)" 2>/dev/null; then
  state=running
else
  state="died"
fi
echo "job $2: $state"
[ -s "$D/steps.tsv" ] && awk -F'\t' '{print "  " $1 "\trc=" $2 "\t" $3 "\t" $4}' "$D/steps.tsv"
n=$(wc -l < "$D/steps.tsv" 2>/dev/null || echo 0)
current=$(sed -n "$((n+1))p" "$D/steps.txt" 2>/dev/null | cut -d'|' -f1)
[ "$state" = running ] && [ -n "$current" ] && echo "  current: $current $(bash "$(dirname "$0")/run-status.sh" "$D" "$current")"
exit 0
