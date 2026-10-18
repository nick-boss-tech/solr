# Test sleep audit: Solr test sources on apache/solr main (2026-10-18T22:12:45ZZ, vm5)

Pinned head: apache/solr `main` `c8ab71430f`. Inventory: `reports/test-sleep-audit/sleeps-apache-main.csv` (one row per call, with file, line, heuristic category, duration where literal, and the code line).

Scope: `Thread.sleep` and `TimeUnit.*.sleep` in `solr/**/src/test` and `solr/**/src/test-framework`.

## Totals

- 374 sleep calls in 155 files.
- Heuristic categories: poll-loop 150, beside-wait-helper 25, other 199.
- Literal durations: 180 under 1 s, 109 from 1 to 10 s, 11 at 10 s or more, and 74 with a computed duration.

The heuristic looks at 8 lines before the call for a `while`/`for` loop, and at 8 before and 3 after for a wait helper. It is a sort key, not a verdict. A poll loop can still be a legitimate pacing loop, and an "other" call can be a poll loop with an unusual shape.

## Top files

| File | Sleep calls |
| --- | --- |
| `solr/core/src/test/org/apache/solr/cloud/LeaderElectionTest.java` | 12 |
| `solr/core/src/test/org/apache/solr/update/TestInPlaceUpdatesDistrib.java` | 12 |
| `solr/core/src/test/org/apache/solr/cloud/DistributedVersionInfoTest.java` | 11 |
| `solr/core/src/test/org/apache/solr/search/TestCoordinatorRole.java` | 10 |
| `solr/core/src/test/org/apache/solr/cloud/RestartWhileUpdatingTest.java` | 9 |
| `solr/core/src/test/org/apache/solr/cloud/ZkSolrClientTest.java` | 8 |
| `solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java` | 8 |
| `solr/core/src/test/org/apache/solr/cloud/ReplicationFactorTest.java` | 7 |
| `solr/core/src/test/org/apache/solr/cloud/BasicDistributedZk2Test.java` | 6 |
| `solr/cross-dc-manager/src/test/org/apache/solr/crossdc/manager/RetryQueueIntegrationTest.java` | 6 |
| `solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/stream/StreamDecoratorTest.java` | 6 |
| `solr/solrj/src/test/org/apache/solr/client/solrj/impl/StallDetectionTest.java` | 6 |
| `solr/core/src/test/org/apache/solr/cloud/ChaosMonkeyShardSplitTest.java` | 5 |
| `solr/core/src/test/org/apache/solr/cloud/DistributedQueueTest.java` | 5 |
| `solr/core/src/test/org/apache/solr/cloud/HttpPartitionOnCommitTest.java` | 5 |

## Reference: the change this audit follows

SOLR-16630 (#5106, `solr-16630-submit`, head `c9caf1e4b8`) converted every `Thread.sleep` in `TestCoordinatorRole.java` to latches or waits. The receipt is `receipts/SOLR-16630.md`: zero sleeps remain in the file, and the focused test passed on six seeds. The reviewer asked for the conversion twice. The coordinator handoff rule says the conversion design is attempted before any feasibility reply, and no request is narrowed on a lane's own judgment.

## Proposed phases (for the ticket, not yet started)

1. **Poll loops (130).** Replace each polling sleep with the existing wait helper for that condition, or a latch counted down by the code under test. Attempt the design file by file, and record each sleep that cannot be replaced with the reason.
2. **Beside a wait helper (25).** Check whether the sleep is needed once the wait is in place. Remove it if the test passes with the wait alone; keep it with a stated reason otherwise.
3. **Other (219).** Read each call. Classify as: replaceable by a latch, a deliberate delay that needs a design, or a sleep inside a helper used by many tests (change the helper once).

## What is not decided here

- The design per file, and whether any sleep stays. The attempt decides, not this inventory.
- Whether to convert the cross-DC and SolrJ modules in the same ticket or in follow-ups.
