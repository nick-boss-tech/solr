# RC3 smoke loop, round 1: final report (vm5)

The loop ran the Solr 10.1.0 RC3 smoke tester (the command from the vote thread, tip 749712b719) on vm5 from 2026-10-24 08:07 UTC until the owner stood it down at 19:23 UTC. Java: `openjdk version "21.0.12.1" 2026-08-18 LTS` (`/workspace/jdk/jdk-21.0.12.1+1`), recorded as the toolchain in every run log. Host: aarch64, 4 CPUs, 7 GB RAM. Source of record: `runs/results.tsv` and the run logs on vm5.

## Runs

| Run | Start (UTC) | Duration | Outcome | Failures in solr:core |
|---|---|---|---|---|
| 001 | 08:07 | 7850 s | fail (BUILD FAILED) | 6 of 5,898 run |
| 002 | 10:18 | 7493 s | fail | 4 of 7,374 |
| 003 | 12:23 | 7803 s | fail | 7 |
| 004 | 14:33 | - | died at host reboot (~15:05) | none recorded; partial output deleted by mistake |
| 005 | 15:08 | 7499 s | fail | 5 |
| 006 | 17:14 | 7561 s | fail | 5 |
| 007 | 19:19 | - | stopped by owner request (~20:02) | none recorded; 27 result files |

Totals: 5 runs completed to BUILD FAILED, 1 died, 1 stopped. No run passed.

## Failing tests (solr:core)

Run 001 (6): CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestTlogReplica.testKillLeader; TestTlogReplica.testRecovery; TestTlogReplica.testOnlyLeaderIndexes; RecoveryAfterSoftCommitTest.test; TestCoordinatorRole.testNRTRestart.

Run 002 (4): TestCoordinatorRole.testNRTRestart; TestTlogReplica.testKillLeader; TestTlogReplica.testRecovery; CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure.

Run 003 (7): TestCoordinatorRole.testNRTRestart; TestTlogReplica.testRecovery; TestTlogReplica.testOnlyLeaderIndexes; TestTlogReplica.testKillLeader; CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestTaskManagement.classMethod (Error shutting down MiniSolrCloudCluster; suppressed: timeout waiting for closeThreadPool to shut down); TestTermsQParserPlugin.classMethod (ObjectTracker found 16 objects not released, all SolrMetricsContext, created by CoreAdminHandler.initializeMetrics).

Run 005 (5): CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestTlogReplica.testOnlyLeaderIndexes; TestTlogReplica.testRecovery; TestTlogReplica.testKillLeader; TestCoordinatorRole.testNRTRestart.

Run 006 (5): TestTlogReplica.testRecovery; TestTlogReplica.testKillLeader; TestTlogReplica.testOnlyLeaderIndexes; CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestCoordinatorRole.testNRTRestart.

Four methods failed in every completed run: TestTlogReplica (testKillLeader, testRecovery), CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure, and TestCoordinatorRole.testNRTRestart. Seeds differed between runs (for example 787C6229591C6F1E in run 001, 8D8640B367EDAD08 in run 002, 5AB00AEB1AE2392 in run 003), so the failures repeat across seeds.

## Stand-down and mistakes

- Owner stood the loop down at 19:23 UTC. The driver was stopped at 19:37 UTC. On the owner's direction, run 007 was stopped at about 20:02 UTC and recorded as stopped.
- After the host reboot at about 15:05 UTC the restarted loop renumbered from 001 and overwrote `runs/run-001.log` (the tester's stdout). The test log and XML for run 001 survive under `tmp/run-001`.
- Partial build output for run 004 was deleted by mistake while recording it as died.
- The `first_failure_lines` column in `runs/results.tsv` is a grep for "Exception" and picks up harmless locale and security-policy noise. Use the test logs for failure counts.

## Not done

- No check on another host or on the 10.0.0 release, so whether these failures come from RC3 or from this host is unknown.
- No BATS run and no JDK 25 run.
