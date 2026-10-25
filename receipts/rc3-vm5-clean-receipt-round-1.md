# RC3 receipt from vm5 (clean, round 1)

Single source for vm5's RC3 work. Built from vm5's logs, `results.tsv` and JUnit XML. Earlier reports and drafts are history; where they disagree with this receipt, the disagreement section says which reading the logs support.

## Artifacts and host

- Artifacts: Solr 10.1.0 RC3, rev `749712b71940c9f96d824c533f32f5c56dcd3924` (tip of `branch_10_1`).
- Host: vm5, Linux aarch64, 4 CPUs, 7 GB RAM. Root disk 7.8 GB; temp directories on `/workspace` (NFS).
- JDK: `openjdk version "21.0.12.1" 2026-08-18 LTS` (`/workspace/jdk/jdk-21.0.12.1+1`). Every smoke run log records it.

## Smoke runs (`/workspace/smoke-rc3/runs/results.tsv`; counts from `tmp/run-NNN/.../test-results/test/*.xml`)

| Run | Start (UTC) | End / state | Outcome | Testcases | Failing testcases | Failing methods (from XML) |
|---|---|---|---|---|---|---|
| 001 | 08:07 | 10:18 | completed, BUILD FAILED | 7,374 | 6 | CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; RecoveryAfterSoftCommitTest.test; TestCoordinatorRole.testNRTRestart; TestTlogReplica.testKillLeader; TestTlogReplica.testOnlyLeaderIndexes; TestTlogReplica.testRecovery |
| 002 | 10:18 | 12:23 | completed, BUILD FAILED | 7,374 | 4 | CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestCoordinatorRole.testNRTRestart; TestTlogReplica.testKillLeader; TestTlogReplica.testRecovery |
| 003 | 12:23 | 14:33 | completed, BUILD FAILED | 7,376 | 7 | CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestCoordinatorRole.testNRTRestart; TestTaskManagement.classMethod; TestTermsQParserPlugin.classMethod; TestTlogReplica.testKillLeader; TestTlogReplica.testOnlyLeaderIndexes; TestTlogReplica.testRecovery |
| 004 | 14:33 | 15:07 | died (host reboot, about 15:05) | partial (27 XML, 118 testcases) | 0 in partial output | none recorded; partial output deleted by mistake |
| 005 | 15:08 | 17:14 | completed, BUILD FAILED | 7,374 | 5 | CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestCoordinatorRole.testNRTRestart; TestTlogReplica.testKillLeader; TestTlogReplica.testOnlyLeaderIndexes; TestTlogReplica.testRecovery |
| 006 | 17:14 | 19:20 | completed, BUILD FAILED | 7,374 | 5 | CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure; TestCoordinatorRole.testNRTRestart; TestTlogReplica.testKillLeader; TestTlogReplica.testOnlyLeaderIndexes; TestTlogReplica.testRecovery |
| 007 | 19:19 | about 20:02 | stopped on owner request | partial (27 XML, 118 testcases) | 0 in partial output | none recorded |

- Completed runs: 5 (001, 002, 003, 005, 006). None passed.
- Run 004 and run 007 have partial output only. Their "0 failing" is not a pass.
- Run 001 failing count is from the XML (6). Its stdout log `runs/run-001.log` was overwritten by a mis-numbered restart; `tmp/run-001` XML is intact.
- Runs 004 and 007 are counted as incomplete, not as results.

Repeating across all five completed runs and across seeds: TestCoordinatorRole.testNRTRestart (5 of 5), CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure (5 of 5), TestTlogReplica.testKillLeader (5 of 5), TestTlogReplica.testRecovery (5 of 5), TestTlogReplica.testOnlyLeaderIndexes (4 of 5; not in run 002).

## Fix-set table (vehicles and proof-pair status under `assignments/rc3-proof-pair-requirement-round-1.md`)

| Item | Failing methods | Vehicle | Proof-pair status (as the assignment records it) |
|---|---|---|---|
| RecoveryAfterSoftCommitTest.test (run 001 only) | 1 run | PR #5098, SOLR-18530 (not in RC3) | No factory pair. Pre leg is the RC3 smoke failure (external to the PR); the post leg is the PR author's evidence, cited as his. |
| TestCoordinatorRole.testNRTRestart | 5 of 5 | PR #5106, SOLR-16630 (not in RC3) | Post leg running at head `a02cc7ba6b90` (vm6). Pre leg behavioral (about 30 s on main vs about 7 s at head). Pair completes when that receipt lands. |
| TestTermsQParserPlugin / TestTaskManagement (run 003) | 1 run | SOLR-18564, PR #5192 (open) | Pair complete at fix commit `d7caa8cb784` (2026-10-22 gate). This vm5 unit 3 RC3-tip pair is not matched by seed (see disagreements). |
| TestTlogReplica.testKillLeader, testRecovery, testOnlyLeaderIndexes | 4 to 5 of 5 | SOLR-18586 (no fix) | Pre leg done: reproduces at RC3 tip and at main (seeds 3635CC89D23D0929 and 9D6BB87C4F145405). No post leg. Peersync probe ruled out the flag alone. |
| CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure | 5 of 5 | SOLR-18587 (no fix) | Pre leg done: reproduces at RC3 tip and main, seed 787C6229591C6F1E. No post leg. Cause not established. |

## Tarball check verdict (vm5, round 1)

Export-ignoring `/dev-tools` does not affect building or checking the source release on this evidence. Archive listings differ only by 39 `dev-tools/` entries. RAT passed from the unpacked tarball (rc=0). The full `check --continue` failed in two tasks: `:allHelpFilesExit` (missing `dev-docs/gradle-help/workflow.txt`, a pre-existing gap because `/dev-docs` is export-ignored in the PR head) and `:solr:core:test` (5 methods, the same as the loop). Source: `reports/vm5-release-tarball-dev-tools-check-round-1.md` and its addendum. Logs: `/workspace/smoke-rc3/tarball-logs/` (`rat.log`, `check.log`).

## TLOG reproduction (RC3 tip and main)

- RC3 tip `749712b7194`: `/workspace/smoke-rc3/tlog-repro/rc3-tip-run1.log`. 14 tests, 2 failed (testKillLeader, testRecovery), 2 skipped. Seed 3635CC89D23D0929.
- apache/main `13c693d805f`: `/workspace/smoke-rc3/tlog-repro/main-run1.log`. 14 tests, 3 failed (testKillLeader, testRecovery, testOnlyLeaderIndexes), 2 skipped. Seed 9D6BB87C4F145405.
- The XML for these two runs was overwritten by later runs in the same scratch worktrees; see `reports/rc3-repro-xml-correction-round-1.md`. The logs are the record.

## CoreAdmin reproduction (RC3 tip and main)

- RC3 tip: `/workspace/smoke-rc3/coreadmin-repro/rc3-tip-run1.log`, 1 test, failed. Seed 787C6229591C6F1E.
- apache/main: `/workspace/smoke-rc3/coreadmin-repro/main-run1.log`, 1 test, failed. Same seed, same message.
- Archived XML: `/workspace/smoke-rc3/repro-archive/coreadmin-2026-10-25/`.

## SOLR-18564 hypothesis test (unit 3, one paragraph)

The leak charge appeared in one of five completed runs (run 003), where `TestTaskManagement` also failed on a close-pool timeout; the two classes' XML timestamps are about two minutes apart, and the XML does not record a JVM id. On the RC3 tip, with the regression test from `d7caa8cb784` added, the unmodified tree fails with the close-pool timeout and 15 unreleased metrics contexts (seed D4DAB631E49B77A8), and the tree with the fix passes (seed E89FE6C82AB0D707). The two legs use different seeds, so they are not a matched pair under the proof-pair rule. Source: `reports/rc3-termsqparser-metrics-leak-round-1.md`.

## Limits

- One small aarch64 host with 4 CPUs. None of these failures has been checked on another host or on the 10.0.0 release.
- Run-001 stdout (`runs/run-001.log`) was overwritten by a mis-numbered restart after the host reboot. Its XML is intact.
- Run-004 partial output was deleted by mistake while recording it as died.
- The `first_failure_lines` column in `results.tsv` is a grep for "Exception" and includes harmless locale and security-policy noise. Counts here come from the XML.
- The TLOG and CoreAdmin scratch-worktree XML was overwritten; logs and the CoreAdmin archive are the record.
- Units 2 and 3 of the root-cause assignment are described above; no fix branch exists from this round.

## Disagreements with earlier files

- `drafts/rc3-fix-set-email-draft.md` says a SOLR-18564 fix "is prepared and its PR is being put up". The fix-set draft's wording of the root cause matches the record. Its support for SOLR-18564 should rest on the 2026-10-22 matched pair at `d7caa8cb784`, not on vm5's RC3-tip legs, which use different seeds.
- `drafts/rc3-fix-set-email-draft.md` and `drafts/rc3-vote-followup-draft.md` say the TestTlogReplica failures and testNRTRestart happened in all five runs. The XML agrees for testKillLeader, testRecovery, and testNRTRestart (5 of 5). testOnlyLeaderIndexes is 4 of 5. The vote follow-up says "(testOnlyLeaderIndexes in four)", which matches; the fix-set draft says "and testOnlyLeaderIndexes in four", which also matches.
- `drafts/rc3-vote-note-draft.md` (the release-note findings) is about changelog and cuVS wording, not the smoke runs; it is consistent with the tarball and changelog checks in the record. It says "I did not build or run the release", which holds for that note's file checks; the smoke runs were a separate activity.
- `reports/rc3-smoke-loop-round-1.md` lists run 005 with testOnlyLeaderIndexes; the XML agrees.
- `reports/rc3-tlogreplica-rootcause-round-1.md` said the XML for each run is under the scratch worktrees; that is no longer true (see correction).
- The loop's `results.tsv` `first_failure_lines` column is not a failure list (see Limits).
