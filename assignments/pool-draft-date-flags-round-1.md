# Pool assignment: verification-date flags in the draft-fidelity family, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent per two drafts, at most three in parallel. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

The draft-fidelity fixes left a verification-date item unchanged by decision. Two drafts still carry dates that the receipts do not record: `pr-drafts/core-admin/SOLR-12007.md` says "Verified 2026-10-08", and `pr-drafts/core-admin/SOLR-13246.md` says "checked 2026-10-06". The policy proposal notes these as still open. This round settles them, and sweeps the other converged drafts for the same kind of date.

## Checks

1. For each draft named above, find the receipt's newest run section for the draft's head. Quote the recorded run date.
2. If the draft's date differs from the recorded run date, the mechanical fix is to change the date to the recorded run date. If the receipt records no run date for the sentence, the fix is to remove the date, so the sentence states the counts and the head only.
3. Sweep the other drafts marked CONVERGED on this branch for "verified" or "checked" dates (grep `pr-drafts/` for `verified 20` and `checked 20`), and check each against its receipt. Report every mismatch.

## Ladder

Information first: the receipt is the source for every date. Where no receipt date exists, the effort route is to remove the date rather than invent one. A date is never taken from a takeover log or a claim heartbeat.

## Rules

- Mechanical date edits only. Change nothing else in the draft.
- No builds, no tests, no Gradle, no posting.

## Deliverable

`reports/draft-date-flags-round-1.md`: for each draft, the draft date, the receipt's recorded date, the verdict and the exact before and after text. Apply the edits in the same push and say so. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
