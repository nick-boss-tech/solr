# Skip-repro sweep, round 1: locate and reproduce flake tickets without a named class

Assignment: `assignments/pool-skip-repro-flake-locate-round-1.md`. Claim: `claims/pool-skip-repro-flake-locate-round-1.md`.
Head: fork origin/main `498a09aea9`, detached worktree `/workspace/worktrees/vm5-skip-flake-locate`. JDK 21.0.12.1 at `/workspace/jdk/jdk-21.0.12.1+1` and GRADLE_USER_HOME `/workspace/gradle-home`, both from `~/.pool-env` on vm5.
Locate: each ticket's JIRA description was read through the anonymous REST API (`issues.apache.org/jira/rest/api/2/issue/KEY?fields=summary,description`, read only). Only the description was used to name a class, per the Locate default.
Run cadence: each class run once with `--rerun`. No run failed, so no second run and no failing-seed rerun was made.
Build: each run went under the vm5 host lock through `pool/bin/sweep-run.sh`. Five runs were made; the other twelve tickets have no run (reasons below).
Headroom: sweep start 2026-10-18T06:37:49Z, 3.6G free and 454453 inodes free on `/`; sweep end 2026-10-18T06:53:14Z, 3.6G free and 454455 inodes free on `/`. Per-run headroom logs sit beside each log.
Tests / failures / errors / skipped is read from each class's JUnit XML. Skipped counts are reported as-is.

## Runs

| Ticket | Class located from | Test class | Gradle task | Build | Tests / failures / errors / skipped | Seed | Log | XML |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| SOLR-6593 | description | `org.apache.solr.cloud.DeleteReplicaTest` | `:solr:core:test --rerun --tests org.apache.solr.cloud.DeleteReplicaTest` | SUCCESSFUL | 7 / 0 / 0 / 0 | 80D3237B6848C6DD | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-6593.log` | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-6593.xml/TEST-org.apache.solr.cloud.DeleteReplicaTest.xml` |
| SOLR-6914 | description | `org.apache.solr.update.UpdateParamsTest` | `:solr:core:test --rerun --tests org.apache.solr.update.UpdateParamsTest` | SUCCESSFUL | 1 / 0 / 0 / 0 | AF03155EB66B7756 | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-6914.log` | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-6914.xml/TEST-org.apache.solr.update.UpdateParamsTest.xml` |
| SOLR-9426 | description | `org.apache.solr.handler.TestReplicationHandler` | `:solr:core:test --rerun --tests org.apache.solr.handler.TestReplicationHandler` | SUCCESSFUL | 25 / 0 / 0 / 25 | 420562093AFD92BD | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-9426.log` | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-9426.xml/TEST-org.apache.solr.handler.TestReplicationHandler.xml` |
| SOLR-11193 | description | `org.apache.solr.schema.EnumFieldTest` | `:solr:core:test --rerun --tests org.apache.solr.schema.EnumFieldTest` | SUCCESSFUL | 15 / 0 / 0 / 1 | 364457875F164E62 | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-11193.log` | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-11193.xml/TEST-org.apache.solr.schema.EnumFieldTest.xml` |
| SOLR-12220 | description | `org.apache.solr.handler.TestReplicationHandler` | `:solr:core:test --rerun --tests org.apache.solr.handler.TestReplicationHandler` | SUCCESSFUL | 25 / 0 / 0 / 25 | 265F41BA244CFB3 | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-12220.log` | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-12220.xml/TEST-org.apache.solr.handler.TestReplicationHandler.xml` |
| SOLR-18007 | description (a log path in the description names the class) | `org.apache.solr.spelling.suggest.TestAnalyzedSuggestions` | `:solr:core:test --rerun --tests org.apache.solr.spelling.suggest.TestAnalyzedSuggestions` | SUCCESSFUL | 2 / 0 / 0 / 0 | D60B805699E59882 | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-18007.log` | `/workspace/gates/logs/vm5/skip-flake-locate/SOLR-18007.xml/TEST-org.apache.solr.spelling.suggest.TestAnalyzedSuggestions.xml` |

Notes per run:
- SOLR-6593: the description also names `DeleteLastCustomShardedReplica`. The row names one class, so that name was not run.
- SOLR-9426 and SOLR-12220 both name `TestReplicationHandler`. Each ticket has its own run. In both, all 25 tests are skipped, the same shape as the earlier round-1 rows for this class.
- SOLR-11193: the one skipped test in `EnumFieldTest` is recorded as-is.

## Not run

| Ticket | Result | Reason |
| --- | --- | --- |
| SOLR-2715 | no test named | The description names no class. The summary names `TestJMXSolrIntegration`. Per the Locate default, escalated to the main agent; not run. |
| SOLR-2720 | no test named | The description names no class. The summary names `TestLBHttpSolrServer`. Escalated; not run. |
| SOLR-3944 | no test named | The description names no class (an OOM report on Solr Cell). Escalated; not run. |
| SOLR-5948 | no test named | The description is empty. Escalated; not run. |
| SOLR-7646 | no test named | The description names no class (an OOM stack trace from the suggest handler). Escalated; not run. |
| SOLR-9182 | no test named | The description names no class. Escalated; not run. |
| SOLR-11168 | not on main | Names `ConcurrentUpdateSolrClientTest`. No file of that name exists at `498a09aea9`. Recorded as "not on main". |
| SOLR-11208 | no test named | The description names no class. Escalated; not run. |
| SOLR-11214 | no test named | The description names no class. Escalated; not run. |
| SOLR-11259 | not on main | Names `SharedFSAutoReplicaFailoverTest`. No file of that name exists at `498a09aea9`. Recorded as "not on main". |
| SOLR-12369 | no test named | The description names no class. Escalated; not run. |
| SOLR-12748 | no test named | The description names no class (an OOM report on a cloud example startup). Escalated; not run. |

## Escalations for the main agent

- Ten tickets have no class in the description: SOLR-2715, SOLR-2720, SOLR-3944, SOLR-5948, SOLR-7646, SOLR-9182, SOLR-11208, SOLR-11214, SOLR-12369 and SOLR-12748. Two of them, SOLR-2715 and SOLR-2720, name a class in the summary only. Per the Locate default these are not run. The choice whether the summary name counts is the main agent's.
- No run failed, so no ticket was flagged as ready to close. Whether any row closes is not decided here.
