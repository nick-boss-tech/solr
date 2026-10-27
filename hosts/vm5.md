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

Heartbeat: 2026-10-24T13:30Z (rc3-smoke-loop-round-1: run-001 and run-002 fail, see earlier heartbeats; run-003 in unit tests, about 1h07m in, temp dir 1.1 GB)

Heartbeat: 2026-10-24T14:18Z (rc3-smoke-loop-round-1: run-003 in unit tests, about 1h55m in, temp dir 1.3 GB; run-001 and run-002 fail as recorded above)

Heartbeat: 2026-10-24T15:10Z (rc3-smoke-loop-round-1: host restarted about 15:05Z and killed run-004 (recorded as died). Loop restarted 15:08Z and numbering fixed; run-005 running. Run-001 stdout log was overwritten by a mis-numbered restart; its test.log and XML are intact under tmp/run-001. Partial build output of died run-004 was deleted by mistake.)

Heartbeat: 2026-10-24T15:58Z (rc3-smoke-loop-round-1: run-005 in unit tests, about 50 min in; runs 001 to 003 fail, run-004 died at host restart)

Heartbeat: 2026-10-24T16:53Z (rc3-smoke-loop-round-1: run-005 in unit tests, about 1h45m in; runs 001 to 003 fail, run-004 died at host restart)

Heartbeat: 2026-10-24T17:41Z (rc3-smoke-loop-round-1: run-006 in unit tests, about 27 min in; run-005 failed with the same tests as before)

Heartbeat: 2026-10-24T18:45Z (rc3-smoke-loop-round-1: run-006 in unit tests, about 1h31m in; run-005 failed with the same tests as before)

Heartbeat: 2026-10-24T19:49Z (rc3-smoke loop stood down by owner direction; driver stopped, run-007 still running and waiting on the owner to say stop or finish; release check pending the lock)

Heartbeat: 2026-10-24T20:12Z (vm5-release-tarball-dev-tools-check-round-1: archives diff shows dev-tools (39 entries) as the only removed tree; unpacked tree has no .git, dev-tools or dev-docs; RAT passed rc=0 in 3m32s; full root check with --continue running, log /workspace/smoke-rc3/tarball-logs/check.log; run-007 stopped by owner request and recorded in runs/results.tsv)

Heartbeat: 2026-10-24T21:16Z (vm5-release-tarball-dev-tools-check-round-1: full root check still running in :solr:core:test; no failed task yet; RAT passed rc=0)

Heartbeat: 2026-10-24T22:23Z (vm5-release-tarball-dev-tools-check-round-1: full root check still running, no failed task yet; rc3-smiley-failure-comparison-round-1: reports pushed)

Heartbeat: 2026-10-24T23:20Z (rc3-tlogreplica-rootcause-round-1 unit 1: focused TestTlogReplica run at RC3 tip running under the host lock, log /workspace/smoke-rc3/tlog-repro/rc3-tip-run1.log; main worktree /workspace/worktrees/tlog-main ready)

Heartbeat: 2026-10-25T00:07Z (rc3-tlogreplica-rootcause-round-1: reproduction report pushed; reading the TLOG recovery path; no Gradle run live; phone-agent reply still pending on assignments/vm5-to-phone-agent-tarball-status-round-1.md)

Heartbeat: 2026-10-25T01:04Z (rc3-tlogreplica-rootcause-round-1 unit 1 and rc3-termsqparser-metrics-leak-round-1 unit 3: unit 3 done; reading new rc3-tlogreplica-peersync-probe-round-1 assignment)

Heartbeat: 2026-10-25T02:06Z (rc3-repro-xml-correction-round-1: TLOG XML overwritten by CoreAdmin runs in scratch worktrees; logs remain the record; CoreAdmin XML archived under smoke-rc3/repro-archive)

Heartbeat: 2026-10-25T16:34Z (rc3-test-failure-causes-round-2: report pushed at 70a2440cc96; SOLR-18587 fixed by matched pairs; TLOG and NRT failures are NFS hard-link (EMLINK) on /workspace; TermsQ fix matched; SOLR-18530 open; waiting for owner on candidate and smoke tmp-dir)

Heartbeat: 2026-10-27T17:29:15Z (pr5112 linux gate: superseding gate at 46bc7e4 green; receipts/PR-5112-target-header-followup.md; gradle-help review claim paused, its heartbeat stale since 07:44Z)

Heartbeat: 2026-10-27T18:54:09Z (SOLR-18564 timeout-type gate at 6ca1cba8061b GREEN: receipts/SOLR-18564-timeout-type-round-1.md)

Heartbeat: 2026-10-27T20:48:13Z (SOLR-17887 PR prep: draft and report pushed; PR ready to open, awaiting owner)

Heartbeat: 2026-10-27T21:27:22Z (SOLR-17887 internal selection pushed at 03d5efbf; receipts/SOLR-17887-vm5-selection.md; round-2 PR draft drafts/pr-solr-17887-file-tree-round-2.md)
