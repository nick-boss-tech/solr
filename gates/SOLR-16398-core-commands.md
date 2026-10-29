# Gate job: SOLR-16398, JAX-RS core command APIs (split core, recovery, leader election)

- Branch: solr-16398-core-commands. Head: cf2bbb0558 (fork tip after the vm6 gate, previously d4a7840649c86f970381f8e647291635016923ac; verified by ls-remote 2026-10-28; one commit on origin/main 74197140a666). Base: 74197140a666d4f0396b68484c2bf9df7e79221e.
- Status: DONE. Gate PASS at cf2bbb0558 (tidy commit 099962c50f on d4a7840649, plus the route test). See `receipts/SOLR-16398-core-commands.md`.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Capability tags: gate.
- Why: the branch was implemented 2026-10-28 at the owner's direction with builds waived; this gate is its first verification. It converts the eight remaining annotated CoreAdmin v2 classes (SplitCoreAPI, PrepareCoreRecoveryAPI, RequestCoreRecoveryAPI, RequestSyncShardAPI, RequestBufferUpdatesAPI, RequestApplyCoreUpdatesAPI, OverseerOperationAPI, RejoinLeaderElectionAPI) to JAX-RS resources that delegate to CoreAdminHandler through a new CoreAdminAPIBase.invokeCoreAdminAction helper. CoreAdminHandler.getApis() becomes empty and the eight join getJerseyResources().
- Steps, in order:
  1. Changelog YAML parse (changelog/unreleased/SOLR-16398-core-commands.yml).
  2. Tidy, then confirm the tree is clean (a tidy-only diff gets the standard tidy commit and retarget, per the standing rule).
  3. Error Prone compile of :solr:api:compileJava, :solr:core:compileJava and :solr:core:compileTestJava.
  4. SolrJ generation: :solr:solrj:compileJava (exercises the API code generator on the new endpoints).
  5. Module check :solr:core:check -x test.
  6. Route check (added by vm6, 2026-10-29): CoreCommandsV2RoutesIntegrationTest, the eight converted paths must not return "Cannot find API for the path". Added because the listed steps run no tests.
- Notes for the runner: V2CoreAPIMappingTest and V2NodeAPIMappingTest are deleted by this branch on purpose (they covered only the converted APIs); their absence is not a failure. There is no pre-existing focused unit test for the new resources; the compile and module check legs are the verification, and any Jersey-resource registration failure will surface in the check leg's test runs.
- On green: write receipts/SOLR-16398-core-commands.md at the gated head, mark this job DONE. On failure: record the failing leg and the log paths in this file and in the receipt; do not fix code on this job (fixes return to the owner side as a new assignment).
