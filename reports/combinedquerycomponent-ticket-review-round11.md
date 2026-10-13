# CombinedQueryComponent ticket review, round 11

Verdict: STILL OPEN.

Basis: `assignments/pool-combinedquerycomponent-ticket-review-round11.md` at pr-prepare `9f13cde2b27`. Settlement under review: `reports/combinedquerycomponent-ticket-review-round10-response.md`. Code read at main `d76fc14b9857`. Claim: `claims/pool-combinedquery-round-11-review.md`.

Staffing: the assignment named one subagent. The scheduled step asks for at least two, so this round used three. Subagent A checked P1, P2, P3 and P5. Subagent B checked the line-52 statements, line 22, line 66, and lines 68 and the others. Subagent C swept the whole ticket.

The probe source is outside this repository. The A3 answer (one sub-query per case) and the A4 answer (case P set `maxHitsTerminatedEarly`) are checked here only for consistency with the draft, not against the probe.

## (a) Settlement edits

- P1, line 20: confirmed. Case P is named, and the run result stands.
- P2, line 21: confirmed. Case F states numFound 5 and approximateTotalHits 7, and says nothing about numFoundExact.
- P3, line 22: confirmed. Line 343 keeps the flag true only while shards report false, so a mixed merge ends false, which is correct.
- P5, line 18: confirmed. Line 285 is the `mergeIds` declaration, with `@Override` on line 284.
- Line 52, the one-sub-query statements: confirmed. The entries match the settlement's "After" text, and the direction holds at lines 315, 343 and 449.
- Line 22: confirmed. The single-sub-query outcome is run-confirmed for X1 and X2.
- Line 66, the B3 and Q2 edit: confirmed with an edit. See O2 below.
- Line 68, Q3 and Q4: confirmed with an edit. The header qualifier depends on a probe answer. See O3 below.

## (b) Q1, line 64

Accepted. The settlement held round 10's Q1 replacement because the A3 answer cures its premise. Line 52 now states that X1 and X2 each ran one sub-query, so "this change settles the single-sub-query case, which the reproduction pins" has support in the text. Applying round 10's replacement would say the sub-query count is still an open probe item, which line 52 now refutes. The sentence stands as it is, subject to the label items in S3 and S4 below.

## (c) A1, line 40

Applied. Before: "{{queryNumFound}} at line 384 already includes the current shard, because line 342 ran earlier". After: "{{queryNumFound}} at line 384 already includes the current shard when that shard sent a per-query entry, because line 342 ran earlier". The qualifier is correct: line 342 sits inside the guard at lines 338 to 340.

## (d) Final sweep

Factual claims not yet labelled, or labelled wrongly. Each needs its replacement text applied in a later round. None is applied here.

- O1, line 20. The clause "(15 instead of 5)" is a run result with no label or case. Line 48 makes it case P2. Replace "(15 instead of 5)" with "(15 instead of 5, confirmed by run, case P2 below)".
- O2, line 66. The label "(case X1 confirms this by run; the single-sub-query scope is from code reading, not run)" conflicts with line 22, which already treats the single-sub-query scope as run-confirmed through A3. Replace the parenthetical with "(case X1 confirms this by run for one sub-query; that the one-line change settles the single-sub-query case is from code reading, not run)".
- O3, line 68. The phrase "(run result)" attached to the merged approximate totals of 21 and 7 holds only if the response header carries the value. Line 487 writes it only when `maxHitsTerminatedEarly` is set (line 479), and line 483 needs a value above zero. The settlement records that flag for case P only. The probe must answer this (new item A5, below). If M1 and M2 set the flag, insert "read from the response header, which carries it only when a shard sets maxHitsTerminatedEarly" after "(run result". If they did not, change "(run result)" to "(from code reading, not run)".
- S1, line 12 (title). The title states current behavior with no label. Replace the title with "CombinedQueryComponent.mergeIds counts a skipped partial shard that sends a per-query entry in the per-query totals, adds the running total as the approximateTotalHits fallback, and folds hitCountIsExact inverted (from code reading; run results in Reproduction)".
- S2, line 64. "equivalently the if form {{QueryComponent}} uses at lines 1173 to 1174" carries a claim about current code. Append " (from code reading, not run)" after "lines 1173 to 1174".
- S3, line 64. "The flag is one flag shared by every query" is a present-tense claim about current code. Append " (from code reading, line 315)" after it.
- S4, line 64. "so that for the lists a single sub-query contributes, the merged flag is true only when every contributing list reports its count exact" is a prediction of the post-fix result, stated in the present tense. Replace it with "so that, for the lists a single sub-query contributes, the merged flag would be true only when every contributing list reports its count exact".
- S5, line 66. The label stops before the second clause, which also asserts a fact about the current fold. Replace "which is a fold over every shard that sends a per-query entry (from code reading, not run) and cannot be un-folded without per-shard tracking." with "which is a fold over every shard that sends a per-query entry and cannot be un-folded without per-shard tracking (from code reading, not run)."
- S6, line 70. The label precedes the QueryComponent fallback claim, which sits outside it. Replace "counts a skipped partial shard in the merged totals (from code reading, not run in this ticket) and whose approximateTotalHits fallback adds the running total" with "counts a skipped partial shard in the merged totals and whose approximateTotalHits fallback adds the running total (from code reading, not run in this ticket)". The claim holds: QueryComponent line 1162 adds the running numFound before line 1171 folds in the shard.
- S7, line 48. The cases X1 and X2 are not tagged as one sub-query here, although line 52 tags them. Replace "X1 (all shards exact, merged flag false) and X2 (all shards inexact, merged flag true)" with "X1 (one sub-query; all shards exact, merged flag false) and X2 (one sub-query; all shards inexact, merged flag true)".

Checked and matching main `d76fc14b9857`: CombinedQueryComponent lines 77, 284 to 285, 315, 328, 330 to 331, 334, 340, 341, 342 to 343, 348 to 350, 359 to 364, 381 to 387, 390 to 400, 434, 441 to 442, 448 to 449, 479 to 487. SolrDocumentList line 50. QueryComponent lines 971, 1162, 1171, 1173 to 1174.

## Open items

- A5, probe item (new). Did case M1 or case M2 set `maxHitsTerminatedEarly` in any shard response? The main side must answer from the probe source. The answer selects the O3 text.
- A3 and A4 are answered by the settlement. They are not reopened here.

## Verdict

STILL OPEN. The items that hold the verdict are O1, O2, O3 (with the A5 answer), and S1 to S7. Each has its replacement text above. Round 12 needs an assignment that allows edits to labels and to these lines, since this round allowed only A1 and the Q1 decision. The consolidated new-ticket email does not go out, since the ticket text is not final.

## Not done

- No PR, comment, Jira write, or email.
- No build, test, or gate. The probe source was not read.
