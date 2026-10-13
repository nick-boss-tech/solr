# SOLR-18010 main-side investigation: check, round 1

Trigger: pr-prepare tip `0007e720e6a`, recorded from `c2352b9`. The new commit `da8bf44a853` adds a main-side investigation of the not-green SOLR-18010 review-patch gate run. Round claim: `claims/pool-pr-prepare-tip-0007e72-round-1.md`.

Scope: a read-only check of one claim in the investigation. No gate relaunched, no status line changed, no PR touched, no build, test or Gradle run. No subagents were started, since the new commit carries no assignment and the one claim is a grep.

## What the investigation says

- The fail-before leg at the merge-base failed four tests. The new test failed with `previous security.json was changed`, `expected.length=88 actual.length=0`.
- The other three failures (`testConcurrentEditsToLocalSecurityJson`, `testConcurrentPersistConfLeavesOneWholeDocument`, `BasicAuthStandaloneTest.testBasicAuth`) come from the parent PR's own `persistConf` change, which the revert to the merge-base removed.
- The classification is a gate design problem: the step 6 baseline should have been the parent, not the merge-base.
- The classification does not rest on the missing vm2 log, because the four names and the new test's assertion text are on the branch.

## The check

Read-only grep at `origin/pr-prepare` for each named item:

| Item | In the gate file | In the queue monitor |
|---|---|---|
| `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile` | 4 | 1 |
| `testConcurrentEditsToLocalSecurityJson` | 2 | 1 |
| `testConcurrentPersistConfLeavesOneWholeDocument` | 2 | 1 |
| `testBasicAuth` | 2 | 2 |
| `previous security.json was changed` | 2 | 0 |
| `expected.length=88 actual.length=0` | 2 | 0 |
| `authentication config should not contain internal metadata` | 2 | 0 |

Verdict: the claim holds. The four names and the new test's assertion text are on the branch, in the gate file. The queue monitor carries the names but not the assertion text, which the claim did not say it did.

## What this does not settle

- Whether the new test passes at the parent. The corrected parent run is claimed by vm2 and is recorded as BUILD SUCCESSFUL in the assignment, with all six tests passing. The record gives no seed and no head for it.
- Whether the three extra failures match the premise. The record does not give their failure texts. The settling fact is the vm2 log, which is off the branch.
- The route choice for the test fix. That is in `reports/18010-fail-before-route-round-1.md`, and it waits for your go-ahead.
