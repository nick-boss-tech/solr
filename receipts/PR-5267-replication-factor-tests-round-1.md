# Receipt: PR 5267 replication-factor tests, round 1 (phone agent, Linux)

Status: GREEN at F `b244ae94d6c84eec06bf1de5754e566b0fdb88d1`.

- Job: `gates/pr-5267-replication-factor-tests-round-1.md` (immutable; not edited). Claim: `claims/pr-5267-replication-factor-tests-round-1-phone-agent.md`. Implementation claim: `claims/pr-5267-replication-factor-tests-windows-round-1.md`; implementation report: `reports/pr-5267-replication-factor-tests-implementation-round-1.md`.
- F: `b244ae94d6c84eec06bf1de5754e566b0fdb88d1` on `nick-boss-tech/solr:solr-18603-submit` (fork ref read back at that head after the run). Contributor head O: `7f9acb9df610338c20537e1efd6ed75a6c64a13b` (isaric's PR #5267 head).
- Host: phone agent (Linux, this VM), JDK 21.0.12.1+1, Gradle 9.7.0 via solr-gradle.sh, host test-queue lock held for the whole run. Seed `18603C0DE` for every test leg.
- Evidence: `~/workspace/tools/5267-gate-logs/` on this host (step summary, per-leg logs, fresh JUnit XML copies, mutation diffs and hashes).

## Results

- Scope: `O..F` changes exactly one file, `solr/solrj/src/test/org/apache/solr/client/solrj/impl/CloudHttp2SolrClientTest.java`. Production bytes at F match O (no production file in the diff).
- Tidy at F: run, tree clean afterwards; `git diff --check` clean.
- F tests (`:solr:solrj` CloudHttp2SolrClientTest + NestableJsonFacetTest): 22 + 1 tests, 0 failures, 0 errors, 0 skipped. `testShardReplicationFactors` and `testShardReplicationFactorsWithoutRouteFactors` both executed (named in the fresh XML).
- F root `check -x test`: BUILD SUCCESSFUL. Environment condition: this leg ran with the combined Hatch CA truststore (`~/workspace/tools/truststore-with-hatch-ca.jks`) via JAVA_TOOL_OPTIONS, with the Gradle daemon stopped before and after, per this host's standing root-check practice.
- O comparison run (same command): 20 + 1 tests, 0 failures, 0 errors, 0 skipped. Both heads pass, as the job expects for a coverage repair.
- Mutation omit-route-map (CloudSolrClient drops the per-route factor header): `testShardReplicationFactors` runs 1 test, exactly 1 failure, on the route-map comparison (expected the route URL/factor map, actual null).
- Mutation corrupt-shard-factor (`results.put(shard, 0)`): `testShardReplicationFactors` runs 1 test, exactly 1 failure, on the shard-map comparison: expected `<{shard1=2, shard2=3}>` but was `<{shard2=0, shard1=0}>`; the route-header comparison passed first, as specified.

## Execution note

A first launch of this gate's runner on 2026-11-02 ran concurrently with three other gate runners because of a faulty lock pattern in the runner scripts; that window was discarded in full, all four runners and their daemons were killed, and this gate was rerun from scratch as part of a single serial chain that held the host lock for the whole run. Every result above is from the serial rerun (16:19 to 16:38 MST).

No public posts. No fork pushes; the branch head never moved.
