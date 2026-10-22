# Review: SOLR-17719, branch solr-17719-semaphore-newsearcher (unit 35)

Head reviewed: 732d3fbcc47358dfa532cbdf77d6205fde6a0368. Base: c8ab71430f. Ticket: SOLR-17719, "TestManagedSchemaWithMultipleAdd timing related failures".

## Pass 1

- Ticket fit. The ticket reports timing-related failures in `TestManagedSchemaWithMultipleAdd`. The branch replaces a fixed sleep with a wait for a `newSearcher` event, via a `TestInjection` hook, so the test waits for the searcher the test depends on. That fits the ticket.
- Scope. Test-only change in one file (plus a draft PR description file in `drafts/SOLR-17719/`, which is not code). No production code.
- Finding, major: no fail-before evidence. The branch shows that the test passes at head; it does not show that the old sleep version fails under the timing the ticket describes. The proof leg was not run because the change is timing-only. Recorded, not repaired.
- Finding, minor: the hook is installed in `@BeforeClass` (`TestInjection.newSearcherHook(newSearcherHook)`) and never cleared. A static hook leaks to later tests in the same JVM. Recorded, not repaired (a cleanup method is a test change outside the repair scope).
- Repairs: none.

Regate: `gates/SOLR-17719-review-pass-1.md`, GATE GREEN at 732d3fbcc4 on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 732d3fbcc4. - Pass 1 major finding, unchanged: no fail-before evidence for the timing fix (the proof leg was not run). Not repaired.
- Pass 1 minor finding, unchanged and confirmed in the file: `TestInjection.newSearcherHook` is installed in `@BeforeClass` and no code clears it (`TestManagedSchemaWithMultipleAdd.java`, no `@AfterClass`). Not repaired.
- Repairs: none.

Regate: `gates/SOLR-17719-review-pass-2.md`, GATE GREEN at 732d3fbcc4 on vm4.
