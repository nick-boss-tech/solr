# Pool assignment: SCH-R1-16 in the schema lane, round 1

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

The schema round 2 record (`reports/schema-convergence-round-2.md`, line 34) lists SCH-R1-16 as the one round 1 note still open. Its content has never been checked in the reviews since. It is the last open item in the schema lane, so it needs a verdict.

## Task

1. Find SCH-R1-16 in the schema round 1 register (`reports/schema-convergence-round-1.md`, and the register or response that defines it). Quote its text.
2. Check the claim it makes against the live text of the schema drafts it touches (the drafts under `pr-drafts/schema-analysis/` and `pr-drafts/schema-*` as the register names them) and against the branch code at the head the draft names.
3. Give a verdict: CLOSED (already correct in the live text), CLOSED WITH A TEXT CHANGE (give the exact replacement), or DEFERRED (a named Limits follow-up with a stated plan).

## Ladder

Get the information from the record and the code first. Where the fix is effort, do it. Where the item is optional, defer it as a named Limits follow-up. A recorded recommendation is adopted as the decision.

## Deliverable

`reports/schema-scr1-16-round-1.md`: the register text, the check made, the verdict, the exact replacement if any, and the ladder rung used. If a text change is needed, apply it to the draft in the same push and say so in the report; no other edits.

Rules: read-only except for the one replacement, if any. No builds, no tests, no Gradle, no posting. WORKFLOW.md binds (claim before work, hourly heartbeats, mark the claim DONE in the same push as the deliverable).
