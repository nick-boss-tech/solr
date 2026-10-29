# Gate job: SOLR-16395, copy-field JAX-RS endpoints, superseding job at head 0b9ecd0

- Supersedes: `gates/SOLR-16395-copyfield-jaxrs-superseding-f0fb1d1.md` for the proof. That job is green at `f0fb1d15b1`, and this job adds a test-only commit on top of it.
- Branch: `solr-16395-copyfield-jaxrs` on `nick-boss-tech/solr`. Proof head: `0b9ecd02ce57e90ce3d6bc0b19bb7500b21bd430`, one commit above `f0fb1d15b17c7475c97f4fc0b92b05f29af06bd4`.
- Change in the new commit: new test `solr/core/src/test/org/apache/solr/handler/admin/api/V2UpdateSchemaCopyFieldsTest.java`. Two cases on a real managed schema: `deleteCopyFields` on a source with no copy fields is a no-op; `deleteCopyFields` on `copyfield_source` removes its copy fields. No production code changed.
- Base: `74197140a666d4f0396b68484c2bf9df7e79221e`.
- Status: CLAIMED (vm4, 2026-10-29T02:53Z)
- Intended host: any capable Linux host. Capability tags: gate.
- Steps, in order: changelog YAML parse; tidy with clean-tree check; Error Prone compile of `:solr:api:compileJava :solr:core:compileJava :solr:core:compileTestJava`; `:solr:solrj:compileJava`; focused tests `V2UpdateSchemaErrorCaseTests`, `V2UpdateSchemaCopyFieldsTest` and `GetSchemaAPITest` after `cleanTest`; `:solr:core:check -x test`; final tidy and clean-tree check.
- On green: write `receipts/SOLR-16395.md` at the new head and mark this job DONE. On failure: record the failing leg and log paths here and in the receipt; do not fix code on this job.
