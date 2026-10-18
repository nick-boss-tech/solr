# Flake ticket reproduction, round 1 (vm2)

Assignment: `assignments/pool-skip-repro-flake-round-1.md`. Claim: `claims/pool-skip-repro-flake-round-1.md`.

- Head: `498a09aea9` (fork `origin/main`, pinned at posting time, checked out detached at `/workspace/worktrees/pool-vm2-flake-r1`). All 28 test classes exist on this head.
- Host: vm2. JDK 21.0.12.1 (Temurin) at `/workspace/jdk/jdk-21.0.12.1+1`, GRADLE_USER_HOME `/workspace/gradle-home`.
- Command per run, through `pool/bin/sweep-run.sh`: `./gradlew --no-daemon --console=plain <module>:test --tests <class> --rerun`. The rerun adds `-Ptests.seed=<seed>`.
- Logs: `/workspace/gates/logs/vm2/flake-r1/<ticket>-p1.log` (pass 1) and `<ticket>-p2.log` (rerun). XML copies: `<ticket>-p1.xml/`, `<ticket>-p2.xml/`. Headroom: `<ticket>-p*.headroom.log`.
- Pass 1 ran each row once with no explicit seed. The seed is the one Gradle printed. Per the Decisions row, each failing ticket got one rerun with its failing seed pinned.

## Summary

- Pass 1: 26 of 28 rows passed (rc=0, no failures, no errors). 2 rows failed (rc=1): SOLR-10841 and SOLR-11058. Both are `TestTlogReplica`.
- Rerun at the failing seed: both failed again, with the same failing test methods and the same messages. The rerun reproduces the failure at each seed.
- Free space on `/` stayed at 4.5 GB and free inodes at about 91% for the whole sweep.

## Results

| Ticket | Test class | Gradle task | Pass 1 | rc | Seed | tests/fail/err | Rerun, failing seed pinned | Pass 1 XML |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| SOLR-2865 | `org.apache.solr.client.solrj.embedded.LargeVolumeBinaryJettyTest` | `:solr:solrj:test` | PASS | 0 | 9EB8C4018C18B692 | 1/0/0 | not run | `flake-r1/SOLR-2865-p1.xml/` |
| SOLR-3220 | `org.apache.solr.cloud.RecoveryZkTest` | `:solr:core:test` | PASS | 0 | 8798F4E55CD18AE | 1/0/0 | not run | `flake-r1/SOLR-3220-p1.xml/` |
| SOLR-5791 | `org.apache.solr.handler.component.DistributedQueryElevationComponentTest` | `:solr:core:test` | PASS | 0 | A0F6793AC7F42061 | 1/0/0 | not run | `flake-r1/SOLR-5791-p1.xml/` |
| SOLR-5792 | `org.apache.solr.handler.component.TermVectorComponentDistributedTest` | `:solr:core:test` | PASS | 0 | 2A6A9666CA26C5CE | 1/0/0 | not run | `flake-r1/SOLR-5792-p1.xml/` |
| SOLR-5793 | `org.apache.solr.update.processor.SignatureUpdateProcessorFactoryTest` | `:solr:core:test` | PASS | 0 | 84BC03CE7EE0459A | 6/0/0 | not run | `flake-r1/SOLR-5793-p1.xml/` |
| SOLR-6287 | `org.apache.solr.cloud.api.collections.CollectionsAPIDistributedZkTest` | `:solr:core:test` | PASS | 0 | 5D0AB02F48FB5320 | 17/0/0 | not run | `flake-r1/SOLR-6287-p1.xml/` |
| SOLR-7597 | `org.apache.solr.TestRandomFaceting` | `:solr:core:test` | PASS | 0 | 9C6BD3B3B9E488EF | 1/0/0 | not run | `flake-r1/SOLR-7597-p1.xml/` |
| SOLR-7992 | `org.apache.solr.handler.component.QueryElevationComponentTest` | `:solr:core:test` | PASS | 0 | 40CFFD8971369095 | 16/0/0 | not run | `flake-r1/SOLR-7992-p1.xml/` |
| SOLR-9144 | `org.apache.solr.handler.TestReplicationHandler` | `:solr:core:test` | PASS | 0 | C216E516B8C5627A | 25/0/0 | not run | `flake-r1/SOLR-9144-p1.xml/` |
| SOLR-9328 | `org.apache.solr.search.stats.TestDistribIDF` | `:solr:core:test` | PASS | 0 | 1B3B3071A669191C | 3/0/0 | not run | `flake-r1/SOLR-9328-p1.xml/` |
| SOLR-9390 | `org.apache.solr.cloud.LeaderFailoverAfterPartitionTest` | `:solr:core:test` | PASS | 0 | 393515668CFE64B8 | 1/0/0 | not run | `flake-r1/SOLR-9390-p1.xml/` |
| SOLR-9427 | `org.apache.solr.handler.TestReplicationHandler` | `:solr:core:test` | PASS | 0 | 22C6DD368C4D28A3 | 25/0/0 | not run | `flake-r1/SOLR-9427-p1.xml/` |
| SOLR-9428 | `org.apache.solr.schema.TestManagedSchemaAPI` | `:solr:core:test` | PASS | 0 | C825CEC6D875F28E | 1/0/0 | not run | `flake-r1/SOLR-9428-p1.xml/` |
| SOLR-9965 | `org.apache.solr.handler.TestReplicationHandler` | `:solr:core:test` | PASS | 0 | 6EF7EAAC35553A06 | 25/0/0 | not run | `flake-r1/SOLR-9965-p1.xml/` |
| SOLR-10592 | `org.apache.solr.metrics.SolrCoreMetricManagerTest` | `:solr:core:test` | PASS | 0 | F7EF26FDD0F61C31 | 4/0/0 | not run | `flake-r1/SOLR-10592-p1.xml/` |
| SOLR-10759 | `org.apache.solr.schema.TestUseDocValuesAsStored` | `:solr:core:test` | PASS | 0 | 9EEEC0633D8796DF | 11/0/0 | not run | `flake-r1/SOLR-10759-p1.xml/` |
| SOLR-10841 | `org.apache.solr.cloud.TestTlogReplica` | `:solr:core:test` | FAIL | 1 | B2EDDADF9B4A881F | 14/2/0 | FAIL, rc=1, 14/2/0, same seed | `flake-r1/SOLR-10841-p1.xml/` |
| SOLR-10976 | `org.apache.solr.client.solrj.io.stream.StreamExpressionTest` | `:solr:solrj-streaming:test` | PASS | 0 | 5DF3F7F65065E53C | 33/0/0 | not run | `flake-r1/SOLR-10976-p1.xml/` |
| SOLR-11058 | `org.apache.solr.cloud.TestTlogReplica` | `:solr:core:test` | FAIL | 1 | F982A35C90FB67A5 | 14/3/0 | FAIL, rc=1, 14/3/0, same seed | `flake-r1/SOLR-11058-p1.xml/` |
| SOLR-11247 | `org.apache.solr.common.cloud.TestCollectionStateWatchers` | `:solr:solrj:test` | PASS | 0 | 21FA8A11BC9942C6 | 10/0/0 | not run | `flake-r1/SOLR-11247-p1.xml/` |
| SOLR-11463 | `org.apache.solr.cloud.TestCloudSearcherWarming` | `:solr:core:test` | PASS | 0 | 79F00C0EE1E59DB2 | 2/0/0 | not run | `flake-r1/SOLR-11463-p1.xml/` |
| SOLR-11627 | `org.apache.solr.cloud.AddReplicaTest` | `:solr:core:test` | PASS | 0 | 130AB2BA39750A2E | 3/0/0 | not run | `flake-r1/SOLR-11627-p1.xml/` |
| SOLR-11826 | `org.apache.solr.security.TestPKIAuthenticationPlugin` | `:solr:core:test` | PASS | 0 | B936D489BA66A869 | 4/0/0 | not run | `flake-r1/SOLR-11826-p1.xml/` |
| SOLR-12093 | `org.apache.solr.cloud.LeaderVoteWaitTimeoutTest` | `:solr:core:test` | PASS | 0 | 97B5BFB090B42617 | 2/0/0 | not run | `flake-r1/SOLR-12093-p1.xml/` |
| SOLR-12302 | `org.apache.solr.cloud.BasicDistributedZkTest` | `:solr:core:test` | PASS | 0 | 152ECDC474A04E45 | 1/0/0 | not run | `flake-r1/SOLR-12302-p1.xml/` |
| SOLR-12844 | `org.apache.solr.handler.component.TestDistributedStatsComponentCardinality` | `:solr:core:test` | PASS | 0 | A0D025C0835FBE2A | 1/0/0 | not run | `flake-r1/SOLR-12844-p1.xml/` |
| SOLR-14881 | `org.apache.solr.schema.ManagedSchemaRoundRobinCloudTest` | `:solr:core:test` | PASS | 0 | 80C222C6E18916C8 | 1/0/0 | not run | `flake-r1/SOLR-14881-p1.xml/` |
| SOLR-16094 | `org.apache.solr.handler.TestSolrConfigHandlerCloud` | `:solr:core:test` | PASS | 0 | 64F796917C63A967 | 1/0/0 | not run | `flake-r1/SOLR-16094-p1.xml/` |

Shared classes: `TestReplicationHandler` is in rows SOLR-9144, SOLR-9427 and SOLR-9965. `TestTlogReplica` is in rows SOLR-10841 and SOLR-11058. Each row was run as its own build.

## Failing rows

**SOLR-10841 and SOLR-11058, `org.apache.solr.cloud.TestTlogReplica`.**

| Run | Seed | Failing test methods, each `AssertionError` |
| --- | --- | --- |
| SOLR-10841 pass 1 | B2EDDADF9B4A881F | `testKillLeader` "Expected collection to be 1x2"; `testRecovery` "Replica didn't recover" |
| SOLR-10841 rerun | B2EDDADF9B4A881F | same two methods, same messages |
| SOLR-11058 pass 1 | F982A35C90FB67A5 | `testRecovery` "Replica didn't recover"; `testOnlyLeaderIndexes` "Some replicas are not in sync with leader"; `testKillLeader` "Expected collection to be 1x2" |
| SOLR-11058 rerun | F982A35C90FB67A5 | same three methods, same messages |

Logs: `flake-r1/SOLR-10841-p1.log`, `SOLR-10841-p2.log`, `SOLR-11058-p1.log`, `SOLR-11058-p2.log`. XML: `flake-r1/SOLR-10841-p1.xml/`, `SOLR-10841-p2.xml/`, `SOLR-11058-p1.xml/`, `SOLR-11058-p2.xml/`.

The exception lines in both failing logs are the same kind. Index fetch in the recovering replica fails with `FileSystemException: <path>/<segment> -> <path>/<segment>: Too many links`. The log has 12 such lines for SOLR-10841 and 29 for SOLR-11058. The file names are `_0.si`, `_2.si`, `_b.fdm` and `_k.fdt`, under `tlog_replica_test_*_shard1_replica_t3/data/index.*`. The "Too many links" text is the errno for hard-link creation (EMLINK). Nothing in this assignment interprets that further.

## Notes

- Every run used the decided command, and no run was repeated without a changed input. The reruns of the two failing rows are the only repeats. The first runs had no `-Ptests.seed`. The reruns pin the printed seed, and the claim records that before each rerun.
- No code, test or branch change was made. The only commit is this deliverable and the claim file.
