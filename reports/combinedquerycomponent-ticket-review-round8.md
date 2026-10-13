# CombinedQueryComponent ticket review, round 8 (vm2, 2026-10-13)

Verdict: STILL OPEN.

Round 8 was posted on pr-prepare and no reviewer claimed it, so the review was done on vm2 (the assignment's intended host is Windows). The checks were read-only: the draft at the pr-prepare tip, and code at main `d76fc14b9857`. No build, test or gate was run. Run results cited in the draft were taken from the premise report and were not re-run.

Item (b) edits were applied to the draft in this push, each listed below. The (c) sweep found unlabelled factual claims, so the verdict is STILL OPEN. Those (c) findings are not applied this round.

## (a) Round 7's applied edits

- Line 44, label "(from code reading, not run)": holds. The skip at lines 399 to 400 depends only on `thisResponseIsPartial`, `sortFieldValues` and `needsUnmarshalling`, and none of them is a total.
- Line 56, folds at lines 342 and 343 "fire on every merge in which a shard sends a per-query entry": holds. Both run before the skip.
- Lines 20 and 56, approximate wording: holds. A shard with no per-query entry reaches the block at 381 to 387, since only the exception `continue` at 348 to 350 leaves earlier. The header value is added at 386. With no header, line 384 adds `queryNumFound`, which is the running total. The sentence the round 7 settlement quotes matches the code.

## (b) Fix-design items

1. Line 62, "so a skipped shard never enters that query's totals": present-tense prediction. Edited to "so that a skipped shard would never enter that query's totals."
2. Line 62, "a query whose shards are all skipped contributes a count of zero": present-tense prediction. Edited to "would contribute a count of zero."
3. Line 66, "Correcting the totals when the skip fires instead means reconstructing": edited to "would mean reconstructing". The clause "for the approximate total that means knowing" is edited the same way, to "would mean knowing".
4. Line 66, "Testing the skip before accumulating leaves each query's totals describing the same set of shards": edited to "would leave".
5. Line 66, "so a reorder alone leaves the double count": edited to "would leave". Its first clause, "the fallback fires for complete shards too", asserts current behavior. It is judged under (c) below and is not edited this round.

Before and after for each edit is in the diff of `material/combinedquerycomponent-ticket-draft.md` in this push.

## (c) Sweep, factual claims only

Each entry gives the passage, the problem, and the replacement. None is applied this round. Line numbers below are the draft's lines.

- C1, line 21, "in the other order the total is 8": derived, not run. Replace with "in the other order the total is 8 (from code reading, not run)".
- C2, line 22, "Only a mixed merge happens to come out right.": derived from line 343, unlabelled. Append "(from code reading, not run)".
- C3, line 22, the all-exact and all-inexact outcomes: these are run results (cases X1 and X2), but the Symptom does not say so. Append "(confirmed by run, cases X1 and X2 below)". Minor.
- C4, line 40, "queryNumFound at line 384 already includes the current shard ... headered or not": code-reading claim, verified at lines 342 and 384, unlabelled. Append "(from code reading; the resulting total is confirmed by run in the fallback case below)".
- C5, line 40, the arrival-order formula and "(shard responses are in completion order)": derived and unlabelled. Completion order is verified: `HttpShardHandler.java` takes responses from a queue at line 440. Replace the parenthetical with "(from code reading, not run; responses arrive in completion order, HttpShardHandler.java line 440)". The formula counts only shards that complete normally, because exception shards return at 348 to 350 before the fallback. Add "that complete normally" after "headerless shards arriving at or after it".
- C6, line 40, "two shards of 2 and 3 in arrival order A then B give 2 instead of 5": the 2 is derived. Lines 1161 to 1162 read the running total before the fold at 1171, which is verified. Append "(from code reading, not run)".
- C7, lines 42 and 43, "SolrDocumentList.numFoundExact defaults to true (line 50)" and "a shard writes true when its count is exact": line 50 is verified. The shard-side write was not traced. The paragraph has no label. Append "(from code reading, not run)" to the paragraph. The QueryComponent form at lines 1173 to 1174 is verified and falls under the same label.
- C8, line 54, "None of the component's existing tests asserts numFoundExact or approximateTotalHits on the merged response": a grep of the component's test classes finds no match, but the sentence is unlabelled. Append "(from a grep of the component's test classes, not run)".
- C9, line 56, "any shard response without the approximateTotalHits header exercises the fallback": overbroad. A response carrying a search exception returns at lines 348 to 350, before the header is read at 381. Replace with "any shard response that completes without the approximateTotalHits header exercises the fallback".
- C10, line 66, "which is a fold over every shard": code-reading claim, unlabelled. Append "(from code reading, not run)" after the clause.
- C11, line 66, the fallback "fires for complete shards too": current behavior, and run case F confirms it. Replace with "the fallback fires for complete shards too (confirmed by run, fallback case below), not only skipped ones, so a reorder alone would leave the double count." This is the first clause of item (b)5.
- C12, line 66, "keeps reporting an exact single-sub-query merge as inexact": run result (case X1), unlabelled. Append "(confirmed by run, case X1 below)". Minor.
- C13, line 68, "the response numFound is currently the largest single sub-query match count (... at lines 441 to 442)": verified, `max` at 441 and 442. Unlabelled. Append "(from code reading; confirmed by run in the two maximum-fold cases below)".
- C14, line 68, "two disjoint sub-queries with two hits each return four documents while numFound is 2": derived from the maximum fold, not run. Append "(from code reading, not run)".

## Observation, not a finding

Line 56 says the merge "reads it with no size check". That is accurate. The read is `docList.get(queryIndex)` at line 340, and a list shorter than the query count would throw `IndexOutOfBoundsException` there. The draft says whether the merge folds, skips or fails in those shapes is not established. Code reading suggests a throw at line 340, so the draft could say so with a label. Left unchanged, since the draft's position is safe as written.

## (d) Verdict

STILL OPEN. Remaining work: apply C1 to C14 (with the replacements above), then re-run round 8's sweep on that text. Round 9 should check that the labels are in place and that the replacements do not add new unlabelled claims.
