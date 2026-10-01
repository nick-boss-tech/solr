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
ERROR behavior. The id-cache clearing after the try/catch still runs in the
closed-core path (harmless in-memory maps). This matches the reporter's
proposed fix ("if we are closing the core then we don't really care about
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

Suggested tests (not written):

1. Unit test on `UpdateLog.openRealtimeSearcher()` with a closed core
   (mock `UpdateHandler`/`SolrCore` with `isClosed()=true` and
   `openNewSearcher` throwing `CoreIsClosedException`): assert no ERROR is
   logged (log capture) and no exception escapes.
2. Same for `deleteAll()` (currently for testing only).
3. Regression: a genuine `openNewSearcher` failure (non-closed core) still
   logs ERROR as before.
4. Existing UpdateLog test suites.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
