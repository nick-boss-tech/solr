# solr-12991-submit

- Branch: origin/solr-12991-submit
- Head: 1a86966179e1 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `RecoveryStrategy.java` (`pingLeader`: both failed-connect branches change from `log.error` without the exception to `log.warn` with the exception attached), `RecoveryStrategyLeaderUnreachableLogTest.java` (new, one cloud test that points the leader's base URL at a dead port and asserts a WARN with an `IOException` in the cause chain), changelog `SOLR-12991-recovery-leader-root-cause.yml` (`type: fixed`). Logging change only; no control-flow change.
- Verdict: Nearly (unchanged from the bulk verdict). The root-cause logging is correct. Before this is settled, the log level and the per-retry stack-trace volume need an owner decision, because the retry loop is unbounded.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. The cloud test was read, not executed. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-12991-review.md` (Nearly) reviewed this head (`1a86966179e`). No delta.

- Bulk F1 (LOW, confirm the log level for routine retry failures): **confirmed as an owner call, and the severity is raised to MEDIUM.** The bulk note treated the cost as a level question. The retry loop has no bound, so the cost is volume over time. See finding 1.
- Bulk point that the test and changelog encode WARN (test at `RecoveryStrategyLeaderUnreachableLogTest.java:56-85`, changelog `:1-8`): **confirmed.**

## Findings (ranked)

1. **MEDIUM, verified (code path). Each retry now writes a WARN with a full stack trace, and the retry never stops while the leader is unreachable.** `pingLeader` runs `while (true)` (`solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java:789`). It leaves only on success, on a non-IO failure, or when the replica is closed (`isClosed()` at `:804`). Each failed connect logs at WARN with the exception (`:830-832`, and `:835-837` for the `Exception` branch) and then sleeps 500 ms (`:832`, `:837`). For a leader that stays unreachable, that is about two WARN stack traces per second per recovering replica, for as long as the condition holds. The old code logged the same cadence at ERROR without the stack trace. The JIRA maintainer comment, as the bulk review summarizes it, says connect failures during cluster transitions are routine. That is the volume concern. Verified by reading the loop. The per-trace size and the real log volume were not measured.
   - Proposed fix (not applied; owner call 1): log the first failure of a `pingLeader` call at WARN with the exception, and log later identical failures at DEBUG without it. Or keep WARN and the cause for every attempt and accept the volume. The test reads only the first WARN event (`RecoveryStrategyLeaderUnreachableLogTest.java:71-78`), so it works with either choice.

2. **LOW, verified. The level drops from ERROR to WARN.** Any alert or log filter on ERROR for "Failed to connect leader" stops matching. The changelog says "at WARN level", which records the change, but not that it was ERROR before. Owner call 2.

3. **LOW, verified (checked, no issue). Both failed-connect branches are changed the same way.** The `IOException` branch and the `Exception` branch with an `IOException` cause both log at WARN with `e`. The non-IO branch still returns the leader without logging, as before. Control flow is unchanged.

4. **LOW, hypothesis. The test's port choice can race.** `unusedPort()` (`RecoveryStrategyLeaderUnreachableLogTest.java:90-93`) binds port 0, reads the port, and closes the socket before the leader URL is set. Another process could take the port in between, and the connect would then reach a live non-Solr listener instead of a refused connection. That could make the WARN carry a different cause or make the test flaky. Not run, so this stays a hypothesis.

5. **Checked, no issue.** The test restores the leader URL in `finally` (`setLeaderBaseUrl(... leader.getBaseUrl())`) and waits for the follower to become active again. The `@SuppressForbidden` for log4j is scoped to the test method and matches the test's need.

## Owner calls (not decided here)

1. **Level and volume.** Which of these should `pingLeader` do? (a) WARN with the cause on every attempt, as the branch does, accepting about two traces per second per replica for as long as the leader is unreachable. (b) INFO with the cause on every attempt, as the JIRA maintainer suggested per the bulk review. (c) WARN with the cause on the first failure, and DEBUG for the rest. This review does not choose.
2. **ERROR to WARN.** Is it acceptable that alerts keyed to ERROR stop matching this message? If not, option (b) or (c) still changes the level, so the owner should confirm the level either way.

## Proposed fixes (not applied; the owner decides)

- Finding 1: option (c) above. Track whether the current `pingLeader` call has already logged a failure, and attach the exception only on the first. Keep the test as it is.
- Finding 2: follows from owner call 2. Mention the old ERROR level in the changelog if WARN is kept.

## Not checked

- Nothing compiled, formatted, or run. The cloud test was read, not executed.
- The size of a connect-failure stack trace and the real log volume were not measured (finding 1).
- The JIRA maintainer comment was not re-read; the bulk review's summary stands as context.
- Whether `isClosed()` can be false for a long time during a normal shutdown path was not traced.
