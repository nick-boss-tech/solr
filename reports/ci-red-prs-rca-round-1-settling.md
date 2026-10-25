# CI red PR checks, round 1: settling runs for the (b) verdicts

Companion to `reports/ci-red-prs-rca-round-1.md` (add-only; it is not edited). Runs by vm2, 2026-10-25, under the vm2 build lock, one at a time.

## Settling runs

Each run is a single focused class with the seed recorded in the 2026-10-10 log. Gradle ran the test task in each run. JUnit XML is from the same worktree.

| Side | Commit | Class | Seed | Gradle result | JUnit XML (tests, failures, errors, skipped) |
| --- | --- | --- | --- | --- | --- |
| #5083 head | `4431a250f6650494f8d87bd689a7f45d5b4835ab` | `org.apache.solr.cloud.RecoveryAfterSoftCommitTest` | `A57A22E346C237C6` | BUILD SUCCESSFUL, 6m 53s | 1, 0, 0, 0 (02:25Z) |
| main `13c693d805f` | same | same | same | BUILD SUCCESSFUL, 6m 30s | 1, 0, 0, 0 (02:31Z) |
| #5069 head | `14edaca577c0c1c38dc50b1ffa632c31fd1613de` | `org.apache.solr.gcs.GCSInstallShardTest` (`:solr:modules:gcs-repository`) | `B94347D2600CC75D` | BUILD SUCCESSFUL, 4m 46s | 6, 0, 0, 0 (02:36Z) |
| main `13c693d805f` | same | same | same | BUILD SUCCESSFUL, 2m 23s | 6, 0, 0, 0 (02:39Z) |

The seed appears in the Gradle log of each run (`tests.seed=` lines). Logs: `gates/ci-red-rca/logs/` on vm2. Per-run rc: `summary.txt` (rc=0 for all four).

## Outcome

- **#5083 (SOLR-11483).** The failure did not recur at the recorded seed on either the PR head or main. The 2026-10-10 failure is not reproduced, so nothing points at the PR. It is also not explained: one run and one seed cannot say whether it is a flake. The RCA verdict (b) stays. The settling result does not confirm a flake.
- **#5069 (SOLR-3657).** The failure did not recur at the recorded seed on either side. The class ran all six tests on both sides. The 2026-10-10 failure is not reproduced.
- **#5081 (SOLR-7504).** Not settled here. Its verdict (c) is an infrastructure port collision, and the cheapest honest path is to re-run the failed job. That re-run was not requested in this round and was not done.

## Limits

- One seed per side. A pass at one seed does not rule out a seed-dependent failure. The original job failed at its own seed, which is the same seed here, so a pass is evidence that the seed alone does not trigger the failure in this environment.
- Runs were on vm2 (Linux aarch64, JDK 21.0.12.1). The 2026-10-10 runs were on other infrastructure. A host difference is possible.
- Only the named class ran. The rest of the job's tests were not repeated.
- No PR, Jira, or GitHub write.
