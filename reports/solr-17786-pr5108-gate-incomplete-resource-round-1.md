# SOLR-17786 / PR #5108 gate: incomplete resource result

**Disposition: INCOMPLETE, not green.** The gate remains OPEN. Per `WORKFLOW.md`, no `receipts/SOLR-17786.md` was written because a receipt marks gate completion; required proof legs remain unfinished. The claim verdict is `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard-verdict-incomplete.md`.

## Verified input

The fork branch `solr-17786-reverse-cursor-submit` remained at the exact gate head `2c8af559c8456ded97811455622fb06786ae74ba`, with candidate parent `a3d97ca294f43adce99557fe011da90cd2528ce9`. The positive worktree was detached and clean at that head. The public PR #5108 remained open at head `32f80171030ca1108cd20292f50232d2729ed930`, base `f3a798e424bb933ba988c8d316d0e8f35c8b6e0f`; it was not changed.

The original gate pin `7ba3b78c45c1c2c59225426ec93e975f1c76f24e` required a five-file tidy repair. That formatting-only commit is `a3d97ca294f43adce99557fe011da90cd2528ce9`. The post-tidy harness still configured one shard, so the gate-required, three-line fixture correction `sliceCount = 2; fixShardCount(2);` was committed in `2c8af559c8456ded97811455622fb06786ae74ba`. It changes only `DistribCursorPagingTest`, with no product logic or new test. Earlier immutable records are `reports/solr-17786-pr5108-gate-preflight-blocked-round-1.md`, `reports/solr-17786-pr5108-gate-continuation-after-tidy-round-1.md`, and `reports/solr-17786-pr5108-gate-two-shard-fixture-round-1.md`.

The detached tidy preflight at the final pin returned `tidy clean`. Its logs are in `receipts/SOLR-17786-gate-round-1-two-shard/preflight/`. Java 21, Gradle 9.7.0, and Ubuntu 24.04.4 LTS were used.

## Checks that completed

The prescribed `./gradlew :solr:core:compileTestJava :solr:solrj:compileTestJava` passed with 20 actionable tasks, all executed. Log: `receipts/SOLR-17786-gate-round-1-two-shard/compile/compile-test-java.log`.

The seed `5108C0DE` core suite passed 16 tests, 0 failures, 0 errors, 0 skipped. Its command was `./gradlew :solr:core:test --tests org.apache.solr.search.SortSpecReverseTest --tests org.apache.solr.search.CursorMarkTest --tests org.apache.solr.CursorPagingTest --tests org.apache.solr.cloud.DistribCursorPagingTest -Ptests.seed=5108C0DE`. Log and fresh XML are in `receipts/SOLR-17786-gate-round-1-two-shard/positive/seed-5108/`.

The seed `5108C0DE` SolrJ suite passed 7 tests, 0 failures, 0 errors, 0 skipped. Its command was `./gradlew :solr:solrj:test --tests org.apache.solr.client.solrj.response.QueryResponseTest -Ptests.seed=5108C0DE`. Log and fresh XML are in `positive/seed-5108-solrj/`.

The seed `17786C0DE` core suite passed 9 tests, 0 failures, 0 errors, 0 skipped. Its command was `./gradlew :solr:core:test --tests org.apache.solr.search.SortSpecReverseTest --tests org.apache.solr.search.CursorMarkTest --tests org.apache.solr.CursorPagingTest.testReverseCursorPaging --tests org.apache.solr.cloud.DistribCursorPagingTest.testReverseCursorPaging -Ptests.seed=17786C0DE`. Log and fresh XML are in `positive/seed-17786/`.

The distributed XML records two named shards, `shard1` and `shard2`, with one core/node per shard. Runtime logs contain both values of `multiThreaded` and `distrib.singlePass`. The source conditions were checked through `QueryComponent`, `MultiThreadedSearcher.allowMT`, the four-thread test `solr.xml`, and `SolrIndexSearcher.searchCollectorManagers`. No TRACE-level collector-worker message was captured, so the evidence is the runtime layout/requests plus the checked source path, not a direct worker-thread trace. Nonfatal JVM security-policy and CLDR locale-provider messages appeared in logs; all positive JUnit totals were clean.

## Mutation witness and unfinished legs

Mutation 1 used a separate disposable checkout at the pinned head. The captured one-file patch restores the old one-sided zero-row marker behavior in `QueryResult.setCursorMarkNoResults`; it is `receipts/SOLR-17786-gate-round-1-two-shard/mutations/zero-row/mutation.patch`. The exact command was `./gradlew :solr:core:test --tests org.apache.solr.search.CursorMarkTest.testZeroRowCursorMarks -Ptests.seed=5108C0DE`. Its XML reports 1 test, 1 expected assertion failure, 0 errors, 0 skipped. The failure is `assertNotNull` at `CursorMarkTest.java:150`, for the required previous marker. This is the expected behavior witness, not a compile/setup failure. The runner log, headroom log, XML, test-output snapshot, run-info, and prelaunch check are all under `receipts/SOLR-17786-gate-round-1-two-shard/mutations/zero-row/`.

Mutation 2, removing only local result normalization and running `CursorPagingTest.testReverseCursorPaging`, was not run. Mutation 3, removing only response-position reversal and running `DistribCursorPagingTest.testReverseCursorPaging`, was not run. The final `./gradlew :solr:core:check :solr:solrj:check :solr:solr-ref-guide:check -x test` was not run. These remain unverified, so the gate is not green. No PR description was prepared for submission and no PR, Jira, comment, or ticket-state action was taken.

## Resource blocker

The hard stop is 15% free inodes. Before the first independent mutation checkout, `/` had 160,237 free inodes. Creating that checkout used 10,092; its fresh core test run used a further 10,606, for 20,699 total. The runner recorded 139,538 free after the test. At the stop check, `/` had 139,521 free of 837,408 (16.65%), only about 13,910 above the 15% boundary of 125,612. The remaining prescribed separate mutation checkout and fresh build would exceed the remaining margin based on this measured consumption. No files were deleted and no further Gradle task was started.

At the stop check no Gradle worker, test executor, or runner was active; only idle daemons remained and the host lock was available. The candidate branch stayed at the pinned head and the positive checkout stayed clean. Gate status remains OPEN; this report is not a completion receipt and does not mark the claim DONE.


## Multithreaded-path source references

The runtime request loop is in `solr/core/src/test/org/apache/solr/cloud/DistribCursorPagingTest.java:123-171`. `QueryComponent` propagates the request flag at `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:379-383`. `MultiThreadedSearcher.allowMT` checks the true flag, non-null executor, no post-filter, and no segment termination at `solr/core/src/java/org/apache/solr/search/MultiThreadedSearcher.java:120-128`. The test `solr.xml` sets four executor threads at `solr/core/src/test-files/solr/solr.xml:32`; `SolrIndexSearcher.initCollectorExecutor` creates that executor at `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:245-264` and calls the collector-manager branch at `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:1900-1924`. The fresh XML/log snapshots remain under `receipts/SOLR-17786-gate-round-1-two-shard/positive/`. No worker-thread trace was captured, so this report claims branch eligibility from the executed request/layout and source conditions, not an observed worker event.
