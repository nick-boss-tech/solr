# Assignment revision: SOLR-18586 rerun on local test storage, and RC3 re-triage (round 1, revision 1)

Status: OPEN
Supersedes: `assignments/solr-18586-local-storage-rerun-round-1.md`. The original is not edited; this revision carries the full background and the same work.
Tags: premise-run, records
Intended host: a pool Linux host whose test temp directory is on local disk, not NFS. Check first with `df -T` on the worktree and the Gradle temp path. If the temp path is NFS, the host is not eligible and the claim must say so.

## Background: what has already been done

This section records everything run so far, so the next host does not repeat it.

1. Original report (vm5, rounds before this one). `TestTlogReplica.testKillLeader` failed at RC3 `749712b719` and at apache/main `13c693d805f`, with the rejoining TLOG replica stuck in `recovering`. Logs: `/workspace/smoke-rc3/tlog-repro/`. Report: `reports/rc3-tlogreplica-rootcause-round-1.md`. The `firstTime = true` change in `RecoveryStrategy.java` was ruled out (`reports/rc3-tlogreplica-peersync-probe-round-1.md`). The vm5 claims were released as stale on 2026-10-27.
2. Windows source investigation (`reports/solr-18586-tlog-windows-investigation-round-1.md`). Read the recovery code and wrote a logging-only generator (`rc3-failure-probes.py`, SHA-256 `385ba51970b26737bdf166c6fc1ffe4241e797a0969b77b4f895da99f73381bb`). The Python generator was dry-run on RC3 and never executed. It was handed to Linux.
3. Probe-run assignment (`assignments/solr-18586-probe-run-round-2.md`, pushed `0ee9584eb9e`). Claimed on vm2 (`claims/solr-18586-probe-run-round-2-vm2.md`, pushed `ab64a120f32`).
4. Setup. Clean control and probe worktrees at RC3 under `/workspace/probes/solr-18586/{control,probe}`. `/workspace` is NFS (`nfs4`). The generator ran with `--apply` on the probe arm only. Diff: `material/solr-18586-probe-round-2.diff`, 3 files, 49 insertions, 1 deletion, logging only.
5. First matched batch on NFS, `testKillLeader` seed `3635CC89D23D0929`, control and probe. Both failed with "Expected collection to be 1x2" at `TestTlogReplica.java` line 484.
6. Second matched batch on NFS, the other five runs per arm. Results below.

### NFS results (`/workspace`, nfs4)

| Run | Control | Probe | EMLINK lines (control / probe) |
| --- | --- | --- | --- |
| testKillLeader, seed 3635CC89D23D0929 | FAIL "Expected collection to be 1x2" | FAIL, same | 4 / 4 |
| testRecovery, seed 3635CC89D23D0929 | FAIL "Replica didn't recover" | FAIL, same | 4 / 4 |
| testOnlyLeaderIndexes, seed 3635CC89D23D0929 | FAIL "Some replicas are not in sync with leader" | FAIL, same | 21 / 21 |
| testKillLeader, seed 9D6BB87C4F145405 | FAIL "Expected collection to be 1x2" | FAIL, same | 4 / 4 |
| testRecovery, seed 9D6BB87C4F145405 | FAIL "Replica didn't recover" | FAIL, same | 4 / 4 |
| testOnlyLeaderIndexes, seed 9D6BB87C4F145405 | PASS | PASS | 0 / 0 |

Probe log for `testKillLeader` on NFS: `core_node4` (`replica_t2`) passes `checkRecovery` (leader=false, skip=false), schedules recovery, and goes through attempt 0 (pingLeader, bufferUpdates, publish `recovering`, prepRecovery, replicate, commitOnLeader, doFetch). doFetch fails with `FileSystemException: ... data/index.<timestamp>/_0.si: Too many links`. Attempt 1 repeats the same steps. `core_node3` (`replica_t1`) logs "No registered leader was found after waiting for 4000ms" during its own fetch.

### Local-storage results (`/tmp`, ext4)

Worktrees at `/tmp/solr-18586/{control,probe}`, same SHA, same probe diff (regenerated, byte-identical). Six matched runs per arm: all PASS. Each XML shows `tests="1" failures="0" errors="0" skipped="0"`, twelve files in total, each run 1 to 3 minutes. Zero EMLINK lines and zero index-fetch failures in every run.

Evidence on vm2: NFS `/workspace/evidence/solr-18586/logs/{control,probe,control2,probe2}/`, local `/workspace/evidence/solr-18586/logs-tmp/{tmp-control,tmp-probe}/`. These logs stay on vm2 and are not committed.

### Board records from this work

- `reports/solr-18586-probe-run-round-2.md` (NFS round), with the diff, pushed `97bb6505469`.
- `reports/solr-18586-probe-run-round-2-local-tmp.md` (local repeat), with a vm2 heartbeat, pushed `93de21a4bd1`.
- vm2 heartbeats `fdaf9d3cf15` (03:15Z) and `93de21a4bd1` (07:10Z).

### Current conclusion, with its limits

- The same RC3 code and the same seeds fail on NFS and pass on local ext4. The difference tracks the storage path. The EMLINK hard-link failure appears only in failing NFS runs.
- This does not show that the vm5 or apache/main failures came from the same mechanism. Those hosts have not been rerun.
- The original stall (a rejoining TLOG replica stuck in `recovering`) may be an NFS artifact, or a real defect that NFS exposes. The local pass does not settle which.
- The probe did not change any outcome in either storage setting.
- Nothing has been fixed, gated, or submitted. No fix branch exists.

## Question this assignment answers

Does the failure reproduce on local test storage on a host other than vm2, and do the four RC3 smoke-test classes pass on local storage? A pass on another host strengthens the environmental reading. A failure on local storage would show that the storage path does not explain it, and the investigation returns to the leader-publication sequence in the probe log.

## Work

1. Record the host, `df -T` for the worktree and the Gradle temp path, JDK, and RC3 SHA. Stop and record if the temp path is NFS.
2. Create clean RC3 worktrees on local disk. Do not apply the probe to the first run. The probe is diagnostic only.
3. Run the six matched `TestTlogReplica` runs (testKillLeader, testRecovery, testOnlyLeaderIndexes at seeds `3635CC89D23D0929` and `9D6BB87C4F145405`) under the host lock, one at a time, using `pool/bin/sweep-run.sh` or `bg-steps.sh` with continue-on-failure. Archive each run's log and XML before the next.
4. Count EMLINK lines and index-fetch failures per run. Confirm test counts from the XML. A pass needs `failures="0" errors="0"`.
5. If the six runs pass, rerun the four RC3 smoke-test classes (TestTlogReplica, CoreAdminHandlerTest, TestCoordinatorRole, TestTermsQParserPlugin) on local storage at their recorded seeds, using the same matched method. Report pass or fail with the seed.
6. If any run fails on local storage, record the log and whether EMLINK appears. Do not call it a code defect until the EMLINK question is answered.

## Rules

- Premise run only. No fix, no fix branch, no PR, no Jira or GitHub comment, no dev-list text.
- No gate: no tidy, no Error Prone, no changelog check, no receipt.
- Never reuse old XML as new evidence.
- Record each decision with its rung (`material/decision-ladder.md`).
- Commit as Nick Shanin with no trailers.

## Deliverables

- `reports/solr-18586-local-storage-rerun-round-1.md`: host, filesystem check, SHA, JDK, a per-run table (pass or fail, EMLINK count, seed), the re-triage table for the four smoke classes, and what remains unexplained.
- Run evidence stays on the host; the report gives absolute paths.
