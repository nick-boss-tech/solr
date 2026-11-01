# Gate: SOLR-16065 Overseer exit leadership check, round 1

Status: OPEN, unclaimed. Implementation exists and is not reviewed; the gate is incomplete. No public writes, no PR, no JIRA action.

- JIRA: https://issues.apache.org/jira/browse/SOLR-16065 ("Run checkIfIamStillLeader outside of a Thread in the overseer"). Open, unassigned, no comments, no linked PR at the time of the check.
- Base: `upstream/main` `83d560030fde` (apache/solr).
- Fork branch: `nick-boss-tech/solr:solr-16065-overseer-exit-main`, head `94d9e6a80657` (new branch, not a PR). Commit title: "SOLR-16065: check leadership on Overseer exit in the exiting thread".
- Files changed: `solr/core/src/java/org/apache/solr/cloud/Overseer.java` (the exit check runs inline in `run()`'s `finally` after `Thread.interrupted()`, replacing the separate `OverseerExitThread`), `solr/core/src/test/org/apache/solr/cloud/OverseerTest.java` (new `testOverseerExitRemovesLeaderNodeBeforeClose`), `solr/test-framework/src/java/org/apache/solr/SolrIgnoredThreadsFilter.java` (removes the now-dead `OverseerExitThread` ignore entry), `changelog/unreleased/SOLR-16065-overseer-exit-check-inline.yml`.
- Gate script from the vm2 run: `gates/artifacts/SOLR-16065-overseer-exit-round-1/gate-16065.sh`. It takes `<worktree> <head> <base>`, takes the host Gradle lock, and writes logs under `/workspace/logs/<host>/gate-16065/`. Change the log path to match the host before running.

## Verified on vm2 (head `94d9e6a80657`)

- Changelog YAML parses: rc=0.
- Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`): `BUILD SUCCESSFUL`, rc=0.
- Focused `OverseerTest` and `OverseerRolesTest` with seed `16065C0FFEE16065`: OverseerTest 14 run, 1 skipped, 0 failures; OverseerRolesTest 1 run, 0 failures. Logs were overwritten by a later run, so this needs a fresh run to keep as evidence.

## Not verified

- Tidy: no complete result on the final head. The tidy log was truncated, and the earlier tidy run (before the amend that folded in its whitespace change) passed. Re-run `./gradlew tidy` and confirm `git status` is clean afterward.
- `:solr:core:check -x test`: a rc=0 line was seen once, but its log was rewritten by a later run. Re-run it.
- Fail-before on base: NOT reproduced. The new test passed on the base `Overseer.java` in 3 of 3 runs. The base code deletes the leader node in the exiting thread before `close()` returns in those runs, so the test does not discriminate. Do not write a fail-before claim. Either make the test deterministic (for example, hold the exit check on a latch) or record the test as a behavioral pin only.

## Risk to review

`doClose()` joins the updater thread with no timeout. The leader check is now inline in that thread, so a ZK call that blocks during shutdown blocks `close()`. The old separate thread avoided that. Review whether the inline call is acceptable, and whether `close()` needs a bounded wait or the check needs a timeout.

## Known harness issue

On vm2, the gate script kept re-running tidy, focused, and check steps after printing `GATE DONE`, and its logs were overwritten. The run was killed. Check for stray `gate-16065.sh` or Gradle processes before starting, and use a new log tag per run.

## Next gate host

1. Fetch `nick-boss-tech/solr:solr-16065-overseer-exit-main` and confirm the head is `94d9e6a80657` and the base is `83d560030fde`. Stop and report if either differs.
2. Decide the fail-before question first. If the test stays as is, record the fail-before result as not reproduced.
3. Run the gate script with a fresh log tag. Confirm each `_rc=` line, and copy the fresh XML for each test run.
4. Confirm `git status` is clean after tidy.
5. Report the result on the board. Do not open a PR or post publicly; the owner decides.

Attribution: commit has no trailers, per owner instruction.
