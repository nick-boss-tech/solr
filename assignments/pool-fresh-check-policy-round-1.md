# Pool assignment: proposal for a fresh check before a draft is marked converged, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

Late revisions on PR drafts come mostly from drafts marked converged after one pass. This round writes a proposal for the main side to decide, so the review cost of a fresh check is weighed against the rework it saves.

## Task

1. Count, from the reports under `reports/` on this branch, how many drafts were marked converged in a round and then needed a patch or a fix in a later round. Use the round reports and the patch-round reports (for example `reports/moved-heads-draft-recheck-round-1.md`, `reports/solr-11129-draft-limits-touchup-round-1.md`, `reports/schema-recheck-14199-landing-round-1.md`). Give the count and the examples.
2. Count, for the same drafts, how many of the late fixes were caught by a fresh check and how many by the first pass.
3. Write the proposal: a one-paragraph standing rule for drafts (a fresh check before converged, scoped to receipt facts and head code), the expected review cost, and the expected saving. Name it as a proposal for the main side.

## Ladder

Information first: the counts above come from the record. A process change to WORKFLOW.md or the formula is not the review side's call, so the proposal goes to the main side and nothing is edited.

## Deliverable

`reports/fresh-check-policy-proposal-round-1.md`: the counts, the examples, the proposal text, and an explicit line that the decision belongs to the main side. No file other than the report changes.

Rules: read-only. No builds, no tests, no Gradle, no posting. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
