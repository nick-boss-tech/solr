# ObjectReleaseTracker AutoCloseable fallback: research handoff

## Finding

Current Apache `main` (`79b23df58daac14b92cc35f23b8b81f72098b4ba`) still has a failure-cleanup gap in `solr/solrj/src/java/org/apache/solr/common/util/ObjectReleaseTracker.java:117-133`. `tryClose()` closes tracked `Closeable` objects and specially shuts down `ExecutorService` objects, but it skips an object that implements only `AutoCloseable`.

`SolrTestCase.java:96-109` invokes `ObjectReleaseTracker.tryClose()` from the test rule's `afterAlways` path when test errors have occurred. After a successful test class, it instead checks for unreleased objects and clears the tracker. This is a best-effort test-failure cleanup gap, not Solr's normal production shutdown path.

## Affected tracked types

- `SolrMetricsContext` (`solr/core/src/java/org/apache/solr/metrics/SolrMetricsContext.java:53, 61, 67, 284-288`) implements `AutoCloseable`, tracks itself, and its `close()` releases its tracker entry, closes registered resources, and clears the list.
- `ZkCollectionTerms` (`solr/core/src/java/org/apache/solr/cloud/ZkCollectionTerms.java:27, 36, 68-74`) implements `AutoCloseable`. Its `close()` closes every contained `ZkShardTerms` before releasing itself.
- `ZkShardTerms` (`solr/core/src/java/org/apache/solr/cloud/ZkShardTerms.java:74, 114, 184-193`) implements `AutoCloseable`; its close marks it closed, clears listeners, resets terms, and releases its tracker entry.
- `HttpJettySolrClient` is tracked and is already `Closeable` through `SolrClient`; its separate interrupted-stop path can throw before its tracker release at `HttpJettySolrClient.java:346-370`. Adding a generic `AutoCloseable` branch does not fix that path.

A blanket `AutoCloseable` branch therefore needs an order/lifecycle review. Preserve the existing `ExecutorService` special case, since it uses `ExecutorUtil.shutdownAndAwaitTermination`. Also consider that closing a tracked `ZkCollectionTerms` can close its separately tracked child `ZkShardTerms`; `tryClose()` iterates a concurrent key set, so the child may be reached again. `ZkShardTerms.close()` currently appears repeat-safe, but a test should pin the behavior or the implementation should prevent redundant close calls if needed.

## History

- `SOLR-17857` / merged PR #4226 changed `SolrMetricsContext` to reliably close its registered resources and added `ObjectReleaseTracker` tracking so leaked contexts fail tests.
- The `Closeable`/executor-only `tryClose()` implementation dates back to SOLR-15550 (2021) and was not widened in PR #4226.
- Relevant public records: [SOLR-17857](https://issues.apache.org/jira/browse/SOLR-17857), [PR #4226](https://github.com/apache/solr/pull/4226), and [current tracker source](https://github.com/apache/solr/blob/main/solr/solrj/src/java/org/apache/solr/common/util/ObjectReleaseTracker.java#L117-L133).

## Existing test coverage

`solr/core/src/test/org/apache/solr/common/util/TestObjectReleaseTracker.java` has five test methods. They cover tracking/releasing objects, unreleased-object reporting, anonymous classes, and asynchronous submitter traces. None calls `tryClose()` on an `AutoCloseable`-only object. The asynchronous test shuts its executor down directly, so it does not pin the executor branch in `tryClose()` either.

A focused run on local branch `solr-10928-submit` at `f2001e640a464e8e861ee3c202821b6dcc4bd497` passed 5 tests, 0 failures, 0 errors, 0 skips, with seed `3B1F3E98A7C2D144`. That is the current test suite passing, not proof of the missing branch. Error Prone was skipped by the local build because it was not run in CI mode.

## Relation to the reported CI leaks

This fallback can fail to clean up tracked metrics contexts after another test error and can leave their registered closeables alive during failure recovery. It is a plausible common leak amplifier. It does not by itself explain the initiating failures:

- ThinCache skips its explicit context close when an earlier assertion throws.
- CloudHttp2SolrClientTest hits a close-pool timeout that bypasses a `CoreContainer.shutdown()` cleanup tail.
- The custom-sort test shows an interrupted Jetty client stop; the precise path by which the factory metrics context remained tracked is still unresolved.

No conclusion that this one method is the root cause of all three CI reports is warranted.

## Candidate test plan

1. Add a minimal tracked object implementing `AutoCloseable` but not `Closeable`. Call `tryClose()` and assert its close method ran exactly once and it is no longer tracked.
2. Add a case where one `AutoCloseable.close()` throws and a later tracked closeable still receives its close attempt, preserving best-effort behavior.
3. Preserve and test `ExecutorService` handling through `ExecutorUtil.shutdownAndAwaitTermination`; do not let a generic close branch shadow it.
4. Cover nested tracked resources: either a `SolrMetricsContext` with a registered closeable, or a controlled parent/child pair like `ZkCollectionTerms` and `ZkShardTerms`. Assert each resource ends in the intended state even if parent and child both appear in the tracker snapshot.
5. Ensure each test clears the global tracker in `finally`, so the tracker test itself cannot pollute subsequent tests.
6. Run tidy, Error Prone, the focused tracker test class, and scoped `:solr:core:check -x test` / `:solr:solrj:check -x test`. Do not use a broad full suite as a substitute for these cases.

## Limits and unresolved questions

- This follow-up should target only the test-failure fallback and its regression tests. Do not change production shutdown behavior, `HttpJettySolrClient.close()`, `CoreContainer.shutdown()`, or unrelated cache/Cloud cleanup as part of this task.
- Before implementation, enumerate every tracked type that implements only `AutoCloseable` and check whether closing it during test failure is safe if its owner is still active or its parent is also tracked.
- The assignment is for any pool Linux host. No PR, Jira comment, or new Jira issue is authorized by this handoff. Work on a separate candidate branch; do not modify an open PR branch. Keep the candidate local until Nick reviews the implementation and decides where it belongs.
