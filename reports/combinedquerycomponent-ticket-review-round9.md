# CombinedQueryComponent ticket review, round 9

Verdict: STILL OPEN.

Basis: `assignments/pool-combinedquerycomponent-ticket-review-round9.md` at pr-prepare `4546d4ac69c`. Code read at main `d76fc14b9857`. Two read-only subagents: part A covered (a) for C1 to C8 and the line-56 observation, and the sweep of lines 1 to 40. Part B covered (a) for C9 to C14, item (b), and the sweep of lines 41 to 72. No build, test, or gate was run. No draft edit was made, because every open item is either under (a) or under (c), and the round allows edits only for (b). Item (b) needed none.

Process note: the first subagent read the component source in full. The claim and review scope are in `claims/pool-combinedquery-round-9-review.md`.

## (a) Round 8 labels

- C1 (line 21): HOLDS. The label follows "8". Order A then B gives 8 for the reverse order, which the label scopes correctly.
- C2 (line 22): HOLDS. Line 343 keeps the flag true only when every contributor reports false, so a mixed merge is correct for one sub-query.
- C3 (line 22): HOLDS on the tags. Gap: X1 and X2 are not stated to use one sub-query, which line 22 scopes to. Open item A3 below.
- C4 (line 40): HOLDS. The fold at line 342 runs before the fallback at 384, so the running total includes headered shards. The run pointer covers the headerless case.
- C5 (line 40): HOLDS. The formula is exact. HttpShardHandler.java line 440 is `return responses.take();` on the completion queue.
- C6 (line 40): HOLDS. QueryComponent.java line 1162 runs before the fold at 1171, so A then B gives 2.
- C7 (lines 42 to 43): HOLDS. SolrDocumentList.java line 50 is verified. The shard-side write is JavaBinResponseWriter.java line 150.
- C8 (line 54): HOLDS. A case-insensitive grep of the eight combiner and solrj test files finds no match.
- Observation (line 56): HOLDS. `docList.get(queryIndex)` at line 340 has no size check.
- C9 (line 56): HOLDS. Lines 348 to 350 skip the approximate block only on a shard exception.
- C10 (line 66): HOLDS. The fold at line 343 sits inside the guard at lines 338 to 340.
- C11 (line 66): HOLDS WITH EDIT. Case F is at line 51, above line 66, so "below" is wrong. Open item A1 below.
- C12 (lines 48, 52): HOLDS. X1 is all exact, merged flag false. X2 is all inexact, merged flag true. Both match line 22 and the line-343 direction.
- C13 (line 68): HOLDS. The failed push lowers numFound at line 434 before the max fold. The only false return from the ShardDocQueue push is the duplicate branch (QueryComponent.java line 936).
- C14 (line 68): HOLDS. Disjoint sub-queries push no duplicates, so numFound is 2.

## (b) Fix-design proposal phrasing

All five read as proposals or carry a label. No draft edit is needed.

1. Line 62, "so that a skipped shard would never enter that query's totals": PROPOSAL.
2. Line 62, "would contribute a count of zero": PROPOSAL.
3. Line 66, "Correcting the totals ... would mean reconstructing": PROPOSAL.
4. Line 66, "Testing the skip before accumulating would leave ...": PROPOSAL.
5. Line 66, the double-count clause: the first clause is LABELLED, and the second clause is a conditional prediction that holds on reading. Read as a conditional, it is a proposal.

## (c) Sweep: unlabelled factual claims

Each item gives the passage, the problem, and the replacement. None is applied this round.

Lines 1 to 40 (part A):

- S1, line 21, first two sentences. The running-total claim is unlabelled. The "7 instead of 5" and "correct (5)" results are run results with no label. The merged total reaches the response only under maxHitsTerminatedEarly (CQC lines 479 to 487), which the sentence omits. Replacement: "When a shard response carries no approximateTotalHits header, the merge falls back to adding the running per-query total, which already includes the current shard (from code reading; see Mechanism). Headerless shards therefore accumulate cumulatively: two shards with 2 and 3 documents and no approximateTotalHits header merge to an approximate total of 7 instead of 5 in arrival order A then B (confirmed by run, fallback case below, where a shard flags maxHitsTerminatedEarly so the total reaches the response; in the other order the total is 8, from code reading, not run). The numFound for that query is correct (5, confirmed by run, fallback case below); only the approximate total is wrong."
- S2, line 22, first sentence. "only while shards report their counts as not exact" is an unlabelled code claim. Replacement: end the sentence with "as not exact (from code reading, line 343)."
- S3, line 28. The first sentences (lines 315, 328, 330 to 331, 334, 449) are unlabelled, and the only label is at the paragraph end. Replacement for the closing label: "(every line reference in this paragraph and in the list below is from code reading; see Reproduction):"
- S4, line 28. "The per-query totals reach the response through lines 441 to 442" is labelled but inaccurate. Lines 441 and 442 fold into the merged variables. numFound is written at line 448, and approximateTotalHits reaches the header only at lines 479 to 487, under maxHitsTerminatedEarly. Replacement: "The per-query totals fold into the merged totals at lines 441 to 442, numFound is written at line 448, and approximateTotalHits reaches the response header only at lines 479 to 487, when maxHitsTerminatedEarly is set, and each shard that sends a per-query entry has its contribution folded in before the partial-results skip (from code reading; see Reproduction)."
- S5, line 40. "here the same order gives 7 instead of 5" sits after the code-reading label, so it reads as code reading. Replacement: "here the same order gives 7 instead of 5 (confirmed by run, fallback case below)."

Lines 41 to 72 (part B):

- B1, line 48. "every failure an assertion carrying the predicted wrong value" is not supported for M1 and M2, because the draft states no predicted value for them. Replacement: "All seven probe cases failed on unmodified main; for P, P2, F, X1 and X2 each failure was an assertion carrying the predicted wrong value (M1 and M2 outcomes are at line 68)."
- B2, line 63. "because docs can be null when a shard has no per-query entry" is an unlabelled code claim (docs is set to null at line 335 and assigned only inside the guard at line 341). Replacement: append "(from code reading, not run)".
- B3, line 66. The ending "as inexact" is a run result without its case. Replacement: "as inexact (confirmed by run, case X1)."
- B4, line 68. "do not all match the same documents" is an unlabelled current-behavior claim. Replacement: append "(from code reading, not run)".
- B5, line 70. The sentence asserts the sibling ticket's filed status and content. The draft's own status line says NOT filed, so this is not established. Replacement: "The sibling defect in QueryComponent.mergeIds, which counts a skipped partial shard in the merged totals and whose approximateTotalHits fallback adds the running total (from code reading, not run in this ticket), is to be covered by its own ticket, filed before this one; this ticket is intended as its follow-up."
- B6, line 70. "(the partial-results work this component shares with QueryComponent)" is a claim about SOLR-14758 and SOLR-15595 that the listed sources cannot check. Replacement: "Related: SOLR-14758, SOLR-15595."

Not swept (outside the listed sources, or outside the code, run, and ticket-behaviour scope): lines 3 and 5 (process and status), line 12 (the title, which the body scopes), and the SolrDocumentList claim at line 42, which sits under its paragraph's label.

## Open items for round 10

- A1 (C11, line 66): replace "fallback case below" with "fallback case F in Reproduction".
- A3 (C3, line 22): confirm whether the X1 and X2 probe used one sub-query. If so, add "(one sub-query)" to both entries at lines 48 and 52. The sources listed for this round cannot settle it, so the main side must confirm it from the probe.
- Note (part A, line 20): the merged 15 for case P does not say whether P set maxHitsTerminatedEarly, which decides whether a total reaches the response at all. The main side must confirm it from the probe.
- S1 to S5 and B1 to B6 above, with their replacement texts.

Round 10 applies the open items under (c) and (a), then re-checks them. The draft may be edited for (b) only in this round, so round 10's assignment must allow the edits.

## Verdict

STILL OPEN. The ticket text is not final, so the consolidated new-ticket email does not go out. This round sends nothing and posts nothing.
