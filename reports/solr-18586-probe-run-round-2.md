# SOLR-18586 Linux probe run, round 2 (vm2)

Status: EXECUTED. Root cause not established. No fix, no branch, no gate.

Claim: `claims/solr-18586-probe-run-round-2-vm2.md`. Assignment: `assignments/solr-18586-probe-run-round-2.md`. Host: vm2. Tags: premise-run, records.

## Setup

- Source: RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`, clean control worktree and probe worktree at the same SHA.
- Probe: the Python generator in `reports/solr-18586-tlog-windows-investigation-round-1.md` (SHA-256 `385ba51970b26737bdf166c6fc1ffe4241e797a0969b77b4f895da99f73381bb` verified), applied to the probe arm only with `--apply`. Diff: `material/solr-18586-probe-round-2.diff` (3 files, 49 insertions, 1 deletion; logging only).
- JDK: OpenJDK 21.0.12.1 (`/workspace/jdk/jdk-21.0.12.1+1`). Gradle through the wrapper, under the vm2 host lock via `pool/bin/sweep-run.sh`.
- Command per run: `:solr:core:test --tests org.apache.solr.cloud.TestTlogReplica.<method> -Ptests.seed=<seed> --no-daemon`.
- Evidence (logs, XML, headroom records) is on vm2 under `/workspace/evidence/solr-18586/logs/{control,probe,control2,probe2}/`. Logs are not committed here.

## Results (matched, same method and seed on both arms)

| Run | Control (clean) | Probe (instrumented) | EMLINK lines (control / probe) |
| --- | --- | --- | --- |
| testKillLeader, seed 3635CC89D23D0929 | FAIL: Expected collection to be 1x2 | FAIL: same | 4 / 4 |
| testRecovery, seed 3635CC89D23D0929 | FAIL: Replica didn't recover | FAIL: same | 4 / 4 |
| testOnlyLeaderIndexes, seed 3635CC89D23D0929 | FAIL: Some replicas are not in sync with leader | FAIL: same | 21 / 21 |
| testKillLeader, seed 9D6BB87C4F145405 | FAIL: Expected collection to be 1x2 | FAIL: same | 4 / 4 |
| testRecovery, seed 9D6BB87C4F145405 | FAIL: Replica didn't recover | FAIL: same | 4 / 4 |
| testOnlyLeaderIndexes, seed 9D6BB87C4F145405 | PASS | PASS | 0 / 0 |

The probe does not change the outcome in any matched pair.

## What the probe log shows (testKillLeader, seed 3635)

- The rejoining TLOG replica `core_node4` (`replica_t2`) goes through `checkRecovery` (leader=false, skip=false), schedules recovery, and takes the recovery lock.
- Attempt 0: pingLeader, bufferUpdates, publish `recovering`, prepRecovery, replicate, commitOnLeader, then doFetch.
- doFetch fails with `SolrException: Index fetch failed`, caused by `FileSystemException: ... data/index.<timestamp>/_0.si: Too many links`. The fetch hard-links segment files from the leader's index into a new `index.<timestamp>` directory.
- Attempt 1 starts and repeats the same sequence. The replica stays `recovering`.
- The other TLOG replica (`core_node3`, `replica_t1`) logs `No registered leader was found after waiting for 4000ms` on its own index fetch in the same run.

## Observations

- Every failing run logs EMLINK on the `_0.si` hard link. The only passing run (`testOnlyLeaderIndexes`, seed 9D6B) logs none. The count is identical on both arms.
- The test temp directory is under the worktree, which lives on `/workspace` (`nfs4`). `/tmp` is a local ext4 volume.
- This is a correlation. It has not been tested whether EMLINK is an NFS hard-link limit, and it has not been shown that the EMLINK failure is the cause of the RC3 stall rather than a side effect.

## Not established

- Root cause of the stall. The EMLINK fetch failure explains why attempts never reach ACTIVE on this host, but not why the link count is exceeded or whether the same happens on the failing RC3 hosts.
- Whether the failure is host-specific. The original vm5 failures and the apache/main reproduction are not reproduced on a non-NFS temp directory in this round.

## Proposed next step (not run)

Run the matched control and probe arms again with the worktrees under local `/tmp` (ext4). If the failure disappears on local disk, the RC3 stall is environmental, and the reproduction should move to a host that uses local temp storage. If it persists with EMLINK absent, the EMLINK line is a side effect and the investigation returns to the leader-publication sequence in the probe log.

## Decision ladder

Rung 2 (probe that separates the explanations). Matched pairs at both seeds were run as the assignment requires. The NFS hypothesis is a new reading of the probe output, so it is recorded here as a hypothesis and not as a finding.

## Deliverables

- This report.
- `material/solr-18586-probe-round-2.diff`: the instrumentation diff applied to the probe arm.
