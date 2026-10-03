# SOLR-16356 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

If a deleteByQuery (e.g. the DocExpiration auto-expire background thread)
ran while the core was closing, `UpdateLog.openRealtimeSearcher()` called
`SolrCore.openNewSearcher()`, which throws
`SolrCoreState.CoreIsClosedException` on a closed core. The generic
`catch (Exception e)` then logged a scary ERROR with a full stack trace
("Error opening realtime searcher"), even though the DBQ was already recorded
in the tlog and a closed core needs no new searcher.

Both `openRealtimeSearcher()` and `deleteAll()` (same pattern, "Error
opening realtime searcher for deleteByQuery") now catch
`SolrCoreState.CoreIsClosedException` separately and log at debug level
instead of ERROR with a stack trace. All other exceptions keep the old
ERROR behavior. In `openRealtimeSearcher()` the closed-core path now also
returns before the id caches are cleared, like the generic failure path does
(round-3 review correction: the first version fell through and cleared the
caches although no fresh searcher had been opened, which would let a realtime
get on a still-closing core miss tlog pointers; `deleteAll()` clears the
caches after a failure on `main` already, so it is unchanged). This matches the
reporter's proposed fix ("if we are closing the core then we don't really care about
failure to open a new searcher") and existing precedent in
`SolrCore.java:3432`, which swallows `CoreIsClosedException` with "no
problem this core is already closed" on reload.

Files changed:
- `solr/core/src/java/org/apache/solr/update/UpdateLog.java`

No import needed: `SolrCoreState` is in the same package
(`org.apache.solr.update`).

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Test added (round-3 patch pass, **not compiled or run**):

- `UpdateLogClosedCoreTest#testOpeningRealtimeSearcherOnClosedCoreIsNotAnError`
  (new class, same tlog config as `UpdateLogTest`): grabs the `UpdateLog`,
  closes the core with `deleteCore()`, then calls `openRealtimeSearcher()` and
  `deleteAll()` and asserts no ERROR event is logged by `UpdateLog` (using the
  test framework's `LogListener`). Before this change the generic catch logs
  `Error opening realtime searcher`. It does not cover the `return` (cache
  retention) or a genuine non-closed failure still logging ERROR.

Queued for the verification run:
`org.apache.solr.update.UpdateLogClosedCoreTest` and
`org.apache.solr.update.UpdateLogTest`, with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Not changed, for a scope decision: the ticket's log also shows
  `DocExpirationUpdateProcessorFactory ... Runtime error in periodic deletion of
  expired docs: SolrCoreState already closed` (a plain `SolrException` from the
  commit, not `CoreIsClosedException`). Quieting that thread when the core is
  closing belongs in `DeleteExpiredDocsRunnable`; leave it for a follow-up unless
  the whole ticket should close in one PR.
- Changelog fragment added: `changelog/unreleased/SOLR-16356.yml`.
- Remove this file before opening the upstream PR.
