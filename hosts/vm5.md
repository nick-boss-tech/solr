# Host: vm5

Pool Linux host. Capabilities and rules are in `hosts/pool-linux.md`.

Heartbeat: 2026-10-24T07:46Z

Heartbeat: 2026-10-24T07:55Z

RC3 smoke loop (rc3-smoke-loop-round-1): run-001 started 2026-10-24T08:08Z, log /workspace/smoke-rc3/runs/run-001.log, results /workspace/smoke-rc3/runs/results.tsv. Heartbeat: 2026-10-24T08:08Z

Heartbeat: 2026-10-24T09:12Z (rc3-smoke-loop-round-1: run-001 in unit tests, 1h into the run)

Heartbeat: 2026-10-24T10:17Z (rc3-smoke-loop-round-1: run-001 BUILD FAILED after 1h44m, 6 failures of 5898 in solr:core: RecoveryAfterSoftCommitTest, TestTlogReplica, CoreAdminHandlerTest, TestCoordinatorRole; tmp kept at /workspace/smoke-rc3/tmp/run-001)

Heartbeat: 2026-10-24T11:05Z (rc3-smoke-loop-round-1: run-002 in unit tests, 47 min in; run-001 failed, 6 of 5898)

Heartbeat: 2026-10-24T11:53Z (rc3-smoke-loop-round-1: run-002 in unit tests, 1h35m in; test JVMs busy, no new XML since 10:44; run-001 failed, 6 of 5898)

Heartbeat: 2026-10-24T12:42Z (rc3-smoke-loop-round-1: run-001 fail 6 of 5898; run-002 fail 4 of 7374 with the same methods TestTlogReplica.testKillLeader, testRecovery, CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure, TestCoordinatorRole.testNRTRestart under different seeds; run-003 in documentation checks)
