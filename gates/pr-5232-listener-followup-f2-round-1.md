# Gate: PR 5232 listener correctness follow-up at F2, round 1 (superseding)

Status: OPEN. Immutable once pushed. Proves only the exact head F2 below.
Supersedes: `gates/pr-5232-listener-followup-round-1.md`, which named head `b3a11484f13feaab55fe4f27504bfce3d1bdd2ef`. That head is tidy-dirty by one line in `SearchRequestListenerTest.java`, so the job's own rule requires a formatting commit and a superseding gate before proof.

- Exact head F2: `23c68215cf0c858b7dd1ceda6b945d2d9daaa174` (commit "SOLR-11994: tidy the CPU assertion line in SearchRequestListenerTest", author Nick Shanin, no trailers, one commit above b3a11484f1).
- Implementation head: `b3a11484f13feaab55fe4f27504bfce3d1bdd2ef` (parent of F2).
- Fork: `nick-boss-tech/solr`, branch `solr-11994-submit`. Read back by ls-remote after the push.
- Original PR proof base: apache/solr PR 5232 head `2c9243b111b38367677d7e0c9b30b8e72c554d2c` (`sarkaramrit1993/solr:SOLR-11994-search-request-listener`).
- Claim: `claims/pr-5232-listener-followup-linux-vm4-round-1.md`.
- Host: vm4, Linux 6.1.166 aarch64. JDK Temurin 21.0.12.1+1 at `/workspace/tools/jdk21`. Gradle 9.7.0 via the repo wrapper, `GRADLE_USER_HOME=/workspace/gradle-home`.
- Build lock: `/workspace/tools/.gradle-vm4.lock`, through `/workspace/tmp/pool-bin/sweep-run.sh`. One build at a time. Runner records df before and after each leg and runs the inode guard.
- Positive worktree: `/workspace/worktrees/gate-5232`, detached at F2, clean.
- Negative worktree: `/workspace/worktrees/gate-5232-neg`, detached at PR head `2c9243b1`, overlaid with the two test files from F2 and nothing else.
- Logs: `/workspace/gates/logs/vm4/pr-5232-listener/`, prefixes `f2-`. Each leg has `.log`, `.headroom.log`, and `.xml/` for fresh JUnit XML copied by the runner.
- Preflight: `preflight-tidy.log` at b3a11484f1 (tidy changed one test line; see above). `preflight-tidy-f2.log` at F2: tidy rc 0, tree clean.

## Legs, in order

1. Positive focused class, F2:
   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestInfoTest --tests org.apache.solr.handler.component.SearchRequestListenerTest -Ptests.seed=5232C0DE`
   Require 17 executed tests (4 CPU snapshot tests, 13 listener tests), zero failures, errors, and skips. Thread CPU timing must be supported so the CPU test executes.
2. Neighbor behavior, F2:
   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.request.TestSolrRequestInfo --tests org.apache.solr.response.transform.TestSubQueryTransformerDistrib -Ptests.seed=5232C0DE`
   Require zero failures, errors, and skips. Record actual counts.
3. Root check, F2: `./gradlew check -x test`. Record the real result. No silent exclusions.
4. `git diff --check` and clean tree at F2.
5. Negative leg, parent `2c9243b1` plus the two F2 test files only (`SearchRequestInfoTest.java`, `SearchRequestListenerTest.java`). Record the synthetic tree and test-file hashes. Run:
   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestInfoTest.testDistributedFailureCountsLocalCpuOnce --tests org.apache.solr.handler.component.SearchRequestListenerTest.testDistributedSubQueriesNotReported --tests org.apache.solr.handler.component.SearchRequestListenerTest.testLocalSubQueriesNotReported -Ptests.seed=5232C0DE`
   Require all three to fail for the intended assertions: CPU `expected 7` against actual 14, and more than one event in each subquery test after the child-result assertions pass. Record observed counts and assertion messages. A compile error, startup failure, skip, missing method, or passing method is not fail-before evidence.

## Completion

GREEN requires exact-head positive proof (legs 1 to 4), discriminating negative proof for both findings (leg 5), a clean tidy at F2, and retained fresh XML and logs. Failed executed tests are investigated and repaired under WORKFLOW.md, not retried blindly. Receipt: `receipts/PR-5232-listener-followup.md`, with the final section as a suggested upstream update for Nick to review. No public posting.
