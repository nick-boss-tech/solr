# Gate job: SOLR-16395, JAX-RS schema copy-field endpoints, superseding job at head f0fb1d1

- Supersedes: `gates/SOLR-16395-copyfield-jaxrs.md` for the proof. That job's head `425ec1e402` is red (`receipts/SOLR-16395.md`).
- Branch: `solr-16395-copyfield-jaxrs` on `nick-boss-tech/solr`. Proof head: `f0fb1d15b17c7475c97f4fc0b92b05f29af06bd4`, one commit above `425ec1e402c0f133a83465d2273c9b00607e8fbc`.
- Change in the new commit: `UpdateSchema.addCopyField` error text is `Missing required parameter: dest`, without quotes, matching `V2UpdateSchemaErrorCaseTests.testAddCopyFieldOperationRequiresSourceAndDestinations`. No other file changed.
- Base: `74197140a666d4f0396b68484c2bf9df7e79221e`.
- Status: DONE (vm4, 2026-10-29T02:00Z). Green at `f0fb1d15b1`.
- Intended host: any capable Linux host. Capability tags: gate.
- Steps, in order, same as the superseded job: changelog YAML parse; tidy with clean-tree check; Error Prone compile of `:solr:api:compileJava :solr:core:compileJava :solr:core:compileTestJava`; `:solr:solrj:compileJava`; focused tests `V2UpdateSchemaErrorCaseTests` and `GetSchemaAPITest` after `cleanTest`; `:solr:core:check -x test`; final tidy and clean-tree check.
- On green: write `receipts/SOLR-16395.md` at the new head and mark this job DONE. On failure: record the failing leg and log paths here and in the receipt; do not fix code on this job.
