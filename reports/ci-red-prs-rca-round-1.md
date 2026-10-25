# CI red PR checks, round 1: RCA for three stale failures

Claim: `claims/ci-red-prs-rca-round-1-vm2.md`. Assignment: `assignments/ci-red-prs-rca-round-1.md`.
Logs: GitHub API `GET /repos/apache/solr/actions/jobs/<id>/logs`, fetched 2026-10-25 (full logs; sizes 0.9 to 2.1 MB). Failed runs were 2026-10-10. Line numbers below are in the fetched log files.

## Verdicts

| PR | Ticket | Job | Failing test | Seed | Verdict | Diff touches the failing test? |
| --- | --- | --- | --- | --- | --- | --- |
| #5083 | SOLR-11483 | 114192700428 | `RecoveryAfterSoftCommitTest.test` | `A57A22E346C237C6` | (b) flake family, settling run needed | No. The diff touches `UpdateLog.java` and `TestRecovery.java`. The test is in the recovery path, so the PR is not ruled out. |
| #5081 | SOLR-7504 | 114192610971 | `LeaderElectionIntegrationTest.testSimpleSliceLeaderElection` | `3E3D9FF553211ED6` | (c) infrastructure: port collision | No. The diff touches `CountFieldValuesUpdateProcessorFactory.java` and `FieldMutatingUpdateProcessorTest.java`. |
| #5069 | SOLR-3657 | 114192758248 | `GCSInstallShardTest.classMethod` (cluster shutdown) | `B94347D2600CC75D` | (b) unrelated failure, settling run needed | No. The diff touches `DocumentBuilder.java` and `DocumentBuilderTest.java`. The failure is in `solr/modules/gcs-repository`. |

## Evidence

**#5083.** Log line 6385: `RecoveryAfterSoftCommitTest > test FAILED`, with `SolrServerException: IOException occurred when talking to server at http://127.0.0.1:39913/...`. The nested cause (line 6451) is `ClosedChannelException` from Jetty HTTP2 `onShutdown`. The suite summary (line 8392) is `6061 tests completed, 1 failed, 273 skipped`. The RC3 smoke loop report (`reports/rc3-smoke-loop-round-1.md`, run 001) also lists `RecoveryAfterSoftCommitTest.test` as failing, and the RC3 notes name PR 5098 as its fix. So this is a known flake family. The seed is `A57A22E346C237C6`.

**#5081.** Log line 6396: `LeaderElectionIntegrationTest > testSimpleSliceLeaderElection FAILED`. The cause is `java.net.BindException: Address already in use` from Jetty `ServerConnector.openAcceptChannel`. The bind fails at cluster start, before any Solr code under test runs. The suite summary (line 8646) is `6066 tests completed, 1 failed, 271 skipped`. This is an infrastructure signature. The 2026-10-10 note called it a Jetty port collision. The log now supports that claim. The seed is `3E3D9FF553211ED6`.

**#5069.** Log line 6385: `GCSInstallShardTest > classMethod FAILED`, with `java.lang.Exception: Error shutting down MiniSolrCloudCluster` from `MiniSolrCloudCluster.checkForExceptions`. The nested causes (lines 11633 to 12552) are `IndexNotFoundException: no segments* file found` and, in the restore path, `RuntimeException: Exception while restoring the backup index for file _0_Lucene90_0.dvd` caused by `UnsupportedOperationException`. The module summary (line 13859) is `35 tests completed, 1 failed, 6 skipped`. The failure is in the GCS backup-restore path, not in `DocumentBuilder`. The seed is `B94347D2600CC75D`.

## Follow-on

- #5083 and #5069 are (b). Each needs a settling run at its recorded seed on the PR head and on `apache/solr` main. Record which side fails. Settling runs are not run in this report. They are gate jobs, to be queued on a Linux gate host with the build lock.
- #5081 is (c). The cheapest honest re-run is to re-run the failed job on the same head. The failure is in an unrelated class, so a re-run tests whether the port collision recurs. It needs no code change.
- No (a) verdict, so no implementation assignment is filed.

## Status lines for the board

- PR #5083: CI red 2026-10-10 is a RecoveryAfterSoftCommitTest flake (seed A57A22E346C237C6). Settling run pending on head and main.
- PR #5081: CI red 2026-10-10 is a Jetty bind failure in LeaderElectionIntegrationTest (seed 3E3D9FF553211ED6). Infrastructure; re-run the job on the head.
- PR #5069: CI red 2026-10-10 is a GCSInstallShardTest restore-path failure (seed B94347D2600CC75D). Settling run pending on head and main.

## Limits

- Log and diff reading only. No test was run here.
- The RecoveryAfterSoftCommitTest link to PR 5098 comes from the RC3 notes, not a fresh check of PR 5098.
- No PR, comment, or push was made.
