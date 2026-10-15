# Pool assignment: SolrCloud READY drafts, batch B, plus the two NOT READY drafts, review to SATISFIED

Status: DONE

Capability tags: `review`. Intended host: Windows (review coordinator), under the standing coordinator claim. Staffing: one review agent per draft in parallel where the coordinator can staff it, rounds run per draft independently.

## Owner directive (recorded 2026-10-12)

The review agent applies the patches or wording changes its findings call for and keeps reviewing in further rounds until a SATISFIED verdict comes back on each item. A round that only lists findings without applying them does not discharge this assignment. The loop ends per draft only at SATISFIED, or at a finding the reviewer cannot settle under the rules below, which is then recorded as an owner item in the report with the reason.

## The batch

Batch B is the second half of the READY set in `reports/solrcloud-pr-audit-round-3.md` (9 READY), split in ticket-number order, plus the two NOT READY drafts from the same report, whose recorded findings are the starting list. Batch A and the HOLD set are separate assignments. Governing records: `reports/solrcloud-pr-audit-round-3.md`, `reports/solrcloud-decisions-round-5.md`, `reports/solrcloud-readiness-round-1-application.md`, `reports/solrcloud-application-round-2.md`, `material/solrcloud-round-1-answers.md`, `material/solrcloud-round-2-decisions.md`. Where a later record changes a round 3 verdict, the starting note below says so; the loop works from the live state, not from the round 3 table alone.

### READY drafts

1. **SOLR-16013.** Draft: `pr-drafts/solrcloud/SOLR-16013.md`. READY in both rounds. The history rewrite is executed and re-gated green at `408317d7e241`; round 3 already applied its one Proof fix. Verify the draft at the live head against the receipt and close.
2. **SOLR-17292.** Draft: `pr-drafts/solrcloud/SOLR-17292.md`. READY in both rounds. The remedy re-gate is green at `f614a42fbc8`. Verify and close.
3. **SOLR-17680.** Draft: `pr-drafts/solrcloud/SOLR-17680.md`. READY in round 3; decisions round 5 moved it to HOLD on the vm2 two-router test (`gates/SOLR-17680-v2-two-router-test.md`). The text is final, and the live head is tree-identical to the earlier gated head. If the test record has landed, fold its result into the Proof and Limits and close. If it has not, the verdict line names that job as the remaining record item.
4. **SOLR-17733.** Draft: `pr-drafts/solrcloud/SOLR-17733.md`. READY in both rounds. A dated run at `636196b7954` is recorded. Verify the draft at the live head against the receipt and close.

### NOT READY drafts (starting findings from round 3)

5. **SOLR-14919.** Draft: `pr-drafts/solrcloud/SOLR-14919.md`. Round 3 findings: the receipt covers `84e7bcaeb57`; the draft's links are at `c1fa37e33c1`; the live tip is `8f1f4ef91e79` after the tidy fix; the gate at that tip stopped at the focused-head leg and was not relaunched (`gates/SOLR-14919-processor-only-gate.md`). The processor-only route is adopted (`material/solrcloud-round-2-decisions.md`): it fixes forwarded commits, and the replication failure the ticket reports is not fixed by it; the draft's Limits say so, and the follow-up ticket text sits at `material/solr-14919-replication-ticket-draft.md`, unfiled. Loop: verify the draft end to end at the live tip, re-point any citation the receipt supports, apply wording fixes under the patch loop. The gate at the live tip is the record path; the reviewer does not relaunch a gate that executed tests, and the verdict line names the gate job if the record is still owed.
6. **SOLR-9155.** Draft: `pr-drafts/solrcloud/SOLR-9155.md`. Round 3 findings: a re-gate is owed at the live head (`gates/SOLR-9155-logging-regate.md`); the counts in the draft stay at `dbd88c84ff2` until it lands; the visible length is about 3,880 characters with the run bracket, and about 380 characters come out once the counts land. The declared route (`getLeader` declares `InterruptedException`) and the restored base ERROR line are adopted (`material/solrcloud-round-2-decisions.md`). Loop: if the re-gate record has landed, apply the counts and the length cut and close. If it has not, complete every text item that does not depend on the counts, and the verdict line names the gate job.

## How the loop runs

- **Verify first.** Each round starts from the live fork head (`git ls-remote`) and the receipt in `receipts/` at that exact head. A receipt at the exact live tip settles gate state. Where the draft and the receipt disagree, the receipt wins and the draft is patched to match. If a live head has moved past the head any record names, review the live head and say so.
- **Gate records.** A draft whose receipt is not at its live tip gets a gate job posted under `gates/` naming the exact run owed (branch, head, base, module, test classes, seed), following the existing gate job shape. A posted gate job does not block the text review; the verdict line names it as the remaining record item until its receipt lands. Windows runs no builds; a Linux host claims and runs gate jobs.
- **Wording findings** are applied by the reviewer in the same round to the pr-prepare draft file, committed and pushed with the round's report.
- **Code findings:** the reviewer writes the patch on the branch in its own worktree and pushes it, then posts a gate job under `gates/` for the patched head. The review continues once the gate receipt for the patched head lands. Ungated code is never the reviewed head: a verdict is always stated against a head with a gate receipt at that exact head.
- **Rounds repeat per draft** until that draft returns SATISFIED. A confirmation round after applied fixes checks only that the fixes are in and hold; it does not restart the sweep from zero unless a fix changed the area under review.
- **Scope questions** are decided under the decision default and the three-rung ladder (get more information; where the cost is effort, do the hard thing; where the item is optional, defer it as a named follow-up in the draft's Limits), and each decision is recorded where the category records live (`material/solrcloud-round-1-answers.md`, `material/solrcloud-round-2-decisions.md`) or in the round report. An item that survives all three rungs is recorded as an owner item.
- **Voice and text rules** apply to every edit: no em dashes; authored text never uses "we" for the owner plus agent (write as "I" or neutrally); public PR text carries no internal process vocabulary; titles must stay accurate; citations to changed code point at the head SHA and citations to the pre-change symptom point at the merge-base, saying so; every section of a draft keeps its bold one-line summary.

## Rules

- No PR is opened, closed, or flipped by this assignment, and no comment is posted anywhere. SATISFIED drafts are reported as READY TO OPEN; openings stay with the owner.
- Branch edits are limited to patches for findings this review raises on these six branches, pushed by the patching agent as the loop rules state.
- Nothing is sent by email and no Jira text is written.

## Deliverable

`reports/solrcloud-ready-review-batch-b-round-1.md`, with one section per draft giving: rounds run, findings raised and how each was applied (wording edit, code patch with its gate job and receipt, decision recorded, gate job posted, or owner item), the final head reviewed with its gate receipt, and a verdict line per ticket: **SATISFIED** (reported as READY TO OPEN) or the unsettleable owner item, stated in one sentence. The assignment is discharged only when all six verdict lines exist.
