# SOLR-18586 probe run, round 2, local-/tmp repeat (vm2)

Status: EXECUTED. The storage hypothesis in `reports/solr-18586-probe-run-round-2.md` is supported by a matched comparison. No fix, no branch, no gate.

Claim: `claims/solr-18586-probe-run-round-2-vm2.md`. Host: vm2. Tags: premise-run, records.

## Setup

- Source: RC3 `749712b71940c9f96d824c533f32f5c56dcd3924`. Clean control and probe worktrees on local ext4 `/tmp`. The earlier round used worktrees under `/workspace` (nfs4).
- Probe: same generator and same diff as before (`material/solr-18586-probe-round-2.diff`), applied with `--apply`. The regenerated diff is byte-identical to the earlier one.
- JDK: OpenJDK 21.0.12.1. Gradle through the wrapper, under the vm2 host lock via `pool/bin/sweep-run.sh`.
- Same six matched runs as before: testKillLeader, testRecovery, testOnlyLeaderIndexes at seeds `3635CC89D23D0929` and `9D6BB87C4F145405`.
- Evidence on vm2 under `/workspace/evidence/solr-18586/logs-tmp/{tmp-control,tmp-probe}/`.

## Results

| Run | Control (local /tmp) | Probe (local /tmp) | EMLINK lines (control / probe) |
| --- | --- | --- | --- |
| testKillLeader, seed 3635CC89D23D0929 | PASS | PASS | 0 / 0 |
| testRecovery, seed 3635CC89D23D0929 | PASS | PASS | 0 / 0 |
| testOnlyLeaderIndexes, seed 3635CC89D23D0929 | PASS | PASS | 0 / 0 |
| testKillLeader, seed 9D6BB87C4F145405 | PASS | PASS | 0 / 0 |
| testRecovery, seed 9D6BB87C4F145405 | PASS | PASS | 0 / 0 |
| testOnlyLeaderIndexes, seed 9D6BB87C4F145405 | PASS | PASS | 0 / 0 |

Pass status comes from the per-run JUnit XML: each file shows `tests="1" failures="0" errors="0" skipped="0"`, twelve files in total. Each run took 1 to 3 minutes.

## Comparison with the /workspace (NFS) round

On `/workspace`, the same RC3 code failed five of six matched runs at the same seeds. The rejoining replica stayed in `recovering`, and index fetch failed with `FileSystemException: Too many links` when hard-linking `_0.si`. On local `/tmp`, the same code and seeds pass in every run, with zero EMLINK lines and zero index-fetch failures. The NFS-only failure at seed 3635 for `testKillLeader` passes here.

## What this establishes and what it does not

- Established: with the test temp directory on local ext4, the matched runs pass and the EMLINK failure does not occur. The earlier round's correlation is now a controlled comparison on identical code, seeds and probe.
- Not established: whether the vm5 and apache/main failures came from the same mechanism. Those hosts were not rerun here. The original finding (a rejoining TLOG replica stuck in `recovering`) may be environmental on NFS, or a real defect that NFS exposes. A pass off NFS does not rule out the second reading for other storage.
- Not established: any timing effect of the probe. Both arms passed here, so the probe did not cause failure on local disk.

## Recommendation for the owner

Before any fix work on SOLR-18586, rerun the original failing shape on a host with local temp storage, and on the vm5 setup if available, to confirm whether the stall reproduces off NFS. If it does not, the board should record the RC3 failure as a test-environment defect, and the RC3 smoke-test failures need re-triage on local storage. This is a recommendation, not a closure of the ticket.

## Decision ladder

Rung 2 (probe that separates the explanations). This is the matched repeat that the earlier round named as its next step.

## Deliverables

- This report. The earlier round's report and diff are unchanged.
