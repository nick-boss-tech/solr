# CombinedQueryComponent ticket review, round 10

Verdict: STILL OPEN.

Basis: `assignments/pool-combinedquerycomponent-ticket-review-round10.md` at pr-prepare `4e9da8ca234`. Code read at main `d76fc14b9857`. The claim is `claims/pool-combinedquery-round-10-review.md`.

Two probe items cannot be settled from the listed sources: A3 (the sub-query count of cases X1 and X2) and the case-P note (whether case P set `maxHitsTerminatedEarly`). Until both are answered, the verdict cannot be SATISFIED. The sweep also found new unlabelled claims, so the verdict is STILL OPEN on those as well.

Process: four read-only subagents ran, two before the edits and two after. No build, test, or gate was run. Nothing was posted.

## Edits applied this round

Each edit was checked against the code at `d76fc14b9857` before it was applied, or confirmed after it. Before and after text is given for each.

- S1, line 21, part a. Before: "...the running per-query total, which already includes the current shard. Headerless shards". After: "...the running per-query total, which already includes the current shard when that shard sent a per-query entry (from code reading; see Mechanism). Headerless shards". Correction made from the pre-apply check: the qualifier.
- S1, line 21, part b. Before: "in arrival order A then B (A the shard with 2 documents, B the shard with 3; in the other order the total is 8 (from code reading, not run))." After: "in arrival order A then B (confirmed by run, fallback case below, where a shard flags maxHitsTerminatedEarly so the total reaches the response; A the shard with 2 documents, B the shard with 3; in the other order the total is 8, from code reading, not run)."
- S1, line 21, part c. Before: "The {{numFound}} for that query is correct in that case (5); only the approximate total is wrong." After: "The {{numFound}} for that query is correct in that case (5, confirmed by run, fallback case below); only the approximate total is wrong."
- S2, line 22. Before: "only while shards report their counts as not exact." After: "only while shards report their counts as not exact (from code reading, line 343)."
- S4, line 28. Before: "The per-query totals reach the response through lines 441 to 442, and each shard". After: "The per-query totals fold into the merged totals at lines 441 to 442, numFound is written at line 448, and approximateTotalHits reaches the response header only at lines 479 to 487, when maxHitsTerminatedEarly is set, and each shard".
- S3, line 28. Before: "(from code reading; see Reproduction):". After: "(every line reference in this paragraph and in the list below is from code reading; see Reproduction):".
- S5, line 40. Before: "here the same order gives 7 instead of 5." After: "here the same order gives 7 instead of 5 (confirmed by run, fallback case below)."
- B1, line 48. Before: "All seven probe cases failed on unmodified main, every failure an assertion carrying the predicted wrong value." After: "All seven probe cases failed on unmodified main; for P, P2, F, X1 and X2 each failure was an assertion carrying the predicted wrong value (M1 and M2 outcomes are at line 68)."
- B2, line 63. Before: "because {{docs}} can be null when a shard has no per-query entry." After: "because {{docs}} can be null when a shard has no per-query entry (from code reading, not run)."
- B4, line 68. Before: "whenever the sub-queries do not all match the same documents." After: "when the distinct documents the sub-queries match together outnumber the largest single sub-query match count (from code reading, not run)." Correction made from the pre-apply check: the original wording was too broad.
- B5, line 70. Before: "is covered by its own ticket, filed separately; that ticket names this component as a follow-up covering all three of its defects, and this ticket is that follow-up." After: "is drafted separately and is not yet filed; this ticket is intended to follow it." The assignment's wording was applied as written. It drops the clause that the sibling names this component as a follow-up. The sibling draft (line 63) supports that clause, so restoring it is an option for the main side.
- B6, line 70. Before: "Related: SOLR-14758, SOLR-15595 (the partial-results work this component shares with {{QueryComponent}})." After: "Related: SOLR-14758, SOLR-15595."
- A1, line 66. Before: "(confirmed by run, fallback case below), not only skipped ones". After: "(confirmed by run, fallback case F in Reproduction), not only skipped ones".

Not applied: B3, line 66 (the X1 sentence with "single-sub-query"). Its scope is open item A3, and the label would present that scope as run-confirmed.

## (a) Applied edits, after the edits

- S1, line 21: HOLDS. The qualifier is correct. Line 342 sits inside the guard at lines 338 to 340. The case-F pointer holds, since the merged total reaches the response only through lines 479 to 487.
- S2, line 22: HOLDS. Line 343 keeps the flag true only while `Boolean.FALSE.equals(docs.getNumFoundExact())` holds.
- S3 and S4, line 28: HOLD. Every cited line matches the code. The paragraph omits the `approximateTotalHits > 0` test at line 483, which does not make it wrong.
- S5, line 40: HOLDS. The order A then B gives 2, then 2 + 5 = 7.
- B1, line 48: HOLDS. Lines 50 to 52 and 68 give the stated values. Line 68 gives the M1 and M2 values only, so the "failed" clause for those two rests on line 48.
- B2, line 63: HOLDS. `docs` is null for a partial shard and, with `shards.tolerant`, for any shard.
- B4, line 68: HOLDS. The numFound is the running maximum of `queryNumFound` (line 441), written at line 448.
- B5, line 70: HOLDS. The sibling draft says "Status: NOT filed", and its title and description match.
- B6, line 70: HOLDS. Nothing else on the line is dropped.
- A1, line 66: HOLDS. Case F is at line 51, above line 66, under Reproduction.

## (b) Fix-design proposal phrasing

Lines 62 and 66 still read as proposals, with one exception: the X1 sentence on line 66 (open item A3 below). No (b) edit is needed beyond B3.

## (c) Unlabelled claims found by the post-apply sweep

None is applied this round.

- P1, line 20. "(7 instead of 3 in the constructed reproduction below)" is a run result without a case name. Replace with "(7 instead of 3, confirmed by run, case P below)". The "15 instead of 5" in the same sentence is case P2; it is pending open item A4.
- P2, line 21. "only the approximate total is wrong." makes a claim about case F's whole response, but line 51 does not state the merged numFoundExact. Replace with "the approximate total is wrong there (7 instead of 5, confirmed by run, same case); case F does not state the merged numFoundExact, so this sentence makes no claim about it."
- P3, line 22. "Only a mixed merge happens to come out right." is unlabelled, and no probe case covers a mixed merge. Replace with "Only a mixed merge comes out right (from code reading, not run)."
- P4, line 22. "Where a single sub-query contributes" is open item A3.
- P5, line 18. "mergeIds" is accurate (line 285 overrides it), but unlabelled. Append " (from a grep, line 285)" after "mergeIds".
- Q1, line 64. "this change settles the single-sub-query case, which the reproduction pins." Line 52 does not state a sub-query count for X1 or X2, so the reproduction pins no single-sub-query case. Replace with "this change settles the single-sub-query case, which code reading covers (from code reading, not run; the sub-query count of cases X1 and X2 is an open probe item)."
- Q2, line 66. "an exact single-sub-query merge as inexact." Replace the sentence's ending with "an exact merge as inexact (case X1 confirms this by run; the single-sub-query scope is from code reading, not run)." This is the B3 correction; it waits on the probe answer for A3 before it is applied as a label.
- Q3, line 68. "in each case the merged approximate total is the winning sub-query's own total, error included, and the other sub-query's error is invisible." "Error" is never defined, and the draft does not state the true counts behind M1 and M2. Replace with "in each case the merged approximate total equals the larger of the two per-query approximate totals (run result), so the smaller one does not appear in it (the max rule is from code reading, line 442, not run)."
- Q4, line 68. "per-query totals of 7 and 3" should read "per-query approximate totals of 7 and 3", since the merged numFound is 5, and 7 and 3 cannot be numFound values.

## Open items

- A1 (line 40). "queryNumFound at line 384 already includes the current shard" lacks the qualifier that the shard must have sent a per-query entry. Replace with "queryNumFound at line 384 already includes the current shard when that shard sent a per-query entry". This was not on the round's list.
- A3 (lines 22, 48, 52, 66). The sub-query count of cases X1 and X2 is not stated. If the probe ran one sub-query, add "(one sub-query)" to both entries at line 52, and apply B3 and Q2 as written. The main side must confirm this from the probe source.
- A4, case P (line 20, and lines 48 and 50). Whether case P set `maxHitsTerminatedEarly` decides whether the merged 15 reaches the response at all (lines 479 to 487). The main side must confirm it from the probe source.
- Note, line 411 (not a ticket claim, a code fact for the main side). Under a score-only sort, the skip at lines 399 to 400 never fires, and `docs` can be null for a shard that is not skipped. Line 411 then dereferences null, which throws NullPointerException. The draft leaves the merge outcome for that shape "not established", which remains accurate. The main side may want to name this outcome, labelled from code reading, not run.

## Next round

Round 11 needs the probe answers for A3 and A4, and the (c) items P1 to P5 and Q1 to Q4 applied. Round 11's assignment must allow those edits, since this round allowed only the list above.

## Verdict

STILL OPEN. The ticket text is not final, so the consolidated new-ticket email does not go out. This round sends nothing and posts nothing.
