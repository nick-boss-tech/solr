# Gate job: SOLR-16655 (selected fields beneath unselected child labels)

Status: CLAIMED (claims/phone-agent-gate-SOLR-16655-phase-selected-descendants.md)

- Branch: `solr-16655-submit`. Head: `6c5c11aaf5a4c0b27daec2b4ab39a9139e95ef1c` (fork and PR head confirmed on 2026-10-23). If the tip moves before claim or execution, record it and stop.
- Prior gated head and patch parent: `5e2317443f4116a9e97330b0d6ce175cf44b54df`. Merge-base with upstream/main: `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.
- Gate ID: `SOLR-16655-phase-selected-descendants`.
- Intended host: existing vm3 claimant for claim reconciliation; after settlement, any capable Linux gate host under WORKFLOW.md. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.update.processor.FieldMutatingUpdateProcessorTest`, `org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest`, `org.apache.solr.update.TestNestedUpdateProcessor`, and `org.apache.solr.MinimalSchemaTest`. Seed: `16655C0FFEE16655`.
- Patch delta against prior head: 6 files, +93/-23. Whole branch against merge base: 7 files, +681/-25.
- Why: owner direction on 2026-10-23 was to patch PR #5093 and hand off the gate assignment. The reviewed head gated traversal of labelled children on selection of their parent field. The patch traverses surviving child values independently of the parent label, while the selector still chooses each document's fields to mutate. Updated tests cover explicit selectors through two labels, unselected scalar preservation, shared child instances, and field removal. A new JSON update/index/query test covers labelled and anonymous children using an explicit date selector and the ticket's unzoned date. The old receipt does not cover this head.

Links: Ticket or parent: SOLR-16655 / https://github.com/apache/solr/pull/5093. Category: Update processing and atomic updates. Siblings: SOLR-12705 changes the same shared base; its combined tree remains out of scope. Supersedes: proof applicability of `5e2317443f4116a9e97330b0d6ce175cf44b54df` for this branch; does not rewrite `gates/SOLR-16655-review-pass-1.md` or its claim.

## Claim prerequisite

At handoff, `claims/pool-newdev-critblock-review-round-1-solr-16655.md` remains CLAIMED by vm3 with last recorded heartbeat 2026-10-20T15:54:54Z. Its gate is `gates/SOLR-16655-review-pass-1.md`, pinned to the old head. Its recorded log directory is `/workspace/gates/logs/vm3/solr-16655-review-pass-1/` and runner is `/workspace/tools/gate-solr-16655-p1.sh` under flock.

This is a handoff, not a second runnable gate or a claim takeover. The claim holder should finalize or end its old-head work with the actual result and note that the branch advanced by owner direction. A different host must follow the stale-gate takeover rules, including independently checking that the recorded log stopped growing; a timestamp alone is insufficient. Windows has not inspected that log and has not declared the claim stale. Preserve any old-head result as old-head evidence.

Once the ticket-level claim is terminal and no conflicting claim exists, the authorized Linux host sets this job OPEN and then CLAIMED with its own phase-specific claim in the normal push sequence. Record the settlement and new claim path here before running. Do not move the head in the existing claimed gate. The request-gate helper was not used because its live-ticket-claim prerequisite is currently unsatisfied.

## Diff scope

Only these paths may differ from the merge base:

- `changelog/unreleased/SOLR-16655-field-mutating-urps-nested-docs.yml`
- `solr/core/src/java/org/apache/solr/update/processor/FieldMutatingUpdateProcessor.java`
- `solr/core/src/test/org/apache/solr/update/processor/FieldMutatingUpdateProcessorTest.java`
- `solr/core/src/test/org/apache/solr/update/processor/ParsingFieldUpdateProcessorsTest.java`
- `solr/core/src/test/org/apache/solr/update/TestNestedUpdateProcessor.java`
- `solr/core/src/test-files/solr/collection1/conf/schema-nest.xml`
- `solr/core/src/test-files/solr/collection1/conf/solrconfig-minimal.xml`

The patch changes six of these; ParsingFieldUpdateProcessorsTest is unchanged from the previous head but remains part of the branch and focused regression scope. The old divergent Windows worktree was preserved; the implementation was made in a fresh worktree of the PR head. No local testing handoff files enter the PR.

## What to run

Use the host's standard gate runner and lock. Do not run the build tool directly. Check out the exact head above and verify the fork tip and scope first. No Gradle, Java tests, or formatting ran on Windows.

- Step 0: parse `changelog/unreleased/SOLR-16655-field-mutating-urps-nested-docs.yml`; confirm `type: changed`, author Nick Shanin, and no `nick:` line.
- Step 1: tidy. Apply and record any mechanical output, commit and push it fast-forward, record the resulting exact head and tree, and continue under WORKFLOW.md Gate repairs. A tidy change is not proof for the pre-tidy commit; the final receipt must identify the tested pushed head.
- Step 2: Error Prone compile using `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`. Record whether tasks actually execute or are up-to-date; use clean compile tasks if the host gate requires them.
- Step 3, fail-before proof: retain all head tests and fixtures, but replace only `FieldMutatingUpdateProcessor.java` with that path from patch parent `5e2317443f4116a9e97330b0d6ce175cf44b54df`. Clean test outputs and run only `org.apache.solr.update.processor.FieldMutatingUpdateProcessorTest.testTrimFieldsRecurseIntoUnselectedParentFields` and `org.apache.solr.update.TestNestedUpdateProcessor.testParseSelectedDatesInNestedDocuments` at the seed above. Expected: both fail. The first must fail because the child's selected `name` remains padded under `child_doc`; the second must fail while indexing the labelled child's unparsed `date_dt=2015-10-02T14:52:37`, with the invalid-date error. A compile failure, setup error, absent test, or different failure is not proof. Record method names, exact messages and fresh XML counts. Restore only the production file to the pushed head and confirm the tree is clean before step 4. This is a production-path reversion proof, not an unchanged whole-base run.
- Step 4, focused head run: run `:solr:core:cleanTest`, then `:solr:core:test --tests org.apache.solr.update.processor.FieldMutatingUpdateProcessorTest --tests org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest --tests org.apache.solr.update.TestNestedUpdateProcessor --tests org.apache.solr.MinimalSchemaTest -Ptests.seed=16655C0FFEE16655`. Count each class from fresh JUnit XML immediately after the run. FieldMutatingUpdateProcessorTest now includes `testTrimFieldsRecurseIntoUnselectedParentFields`, `testMutatingProcessorsVisitSharedNestedDocumentsOnce`, and `testRemovedChildFieldIsNotTraversed`. The nested indexing method exercises both labelled and anonymous representations and queries all three stored dates plus unchanged scalar values. MinimalSchemaTest checks compatibility of the shared minimal config fixture. No invented or skipped-test counts.
- Step 5: module check `:solr:core:check -x test`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Parent-label selector semantics | decided: selection limits mutation within each visited document | Traverse surviving labelled and anonymous children regardless of their parent label. Do not restore the old selector gate or change tests to expect untouched selected descendants. | Nick Shanin, patch direction 2026-10-23 |
| Existing ticket-level gate claim | decided: settle before execution | Preserve the old job head and result. Follow Claim prerequisite; no concurrent run and no timestamp-only stale takeover. | Standing workflow |
| Tidy changes a scoped file | decided: mechanical repair | Record, commit, push, and gate the actual final head under WORKFLOW.md Gate repairs. | Standing workflow |
| Compile, changelog, lint, or up to two failing head tests | decided: bounded repair and one retry | Record diagnosis and repair commit; rerun from step 0 on the pushed repair head. Do not rerun unchanged code. Do not change the selected-descendant assertions to make the gate pass. | Standing workflow |
| Failure outside repair scope or after retry | decided: hand back | Set HANDED BACK; record task/test, error, seed, fresh XML counts, both attempts and any repairs. No silent scope reduction. | Standing workflow |
| Cluster fails before assertions | default: same command once more | If it fails again before assertions, record the first exception and stop as harness failure. | Standing workflow |
| Proof differs from expected content | decided: record and stop | Restore the exact head and record the failure; do not weaken tests or claim proof. | Review acceptance bar |
| Fork tip moved | decided: stop | Record both heads and hand back unless the move is this gate's own recorded mechanical repair. | Standing workflow |
| Public PR text, comments, or JIRA | decided: no writes in this job | The current body describes old parent-gated traversal and old proof. Record updated proof and proposed corrections in the receipt; live body edits or comments require separate owner authorization. | Workspace instructions |

## On green

1. Prepend a section to `receipts/SOLR-16655.md` naming this gate ID, exact pushed and tested head, UTC run dates, host, log paths, all step results, proof method/error/count details, focused counts and skips, and any repair attempt. Keep old-head receipts as history.
2. No public writes. The branch remains Implemented, Proof Pending until the exact final head is green. A gate on the former head is not current proof.
3. Mark this job and the phase claim DONE in the same push as the receipt. Record how the older ticket-level claim was settled; do not edit a terminal claim.

## Checklist

- [ ] old ticket-level claim settled and phase claim recorded
- [ ] fork and PR head confirmed
- [ ] seven-path branch scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean or repaired and pushed
- [ ] step 2 Error Prone compile
- [ ] step 3 both proof failures confirmed by content; production restored
- [ ] step 4 four classes counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written for final exact head
- [ ] job and claim DONE

## Deliverable and limits

Receipt plus this job's actual run record and DONE marks on coordination, with any scoped gate repair pushed to the fork. No PR opening or state change, public comment, PR-body edit, JIRA or email, force push, or SOLR-12705 integration. Commit as Nick Shanin with no attribution trailers. No em dashes or en dashes.

## Settlement and claim

- 2026-10-23T18:21:25Z, phone-agent: the claim prerequisite above is settled. claims/pool-newdev-critblock-review-round-1-solr-16655.md was taken over under WORKFLOW.md Claims rule 4 (last heartbeat 2026-10-20T15:54:54Z; the owner confirms vm3 died many hours ago; the rule 4 gate-log check could not be performed from vm1 because the log is on vm3's disk, recorded in the takeover entry) and marked ENDED with the old-head evidence preserved. gates/SOLR-16655-review-pass-1.md is ENDED with no recorded result.
- This job is CLAIMED by phone-agent: claims/phone-agent-gate-SOLR-16655-phase-selected-descendants.md.

## Gate run 1 and bounded repair

- Run 1 at 6c5c11aaf5a4c0b27daec2b4ab39a9139e95ef1c (phone-agent, vm1; log gate-16655-phase.log under the host's gate-logs-2026-10-23): steps 0 to 3 passed (changelog parse; tidy clean; Error Prone compile; proof: both named methods failed on patch-parent production with the specified content). A runtime restart killed the run inside step 4; steps 4 and 5 were resumed in the same log. Step 4 passed: FieldMutatingUpdateProcessorTest 37, ParsingFieldUpdateProcessorsTest 44, TestNestedUpdateProcessor 9, MinimalSchemaTest 3, 0 failures. Step 5 failed at :solr:core:forbiddenApisTest: String#formatted (default locale) at TestNestedUpdateProcessor.java:138, a lint failure in the patch's new test.
- Repair (2026-10-23T18:52:00Z): TestNestedUpdateProcessor now uses String.format(Locale.ROOT, ...) with the text block unchanged; commit 9596593aa808254842b7fa37e469a23f48ecfa46, pushed fast-forward. This is the bounded lint repair in the Decisions table; the gate reruns from step 0 on the repair head.

## Repair 2 (defective repair correction)

- The retry-1 gate at 9596593aa808254842b7fa37e469a23f48ecfa46 failed at step 1 (tidy): the repair commit itself was defective. Splitting the text block into a template variable dropped the semicolon after the closing delimiter, so spotless reported a parse error at TestNestedUpdateProcessor.java:138 (';' expected). The branch's intended code never ran in retry 1; the failure was in the repair, recorded here rather than counted as a gate verdict on the patch.
- Correction: commit ea300ef237c8d2869f9e40ddc890878a9d233aac adds the semicolon (one line), pushed fast-forward. The gate reruns in full from step 0 on ea300ef237c.
