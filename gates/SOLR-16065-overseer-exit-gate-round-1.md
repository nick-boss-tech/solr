# Gate handoff: SOLR-16065 Overseer exit leadership check, from vm2

Status: OPEN for the gate phase. This is a handoff note, not a new assignment. The governing assignment is `assignments/solr-16065-overseer-leader-check-thread-round-1.md` (two phases: phase 1 implementation and verdict, phase 2 gate under a separate claim). Phase 1 is not finished. The phase 1 report `reports/solr-16065-implementation-round-1.md` does not exist yet. No public writes, no PR, no JIRA action.

- JIRA: https://issues.apache.org/jira/browse/SOLR-16065 (open, unassigned, no comments, no linked PR at the time of the check).
- Base: `upstream/main` `83d560030fde`.
- Fork branch: `nick-boss-tech/solr:solr-16065-overseer-exit-main`, head `94d9e6a80657`. Pushed as a new branch, not a PR. The assignment names `solr-16065-submit`; rename or re-push before the phase 1 report, and record the head there.
- Commit author Nick Shanin, no trailers.
- Files changed: `solr/core/src/java/org/apache/solr/cloud/Overseer.java` (exit check runs inline in `run()`'s `finally` after `Thread.interrupted()`, replacing the separate `OverseerExitThread`), `solr/core/src/test/org/apache/solr/cloud/OverseerTest.java` (new `testOverseerExitRemovesLeaderNodeBeforeClose`), `solr/test-framework/src/java/org/apache/solr/SolrIgnoredThreadsFilter.java` (removes the dead `OverseerExitThread` ignore entry), `changelog/unreleased/SOLR-16065-overseer-exit-check-inline.yml`.
- Gate script from the vm2 run: `gates/artifacts/SOLR-16065-overseer-exit-round-1/gate-16065.sh`. Usage `<worktree> <head> <base>`. It takes the host Gradle lock and writes logs under `/workspace/logs/<host>/gate-16065/`. Change the log path to match the host.

## Phase 1 still owed

1. Census: `~/workspace/tools/solr-work.sh SOLR-16065` (the assignment's step 1). Not run.
2. Verdict with evidence: git log and blame on the `Overseer.run()` interruption comment at the base commit. Not done. The blame on the fix commit only shows the lines I changed. The verdict must say whether anything still interrupts the main thread's waits. Known: `close()` interrupts the updater thread, and `doClose()` joins it with no timeout.
3. Phase 1 report at `reports/solr-16065-implementation-round-1.md`.

## Verified on vm2 (head `94d9e6a80657`)

- Changelog YAML parses: rc=0.
- Error Prone compile: `BUILD SUCCESSFUL`, rc=0.
- Focused OverseerTest and OverseerRolesTest, seed `16065C0FFEE16065`: OverseerTest 14 run, 1 skipped, 0 failures; OverseerRolesTest 1 run, 0 failures. Logs were overwritten by a later run, so rerun to keep evidence.

## Not verified

- Tidy: no complete result on the final head. The tidy log was truncated. The earlier tidy run passed on the pre-amend commit, and the amend folded in its one whitespace change. Re-run `./gradlew tidy` and confirm a clean `git status`.
- `:solr:core:check -x test`: one rc=0 line was seen, but its log was rewritten by a later run. Re-run it.
- Fail-before on base: NOT reproduced. The new test passed on the base `Overseer.java` in 3 of 3 runs, so it does not discriminate. Do not write a fail-before claim. Either make the test deterministic, for example by holding the exit check on a latch, or record it as a behavioral pin.
- Test base class: `OverseerTest` extends `SolrTestCaseJ4`. The assignment requires new tests to extend `SolrTestCase` or `SolrCloudTestCase`. The new method is in an existing class, so decide whether it needs its own class.

## Risk to review

`doClose()` joins the updater thread with no timeout. The check is now inline in that thread, so a ZK call that blocks during shutdown blocks `close()`. The old separate thread avoided that. The phase 1 verdict should decide whether this is acceptable or needs a bounded wait.

## Harness issue

On vm2, the gate script kept re-running tidy, focused, and check steps after printing `GATE DONE`, and overwrote its logs. The run was killed. Check for stray `gate-16065.sh` or Gradle processes before starting, and use a new log tag per run.

## Next steps

1. Run phase 1 (census, verdict, report). Stop if the verdict says the thread is still needed.
2. Phase 2 under a separate claim, on the exact pushed head from the phase 1 report.
3. Do not open a PR or post to JIRA without the owner's go.
