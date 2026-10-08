# solr-4502-submit

- Branch: origin/solr-4502-submit
- Head: 4491f5162c1d (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files, +49. `solr/core/src/java/org/apache/solr/core/CoreContainer.java` (guard in the four-argument `create`: throws SERVER_ERROR naming `load()` when `shardHandlerFactory == null`, before `inFlightCreations` is touched), `solr/core/src/test/org/apache/solr/core/TestCoreContainer.java` (new `testCreateBeforeLoadIsRejected`), changelog `changelog/unreleased/SOLR-4502-create-before-load.yml` (`type: fixed`, author Nick Shanin), and `SOLR-4502-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Ready for review
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no test run, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-4502-TESTING.md` is labeled "hypothetical reproduction (nothing was compiled or run)" and is treated as unverified.

## Findings (ranked)

1. **LOW, verified. The TESTING note misplaces the assignment.** It says `shardHandlerFactory` is "set at the top of `load()`, before any core is created". The assignment is at `CoreContainer.java:789`, inside `loadInternal()` after the log watcher and cluster plugin source setup (`load()` starts at line 743). Nothing in lines 743-789 creates a core, so the guard's conclusion holds; the note's description is wrong. Documentation only. Not patched.

2. **Verified (checked, no issue). The guard is placed as described.** It sits before `inFlightCreations` is touched (`CoreContainer.java:1501-1507`). The two-argument `create(String, Map)` delegates to the four-argument method (lines 1487-1488), so the test's call reaches the guard. `ErrorCode` and `SolrException` are imported (`CoreContainer.java:84-85`). The test's `CONFIGSETS_SOLR_XML` constant (line 498) and `java.util.Map` import (line 34) exist.

3. **Verified (checked, no issue). Existing call sites are unaffected.** Across `solr/`, every `new CoreContainer(` site either calls `load()` (directly or via a helper such as `EmbeddedSolrServer`'s `load(...)`), or, in `DirectoryFactoryTest.java` (three containers, no `load()`), never calls `create`. The guard does not change them.

4. **Verified (checked, no issue). Fail-before is a real failure.** Without the guard, `create` proceeds into core construction on an unloaded container, so `expectThrows(SolrException.class, ...)` either fails (no exception, or an exception of another kind) or the `contains("load()")` assertion fails. Either way the test fails on `main`, not because an API is missing.

5. **Hypothesis, LOW. Behaviour change for an unloaded container.** Any caller that uses `create` on a container that was never `load()`ed would now get an error instead of a core that later fails on search. The grep above found none in the tree. Not checked beyond `solr/`.

## Owner calls (not decided here)

None needed for the stated case.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before run, no Spotless, no Error Prone.
- Whether `SolrException.getMessage()` returns the raw message (the test relies on it containing `load()`). Assumed, not traced.
- Downstream code outside `solr/` that might call `create` on an unloaded container: not searched.
- `SOLR-4502-TESTING.md` is treated as unverified. Left in place.
