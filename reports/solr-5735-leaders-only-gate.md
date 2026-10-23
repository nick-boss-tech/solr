# Report: SOLR-5735, repeat the leaders-only safe-leader runs on another host

- Assignment: `assignments/solr-5735-leaders-only-gate-round-1.md`.
- Run by: phone-agent (vm1), 2026-10-23, on this host's local disk (not a network file system). Builds ran serialized under the vm1 queue lock through solr-gradle.sh (the host equivalent of `pool/bin/sweep-run.sh`); logs and JUnit XML are under `~/workspace/tools/assign-logs-2026-10-23/` on vm1. Counts below are from the saved JUnit XML of each run.
- Branch: `solr-5735-safeleader-leaders-only` at `60eb0e7fd0521b8d866443a7b72dd623bacd884d`. Nightly tests on (`-Ptests.nightly=true`) for every run.

## Item 1: ChaosMonkeySafeLeaderWithPullReplicasTest at the recorded seed

Seed `E4EFD2B86C0B81D4`, three runs: 1 test each, 0 skipped, 0 failures, 0 errors. 3 of 3 passed.

## Item 2: both safe-leader tests, random seeds

`ChaosMonkeySafeLeaderWithPullReplicasTest`:

| Run | Seed | Tests | Failures |
| --- | --- | --- | --- |
| 1 | 84A781B21948DEBD | 1 | 0 |
| 2 | B46DA72D70F758E3 | 1 | 0 |
| 3 | CD8828B58E36D6D1 | 1 | 0 |

`ChaosMonkeySafeLeaderTest`:

| Run | Seed | Tests | Failures |
| --- | --- | --- | --- |
| 1 | C0590490004EF7D9 | 1 | 0 |
| 2 | BEBBEA6AADCEF8E9 | 1 | 0 |
| 3 | 68BFA92192470EE0 | 1 | 0 |

6 of 6 passed.

## Item 3: failure detail

No run failed, so there is no assertion and no failed request to classify.

## Conclusion

The leaders-only fix repeated clean on a second host and a second file system: 9 of 9 runs passed, including the three runs at the seed that produced the original timing evidence. The zero-failure check held with the leaders-only client every time. Findings only; no branch change.
