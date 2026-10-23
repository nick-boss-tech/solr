# Teardown leaks: three different failure paths

The evidence does not show that the six reviewed PRs are collectively making teardown worse. It shows two production cleanup weaknesses and one test-cleanup weakness. A failure appearing on a PR's CI run is not proof that its diff caused it.

**PR [5206](https://github.com/apache/solr/pull/5206) is separate.** It makes `BasicDistributedZk2Test` wait for ZooKeeper session expiry and reconnection. It does not change any of the three failing classes, teardown code, or leak assertions. It should not be presented as a teardown fix.

**PR [5192](https://github.com/apache/solr/pull/5192) is directly relevant** to the Cloud client failure: a close-pool timeout exits `CoreContainer.shutdown()` before its final handler, plugin and metrics closes. It is not a demonstrated fix for every object in the other two failures.

All three requested classes passed once, unmodified, at the exact assigned SHA. Those passes do not disprove intermittent CI failures or establish that current upstream or any PR merge revision is clean.

## What the CI failures actually show

### Custom-sort test: an interrupted client close, plus an unresolved factory-context release

[CI run 39903819776, job 120373474924](https://github.com/apache/solr/actions/runs/39903819776/job/120373474924) ran merge `c601b1ea825`, PR head `7e0251ee029125152c4d1791660189ef1e9dea06`, on base `79b23df58daac14b92cc35f23b8b81f72098b4ba`.

The first tracker report contains exactly two objects. Later repeated reports are duplicates, not additional leaks. Line references below refer to the downloaded original `ci/pr5202-job.log` in the evidence archive.

| Tracked object / owner | Creation stack in CI | Release that should have run |
|---|---|---|
| `SolrMetricsContext`, owner `HttpShardHandlerFactory@1c0ddc54@470670420` | Lines 5994-6066: tracker -> context constructor/getChildContext -> `HttpShardHandlerFactory.initializeMetrics:461` -> `CoreContainer.loadInternal:792` -> provider/Jetty startup | Factory `close()` -> `SolrMetricProducer.super.close()` -> its context `close()`; baseline `HttpShardHandlerFactory.java:359-364` |
| `HttpJettySolrClient@915843527` | Lines 6068-6141: tracker -> client constructor `:179` -> builder `:1155` -> `HttpShardHandlerFactory.init:316` -> `CoreContainer.loadInternal:790` | The factory's `defaultClient.close()`, through `IOUtils.closeQuietly`, should reach client tracker release; baseline client `close():340-364` |

The captured teardown error is `RuntimeException: Exception on closing client`, caused by `InterruptedException` in Jetty `ManagedSelector.doStop` / `CountDownLatch.await` (lines 6752-6779). The call is through `HttpShardHandlerFactory.close:353`. The client throws before its final tracker-release statement. This explains the client's retained tracker entry; moving only that release would not prove successful client shutdown.

**The factory-context mechanism is not fully established.** `IOUtils.closeQuietly` catches the logged client exception (`IOUtils.java:33-40`), so that exception alone does not explain bypassing the factory's later metrics close. A load-balancer exception, executor cleanup failure, or still-in-progress cleanup could prevent that release; the supplied log does not identify the precise escape/completion point. There is no logged `Timeout waiting for pool to shutdown` in this job. Do not state that the quiet client-close exception automatically skipped the factory's metrics close.

The same output reports one suite-scope leaked thread, `httpShardExecutor-311-thread-2`, in Jetty selector code (lines 6797-6816). This is stronger evidence of unfinished activity than an object-tracker entry alone, but does not identify the exact factory-context escape path.

### Cloud client test: close-pool timeout skips the container's cleanup tail

[CI run 39699700526, job 119565807999](https://github.com/apache/solr/actions/runs/39699700526/job/119565807999) ran merge `0f930fd6b9b`, PR head `777b9838baad89e99f2863572ef01be1126ee2fb`, on base `78923482727b8530d26ad08db734712ab2dacb12`.

`CloudHttp2SolrClientTest.tearDownAfterClass:180` fails while shutting down its cluster. `closeThreadPool` still has one active worker and throws `Timeout waiting for pool to shutdown` (original `ci/pr5030-job.log`, lines 11557-11625). The first tracker report then names **16 metrics contexts**, not 16 client objects.

The creation stacks are container-startup stacks. Each row below is one distinct tracked context. Class names abbreviate `org.apache.solr.handler.admin` unless otherwise shown; owner strings and numeric identities come directly from the first report.

| Context owner identity | Original log stack | Key creation frame | Missed release owner |
|---|---|---|---|
| `ConfigSetsHandler@29307e96@691043990` | 11791-11864 | `RequestHandlerBase.initializeMetrics:161` -> `CoreContainer.createHandler:2279` | Container handlers |
| `SolrMetricsContext@690513923` | 11866-11936 | `CoreContainer.loadInternal:762` | Container root context |
| `LoggingHandler@4612ab08@1175628552` | 11938-12012 | `RequestHandlerBase.initializeMetrics:161` -> `InfoHandler.initializeMetrics:71` | InfoHandler and its registered handlers |
| `PropertiesRequestHandler@25cdf294@634253972` | 12014-12088 | Same InfoHandler initialization chain | InfoHandler and its registered handlers |
| `SystemInfoHandler@747cbb1e@1954331422` | 12090-12164 | Same InfoHandler initialization chain | InfoHandler and its registered handlers |
| `InfoHandler@9a00662@161482338` | 12166-12240 | `RequestHandlerBase.initializeMetrics:161` -> `InfoHandler.initializeMetrics:68` | Container handlers |
| `CoreAdminHandler@176e3470@393098352` | 12242-12316 | `RequestHandlerBase.initializeMetrics:161` -> `CoreAdminHandler.initializeMetrics:131` | Container handlers; its earlier `shutdown()` only shuts down async trackers |
| `CollectionsHandler@7bd89bad@2077793197` | 12318-12391 | `RequestHandlerBase.initializeMetrics:161` -> `CoreContainer.createHandler:2279` | Container handlers |
| `security.PublicKeyHandler@67bf34e8@1740584168` | 12393-12465 | `RequestHandlerBase.initializeMetrics:161` -> `CoreContainer.loadInternal:785` | Container handlers |
| `MetricsHandler@45918262@1167164002` | 12467-12539 | `RequestHandlerBase.initializeMetrics:161` -> `CoreContainer.loadInternal:902` | Container handlers |
| `ZookeeperInfoHandler@abfd428@180343848` | 12541-12614 | `RequestHandlerBase.initializeMetrics:161` -> `CoreContainer.createHandler:2279` | Container handlers |
| `ThreadDumpHandler@1caa01a2@480903586` | 12616-12690 | `RequestHandlerBase.initializeMetrics:161` -> `InfoHandler.initializeMetrics:71` | InfoHandler and its registered handlers |
| `HealthCheckHandler@1b6b0aff@459999999` | 12692-12766 | `RequestHandlerBase.initializeMetrics:161` -> `InfoHandler.initializeMetrics:71` | InfoHandler and its registered handlers |
| `security.PKIAuthenticationPlugin@3004e866@805628006` | 12768-12840 | `AuthenticationPlugin.initializeMetrics:148` -> `CoreContainer.loadInternal:836` | PKI authentication plugin |
| `SecurityConfHandlerZk@6d5ea455@1834918997` | 12842-12914 | `RequestHandlerBase.initializeMetrics:161` -> `CoreContainer.loadInternal:906` | Container handlers |
| `ZookeeperStatusHandler@7f952ad2@2140482258` | 12916-12990 | `RequestHandlerBase.initializeMetrics:161` -> `CoreContainer.createHandler:2279` | Container handlers |

In the assigned source, the failing await is at [`CoreContainer.java:1326`](https://github.com/nick-boss-tech/solr/blob/498a09aea97aac86c2cc7ac114113b946d249942/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L1308-L1380). It occurs before PKI/plugin closing (`:1343-1367`), `containerHandlers.close()` (`:1374`), and root context closing (`:1378`). A timeout therefore bypasses those releases. Handler closes reach `SolrMetricProducer.close()` and their own context; InfoHandler registers its subhandlers as closeables (`InfoHandler.java:67-74`). PKI close delegates to the inherited metrics close.

This is the strongest incident-to-source match for SOLR-18564 / PR 5192. Its proposed catch-and-continue addresses that skipped tail. This audit did not run the PR's regression test or establish why the original worker became stuck. A root-context close alone is not recursive cleanup of every child: `getChildContext()` does not register the child for closing (`SolrMetricsContext.java:80-94`).

### ThinCache: an assertion bypasses test-owned cleanup

[CI run 39596535427, job 119215684665](https://github.com/apache/solr/actions/runs/39596535427/job/119215684665) ran merge `fccb649d48c`, PR head `91500069e48cbdaec20d868cca04c1d042e2fda3`, on base `1afeee27c48416615cf26b13488a0aee976424dd`.

`testSimple` first fails `expected:<0> but was:<1>` at line 170 (original `ci/thincache-job.log:6248-6255`). Source identifies that assertion as the eviction counter. The three tracker entries are the exact parent-and-two-cache contexts created inside this test, not container-startup contexts:

| Tracked context / owner | Creation stack in original log | Release that was bypassed |
|---|---|---|
| `ThinCache@83155272` | 6304-6354: `ThinCache.initializeMetrics:302` -> `TestThinCache.testSimple:123` | Parent closes registered `newLFUCache`, then its child context |
| `ThinCache@252279758` | 6356-6406: `ThinCache.initializeMetrics:302` -> `TestThinCache.testSimple:112` | Parent closes registered `lfuCache`, then its child context |
| `SolrMetricsContext@617222935` | 6408-6456: `TestThinCache.testSimple:110` | Explicit `solrMetricsContext.close()` at test line 172 |

The source puts `solrMetricsContext.close()` **after the failing assertion**, without `finally` or try-with-resources ([`TestThinCache.java:104-172`](https://github.com/nick-boss-tech/solr/blob/498a09aea97aac86c2cc7ac114113b946d249942/solr/core/src/test/org/apache/solr/search/TestThinCache.java#L104-L172)). The test registers both caches with that parent (`:111`, `:120`), and cache close reaches its own metrics-context close. Combining the observed assertion and exact creation stacks with this control flow explains the three retained entries as secondary to assertion failure. There is no need to invoke `CoreContainer.shutdown()` to explain this case.

PR 5029 addresses the warming/eviction flake, but leaves this assertion-sensitive cleanup structure unchanged. It is not a general cleanup-hardening fix. No code change was made here.

## One run per class at the assigned baseline

| Class | Module | Seed | JUnit result | Leak reproduced? |
|---|---|---|---|---|
| `DistributedQueryComponentCustomSortTest` | core | `FFF5A3C3AAC69084` | 1 test, 0 failures/errors/skips | No |
| `CloudHttp2SolrClientTest` | solrj | `A35B8669BA78E261` | 20 tests, 0 failures/errors/skips | No |
| `TestThinCache` | core | `1668E8BD438E6830` | 2 tests, 0 failures/errors/skips | No |

All runs used the exact assignment flags below, plus `--no-daemon --max-workers=2 -Ptests.jvms=1`, a module-specific `cleanTest`, and one fully qualified class filter. Both host lock conventions were held; disk and inode guards passed. No full core/SolrJ suite, PR-head run, or retry was executed. All three Gradle exits were zero. Fresh matching JUnit XML was preserved before another run could overwrite it.

```text
-Ptests.jvmargs="-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m"
-Ptests.timeoutSuite=600000!
-Ptests.useSecurityManager=true
-Ptests.file.encoding=ISO-8859-1
-Ptests.seed=the seed in the table
```

**Baseline limitation:** `498a09aea97aac86c2cc7ac114113b946d249942` is the fork's `origin/main`, not current Apache main. Its three fork-only commits change only `.github/workflows/fork-test-runner.yml`; product source matches Apache ancestor `e044bf20b405b8ca985730be3553d77d1fc8ff31`. Current upstream main was `79b23df58daac14b92cc35f23b8b81f72098b4ba`. The requested SHA was followed, not silently replaced. A narrow comparison found the relevant cleanup ordering unchanged upstream, but that is not a current-main execution result.

**CI comparability limitation:** these CI jobs tested PR merge refs in a full-suite run, not this older isolated baseline. All three randomized-runner reproduction NOTE lines say UTF-8. Gradle's later failure summaries say ISO-8859-1 for CustomSort and CloudHttp2, but UTF-8 for ThinCache. The assignment requested ISO-8859-1 for all three. These recorded flag discrepancies and scheduler/environment differences limit what a single passing seeded class can establish.

## Read-only audit of the six open PRs

“Yes” below means a diff changes at least one named teardown surface, not that it caused a leak. The named surfaces are `CoreContainer.shutdown`, `HttpShardHandlerFactory`, `HttpJettySolrClient`, `CoreContainerProvider`, `JettySolrRunner`, and `BaseDistributedSearchTestCase`.

| PR | Changes a named teardown surface? | File and diff location | What that means |
|---|---|---|---|
| [5192 / SOLR-18564](https://github.com/apache/solr/pull/5192) | **Yes** | `solr/core/src/java/org/apache/solr/core/CoreContainer.java`, hunk `@@ -1312,7 +1312,17 @@` | Catch close-pool `RuntimeException`, continue remaining closes. Adds `CoreContainerShutdownTest` with a blocking shard-handler subclass; no production factory/client change. Direct match for Cloud test's skipped tail, not proof of all-object coverage. |
| [5029 / SOLR-18506](https://github.com/apache/solr/pull/5029) | **No** | `solr/core/src/test/org/apache/solr/search/TestThinCache.java`, hunks `@@ -150,6 +150,13 @@`, `@@ -167,6 +174,8 @@` | Changes backing-cache size for warming/eviction test stability. Does not make metrics cleanup unconditional. |
| [5002 / SOLR-11431](https://github.com/apache/solr/pull/5002) | **No** | `solr/core/src/java/org/apache/solr/core/CoreContainer.java`, hunk `@@ -2215,7 +2215,7 @@`; related exception/test changes | Failed-core HTTP status changes, not `shutdown()`. Same file as 5192, distinct method/hunk. |
| [5101 / SOLR-18531](https://github.com/apache/solr/pull/5101) | **Yes** | `solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java`, start `@@ -497,6 +512,10 @@`, stop `@@ -660,6 +679,10 @@`, close `@@ -887,5 +971,10 @@` | Port-reservation lifecycle, with related cluster shutdown changes. No change to client close or metrics release. Lifecycle-adjacent, not demonstrated leak cause/fix. |
| [5202 / SOLR-18446](https://github.com/apache/solr/pull/5202) | **No** | `V2HttpCall.java`, hunk `@@ -132,9 +132,8 @@`; `HttpSolrCall.java`, hunk `@@ -277,11 +278,23 @@` | Path-scoped collection routing. No named teardown surface or failing CustomSort class changed. The failing CI association is not causal proof. |
| [5206 / SOLR-18576](https://github.com/apache/solr/pull/5206) | **No** | Only `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java`, hunk `@@ -151,15 +154,26 @@` | Waits for ZooKeeper session expiry/reconnection, removes listener. Does not suppress leak checks or change cleanup. |

All six were open, unmerged and non-draft. Audited head/base SHAs, respectively:

| PR | Head | API base |
|---|---|---|
| 5192 | `dd65b21b41dc1d008c51031b71f61964ee2ca169` | `78923482727b8530d26ad08db734712ab2dacb12` |
| 5029 | `77c019e1ff0e922e3c2350379dece841651b1d44` | `22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a` |
| 5002 | `968fad873c4b35fff0605ae88dbecca16d1dfa2f` | `cabedd1d968059215188f4e7563fb303241899ed` |
| 5101 | `c8a67c7267d00e33ad8323c683c040b539989b0d` | `3f5d4c5bf8ac96fec3a3219398b0c52c5418d48c` |
| 5202 | `7e0251ee029125152c4d1791660189ef1e9dea06` | `79b23df58daac14b92cc35f23b8b81f72098b4ba` |
| 5206 | `4c8f12f526f54e4e90a8ea49d46c1d28d899cb65` | `79b23df58daac14b92cc35f23b8b81f72098b4ba` |

No hunk-level dependency among these six diffs was identified. That is not combined-branch integration proof. In particular, PR 5192's metrics-tail fix and a possible client tracker-release change solve different statements on different owners. The assignment's vm4 local client-close commit was not obtained for this audit, validated, or changed.

## What remains for the owner

Keep the ZooKeeper synchronization change separate from teardown diagnosis. The strongest shared-code issue here is exception-sensitive cleanup, but the ThinCache failure has a direct test-local explanation and the CustomSort factory-context release still has an unresolved completion/escape path. These findings do not justify a broad revert of unrelated PRs.

Whether to repair unconditional cleanup, change a PR, obtain a current-main/CI-revision reproduction, rerun CI, or post any PR discussion remains with Nick and the main agent. None was done under this assignment.

## Evidence and completion

Original logs, first tracker blocks with all creation stacks, source trace, six-PR audit, commands/state, and preserved JUnit results are in [`material/teardown-objecttracker-evidence.tar.gz`](../material/teardown-objecttracker-evidence.tar.gz). The local originals remain at `/workspace/gates/logs/phone-agent-2/teardown-objecttracker/`; `provenance.json` records original-log hashes. Archive `reproduction-state.json` records exact commands and times; `custom-sort.xml/`, `cloud-http2.xml/`, and `thin-cache.xml/` hold the matching JUnit XML.

Diagnostic assignment completed by `phone-agent-2`. No production/test source edits, PR-branch pushes, PR comments, or full-module test suites. The detached source worktree remained clean. This closes the bounded investigation, not the unresolved defects or any owner decision.
