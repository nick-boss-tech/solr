# Pool assignment: Reproduce flake tickets with named test classes, round 1

Status: DONE (claims/pool-skip-repro-flake-round-1.md, vm2, 2026-10-18T08:20Z; deliverable reports/2026-10-18-skip-repro/A1-flake-round-1.md)

- Slug: `pool-skip-repro-flake-round-1`.
- Capability tags: sweep.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Run the tickets in the table.** For each row, run the Gradle task with the class from that row, following the Decisions table. Start from the pinned head. Output: one line per ticket in the deliverable.
2. **Record.** Write each ticket result into the deliverable file as it finishes, so a stop keeps partial results.

### Tickets

| Ticket | Test class (main) | Gradle task | Skip note (from queue) |
| --- | --- | --- | --- |
| SOLR-2865 | org.apache.solr.client.solrj.embedded.LargeVolumeBinaryJettyTest | `:solr:solrj:test` | test flake |
| SOLR-3220 | org.apache.solr.cloud.RecoveryZkTest | `:solr:core:test` | test flake |
| SOLR-5791 | org.apache.solr.handler.component.DistributedQueryElevationComponentTest | `:solr:core:test` | J9 test flake |
| SOLR-5792 | org.apache.solr.handler.component.TermVectorComponentDistributedTest | `:solr:core:test` | J9 test flake |
| SOLR-5793 | org.apache.solr.update.processor.SignatureUpdateProcessorFactoryTest | `:solr:core:test` | J9 test flake |
| SOLR-6287 | org.apache.solr.cloud.api.collections.CollectionsAPIDistributedZkTest | `:solr:core:test` | test flake |
| SOLR-7597 | org.apache.solr.TestRandomFaceting | `:solr:core:test` | test flake |
| SOLR-7992 | org.apache.solr.handler.component.QueryElevationComponentTest | `:solr:core:test` | test flake |
| SOLR-9144 | org.apache.solr.handler.TestReplicationHandler | `:solr:core:test` | test flake |
| SOLR-9328 | org.apache.solr.search.stats.TestDistribIDF | `:solr:core:test` | test flake |
| SOLR-9390 | org.apache.solr.cloud.LeaderFailoverAfterPartitionTest | `:solr:core:test` | test flake |
| SOLR-9427 | org.apache.solr.handler.TestReplicationHandler | `:solr:core:test` | test flake |
| SOLR-9428 | org.apache.solr.schema.TestManagedSchemaAPI | `:solr:core:test` | test flake, root cause in ZK read retry |
| SOLR-9965 | org.apache.solr.handler.TestReplicationHandler | `:solr:core:test` | test flake |
| SOLR-10592 | org.apache.solr.metrics.SolrCoreMetricManagerTest | `:solr:core:test` | test flake |
| SOLR-10759 | org.apache.solr.schema.TestUseDocValuesAsStored | `:solr:core:test` | test flake |
| SOLR-10841 | org.apache.solr.cloud.TestTlogReplica | `:solr:core:test` | test flake |
| SOLR-10976 | org.apache.solr.client.solrj.io.stream.StreamExpressionTest | `:solr:solrj-streaming:test` | Windows/jenkins test flake |
| SOLR-11058 | org.apache.solr.cloud.TestTlogReplica | `:solr:core:test` | Windows test flake |
| SOLR-11247 | org.apache.solr.common.cloud.TestCollectionStateWatchers | `:solr:solrj:test` | per session 2 handoff triage: flake, AwaitsFix on old branch, obsolete (Trie/EnumField removed), or design-level |
| SOLR-11463 | org.apache.solr.cloud.TestCloudSearcherWarming | `:solr:core:test` | test flake (TestCloudSearcherWarming on Jenkins 2017), test-infra only. |
| SOLR-11627 | org.apache.solr.cloud.AddReplicaTest | `:solr:core:test` | test flake (AddReplicaTest on Jenkins 2017); only debug logging was added, no product defect identified. |
| SOLR-11826 | org.apache.solr.security.TestPKIAuthenticationPlugin | `:solr:core:test` | test flake (TestPKIAuthenticationPlugin NPE, Jenkins 2018), test-infra only. |
| SOLR-12093 | org.apache.solr.cloud.LeaderVoteWaitTimeoutTest | `:solr:core:test` | test flake (LeaderVoteWaitTimeoutTest NPE in JettySolrRunner.getNodeName), nightly Jenkins 2018; test-infra only, no ... |
| SOLR-12302 | org.apache.solr.cloud.BasicDistributedZkTest | `:solr:core:test` | ant-era BasicDistributedZkTest seed failures (2018); ant build gone, test flake |
| SOLR-12844 | org.apache.solr.handler.component.TestDistributedStatsComponentCardinality | `:solr:core:test` | stale 2018 7.x nightly test flake, 0 comments, no triage, no repro on current main |
| SOLR-14881 | org.apache.solr.schema.ManagedSchemaRoundRobinCloudTest | `:solr:core:test` | Stale 2020 test-flake ticket; Noble Paul's fix commit already in the tree, ticket left Open |
| SOLR-16094 | org.apache.solr.handler.TestSolrConfigHandlerCloud | `:solr:core:test` | stale 2022 test-flake: TestSolrConfigHandlerCloud hang on OSX, no comments, never reproduced |

### Decisions

| Decision | Answer or default |
| --- | --- |
| Head | decided: the fork origin/main sha 498a09aea9 at posting time. Record the sha you ran. Do not move it. |
| Build protocol | decided: one Gradle build per host, under the host lock in `hosts/pool-linux.md` (`flock -n`). Record `df -h /` and `df -i /` before and after each run in the claim. |
| Worktree | decided: one detached worktree under `/workspace/worktrees/` at the pinned head. Do not reuse another session worktree. Never run bare `git stash`. |
| JDK and Gradle home | decided: the JDK and GRADLE_USER_HOME recorded in your host file under `hosts/`. Caches under /workspace only. |
| Run cadence | decided: run each class once with `--rerun`. If it fails, run it once more with the failing seed (pass the seed printed in the failing log as `-Ptests.seed`, written out in full). Stop at two runs per ticket. |
| Outcome recording | decided: record the test name, pass or fail, the assertion or exception line, the seed, the log path, and the XML path for each run. Do not interpret beyond that in this assignment. |
| Class not on main | default: record "not on main" and move to the next ticket; no other action. |
| Disk | default: if root filesystem free space falls under 2 GB, stop the sweep, record it, and escalate to the main agent. |
| Code changes | decided: none. No edits to main code or tests. The only commit is the deliverable file. |
| Closing or commenting | decided: none. Nothing is closed, commented or posted to JIRA or GitHub. Escalate any ticket that looks ready to close. |
| Scope | decided: only the rows in this table. |

### Deliverable

`reports/2026-10-18-skip-repro/A1-flake-round-1.md`: one row per ticket with the run results, seeds, and XML paths.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

