# Claim: pool-vm6-gate-SOLR-14649-phase-sha512-signatures

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: ACTIVE. Job: `gates/SOLR-14649-phase-sha512-signatures.md`. Mark DONE with the receipt path when the gate lands.
- Job or assignment: `gates/SOLR-14649-phase-sha512-signatures.md`.
- Slice: the whole gate job.
- Capability tags used: gate.
- Started: 2026-10-23T15:31:53Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: SOLR-14649-phase-sha512-signatures@800e91188e0d9f975b9096c9413915ea18713b23.
- Links: Ticket or parent: SOLR-14649 (branch `solr-14649-submit`). Category: see pool/work-map.md. Siblings: none known. Supersedes: none.
- Preflight: `/workspace/gates/logs/vm6/preflight/SOLR-14649-phase-sha512-signatures/`, exit 3 at 2026-10-23T15:30Z. Tidy changed one file, `solr/core/src/test/org/apache/solr/filestore/TestDistribFileStore.java` (formatting only). The patch is `tidy.patch` in that directory. It is applied as a gate repair under WORKFLOW.md "Gate repairs".

Heartbeat: 2026-10-23T16:46:00Z

Result: 2026-10-23T16:41Z: relaunch `s14649-head-r2` refused at compile, rc=4, by the inode guard (`pool/bin/inode-guard.py`, commit 6b4decacc2, "stop Gradle at 15 percent free inodes"). Cause: `/workspace` is an NFS mount (127.0.0.1:/agents/5yjgb5j855/workspace). `statvfs` reports 0 inodes there, and the guard refuses unavailable counters. `/` has 479583 of 524288 inodes free (91%), so the guard does not see `/`. Gradle has not run for the head. No files were deleted. Next: the owner decides how the guard treats NFS paths. Until then the head run (compile, cleanTest, focused tests, check) cannot run on this host. Proof leg (`s14649-proof`) ran before the guard existed.

Result: 2026-10-23T15:49Z: proof leg (`s14649-proof`) 4 of 4 TestCryptoKeys tests failed as expected; receipt not yet written. Head job `s14649-head` died at 15:53 during its compile step, before any test ran, after the tick worktree's `pool/bin` scripts were replaced mid-run at 15:53:55.

Result: 2026-10-23T16:45Z: inode guard fixed on coordination (`d3614ec136`: a mount reporting zero inode counters is checked through the local root; tests 11 of 11). Head run relaunched as `s14649-head-r3` from a frozen copy of pool/bin at `/workspace/gates/snapshots/s14649-head-r3/bin`, so branch updates cannot change scripts mid-run. Head: `25589eb5e6`.
