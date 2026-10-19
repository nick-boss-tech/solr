# SOLR-17719: TestManagedSchemaWithMultipleAdd waits for a newSearcher event

## What happens today

The test adds a field, sends 1000 documents that use it, then sleeps for the autoSoftCommit interval plus 500 ms before querying. On a slow machine the searcher opens later than that, so the count query runs too early and the test fails sporadically.

## What this change does

The sleep is replaced with a wait for a newSearcher event on the test collection. The test registers a `TestInjection` searcher hook, and the wait uses a semaphore: after the bulk add it blocks until a new searcher opens for the collection, with a 20 second timeout that fails the test. The assertion is unchanged.

Behavior change: none in product code. Only the test's wait changes.

## Proof

- `TestManagedSchemaWithMultipleAdd`: 1 of 1 passes at head bd878b38f8 on 2026-10-19, 0 failures, 0 skipped, about 25 s. Log `/workspace/gates/logs/vm4/solr-17719-gate.log`, XML `/workspace/gates/logs/vm4/solr-17719-TEST-TestManagedSchemaWithMultipleAdd.xml`.
- `:solr:core:spotlessJavaCheck` passes in the same run.
- No base-versus-change run: the failure is sporadic and this host did not reproduce it, so there is no fail-before result to show. The change removes a fixed sleep, which is the cause the ticket names.

## Question for the maintainer

Should the 20 second timeout be tied to the autoSoftCommit setting, or is a fixed bound enough?
