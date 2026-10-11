# Review: mergeIds fix, round 4 confirmation

Verdict: **STILL OPEN.** The branch needs no change. The code and test claims in the settlements check out. Three things keep the verdict open: a wrong mechanism in the sort-values scope note (settlement 1, repeated in three response records and in the ticket's Mechanism line 39), a changelog link in the PR draft that points at a file the head does not contain, and record statements that are now stale (the PR draft's "full checks green", and two lines of our own round 3 report).

Branch: `mergeids-skipped-shard-totals` at head `9e85ce5c162086ae806f617419c4405512792dde`, base `d76fc14b9857e77b830d7a82514b28c523a239c2`. Nothing was run, built, tested, gated, posted or filed. No branch, draft or record was edited.

## Gate state this review relied on

- `d211794596b`: GREEN (receipt, GATE RUNNER DONE 2026-10-11T19:22:52Z).
- `73d20a0f939`: GREEN (receipt, re-gate, GATE RUNNER DONE 2026-10-11T20:36:48Z).
- `9e85ce5c162`: GREEN (receipt lines 42 to 52, re-gate, GATE RUNNER DONE 2026-10-11T21:46:01Z; proof leg 17 tests with the seven premise failures; head 17 of 17; `:solr:core:check -x test` rc=0). The receipt also lists the four new tests (lines 45 and 50). The log is on vm1 by design and is not on disk here, so the receipt's record was checked, not the log.
- Production code at `9e85ce5c16` is identical to `73d20a0f939`; the diff is the test file only (+150 lines).

## Settlement 1: decline wording and scope note

- **1a. Stubs: CONFIRMED.** `MockSortSpecBuilder.java` stubs `getCount` (line 29), `getSort` inside `withSortFields` (line 38) and `includesNonScoreOrDocField` (line 43). No `getSchemaFields` stub exists under `solr/core/src/test`.
- **1b. Call sites and failure order: CONFIRMED, with a qualifier.** `unmarshalSortValues` (`QueryComponent.java` 1373 to 1411) does not read its `IndexSchema` parameter. It calls `getSchemaFields()` at 1379 and `getSort().getSort()` at 1380, and `schemaFields.get(sortFieldNum)` at 1398. The `IndexOutOfBoundsException` comes first only when `withSortFields` has stubbed `getSort`. Without it, `getSort()` returns null under Mockito defaults and line 1380 throws a null pointer exception first. A sort_values case needs `withSortFields` in any case.
- **1c. Null-entry stub: CONFIRMED.** Lines 1398 to 1400: a null at `sortFieldNum` takes the add-as-is branch. The stubbed list needs an entry at each sort position.
- **1d. Remaining cost: CONFIRMED, with one addition.** The fabricated sort_values subsection must be keyed by the SortField name, because the assertion at 1393 to 1394 checks it, and Gradle test runs enable assertions by default.
- **1e. Mechanism of the count change: FINDING (wrong; must be corrected).** The response passages say sort values change `numFound` "through the queue push on overflow". The evidence says otherwise. `ShardDocQueue.push` (`QueryComponent.java` 928 to 945; the same on main at 930 to 944) returns false only when `shardDoc.id` is already in the unique set (936). The non-duplicate path calls `insertWithOverflow` (944) and returns true (945), so overflow never reaches `numFound`. The decrement at 1233 (main 1225) fires on a document id already pushed, from any shard. Sort values do not feed that check. **Passages to correct:** `reports/mergeids-fix-review-response.md` line 25; `reports/mergeids-fix-review-round2-response.md` line 11; `reports/mergeids-fix-review-round3-response.md` line 9. The round 4 assignment's item 1 repeats the premise and needs the same correction.
- **1f. "Sort values feed only the unmarshal and the queue push": FINDING (incomplete).** Sort values also feed the skip gate at 1180 (`sortFieldValues.size() == 0` with `needsUnmarshalling`), which decides whether a partial shard's totals fold at all (the fold starts at 1188). The existing skip tests pin that gate, for example `excludesPartialShardResultWhenUsingNonScoreOrDocSortField` and `skippedPartialShardDocsAreAbsent`. The declined case (sort_values present on a merged partial shard) takes the non-skipped path, so the decline itself stands. **Suggested scope sentence for the three records:** "The decline covers the totals block only. Sort values gate the skip at QueryComponent.java 1180, which the existing tests pin; the declined case, sort_values on a merged partial shard, is not skipped, and its totals do not depend on sort values. numFound is decremented when the queue push returns false, which happens only for a document id already pushed (930 to 936, 1233). Overflow does not change numFound. No applied test sends a duplicate id."
- **1g. No applied test fills the queue: CONFIRMED on the numbers.** The queue size is `ss.getOffset() + ss.getCount()` (1016), here 0 plus 10. Test shards hold 0, 2 or 3 documents each, at most 5 merged. The decrement path is a duplicate id, and no applied test sends one: `createSolrDocumentList` (test lines 520 to 530) uses a static counter for ids.

## Settlement 2: the Mechanism passage and fix option 1

- **2a. Main's line map: CONFIRMED.** On main: 1160 approximate total, 1162 the fallback that adds the running `numFound`, 1168 to 1170 maxScore, 1171 the numFound fold, 1173 to 1175 the exactness fold, 1193 to 1195 the skip, 1225 `numFound--`. Ticket Mechanism lines 33 to 37 cite these correctly, apart from 2e.
- **2b. Main's all-skipped behaviour: CONFIRMED.** Both folds run before the skip, so an all-skipped merge reports the skipped sum (5 for shards of 2 and 3 exact documents) and keeps the exact flag. Ticket line 41 says so.
- **2c. Fixed behaviour: CONFIRMED.** The skip at 1180 precedes the folds at 1199 to 1203. `hitCountIsExact` starts true (1025). The guard at 1241 to 1243 (`!anyShardMerged && skippedShardReportedHits`) clears it. The zero is inexact only when a skipped shard reported hits.
- **2d. Fix option 1: CONFIRMED.** The reorder alone leaves the flag at its initial true for an all-skipped merge, so the guard is the no-shard-merged case of the second option. The second option clears the flag on any skip; the guard also requires hits, which the ticket's parenthetical states. The wording is accurate.
- **2e. Ticket Mechanism line 39: FINDING (wrong trigger).** `material/mergeids-ticket-draft.md` line 39 says `numFound--` "runs per document when the merge queue rejects one". The push returns false only for an already-pushed id (936; 930 on main), so overflow does not reach 1233. The conclusion, that it never compensates for a skipped shard, still holds. **Suggested wording:** "runs per document when the queue push returns false, which happens only for a document id already pushed (from any shard)."
- **2f. Round 2 response, item 4 (`reports/mergeids-fix-review-round2-response.md` lines 17 to 19): CONFIRMED.** It matches ticket lines 41 and 57.
- **2g. PR draft, first section (`pr-drafts/search-components/mergeids-skipped-shard-totals.md`): CONFIRMED.** Line 17 gives main's all-skipped behaviour. Lines 21 to 23 give the fixed behaviour with the head citations (1180 to 1203, 1189 to 1193, 1238 to 1243), which match. The PR draft carries no `numFound--` mechanism, so 2e does not apply there.

## Settlement 3: arrival-order qualifiers and the Related line

- **3a. QueryComponent fallback values: CONFIRMED.** On the base (`d76fc14b98`), line 1162 adds the running `numFound` before the shard's own count is folded at 1171, inside the loop at 1034. A then B gives 2; B then A gives 3. Ticket draft lines 27 and 49 match.
- **3b. CombinedQueryComponent values: CONFIRMED.** The loop is at 334, `queryNumFound` is folded at 342, the fallback at 384 adds `queryNumFound`, which already includes the current shard, and the header value is at 386. A then B gives running values 2 then 7 (final 7 after the max at 442). B then A gives 3 then 8. The premise report, section 2, and ticket line 63 match.
- **3c. The inverted fold: CONFIRMED.** `CombinedQueryComponent.java` line 343 is `hitCountIsExact = hitCountIsExact && Boolean.FALSE.equals(docs.getNumFoundExact());`. `QueryComponent` 1173 to 1174 is the `if` form, identical on both commits. The premise report, section 3, matches: all-exact input gives false, all-inexact gives true.
- **Related line and qualifiers: CONFIRMED.** Ticket draft lines 27, 49 and 63 carry the arrival order. Line 63 names the pre-skip fold, the fallback double count and the inverted fold. The PR draft carries the qualifier at line 17 and line 42. The round 1 response (Area 4, line 31) gives 342, 343, 384 to 386 and 399 to 400, which match.
- **Label FINDING: `reports/combinedquerycomponent-premise.md` line 12** calls `cabedd1d968` "the merge-base". `git merge-base cabedd1d968 d76fc14b98` returns `cabedd1d968`, so it is an ancestor. The branch's merge-base is `d76fc14b98`, as receipt lines 8 and 44 say. The two component files are identical across both commits, so no value changes. **Relabel** `cabedd1d968` as an older snapshot.

## Settlement 4: gate annotations and the receipt

- **Round 1 response, lines 3 and 53: CONFIRMED.** Both read GREEN, GATE RUNNER DONE 2026-10-11T20:36:48Z, matching receipt line 29. No GATE PENDING remains in the file.
- **Round 2 response, lines 3 and 27: CONFIRMED.** GREEN, 2026-10-11T21:46:01Z, matching receipt line 46.
- **Receipt, lines 42 to 52: CONFIRMED.** 17 tests; the seven premise failures with their contents; head 17 of 17; `:solr:core:check -x test` rc=0. `git diff --stat 73d20a0f939 9e85ce5c162` shows only the test file, 150 insertions, so the production citations hold.
- **PR draft head citations: CONFIRMED** against `9e85ce5c16`: the skip at 1180 to 1185, the accumulation at 1186 to 1203, the fallback at 1189 to 1193 and the zero guard at 1238 to 1243. The base citations at 1160 to 1175 and 1193 to 1195 match `d76fc14b98`.
- **FINDING, PR draft line 4:** "full checks green there" overstates the receipt. Step 5 is `:solr:core:check -x test`, with tests excluded. The only test run recorded is the focused class, 17 of 17 (step 4). **Replace with** that scope.
- **FINDING, PR draft line 44:** the changelog link points at `changelog/unreleased/SOLR-XXXXX.yml` at `9e85ce5c16`. That file is not in the head tree (`git ls-tree -r --name-only 9e85ce5c16 -- changelog/unreleased` lists no such file), and receipt step 0 says the branch has no fragment, which is owed at packaging. **Drop the link, or mark it as created at packaging.**
- **FINDING, stale in our round 3 record, `reports/mergeids-fix-review-round3.md` line 44:** "9e85ce5c162: no receipt entry, and no pending line" and "the round 2 response says the full gate is GATE PENDING" are false now. Receipt lines 42 to 52 hold a GREEN entry, and the round 2 response lines 3 and 27 say GREEN. **Correct the line.**
- **FINDING, stale, same file line 45:** "The four new tests have no receipt entry" is false; receipt lines 45 and 50 record them, including the pass on base production as pins. **Correct the line.**
- **FINDING, dated, `reports/mergeids-fix-review-round2.md` line 10:** "It is GATE PENDING" was true when written and now contradicts receipt line 29. **Add a dated annotation**, as was done for the round 1 response.
- **Note, `reports/mergeids-fix-review-round3.md` line 53, request 5:** the receipt now records the 9e85ce5c16 green state and the four tests. The log reference is a vm1 path, not on disk here. The receipt says the logs live on vm1 by design, so whether that meets "on disk" is for the coordinator to decide, not a contradiction.
- **Resolved, no action:** `reports/mergeids-fix-review-round3.md` line 37 (the stale GATE PENDING finding for `73d20a0f939`) was applied at `reports/mergeids-fix-review-round3-response.md` line 25.
- **Sweep:** no GATE PENDING remains in `receipts/mergeids-fix.md` or the PR draft. Other GATE PENDING hits belong to other tickets and were not checked.

## Corrections needed before SATISFIED

1. **Mechanism of the count change (settlement 1e, 2e):** correct the scope sentence in `reports/mergeids-fix-review-response.md` line 25, `reports/mergeids-fix-review-round2-response.md` line 11, `reports/mergeids-fix-review-round3-response.md` line 9, and the round 4 assignment's item 1 premise; correct `material/mergeids-ticket-draft.md` line 39.
2. **Scope sentence (1f):** add the skip-gate sentence to the same three records.
3. **Label (settlement 3):** relabel `cabedd1d968` in `reports/combinedquerycomponent-premise.md` line 12 as an older snapshot.
4. **PR draft (settlement 4):** correct line 4 ("full checks green") and line 44 (the changelog link).
5. **Our round 3 record (settlement 4):** correct lines 44 and 45 of `reports/mergeids-fix-review-round3.md`; add the dated note to `reports/mergeids-fix-review-round2.md` line 10.

The branch needs no change.

## Claim note

The round 4 claim is on the `pr-prepare` branch (`claims/pool-mergeids-fix-review-round4.md`), not on the fork branch. A check of the fork branch alone would not find it.
