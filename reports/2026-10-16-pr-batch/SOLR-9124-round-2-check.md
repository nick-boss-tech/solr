# SOLR-9124 (PR 5147) round 2 check, item 2

- Status: checks run; gaps closed; no defect found at the head. The within-group clause is exercised by the grouped stats tests.
- Head: `3dd985b93ac0e26a3850c338765ba1235bd811d4`, fork branch `solr-9124-submit` (confirmed by `git ls-remote` before the runs).
- Host: vm5. Claim: `claims/pool-vm5-review-2026-10-16-code-round-2-item-2.md`.
- Seed for every Gradle run: `9124C0FFEE9124`. Module `:solr:core`.

## (a) Grouped distributed classes at the head

Command: `./gradlew :solr:core:test --tests <class> -Ptests.seed=9124C0FFEE9124`, one class per run. Counts from fresh JUnit XML.

| Class | Tests | Failures | Errors | Skipped | Result |
| --- | --- | --- | --- | --- | --- |
| `org.apache.solr.TestDistributedGrouping` | 2 | 0 | 0 | 0 | passed (log `9124r2-a2-distributed-grouping.log`, rc 0) |
| `org.apache.solr.cloud.BasicDistributedZkTest` | 1 | 0 | 0 | 0 | passed (log `9124r2-a-distributed-head.log`, rc 0) |
| `org.apache.solr.cloud.DistribCursorPagingTest` | 1 | 0 | 0 | 0 | passed (log `9124r2-a-distributed-head.log`, rc 0) |

The first run used the filter `org.apache.solr.handler.component.TestDistributedGrouping`. That is the wrong package, so the class did not run. `TestDistributedGrouping` lives at `org.apache.solr.TestDistributedGrouping` and was rerun alone at the same head and seed. The result above is from the rerun.

## (b) Within-group clause removed in a scratch copy

- The clause, found by diffing `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java` against `upstream/main` merge-base `cabedd1d9680`: the term `rb.grouping() && rb.getGroupingSpec().getWithinGroupSortSpec().includesScore()` in `needsDistributedStats`. It is the only clause in the diff that reads the within-group sort spec, so there was one match and no escalation.
- Scratch copy: detached worktree at `3dd985b93a`, no commit. The only change is removing that term. `git diff --stat` shows one line changed.
- Command: `./gradlew :solr:core:test` on `TestDefaultStatsCache`, `TestExactStatsCache`, `TestLRUStatsCache` and `TestExactSharedStatsCache`, seed `9124C0FFEE9124` (log `9124r2-b-mutant-stats.log`, rc 1: `5 tests completed, 1 failed`).

| Class | Tests | Failures | Result |
| --- | --- | --- | --- |
| `TestDefaultStatsCache` | 1 | 0 | passed |
| `TestExactSharedStatsCache` | 1 | 0 | passed |
| `TestLRUStatsCache` | 1 | 0 | passed |
| `TestExactStatsCache` | 2 | 1 | `test` failed |

Failure by content, from `TEST-org.apache.solr.search.stats.TestExactStatsCache.xml`:

    java.lang.AssertionError: expected:<[g0=4, g1=3]> but was:<[g0=4, g1=5]>
        at org.apache.solr.search.stats.TestBaseStatsCache.checkWithinGroupOrder(TestBaseStatsCache.java:117)
        at org.apache.solr.search.stats.TestBaseStatsCache.checkGroupedScores(TestBaseStatsCache.java:97)

Line 117 is `assertEquals(controlDocs, shardDocs)`. The top document per group on the shards differs from the control node when the within-group clause is removed. So the grouped stats test detects the clause.

## Notes

- The head runs used the fork commit as is. No production or test code was changed for this check.
- No base-code proof leg was run for the three distributed classes. The assignment does not require one.
- The index `reports/2026-10-16-pr-batch/code-round-2-index.md` covers all four items and is not written here.
