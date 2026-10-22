# Gate job: SOLR-18563 (reopen the searcher on every replica after an additive schema change)

Status: OPEN

- Branch: `solr-18563-searcher-reopen`. Head: `0af85fe84729f83371950528006aeaec0ab2422f` (fork tip, confirmed by ls-remote on 2026-10-22). If the tip has moved at claim time, stop and record the new tip here. Do not retarget this job.
- Base: `7892348272727b8530d26ad08db734712ab2dacb12` (upstream main). Prior gated head: `8ef09875876fe25afdf052a2360df9b23cf18ca0` (the test-only branch; its focused run was recorded in gate job `SOLR-18563-receiving-replica-facet`). Merge-base with upstream/main: `7892348272727b8530d26ad08db734712ab2dacb12`.
- Gate ID: `SOLR-18563-searcher-reopen`. Claim file: `claims/pool-<host>-gate-SOLR-18563-searcher-reopen.md`.
- Intended host: vm2. Any capable Linux host with the gate capability may claim if vm2 is busy (WORKFLOW.md).
- Capability tags: gate.
- Module: `:solr:core`.
- Focused classes: `org.apache.solr.schema.TestManagedSchemaAPI` and `org.apache.solr.rest.schema.TestBulkSchemaAPI`. Focused method: `org.apache.solr.schema.TestManagedSchemaAPI.testFacetOnNewFieldBeforeCommitOnEveryReplica`. Seed: `C0FFEE18563A0002`.
- Changelog: `changelog/unreleased/SOLR-18563-additive-schema-api-no-reload.yml`.
- Diff scope against base: 7 files, 478 insertions and 2 deletions. The PR 5194 files (`SchemaManager.java`, `TestBulkSchemaAPI.java`, `TestManagedSchemaAPI.java`, `TestSchemaChangeDuringMerge.java`, `schema-api.adoc`, the changelog) plus the option 1 change: `ZkIndexSchemaReader.java` (+3) and one changed line in `SchemaManager.java`, and the every-replica test in `TestManagedSchemaAPI.java`. Against the test-only head `8ef0987587` the scope is 3 files, 14 insertions and 5 deletions. Anything else in the tree is out of scope.
- Why: PR 5194 makes add-field requests skip the core reload. A facet on the new field before a commit then fails on every replica. This branch reopens the searcher after an additive change on the node that took the request and on replicas that load the schema from ZooKeeper. The job must show that the every-replica test fails on the pre-change code and passes on this head, and that the existing tests in the two classes still pass.

## What to run

Use the host's standard gate runner. Check out the exact head first. Do not run the build tool directly.

- Step 0: parse `changelog/unreleased/SOLR-18563-additive-schema-api-no-reload.yml`.
- Step 1: run tidy. If tidy changes any file, apply the tidy output as one gate repair commit (WORKFLOW.md, Gate repairs), push it fast-forward to `solr-18563-searcher-reopen` from the gate head, record the patch and the new head here, and rerun steps 0 to 5 once on the repaired head.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: in the worktree at `8ef09875876fe25afdf052a2360df9b23cf18ca0`, overlay only `solr/core/src/test/org/apache/solr/schema/TestManagedSchemaAPI.java` from this head, and run `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.schema.TestManagedSchemaAPI.testFacetOnNewFieldBeforeCommitOnEveryReplica -Ptests.seed=C0FFEE18563A0002`. Expected: failure with `undefined field` in the message. A compile error stops the job.
- Step 4, focused head run: run `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.schema.TestManagedSchemaAPI --tests org.apache.solr.rest.schema.TestBulkSchemaAPI -Ptests.seed=C0FFEE18563A0002`. Count both classes from the fresh JUnit XML. Expected: every test passes, including `testFacetOnNewFieldBeforeCommitOnEveryReplica`.
- Step 5: run `:solr:core:check -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Step 3 fails with `undefined field` | decided: expected result | record it as the pre-change result. The test catches the regression | Nick |
| Step 3 passes | decided: stop and record | the test does not separate the two heads; record the output and stop | Nick |
| Step 3 fails with any other message or does not compile | decided: stop and record | record the output and stop; do not change the test | Nick |
| Step 4 fails in any test | decided: stop and record | record the test name and message and stop; do not relaunch and do not change code | Nick |
| Tidy changes a file | default: one repair commit | apply as in Step 1 and push fast-forward to the option branch | Nick |
| Flaky failure in a replica facet | decided: stop and record | if the every-replica test fails once and passes on rerun, record both results and stop; the job does not rerun on its own | Nick |

No row may leave the choice to the claimant.

## On green

Write the results into this file under a `## Result` heading: head, host and date, log paths, the step 3 result with the failure text, the step 4 counts from the fresh XML, and any repair commit. Then mark this job DONE and the claim DONE in the same push. Do not write a receipt for this job; receipts belong to PR-branch gates.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, failure at pre-change head by content
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] result written into this job
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the result section and the DONE marks, plus any repair commit (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
