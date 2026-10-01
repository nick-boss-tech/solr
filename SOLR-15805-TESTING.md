# SOLR-15805 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

During servlet startup, `CoreContainerProvider.init()` caught **all**
throwables from `createCoreContainer()`, logged "Could not start Solr", but
only rethrew `Error`s — any other exception (`SolrException`, `IOException`,
...) was swallowed. `cores` then stayed null and every subsequent request
failed via `checkReady()` with a misleading
`UnavailableException("...CoreContainer has shut down.")`: the node stayed up
but non-functional — a zombie that had to be killed externally.

Fix (`solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java`):
the catch block now fails fast for every throwable. `Error`s are rethrown
as before, `RuntimeException`s (including `SolrException`, preserving their
original error codes) are rethrown as-is, and checked throwables are wrapped
in a `SolrException(SERVER_ERROR)`. Rethrowing marks servlet-context
initialization as failed instead of leaving a null-`cores` zombie. The old
"catch this so our filter still works" swallow is deliberately overridden —
that is the behavior the ticket asks to change.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- Unit-level: exercise `init()` with a `createCoreContainer()` path that
  throws a non-`Error` (e.g. bad solr-home config) and assert the exception
  escapes instead of `cores` ending up null. `init(ServletContext)` is
  private, so the test likely needs a subclass/package seam, or it can go
  through `contextInitialized(ServletContextEvent)` with a stubbed
  `ServletContext`.
- Existing servlet-startup / CoreContainer test suites for regressions —
  especially any test that relied on the old swallow-and-continue behavior
  (none is expected, but verify).

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
