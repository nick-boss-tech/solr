# Claim: pool tick halted at step 0 on vm4, no pool host id (2026-10-18 15:00Z)

- Claimant host: vm4, matching a file in `hosts/`.
- Status: STALE 2026-10-18T15:00Z. Tick stopped at step 0 before any work was claimed. No live work.
- Job or assignment: none.
- Slice: none taken.
- Started: 2026-10-18T15:00Z (UTC).

Result: 2026-10-18T15:00Z: `pool/cron-prompt.md` step 0 could not run. `~/.pool-host` and `~/.pool-env` are both missing for user `claude` (`HOME=/home/claude`), so `POOL_HOST` is empty. The cron prompt says to exit and record nothing. The operator's instruction for this tick says to stop and record the reason in a claim file on coordination, so this file records it. No file was created on the host, no Gradle lock was taken, no build ran, and no assignment or gate was touched. Disk on `/`: 4.6G free, 9% inodes used. Earlier halt records: `claims/pool-vm4-tick-halt-host-id-2026-10-18-1456.md`, `claims/pool-vm4-tick-stopped-no-pool-host.md`.

STALE: tick stopped at step 0, no work taken. Not a live claim. The next tick needs `~/.pool-host` set to `vm4` and `~/.pool-env` on this VM's local disk, or an explicit go-ahead to create them.
