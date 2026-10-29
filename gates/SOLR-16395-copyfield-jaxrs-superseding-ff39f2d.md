# Gate job: SOLR-16395, copy-field JAX-RS endpoints, superseding job at head ff39f2d

- Supersedes: `gates/SOLR-16395-copyfield-jaxrs-superseding-fb0c16b.md`.
- Branch: `solr-16395-copyfield-jaxrs` on `nick-boss-tech/solr`. Proof head: `ff39f2dab67e8ae4aa4494a4c1059bef35a5a165`, one commit above `fb0c16b70a7601a50236427b7576cee2e925c768`.
- Change in the new commit: one line in `V2UpdateSchemaCopyFieldsTest`, a `SolrTestCaseJ4.newRandomConfig()` call after `startSolr`, which the embedded server needs before creating the collection. No production code changed.
- Base: `74197140a666d4f0396b68484c2bf9df7e79221e`.
- Status: CLAIMED (vm4, 2026-10-29T04:07Z)
- Intended host: any capable Linux host. Capability tags: gate.
- Steps, in order: changelog YAML parse; tidy with clean-tree check; Error Prone compile of `:solr:api:compileJava :solr:core:compileJava :solr:core:compileTestJava`; `:solr:solrj:compileJava`; focused tests `V2UpdateSchemaErrorCaseTests`, `V2UpdateSchemaCopyFieldsTest` and `GetSchemaAPITest` after `cleanTest`; `:solr:core:check -x test`; final tidy and clean-tree check.
- On green: write `receipts/SOLR-16395.md` at the new head and mark this job DONE. On failure: record the failing leg and log paths here and in the receipt; do not fix code on this job.
