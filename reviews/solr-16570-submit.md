# solr-16570-submit

- Branch: origin/solr-16570-submit
- Head: 974c44f9608 (listed head 5676646936d5, plus one review patch commit)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: the author's 3 commits plus 1 review patch. `solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java` (+4: return the slow reader when there is nothing to uninvert), `solr/core/src/test/org/apache/solr/search/TestCollapseQParserPlugin.java` (+28, then the patch: `testTopFcHintOnDocValuesWithoutUninversion`), `solr/core/src/test-files/solr/collection1/conf/schema11.xml` (+1: `*_s_dv_not_uninvert`), the changelog fragment, and `SOLR-16570-TESTING.md` (kept in place).
- Verdict: Nearly
- Patch: 974c44f9608, `SOLR-16570: count only main results in expand assertion (//doc also matched the expanded doc)`
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle, no spotless, no test runs. Every claim below was checked by reading code.

## Premise check (hypothetical-reproduction handoff)

- VERIFIED: base `getTopFieldCacheReader` (`CollapsingQParserPlugin.java:610-628` on base) passes `Map.of(collapseField, type)::get`, and `type` stays null unless the field is `indexed && uninvertible`. `Map.of` rejects null values, so a docValues-only string field throws NPE.
- VERIFIED: the request-level check (`CollapsingQParserPlugin.java:354-362`) accepts a field that has docValues but is not uninvertible. So the NPE is reachable from a valid request with `hint=top_fc`.
- VERIFIED: `ExpandComponent` calls `getTopFieldCacheReader` on the same top_fc path (`ExpandComponent.java:218-219` and `:402-403`). The fix covers both call sites.
- VERIFIED: the default, non-hint path already reads doc values directly with `DocValues.getSorted(searcher.getSlowAtomicReader(), ...)` (`CollapsingQParserPlugin.java:591`). Returning the slow reader for top_fc matches that existing path.
- VERIFIED: a field with neither docValues nor uninvertible is rejected before this code runs (`:354-362`). So the `type == null` branch does not turn an invalid request into silently empty results. Its remaining cases are docValues fields with `indexed=false` or `uninvertible=false`, and both have doc values to read.

## Findings (ranked)

MEDIUM (verified, patched in 974c44f9608): The expand assertion was wrong. It used `*[count(//doc)=2]`, and `//doc` also matches the document inside `lst[@name='expanded']`. In the expand case the count is 3, so the assertion would fail even with the fix. The patch changes it to `*[count(/response/result/doc)=2]`, the form `TestExpandComponent` uses for main results (`TestExpandComponent.java:206`). The single-line change is the only edit in the patch.

LOW (verified, not patched): `TestCollapseQParserPlugin.java:1038` (the `req(...)` line in the first `assertQ`) is longer than 100 columns. spotless reflows it when the Linux gate runs, so this is formatting only.

## Verified correct (by reading; not run)

- The test expectations. Group `a` holds docs 1 (`test_i`=5) and 2 (`test_i`=10), so the head is doc 2. Group `b` holds doc 3. Sorted by id, the main results are [2, 3], and the expanded group `a` contains doc 1. The first iteration (no hint) passes on base. The `hint=top_fc` iteration fails on base with NPE, which the `assertQ` reports as a 500. So the test fails before the fix and passes after, by reading.
- The schema field. `*_s_dv_not_uninvert` matches `grp_s_dv_not_uninvert`. It does not collide with `*_s_dv` because the suffix differs. The test class loads `schema11.xml` (`TestCollapseQParserPlugin.java:47`).
- The changelog fragment has the right format, `type: fixed`, and the ICLA author.
- All commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions

None. The premise holds, the fix is small, and the change does not alter behavior for fields that already worked (uninvertible fields keep the uninvert path).

## Not checked

- Nothing was compiled or run. The Linux gate is the next check.
- Whether the `ExpandComponent` field argument always passes the same request-level check before expand runs. The TESTING doc's second risky guess is the expand config registration (`solrconfig-collapseqparser.xml`), which was not checked.
- Lucene's `SlowCompositeReaderWrapper.getSortedDocValues` behavior was not traced beyond the existing default-path use at `CollapsingQParserPlugin.java:591`.
- Upstream conflicts were not checked. The branch is 16 commits behind `upstream/main`.
- The `schema11.xml` consumers were checked only by suffix collision, not by running them.
