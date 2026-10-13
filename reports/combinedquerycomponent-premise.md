# Report: CombinedQueryComponent totals premise

Verdicts, one per defect (they do not all have to agree):

- **Skip ordering: CONFIRMED BY CODE.** The per-query totals are folded before the partial skip. Not confirmed by run.
- **Fallback: CONFIRMED BY CODE.** The approximate fallback adds the running total, which already includes the current shard. Not confirmed by run.
- **Exactness fold: CONFIRMED BY CODE, inverted.** It is a third defect in the same lines. Not confirmed by run.
- **Maximum fold: not a defect in the code's own terms.** Whether a maximum is the intended combined count is an open semantics question for Nick.

CONFIRMED BY RUN: none. No run exists for this component. The run spec below is for a Linux host and was not executed here.

Ref read: main `d76fc14b9857e77b830d7a82514b28c523a239c2`. The older main snapshot `cabedd1d968059215188f4e7563fb303241899ed` (an ancestor of the branch base, not the merge-base) and the fixed branch head `73d20a0f939f0ddbaea89a9bd590ce9527f7dd29` have the identical `CombinedQueryComponent.java`, so the verdicts hold on both sides.

## Evidence

File: `solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java`, method `mergeIds`. Lines below are at main.

**Structure.** `hitCountIsExact` is declared once, outside the query loop (315). The outer loop runs over `queriesToCombineKeys` (328, closed at 443). The inner loop runs over `sreq.responses` (334, closed at 437). Per-query totals reset at 329 to 331. `numFound`, `hitCountIsExact`, `thereArePartialResults` and `approximateTotalHits` are shared across queries.

**Order of the folds and the skip.**
- 342: `queryNumFound += docs.getNumFound()`.
- 343: `hitCountIsExact = hitCountIsExact && Boolean.FALSE.equals(docs.getNumFoundExact());`
- 348 to 350: the exception continue, which comes after the reads and folds above.
- 359 to 364: `thisResponseIsPartial` from the response header.
- 381 to 387: the approximate block. 384 is the fallback, `queryApproximateTotalHits += queryNumFound`. 386 is the header value.
- 390 to 396: the `sort_values_<q>` lookup.
- 398: `needsUnmarshalling = ss.includesNonScoreOrDocField()`.
- 399 to 400: `if (thisResponseIsPartial && sortFieldValues.size() == 0 && needsUnmarshalling) continue;`
- 441 and 442: `numFound = max(...)` and `approximateTotalHits = max(...)`.
- 449: `responseDocs.setNumFoundExact(hitCountIsExact)`.

So a skipped shard has already contributed its numFound (342), its exactness (343) and its approximate count (384 or 386) to the per-query totals before the continue at 400.

**Exactness fold (343).** The written form is `hitCountIsExact && Boolean.FALSE.equals(...)`. The flag stays true only while shards report FALSE, which is the inverse of the meaning the flag carries when written at 449. `SolrDocumentList.numFoundExact` defaults to TRUE (`solrj/src/java/org/apache/solr/common/SolrDocumentList.java` line 50), and a shard writes TRUE when its count is exact (`JavaBinResponseWriter` line 150). So the merged flag is wrong for all-exact input (it reports FALSE) and for all-non-exact input (it reports TRUE). Only mixed input happens to come out right. The `QueryComponent` form is `if (hitCountIsExact && Boolean.FALSE.equals(...)) hitCountIsExact = false;` (1173 to 1174 at main). I spot-checked both lines at main.

**Fallback (384).** The fallback adds `queryNumFound`, which already includes the current shard, because 342 ran earlier in the same iteration. So the fallback adds the running total over all shards so far, headered or not. The formula is in section 2.

**Exception path.** An exception response never yields a per-query list. `HttpShardHandler` sets the response only on success (250), and sets the exception at 231, 306 and 322. Without a response, `SolrResponseUtil` line 40 throws a null pointer exception, which is caught and returns null only when `shards.tolerant` is set (line 68). So the accumulation before the exception check (348) is not reachable on the paths traced. One route exists where a response is parsed and then throws in 304 to 314, but the only code that can throw there is not on the base paths traced.

**Where the per-query sort values come from.** Empty sort values come from the timeAllowed exit path (`QueryComponent` line 574, reached from `doFieldSortValues` at line 210). A shard's `sort_values_<q>` is absent or empty only under that path.

### 1. Skip ordering: CONFIRMED BY CODE

**Trigger, all required:** `thisResponseIsPartial` (359 to 364), `sort_values_<q>` empty or absent (392 to 396), and `needsUnmarshalling` true (398).

**Totals affected:** numFound and exactness (342 and 343), and the approximate count (384 or 386). Those reach the response only through the maximum at 441 and 442, so a skipped shard shows only when its query's inflated total is the largest. The exactness flag is global, so a skipped shard's flag always counts.

**Same SOLR-14758 shape as `QueryComponent`?** The skip has the same condition and the same continue. But it is evaluated per (query, shard): a shard can be skipped for one query and counted for another. The exception check here (348) also follows the accumulation, while `QueryComponent`'s precedes it. That second difference is the parse-then-throw route above, which the base paths do not trigger.

### 2. Fallback: CONFIRMED BY CODE

Let N_j be the shard j's count when its per-query entry was read, and 0 otherwise. Let h_k be the header value when present. Let p(j) be the arrival position in `sreq.responses`, which is completion order (`HttpShardHandler` line 403, no sort found). Let H be the set of shards that carry the header.

- Approximate total for a query: the sum of h_k over k in H, plus the sum over k not in H of P_k, where P_k is the sum of N_j over j with p(j) at or before p(k).
- Overshoot: sum over j of N_j times the number of header-less shards k with p(k) > p(j).
- Headerless shards only, n shards: approximate = sum of N_j times (n − p(j) + 1); overshoot = sum of N_j times (n − p(j)). For n equal counts x, approximate = x·n(n+1)/2 against the correct n·x. For n = 3, x = 1: approximate 6, correct 3.
- Worked example: arrival A(2) then B(3). Approximate 2 + 5 = 7, correct 5, overshoot 2. Arrival B(3) then A(2) gives 8. So the base value depends on arrival order; the assignment's 7 is the A-then-B value.
- A headered shard's h_k is added once per query, so it enters every query's total, and the maximum then picks one.
- The fallback fires for any shard whose response lacks the header, partial or not, and it fires before the skip. So a partial shard with no per-query list and no header still adds the running total.
- Mixed headers: a headered shard i contributes h_i once, and its N_i still enters every later headerless prefix. Example: header on shard 1, headerless shard 2: base h1 + N1 + N2, correct h1 + N2, error N1.

Whether headers appear in practice was not traced, so the frequency of the mixed case is not established.

### 3. Exactness fold: CONFIRMED BY CODE, inverted

The stated semantics of the combined response are: exact if and only if every contributing list is exact. That needs the AND of "flag is TRUE" over the lists. The code computes the AND of "flag is FALSE", starting from TRUE (315). These agree only for mixed lists (both FALSE) or for no folded lists (both TRUE). On uniform input they disagree. No reading makes the code sound: the variable is initialised to TRUE and written as the response's exactness flag at 449.

Test coverage: none of `CombinedQueryComponentTest`, `DistributedCombinedQueryComponentTest`, `CombinedQuerySolrCloudTest`, `CombinedQuerySearchHandlerTest` or the `combine/` tests asserts `numFoundExact` or `approximateTotalHits`.

### 4. Maximum fold (441 and 442): not a defect in the code's own terms

- A skip error in query q passes through unchanged if q's total is the largest, and is masked otherwise. It is never amplified.
- A fallback error in query q passes through only if that query wins the maximum. The weighting inside a query comes from the fallback, not from the maximum.
- An exactness error is not touched by the maximum, because the flag is shared across queries (315, 449).
- Intended semantics: the class javadoc (about lines 67 to 76) describes hybrid search that fuses per-query result sets (for example, RRF). The numFound of a fused result should be the size of the union of the matches. The union is at least the largest single query's match count, and at most the sum. The maximum equals the union only when the match sets nest. The reference guide (`json-combined-query-dsl.adoc`) defines no numFound for combined queries. Whether the maximum is the owner's intended count is a semantics decision for Nick, not something this trace settles.

### Adjacent items, not verdict-bearing (NEEDS RUN)

- **Dedup decrement leaks through the maximum.** `numFound--` at 433 to 434 runs on the combined running max, not on the query's own total. Example: query 0 has a sum of 10 with no duplicates; query 1 has shard A (numFound 2, ids 1 and 2) and shard B (numFound 1, id 1). The base gives 9, and the expected value under the component's own maximum rule is 10. Not run.
- **Null docs at 411.** The loop over `docs.size()` has no null guard. `docs` is null when a partial shard has no per-query entry, or when `shards.tolerant` is set. Under a score-only sort the path reaches line 411. Not run, and whether a real shard produces that shape was not traced.
- **Unguarded `docList.get(queryIndex)` at 339 to 340.** There is no size check. Not run.

## Comparison with `QueryComponent.mergeIds`

- **Pre-fix, on the merge-base.** The fallback at 1162 adds the running sum before the current shard is folded, which gives 2 where 5 is correct (error = the sum over headerless k of (sum of N_i before k, minus N_k)). Here, at 384, the fallback adds the running total including the current shard, which gives 7 where 5 is correct. These are opposite error directions with the same line shape.
- **As fixed on `mergeids-skipped-shard-totals` (head `73d20a0f939`).** The skip sits at 1180, decided only from the partial flag, sort values and `needsUnmarshalling`, and placed above the totals. The fallback at 1190 adds `docs.getNumFound()`. The exactness fold at 1201 to 1203 is the correct `if` form. The zero-count rule at 1242 has no per-query equivalent here.
- **Is the same reorder shape available?** Yes. The skip inputs (353 to 364, 390 to 393, 398) depend on no total and no cross-shard state. So the skip can be decided inside each (query, shard) iteration before the folds.

### Fix spec (spec only, not implemented)

1. **Move the blocks.** Move the folds at 342 and 343, and the approximate block at 381 to 387, to after the skip at 399. Then the skip decides for both. (Range corrected from 383 to 387 by the main-side adjudication of the ticket review, 2026-10-11: lines 381 and 382 declare the ath variable, so the block the fix must move starts at 381. The Evidence section above already stated 381 to 387.)
2. **Change the fallback operand.** The fallback must use the shard's own count, `docs.getNumFound()`, not the running total. This is required, not optional: a reorder alone leaves the double count. `docs` can be null, so the fallback needs a null guard that gives 0.
3. **Exactness fold.** Replace the write at 343 with `hitCountIsExact = hitCountIsExact && !Boolean.FALSE.equals(docs.getNumFoundExact());`, which is equivalent to the `QueryComponent` `if` form.
4. **Per-query zero-count rule (optional).** The fixed `QueryComponent` marks the merged response inexact when no shard was merged and a skipped shard reported hits (1242). A per-query form needs an any-merged flag and a skipped-reported flag per query, plus a decision on whether a skipped query marks the shared flag inexact.
5. **Null guard at 411.** A guard for null `docs` is needed if the adjacent item is confirmed.

Behaviour change: the numbers of a combined response change only where a skipped partial shard or a headerless shard is involved. Shards that are complete and carry headers do not change, except through the exactness fix, which changes the all-exact and all-non-exact cases.

### Confirmation run spec (for a Linux host; not run)

Existing classes do not fabricate shard responses. `CombinedQueryComponentTest` and `DistributedCombinedQueryComponentTest` extend `BaseDistributedSearchTestCase` with real shards; `CombinedQuerySolrCloudTest` runs a cluster; `CombinedQuerySearchHandlerTest` runs a handler. None constructs `ShardResponse`, `NamedList` or `SolrDocumentList` shard responses. The closest analogue is `QueryComponentPartialResultsTest`, but it builds only `response` and `responseHeader`, and it mocks the request parameters so `COMBINER_QUERY` is null.

**Smallest new test shape:**
- Module and selector: `:solr:core`, a new class `org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest` in `solr/core/src/test/org/apache/solr/handler/component/`, extending `SolrTestCaseJ4`, in the same package so it can call the protected `mergeIds`.
- Command: `./gradlew :solr:core:test --tests "org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest" -Ptests.seed=C0FFEE42C0FFEE42`, plus `:solr:core:spotlessJavaCheck`. Whether the runner accepts a 16-digit seed is unverified; the default format is 8 digits, so `C0FFEE42` is the fallback.
- Setup: `initCore("solrconfig.xml", "schema-vector-catchall.xml")`, as `CombinedQueryComponentTest` does at lines 54 to 55. Build `CombinedQueryComponent cqc`, call `cqc.init(new NamedList<>())` and `cqc.inform(h.getCore())`, which installs the RRF combiner. Use a request with one `CombinerParams.COMBINER_QUERY` key and a `ResponseBuilder(req, new SolrQueryResponse(), List.of(cqc))`. Score sort for F and X; `intDefault asc` for P.
- Fabricated shard responses: a `ShardRequest` with `responses` set and `purpose` set to `PURPOSE_GET_TOP_IDS`. Each `ShardResponse` has `setShard(name)` and a `SolrResponse` whose `getResponse()` returns a NamedList with `responseHeader` (optional `partialResults`, `maxHitsTerminatedEarly`, `approximateTotalHits`), `response` (the shard's documents), `response_per_query` (a list with one `SolrDocumentList`, carrying numFound and an explicit `numFoundExact`), and, for P, `sort_values_0` (omitted on the partial shard).
- Doc ids are unique across shards in every case, so the dedup decrement is not exercised.

**Cases and expected values:**
- **F, fallback, score sort, one query, no partials.** Shard 1: N=2 with two documents, header `maxHitsTerminatedEarly=TRUE`, no approximate header. Shard 2: N=3 with three documents, empty header. Assert `numFound` = 5 and header `approximateTotalHits` = 5. On unmodified main: numFound 5 (passes), approximateTotalHits 7 (fails). **Correction to the assignment's wording:** the approximate value is written to the header only when some shard reports `maxHitsTerminatedEarly`, so the case must set that flag. "No headers" must read "no approximateTotalHits header".
- **P, partial skip, sort `intDefault asc`, one query.** Shard 1: `partialResults=TRUE`, `maxHitsTerminatedEarly=TRUE`, `approximateTotalHits=10`, N=4 with two documents, no `sort_values_0`. Shard 2: complete, `approximateTotalHits=5`, N=3 with three documents, `sort_values_0` with three values. Assert `numFound` = 3, `approximateTotalHits` = 5, response size 3. On main: numFound 7 (fails), approximateTotalHits 15 (fails), size 3 (passes). Once fixed: 3, 5, 3. If a fix also gates the max-hits flag behind the skip, the header disappears in P and the assertion should expect null. This case tests the condition directly; it is not a traced real shard path. In a real timeout, `doFieldSortValues` skips both the sort values and the per-query add, which is a different shape.
- **X, exactness, required because the fold is inverted.** Score sort, two shards with N=2 and N=3, explicit `numFoundExact` TRUE on both per-query lists, no headers. Assert `getNumFoundExact()` is TRUE. On main: FALSE (fails). Companion case with both FALSE: expected FALSE; main gives TRUE.

## Scope recommendation (Nick decides)

- The skip ordering, the fallback and the exactness fold are three defects in the same method and the same totals. The mergeIds ticket's Related line already promises this as a follow-up.
- Recommend one follow-up ticket covering the three CombinedQueryComponent defects, filed after the mergeIds ticket is filed. The exactness fold is a different kind of defect from the other two (a flag inversion, not an order or operand error) and could be split if Nick wants a one-concern change.
- The maximum semantics question should be settled before the fix, because the fix's numbers depend on whether the maximum is the intended count.
- The fix is per-query restructuring, so it needs its own analysis and gate, not a ride-along on the mergeIds branch.

## Paths checked that did not change a verdict

- `CombinedQueryComponent` lines 176 to 232 and 456 to 560.
- `QueryComponent` lines 570 to 576, 1098 to 1200, 1224 to 1225 and 1295.
- `HttpShardHandler` lines 220 to 250, 280 to 330, 354 and 395 to 410.
- `SolrResponseUtil` lines 31 to 70.
- `SolrDocumentList` lines 25 to 60.
- `JavaBinCodec` lines 738 to 765, and `ResponseCanonicalizer` lines 115 to 125.
- `ResponseBuilder` lines 496 to 510.
- `json-combined-query-dsl.adoc` (no numFound definition).
- Diffs for this file between cabedd1d968, d76fc14b98 and the fixed branch (73d20a0f939): empty.

## Not verified

- The run itself, for all three defects and for the adjacent items.
- Whether a real shard produces the null-docs shape or the parse-then-throw route.
- How often headers appear in practice, so the frequency of the mixed-header case.
- The seed format accepted by the runner.
- The Mockito stub details and the exact `intDefault` value shape for `unmarshalSortValues`, to be checked when the test is written.
- `populateNextCursorMarkFromMergedShards`, assumed a no-op without a cursor mark.

---

## Main-side run addendum (2026-10-11): all three defects CONFIRMED BY RUN

The run spec above was executed on vm1 against unmodified main `d76fc14b9857e77b830d7a82514b28c523a239c2`, seed `C0FFEE42C0FFEE42`, with the probe class `org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest` (kept in the vm1 worktree `~/workspace/solr-worktrees/cqc-premise`). Final run: 7 tests, 7 failures, 0 errors, every failure an assertion carrying the predicted wrong value. Evidence: log `~/workspace/tools/gcqc-premise3.log`; fresh JUnit XML copied to `~/workspace/tools/gcqc-premise-TEST-org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest.xml`.

Per defect:

- **Skip ordering: CONFIRMED BY RUN.** Case P (partial shard, header approximateTotalHits 10, numFound 4, no sort_values_0; complete shard header 5, numFound 3): merged numFound expected 3, was **7**. Case P2 (same setup, approximate assertion): merged approximateTotalHits expected 5, was **15** (the skipped shard's 10 plus the merged shard's 5). The response-size assertion (3) sits after the totals assertions in both cases and was not reached; it is not needed for this verdict.
- **Fallback: CONFIRMED BY RUN.** Case F (two complete shards, numFound 2 and 3, no approximateTotalHits headers, first shard flags maxHitsTerminatedEarly): merged numFound 5 (passed), merged approximateTotalHits expected 5, was **7** (2, then 2 + 3 folded again as the running total).
- **Exactness fold: CONFIRMED BY RUN, inverted, both directions.** Case X1 (both per-query lists numFoundExact TRUE): merged flag expected TRUE, was **FALSE**. Case X2 (both FALSE): expected FALSE, was **TRUE**.

**Maximum fold evidence (question left open by the report; not decided here).** Two extra cases ran the fallback scenario under two combiner queries, so each query's per-query total is wrong by a known amount, and the merged value was observed under both winners:
- M1: q0 totals (correct 5, base 7), q1 totals (correct 11, base 21). Merged approximateTotalHits on base was **21**: exactly q1's own inflated per-query total. Merged numFound 11, as the maximum rule gives on both base and fixed code.
- M2: q0 as in M1 (correct 5, base 7), q1 small (correct 2, base 3). Merged approximateTotalHits on base was **7**: exactly q0's inflated total.
So in the run, the maximum fold **passes the winning query's error through unchanged, masks the losing query's error entirely, and never amplifies**: the merged value never exceeded the largest per-query wrong total, and was never a sum or product of the two errors. That matches the report's section 4 trace. Whether the maximum is the intended combined count stays open for Nick, as the report left it.

**Harness adaptations from the spec, recorded:** (1) the response header map must be installed on the SolrQueryResponse (`addResponseHeader`) before mergeIds runs, as the request pipeline does; the spec's shape left it null. (2) `sort_values_0` is keyed by field name with the per-doc values in doc order (the unmarshal assert requires the field name), not keyed by doc id. (3) A real SortSpec replaces the mocked one (the mock's schema-fields stub cannot serve the merged shard's unmarshal), with a null schema-field slot for the score sort, as production builds it. (4) The probe closes its request after each test. No adaptation changed a case's setup numbers or expected values.

---

## Real-exit addendum (2026-10-13): what a real timeAllowed exit does, settled by run

The real-exit job (gates/combinedquerycomponent-real-exit-premise.md) ran on vm1 against unmodified main `d76fc14b9857e77b830d7a82514b28c523a239c2`, seed `C0FFEE42C0FFEE42`, probe class `org.apache.solr.handler.component.CombinedQueryComponentRealExitPremiseTest` (worktree `~/workspace/solr-worktrees/cqc-real-exit`). Unlike the fabricated cases above, every shard shape came from the shard-side combined process itself (CombinedQuerySearchHandler with distrib=false, isShard=true, a real core, the deadline expiring through the ExitableDirectoryReader path), captured exactly as produced and driven unedited through `CombinedQueryComponent.mergeIds` alongside a complete second shard. Corpus: shard A 20,000 docs (q0 matches 20,000; q1 matches 6,667), shard B 1,500 docs (500). Controls: B-only merge numFound 1,500; A+B merge numFound 21,500, numFoundExact false, no merged approximateTotalHits. Operative run: 1 test, 1 failure, 0 errors, the failure being the Q3 assertion carrying its observed values. Log `~/workspace/tools/gcqc-real-exit.log`; JUnit XML in the worktree under `solr/core/build/test-results/test/`.

- **Q1, exit on query index 0 (field sort, timeAllowed 180): no total is inflated. CONFIRMED BY RUN.** The produced shape carries 0 per-query entries, no sort_values keys, partialResults set, a combined response with numFound 0, and a header with no approximateTotalHits. Its merge with the complete shard completed with numFound 1,500, exactly the complete shard's own fold, and numFoundExact unchanged: the exiting shard's numFound and exactness are never folded, as the code reading said. No merged approximateTotalHits was emitted, exactly as in the control, because mergeIds writes that header only on the maxHitsTerminatedEarly path and no header set it; the reading's header-add case did not arise, since the produced exit header carried no approximateTotalHits value.
- **Q2, exit above query index 0 (field sort, timeAllowed 275): the merge fails. CONFIRMED BY RUN.** The produced shape carries 1 per-query entry (q0 numFound 20,000) and sort_values_0 only, partialResults set. Its merge with the complete shard threw `IndexOutOfBoundsException: Index 1 out of bounds for length 1`, the unchecked `docList.get(queryIndex)` at lines 339 to 340. No skip, no fold for the missing entry.
- **Q3, score-only sort: the predicted NullPointerException was NOT produced, and the shape it needs was not forced.** The score-sort ladder (300 down to 5 ms) produced either complete responses or, at 5 ms, a partial response still carrying both per-query entries and the full combined response; that shape's merge completed with numFound 21,500 and no exception. The exit landed after both sub-queries' entries existed, not inside the first query's search, so the no-entry shape behind the line 411 prediction did not occur at any ladder step. The prediction is neither confirmed nor refuted by this run.
