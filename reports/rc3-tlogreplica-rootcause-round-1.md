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

The test (`TestTlogReplica.doReplaceLeader`, `solr/core/src/test/org/apache/solr/cloud/TestTlogReplica.java` lines 438 to 487) stops the leader jetty, waits for a new leader (`waitForLeaderChange`), adds and commits a document, then restarts the old leader with `leaderJetty.start()` and waits for `clusterShape(1, 2)` with the message "Expected collection to be 1x2" (line 484). The failure is at that last wait, after the restart, not during the leader kill.

The captured state at that point, for `tlog_replica_test_kill_leader`:

- `core_node2` (TLOG, `127.0.0.1:42099_solr`): `"state":"recovering"`, not leader.
- `core_node4` (TLOG, `127.0.0.1:44675_solr`): `"state":"active"`, `"leader":"true"`.
- Both nodes live. The shard is `active`, with 2 TLOG replicas.

So the replica that rejoins stays in `recovering`, and the shard never reaches 1x2 active.

Code read, not yet traced to a cause (`solr/core/src/java/org/apache/solr/cloud` on main):

- `ZkController.java` lines 1504 to 1512: a TLOG replica that is not leader skips the startup `recoverFromLog()` path and copies over old updates instead.
- `ZkController.java` lines 1834 to 1855: a non-leader that needs recovery calls `doRecovery`; a TLOG replica whose term is lower than the leader's also goes through `doRecovery`.
- `RecoveryStrategy.java` line 571: "we temporary ignore peersync for tlog replicas" (`firstTime = replicaType != TLOG`). TLOG recovery therefore follows a different path from NRT. This is the likeliest place the rejoin stalls, and it is a hypothesis until a probe confirms it.

## Not done in this round

- Root cause: not established. The assignment's starting hypothesis (routing and timeout around tlog replicas) is only a hypothesis. The next steps are reading the TLOG recovery path in `solr/core` (`RecoveryStrategy` and the leader-election hook for TLOG replicas) and the test's wait conditions.
- Units 2 and 3 of the assignment are not claimed here.
- Repeated runs with the loop's own seeds were not done. The loop's seeds were recorded only in some run logs.
- No fix branch and no gate job.

## Status at 2026-10-25 00:10 UTC

- Hypothesis (unconfirmed): the TLOG rejoin stalls because TLOG replicas skip peersync (`RecoveryStrategy.java` line 571, `firstTime = replicaType != Replica.Type.TLOG`), so the rejoining replica never moves past `recovering`.
- A probe would decide it: a focused `testKillLeader` run with peersync enabled for TLOG on a scratch branch, under the host lock, with the same seed. A pass would support the hypothesis; a fail would rule it out. That probe changes recovery code, which is an implementation step, so it needs a decision from the owner or the assignment before it runs.
- Phone-agent reply on `assignments/vm5-to-phone-agent-tarball-status-round-1.md`: none yet.
