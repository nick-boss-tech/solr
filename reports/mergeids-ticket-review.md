# Review: mergeIds Jira ticket draft

Verdict: **STILL OPEN**

- Fixes made to `material/mergeids-ticket-draft.md`: 4 (wording, one markup header, one omitted reproduction value, one added sentence). Before and after text below.
- Evidence disagreements flagged, not fixed: 2 (D1, D2).
- Code checked at `cabedd1d968059215188f4e7563fb303241899ed:solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`, read with `git show` from this worktree.
- Not done by this run: no commit, no push, no claim update to DONE (the parent does that with the deliverable), no Jira, email, comment or build.

## Why STILL OPEN, and what flips it to SATISFIED

1. D1 (fact, needs a main-side decision): fix option 1's parenthetical omits `approximateTotalHits`. The Mechanism section and the evidence both place it before the skip (lines 1160-1165). A reader who moves only the three named accumulators would leave the skipped shard counted in `approximateTotalHits`. Add it to the parenthetical, or confirm it is out of scope.
2. O1 (framing, needs confirmation): see criterion 3. The ticket's position that `numFoundExact=true` is misleading should be confirmed before filing.

To reach SATISFIED: resolve D1 and confirm O1. D2 needs no ticket change. O4 needs handling at the email step.

## Fixes made (before and after)

F1. Criterion 6, AI header was missing. Inserted between the Title line and `h2. Symptom`:
- After: a line with the robot emoji, then `_AI text below_`, then the robot emoji, then `_(posted on behalf of Nick Shanin)_`, then a blank line. Form matches the assignment's Jira underscore form.

F2. Wording, Symptom paragraph:
- Before: `the {{timeAllowed}} / query-limit case fixed against an NPE by SOLR-14758 and narrowed by SOLR-15595)`
- After: `the {{timeAllowed}} / query-limit case that SOLR-14758 fixed for an NPE and SOLR-15595 narrowed)`

F3. Omitted evidence value, Reproduction (the evidence gives shard2 `numFoundExact=true`; the draft omitted it):
- Before: `shard2 normal, 3 documents, {{numFound=3}}, {{maxScore=3.0}};`
- After: `shard2 normal, 3 documents, {{numFound=3}}, {{numFoundExact=true}}, {{maxScore=3.0}};`

F4. Criterion 4 fairness, added rationale (substance addition, flagged here so it can be reverted). The option 2 bullet did not say why option 2 is not enough:
- Before: ` * Keep the totals, but set {{hitCountIsExact = false}} when a shard is skipped, so the response stops asserting the count is exact.`
- After: the same bullet plus ` That leaves {{numFound}} and {{maxScore}} counting the skipped shard, so it corrects only one of the three symptoms above.`

The draft is no longer verbatim from the staged text. These four edits are the only changes.

## Evidence disagreements flagged (not fixed)

D1. Fix option 1 parenthetical is incomplete against the evidence. The draft lists "numFound / maxScore / hitCountIsExact accumulation" as the lines to move below the skip. The accumulation before the skip also includes `approximateTotalHits` (lines 1160-1165), which the draft's own Mechanism section lists. Code: `approximateTotalHits += ...` at 1162-1164 runs before the skip at 1193-1194. The recommended reorder must move it too, or the skipped shard stays in that total.

D2. "The only downward adjustment, `numFound--` at line 1225" is correct only inside `mergeIds`. `regularFinishStage` (lines 754-770, specifically 762) also does `setNumFound(getNumFound() - 1)` for each null document after fields retrieval. The ticket paragraph is scoped to `mergeIds`, so the ticket sentence is accurate as written; the evidence wording is not. No ticket change needed; flagged so the evidence record can be corrected.

## Criteria

1. Title: clean. States counting of numFound and maxScore, skipped documents, and numFoundExact=true still reported. Matches the code.

2. Numbers and lines: clean against the base commit. Checked:
   - approximateTotalHits accumulation 1160-1165; maxScore 1168-1170 (draft cites ~1169, inside the block); numFound += 1171; hitCountIsExact fold 1173-1175 (draft cites 1174, inside the fold).
   - skip: `if` at 1193, `continue` at 1194; `numFound--` at 1225 (inside the push-rejected branch of the document loop, which starts at 1203).
   - accumulators declared 1024-1031; `setNumFoundExact(hitCountIsExact)` at 1295 (not cited in the draft); `mergeIds` at 971.
   - Reproduction values follow from the code: shard1 contributes 2 docs and numFound 2, shard2 contributes 3 docs and numFound 3, the skipped shard's documents never reach the queue so no decrement, so merged numFound is 5. maxScore is a running max that keeps shard1's 99.0. Merged list 3 documents.
   - `QueryComponentPartialResultsTest` was added by 812de21e75d (SOLR-15595). It has 4 `@Test` methods and never sets numFound, as the draft says.

3. Mechanism: substantively accurate. Checked:
   - `partialResults=true` is put on the merged header at 1239-1243 when `thereArePartialResults` is set (1142, before the skip). Draft's statement is correct.
   - The shard's flag comes from `hitCountRelation()` (JavaBinResponseWriter line 150, SearchHandler line 748). No code site in `solr/core/src/java` or `solr/solrj/src/java` downgrades numFoundExact from the partial state (checked by git grep on the base commit).
   - SOLR-14758 path: `doFieldSortValues` (line 455, called at 1886) runs after `searcher.search` (1857), and the `sort_values` it leaves empty on ExitingReaderException (lines 571-575) matches the skip condition. The draft's "after the shard's search has completed" is correct.
   - No overstatement of crash, data loss or security. Clean.
   - Observations (no change):
     - O1 (framing, needs confirmation). The title and Symptom treat `numFoundExact=true` as the defect. On the reproduction values both shards' counts are exact, so 5 is an exact count of matching documents. The defect the code supports is that merged numFound and maxScore describe documents the merged list does not carry, so the count is not the count of the merged result. The ticket's framing is a design position. Maintainers could argue the flag is correct for the matched set. Confirm before filing.
     - O2. The "more generally" sentence is accurate. The same non-downgrade also holds when a search is cut short mid-collection (SolrIndexSearcher lines 337-340 set only partialResults; the relation comes from the collector via lines 1928 and 1753). I did not verify Lucene's collector relation under interruption. The draft does not claim it; no change needed.
     - O3. The Reproduction says "End to end, the ingredients are..." and does not say that no end-to-end run was done (the evidence says it was not attempted). A short "not run end to end" sentence would make the limit explicit. Not edited.

4. Fix options: reasoning checked against the code and holds.
   - Accumulators are scalars (1024-1031). No per-shard record is kept, so the merged maximum cannot be decremented.
   - `maxScore` is consumed only by `setResultIdsAndResponseDocs` (1286-1293, passed in as a parameter). No `setMaxScore` in QueryComponent other than 1293. `regularFinishStage` (754-770) does not touch maxScore. Nothing downstream recomputes it.
   - `hitCountIsExact` is a one-way AND (1173-1174). It cannot be restored from the fold alone.
   - Option 2's shortfall was not stated; added in F4.
   - Precedent for option 1: exception responses are already skipped before accumulation (lines 1101-1104). Could be cited in the ticket; not added.
   - D1 applies to option 1's wording.

5. Jira wiki markup: clean. `h2.` headings, `{{...}}` code spans, ` * ` bullets, no GitHub markdown in the ticket text (the staging preamble above `Type: Bug` is outside the ticket text; see O4). No em dashes in the file after edits.

6. Disclosure: footer present as the last element and matches the assignment's wording exactly. AI header was missing; added (F1).

7. Process vocabulary: clean in the ticket text. The reproduction seed is not in the ticket. Preamble issue is O4.

8. "we": none in the ticket text.

## Observations outside the ticket

O4 (criterion 7, email step). Lines 1-9 of the draft file (the `#` heading, Status, Source line with the internal goal path and the assignment path, and the staging note) are not ticket text, but they sit in the file the main agent will email. Send only the text from `Type: Bug` down, or strip the preamble. Not edited, since this changes structure rather than wording.

O5 (pre-existing code, not in the draft). `QueryComponent.java` line 1162, `approximateTotalHits += numFound;`, runs when a shard response omits the `approximateTotalHits` header. At that point `numFound` is the running sum of earlier shards (declared at 1024, updated at 1171), not this shard's `docs.getNumFound()`. This looks like a separate bug. It is outside this ticket. Flagged for the main side. D1 moves this same line, so the main side may want to decide on it together with D1.

## Main-side adjudication (2026-10-11)

Settled against the verification report (CONFIRMED BY RUN, 2026-10-06, main side hidden_files) and the code at cabedd1d968059215188f4e7563fb303241899ed, re-read for this adjudication.

D1: reviewer is right; draft corrected. The accumulation before the skip includes `approximateTotalHits` (verification report section 1: accumulated at lines 1160-1165, ahead of `maxScore` at 1168-1170, `numFound += docs.getNumFound()` at 1171, and the `hitCountIsExact` fold at 1173-1175; code re-read confirms `approximateTotalHits += ...` at 1162-1164). The draft's own Mechanism section lists it, so the fix option parenthetical was incomplete against the draft itself. Fix option 1 now reads: move the `{{approximateTotalHits}} / {{numFound}} / {{maxScore}} / {{hitCountIsExact}}` accumulation below the skip, in code order. No other passage changes: the option's lead-in already says "the shard's totals", and the recommendation paragraph's subtraction argument is unaffected (a plain sum is the one total subtraction can repair).

D2: draft stands; no ticket change. The code reading is confirmed: `regularFinishStage` (lines 754-770) does `setNumFound(responseDocs.getNumFound() - 1)` at line 762 for each null document. That decrement belongs to the fields-retrieval finish stage, not to `mergeIds`, and it fires only for documents present in the merged list that come back null on retrieval; a wholesale-skipped shard's documents are never in that list, so the decrement cannot compensate for a skip either. The ticket sentence sits inside the Mechanism section, whose subject is `mergeIds`, and inside `mergeIds` the only downward adjustment is `numFound--` at line 1225. The ticket's point, that nothing compensates for a shard skipped wholesale, holds with or without the finish-stage decrement. The verification report's section 1 sentence ("The only compensation, `numFound--` at line 1225") is written inside a `mergeIds`-scoped section but reads unscoped; flagged for a scoping correction in that record on the main side.

O1: framing stands; no ticket change. The ticket does not claim either shard's own count is inexact. Its Symptom text says the merged count "includes a shard that contributed no documents" and that "nothing tells them the count is not the count of the merged result". That is the verification report's own framing: section 3 calls the pairing of `numFoundExact=true` with a count of a zero-document shard "the sharpest form of the finding" and states "the response actively asserts exactness"; section 6 names the same combination (exactness asserted over a count that includes a shard that contributed no documents, plus a `maxScore` no returned document attains) as the strongest framing, anticipating the defense that a partial shard's hits are a defensible total for a flagged-partial response. The run fact behind the title is direct: the scratch test `mergedNumFoundExactIsFalseWhenShardSkipped` failed with "expected:<false> but was:<true>" (seed F2C2B3B7EFFDB021). The counter-position (5 is an exact count of documents matching across the two shards) is recorded here and in the report above; it answers a claim the ticket does not make.

Fixes F1 to F4 verified against the evidence; none reverted.

- F1 (AI header added): form matches criterion 6 exactly. No claim, number, or citation content.
- F2 (Symptom wording): same facts, different phrasing. SOLR-14758 is the NPE ticket that added the `sort_values` null guard and SOLR-15595 narrowed the skip (verification report sections 1 and 2). No claim changed.
- F3 (shard2 `numFoundExact=true` added to Reproduction): restores a value the staged draft had dropped. The verification report's run setup (section 4) gives shard2 `numFound=3`, `numFoundExact=true`, `maxScore=3.0`. No other number or citation touched.
- F4 (option 2 shortfall sentence added): accurate against the draft and the code. Option 2 sets `hitCountIsExact = false` only; `numFound` and `maxScore` are accumulated before the skip and option 2 does not touch them, so of the three Symptom bullets it corrects only the `numFoundExact=true` one. No numbers or citations changed.

O3: no change. The Reproduction section opens "At the merge level", which states the level of the run plainly, and the end-to-end paragraph lists ingredients rather than claiming a run. O4 is a send-step handling note for the main side (only the text from `Type: Bug` down is ticket text). O5 is recorded for the main side as a separate code question outside this ticket; the D1 correction moves line 1162 as part of the accumulation block without changing what it adds.

State after adjudication: the draft is final on the main side. A round 2 confirmation pass is posted as assignments/pool-mergeids-ticket-review-round2.md, scoped to the D1 correction, the D2 and O1 settlements, and the F1 to F4 passages as they now stand.
