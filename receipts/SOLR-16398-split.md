# SOLR-16398 split round — receipt (2026-10-29, phone agent)

## What happened

The four gated family branches were split into eleven branches per Jason
Gerlowski's sizing guidance on SOLR-16398 (one or two APIs per PR), and the
ticket comment (18134470) was edited to that trajectory at the owner's
direction, including a step-aside offer if Jalaz Kumar is already working on
any of the slices. Eric Pugh replied (comment 18134536): open one
representative collection command first (split shard), tune it against
dev-docs/apis-v2-conventions.adoc, and scale up from there; replication stays
with Jalaz Kumar.

## Split branches and verification

Each child was verified at its own head: tidy clean, Error Prone compile
rc 0 (including SolrJ OpenAPI generation), and the family's focused tests
green. Per the owner's gate ruling O6, the parent family gates stand as the
broader test evidence; the PR bodies say so where relevant.

- solr-16398-split-move at fb1892a82b (CollectionCommandsV2IntegrationTest,
  TestCollectionAPIs, V2CollectionAPIMappingTest green)
- solr-16398-rebalance-migrate at f47e324e7b (same three classes green)
- solr-16398-core-recovery at 8b9e2bff45 (CoreCommandsV2RoutesIntegrationTest,
  V2CoreAPIMappingTest, V2NodeAPIMappingTest green)
- solr-16398-core-election at ad7c7cea83 (same three classes green)
- solr-16398-core-updates at 2eaa7088d5 (same three classes green)
- solr-16398-core-split at 2106b4ce39 (same three classes green)
- solr-16398-replication-fetch at 753cfbcd4e (CoreReplicationAPITest green)
- solr-16398-reindex at 528a9eb9549 (already a standalone gated branch;
  unchanged by the split)

Assembled but not verified (verification cut to expedite the representative
PR; the replication area is stepped aside to Jalaz Kumar per Eric's comment):
solr-16398-replication-poll, solr-16398-replication-backup,
solr-16398-debug-dump, each in its worktree under ~/workspace/solr-worktrees/.

## The representative PR

solr-16398-split-shard at 6c56a1823689af74ecef99caca12fa7f3f8990ad was opened
as draft PR #5265 (https://github.com/apache/solr/pull/5265) on 2026-10-29 at
the owner's direction, with Eric Pugh as sponsor.

Conventions audit of the slice: path, POST verb, response type
(SubResponseAccumulatingJerseyResponse via
submitRemoteMessageAndHandleResponse), and async handling conform to
dev-docs/apis-v2-conventions.adoc and match landed siblings. One defect found
and fixed: a bare swagger @RequestBody on the endpoint silently dropped the
request body from the generated OpenAPI spec (the split operation had no
requestBody and the generated ShardsApi.SplitShard client had no body
setters). The fix, @RequestBody(description = "Contains user provided
parameters") following BalanceReplicasApi, restores both; verified in the
regenerated client (setNumSubShards present). The same bare annotation exists
in the sibling branches' endpoints (MoveReplicaApi confirmed) and gets the
same fix in each branch's own tuning pass.

Tests at the PR head: SplitShardAPITest 2 tests, 0 failures (dedicated test
in the doc's generated-client style, replacing the shared integration test
for this API); TestCollectionAPIs 2 tests, 0 failures.
