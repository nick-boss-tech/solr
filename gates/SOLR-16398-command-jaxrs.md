# Gate job: SOLR-16398, JAX-RS collection command APIs (split-shard, move-replica, rebalance-leaders, migrate-docs)

- Branch: solr-16398-command-jaxrs. Head: 1a8ef0a44b779e0573914c3368ef7fb1f66a9299 (fork fast-forward from 5cc3fc4c0019a84a2875c15920664dbe93a9ee4d, the round 1 head). Base: 74197140a666d4f0396b68484c2bf9df7e79221e.
- Status: DONE. Round 2 PASS at 1a8ef0a (`receipts/SOLR-16398.md`). Round 1 at 5cc3fc4 passed its listed steps but did not exercise the four command resources; that gap is fixed in round 2, which the owner assigned on 2026-10-29 ("we need to try to complete 16398 work even if it takes some implementation").
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Capability tags: gate.
- Why: the branch was implemented 2026-10-28 at the owner's direction with builds waived. It replaces the annotated SplitShardAPI, MoveReplicaAPI, RebalanceLeadersAPI and MigrateDocsAPI with JAX-RS resources that build the same overseer messages the v1 translations produced and submit them through AdminAPIBase. Round 2 registers the resources and runs rebalance-leaders in-process, as the v1 action does.
- Steps, in order:
  1. Changelog YAML parse (changelog/unreleased/SOLR-16398.yml). Passed in round 1; the file is unchanged.
  2. Tidy, then confirm the tree is clean.
  3. Error Prone compile of :solr:api:compileJava, :solr:core:compileJava and :solr:core:compileTestJava.
  4. SolrJ generation: :solr:solrj:compileJava (exercises the API code generator on the new endpoints).
  5. Focused tests at the head: org.apache.solr.handler.admin.api.* (includes CollectionCommandsV2IntegrationTest, the end-to-end V2 tests for the four commands, and V2CollectionAPIMappingTest), org.apache.solr.handler.admin.TestCollectionAPIs, org.apache.solr.handler.V2ApiIntegrationTest. Count from fresh JUnit XML after cleanTest. Note: V2ShardsAPIMappingTest is deleted by this branch on purpose; its absence is not a failure.
  6. Module check: :solr:core:check -x test.
- On green: write receipts/SOLR-16398.md at the gated head, mark this job DONE. On failure: record the failing leg and the log paths in this file and in the receipt; do not fix code on this job (fixes return to the owner side as a new assignment).
