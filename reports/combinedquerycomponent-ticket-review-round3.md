# Review: CombinedQueryComponent Jira ticket draft, round 3 confirmation

Verdict: **STILL OPEN.** Items 1, 2 and 5 are confirmed. Item 4 has one unlabelled general claim on line 20. Item 6 has a gate job whose expected outcome for Q3 is not stated. Item 3 has one value, M2's merged `numFound`, that this host cannot check, because the probe assertion is in the VM1 worktree.

Scope: the six settlements of `assignments/pool-combinedquerycomponent-ticket-review-round3.md`, read against the draft at `345dfdfd988`, the main-side adjudication in `reports/combinedquerycomponent-ticket-review-round2.md`, the run addendum in `reports/combinedquerycomponent-premise.md` (lines 145 to 175), the gate job `gates/combinedquerycomponent-real-exit-premise.md`, and the code at `d76fc14b9857`. No draft, gate, report or record was edited. Nothing was run.

## One line per item

1. **Real-exit sentences scoped (Symptom bullet 1, line 20): CONFIRMED.** The scoped shape matches the code. At `CombinedQueryComponent.java`, the per-query `doFieldSortValues` call at 210 sits inside the loop at 183 to 221, so an exit there skips the `sort_values_<q>` add at 213 and the per-query entry at 215 to 219. The skip at 399 needs `needsUnmarshalling`. The header value at 386 is added in the block at 381 to 387, which runs after the `if` at 338 closes at 344, so it runs for every shard response. The two unsettled shapes are stated as unsettled, and no outcome is claimed for them. The only finding here is the unlabelled general sentence under item 4.
2. **End-to-end paragraph (line 56) and the two precision minors: CONFIRMED.** The paragraph scopes the real-exit claim to the index-0, unmarshalling shape. It names the score-only exit, which stops in the search at line 186 (`super.process`), and the later-query exit, which leaves the per-query list short with no size check, as unsettled. The skip sentence now requires `needsUnmarshalling`, and the fold sentence is limited to merges where a shard sends a per-query entry. The folds at 341 to 343 sit inside the guard at 338, so the limit is correct.
3. **M1 and M2 values (Fix options and Reproduction): PARTLY CONFIRMED, one value NOT CHECKED.**
   - M1 values confirmed against the addendum, lines 156 and 157: per-query approximate totals 7 and 21 (base), merged `approximateTotalHits` 21, merged `numFound` 11.
   - M2 merged `approximateTotalHits` 7 confirmed against the addendum, line 157.
   - M2 merged `numFound` 5: **NOT CHECKED.** The addendum does not state it. The assertion is in `maximumFoldOtherQueryWinning`, which exists only in the VM1 worktree (`~/workspace/solr-worktrees/cqc-premise`). The consistency check passes only if q0's `numFound` is 5, which the addendum's "q0 as in M1" implies but does not state. Read the assertion on VM1 before this item is closed.
   - The Reproduction sentence names M1 and M2 as "whose outcomes are stated with the open question in Fix options", and that matches.
4. **Verification-level wording: FINDING, one sentence.** Every real-exit product in the ticket is either in the scoped shape with its label or stated as not settled, except the first sentence of line 20. See finding A. The causal phrase at old line 54 is absent. The only "which is why" left is the arrival-order sentence in Mechanism (line 40), whose cause is stated in that passage.
5. **Exactness scoped to one sub-query: CONFIRMED.** Symptom bullet 3 (line 22) claims the inverted fold only "where a single sub-query contributes". Fix option 3 is scoped the same way, and the sentence after the options says "an exact single-sub-query merge". The inversion matches the code at line 343: `Boolean.FALSE.equals(docs.getNumFoundExact())` keeps the flag true only while shards report inexact. The open question states that sub-queries "match their own documents, overlapping or not", and that the maximum undercounts "whenever the sub-queries do not all match the same documents". No passage presumes the answer.
6. **Premise job extension (Q3): FINDING, one sentence.** The extension is in the job: Q3 at line 15, the reading at line 23, the third Step 1 run at line 28, and the checklist item at line 42. The line numbers match the code: line 186 is `super.process`, 399 to 400 is the skip, 411 is the `docs.size()` loop, and `QueryComponent` 520 is the SCORE and DOC continue. The expected-outcome line (line 31) does not state a prediction for Q3. It says "Q3 decides whether line 411 throws", but the code reading predicts a throw. See finding B.

## Findings

### A. Line 20, first sentence (items 4 and 1)

The sentence "The real {{timeAllowed}} exit is not the shape that produces this." is a general claim about every real exit. It rests on the same code reading, and it is not labelled.

Replace "The real {{timeAllowed}} exit is not the shape that produces this." with:

> The real {{timeAllowed}} exit is not the shape that produces this (from code reading, not run).

### B. Gate job, expected-outcome line for Q3 (item 6)

`gates/combinedquerycomponent-real-exit-premise.md` line 31 ends "and Q3 decides whether line 411 throws on the null docs." The code reading predicts a throw, so the expected outcome should say so before the run. The reasons are:

- `docs` is declared null at line 335 for each shard response. Only the per-query guard at 338 assigns it, at 341. With no per-query entry, it stays null.
- The skip at 399 does not throw first. `sortFieldValues` defaults to an empty list at lines 394 to 396, and `needsUnmarshalling` is false for a score-only sort, so the skip does not apply.
- The loop at 411 calls `docs.size()` with no guard.

Replace "and Q3 decides whether line 411 throws on the null docs." with:

> The expected outcome under the code reading for Q3 is a NullPointerException at line 411: `docs` is null, since there is no per-query entry and nothing assigns it at line 341, and the loop calls `docs.size()` with no guard. No throw occurs earlier at line 399, because `sortFieldValues` defaults to an empty list at lines 394 to 396 and `needsUnmarshalling` is false, so the skip does not apply.

## Pending

- Item 3, M2 merged `numFound` (5). Needs the probe assertion read on VM1. Until then this item is not closed.

## Scope

Read-only. Nothing was run, edited, posted, filed or emailed. This round does not depend on the real-exit premise job's verdict. The job still runs on a Linux host, and its verdict can correct the exit passages, which the main side owns.
