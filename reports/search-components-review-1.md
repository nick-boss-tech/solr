# Search components review, sub-batch 1: SOLR-10492, 10844, 11129, 12556, 15331, 17051, 5394, 6193, 6831

Reviewer: Windows review host (claim `claims/search-components-review.md`), 2026-10-13.
Scope: the nine drafts under `pr-drafts/search-components/` in the first earlier sub-batch.

Method: each draft was read against its receipt. Each receipt head was compared with the live fork tip (`git ls-remote`); all nine agree. Proof counts and dates were compared with the receipts. Each GitHub citation was read at the SHA it names with `git show` (cite-check). Merge-bases and changed files were checked with `git diff --stat`. Uncited sentences were checked against the code where a short read settled them. Public-text lint: no "we", no em or en dashes, and no process words in any of the nine drafts. Nothing was built, no test was run, and nothing was written to GitHub.

## Summary

| Ticket | Verdict | Edits in this tree |
|---|---|---|
| SOLR-10492 | SATISFIED | 1 |
| SOLR-10844 | SATISFIED | none |
| SOLR-11129 | SATISFIED | none |
| SOLR-12556 | SATISFIED | none |
| SOLR-15331 | SATISFIED | none |
| SOLR-17051 | SATISFIED | 1 |
| SOLR-5394 | SATISFIED | none |
| SOLR-6193 | OPEN (gate) | none (scope and branch items, see below) |
| SOLR-6831 | SATISFIED | none |

## SOLR-10492: SATISFIED

- Head `ecf21e2192cb91e1ba4a44abce2c43ab8a5409b9` equals the live tip and the receipt. Counts: TestDistributedGrouping 2, TestGroupingSearch 17, DistributedFacetPivotSmallTest 1, total 20, matching the Proof.
- Base `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`. The branch changes `SimpleFacets.java`, its test and the changelog. `StoredFieldsShardRequestFactory.java` is not changed.
- Cites checked: `SimpleFacets.java` L979 at base (the `numDocs` call), L979-L996 and L1005-L1028 at head, L792-L797 at head. The changelog title is at L1.
- Edit: the sentence that cites `StoredFieldsShardRequestFactory.java` L66 links the merge-base, and the text now says so ("as it does at the base commit"; "this change does not touch that file"). The branch does not touch that file.
- Uncited, not run: "A `group.field` given only as a local parameter ... still returns BAD_REQUEST." The head code reads `group.field` from `rb.req.getParams()`, which is consistent with this, but no run records it. NOTE for the main side: verify or drop before the PR opens.

## SOLR-10844: SATISFIED

- Head `e87515c56d048f96850d4586d6e0508dc177e4c7` equals the live tip and the receipt. `SimpleFacetsTest` 49 tests, 0 failures, 0 errors, 1 skipped, matching the Proof. Verified date 2026-10-05 matches the receipt.
- Base `22a8cfebbbdb`. Cites checked: `SimpleFacets.java` L470-L473 at base (the existing Points 400), L799-L818 at head (the new check), `SimpleFacetsTest.java` L609-L667 at head, changelog L1.
- The check rejects a numeric field that is multi-valued, has doc values, or is a Points field. That is what the draft says. The enum claim ("an enum field with doc values is rejected as well") holds: `EnumFieldType.getNumberType()` returns `NumberType.INTEGER` (head, L495-L496).
- The Proof's base failure ("expected 400 was 500" for Points and Trie with doc values) matches the premise matrix named in the receipt.

## SOLR-11129: SATISFIED

- Head `dc82676b8096ed85cb018d4e5b80f6855e5d26d2` equals the live tip and the receipt. Counts: focused 4 of 4, matching the Proof. The receipt's previous-head proof (`6c1356bdff7`) matches the offset-test description.
- Cites checked: `FacetComponent.java` L1398 (the `wrapDefaults(localParams, ...)` precedence) and L907-L911 (the coordinator's minimum-count drop), changelog L1.
- The draft's Limits says the changelog names facet.mincount only. That matches the receipt's scope note and the changelog title.
- Uncited, not run: "For `facet.zeros=false`, the coordinator now also drops zero-count terms, whether the setting is local or in the request. It did not do that before." The head code at L907-L911 is consistent, but the base behavior is not cited. NOTE for the main side: cite the base line or drop "It did not do that before".

## SOLR-12556: SATISFIED

- Head `033ec65a0e1bc12fd16d65b77a5b19458c837040` equals the live tip and the receipt. `TestJsonFacetRefinement` 13 tests, 0 failures, 0 errors, 1 skipped, matching the Proof.
- Counts in the file: 12 `@Test` methods (`grep -c`), the thirteenth testcase `testIndexAscRefineConsistency` (head L2241) has no annotation, and the skipped test is the `@AwaitsFix` method at L1112. This matches the Proof.
- Base `14c7aac0d151`. Cites checked: `FacetModule.java` L195-L200, `FacetMerger.java` L59-L62, `FacetRequestSortedMerger.java` L160-L180 and L189-L191, `FacetFieldMerger.java` L122-L124, all at head. The base failure (`testProcessEmptyRefinement`, 'Ax' != 'By') matches the receipt.
- The "old rule" link points to L189-L191, the predicate that selects it. The predicate traces; no edit.

## SOLR-15331: SATISFIED

- Head `6769b4cd8a12d8606a1616c6269c2fdb224d019b` equals the live tip and the receipt. `NestableJsonFacetTest` 2 of 2, matching the Proof.
- The Proof says "inconclusive by construction", which matches the receipt's INCONCLUSIVE record. The draft does not claim a base failure.
- Cites checked: base `FacetFieldProcessor.java` L470 and `BucketBasedJsonFacet.java` L73-L77 (the ignored key), head `BucketBasedJsonFacet.java` L74-L75 and L145-L153 (the new read and `getMissingCount()` with `UNSET_FLAG`), `NestableJsonFacetTest.java` L129-L152, changelog L1.
- Limits names SOLR-17051 as the case where the server omits `missing`. That is consistent with the 17051 branch (head `FacetFieldProcessor.java` L534-L542).

## SOLR-17051: SATISFIED

- Head `148544e9ed54a7c6dd6714197d3bf74b06fff1ca` equals the live tip and the receipt. `TestJsonFacets` 30 of 30 and `TestJsonFacetRefinement` 10 tests with 2 skipped, matching the Proof.
- Base `14c7aac0d151`. Cites checked: base `FacetFieldProcessor.java` L470 and `FacetFieldMerger.java` L139-L141; head `FacetFieldProcessor.java` L534-L542, `FacetFieldMerger.java` L139-L145, changelog L1.
- Edit: the draft said the new checks sit "inside `doStats`". Lines 2566-2585 are inside `doStatsTemplated` (head L1718), which `doStats` calls (head L1480). `doStats` is called by the standalone test (L1466) and the distributed test (L1477). The sentence now says so.
- The Proof's `mincount` 4 and `mincount` 5 checks match the test lines.
- The docs claim ("the JSON Facet API reference page does not describe the new rule yet") holds for this branch: the branch diff touches no docs file.
- Not checked: "Sub-facets under the missing bucket are still computed before the bucket is dropped." Plausible from the processor flow, not run. NOTE for the main side.

## SOLR-5394: SATISFIED

- Head `967445622f9294e26e425ec494d37e5ca13d4fe4` equals the live tip and the receipt. `SimpleFacetsTest` 49 tests, 0 failures, 0 errors, 1 skipped, verified 2026-10-07, matching the Proof.
- Base `b6b2b8f10e98`. Cites checked: base `SimpleFacets.java` L183 (`threads = -1`), base `PerSegmentSingleValuedFaceting.java` L128, base `SimpleFacets.java` L885 (the global `facet.threads` that picks the per-field executor), head `SimpleFacets.java` L183 and L208-L211, changelog L1.
- Thread semantics checked at head: `SimpleFacets.java` L593-L594 chooses `directExecutor` when `threads == 0` and calls `ps.setNumThreads(threads)`. So "threads=0 still runs on the calling thread, and threads=N still runs N at a time" holds, and the default of 1 runs segments one at a time on the facet executor. The Limits sentence about the reporter's `facet.threads=0` case holds: the global parameter selects the per-field executor, and the fcs default still uses the pool.
- The failing base output in the Proof ("expected:<1> but was:<-1>") matches the receipt.

## SOLR-6193: OPEN (gate)

The draft is accurate about what the branch does. The problem is the branch's scope against the ticket, and the branch's own changelog and history. The earlier scope audit (`reports/search-components-1-f3.md`) found these, and the branch has not moved since:

- Scope: the ticket is about `facet.field` local params in distributed search. The branch changes distributed `facet.pivot` merging only. The f3 audit lists scope options A (narrow, owner decides), B (keep the ticket open and add a facet.field fix) and C (hold). It wrote the draft to option A but records no recommendation between them. The round 28 and round 36 reviews both give Needs work on scope and record no recommendation either.
- Changelog title (head L2): "Distributed pivot facets now honor facet.limit, facet.sort, facet.offset and facet.pivot.mincount given as local params". The tests do not cover local `facet.offset` or `facet.pivot.mincount` (f3 item 2; the head test file has no such local-param case). The title overclaims.
- Commit history: subjects still carry process words ("add hypothetical-reproduction handoff doc", "remove the hypothetical-reproduction handoff doc"), and `900ca1aa793` has a doubled prefix ("SOLR-6193: SOLR-6193:"). The f3 audit's item 6 asks for a squash and reword.

Decision SC-D1 (recorded in the summary): the scope question has no recommendation on record, so the harder option applies. The facet.field example gets an in-branch fix. Its expected value is first confirmed by a run (SC-G1), because the code reading (single-node returns 20 for the ticket example) and the ticket's expectation (10) disagree and the run has not been made.

Gate requests (for the gate coordinator to stock; not posted here):

- SC-G1 (information only, no code change). Run the ticket's `facet.field={!key myblah facet.offset=10}blah&f.blah.facet.offset=20` case on single-node and distributed, at base `c3cdf7b46e8` and at head `ec94bf50c80`. Record the value returned for each. The ticket says single-node returns 10.
- SC-G2 (branch change, gated). On `solr-6193-submit`: narrow the changelog title to the local params the tests cover ("Distributed pivot facets now use facet.limit and facet.sort given as local params of facet.pivot when merging shard responses."), and rewrite the four fork commits so no subject carries process words and no prefix is doubled. Force-push on the fork submit branch is allowed under the standing direction, pinned with `push-fork-lease.ps1`. Gate at the rewritten head.
- SC-G3 (branch change, gated, after SC-G1). The facet.field fix in this branch, with a test that fails at base `c3cdf7b46e8`. Shape depends on SC-G1: if single-node returns 10, the distributed path is changed to match single-node; if single-node returns 20, see the owner item.

Owner item (conditional): if SC-G1 shows single-node returns 20 today, the ticket's expected value contradicts current single-node precedence. Fixing it changes single-node results, and SOLR-11129 shares the same precedence rule (f3 owner call 2). That is a change of product behavior, not effort, so it comes back as an owner item rather than being decided here. The draft's Choice section already puts this question to the owner.

The draft's "What happens today" paragraph leads with a pivot example as the ticket's symptom. Once the facet.field case is fixed, that paragraph is rewritten to the ticket's own example. No text change is made now.

## SOLR-6831: SATISFIED

- Head `96b33ba5f6f834038e570d0b93f8684dc887fbe8` equals the live tip and the receipt. `TestQueryLimits` 4 of 4 at the head, and 4 tests with 1 failure at base, matching the Proof.
- Base `97d973814336`. Cites checked: base `PivotFacetProcessor.java` L328-L345 (the per-value subset loop), head L328-L333 (the limit check), test L135-L188 (with `CallerSpecificQueryLimit`), changelog L1.
- Uncited, plausible, not run: "the check covers every limit the shared code knows, so `memAllowed` is included", and "the check runs at every level". NOTE for the main side: cite `QueryLimits` or drop the `memAllowed` sentence before the PR opens.

## Not checked

- Jira text for every ticket. No Jira packet was opened. The statements that rest on tickets (for example SOLR-5394's "2014 comment" and SOLR-6193's "46 facet executor threads") are unverified.
- Gate logs. Not on disk here, so counts are taken as the receipts record them.
- Full test bodies. Only the cited blocks were read.
- The uncited sentences listed above, and the other sentences that have no citation.
- No builds, no tests, no gate runs, no writes to GitHub.
