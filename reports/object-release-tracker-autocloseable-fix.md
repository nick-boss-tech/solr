# ObjectReleaseTracker AutoCloseable failure cleanup

Status: DONE

## Finding and change

`ObjectReleaseTracker.tryClose()` was the best-effort cleanup path used after test failures, but it skipped tracked objects that implemented `AutoCloseable` without `Closeable`. The candidate adds a final `AutoCloseable` branch after the existing `Closeable` and `ExecutorService` branches. It preserves executor shutdown through `ExecutorUtil.shutdownAndAwaitTermination` and logs close failures while continuing cleanup.

The candidate is based on Apache Solr main at `79b23df58daac14b92cc35f23b8b81f72098b4ba`; `upstream/main` still matched that SHA when checked. Local candidate branch: `phone-agent-2-objectrelease-autocloseable-candidate`. Candidate commit: `35faf539e94e613d5f5528c71962e1970786bc93`.

Changed files only:

- `solr/solrj/src/java/org/apache/solr/common/util/ObjectReleaseTracker.java`
- `solr/core/src/test/org/apache/solr/common/util/TestObjectReleaseTracker.java`

No product branch was pushed. No PR or Jira record was changed.

## Tracked-type safety audit

The tracker-registration and type-inheritance audit found three tracked types that implement only `AutoCloseable`:

- [`SolrMetricsContext`](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/metrics/SolrMetricsContext.java#L53) releases its own tracker entry, closes registered resources through `IOUtils.closeQuietly`, and clears its resource list. Its child contexts are separately tracked and have independent lifecycles.
- [`ZkCollectionTerms`](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/cloud/ZkCollectionTerms.java#L27) closes the contained shard-term objects under its terms lock, clears the map, and releases itself.
- [`ZkShardTerms`](https://github.com/apache/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/cloud/ZkShardTerms.java#L74) marks itself closed, clears listeners, resets its term state, and releases its tracker entry. Repeating those state updates is safe. A collection close may close a tracked shard before the tracker iteration reaches it; the regression test pins the corresponding nested, idempotent cleanup shape.

The indirect-interface check also found tracked `SolrQueryRequestBase`: it implements `SolrQueryRequest`, which extends `AutoCloseable`, but it also implements `Closeable`, so the pre-existing `Closeable` branch handles it. The registration audit found no other tracked AutoCloseable-only types. The fallback remains limited to test-failure cleanup; it does not change normal production shutdown.

## Regression coverage and results

Five tests were added, bringing `TestObjectReleaseTracker` to 10 tests. The new cases cover a minimal AutoCloseable-only tracked object, continuing after one close throws, a real `SolrMetricsContext` with a registered resource, an idempotent tracked parent/child pair, and preservation of the executor-specific shutdown branch. Each new test clears global tracker state in `finally`.

The focused class passed **10 tests, 0 failures, 0 errors, 0 skipped**, with seed `3B1F3E98A7C2D144`.

Tidy passed. The first Error Prone-enabled compile stopped on a javac `-Xlint:try` warning because the failing test fixture declared `close() throws Exception`, which could include `InterruptedException`. The fixture was changed to throw `IllegalStateException`. The focused test compilation and the later scoped checks both ran with `-Pvalidation.errorprone=true` and passed after that correction.

Both scoped checks passed with tests excluded: `:solr:core:check -x test` and `:solr:solrj:check -x test` (78 tasks, no full module test suite). The checks included source validation, ECJ lint, forbidden APIs, licenses, jar checks, and Spotless checks.

## Validation limits

All Gradle invocations used the host sweep runner, which serialized work under `/workspace/tools/.gradle-phone-agent-2.lock` and passed the 15% inode guard before each build. The focused output also contained Security Manager and locale-provider warnings; they did not affect the successful test result.

No full `:solr:core:test` or `:solr:solrj:test` suite was run. This fallback addresses secondary cleanup after test failures; the diagnosis does not establish that it caused every previously reported CI leak.