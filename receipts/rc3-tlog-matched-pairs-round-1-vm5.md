# Receipt: matched NFS vs local-disk runs for the RC3 linking failures (vm5, 2026-10-26)

Question: do the four failing tests fail only on NFS, where `/workspace` refuses hard links (EMLINK), and pass on local disk at the same seed?

Each NFS and local run used the same seed and host. Only the test temp directory differed (`/workspace` NFS vs `/tmp` local). Logs stay on vm5 at `/workspace/smoke-rc3/tlog-repro/`.

| Test | Seed | NFS (`/workspace`) | Local disk (`/tmp`) | EMLINK lines on NFS |
|---|---|---|---|---|
| `TestTlogReplica.testOnlyLeaderIndexes` | `3635CC89D23D0929` | pass | pass | 0 |
| `TestTlogReplica.testRecovery` | `3635CC89D23D0929` | fail | pass | 4 |
| `TestTlogReplica.testKillLeader` (class run) | `3635CC89D23D0929` | fail | pass | 4 |
| `TestTlogReplica.testOnlyLeaderIndexes` | `5EC47295A32A8342` (run 005) | fail: "Some replicas are not in sync with leader" | pass | 21 |
| `TestTlogReplica.testKillLeader` (alone) | `5EC47295A32A8342` | fail: "Expected collection to be 1x2" | pass | 4 |
| `TestTlogReplica.testRecovery` (alone) | `5EC47295A32A8342` | fail: "Replica didn't recover" | pass | 4 |
| `TestCoordinatorRole.testNRTRestart` (alone) | `787C6229591C6F1E` | fail: "Pull jetty replicas didn't become active in time" | pass (class run, `coordrole-local`) | 4 |

Notes:
- The `3635CC89D23D0929` seed does not reproduce `testOnlyLeaderIndexes`; the failure is seed-dependent. The first pair was a wrong-seed run, kept as a control.
- The NFS logs show the mechanism: replica `t1` logs "Replication for recovery failed" on each retry, each caused by the hard link in `IndexFetcher.java:1184`.

## Probe: copy instead of hard link (NFS, seed `5EC47295A32A8342`)

Change: `Files.createLink` replaced with `Files.copy` in `IndexFetcher.java:1184`, worktree reverted afterwards.
Result: `testOnlyLeaderIndexes` passes on NFS, 0 EMLINK lines.

## Gate with the copy fallback (commit `fdb785484c6`, patch-gate)

| Run | Seed | Result | EMLINK |
|---|---|---|---|
| `TestTlogReplica` (full class) | `5EC47295A32A8342` | pass (14 run, 2 skipped) | 0 |
| `TestCoordinatorRole` (full class) | `787C6229591C6F1E` | pass (9 tests) | 0 |
| Replication group (`TestReplicationHandler`, `TestReplicationHandlerBackup`, `IndexFetcherPacketProtocolTest`, `ReplicationHandlerRecoveryAuthIntegrationTest`) | `5EC47295A32A8342` | pass (46 run, 25 skipped) | 0 |
| `tidy`, `:solr:core:check -x test` | - | pass | - |

Limits: one seed per test on each side; no unit test was added for the fallback; the gate used fixed seeds.
