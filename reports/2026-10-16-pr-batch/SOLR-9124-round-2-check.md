# SOLR-9124 (PR 5147) round 2 check: grouped distributed tests and the within-group clause

Host: vm2. Fork branch `solr-9124-submit`, head `3dd985b93ac0e26a3850c338765ba1235bd811d4` (checked by ls-remote at the start of the 05:2x tick, unchanged). Merge-base with upstream/main `cabedd1d9680`. JDK 21.0.12.1 (`/workspace/jdk/jdk-21.0.12.1+1`), Gradle 9.7.0 from the branch wrapper. Seed for every Gradle run: `9124C0FFEE9124`.

## Status

- Gap (a), the grouped distributed classes at this head: confirmed green.
- Gap (b), no run shows the within-group clause matters: closed. Removing the clause makes `TestExactStatsCache.test` fail in `checkWithinGroupOrder`. The clause is required, and the PR's new check catches its removal.
- Clause count: one match. In `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`, `needsDistributedStats` (lines 641 to 646 at head, clause at line 645) has the only `rb.grouping() && rb.getGroupingSpec().getWithinGroupSortSpec().includesScore()` term. No escalation needed.

## Run 1: distributed classes at head

Command (through `pool/bin/sweep-run.sh`, worktree `/workspace/worktrees/vm2-9124-head`):
`:solr:core:cleanTest :solr:core:test --tests org.apache.solr.TestDistributedGrouping --tests org.apache.solr.cloud.BasicDistributedZkTest --tests org.apache.solr.cloud.DistribCursorPagingTest` with `-Ptests.seed=9124C0FFEE9124 -Dtests.seed=9124C0FFEE9124`.

- Result: BUILD SUCCESSFUL, rc=0. TestDistributedGrouping 2 of 2, BasicDistributedZkTest 1 of 1, DistribCursorPagingTest 1 of 1. 0 failures, 0 errors, 0 skipped.
- Log: `/workspace/gates/logs/vm2/9124/head-distrib3-fixed.log`; test XML in `head-distrib3-fixed.xml/`.
- Two earlier attempts did not count as runs of these classes. `head-distrib3` passed `./gradlew` as a task argument, which `sweep-run.sh` already runs, so no tests ran. The next attempt used the wrong package for two classes, so only BasicDistributedZkTest ran. Both logs are kept in `/workspace/gates/logs/vm2/9124/`.

## Run 2: grouped stats tests at head (baseline)

Command: `:solr:core:cleanTest :solr:core:test` with `--tests` for `org.apache.solr.search.stats.TestDefaultStatsCache`, `TestExactStatsCache`, `TestLRUStatsCache`, `TestExactSharedStatsCache`, same seed.

- Result: BUILD SUCCESSFUL, rc=0. 5 of 5 tests, 0 failures. Log `stats-head.log`.

## Run 3: scratch copy with the within-group clause removed

Scratch worktree `/workspace/worktrees/vm2-9124-scratch` at `3dd985b93a`. One uncommitted edit: `needsDistributedStats` loses the `|| (rb.grouping() && rb.getGroupingSpec().getWithinGroupSortSpec().includesScore())` term. Nothing committed or pushed. Same command and seed as run 2.

- Result: BUILD FAILED, rc=1. 5 tests completed, 1 failed.
- Failing test: `org.apache.solr.search.stats.TestExactStatsCache.test`.
- Failure text: `java.lang.AssertionError: expected:<[g0=4, g1=3]> but was:<[g0=4, g1=5]>`.
- Stack: `TestBaseStatsCache.checkWithinGroupOrder(TestBaseStatsCache.java:117)`, called from `checkGroupedScores` (line 97), called from `TestDefaultStatsCache.test` (line 86). Line 117 is the `assertEquals(controlDocs, shardDocs)` in the within-group order check.
- The other four tests in the run passed, including `TestExactStatsCache.testShardsTolerant`.
- Log: `/workspace/gates/logs/vm2/9124/stats-noclause.log`; XML in `stats-noclause.xml/`.

## Reading

The within-group clause decides which stats the group stage uses to rank documents within a group. Without it, shards rank on local stats and the top document per group changes, which the within-group check detects. The PR's test is sufficient to guard the clause.
