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
