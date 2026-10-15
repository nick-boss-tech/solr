# Pool assignment: review the four draft PRs opened 2026-10-12 until each returns SATISFIED

Status: DONE

Capability tags: `review`. Intended host: Windows (review coordinator), under the standing coordinator claim. Staffing: one review agent per PR in parallel where the coordinator can staff it, rounds run per PR independently.

## Owner directive (recorded 2026-10-12)

The review agent applies the patches or wording changes its findings call for and keeps reviewing in further rounds until a SATISFIED verdict comes back on each PR. A round that only lists findings without applying them does not discharge this assignment. The loop ends per PR only at SATISFIED, or at a finding the reviewer cannot settle under the rules below, which is then recorded as an owner item in the report with the reason.

## The four PRs

Each PR gets its own verdict line in the final report.

1. **SOLR-18539, mergeIds skipped-shard totals.** Draft PR #5115. Branch `mergeids-skipped-shard-totals` at `4eb996121804b043f1ee53f5bb7e22b9c3021b0a`. Draft: `pr-drafts/search-components/mergeids-skipped-shard-totals.md`. Receipt: `receipts/mergeids-fix.md`. Its pre-opening text round already returned READY; this round reviews the live PR as opened, body and branch, not only the draft file.
2. **SOLR-18540, gradlew under Git Bash.** Draft PR #5116. Branch `gradlew-msys-double-conversion` at `58e07b4571fbbfc83c14bdd28283cd573acb0d7a`. Draft: `pr-drafts/build-docs/SOLR-18540.md`. Receipt: `receipts/SOLR-18540.md`, which includes the Linux run record for the patched script.
3. **SOLR-18541, CombinedQueryComponent merge totals.** Draft PR #5117. Branch `combinedquerycomponent-merge-totals` at `d5ed34125db9582b584bbfdd149a4de4b40650d0`. Draft: `pr-drafts/search-components/SOLR-18541.md`. Receipt: `receipts/SOLR-18541.md`. This fix was written by a main-side lane and has had no independent code review; this round reviews the code, not only the text.
4. **SOLR-18010, standalone security.json edits.** Draft PR #5118. Branch `solr-18010-submit` at `c3685bb37d9d74e4ed538d6d1b65bb1b60970677`. Draft: `pr-drafts/core-admin/SOLR-18010.md`. Receipt: `receipts/SOLR-18010.md`. Note: an offer comment pointing to this draft is posted on the other contributor's PR #5065 and its answer is outstanding. Review proceeds normally, but SATISFIED on this PR does NOT flip it ready; that decision returns to the owner.

## How the loop runs

- **Wording findings** are applied by the reviewer in the same round, in both places: the live PR body, via the GitHub API under the standing description-edit authorization, with each edit verified by re-reading the live body; and the matching pr-prepare draft file, committed and pushed with the round's report.
- **Code findings:** the reviewer writes the patch on the branch in its own worktree and pushes it, then posts a gate job under `gates/` naming the patched head, the base, the module, the focused test classes and the seed the gate must run, following the existing gate job shape. Windows runs no builds; a Linux host claims and gates it. The review continues once the gate receipt for the patched head lands. Ungated code is never the reviewed head: a round's verdict is always stated against a head with a gate receipt at that exact head.
- **Rounds repeat per PR** until that PR returns SATISFIED. A confirmation round after applied fixes checks only that the fixes are in and hold; it does not restart the sweep from zero unless a fix changed the area under review.
- **Scope questions** arising inside a finding are decided under the decision default and the three-rung ladder (get more information; where the cost is effort, do the hard thing; where the item is optional, defer it as a named follow-up in the PR's Limits), and each decision is recorded in the report. An item that survives all three rungs is recorded as an owner item and does not block SATISFIED on the rest if the PR text names it as a follow-up.
- **Voice and text rules** apply to every edit: no em dashes; authored text never uses "we" for the owner plus agent (write as "I" or neutrally); public text carries no internal process vocabulary; titles must stay accurate; citations to changed code point at the head SHA and citations to the pre-change symptom point at the merge-base, saying so.

## Verdicts and what they release

- For PRs #5115, #5116 and #5117, a SATISFIED verdict is reported as **READY TO FLIP**. The main side flips the PR to ready under the owner's standing instruction; the reviewer does not flip it.
- For PR #5118 (SOLR-18010), the verdict is reported **without a flip**, whatever it is, because of the outstanding answer on PR #5065.

## Rules

- No comments are posted by the review side on any PR. Anything that needs a comment is drafted in the report for the owner to approve.
- No PR is opened, closed, or flipped by this assignment.
- Branch edits are limited to patches for findings this review raises on these four branches, pushed by the patching agent as the loop rules state.
- If a live PR head has moved past the head named above when a round starts, the round reviews the live head and says so; it does not review a stale head.

## Deliverable

`reports/new-prs-review-to-satisfied-round-1.md`, with one section per PR giving: rounds run, findings raised and how each was applied (wording edit, code patch with its gate job and receipt, decision recorded, or owner item), the final head reviewed with its gate receipt, and the verdict line (SATISFIED reported as READY TO FLIP for the first three; SATISFIED or otherwise reported without a flip for SOLR-18010). The assignment is discharged only when all four verdict lines exist.
