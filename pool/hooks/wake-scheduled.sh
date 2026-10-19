#!/bin/bash
# PostToolUse on mcp__user-input__schedule_resume: record that this turn scheduled a wake.
HOST=$(sed -n 's/^- Host id: `\([^`]*\)`.*/\1/p' /workspace/AGENTS.md | head -n1)
[ -n "$HOST" ] || exit 0
mkdir -p /workspace/tools/wake
date +%s > /workspace/tools/wake/$HOST.scheduled
exit 0
