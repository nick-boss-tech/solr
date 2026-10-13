# Pool assignment: mergeIds PR draft text fixes, round 1

Capability tags: `review`. Intended host: windows (review coordinator). Staffing: 1 subagent. Claim: under the standing coordinator claim, `claims/coordinator-review-rounds.md`.

## Scope

Two reviews of the mergeIds PR draft returned NOT READY on the draft text only:

- `reports/mergeids-pr-short-review-round-1.md`
- `reports/mergeids-packaged-pr-review-round-1.md` (the packaged draft review; the 2026-10-12 21:13 queue pass recorded both reviews as NOT READY on PR text only, no new gate needed)

Apply their findings to `pr-drafts/search-components/mergeids-skipped-shard-totals.md`. If an item appears in both reviews, apply it once and note the overlap in the report. This assignment covers the draft text and nothing else.

## Ground truth

- Branch: mergeids-skipped-shard-totals on the fork, packaged head 4eb996121804b043f1ee53f5bb7e22b9c3021b0a (the changelog fragment commit on the gated head 9e85ce5c162). SOLR-18539.
- Receipt: `receipts/mergeids-fix.md` (gate green at the packaged head; proof leg 17 tests with 7 premise failures by content; focused QueryComponentPartialResultsTest 17 of 17).
- The draft at the pr-prepare tip already carries the packaged head and the ticket number; check its citations against 4eb9961218 when you edit.

## What to do (patch loop)

1. Read both reviews in full and list every finding.
2. Verify each finding against the branch at the packaged head and the receipt before applying it (verify-then-apply). Apply the fixes that hold, in the draft file. A finding that does not hold is rebutted in the report with the evidence, not silently dropped.
3. Confirmation pass in the same assignment: re-read the whole updated draft end to end against the PR formula on pr-prepare (sections in order, a bold one-line summary opening each section, citations as real blob links with change code at the PR head SHA 4eb9961218 and any pre-change symptom cited at the merge-base and labelled as such, the changelog line a link at the head SHA, plain language, no internal process vocabulary in the public text).

## Limits

- No branch changes. No new gate: the findings are text-only and the packaged head's gate stands on the receipt.
- If a finding turns out to need a branch change, do not make it. Record it in the report as a branch item for the main side, with the finding's source and what it would take.
- Do not edit the receipt, the reviews, or any other draft in this assignment.

## Deliverable

- The updated `pr-drafts/search-components/mergeids-skipped-shard-totals.md`, pushed to pr-prepare.
- `reports/mergeids-pr-draft-text-round-1.md`: every finding from both reviews, each with its disposition (applied as prescribed / applied with a change, saying what changed / rebutted, with evidence / branch item for the main side), and the final readiness verdict for opening: READY, or NOT READY with exactly what remains.
