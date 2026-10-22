# Gate job: SOLR-18563 (facet on a new field before commit, receiving replica)

Status: OPEN

- Branch: `solr-18563-receiving-replica-facet-test`. Head: `8ef09875876fe25afdf052a2360df9b23cf18ca0` (fork tip, confirmed by ls-remote on 2026-10-22). If the tip has moved at claim time, stop and record the new tip here. Do not retarget this job.
- Base: `7892348272727b8530d26ad08db734712ab2dacb12` (upstream main). Prior gated head: none. Merge-base with upstream/main: `7892348272727b8530d26ad08db734712ab2dacb12`.
- Gate ID: `SOLR-18563-receiving-replica-facet`. Claim file: `claims/pool-<host>-gate-SOLR-18563-receiving-replica-facet.md`.
- Intended host: vm2. Any capable Linux host with the gate capability may claim if vm2 is busy (WORKFLOW.md).
- Capability tags: gate.
- Module: `:solr:core`.
- Focused method: `org.apache.solr.schema.TestManagedSchemaAPI.testFacetOnNewFieldBeforeCommitOnReceivingReplica`. Seed: `C0FFEE18563A0001`.
- Changelog: `changelog/unreleased/SOLR-18563-additive-schema-api-no-reload.yml`.
- Diff scope against base: 6 files, 469 insertions and 2 deletions. The PR's files (`SchemaManager.java`, `TestBulkSchemaAPI.java`, `TestManagedSchemaAPI.java`, `TestSchemaChangeDuringMerge.java`, `schema-api.adoc`, the changelog) plus the one new test method (+22 lines in `TestManagedSchemaAPI.java`). The PR is apache/solr PR 5194, head `8b58872617`. Anything else in the tree is out of scope.
- Why: PR 5194 makes add-field requests skip the core reload. A facet on the new field before a commit then fails on the replica that took the request. The new test shows that on a 5-collection run against the receiving replica. The head is expected to fail it. This job records that result on the branch head and the base-code result for comparison. It is a record, not a pass.

## What to run

Use the host's standard gate runner. Check out the exact head first. Do not run the build tool directly.

- Step 0: parse `changelog/unreleased/SOLR-18563-additive-schema-api-no-reload.yml`.
- Step 1: run tidy. If tidy changes any file, apply the tidy output as one gate repair commit (WORKFLOW.md, Gate repairs), push it fast-forward to `solr-18563-receiving-replica-facet-test` from the gate head, record the patch and the new head here, and rerun steps 0 to 5 once on the repaired head.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: in a separate worktree at base `7892348272727b8530d26ad08db734712ab2dacb12`, overlay only `solr/core/src/test/org/apache/solr/schema/TestManagedSchemaAPI.java` from this head, and run the focused method with the seed. Expected: pass. A compile error or a failure stops the job.
- Step 4, focused head run: run `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.schema.TestManagedSchemaAPI.testFacetOnNewFieldBeforeCommitOnReceivingReplica -Ptests.seed=C0FFEE18563A0001`. Count the method from the fresh JUnit XML. Expected: failure with `undefined field` in the message.
- Step 5: run `:solr:core:check -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Step 4 failure with `undefined field` | decided: expected result | record it as the head result. Do not relaunch and do not treat it as RED | Nick |
| Step 4 passes | decided: stop and record | the head would then not show the regression; record the counts and stop for the owner | Nick |
| Step 4 fails with any other message | decided: stop and record | record the message and stop; do not change the test | Nick |
| Step 3 fails or does not compile | decided: stop and record | the base-code leg does not show what the job needs; record the output and stop | Nick |
| Tidy changes a file | default: one repair commit | apply as in Step 1 and push fast-forward to the test branch | Nick |

No row may leave the choice to the claimant.

## On green

This job does not produce a receipt, because its expected result is a failure at head. When the job ends, write the results into this file under a `## Result` heading: head, host and date, the log paths, the step 3 and step 4 results, the counts from the fresh XML, and any repair commit. Then mark this job DONE and the claim DONE in the same push.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, pass at base code
- [ ] step 4 focused head run, counted from fresh XML, failure by content
- [ ] step 5 module check
- [ ] result written into this job
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the result section and the DONE marks, plus any repair commit (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
