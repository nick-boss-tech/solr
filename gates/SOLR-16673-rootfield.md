# Gate job: SOLR-16673 nested root field type

Status: CLAIMED by vm1 2026-10-19T03:40Z (claims/pool-vm1-gate-solr-16673-rootfield.md; queued behind SOLR-18458 round 2). Main-side decision on the vm5 base-drift question: option (c) in effect; the vm1 runner restores gradle/libs.versions.toml after tidy (known base drift; tidy makes the same change on the merge-base), so step 1 judges the branch files only.

- Branch: `solr-16673-rootfield`. Head: `5fb792482fc3d8fef851513bb91eb3231409a033` (fork tip, confirmed by `git ls-remote` on 2026-10-18).
- Prior gated head: none. Merge-base with upstream/main: `e044bf20b405b8ca985730be3553d77d1fc8ff31`.
- Implementation base: fork main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Intended host: any capable Linux host under the WORKFLOW.md claim rules.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.designer.TestSchemaDesigner`. Seed: `16673B00B516673B`.
- Diff scope against fork main `498a09aea97aac86c2cc7ac114113b946d249942`: `changelog/unreleased/SOLR-16673-root-field.yml` (+7); `solr/core/src/java/org/apache/solr/handler/designer/SchemaDesigner.java` (+5, -4); `solr/core/src/java/org/apache/solr/handler/designer/SchemaDesignerConfigSetHelper.java` (+5, -1); `solr/core/src/test/org/apache/solr/handler/designer/TestSchemaDesigner.java` (+33). Anything else in the tree is out of scope.
- Why: when nested docs are enabled and `_root_` is absent, the base Schema Designer helper adds it as `string`. `IndexSchema.isUsableForChildDocs` requires `_root_` and the uniqueKey field to use the same field type. This branch creates `_root_` with the uniqueKey type, reloads the temporary collection before indexing, and adds a nested-doc regression test.
- Claim: not yet claimed. The Linux claimant must claim before running, then record the claimant host, standard gate runner, and log paths in this file and the claim.

## What to run

Use the host's standard full hardening gate runner. Follow the host file for Java and `GRADLE_USER_HOME`. Do not run the build tool directly.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-16673-root-field.yml`.
- Step 1: tidy. If tidy changes any file, mark GATE INVALID and stop. Leave the ticket branch unchanged.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: use merge-base `e044bf20b405b8ca985730be3553d77d1fc8ff31` production code and copy in only the branch's `TestSchemaDesigner.java`. Keep base `SchemaDesigner.java` and `SchemaDesignerConfigSetHelper.java`. Expected: `testAnalyzeNestedDocsWithNumericUniqueKey` fails because base code leaves `_root_` as `string` for the `plong` uniqueKey. Accept either of these direct assertions from that test: `response.updateError` contains the nested `_root_` and uniqueKey type mismatch, or the loaded `_root_` type assertion reports expected `plong` but actual `string` if live collection refresh masks the indexing error. Record the exact assertion and fresh XML counts. Any other failure shape stops the job.
- Step 4, focused head run: clean the test outputs first, then run `org.apache.solr.handler.designer.TestSchemaDesigner` at the branch head with seed `16673B00B516673B`. Count from fresh JUnit XML. Expected: all tests pass with 0 failures and 0 errors; record skipped count and total count.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route decision.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Branch tip moved at claim time | decided: stop | Record the new tip and stop. Do not retarget the job. | main agent |
| Tidy changes any file | decided: GATE INVALID | Record the tidy diff and stop. Do not commit the tidy change or continue the gate. | main agent |
| Proof leg failure shape | decided: nested root type mismatch only | If the test fails for another reason, or the mismatch is not present on base, record it and stop. Do not edit the test or rerun. | main agent |
| Focused head tests fail after execution | decided: record and stop | Record the failing test, failure text, seed, and XML counts. Do not relaunch. | main agent |
| Receipt update races the other SOLR-16673 gate | decided: serialize coordination pushes | Preserve all existing receipt sections. Fetch and rebase before pushing this section if the other gate has updated `receipts/SOLR-16673.md`. | main agent |
| Public text edits | decided: none | No PR opening, PR edit, PR comment, or Jira comment is part of this job. | Nick |

## On green

1. Add a new top section to `receipts/SOLR-16673.md` with this branch and head, gate date and host, log path, all step results, proof-leg test name and failure text, and focused counts from the fresh XML. Preserve the existing receipt for `solr-16673-submit` and any section added by the other new branch gate.
2. Do not push the ticket branch; its head is already the assignment head.
3. Mark this gate DONE and the claim DONE in the same fast-forward push to `coordination` as the receipt update.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, expected failure by content
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] gate DONE, claim DONE

## Deliverable and limits

- One fast-forward push to `coordination` with the receipt and DONE marks.
- The existing branch head remains unchanged by the gate.
- No PR opening or state change, no comment, no Jira or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.

## Result (vm5, 2026-10-19T00:55:43Z)

- Claimant: vm5. Runner: `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm5.lock`, JDK 21.0.12.1, worktree `/workspace/worktrees/vm5-gate-16673` at head 5fb792482fc3d8fef851513bb91eb3231409a033 (confirmed by `git ls-remote` on 2026-10-19).
- Step 0 changelog YAML parse: OK (`/workspace/gates/logs/vm5/16673-rootfield/step0.log`).
- Step 1 tidy: GATE INVALID. Tidy changed `gradle/libs.versions.toml` by removing `oshai-logging = "8.0.4"`. The gate stopped here as the job requires. Diff: `/workspace/gates/logs/vm5/16673-rootfield/tidy-diff.txt`. The branch was not changed and nothing was committed.
- Base check (diagnostic, not a gate step): tidy on the merge-base `e044bf20b405b8ca985730be3553d77d1fc8ff31` makes the same one-line removal (`/workspace/gates/logs/vm5/16673-base-tidy-check/tidy-diff.txt`). The line is unused by the build (its only other mentions are the renovate config and the changelog). So the drift is in the shared base, not in this branch.
- Steps 2 to 5 (Error Prone compile, proof leg, focused head run, module check): not run.
- Receipt: not written. A receipt is written only on green.

Decision needed (main agent, then Nick): how step 1 should treat the base drift. Options: (a) a tidy-only change on `main`, then re-check both heads; (b) a tidy-only commit on each branch, which moves both heads and requires a new gate; (c) record the base drift as known and exclude `gradle/libs.versions.toml` from step 1 for both gates. Recommended: (c) for now, since it changes no ticket code. Until one is chosen, both gates stay BLOCKED.
