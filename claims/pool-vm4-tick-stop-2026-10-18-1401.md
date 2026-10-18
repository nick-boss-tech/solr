# Claim: pool tick stopped at step 0 on vm4, no pool host id (2026-10-18 14:01Z)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: STALE 2026-10-18T14:01Z. Tick stopped at step 0 before any work was claimed. No live work.
- Job or assignment: none.
- Slice: none taken.
- Started: 2026-10-18T14:01Z (UTC).

Heartbeat: 2026-10-18T14:01Z

Result: 2026-10-18T14:01Z: `pool/cron-prompt.md` step 0 could not run. `~/.pool-host` does not exist for user `claude` (`HOME=/home/claude`), so `POOL_HOST` is empty. The operator's instruction for this tick says to stop and record in the claim file when the prompt's file cannot be read, so this file records it. The host id `vm4` from the operator's instruction was not written to `~/.pool-host` on this VM. The 12:03Z pass (`claims/pool-vm4-merged-pr-followup-promises-round-1.md`) took `vm4` from the operator instruction; this tick does not, because this tick's instruction names stopping as the fallback.

Lock mismatch, not checked this tick: step 3 of the cron prompt and `pool/bin/sweep-run.sh` use `/workspace/tools/.gradle-$POOL_HOST.lock` (`.gradle-vm4.lock`). The operator names `/workspace/tools/.vm4-gradle.lock` as the lock for this host and for manual builds. Both files exist on this VM. No build was run.

STALE: tick stopped at step 0, no work taken. Not a live claim.

Next: the operator sets `~/.pool-host` to `vm4` on this VM (or gives an explicit go-ahead to create it), and decides which Gradle lock the cron tick uses.
Result: 2026-10-18T14:21Z: tick stopped at cron step 0, same cause as 14:01Z. `/home/claude/.pool-host` is still missing and `~/.pool-env` is absent. `/workspace/tools/host-name` reads `vm4`. The file was not created. Both `/workspace/tools/.vm4-gradle.lock` and `/workspace/tools/.gradle-vm4.lock` were free at this tick (`flock -n` succeeded). Disk on `/`: 4.6G free, 91% inodes free. No work claimed, no build run, no source change. Still STALE, not a live claim. Next: the operator sets `~/.pool-host` to `vm4` (or gives an explicit go-ahead to create it), and decides whether the tick uses `.vm4-gradle.lock` (operator's lock) or `.gradle-vm4.lock` (what `sweep-run.sh` takes).
Result: 2026-10-18T14:26Z: tick stopped at cron step 0 again. `/home/claude/.pool-host` is still missing (`ls` reports no such file). The file was not created. The live vm4 claim `claims/pool-skip-repro-14159-round-1.md` was not resumed, because the tick cannot confirm its host id. No work taken, no build run, no source change. Still STALE, not a live claim.
