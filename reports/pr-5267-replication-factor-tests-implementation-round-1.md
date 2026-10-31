# SOLR-18603 replication-factor API coverage: implementation, round 1

Status: Implemented, Proof Pending. Linux gate requested; no runtime result claimed.

- Authority: Nick's request to address the Medium finding from the PR 5267 review and hand it to the gate with pre/post proof.
- Claim: claims/pr-5267-replication-factor-tests-windows-round-1.md, posted at board commit 286b9625c6fca0b1361597305b12582208244260.
- Original contributor head O: 7f9acb9df610338c20537e1efd6ed75a6c64a13b.
- Fork branch: nick-boss-tech/solr:solr-18603-submit.
- Pushed head F: b244ae94d6c84eec06bf1de5754e566b0fdb88d1.
- Public PR: https://github.com/apache/solr/pull/5267, still at O when handed off. The contributor branch and public PR were not changed.
- Gate: gates/pr-5267-replication-factor-tests-round-1.md, OPEN for a capable Linux gate host.
- Receipt destination: receipts/PR-5267-replication-factor-tests-round-1.md, execution-host-owned.

## Change and scope

One modified file: solr/solrj/src/test/org/apache/solr/client/solrj/impl/CloudHttp2SolrClientTest.java. No production changes, dependencies, configsets, API seams or new changelog fragments.

testShardReplicationFactors uses the existing cloud cluster and configset to obtain two real shard leaders, condenses synthetic route responses with distinct reported factors 2 and 3, then checks the complete URL-to-factor header, the complete shard-name-to-factor public API result, and minimum 2. Distinct factors prevent a single minimum copied to every shard from satisfying the map assertion. Synthetic factors do not claim the healthy single-replica cluster actually achieved factors 2 and 3; these inputs isolate the response transformation from network/replication health.

testShardReplicationFactorsWithoutRouteFactors checks omitted factor metadata, empty per-shard results, minimum -1 and the per-shard reader's absent-header behavior. A nonexistent collection name ensures an absent route map does not require collection lookup.

The existing checkSingleServer helper now verifies the number of returned routes, factor values and the exact public per-shard map for real updates to its two-shard, single-replica routing collection. It retains the existing leader-membership assertions. Existing cluster/client lifetimes are reused; no additional client requires closing.

## Pre/post and negative proof

The review identified missing tests, not a production bug. All production bytes at F match O. The gate therefore requires the original two classes at O to pass, then the same classes including the new methods at F to pass. Test-only overlay from F onto O yields F's exact tree. No historical behavioral failure is claimed.

The gate separately requires two isolated production mutations at F to compile and fail at the relevant new assertions: omit rfByRoute from the condensed response header; corrupt each returned per-shard factor to 0. The unchanged minimum shortcut cannot mask the latter because the test calls getShardReplicationFactor directly. Negative controls are synthetic regression checks, not evidence of defects in the contributor's current code.

Positive proof requires clean tidy, complete affected CloudHttp2SolrClientTest and neighboring NestableJsonFacetTest, root check -x test, and final local/remote head agreement. Fresh XML, actual method counts, commands, seed and logs are required in the execution receipt. Any formatting or source repair changes the proof head and requires a superseding gate.

## Review and limits

The test inputs and expected results follow the agreed route-header design and the preserved API. The exact result-map assertions cover missing/extra routes and wrong factor values, unlike leader-membership checks alone. Existing response constructors and the existing cloud harness are reused. Contributor documentation and changelog remain outside this test-only slice, including the Low description finding.

Windows performed static diff review, manual formatting and diff-check only. No compiler, Gradle task, formatter or test ran here. Assertions remain unproven until the Linux receipt records the positive and negative legs. No gate receipt yet covers F. No public comment, PR edit, approval, merge or contributor-branch push was performed.
