# Report: SOLR-6213, repeat the rejoin regression test on another host

- Assignment: `assignments/solr-6213-rejoin-repeat-round-1.md`.
- Run by: phone-agent (vm1), 2026-10-23, on this host's local disk. Builds ran serialized under the vm1 queue lock through solr-gradle.sh (the host equivalent of `pool/bin/sweep-run.sh`); logs and JUnit XML are under `~/workspace/tools/assign-logs-2026-10-23/` on vm1. Counts below are from the saved JUnit XML of each run.
- Branches: `solr-6213-rejoin-fix-test` at `64624f4726f49dc723e319f6c6aa52ba81f52189`; `solr-6213-rejoin-hook` at `e08ab612963d681574063a843b1fbd2354866d10` (live head at run time); `solr-6213-rejoin-fix` at `bcea4f23c1944cf0383193c66fccdb9e6e624567`.

## Item 1: rejoin test on the test branch, 5 runs, random seeds

`ShardLeaderElectionRejoinTest`, one invocation per run, no seed set:

| Run | Seed | Tests | Skipped | Failures | Errors |
| --- | --- | --- | --- | --- | --- |
| 1 | 5FE56C51E90074E7 | 1 | 0 | 0 | 0 |
| 2 | A6D47D55794E9781 | 1 | 0 | 0 | 0 |
| 3 | D92BF51F2BA751FB | 1 | 0 | 0 | 0 |
| 4 | 9CCC5FBF6D1E49B2 | 1 | 0 | 0 | 0 |
| 5 | EFB69B5E2F068956 | 1 | 0 | 0 | 0 |

5 of 5 passed. No run's log or XML reports a ThreadLeakError.

## Item 2: the test on the hook branch (main plus the sync-failure hook)

The test file was copied into the hook-branch checkout as a working-tree file only (not committed, not pushed) and run twice:

| Run | Seed | Tests | Failures | Failure text |
| --- | --- | --- | --- | --- |
| 1 | 51107B2B66B697DB | 1 | 1 | stack grew by 12 frames over 4 rejoins |
| 2 | 739FDDF2FC117912 | 1 | 1 | stack grew by 12 frames over 4 rejoins |

Both runs failed with the recorded growth figure: 12 frames per 4 rejoins, as the PR draft states.

## Item 3: neighboring election tests on the fix branch

One invocation at seed 5474E81BF47DAF8 on `solr-6213-rejoin-fix`:

| Class | Tests | Skipped | Failures | Errors |
| --- | --- | --- | --- | --- |
| DeleteReplicaTest | 7 | 0 | 0 | 0 |
| ZkControllerTest | 11 | 2 | 0 | 0 |
| ReplaceNodeTest | 4 | 0 | 0 | 0 |
| TestCloudRecovery | 2 | 0 | 0 | 0 |

Total: 24 tests, 2 skipped, 0 failures, 0 errors.

## Conclusion

The two-host evidence now agrees: the rejoin test passes consistently on the test branch, the growth figure reproduces exactly on the hook branch, and the neighboring election classes pass on the fix branch. The PR draft's proof and limits statements stand as written. Findings only; no code was changed and nothing was pushed to the three branches.
