# Receipt: SOLR-18603 per-shard replication-factor coverage, Linux gate round 1

Status: GREEN for the pinned final head F on vm5, 2026-10-31. Gate: gates/pr-5267-replication-factor-tests-round-1.md. Claim: claims/pr-5267-replication-factor-tests-linux-round-1.md.

This is coverage repair, not a production fix. F changes only solr/solrj/src/test/org/apache/solr/client/solrj/impl/CloudHttp2SolrClientTest.java (confirmed by git diff --name-only O F). Production bytes at F equal O. The two synthetic mutations are the only discriminating proof; the O and F runs are expected passes.

## Heads and trees

- Contributor head O: 7f9acb9df610338c20537e1efd6ed75a6c64a13b, tree 73d525fc9b7e2d002f4a7f682945ab6339d366ea.
- Final head F: b244ae94d6c84eec06bf1de5754e566b0fdb88d1, tree 5eec1f0c89040c28ef2dc389d520ea6d0397bb44.
- Live fork ref refs/heads/solr-18603-submit read from https://github.com/nick-boss-tech/solr.git at receipt time: b244ae94d6c84eec06bf1de5754e566b0fdb88d1 (matches F).
- Host: vm5, Linux 6.1 aarch64, JDK 21.0.12.1+1, Gradle 9.7.0 wrapper, seed 18603C0DE, build lock /workspace/tools/.gradle-vm5.lock.

## Results

| Step | Command | Actual result | Return code |
| --- | --- | --- | --- |
| 1. O pre-fix | `:solr:solrj:cleanTest :solr:solrj:test --tests CloudHttp2SolrClientTest --tests NestableJsonFacetTest -Ptests.seed=18603C0DE` in worktree gate-5267-O | CloudHttp2SolrClientTest 20 tests, 0 failures, 0 errors, 0 skipped; NestableJsonFacetTest 1 test, pass | 0 |
| 2. F tidy | `./gradlew tidy` in plain clone gate-5267-F | no tracked or untracked changes after tidy | 0 |
| 2. F diff check | `git diff --check` | empty output | 0 |
| 2. F post-fix | same command as step 1 at F | CloudHttp2SolrClientTest 22 tests, 0 failures, 0 errors, 0 skipped; NestableJsonFacetTest 1 test, pass | 0 |
| 2. Named methods | XML | testShardReplicationFactors and testShardReplicationFactorsWithoutRouteFactors both executed and passed | |
| 2. F root check | `./gradlew check -x test` in plain clone gate-5267-F | BUILD SUCCESSFUL in 15m 49s; 556 actionable tasks, 533 executed, 23 up-to-date | 0 |
| 3. Mutation omit-route-map | prepare-mutant.py omit-route-map in worktree gate-5267-M1, then testShardReplicationFactors | 1 test executed, 1 failure at CloudHttp2SolrClientTest.java:387: expected the two route factors, but was null | 1 (expected) |
| 3. Mutation corrupt-shard-factor | prepare-mutant.py corrupt-shard-factor in worktree gate-5267-M2, then testShardReplicationFactors | 1 test executed, 1 failure at CloudHttp2SolrClientTest.java:388: expected {shard2=3, shard1=2}, but was {shard2=0, shard1=0}. Route-header assertion before it passed. | 1 (expected) |

Mutation diffs and hashes: mut-omit-route-map.diff (CloudSolrClient.java hash 8759e212...; test hash ab91ddd7... unchanged) and mut-corrupt-shard-factor.diff (CloudSolrClient.java hash 56f1b49e...; test hash ab91ddd7... unchanged). Both mutations were restored in their disposable worktrees after the run; never committed or pushed.

## Log and XML paths (directory /workspace/gates/logs/vm5/pr-5267-replication-factor-tests-round-1)

SHA-256 prefixes of fresh XML (first 16 hex):
- F-post-xml/test/TEST-...CloudHttp2SolrClientTest.xml: 573b553c8fceaf84
- F-post-xml/test/TEST-...NestableJsonFacetTest.xml: bd77851fe37e1440
- O-xml/test/TEST-...CloudHttp2SolrClientTest.xml: 26c936d7bf705978
- O-xml/test/TEST-...NestableJsonFacetTest.xml: 85df73397a4af64f
- mut-omit-route-map-xml/test/TEST-...CloudHttp2SolrClientTest.xml: 2f6d651ccab89f83
- mut-corrupt-shard-factor-xml/test/TEST-...CloudHttp2SolrClientTest.xml: e1329f05ddb8c11f

Command logs: O-pre.log, F-tidy.log, F-diffcheck.txt, F-post.log (sha256 7271d7c7...), F-root-check.log (sha256 e1e7b1a0...), mut-*.log, mut-*-prepare.log, run.status.

## Environment events and repairs

- Setup: first clone checkout of F failed (the clone did not have F). Fetched F and O into the clone by SHA; created worktrees for O and mutations. Recorded in setup.log.
- VM restart #1: during the O/F run, before F-post completed. Relaunched F-post onward once; logs in relaunch-after-vm-fork-2/ (F-post-partial.log).
- VM restart #2: after the first full run started. O-pre, tidy and diff-check had completed before the restart and are kept as-is.
- Root check: run in the plain clone, not a linked worktree, because Antora rejects linked worktrees (same finding as PR 5268).

## Proof limits

- Coverage repair: O and F are both expected passes. The discriminating evidence is the two synthetic compiling mutations, not an observed contributor bug.
- Synthetic mutations do not prove every assertion in CloudHttp2SolrClientTest; the missing-factor compatibility cases remain pins.
- No upstream CI or PR action. The public PR (apache/solr#5267, head O) is unchanged; no comment, edit or push to the contributor branch.
