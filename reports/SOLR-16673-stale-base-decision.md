# SOLR-16673: the two Linux gate blockers come from a stale base (vm5, 2026-10-19T04:52:42Z)

## What was concerning

Both round-2 gates stopped at Error Prone (step 2) on two `DirectInvocationOnMock` warnings in `CoreReplicationAPITest.java`. The step 1 tidy drift (`oshai-logging`) had already come from the same stale base. Both looked like branch-independent failures, and the fork's `main` is the base the branches were cut from.

## Decision ladder

**Rung 1, read the code and its history.**
- The warnings are at lines 213 and 217 of `CoreReplicationAPITest.java`, from `mockCore.getResourceLoader()`. The check `DirectInvocationOnMock:WARN` is enabled in `gradle/validation/error-prone.gradle`, and the errorprone task runs with `-Werror` by default.
- The fork base `e044bf20b4` (the merge-base) has the same code.
- Upstream `apache/main` fixed this in `78891191f4` ("SOLR-18226: test, fix mock error-prone violation"), which is on `apache/main` and not on the fork.
- `oshai-logging` is absent from `apache/main`'s `gradle/libs.versions.toml`, so the tidy drift is gone upstream too.
- The fork's `main` (`498a09aea9`) is 137 commits behind `apache/main` (`c8ab71430f`). Both branches were cut from `e044bf20b4`, so they are 137 behind as well.

**Rung 2, probe.**
- Error Prone compile on the merge-base `e044bf20b4` (scratch worktree, `base-errorprone` log): fails with the same three warnings in the same file. The failure is in the base, not in the branches.
- Rebasing both branches onto `apache/main` with `git rebase --onto apache/main 498a09aea9` applies cleanly (no conflicts), carrying only the ticket commits.

**Rung 3, stated intent.** The ticket and the PR target `apache/main`. A gate on a base 137 commits behind upstream does not test what would be merged, so the base is the problem, not the branches.

**Rung 4, recorded choice.**
- Re-gate both branches on `apache/main` (`c8ab71430f`). Publish the rebased heads under new names, so no force push is needed:
  - `solr-16673-uniquekey-apache` at `a494962d79` (2 ticket commits).
  - `solr-16673-rootfield-main` at `72e505cc20` (3 ticket commits).
- The old heads (`solr-16673-uniquekey` at `fce77f45a3`, `solr-16673-rootfield` at `e9ce052946`) stay as records.
- Stray branch to clean up (needs owner approval to delete): `solr-16673-uniquekey-main` at `2f9450c544`. It was pushed by mistake with fork-only commits; it is not the intended head.

**Rung 5, to the owner (one question).** The fork's `main` is 137 commits behind `apache/main`. Every pool gate on a fork base inherits the same stale-base failures, not only SOLR-16673. Fast-forwarding the fork's `main` to `apache/main` would drop the fork-only commits from `main`, which needs a force push. Question for Nick: should the fork's `main` be re-based onto `apache/main` (with the fork-only commits carried over), and should pool gates use `apache/main` as the base from now on?

## Actions

1. Gate `solr-16673-uniquekey-apache` and `solr-16673-rootfield-main` on `apache/main`. Logs under `/workspace/gates/logs/vm5/16673-*-apache/`.
2. Record the results in the gate files; the old-head gate files stay as records.
3. Owner: the question above, and approval to delete the stray `solr-16673-uniquekey-main`.
4. The vm1 rootfield claim names an earlier head; the new branch names need a new claim or the main side's decision.
