# Gate job: SOLR-16396, JAX-RS config modification APIs

- Branch: solr-16396-config-jaxrs. Head: 6f1577221646dec1dd4ada8e409faa552d4f2913 (fork tip, verified by ls-remote 2026-10-28; one commit on origin/main 74197140a666). Base: 74197140a666d4f0396b68484c2bf9df7e79221e.
- Status: DONE (receipt receipts/SOLR-16396.md, gated head f8ba76d)
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Capability tags: gate.
- Why: the branch was implemented 2026-10-28 at the owner's direction with builds waived; this gate is its first verification, and it is a behavior-bearing refactor: ModifyConfigComponentAPI becomes a JAX-RS resource implementing the new ModifyConfigApi interface, ModifyParamSetAPI is deleted, and SolrConfigHandler gains a processConfigCommands entry point plus an 'upsert' command prefix. The v1 request path (handleRequestBody) is refactored to share the new runner and must behave identically.
- Steps, in order:
  1. Changelog YAML parse (changelog/unreleased/SOLR-16396.yml).
  2. Tidy, then confirm the tree is clean (a tidy-only diff gets the standard tidy commit and retarget, per the standing rule).
  3. Error Prone compile of :solr:api:compileJava, :solr:core:compileJava and :solr:core:compileTestJava.
  4. SolrJ generation: :solr:solrj:compileJava (exercises the API code generator on the new endpoints).
  5. Focused tests at the head: org.apache.solr.handler.admin.V2ConfigAPIMappingTest (trimmed by this branch to the still-annotated GetConfigAPI) and org.apache.solr.core.TestConfigOverlay. Count from fresh JUnit XML after cleanTest.
  6. Module check :solr:core:check -x test.
- Watch item for the runner: the v1 config-edit path is exercised end to end only by heavier suites; if a config-edit integration test class is identified on the host (SolrConfigHandler command tests), run it as an extra leg and record the result separately.
- On green: write receipts/SOLR-16396.md at the gated head, mark this job DONE. On failure: record the failing leg and the log paths in this file and in the receipt; do not fix code on this job (fixes return to the owner side as a new assignment).
