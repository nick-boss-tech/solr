# Pool assignment: SolrCloud READY drafts, batch A, review to SATISFIED

Capability tags: `review`. Intended host: Windows (review coordinator), under the standing coordinator claim. Staffing: one review agent per draft in parallel where the coordinator can staff it, rounds run per draft independently.

## Owner directive (recorded 2026-10-12)

The review agent applies the patches or wording changes its findings call for and keeps reviewing in further rounds until a SATISFIED verdict comes back on each item. A round that only lists findings without applying them does not discharge this assignment. The loop ends per draft only at SATISFIED, or at a finding the reviewer cannot settle under the rules below, which is then recorded as an owner item in the report with the reason.

## The batch

Batch A is the first half of the READY set in `reports/solrcloud-pr-audit-round-3.md` (9 READY), split in ticket-number order. Batch B (the rest of the READY set plus the two NOT READY drafts) is a separate assignment. The HOLD set is a separate assignment. Governing records: `reports/solrcloud-pr-audit-round-3.md`, `reports/solrcloud-decisions-round-5.md`, `reports/solrcloud-readiness-round-1-application.md`, `reports/solrcloud-application-round-2.md`, `material/solrcloud-round-1-answers.md`, `material/solrcloud-round-2-decisions.md`. Where a later record changes a round 3 verdict, the starting note below says so; the loop works from the live state, not from the round 3 table alone.

1. **SOLR-5813.** Draft: `pr-drafts/solrcloud/SOLR-5813.md`. READY in round 3; decisions round 5 moved it to HOLD because its Proof carries the takeover-log date 2026-10-07 and a dated run is owed (`gates/solrcloud-dated-runs-round-2.md`, item 1). Treat the dated record as part of this loop: the text review proceeds now. If the dated run has landed, verify the Proof against it and close. If it has not, the verdict line names that job as the remaining record item.
2. **SOLR-11479.** Draft: `pr-drafts/solrcloud/SOLR-11479.md`. READY in both rounds. The round 5 identity rewrite moved the head and the carry-over check is `gates/SOLR-11479-identity-carry.md`; verify the draft's links at the live head. Landing order is recorded: this PR opens only after SOLR-15035. SOLR-15035 is not in this batch (it sits in the HOLD batch assignment, and the readiness application already found it READY TO OPEN on its summary and its green receipt). The verdict line for this draft must carry the dependency: SATISFIED, READY TO OPEN after SOLR-15035.
3. **SOLR-12991.** Draft: `pr-drafts/solrcloud/SOLR-12991.md`. READY in round 3; decisions round 5 moved it to HOLD because its Proof carries the ledger date 2026-10-06 and a dated run is owed (`gates/solrcloud-dated-runs-round-2.md`, item 2). Same treatment as SOLR-5813.
4. **SOLR-13186.** Draft: `pr-drafts/solrcloud/SOLR-13186.md`. READY in both rounds. The dated focused run at `b436d90d2a` has landed. Verify the draft at the live head against the receipt and close.
5. **SOLR-15863.** Draft: `pr-drafts/solrcloud/SOLR-15863.md`. READY in both rounds. The changelog title re-gate is green at `78dff1d52f6c` and the receipt is refreshed. Verify and close.

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
- Branch edits are limited to patches for findings this review raises on these five branches, pushed by the patching agent as the loop rules state.
- Nothing is sent by email and no Jira text is written.

## Deliverable

`reports/solrcloud-ready-review-batch-a-round-1.md`, with one section per draft giving: rounds run, findings raised and how each was applied (wording edit, code patch with its gate job and receipt, decision recorded, gate job posted, or owner item), the final head reviewed with its gate receipt, and a verdict line per ticket: **SATISFIED** (reported as READY TO OPEN; for SOLR-11479, READY TO OPEN after SOLR-15035) or the unsettleable owner item, stated in one sentence. The assignment is discharged only when all five verdict lines exist.
