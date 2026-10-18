# B2 removed-feature tickets, round 2

Assignment: `assignments/pool-skip-review-removed-round-2.md`
Claim: `claims/pool-skip-review-removed-round-2.md` (vm3)
Pinned head: fork `origin/main` at `498a09aea97aac86c2cc7ac114113b946d249942`. Not moved.

Method: JIRA description and the first comments read through the anonymous REST endpoint (read only). Named code paths checked on the pinned head with `git grep`, `git show`, `git log -S` and `git merge-base --is-ancestor`. Paths are relative to the repo root. No builds or test runs. No JIRA, GitHub or email posts.

Scope: all 40 rows of the assignment table were checked. No ticket was left unreached.

## Corrections to the queue notes

- Admin UI rows (SOLR-10201, SOLR-10497, SOLR-10511): the queue notes call the UI obsolete. The Angular admin UI is still on main (`solr/webapp/web/js/angular/app.js`, `solr/webapp/web/partials/`). The verdicts come from the code paths: 10497 and 10511 are fixed, 10201 is present.
- SOLR-12446: the queue note says "partly obsolete". `LBSolrClient.java` still raises "No live SolrServers available to handle this request" when no endpoint is left, so the verdict is present.
- SOLR-14023: the `${user.dir}` setting the ticket names is gone (`gradle/testing/randomization.gradle:132` sets `tests.src.home` to null). The upward search the ticket describes is still in `ExternalPaths.java`, so the verdict is present.
- SOLR-13481: `LBHttpSolrClient` has no class on main. The retry loop is now in `LBSolrClient.java`; that code was not compared against the ticket's scenario.

## Verdicts

| Ticket | Verdict | Evidence |
| --- | --- | --- |
| SOLR-9826 | fixed | Commit `9d8a2faf18` (SOLR-9903, 2017-02-10) stops interrupting the update executor on shutdown. `solr/core/src/java/org/apache/solr/update/UpdateShardHandler.java:226` reads "do not interrupt". LIR is also gone: zero `leaderinitiatedrecovery` hits in `*.java`, removed by `a37a213975` (SOLR-11812). |
| SOLR-9872 | gone | `solr/core/src/java/org/apache/solr/update/TransactionLog.java` has no `setLength` call on main (`git grep` zero hits). The constructor now opens the file with `FileChannel.open(... CREATE_NEW)`. |
| SOLR-9913 | gone | LIR removed: zero `leaderinitiatedrecovery` hits in `*.java`. Removal in `a37a213975` (SOLR-11812, 8.0). |
| SOLR-10041 | gone | Same LIR removal. The leader's old LIR call in `SolrCmdDistributor` was removed by `f1ce5419ee` (SOLR-11702). |
| SOLR-10201 | present | `solr/webapp/web/js/angular/app.js:392-393` sets a 10000 ms timeout on every request that has none set. `grep doNotTimeout` finds nothing in `solr/webapp`. Add Collection uses that default path (`solr/webapp/web/js/angular/controllers/collections.js:164`). |
| SOLR-10326 | gone | No `autoAddReplicas` code in `ZkController.java` or `CoreContainer.java`. The only hits in `*.java` outside tests are a comment at `solr/core/src/java/org/apache/solr/cluster/events/impl/CollectionsRepairEventListener.java:51`. The old implementation was removed by `0f7e3be589` (SOLR-10397). `ZkController.preRegister` (`ZkController.java:2126`) publishes no shared data dirs. |
| SOLR-10497 | fixed | `solr/webapp/web/partials/index.html:215` now reads `ng-repeat="arg in commandLineArgs track by $index"`. The diff that adds `track by $index` is `0dadf2c2a8`. |
| SOLR-10511 | fixed | `solr/webapp/web/js/angular/controllers/replication.js` `getIterations` now tests `matchingIterations[0]` and sets status on a new object when nothing matches. The old `if (matchingIterations)` was always true for an array, so `iteration` was undefined and `iteration.status` threw the reported TypeError. Fix: `69d0c70651` (SOLR-10763, 2017-06-23). |
| SOLR-10709 | fixed | `solr/core/src/java/org/apache/solr/schema/FieldType.java:1071-1073` builds a `FieldExistsQuery` for docValues fields, so `field:*` no longer goes through the DocValues rewrite that threw the NUMERIC error. The query type was changed in `c64a35adbc` (SOLR-17631). Not run; the 2017 trace path was not re-run. |
| SOLR-10889 | gone | `OverseerAutoReplicaFailoverThread` has no hits on main. Removed by `0f7e3be589` (SOLR-10397), which is an ancestor of main. |
| SOLR-10904 | gone | Same class and removal as SOLR-10889. |
| SOLR-10959 | gone | `MapSerializable` has no hits on main. Deleted by `f9f5abf21e` (SOLR-17600 p4). |
| SOLR-11009 | fixed | `solr/core/src/java/org/apache/solr/search/facet/FacetModule.java:181-182` checks `facetState.mcontext == null` before refinement, which covers the all-shards-failed case. That check was added by `9c066f60f1`. The merger is null-checked at lines 308-309. |
| SOLR-11246 | fixed | `solr/core/src/java/org/apache/solr/cloud/api/collections/MoveReplicaCmd.java:247` and `:370` pass `ZkStateReader.REPLICA_TYPE`. Added by `86a40c1cd5` (SOLR-13843). |
| SOLR-11378 | unclear | Missing: an isolated reproduction of a commit racing a core unload. The test the ticket came from (`AutoscalingHistoryHandlerTest`) is gone, and the queue note's SOLR-15695 link has no commit on main. No build allowed in this item. |
| SOLR-11435 | gone | `legacyCloud` has no hits in `*.java` on main, so the legacy-mode path the ticket depends on cannot be reached. Last removal in `1ab9b811c6` (SOLR-12823). |
| SOLR-11537 | fixed | `22e2953d2f` (SOLR-15718) removed the `solr.useUnsafeOverseerResponse` path and the Java-serialization branch in `OverseerSolrResponseSerializer.java`, which caused the InvalidClassException. `solr/solrj/src/java/org/apache/solr/client/solrj/SolrResponse.java:29` still declares `Serializable`, but the serialize helper is gone. |
| SOLR-11659 | gone | `JmxObjectNameFactory` has no hits in `*.java` on main. |
| SOLR-11776 | gone | LIR is gone (see SOLR-9913), and the old LIR call in `SolrCmdDistributor` was removed by `f1ce5419ee` (SOLR-11702). |
| SOLR-11787 | unclear | Missing: a reproduction, or a check whether SOLR-6213 is the same root cause. The trace is a log4j date-format frame in leader election. Not reproduced. |
| SOLR-11955 | gone | `git grep` for `SynonymGraphFilter` and `WordDelimiterGraphFilter` finds hits only under `solr/core/src/test-files` and one services file. None under `solr/server`, `solr/example` or the shipped configsets. |
| SOLR-12166 | unclear | Missing: whether the publish-DOWN-after-ACTIVE race can happen without the LIR flag, which the ticket's trigger depends on and which is gone (see SOLR-9913). Paths to read or test: `solr/core/src/java/org/apache/solr/cloud/Overseer.java:492` (rejoinElection) and `ZkController.preRegister` (`ZkController.java:2126`). |
| SOLR-12446 | present | `solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java:428-433`: when no endpoint is left, the message still starts "No live SolrServers available to handle this request", with the last exception attached as the cause. The misleading top-level text remains. |
| SOLR-12585 | unclear | Missing: the pinned ZooKeeper client version and its host-resolution behavior. That behavior is in the ZooKeeper dependency, not this repo. Not checked. |
| SOLR-12645 | gone | `MemClassLoader` has no hits in `*.java` on main. |
| SOLR-12646 | gone | No `autoscaling` `Variable.Type` in `*.java` on main (zero hits), so the NoClassDefFound path from the ticket is gone with autoscaling. |
| SOLR-13304 | gone | `PreAnalyzedField` has no hits in `*.java` on main. |
| SOLR-13481 | gone | `LBHttpSolrClient` has no class on main. Only `solr/core/src/test/org/apache/solr/handler/component/TestShardHandlerFactory.java:105-107` mentions it, in a comment and a method name. |
| SOLR-13501 | gone | `ConcurrentUpdateHttp2SolrClient` has no hits in `*.java` on main. |
| SOLR-13846 | gone | `PreemptiveBasicAuthClientBuilderFactory` has no hits in `*.java` on main. |
| SOLR-14023 | present | `solr/test-framework/src/java/org/apache/solr/util/ExternalPaths.java:71-95` (`determineSourceHome`) still searches upward from the working directory for `test-framework/build.gradle`, with `tests.src.home` as the fallback at line 81. `gradle/testing/randomization.gradle:132` now sets `tests.src.home` to null, so the `${user.dir}` setting is gone. |
| SOLR-14117 | gone | `AnalyticsShardResponseParser` has no hits on main. |
| SOLR-15297 | gone | The error text "TLOG or PULL replica types not supported with placement rules" has no hits in `solr/core/src/java`. |
| SOLR-15310 | gone | `HttpClientUtil` has no class on main. The only `*.java` mentions are two comments in `solr/core/src/test/org/apache/solr/cloud/TestMiniSolrCloudClusterSSL.java:157` and `:376`. |
| SOLR-15350 | gone | No `admin/mbeans` handler in `*.java` on main. The only hits are historical CHANGELOG entries. |
| SOLR-15690 | gone | `DataImportHandler` has no hits in `*.java` on main. Only CHANGELOG entries. |
| SOLR-15704 | gone | Same as SOLR-15690. |
| SOLR-15879 | gone | `FastLRUCache` has no hits in `*.java` on main. Only CHANGELOG entries. |
| SOLR-17763 | fixed | `1177796e32` (SOLR-17118, 2024-05-31) removed the CountDownLatch wait in `CoreContainerProvider` that could return an uninitialized container. `solr/core/src/java/org/apache/solr/handler/admin/api/NodeHealth.java:77-79` keeps the message for the null and shutting-down cases only. |
| SOLR-17993 | gone | `CLOUD_CORE_PATTERN` and `SolrCoreMetric` have no hits in `*.java` on main. |

## Tally

- fixed: 8 (SOLR-9826, 10497, 10511, 10709, 11009, 11246, 11537, 17763)
- present: 3 (SOLR-10201, 12446, 14023)
- gone: 25
- unclear: 4 (SOLR-11378, 11787, 12166, 12585)

Total: 40 rows, each with one verdict.

## What is missing for the unclear rows

- SOLR-11378: a targeted reproduction (a commit racing a core unload, with the tlog reference tracked). Needs a build, which this item does not allow.
- SOLR-11787: a reproduction of the StackOverflowError, or the SOLR-6213 status compared with this trace.
- SOLR-12166: a test of the election-win and recovery-publish sequence without the LIR flag.
- SOLR-12585: the pinned ZooKeeper version from `versions.props` and its client source for host resolution.
