# Gate job: SOLR-16395, copy-field JAX-RS endpoints, superseding job at head fb0c16b

- Supersedes: `gates/SOLR-16395-copyfield-jaxrs-superseding-0b9ecd0.md`, which is RED because the test commit `0b9ecd0` fails the focused and lint legs.
- Branch: `solr-16395-copyfield-jaxrs` on `nick-boss-tech/solr`. Proof head: `fb0c16b70a7601a50236427b7576cee2e925c768`, one commit above `0b9ecd02ce57e90ce3d6bc0b19bb7500b21bd430`.
- Change in the new commit: `V2UpdateSchemaCopyFieldsTest` rewritten to extend `SolrTestCase` (the repo's `AGENTS.md` rule for new suites), start an embedded core from a temp copy of the test home, and drop the static assert imports that ecj flagged. No production code changed.
- Base: `74197140a666d4f0396b68484c2bf9df7e79221e`.
- Status: CLAIMED (vm4, 2026-10-29T03:42Z)
- Intended host: any capable Linux host. Capability tags: gate.
- Steps, in order: changelog YAML parse; tidy with clean-tree check; Error Prone compile of `:solr:api:compileJava :solr:core:compileJava :solr:core:compileTestJava`; `:solr:solrj:compileJava`; focused tests `V2UpdateSchemaErrorCaseTests`, `V2UpdateSchemaCopyFieldsTest` and `GetSchemaAPITest` after `cleanTest`; `:solr:core:check -x test`; final tidy and clean-tree check.
- On green: write `receipts/SOLR-16395.md` at the new head and mark this job DONE. On failure: record the failing leg and log paths here and in the receipt; do not fix code on this job.
