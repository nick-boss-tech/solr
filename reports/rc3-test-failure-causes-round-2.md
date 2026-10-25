# RC3 test failures: causes and matched pairs (vm5, round 2, 2026-10-25)

Scope: the five RC3 smoke-loop failures. Each row below is either explained by a matched pair (base leg and fixed or changed leg, same test, same seed, same host) or marked open. Only matched pairs count as proof.

## Summary

| Failure | Cause | Evidence | Status |
|---|---|---|---|
| SOLR-18587 `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` | Two causes: (a) unload after a failed reload leaves the old loaded core open (code); (b) the test runs on `MockDirectoryFactory`, whose deferred directory removal never deletes files (test setup) | Two matched pairs, both with `useFactory(null)` in the test | Explained. Fix = `1915f8ba014` (= `1de4f006f1c` on fork) plus test commit `13e997b3882` (pushed to fork) |
| SOLR-18564 `TestTermsQParserPlugin` / `TestTaskManagement` (metrics leak) | Shutdown close pool times out and skips the later closes, leaking metrics contexts | Matched pair at seed D4DAB631E49B77A8 | Explained. Already in `rc3-fixset-candidate` as `5d5e7e90f55` |
| SOLR-18586 `TestTlogReplica.testKillLeader`, `testRecovery` | Host environment: `/workspace` is NFS and returns EMLINK ("Too many links") on hard links; `IndexFetcher` hard-links identical local files (`IndexFetcher.java:1184`), so replication fails | Matched pairs for both methods: NFS fails, local disk passes | Explained as host environment. No Solr code fix shown to be needed on local disk |
| SOLR-16630 `TestCoordinatorRole.testNRTRestart` | Same NFS hard-link failure (`Pull jetty replicas didn't become active in time`, 6 EMLINK hits) | Matched pair at seed 787C6229591C6F1E | Explained as host environment. Fix commits already in `rc3-fixset-candidate` |
| SOLR-18530 `RecoveryAfterSoftCommitTest.test` | Client-side `ClosedChannelException` on an update sent during the test's own replica restart; no EMLINK | One loop run (of five) with a failure; no matched pair yet | Open. Cause not reproduced here. Fix commits already in `rc3-fixset-candidate` |

## 1. SOLR-18587 (CoreAdmin)

Matched pairs (same test, same seed, `-Dtests.seed`, `useFactory(null)` added to the test in both legs):

| Seed | Base (RC3 `749712b7194` `CoreContainer.java`) | Fixed (`1915f8ba014` `CoreContainer.java`) |
|---|---|---|
| 787C6229591C6F1E | FAIL, "data directory was not cleaned up" (`probe-usefactory-base-787C.log`) | PASS (`probe-usefactory-fixed-787C.log`) |
| 5A3E9C1D7B2F4808 | FAIL (`probe-usefactory-base-5A3E9C1D7B2F4808.log`) | PASS (`probe-usefactory-fixed-5A3E9C1D7B2F4808.log`) |

Without the test change, the fixed leg still fails at seed 787C6229591C6F1E (`fixed-leg-787C.log`).

Mechanism, from the probes:
- Under the default test factory (`MockDirectoryFactory`, from `solrconfig.xml` `${solr.directoryFactory:solr.MockDirectoryFactory}`), `remove(path, true)` only queues the path. Removal runs in `StandardDirectoryFactory.removeDirectory`, which the mock never reaches, so the directory survives. Probe trace: `removeEntries` held `data/tlog` and `data`, and `removeDirectory` was never called. `useFactory(null)` is set only in `testCreateWithSysVars`, not in the failing test. The test's own comment says it needs FS-based indexes.
- On the base code the unload returns early for a failed reload (`CoreContainer.unloadWithoutCoreOp`) and never closes the still-loaded core, so the data dir is not removed through the close path. The one-line fix routes it through the normal close.

Change made: test commit `13e997b3882` (on fork branch `solr-18587-coreadmin-unload-failed-reload`, fast-forward from `1de4f006f1c`) adds `useFactory(null);` to `testDeleteInstanceDirAfterCreateFailure`. The commit uses the pool identity used by the other commits on this branch.

Not applied: the candidate `rc3-fixset-candidate` does not contain these two commits, and the tarball was built before this decision.

## 2. SOLR-18564 (TermsQ / TaskManagement)

Matched pair at seed D4DAB631E49B77A8 (`CoreContainerShutdownTest`):
- Base (RC3 + test file): FAIL, "Timeout waiting for pool to shutdown [closeThreadPool]" and 15 unreleased `SolrMetricsContext` (`termsq-repro/base-rc3-shutdowntest-v2.log`).
- Fixed (RC3 + the shutdown try/catch hunk from `d7caa8cb784` + test file): PASS, `:solr:core:test (SUCCESS): 1 test(s)` (`termsq-repro/fixed-rc3-shutdowntest-matched-D4DAB631E49B77A8.log`).

The `d7caa8cb784` `CoreContainer.java` diff also removes `checkOverseerDesignate()`. That is a main-line change, so only the shutdown hunk was applied to RC3.

## 3. SOLR-18586 (TLOG) and SOLR-16630 (NRT)

The NFS mount `/workspace` rejects hard links:

```
$ ln base.si l.1
ln: failed to create hard link to 'base.si': Too many links
```

`/tmp` (ext) allows about 65k links. Replication hard-links identical files, so the replicas fail on this host.

Matched pairs on RC3 `749712b7194` (`tlog-rc3-tip` worktree), same seed, same method; the only difference is `-Ptests.workDir`:

| Test | Seed | Default `tests.workDir` (NFS) | `-Ptests.workDir=/tmp/...` (local) | Log |
|---|---|---|---|---|
| `TestTlogReplica.testKillLeader` | 3635CC89D23D0929 | FAIL "Expected collection to be 1x2", 4 EMLINK | PASS | `tlog-repro/matched-nfs-*.log`, `matched-local-*.log` |
| `TestTlogReplica.testRecovery` | 3635CC89D23D0929 | FAIL "Replica didn't recover", 4 EMLINK | PASS | `tlog-repro/recovery-*.log` |
| `TestCoordinatorRole.testNRTRestart` | 787C6229591C6F1E | FAIL "Pull jetty replicas didn't become active in time", 6 EMLINK | PASS | `tlog-repro/coordrole-*.log` |

The smoke-loop XML under `/workspace/smoke-rc3/tmp/run-00N/` shows the same EMLINK hits for every TLOG run (13 to 30 per run) and for `TestCoordinatorRole` (5 to 13 per run).

Implication for the gate: the smoke tester unpacks and tests under `--tmp-dir`. With `--tmp-dir` on `/workspace` (NFS), these tests fail for host reasons, not RC3 code reasons. A smoke run for the gate should use a local `--tmp-dir` (e.g. under `/tmp`). This is a host setting the owner has to choose; it does not change the release content.

## 4. SOLR-18530 (RecoveryAfterSoftCommitTest): open

Failure in loop run 001: `SolrServerException: IOException ... /collection1_shard1_replica_n3/update`, root `java.nio.channels.ClosedChannelException`, from `RecoveryAfterSoftCommitTest.java:106` during the test's own restart. No EMLINK in its XML (0 hits across runs 001 to 006). Seed from the XML: 787C6229591C6F1E. Not reproduced in this round; no matched pair. The candidate's SOLR-18530 commits are unproven on RC3.

## Not done

- No PR, no JIRA comment, no dev-list text, no vote action.
- No smoke run (owner go required).
- Candidate `rc3-fixset-candidate` not changed.
- Owner decision pending: add the SOLR-18587 pair (`1de4f006f1c` + `13e997b3882`) to the candidate and rebuild the tarball, and set the smoke `--tmp-dir` to local disk.
