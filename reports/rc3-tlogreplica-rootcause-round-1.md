# TestTlogReplica failures: reproduction at the RC3 tip and at apache/main (unit 1, round 1)

Verdict so far: reproduces at both RC3 and apache/main. Root cause not established in this round; the next step is the code path below.

## Runs (focused, `./gradlew :solr:core:test --tests org.apache.solr.cloud.TestTlogReplica --no-daemon`, host lock, vm5, JDK 21.0.12.1, aarch64)

| Tree | Commit | Logs | Result | Failing methods | Seed |
|---|---|---|---|---|---|
| RC3 tip | 749712b7194 | `/workspace/smoke-rc3/tlog-repro/rc3-tip-run1.log` | 14 tests, 2 failed, 2 skipped, 9m 05s | testKillLeader, testRecovery | 3635CC89D23D0929 |
| apache/main | 13c693d805f | `/workspace/smoke-rc3/tlog-repro/main-run1.log` | 14 tests, 3 failed, 2 skipped, 9m 05s | testKillLeader, testRecovery, testOnlyLeaderIndexes | 9D6BB87C4F145405 |

The XML for each run is under `solr/core/build/test-results/test/` in `/workspace/worktrees/tlog-rc3-tip` and `/workspace/worktrees/tlog-main`.

The RC3 smoke loop's runs showed the same messages for these methods. The loop's four methods in every run were `testKillLeader`, `testRecovery`, `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` and `TestCoordinatorRole.testNRTRestart`, with `testOnlyLeaderIndexes` in four of five runs (see `reports/rc3-smoke-loop-round-1.md`). Here `testOnlyLeaderIndexes` failed on main and passed on the RC3 tip, so it is intermittent.

## Observed state (testKillLeader, main, seed 9D6BB87C4F145405)

After the leader is killed, the collection `tlog_replica_test_kill_leader` ends with:

- `core_node2` (TLOG, `127.0.0.1:42099_solr`): `"state":"recovering"`, not leader.
- `core_node4` (TLOG, `127.0.0.1:44675_solr`): `"state":"active"`, `"leader":"true"`.
- Both nodes live. The shard is `active`, with 2 TLOG replicas.

The test waits for 1x2 with both replicas active and times out. So the surviving replica does not leave `recovering` after the leader change. The same timeout appears in the other failing methods (their messages: "Replica didn't recover", "Some replicas are not in sync with leader").

## Not done in this round

- Root cause: not established. The assignment's starting hypothesis (routing and timeout around tlog replicas) is only a hypothesis. The next steps are reading the TLOG recovery path in `solr/core` (`RecoveryStrategy` and the leader-election hook for TLOG replicas) and the test's wait conditions.
- Units 2 and 3 of the assignment are not claimed here.
- Repeated runs with the loop's own seeds were not done. The loop's seeds were recorded only in some run logs.
- No fix branch and no gate job.
