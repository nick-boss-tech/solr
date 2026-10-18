# Claim: pool tick halted at step 0 on vm4, no pool host id (2026-10-18 14:56Z)

- Claimant host: vm4.
- Status: STALE 2026-10-18T14:56Z. Tick halted at step 0 before any work was claimed. No live work.
- Job or assignment: none.
- Slice: none taken.
- Started: 2026-10-18T14:56Z (UTC).

Result: 2026-10-18T14:56Z: cron prompt step 0 could not run. `~/.pool-host` does not exist for user `claude` (`HOME=/home/claude`), and `~/.pool-env` is missing too, so `POOL_HOST` and the host JAVA_HOME and GRADLE_USER_HOME cannot be read. `/workspace/tools/host-name` reads `vm4`, but the operator's instruction for this tick says to stop and record in the claim file when the prompt's file cannot be read, so this file records the stop. No build ran, no Gradle lock was taken, and no assignment or gate was claimed. Earlier halt records: `claims/pool-vm4-tick-stop-2026-10-18-1401.md`, `claims/pool-vm4-tick-halt-host-id-2026-10-18.md`.

STALE: 2026-10-18T14:56Z: halt record only, not a live claim. Restore `~/.pool-host` with the value `vm4` and `~/.pool-env` on this VM's local disk before the next tick.
