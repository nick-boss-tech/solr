# Gate job: SOLR-16398, JAX-RS collection command APIs (split-shard, move-replica, rebalance-leaders, migrate-docs)

- Branch: solr-16398-command-jaxrs. Head: 5cc3fc4c0019a84a2875c15920664dbe93a9ee4d (fork tip, verified by ls-remote 2026-10-28; one commit on origin/main 74197140a666). Base: 74197140a666d4f0396b68484c2bf9df7e79221e.
- Status: DONE (steps 1 to 6 pass at 5cc3fc4c plus repair commit 7fa61aa; four command resources not registered, see `receipts/SOLR-16398.md`).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Capability tags: gate.
- Why: the branch was implemented 2026-10-28 at the owner's direction with builds waived; this gate is its first verification. It replaces the annotated SplitShardAPI, MoveReplicaAPI, RebalanceLeadersAPI, and MigrateDocsAPI with JAX-RS resources that build the same overseer messages the v1 translations produced and submit them through AdminAPIBase. Behavior risk concentrates in the message keys: split.key, target.collection, forward.timeout, and the property.* flattening of split-shard coreProperties.
- Steps, in order:
  1. Changelog YAML parse (changelog/unreleased/SOLR-16398.yml).
  2. Tidy, then confirm the tree is clean (a tidy-only diff gets the standard tidy commit and retarget, per the standing rule).
  3. Error Prone compile of :solr:api:compileJava, :solr:core:compileJava and :solr:core:compileTestJava.
  4. SolrJ generation: :solr:solrj:compileJava (exercises the API code generator on the new endpoints).
  5. Focused tests at the head: org.apache.solr.handler.admin.api.V2CollectionAPIMappingTest (retained coverage, ModifyCollection only). Count from fresh JUnit XML after cleanTest. Note: V2ShardsAPIMappingTest is deleted by this branch on purpose; its absence is not a failure.
  6. Module check :solr:core:check -x test.
- On green: write receipts/SOLR-16398.md at the gated head, mark this job DONE. On failure: record the failing leg and the log paths in this file and in the receipt; do not fix code on this job (fixes return to the owner side as a new assignment).
