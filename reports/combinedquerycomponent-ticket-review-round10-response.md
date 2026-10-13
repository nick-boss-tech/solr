# CombinedQueryComponent ticket review, round 10: main-side settlement

Basis: reports/combinedquerycomponent-ticket-review-round10.md (verdict STILL OPEN). Round 10 left two probe items to the main side, A3 and A4, and a set of (c) sweep items, P1 to P5 and Q1 to Q4, that its own round was not allowed to apply. This record answers the probe items from the probe source, applies the (c) items to material/combinedquerycomponent-ticket-draft.md, and states what a confirmation round still has to check. The draft edits below are in the same push as this record. Line numbers for the draft are the draft's lines after round 10's edits landed.

The probe source survives on the main side: CombinedQueryComponentMergeTotalsTest.java in the vm1 worktree solr-worktrees/cqc-premise (solr/core/src/test/org/apache/solr/handler/component/), the class that ran on 2026-10-11 against unmodified main d76fc14b9857, seed C0FFEE42C0FFEE42. Run artefacts: the premise log and the JUnit XML (gcqc-premise3.log, gcqc-premise-TEST-org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest.xml), and the run addendum in reports/combinedquerycomponent-premise.md.

## A3: the sub-query count of cases X1 and X2 is one

Answer: each of X1 and X2 ran with one sub-query.

Evidence from the probe source:

- The request builder newResponseBuilder (probe lines 116 to 128) adds one CombinerParams.COMBINER_QUERY parameter for each query string it is passed, so the number of query strings is the sub-query count.
- X1 (exactnessAllExactStaysExact, probe line 261) builds its request at probe line 274 with a single query string, "q0". Each shard's response_per_query is a one-element list (probe lines 266 and 272).
- X2 (exactnessAllInexactStaysInexact, probe line 281) builds its request at probe line 298, also with the single string "q0". One-element per-query lists at probe lines 289 and 296.
- The only two-query cases in the probe are M1 and M2 (probe lines 326 and 360, "q0", "q1").

Application, as round 10's A3 prescribes for this answer: "(one sub-query)" added to both exactness entries at draft line 52, and B3 and Q2 applied as written (below).

- Before: "Exactness, case X1: two shards whose per-query lists both report {{numFoundExact=true}}: the merged flag was false instead of true. Case X2: with both reporting false, the merged flag was true instead of false."
- After: "Exactness, case X1 (one sub-query): two shards whose per-query lists both report {{numFoundExact=true}}: the merged flag was false instead of true. Case X2 (one sub-query): with both reporting false, the merged flag was true instead of false."

## A4: case P did set maxHitsTerminatedEarly

Answer: yes. The partial shard in case P carried maxHitsTerminatedEarly=true in its response header.

Evidence from the probe source:

- Case P (skippedPartialShardContributesNothing, probe line 175): the partial shard's header (probe lines 184 to 190) sets partialResults to true (lines 185 to 186), maxHitsTerminatedEarly to true (lines 187 to 188), and approximateTotalHits to 10 (lines 189 to 190). Case P2 (probe line 219) uses the same header (the maxHitsTerminatedEarly entry at probe line 230).
- Consequence: the gate at lines 479 to 487 of CombinedQueryComponent.java is satisfied, so the merged approximate total reaches the response header. The run shows this directly: case P2's assertion reads the merged value from the response header, and the JUnit XML records "P2 approximateTotalHits expected:<5> but was:<15>".

Effect on the draft: round 10's P1 held the "(15 instead of 5)" clause at draft line 20 pending this answer. With the answer yes, the clause stands as written: the 15 is a run result read from the response. Round 10 prescribes no replacement text for that clause, and none is applied. Whether the clause should also name its case (P2) is left to the confirmation round.

## (c) items applied

- P1, draft line 20. Applied as prescribed.
  Before: "(7 instead of 3 in the constructed reproduction below)"
  After: "(7 instead of 3, confirmed by run, case P below)"
- P2, draft line 21. Applied as prescribed.
  Before: "The {{numFound}} for that query is correct in that case (5, confirmed by run, fallback case below); only the approximate total is wrong."
  After: "The {{numFound}} for that query is correct in that case (5, confirmed by run, fallback case below); the approximate total is wrong there (7 instead of 5, confirmed by run, same case); case F does not state the merged numFoundExact, so this sentence makes no claim about it."
- P3, draft line 22. Applied as prescribed. The sentence that followed opened with the same label as a free-standing parenthetical; with the label now inside the sentence, the passage carries it once. The full passage before and after:
  Before: "Only a mixed merge happens to come out right. (from code reading, not run) What the flag should report when several sub-queries contribute is part of the open question in Fix options."
  After: "Only a mixed merge comes out right (from code reading, not run). What the flag should report when several sub-queries contribute is part of the open question in Fix options."
- P4, draft line 22. No text change. The flagged words are the single-sub-query scope of the X1 and X2 sentence ("Where a single sub-query contributes, an all-exact merge of its shards reports {{numFoundExact=false}}, and an all-inexact merge reports {{numFoundExact=true}} (confirmed by run, cases X1 and X2 below)"). The A3 answer confirms the probe ran exactly that scope, so the sentence and its run label stand accurate. Closed by the A3 answer.
- P5, draft line 18. Applied as prescribed.
  Before: "merge their per-query shard results in {{CombinedQueryComponent.mergeIds}}. Three defects sit in the totals that merge reports:"
  After: "merge their per-query shard results in {{CombinedQueryComponent.mergeIds}} (from a grep, line 285). Three defects sit in the totals that merge reports:"
- Q2, which is the B3 correction, draft line 66. Applied as written, as round 10's A3 directs for a one-sub-query answer.
  Before: "without it the merged response keeps reporting an exact single-sub-query merge as inexact."
  After: "without it the merged response keeps reporting an exact merge as inexact (case X1 confirms this by run; the single-sub-query scope is from code reading, not run)."
  Note on the label: "from code reading" stays accurate for the scope after the A3 answer. The scope is known from the probe source; no run artefact states a query count. The run itself (case X1) confirms the inversion the label attaches to.
- Q3, draft line 68. Applied as prescribed.
  Before: "in each case the merged approximate total is the winning sub-query's own total, error included, and the other sub-query's error is invisible."
  After: "in each case the merged approximate total equals the larger of the two per-query approximate totals (run result), so the smaller one does not appear in it (the max rule is from code reading, line 442, not run)."
- Q4, draft line 68. Applied as prescribed.
  Before: "per-query totals of 7 and 3 merged to 7"
  After: "per-query approximate totals of 7 and 3 merged to 7"

## Q1 held, with the reason

Q1, draft line 64, is not applied. Round 10 prescribes replacing "this change settles the single-sub-query case, which the reproduction pins." with "this change settles the single-sub-query case, which code reading covers (from code reading, not run; the sub-query count of cases X1 and X2 is an open probe item)."

That replacement rests on two facts that this settlement changes: A3 was an open probe item, and draft line 52 stated no sub-query count, so "the reproduction pins" had no support in the text. The A3 answer and the "(one sub-query)" statements now at line 52 cure the named defect at its source: the reproduction did run the single-sub-query case, and the draft now says so at the case entries. Applying the replacement verbatim would put a statement in the ticket that this record refutes (the count is no longer an open probe item) and would contradict line 52. The sentence therefore stands as it stood before round 10: "this change settles the single-sub-query case, which the reproduction pins."

This is the one application the confirmation round must judge expressly: accept the A3 cure, or direct the Q1 downgrade anyway.

## Also outstanding from round 10

- Open item A1, draft line 40: "{{queryNumFound}} at line 384 already includes the current shard" lacks the qualifier round 10 prescribes ("already includes the current shard when that shard sent a per-query entry"). It is not a (c) item, round 10 noted it was not on its round's list, and it is not applied here. A confirmation round's assignment must allow it if it is to be applied there.
- Round 10's line 411 note (under a score-only sort the skip never fires, docs can be null for a shard that is not skipped, and line 411 then throws NullPointerException; stated there as a code fact for the main side, not a ticket claim). Not taken up here. The draft keeps the score-only-sort shape's outcome "not established", which round 10 records as accurate. Naming the outcome in the ticket, labelled from code reading and not run, stays an option for the owner and the confirmation round.
- Round 10's B5 note: the clause its edit dropped ("that ticket names this component as a follow-up covering all three of its defects") is supported by the sibling draft, and restoring it is an option. Not taken up here; the line 70 text stands as round 10 left it.

## Verification

After the edits, the draft was read end to end. The change set against the pre-settlement draft is exactly the edits listed above (P1, P2, P3, P5, the two line 52 statements, B3/Q2, Q3, Q4); no other line differs.

## Closing state

For the confirmation round (round 11):

1. Confirm the applied edits above against the draft in this push.
2. Judge the Q1 hold: accept the A3 cure, or direct the Q1 downgrade.
3. A1 (draft line 40) remains unapplied; its application needs the round's assignment to allow it.
4. P4 needs no check beyond the A3 answer recorded here.

If the round confirms 1, accepts 2, and its sweep for unlabelled factual claims is clean, the CombinedQueryComponent ticket text is final, and with mergeIds and gradlew already final the consolidated new-ticket email can go out. Nothing was sent or posted by this settlement.
