# Review: CombinedQueryComponent Jira ticket draft, round 5 confirmation

Verdict: **STILL OPEN.** The five round 4 settlements are confirmed. The round 3 carry-through rule, that no general real-exit claim in the ticket text stays unlabelled, is not met yet: four clauses in the draft still need the label or a scope change. The round can close as SATISFIED once they are applied.

Scope: the five items of `assignments/pool-combinedquerycomponent-ticket-review-round5.md`, plus the carry-through sweep of the ticket text, checked against `material/combinedquerycomponent-ticket-draft.md` and `gates/combinedquerycomponent-real-exit-premise.md` at `81d479760dd`. Two read-only subagents did the work: one confirmed the five settlements, and one swept the whole text independently. Nothing was edited, run or posted.

## One line per item

1. **A1, draft line 50 label: CONFIRMED.** The Partial skip bullet reads "...and it is not the shape a real {{timeAllowed}} exit produces (from code reading, not run; see Symptom)."
2. **A2, draft line 56 label: CONFIRMED.** The End to end paragraph opens "End to end, no traced shard path produces the constructed shape (from code reading, not run)."
3. **A3, draft line 20 label: CONFIRMED.** "...under a score-only sort an exit cannot stop in sort-value preparation and stops in the search itself (from code reading, not run),..."
4. **A4, draft line 56 label: CONFIRMED.** "...under a score-only sort the exit cannot stop in sort-value preparation, so it stops in the search itself with the same search handler products (from code reading, not run);..."
5. **B1, gate job line 31: CONFIRMED.** The comma splice is gone, now ". The expected outcome", and the rest of the line matches round 3.

Carry-through: **NOT MET.** See findings R5-1 to R5-4 below.

## Findings

### R5-1. Draft line 20, the later-query clause (should-fix)

"...and an exit on a later query leaves that shard's per-query list shorter than the query count." This is a code-reading claim about a real exit and carries no label. Replacement: replace that clause with "...and an exit on a later query leaves that shard's per-query list shorter than the query count (from code reading, not run)."

### R5-2. Draft line 56, the later-query clause (should-fix)

"...and an exit on a later query leaves the shard's per-query list shorter than the query count, where the merge reads it with no size check." Replacement: "...and an exit on a later query leaves the shard's per-query list shorter than the query count (from code reading, not run), where the merge reads it with no size check."

### R5-3. Draft line 24, the partial header (should-fix)

"The response header does carry {{partialResults=true}} when a shard is partial, ..." describes what a real partial exit sets, which is a code-reading result. The probe responses were fabricated, so no run covers the header. Replacement: add " (from code reading, not run)" before the final period of that sentence.

### R5-4. Draft line 38, the skipped shard's contribution (should-fix)

The sentence beginning "So a skipped shard has already contributed its count, its exactness and its approximate count" is stated for every skipped shard. A real query-0 exit has no per-query entry, so nothing is contributed in that case. Replacement for that sentence: "So a skipped shard that carries a per-query entry for the query has already contributed its count, its exactness and its approximate count to the per-query totals before the {{continue}} fires (from code reading; confirmed by run only for constructed responses; see Reproduction)."

## Notes (not holding the round)

- **R5-N1, draft line 20, first sentence (note).** The condition "flagged partial and carries no sort_values for that query" is also met by a real query-0 exit, which has no per-query entry, so the consequence stated after it does not hold for that exit. Optional replacement: "is flagged partial, carries a per-query entry for that query but no sort_values for that query, and the sort needs unmarshalling," with the sentence ending "(confirmed by run only for constructed responses; see Reproduction)."
- **R5-N2, gate job wording (note; the gate job is not the ticket text).** Lines 13, 15 and 17 to 23 state real-exit and SearchHandler products as fact, and the header "verified by the main side" can read as a run. Optional: "## Reading evidence for the spec (from code reading at d76fc14b9857, not run)", with "The code reading says that" before the Q2 and Q3 statements on lines 13 and 15, and "On the code reading," before "no throw occurs earlier" on line 31.
- **R5-N3, alignment (note).** Gate job line 7 says the draft "claims neither outcome this job settles", but draft lines 20 and 56 state the query-0 Q1 outcome under the code-reading label. The two wordings should agree.

## Not checked

- The gate job's Q1 to Q3 outcomes, which are settled only by the Linux run.
- The master receipts ledger, the takeover log and security material.

## Convergence line

STILL OPEN. Four draft clauses (R5-1 to R5-4) need the label or the scope change. Once they are applied, this round can close as SATISFIED.
