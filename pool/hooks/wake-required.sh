#!/bin/bash
# Stop: while OVERNIGHT is on in pool/cron-prompt.md, a turn may not end without a wake scheduled in it.
input=$(cat)
[ "$(printf '%s' "$input" | jq -r '.stop_hook_active // false')" = true ] && exit 0
HOST=$(sed -n 's/^- Host id: `\([^`]*\)`.*/\1/p' /workspace/AGENTS.md | head -n1)
[ -n "$HOST" ] || exit 0
MODE=$(sed -n 's/^OVERNIGHT: *\([a-z]*\).*/\1/p' /workspace/worktrees/pool-$HOST/pool/cron-prompt.md 2>/dev/null | head -n1)
[ "$MODE" = on ] || exit 0
turn=$(cat /workspace/tools/wake/$HOST.turn 2>/dev/null || echo 0)
sched=$(cat /workspace/tools/wake/$HOST.scheduled 2>/dev/null || echo 0)
[ "$sched" -ge "$turn" ] && exit 0
echo "OVERNIGHT is on and no wake was scheduled this turn. Call mcp__user-input__schedule_resume now: in 2 minutes if this tick pushed work, 15 minutes if it found nothing. The note names the mode, your live claim (or none) and the next step. Then end the turn." >&2
exit 2
