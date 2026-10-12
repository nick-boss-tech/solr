# Pool assignment: fresh re-check of SOLR-13265, round 1

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

`pr-drafts/update-processing/SOLR-13265.md` was marked CONVERGED in `reports/update-processing-convergence-round-3.md`, and it changed after that verdict (commit `4b95c02224f`). No round has re-checked it. Under the fresh-check standing rule proposed in `reports/fresh-check-policy-proposal-round-1.md`, a draft that changed after its verdict needs a fresh check before it is treated as converged.

## Checks

1. Every head SHA and link in the draft names the head the draft's receipt records, and every link path exists at its commit.
2. Every line-range anchor covers the sentence it supports, at the commit the link names.
3. Every count, test name, date and run claim matches the receipt's newest section. Dates must be recorded dates.
4. Every behaviour claim is in the receipt or follows from the code at the named head.
5. Plain language, no internal process words, no em dash, no PR numbers, the AI header and footer present.

## Rules

- Read-only on the draft in this round. A mechanical fix (head SHA, count, date, changelog target) is applied in the same round and named in the report. Anything that needs judgment is a finding with exact replacement text.
- Decisions follow the standing ladder: information first, then the effort route, then a named Limits follow-up for optional items.
- No builds, no tests, no Gradle, no posting.

## Deliverable

`reports/recheck-13265-round-1.md`: verdict first, one line per check with PASS or FAIL, each FAIL with its exact replacement, and the ladder rung for any judgment. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
