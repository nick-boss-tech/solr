# Pool assignment: Reproduce non-flake tickets with named test classes, round 1

Status: CLAIMED

- Slug: `pool-skip-repro-other-round-1`.
- Capability tags: sweep.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Run the tickets in the table.** For each row, run the Gradle task with the class from that row, following the Decisions table. Start from the pinned head. Output: one line per ticket in the deliverable.
2. **Record.** Write each ticket result into the deliverable file as it finishes, so a stop keeps partial results.

### Tickets

| Ticket | Test class (main) | Gradle task | Skip note (from queue) |
| --- | --- | --- | --- |
| SOLR-2900 | org.apache.solr.core.TestCoreContainer | `:solr:core:test` | test hygiene |
| SOLR-2928 | org.apache.solr.update.TestIndexingPerformance | `:solr:core:test` | test leaves write.lock, old |
| SOLR-3461 | org.apache.solr.search.TestRealTimeGet | `:solr:core:test` | test slowness on Windows |
| SOLR-4139 | org.apache.solr.handler.TestReplicationHandler | `:solr:core:test` | test with preventDoubleWrite, old |
| SOLR-5067 | org.apache.solr.handler.TestReplicationHandler | `:solr:core:test` | test cleanup |
| SOLR-5485 | org.apache.solr.cloud.ChaosMonkeySafeLeaderTest | `:solr:core:test` | very rare lucene corruption in test |
| SOLR-7075 | org.apache.solr.handler.TestConfigReload | `:solr:core:test` | revisited in spot-check: 2015 Jenkins failure of TestConfigReload.checkConfReload, 1 comment, only stack trace + atta... |
| SOLR-7899 | org.apache.solr.handler.sql.TestSQLHandler | `:solr:modules:sql:test` | non-reproducible test failure |
| SOLR-11274 | org.apache.solr.ltr.feature.TestEdisMaxSolrFeature | `:solr:modules:ltr:test` | LTR test config problem in reporter's environment (unknown field description); test-infra. |
| SOLR-12296 | org.apache.solr.update.processor.TimeRoutedAliasUpdateProcessorTest | `:solr:core:test` | flaky test (TimeRoutedAliasUpdateProcessorTest, 2018 jenkins), no deterministic repro |
| SOLR-12819 | org.apache.solr.schema.TestPointFields | `:solr:core:test` | stale 8-yr-old seeded test failure report (TestPointFields sort), zero comments, no confirmed product bug, maintainer... |
| SOLR-12918 | org.apache.solr.spelling.suggest.TestHighFrequencyDictionaryFactory | `:solr:core:test` | Stale 2018 test-infra issue; crashing code is upstream Lucene test-framework code, not in the Solr repo; zero comment... |
| SOLR-14784 | org.apache.solr.update.DirectUpdateHandlerTest | `:solr:core:test` | stale 2020 test-only flaky-test ticket; root cause diagnosed in-thread (LUCENE-9456), assertion rewritten on current ... |
| SOLR-17133 | org.apache.solr.client.solrj.io.stream.CloudAuthStreamTest | `:solr:solrj-streaming:test` | test-only failure under PRS mode; empty description, 0 comments; reporter/assignee is Noble Paul (self-assigned maint... |
| SOLR-4593 | org.apache.solr.cloud.api.collections.CollectionsAPIDistributedZkTest | `:solr:core:test` | test flake, fixed 4.9/6.0 |
| SOLR-5822 | org.apache.solr.cloud.ChaosMonkeyNothingIsSafeTest | `:solr:core:test` | chaos monkey test finding, fixed 4.9/6.0 |
| SOLR-9593 | org.apache.solr.cloud.api.collections.ConcurrentDeleteAndCreateCollectionTest | `:solr:core:test` | test flake, fixed 6.7/7.0 |
| SOLR-11563 | org.apache.solr.schema.TestCloudSchemaless | `:solr:core:test` | test failure already fixed by a follow-up commit per SOLR-11484 discussion; reporter confirmed. |
| SOLR-13486 | org.apache.solr.cloud.TestTlogReplayVsRecovery | `:solr:core:test` | design-heavy 2019 tlog-replay vs recovery race; needs maintainer design consensus, no assignee/PR in 7 years |
| SOLR-14061 | org.apache.solr.cloud.api.collections.TestLocalFSCloudBackupRestore | `:solr:core:test` | Stale 2019 ticket: speculative corruption report triggered by SecurityManager-era AccessControlException from Files.e... |
| SOLR-16122 | org.apache.solr.cloud.TestLeaderElectionZkExpiry | `:solr:core:test` | stale 2022 flaky-test ticket (BadApple-mitigated, still AwaitsFix on main); thread-leak test infra, no product bug; s... |

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
| Scope | decided: only the rows in this table. Fixed-in-version rows are checks on main, not fixes. |

### Deliverable

`reports/2026-10-18-skip-repro/A3-other-round-1.md`: one row per ticket with the run results, seeds and XML paths.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

