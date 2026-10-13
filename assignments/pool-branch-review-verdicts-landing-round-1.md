# Pool assignment: land the search-components and query-parsing review verdicts, round 1

Capability tags: `review`. No build work. Claim under `claims/` before starting, per WORKFLOW.md.

## Standing patch-loop directive

Where a verdict or a finding calls for a patch or a wording change, apply it: write the patch, push it, and keep reviewing until the item returns SATISFIED. A findings-only round does not discharge this assignment. Code patches are pushed by the claiming host and gated by a Linux host through a job under `gates/`; wording settlements go to both the draft under `pr-drafts/` and any live PR body the item owns, each edit verified by re-reading. Scope questions are decided under the decision default and the three-rung ladder (missing information: get more information; cost is effort: do the hard thing; optional: defer as a named follow-up), and each decision is recorded in the landing report in the same push.

## State of the two reviews

Two category reviews were run on their own assignment branches and are complete there. Their reports never landed on pr-prepare, so the pool cannot see the verdicts. This assignment lands them and then works them.

Search components: branch `assignment-search-components-review`, verdict tip `289d5d3ad5fe34b21852a4187657b03796bbc5e7`. The verdict reports on that branch are `reports/search-components-review-1.md`, `reports/search-components-review-2.md`, `reports/search-components-review-3.md`, `reports/search-components-review-4.md` and `reports/search-components-review-summary.md`. Scope: the 50 drafts under `pr-drafts/search-components/`, 48 with verdicts. The round 1 category records (`reports/search-components-1.md` to `-4.md`, 71 receipts) are already on pr-prepare and are not part of the landing.

Query parsing: branch `assignment-query-parsing-review`, verdict tip `03fbf23793f0ab23d46dce01709a3afec01a2fe6`. The verdict reports on that branch are `reports/query-parsing-review-q1.md`, `reports/query-parsing-review-q2.md`, `reports/query-parsing-review-q3.md`, `reports/query-parsing-review-q4.md` and `reports/query-parsing-review-summary.md`. Scope: the 18 drafts under `pr-drafts/query-parsing/`, within the 25 round 1 tickets recorded at pr-prepare commit `9bfcb2ff1c1`.

## Work

1. Land the reports. Bring the ten verdict files listed above onto pr-prepare under `reports/`, byte-identical to the branch versions, by cherry-pick or copy. Note the provenance in the landing report (source branch and tip for each set). Do not edit a landed report; disagreements or updates go in the landing report.
2. Carry the accounting forward. Each branch summary accounts for its full scope, including the receipts that have no draft of their own (search components: SOLR-16570, 8088, 6759, 8003, 10305, 8051, 9124, 9595, 17055). The landing report repeats that accounting and flags any gap it finds rather than filling it silently.
3. Work every NOT SATISFIED draft through the patch loop until it returns SATISFIED or lands on a named hold with the reason recorded (an owner item, a landing-order dependency, or a gate that has not run). Each patch round is recorded in the landing report with the branch head it produced.
4. For every READY or SATISFIED draft, verify the draft text against the live fork tip and the ticket's receipt in `receipts/` (a receipt at the exact live tip settles gate state; if the receipt and the tip disagree, believe the tip and record the disagreement). Verified drafts go onto a per-category ready list in the landing report: ticket, branch, live head, receipt head, and what still stands between the draft and an openings slate (packaging steps only; openings stay with the owner).
5. Exclusions from the original assignments stand. Search components: SOLR-18541 and the mergeIds fix (their own strong-review loop), SOLR-17372 (retire pending the owner's word), SOLR-16290 (held pin), SOLR-16155 (no PR of its own), SOLR-15319 (draft text only; it settles behind SOLR-8051's landing). Query parsing: SOLR-13202 (merged, skip), SOLR-17882 (premise dead, skip), SOLR-16130 (live PR; text review only, and edits to its live description stay with the main side). SOLR-11391 belongs to the query-parsing set. SOLR-6014's draft Limits must state the 2026-10-13 premise verdicts from its receipt.

## Deliverable

`reports/branch-review-verdicts-landing-round-1.md`: provenance for both landings, the accounting carried forward, one line per draft with its verdict and current state, the patch-loop record for each item worked, and the two per-category ready lists. The assignment is discharged when every in-scope draft reads SATISFIED, READY (on a ready list), or named hold with a reason.
