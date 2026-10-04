# SOLR-12007 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12007 - "When a SolrCore is closed, cleanupOldIndexDirectories is called in a background thread that will race with DirectoryFactory close" (Mark Miller). One sentence, no comments, no repro.
- Branch: `solr-12007-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`SolrCore.close()` calls `cleanupOldIndexDirectories(false)`, which starts a daemon thread (`OldIndexDirectoryCleanupThreadForCore-<name>`) that calls `DirectoryFactory.cleanupOldIndexDirectories`.
`close()` then goes on to close the `DirectoryFactory`, so the background cleanup may run against a closed factory (or be cut off mid-way). Still true on main (`SolrCore.java` ~L1840, ~L3496).

## What the branch changes
- `SolrCore`: `cleanupOldIndexDirectories(boolean reload)` keeps its public signature and behavior (background thread) by delegating to a new private `cleanupOldIndexDirectories(reload, async)`;
  `close()` now calls it with `async=false`, so the cleanup completes before the factory is closed.
- `SolrCoreCleanupOnCloseTest`: a `MockDirectoryFactory` subclass records the thread that calls `cleanupOldIndexDirectories`; after `deleteCore()` the test asserts the cleanup ran and not on a thread named `OldIndexDirectoryCleanupThreadForCore-*`.

## What was guessed (verify these first)
1. **This is a race fix with no race reproduced.** The test proves "not backgrounded", not that a failure was observed. It fails before the fix by construction (thread name) but is not a data-loss reproduction.
2. **Test wiring**: sets `solr.directoryFactory` in `@BeforeClass` after the base class's own randomization (superclass `@BeforeClass` runs first); relies on `initCore("solrconfig.xml","schema.xml")` reading `${solr.directoryFactory}`
   (seen in `solrconfig.xml` line 41). `deleteCore()` closing via `CoreContainer` shutdown threads is assumed to reach `SolrCore.close()` with `coreStateClosed` true; if it does not, the "ran at close" assertion fails.
3. **Behavior change**: close now blocks for the directory scan/delete of old `index.*` dirs. For huge or slow filesystems this adds latency to core close/unload; consider whether it should be bounded.
4. `getNewIndexDir()` is read inside the close path as before; no new access to closed resources is intended but unverified.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.core.SolrCoreCleanupOnCloseTest" --tests "org.apache.solr.core.SolrCoreTest"
```
Fail-before: revert only `SolrCore.java`; the thread-name assertion should fail.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
