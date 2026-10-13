# Pool assignment: strong review round 2, the unblocked slices

Capability tags: `review`. No build work. Claim under `claims/` before starting, per WORKFLOW.md.

## Context

`assignments/pool-four-new-prs-strong-review-round-2.md` waits on six gate inputs before its full round starts. The main side authorizes starting the slices below now; they do not depend on the outstanding gates. Read the round 2 assignment, the canonical round 1 report (`reports/four-new-prs-strong-review-round-1.md`), the Windows addendum (`reports/four-new-prs-strong-review-round-1-windows-addendum.md`), and the owner decisions recorded at the end of the round 2 assignment before touching text. The PR formula is in `pr-formula.md` on this branch. Voice rules for public text: no em dashes, never "we" for the owner plus agents, no internal process words (gate, receipt, ledger, seed values, run identifiers), citations to changed code link the head SHA and citations to pre-change behavior link the merge-base and say so.

## Standing patch-loop directive

Where a slice calls for a wording change, apply it and re-read the result; a slice that only lists wording changes is not discharged. Where a slice calls for prepared text or a prepared patch, the prepared artifact is the deliverable for that slice, named in the report.

## Slice A: SOLR-18541 body rewrite at the new head (start now)

The branch move is done: `combinedquerycomponent-merge-totals` now stands at `a93f9f6a997e748514cf2575a6e94c5f1c01f85e`, gated green at that head (CombinedQueryComponentMergeTotalsTest 10 of 10); the move and the gate are recorded in `receipts/SOLR-18541.md`. Rewrite the draft `pr-drafts/search-components/SOLR-18541.md` and the live body of PR #5117 to describe that head, per the round 1 lists:

- Exactness wording follows the patched rule at the new head (a skipped shard clears the merged exactness), replacing the sentence round 1 flagged as wrong.
- Proof counts and statements cite the new head's run (10 tests) and the fail-before recorded for the patch.
- Limits carry the Q2 missing-per-query-entry crash as a named follow-up, per owner decision 2 in the round 2 assignment: no follow-up ticket is filed until after the PR merges.
- The multi-query `numFoundExact` question stays posed as the PR's Choice, per owner decision 1 in the round 2 assignment. Do not add the reviewer's recommended change to any patch and do not soften the Choice.
- Every citation link re-points to the new head SHA; pre-change symptom citations stay at the merge-base and say so.

Edit the live body through the REST endpoint, read it back, and compare against the draft. Before editing, check the PR's comments and reviews; if a maintainer has commented since round 1, stop this slice and record it instead.

## Slice B: SOLR-18010 items that do not depend on the corrected gate (start now)

The corrected parent-baseline gate and the G3 record are still outstanding on vm2, so the SOLR-18010 verdict does not move in this assignment. These items are independent of that gate:

1. Finding B3 (the Choice is not posed fairly): prepare the rewritten Choice text as draft text in `pr-drafts/core-admin/SOLR-18010.md`, covering what round 1 named: the copy keeps mode bits only, not owner or group, so who can read the file can still change; the alternative's merit (a credentials file that should stay owner-only) stated plainly; the fail-the-edit option included as a live alternative. The permission-copy failure behavior follows owner item O1's recorded recommendation (warn and proceed) unless the gate record changes it; mark the prepared text as waiting on the verdict before it goes to the live body.
2. Item E13 (the changelog title's "or leave a partly written file" overclaim, finding B5): prepare the corrected fragment title as a patch file under `gates/` for a Linux host to commit on `solr-18010-submit`, with the replacement title stated verbatim and the YAML parse check named. No branch push from the review side on this slice.
3. Owner item O4 (the records gap): list precisely which logs `receipts/SOLR-18010.md` cites (`g18010-gate.log`, `g18010-premise.log`, `g18010-settle.log`), where each one actually is if it can be found on any branch or recorded host path, and which citations no reachable record supports. The list goes in the report, item by item; do not rewrite the receipt.

## Slices still waiting (named, not started)

- SOLR-18539: the branch move waits on the QueryComponent real-exit shape settling run; the patch branches follow whichever exactness rule the run supports. Do not move the branch and do not rewrite PR #5115's body in this assignment.
- SOLR-18010 verdict: waits on vm2's corrected parent-baseline gate at the route 1 head and on the G3 record (`gates/SOLR-18010-review-r1-g3.md`).
- SOLR-18541 score-only exit input: waits on its gate job if it has not recorded a result.

## Deliverable

`reports/strong-review-round-2-unblocked-slices.md`: per slice, what was applied (with the live body read-back result for slice A), the prepared artifacts for slice B with their file paths, the O4 list, and the waiting slices restated with the input each one waits on. Verdicts for the four PRs do not change in this assignment.
