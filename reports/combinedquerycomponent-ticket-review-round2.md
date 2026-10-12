# Review: CombinedQueryComponent Jira ticket draft, round 2 confirmation

Verdict: **STILL OPEN.** Item 6 is confirmed. Items 1 to 5 carry findings, and they are wording and scope fixes in the draft `material/combinedquerycomponent-ticket-draft.md`. Two of them matter most: the real-exit sentences state code-reading results as fact, and the exactness symptom presumes an answer to the open count question.

Scope: the six items of `assignments/pool-combinedquerycomponent-ticket-review-round2.md`, read against main `d76fc14b9857e77b830d7a82514b28c523a239c2` (`CombinedQueryComponent.java` unless another file is named). Nothing was run, edited, posted or filed. The draft was not edited; each fix is for the main side.

Gate state, read only: `gates/combinedquerycomponent-real-exit-premise.md` is OPEN and unclaimed. Its verdict is what would settle the real-exit sentences (item 1 and item 4 below).

## One line per item

1. **Finding A, Symptom bullet 1 (draft line 20): FINDING, one clause.** The code citations are confirmed: `QueryComponent` 571 to 575 (empty `sort_values` at 574, rethrow at 575); `CombinedQueryComponent` 210 calls `doFieldSortValues` inside the per-query loop (183 to 221) with no catch, so 213 and 215 to 219 do not run on a throw; `SearchHandler` 507 to 509 catches and calls `shortCircuitedResults` (782 to 800), which adds no per-query entry; `mergeIds` 338 to 343, 381 to 387 and the skip at 399 to 400, with the header value at 386 added before the skip.
   - **The overreach:** "so the merging node folds neither ... for that query" holds by reading for one shape only: an exit at query index 0 with a sort that needs unmarshalling (`docs` stays null, and the skip fires at 399 to 400). Two other reachable shapes do not fold either, and by reading they fail:
     - (a) Index 0 with a score-only sort. `includesNonScoreOrDocField` is false (`SortSpec.java` line 95 for a null sort), so there is no skip, and the loop at 411 calls `docs.size()` on null docs with no guard, a null pointer exception.
     - (b) Index above 0. `docList.get(queryIndex)` at 340 on a list shorter than `queryIndex` throws an index exception, and `mergeIds` has no catch.
   - **Fix:** scope the sentence to the index-0 unmarshalling case, or state that the other shapes are unsettled (the premise's Q2 and the adjacent null-docs item).

2. **Finding A, case P bullet (line 50) and End-to-end paragraph (line 56): FINDING.** The P bullet's labelling is confirmed: it says it is constructed and not the real exit's shape. The paragraph says the merge "folds neither ... for the unprepared query". That is the non-throwing outcome for an exit above index 0, which is the Q2 question the paragraph must not claim. It also omits case 1(a): under a score-only sort the exit cannot land inside `doFieldSortValues` (`QueryComponent` 520 continues for SCORE and DOC), so it lands in the search (`CombinedQueryComponent` 186, `super.process`) with the same `SearchHandler` products, and then reaches line 411 with null docs. The gate's Step 1 does not cover this.
   - **Minor:** "before it is skipped" omits that the skip also needs `needsUnmarshalling` (399). "The fold fires on every merge" omits that 342 to 343 run only when a per-query entry exists.
   - **Fix:** scope the paragraph to what the gate settles, and add the score-sort exit (see the gate job request below).

3. **Flag B, Reproduction line 48: mapping CONFIRMED; FINDING on one phrase.** Against the addendum, P (numFound 7 for 3), P2 (approximateTotalHits 15 for 5), F (numFound 5, approximateTotalHits 7 for 5), X1 (flag FALSE for TRUE), X2 (TRUE for FALSE), M1 and M2 (the two-query cases) are correct, and the total is 7 tests with 7 failures.
   - **The phrase:** line 48 says M1 and M2 have an outcome "described with the open question in Fix options". Line 68 describes no M1 or M2 outcome, and the values (M1 merged approximateTotalHits 21, M2 7, merged numFound 11 in both) appear nowhere in the ticket.
   - **Fix:** state the values, or change the phrase to "whose question is posed in Fix options".

4. **Flag C, verification-level wording: wording CONFIRMED; FINDING on other passages.** Line 48 reads "Confirmed by run on unmodified main `d76fc14b9857`", which is the suggested wording. The probe is named as scratch and not a proposed test, and no cluster reproduction is claimed.
   - **Finding:** lines 20 and 56 state real `timeAllowed` exit products as fact ("is not the shape that produces this", "folds neither", the header value "can still" be added). The support is code reading only, and the run that tests those products (the premise's Q1) is open.
   - **Fix:** label them "from code reading, not run", or hold them for the verdict.
   - **Minor:** line 54, "which is why this was not visible", is an unevidenced causal claim.

5. **Open question, Fix options lines 62 to 68: FINDING (presumes an answer).** The question itself is confirmed: line 68 states the fold at 441 and 442 (`numFound = max(...)`, `approximateTotalHits = max(...)`), the disjoint two-and-two example, option (a), option (b) with the floored form, the `numFoundExact` question, the sentence that the answer decides whether the exactness fix can land, and it asserts no answer. The union claim is gone, and the draft contains neither "union" nor "javadoc". The class javadoc (67 to 76) states no count; `combine/QueryAndResponseCombiner.java` line 66 describes the result list.
   - **The presumption:** Symptom bullet 3 (line 22) calls an all-exact merge that reports `numFoundExact=false` a defect. Fix option 3 (line 64) and line 66 ("keeps reporting exact merges as inexact"; "part of the same fix") state the exactness outcome for every merge. For a merge of several sub-queries, the open question asks whether `numFoundExact` should be false, and under that reading the current all-exact output would be right. The X1 and X2 evidence uses one combiner query key (`premise report` line 106, run spec), so the inversion is shown without any sub-query question. The intent report (lines 80 and 106) advised keeping fix 3 out of the filed text until the count decision, and the draft still carries it.
   - **Fix:** scope Symptom bullet 3 and fix 3 to a single contributing sub-query, and leave the multi-query value to the question.
   - **Minor, non-blocking:** line 68, "Sub-queries can match overlapping documents, so this number can be smaller", credits overlap. The maximum undercounts whenever sub-queries match different documents, as the disjoint example shows.

6. **Premise range, `reports/combinedquerycomponent-premise.md` line 91: CONFIRMED.** At main, line 381 is `Object ath =`, 382 is its continuation, and 383 to 387 are the if/else, with the fallback at 384 and the header at 386. The block 381 to 387 holds the declaration and every use of `ath`, so moving it keeps the variable in scope. The premise Evidence section (line 25) also states 381 to 387.

## Outside the six items (not verdict-bearing)

- The round 1 adjudication (`reports/combinedquerycomponent-ticket-review.md` line 60) says a list ending before the exiting query leaves `docs` null. By reading it throws (item 1(b)).
- The premise report section 4 (line 75) still cites the class javadoc for the union statement and states the union as the fused numFound. That presumes the open question. The adjudication flagged the citation but did not change the premise text.

## Gate job request (for VM2, to stock into `gates/`)

The real-exit premise job is open and unclaimed. Two additions would make it cover the shapes the draft's sentences rest on:

- **Add a score-sort exit at query index 0.** The current Step 1 sets up a sort-value exit, which cannot occur for a score-only sort. The added case exercises item 1(a): with a score-only sort, the exit lands in the search, the merge reaches line 411 with null docs, and the job records whether a null pointer exception is thrown.
- **Add an exit at query index above 0.** This exercises item 1(b): whether `docList.get(queryIndex)` at 340 throws on a list shorter than the query index.

Each added case states its expected outcome on `d76fc14b9857` before the run, so the verdict can be read against it.

## To clear the verdict

1. Scope or label the real-exit sentences (lines 20 and 56), or hold them for the job's verdict.
2. Fix the line 48 phrase on M1 and M2.
3. Scope Symptom bullet 3 and fix 3 to a single contributing sub-query.
4. Apply the minor wording notes in the same edit.

## Scope

Read-only. Nothing was run, edited, posted or filed. The draft is for the main side to edit.

## Main-side adjudication (2026-10-11)

All six items settled against the code at main `d76fc14b9857`, the run addendum in `reports/combinedquerycomponent-premise.md`, and the probe source (`CombinedQueryComponentMergeTotalsTest` in the vm1 worktree `~/workspace/solr-worktrees/cqc-premise`). The reviewer is right on each finding; every fix below is applied to `material/combinedquerycomponent-ticket-draft.md` unless stated otherwise. Where a finding offered a choice, the option that claims less was taken.

1. **Item 1 (Symptom bullet 1, real-exit sentences): applied.** The sentences are now scoped to the one shape the reading settles (an exit at query index 0 with a sort that needs unmarshalling): the shard adds no per-query entry for that query, the merge folds neither its `numFound` nor its exactness for that query, and a header `approximateTotalHits` value can still be added for it, labelled "(from code reading, not run)". The other two shapes are stated as not settled in the same passage (a score-only sort, where an exit stops in the search itself; an exit on a later query, which leaves the per-query list shorter than the query count), and the passage claims neither outcome for them.
2. **Item 2 (End-to-end paragraph): applied.** The paragraph is scoped the same way and no longer states the non-throwing outcome for any exit: it names the score-only-sort exit (it cannot stop in sort-value preparation, so it stops in the search itself with the same `SearchHandler` products) and the later-query exit (shorter per-query list, read with no size check) as not settled, with nothing claiming their outcomes. Both minors are applied: the skip sentence now requires `needsUnmarshalling` ("no sort values for a query whose sort needs unmarshalling"), and the closing sentence now reads "the folds at lines 342 and 343 fire on every merge in which a shard sends a per-query entry".
3. **Item 3 (Flag B, M1 and M2): applied by stating the values.** The open-question passage in Fix options now states both cases: per-query approximate totals of 7 and 21 merged to an `approximateTotalHits` of 21 with a merged `numFound` of 11, and per-query totals of 7 and 3 merged to 7 with a merged `numFound` of 5, each time the winning sub-query's own total with the other sub-query's error invisible. The Reproduction sentence now names M1 and M2 as "whose outcomes are stated with the open question in Fix options". One correction to the settlement brief itself: it gave the merged `numFound` as 11 for both cases; the addendum states 11 for M1 only, and the probe asserts 5 for M2 (`maximumFoldOtherQueryWinning`), so 5 is what the ticket states.
4. **Item 4 (Flag C): applied.** The real-exit passages keep their "(from code reading, not run)" labels and, after items 1 and 2, state only the scoped shape's products; no passage states an unsettled exit product as fact. The minor about a causal claim at line 54 ("which is why this was not visible") does not match the current draft: a search finds no such phrase anywhere in the ticket text, and the only "which is why" is the arrival-order sentence in Mechanism, whose cause is stated in the same passage. No edit for that minor.
5. **Item 5 (open question presumption): applied.** Symptom bullet 3 is scoped to a single contributing sub-query ("Where a single sub-query contributes, an all-exact merge of its shards reports `numFoundExact=false`..."), and its closing sentence leaves the multi-sub-query value to the open question. Fix option 3 is scoped the same way ("for the lists a single sub-query contributes"), with a sentence stating that the flag is shared by every query and that what it should report when several sub-queries contribute is part of the open question; the paragraph after the options now says "an exact single-sub-query merge". The minor is applied: the open question no longer credits overlap; it states that sub-queries match their own documents, overlapping or not, and that the maximum undercounts whenever the sub-queries do not all match the same documents, with the disjoint example kept.
6. **Item 6 (premise range): confirmed by round 2; no action.**

The two outside notes are not edited: the round 1 adjudication's sentence about a short per-query list and section 4 of the premise report stand as records of the earlier reading, flagged in this report; neither is ticket text.

The premise job `gates/combinedquerycomponent-real-exit-premise.md` is extended in the same push: a new Q3 covers the score-only-sort exit (an exit landing in the search at `CombinedQueryComponent` line 186 under a score-only sort, reaching line 411 with null docs), with the reading evidence (line 520 continues for SCORE and DOC; line 411 has no null guard), a third Step 1 run (score-only sort, exit on query index 0), the expected outcome line, and a checklist item.

Round 3 confirmation is posted as `assignments/pool-combinedquerycomponent-ticket-review-round3.md`, scoped to these settlements only.
