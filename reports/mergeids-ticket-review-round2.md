# Review: mergeIds ticket draft, round 2 confirmation

Verdict: **STILL OPEN**. Items not confirmed: 1 (item 1, order of the option 1 parenthetical only; membership and scope are confirmed).

## One line per item

1. NOT CONFIRMED. The four accumulators are complete and all sit before the skip, and nothing else in Fix options changed. The parenthetical lists them in the wrong order for the accumulation order the item names (numFound is listed before maxScore). Before and after text below.
2. CONFIRMED. The regularFinishStage decrement (line 762) cannot compensate for a wholesale skip, and the draft sentence is correct as scoped to mergeIds.
3. CONFIRMED. Title and Symptom say only that the count and maxScore include a skipped shard's hits and that numFoundExact=true sits on that count. No passage says a shard's own count is inexact.
4. CONFIRMED. F1, F2, F3 and F4 match the adjudication, the two SOLR commits, and the code at the base commit.

## Scope and method

- Draft: `material/mergeids-ticket-draft.md` on branch pr-prepare (HEAD 1d4fad131d9). History used: 914c100f51e (staged text), 3dd5ed6654b (round 1 fixes), 61cf1365913 (adjudication).
- Code: `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java` at cabedd1d968059215188f4e7563fb303241899ed, read with `git show` from this worktree.
- Commits: a72c9931238 (SOLR-14758) and 812de21e75d (SOLR-15595).
- Not in this worktree: the verification report (hidden_files/side-finding-mergeids-verification.md). The shard2 values in item 4 (F3) are checked against the adjudication's description of its section 4 only.
- No draft edit, commit, push, build, test, Jira, email or comment.

## Item 1: option 1 parenthetical (not confirmed on order)

Membership and completeness (confirmed), at the base commit:
- approximateTotalHits: declared at 1030; accumulated at 1160-1165 (`+=` at 1162 and 1164).
- maxScore: accumulated at 1168-1170 (assignment at 1169).
- numFound: `numFound += docs.getNumFound()` at 1171.
- hitCountIsExact: fold at 1173-1175 (assignment at 1174).
- Skip: `if` at 1193, `continue` at 1194. All four accumulate before it. No other total is accumulated before the skip (the pre-skip flags are in Notes).

Change scope (confirmed): `git diff 3dd5ed6654b 61cf1365913 -- material/mergeids-ticket-draft.md` changes only draft line 49, the option 1 bullet. Nothing else in Fix options changed.

Order (not confirmed): code order is approximateTotalHits (1160), maxScore (1168), numFound (1171), hitCountIsExact (1173). The Mechanism bullets (draft lines 30-33) follow that order. The option 1 parenthetical (line 49) does not, and the adjudication says the moved block is "in code order".

- Before (draft line 49): `{{approximateTotalHits}} / {{numFound}} / {{maxScore}} / {{hitCountIsExact}}`
- After: `{{approximateTotalHits}} / {{maxScore}} / {{numFound}} / {{hitCountIsExact}}`

## Item 2: regularFinishStage reasoning (confirmed)

- `regularFinishStage` is at lines 754-770. The decrement at line 762 is `rb.getResponseDocs().setNumFound(responseDocs.getNumFound() - 1);`, inside the loop that removes null entries (lines 759-764). The adjudication abbreviates the same call.
- It runs from `finishStage` (698-713) at the GET_FIELDS stage, not from `mergeIds`.
- Its null entries are slots of `rb.resultIds`, the merged list. Those slots are sized at 1292-1298 and filled only when the GET_FIELDS retrieval returns the document (1520-1537). So it fires only for merged-list documents that fail to come back.
- A skipped shard's documents never reach `shardDocQueue`: the skip at 1194 comes before the push loop (1203-1226). They never occupy a slot the finish stage could decrement.
- Inside `mergeIds`, numFound is written only at 1024 (declaration), 1171 (add) and 1225 (the decrement). The draft sentence "The only downward adjustment, numFound-- at line 1225" is therefore correct as scoped to mergeIds, the subject of that paragraph. Read without that scope it would be inaccurate, because line 762 also lowers the response numFound. The ticket does not say otherwise. No change needed.

## Item 3: O1 framing (confirmed)

- Title (line 12) and Symptom (lines 18-24) say: numFound and maxScore include a skipped shard's hits; numFoundExact=true sits on a count that includes a shard with no documents; nothing tells the reader the count is not the count of the merged result.
- No passage says a shard's own count is inexact. The Mechanism paragraph says the reverse for the SOLR-14758 path ("the shard's own count is exact").
- Notes, no change required:
  - a. The title and Symptom bullet 2 describe the exact-reporting case (the reproduction and the SOLR-14758 path). The hitCountIsExact fold at 1173-1174 runs before the skip, so a skipped shard that itself sends numFoundExact=false still makes the merged flag false. The ticket does not claim otherwise.
  - b. approximateTotalHits is also inflated by the skipped shard. It reaches the response only when maxHitsTerminatedEarly is set (1264-1273), and the Symptom list does not name it. That is an omission, not an overstatement.

## Item 4: F1 to F4 (confirmed)

- F1, line 14 (blank line 15): [robot emoji] _AI text below_ [robot emoji] _(posted on behalf of Nick Shanin)_. Matches the form in the round 1 report and the adjudication ("form matches criterion 6 exactly"). No claim content.
- F2, line 18: SOLR-14758 is commit a72c9931238, "Fix NPE in QueryComponent.mergeIds when using timeAllowed and sorting", which adds the null check on sort_values. SOLR-15595 is commit 812de21e75d, "No longer skip merging partial shard results for non-sort queries", which narrows the skip. The wording matches both.
- F3, line 41: shard2 `numFound=3`, `numFoundExact=true`, `maxScore=3.0`, matching the adjudication's description of the run setup (its section 4 is not available here). The merged result 5 / true / 99.0 follows from the code: 2 + 3 with no push rejection (the merged list holds all 3 shard2 documents), an exact flag that stays true only if both shards report true (fold at 1173-1174), and maxScore = max(99.0, 3.0).
- F4, line 50, closing sentence: option 2 changes only hitCountIsExact. numFound (1171) and maxScore (1169) are accumulated before the skip and option 2 does not touch them, so of the three Symptom bullets it corrects only the numFoundExact one. Accurate.

## Notes, no change required

- Pre-skip flags: thereArePartialResults (1142, which must stay before the skip so partialResults=true still appears), segmentTerminatedEarly (1144-1152) and maxHitsTerminatedEarly (1154-1159, whose header is written at 1264-1267) are also folded before the skip. Option 1's "merged totals" wording covers the four totals only. If the reorder moves only those four, these flags still reflect a skipped shard. The ticket does not claim otherwise.
- The adjudication's paraphrase of line 762 omits the `rb.getResponseDocs().` prefix. It is the same call and needs no change.

## Process

- The claim `claims/pool-mergeids-ticket-review-round2.md` stays ACTIVE. The main side marks it DONE with the push, as in round 1. Not edited under the one-new-file limit.
- A scratch copy of QueryComponent.java was written to the scratchpad for reading and then removed.
- No em dash in this report.
