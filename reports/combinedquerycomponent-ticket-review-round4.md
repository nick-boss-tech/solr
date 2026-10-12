# Review: CombinedQueryComponent Jira ticket draft, round 4 confirmation

Verdict: **STILL OPEN.** Items 2 and 3 are confirmed. Item 1 is open: two general real-exit sentences in the draft are not labelled, and round 3's condition says every general real-exit claim must carry the label.

Scope: the three items of `assignments/pool-combinedquerycomponent-ticket-review-round4.md`, checked against the round 3 record and the settlement, the draft `material/combinedquerycomponent-ticket-draft.md`, the gate job `gates/combinedquerycomponent-real-exit-premise.md`, and the code at `d76fc14b9857`. Two read-only subagents checked the items independently: one covered all three items, and one cross-checked items 2 and 3 from the code. Their results agree. Nothing was edited, run or posted.

Not checked from this host: the probe source `CombinedQueryComponentMergeTotalsTest.java`, which is absent at `d76fc14b9857`. It is in the VM1 worktree, and the run log and JUnit XML are there too. The M2 value is checked against the addendum and the code, not against the probe assertion.

## One line per item

1. **Finding A, the general real-exit sentences: STILL OPEN.** The first sentence of Symptom bullet 1 (line 20) carries the prescribed label. Two general real-exit claims are unlabelled (lines 50 and 56). See findings A1 and A2.
2. **Finding B, the gate job's Q3 expected outcome: CONFIRMED.** The gate job line 31 contains the prescribed sentence, and the code matches it at `d76fc14b9857`: `docs` is declared null at line 335 and assigned only at line 341 under the guard at line 338; `sortFieldValues` defaults to an empty list at lines 394 to 396; `needsUnmarshalling` is set at line 398 and gates the skip at lines 399 to 400; the loop at line 411 calls `docs.size()` with no guard. The cross-check adds that the prediction holds when the exiting shard's header carries `partialResults=true`, which the exit path sets. One wording fault is left in line 31: a comma splice before "The expected outcome". See finding B1.
3. **Item 3, the M2 merged `numFound`: CONFIRMED, with the probe source unchecked.** The addendum's values (`reports/combinedquerycomponent-premise.md`, lines 156 and 157) match the draft's M1 and M2 statements. The merged `numFound` is the maximum of the per-query `numFound` values (lines 441 and 442). For M2 that is max(5, 2) = 5, and for M1 it is max(5, 11) = 11. The M2 `numFound` of 5 is inferred from the addendum rather than stated in it. The probe assertion at line 362 cannot be read on this host.

## Findings

### A1. Draft line 50, Reproduction (blocking)

The sentence reads "...and it is not the shape a real {{timeAllowed}} exit produces (see Symptom)." It is a general real-exit claim, and the cross-reference does not label it. Replacement: "...and it is not the shape a real {{timeAllowed}} exit produces (from code reading, not run; see Symptom)."

### A2. Draft line 56, End to end paragraph (blocking)

The sentence "End to end, no traced shard path produces the constructed shape." is a general claim without the label. Replacement: "End to end, no traced shard path produces the constructed shape (from code reading, not run)."

### A3. Draft line 20, Symptom bullet 1 (should-fix)

The sentence about the score-only sort is an exit claim from code reading and carries no label. Replacement: replace "under a score-only sort an exit cannot stop in sort-value preparation and stops in the search itself," with "under a score-only sort an exit cannot stop in sort-value preparation and stops in the search itself (from code reading, not run),".

### A4. Draft line 56, End to end paragraph (should-fix)

The same unlabelled score-only claim in the paragraph. Replacement: replace "under a score-only sort the exit cannot stop in sort-value preparation, so it stops in the search itself with the same search handler products;" with "under a score-only sort the exit cannot stop in sort-value preparation, so it stops in the search itself with the same search handler products (from code reading, not run);".

### B1. Gate job, line 31 (should-fix; wording only)

The round 3 replacement left a comma splice: "...gives no per-query entry and no skip, The expected outcome under...". Replacement: change ", The expected outcome" to ". The expected outcome".

### B2. Gate job, line 31 (note; optional)

The prediction holds only when the exiting shard's header carries `partialResults=true`, and the exit path sets it. Optional sentence to state that and the reason nothing throws earlier: "The expected outcome under the code reading for Q3 is a NullPointerException at line 411, assuming the exiting shard's header carries partialResults=true (SearchHandler sets it on this path): docs is null, since there is no per-query entry and nothing assigns it at line 341, and the loop calls docs.size() with no guard. No throw occurs earlier: line 399 cannot dereference null because sortFieldValues defaults to an empty list at lines 394 to 396, and the skip does not apply because needsUnmarshalling is false."

### C1. Draft Fix options, the M2 clause (note)

The M1 clause says "approximate totals" and the M2 clause says "per-query totals" for the same kind of value. Replacement: "per-query approximate totals of 7 and 3". Optional, for parallel wording.

## Not checked

- The probe source, the probe's assertions at lines 362 and 363 to 368, and the probe's fabricated per-query values for M2 (VM1 worktree).
- Whether the null pointer exception propagates out of `mergeIds`, since the caller was not traced.
- The master receipts ledger, the takeover log and security material.

## Convergence line

STILL OPEN. Finding A1 and A2 (blocking) and A3 and A4 (should-fix) are open against the draft; B1 is a should-fix in the gate job. The main side applies the replacements, after which this round can close as SATISFIED.
