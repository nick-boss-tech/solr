#!/bin/bash
# UserPromptSubmit: record when this turn began, for wake-required.sh.
HOST=$(sed -n 's/^- Host id: `\([^`]*\)`.*/\1/p' /workspace/AGENTS.md | head -n1)
[ -n "$HOST" ] || exit 0
mkdir -p /workspace/tools/wake
date +%s > /workspace/tools/wake/$HOST.turn
exit 0
