# DistributedExpandComponentTest thread leak: settling run, round 1

Classification: **(b), seed-clean on both trees.** At seed `85F9F77FD74C1D41`, `DistributedExpandComponentTest` passed on branch_9x and on apache main, with no thread-leak error. This is one run per tree at one seed, so it does not rule out a timing-dependent leak. The CI sighting remains unexplained.

## Sighting

PR 5198 (SOLR-17953 branch_9x backport, `dae5ac66f9c`, merged 2026-10-25) failed its Crave test job 121178733450 at seed `85F9F77FD74C1D41`. No test method failed. `DistributedExpandComponentTest` failed at class level with `ThreadLeakError`: one thread from SUITE scope (`httpShardExecutor-420-thread-2`, RUNNABLE at teardown) leaked. The PR changes backup hidden-file filtering and does not touch the expand path.

## Runs

Host vm6, JDK 21.0.12.1+1, each run through `pool/bin/sweep-run.sh` under the host lock. Command: `--no-daemon :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.DistributedExpandComponentTest -Ptests.seed=85F9F77FD74C1D41 -Ptests.file.encoding=ISO-8859-1 --no-build-cache`.

| Tree | Head | Worktree | Result | Tests / fail / err / skip | Log and XML |
|---|---|---|---|---|---|
| branch_9x | `dae5ac66f9c821d0edb26ca62493a1896c001d3b` | `/workspace/worktrees/dx-9x-vm6` | rc 0, BUILD SUCCESSFUL (34 tasks) | 1 / 0 / 0 / 0 | `/workspace/gates/logs/vm6/dx-settling/dx-9x-85F9.log`, `dx-9x-85F9.xml/` |
| apache main | `ee2f1bf2e0109956145d7fd995da63c4091a589f` | `/workspace/worktrees/dx-main-vm6` | rc 0, BUILD SUCCESSFUL (36 tasks) | 1 / 0 / 0 / 0 | `/workspace/gates/logs/vm6/dx-settling/dx-main-85F9.log`, `dx-main-85F9.xml/` |

Both logs have the seed `85F9F77FD74C1D41` and no `ThreadLeak` or "leaked" text. Both XML files show one test case each.

## Limits

- One run per tree at one seed. A timing-dependent leak can pass a single run, so this result does not show the leak cannot happen.
- This is a local run, not the CI runner. The local gate does not match the CI environment exactly. The run used the assignment's flags (`-Ptests.file.encoding=ISO-8859-1`), and I did not verify the security-manager setting in the output.
- The CI failure's stack was not reproduced, so nothing here identifies the leaking thread.
- No fix, branch, Jira text, or GitHub text. Per the assignment, a ticket is the owner's call after this classification.

## Suggested next step

If the owner wants more evidence, the next run is the same class on branch_9x across several seeds, with the CI seed as one of them. That is a new assignment, not part of this one.
