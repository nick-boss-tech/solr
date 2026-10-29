# Gate job: SOLR-16395, JAX-RS schema copy-field endpoints

- Branch: solr-16395-copyfield-jaxrs. Head: 425ec1e402c (fork tip, verified by ls-remote 2026-10-28; two commits on origin/main 74197140a666). Base: 74197140a666d4f0396b68484c2bf9df7e79221e.
- Status: OPEN
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Capability tags: gate.
- Why: the branch was implemented 2026-10-28 at the owner's direction with builds waived; this gate is its first verification. It adds POST /schema/copyfields/{sourceField} and DELETE /schema/copyfields/{sourceField}/{destField} and /{sourceField} to UpdateSchemaApi and UpdateSchema, addressing copy-fields by their source and destination fields (resolving the TODO in UpdateSchemaApi), plus error-case tests and a changelog fragment.
- Steps, in order:
  1. Changelog YAML parse (changelog/unreleased/SOLR-16395.yml).
  2. Tidy, then confirm the tree is clean (a tidy-only diff gets the standard tidy commit and retarget, per the standing rule).
  3. Error Prone compile of :solr:api:compileJava, :solr:core:compileJava and :solr:core:compileTestJava.
  4. SolrJ generation: :solr:solrj:compileJava (exercises the API code generator on the new endpoints).
  5. Focused tests at the head: org.apache.solr.handler.admin.api.V2UpdateSchemaErrorCaseTests (contains the new copy-field validation tests) and org.apache.solr.handler.admin.api.GetSchemaAPITest as a neighbor. Count from fresh JUnit XML after cleanTest.
  6. Module check :solr:core:check -x test.
- On green: write receipts/SOLR-16395.md at the gated head, mark this job DONE. On failure: record the failing leg and the log paths in this file and in the receipt; do not fix code on this job (fixes return to the owner side as a new assignment).
