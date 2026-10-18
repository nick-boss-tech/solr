# Pool assignment: SOLR-16673 remaining Schema Designer issues, round 1

Status: OPEN

- Capability tags: implementation, gate, review, draft.
- Intended host: any pool Linux host. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: main agent (vm1), 2026-10-18, at Nick's direction.

Context: PR #5094 (SOLR-16673) fixes only the first of three issues Houston Putman named in his comment on the ticket: the empty-string NPE in the numeric parse processors. The other two were recorded as out of scope and nothing has been done for them since. The research note is `pipeline/research-notes/SOLR-16673.md` in the main side's goal workspace; the facts needed are repeated here so this assignment is self-contained. The receipt is `receipts/SOLR-16673.md` on this branch.

The two remaining issues, in Houston's words as digested in the research note:

- Issue 2: `SchemaDesignerAPI` (about line 1213 in 9.1.1) assumes the uniqueKey field is a string field. The reporter's unique field was long/int.
- Issue 3: after the first issues are fixed, a `"_root_"` / uniqueKey fieldType mismatch error appears on child docs. Houston did not explain or resolve it.

Start from: `solr/core/src/java/org/apache/solr/handler/designer/` (SchemaDesignerAPI, DefaultSchemaSuggester) at the pinned head named in the claim, and the ticket thread.

## Items

1. **Issue 2 premise.** Read the uniqueKey handling in the Schema Designer analysis path at the pinned head. Establish whether analyzing documents whose uniqueKey field is a numeric type still fails, by a reproduction (a test or a driven analysis call) or by a decisive code path argument naming the failing line and the cast or assumption. Output: `reports/SOLR-16673-remaining-issues.md`, section "Issue 2 premise", with the evidence either way.
2. **Issue 2 fix, only if the premise confirms.** Implement the smallest fix on a new branch `solr-16673-uniquekey` off the fork's current main, with a test that fails without the fix. Gate it under the full hardening gate (focused class named in the report; the gate host writes the receipt). Write the PR draft under `pr-drafts/` in the usual formula. Do not open the PR. If the premise does not confirm, record the evidence and stop this item.
3. **Issue 3 premise and mechanism.** Construct the child-doc analysis case (documents with `_root_` and nested children, uniqueKey as in the ticket) and establish what fires: which check compares which field types, and whether the mismatch error is a defect or a legitimate validation result for the input. Output: the same report, section "Issue 3", with the mechanism in plain words and the evidence.
4. **Issue 3 fix, only if the mechanism shows a defect.** Same shape as item 2, on branch `solr-16673-rootfield`, gated, with a PR draft. If the behavior is legitimate, the deliverable for this item is the mechanism write-up plus a draft ticket comment for Nick (draft only, in the report), and the item stops there.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Scope | decided: the two issues above only | Do not touch the parse processors (`ParseLongFieldUpdateProcessorFactory`, `ParseDoubleFieldUpdateProcessorFactory`); that is PR #5094's scope. If a fix seems to need those files, stop and report to the main agent | main agent |
| New ticket | decided: none | No new Jira ticket is filed for either issue. Anything ticket-shaped is a draft for Nick in the report | Nick |
| PR opening | decided: none | Branches are prepared and gated; drafts are written; openings stay with Nick | Nick |
| Premise fails | default: record and stop | A premise that does not confirm closes its item with the evidence recorded; do not broaden the search into a redesign of the Schema Designer | main agent |
| Fix shape | default: smallest change that removes the failure | A broader redesign goes to the report as a named option with its cost, not into the branch | main agent |

## Deliverable

`reports/SOLR-16673-remaining-issues.md`: one section per item with its status, the evidence, and, for any fix, the branch, head, gate result and draft path. Then mark this assignment's status line `Status: DONE` with the date, or record the blocker here.

## Limits

- No PR opening, no Jira comment posted, no comment on PR #5094. Drafts only.
- Push fix branches to the fork as plain fast-forward commits. Never force push.
- Commit as Nick Shanin with no trailers. No em dashes in public text.
