# Claim: pool tick stopped at step 0 on vm4, no pool host id

- Claimant host: vm4, matching a file in `hosts/`.
- Status: STALE 2026-10-18T03:36Z. Tick stopped before any work was claimed. No live work.
- Job or assignment: none.
- Slice: none taken.
- Started: 2026-10-18T03:36Z (UTC).

Heartbeat: 2026-10-18T03:36Z

Result: 2026-10-18T03:36Z: `pool/cron-prompt.md` step 0 could not run. `~/.pool-host` is missing for user `claude` (`HOME=/home/claude`), so `POOL_HOST` is empty. The cron prompt says to exit and record nothing. The operator instructed a record in the claim file when the prompt cannot be followed, so this file records it. `/workspace/tools/host-name` reads `vm4`, but the cron prompt reads `~/.pool-host`, not that file. No file was created on the host. The gradle lock named by step 3 (`.gradle-$POOL_HOST.lock`) differs from the operator's lock `/workspace/tools/.vm4-gradle.lock`; not checked at this tick.

STALE: tick stopped at step 0, no work taken. Not a live claim. Next tick needs `~/.pool-host` set to `vm4` by the operator, or an explicit go-ahead to create it.
