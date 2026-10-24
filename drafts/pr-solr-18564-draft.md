# PR description draft: SOLR-18564 fix

Status: DRAFT. Branch `corecontainer-shutdown-closes` at `d7caa8cb78`, gated 2026-10-22 (tidy clean, Error Prone compile clean, the new test 1 of 1 from fresh JUnit XML, `:solr:core:check -x test` rc=0, TestCoreContainer 25 tests no failures). Two things before opening: the branch predates the ticket, so a changelog entry citing SOLR-18564 must be added and the final head re-gated after that commit; opening needs Nick Shanin's explicit go.

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

SOLR-18564: Run the trailing closes in CoreContainer.shutdown when the close pool times out

## Summary

When the client close pool in `CoreContainer.shutdown()` times out, `ExecutorUtil` throws, and the throw aborted the rest of shutdown: the plugin closes, `containerHandlers.close()`, and the root metrics-context close never ran. Those calls are the only release path for the container's metrics contexts, so they leaked, and the JVM-global ObjectTracker charged the leak to a later, innocent test. This was seen in CI as 16 unreleased `SolrMetricsContext` objects, and the same signature appeared in the Solr 10.1.0 RC3 smoke tests (a closeThreadPool timeout in one test's teardown, the 16-context charge in another's, in the same run).

## Change

In `CoreContainer.shutdown()`, the close-pool await is wrapped in a catch-and-log on `RuntimeException`, so the trailing closes run regardless. `ExecutorUtil` is untouched; its throw is a contract other callers rely on.

## Tests

New test `CoreContainerShutdownTest.testShutdownRunsTrailingClosesWhenClosePoolTimesOut` blocks a shard-factory close on a latch and lets the real shutdown budget run out. On the base code it fails with the incident signature (the timeout throw plus an ObjectTracker teardown failure reporting 15 unreleased SolrMetricsContext objects); with the fix it passes. `TestCoreContainer` also passes (25 tests, no failures).

## Related

The RC3 smoke tester failed repeatedly on a small set of tests. The fixes are #5098 (RecoveryAfterSoftCommitTest), #5106 (TestCoordinatorRole.testNRTRestart), this PR (SOLR-18564), and two tracked in SOLR-18586 (TestTlogReplica) and SOLR-18587 (CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure), fixes in preparation.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
