# Search components review, sub-batch 4: SOLR-10694, 11310, 12044, 14381, 14931, 15319, 15479, 15895, 16444, 17155, 17791, 6207, 7520

Reviewer: Windows review host (claim `claims/search-components-review.md`), 2026-10-13.
Scope: the thirteen drafts under `pr-drafts/search-components/` in the fourth earlier sub-batch.

Method: the same as the other sub-batches. Receipt heads were compared with the live fork tips; all thirteen agree. Counts and dates were compared with the receipts. Citations were read at the SHA they name (cite-check). The 15479 call site was read to settle a Javadoc term. The 17791 "four new tests" claim was checked against the diff between the merge-base and the head. Public-text lint: no "we", no em or en dashes, and no process words in the thirteen drafts. Nothing was built, no test was run, and nothing was written to GitHub.

## Summary

| Ticket | Verdict | Edits in this tree |
|---|---|---|
| SOLR-10694 | SATISFIED | none |
| SOLR-11310 | SATISFIED | none |
| SOLR-12044 | SATISFIED | none |
| SOLR-14381 | SATISFIED | none |
| SOLR-14931 | SATISFIED | none |
| SOLR-15319 | SATISFIED | none (landing dependency already in Limits) |
| SOLR-15479 | SATISFIED | 1 (Limits: all-matches maxScore as a named follow-up) |
| SOLR-15895 | SATISFIED | none |
| SOLR-16444 | SATISFIED | none |
| SOLR-17155 | SATISFIED | none |
| SOLR-17791 | SATISFIED | none |
| SOLR-6207 | SATISFIED | none |
| SOLR-7520 | SATISFIED | none |

## SOLR-10694: SATISFIED

- Head `64e86811548ba1a9dbc5b2ce2ec9c33983f63c15` equals the live tip and the receipt. `TestCSVResponseWriter` 4 of 4, matching the Proof. The receipt's premise (exactly one failure of four on base) matches the draft's "one of the four tests fails".
- Base `cabedd1d968`. Cites checked: base `TabularResponseWriter.java` L121, L136, L139 and L142 (the empty writers for these value types, which is why the cell prints nothing); head `CSVResponseWriter.java` L418, L436, L441 (the call sites) and L454-L465 (`writeCellVal`), test L359-L397, changelog L1-L8.
- The draft's description of the new method matches the code: maps, `MapWriter`, `IteratorWriter`, iterables (not `Path`), iterators and arrays go to `Utils.toJSONString`, and everything else goes to `writeVal`.

## SOLR-11310: SATISFIED

- Head `e38ddec5279c465f65a2fa2de22ee9c92e0f5e31` equals the live tip and the receipt. `TestLTRReRankingPipeline` 4 of 4 and `TestReRankQParserPlugin` 11 of 11, matching the Proof. The new test's base result (`expected:<[1]> but was:<[0]>`) matches the receipt's round 35 A/B record.
- Base `ReRankCollector.java` L327-L334 (the loop that stops at the first document that is not elevated) and base `LTRRescorer.java` L146-L148 (the sort by document id before the loop). Head `ReRankCollector.java` L324-L333 (the loop with no stop). Changelog L1.
- The Choice (boost every elevated document, or only a leading run when the sort is not a score sort) is settled under the decision default. Option 1 is the only one that fixes the LTR symptom for every sort, and option 2 leaves field-sorted LTR broken. The record's owner item ("confirm the position before opening") is a confirmation at PR-open time, and the draft already poses it as the Choice.

## SOLR-12044: SATISFIED

- Head `c92bbf9349b12e96918646a73bfaa36248b3d523` equals the live tip and the receipt. `TestIndexSearcher` 6 of 6, with the new test as the one base failure, matching the Proof.
- Base cites: `SolrIndexSearcher.java` L1499 (the intersection on the cached path) and `DocSetUtil.java` L123-L124 (the combine on the uncached path). Head: `SolrIndexSearcher.java` L1484-L1487 (the match-all shortcut, which returns the filter or the live docs), `TestIndexSearcher.java` L172-L197 (the new test, which covers the cached and the `WrappedQuery` uncached paths), changelog L1-L9.
- The Choice (trust the filter to hold only live documents, or intersect with live docs) is settled the same way as 11310: option 2 keeps the same intersection, which is the work this ticket is about, so option 1 is the one that addresses the symptom. The Limits say no test covers a filter with a deleted document, which is accurate.

## SOLR-14381: SATISFIED

- Head `a28b3f672cb4daa7cc32833082b0f499f41e3a85` equals the live tip and the receipt. `TopGroupsResultTransformerCompatTest` 2 of 2 and `QueryResponseSectionParityTest` 9 of 9, verified 2026-10-07, matching the Proof.
- The base-production claim (both compat methods fail with `ClassCastException: Long cannot be cast to Integer`) matches the receipt's premise run.
- The earlier head `dd8d16441dc` counts (`GroupingWideCountsTest` 2, `TestGroupingSearch` 17, `TestMissingGroups` 1, verified 2026-10-03) match the receipt. The draft says those three counts come from the earlier head, which is correct.
- Cites checked: head `TopGroupsResultTransformer.java` L340-L346 (`boxCount`: an Integer when the value fits, a Long above the range), changelog L1.
- The Choice (change the SolrJ return types, or add accessors) is settled under the default. Changing the types fixes the truncation at its source, and adding accessors leaves the old getters returning truncated values. The draft's Limits carry the mixed-version limit.
- Not checked: "an older coordinator reads the top-level `totalHitCount` and the shard `matches` with `(Integer)` casts" (uncited; NOTE for the main side, though the Limits rely on it).

## SOLR-14931: SATISFIED

- Head `1a7d678d9a158455b0a6afca47c215ddf88b5a0a` equals the live tip and the receipt. `TestMacros` 2 of 2 and `TestShardMacroExpansion` 1 of 1 at the head, verified 2026-10-07, matching the Proof. The base result (one of two `TestMacros` methods fails, and the cluster test fails) matches the receipt's premise.
- Cites checked: head `RequestUtil.java` L147 and L166 (appends and invariants go through `expandConfigMacros`), L282-L292 (the helper, which returns the values unchanged when `isShard` is false or `expandMacros` is not `true`), changelog L1. The draft's behavior bullets match the helper.
- The Choice (expand on the shard, or skip the second application) is settled under the default. The alternative changes shard behavior for every appends and invariants value, which the report itself calls risky, and the expansion on the shard fixes the reported macro symptom with a narrower change.

## SOLR-15319: SATISFIED (landing dependency noted, as the assignment asks)

- Head `4bda91f46fc18142809c919d7c563129fdf7fb96` equals the live tip and the receipt. `TestDistribIDF` 4 of 4 and `TestExactStatsCacheLegacyResponse` 3 of 3, verified 2026-10-07, matching the Proof.
- Cites checked: head `ExactStatsCache.java` L228 (the `shard.collection` key), L264-L270 (the coordinator's keys), L319-L326 (`perShardKey`), L120 (the exception check that the Limits name), `TestDistribIDF.java` L189 (the multi-collection test), changelog L1.
- Landing dependency: the Limits say the path at line 120 is the SOLR-8051 case, that the production fix is on the SOLR-8051 branch, and that this change lands after SOLR-8051 and is rebased onto its fix if the two `ExactStatsCache` edits need reconciling. That matches the receipt's premise verdict (2026-10-12, SAME CASE as SOLR-8051). Per the assignment this is a fact for the Limits and not a finding against the draft, so no text changes.
- The receipt's two GitHub run IDs have no recorded conclusion; the draft claims none, which is correct.

## SOLR-15479: SATISFIED

- Head `57bd53ce4d07a4a392456db5318d52d64f5680a2` equals the live tip and the receipt. `TestReRankQParserPlugin` 14 of 14, checked 2026-10-07, matching the receipt's 14 tests with 0 failures. The base result (3 failures, the three new maxScore tests) matches the receipt's premise.
- Cites checked: head `SolrIndexSearcher.java` L1827-L1846 (`rescoredMaxScore`), base `SolrIndexSearcher.java` L1911-L1914 (the collected maximum). The call site (head L1950 and L2073) passes `topDocs = topCollector.topDocs(0, len)`, the collected window, so the draft's "collected result window" is what the code computes. The method's Javadoc says "returned docs", and that means the docs the search returns, which is the same window. No Javadoc change is needed.
- Edit (Limits): the receipt reserves the choice "whether maxScore should span all matches after reranking or only the collected result window". The draft's Choice listed the collected window and the returned page, but not the all-matches option. The all-matches option is now a named follow-up in Limits (it would need scores for documents outside the collected window, and its cost is not measured).
- Not checked: "A request with `rows=0` that does not collect a result window (no query result cache for the request) still reports the score from before the rerank" (uncited behavior; NOTE for the main side).

## SOLR-15895: SATISFIED

- Head `02930909397ad52c5c7c61279152b07f2c4ae98d` equals the live tip and the receipt. `TestManagedFileStorage` 2 of 2 and `TestRestManager` 5 of 5, verified 2026-10-03, matching the Proof.
- The Proof says the pre-fix proof is "inconclusive by construction" and that the test cannot show a failure on base. The receipt says the same ("Do not state it as a pass"), so the draft does not claim a pass.
- Cites checked: head `ManagedResourceStorage.java` L195-L215 (`FileStorageIO.validateStoredResourceId`), `RestManager.java` L474 (the create-path call), `ManagedResourceStorage.java` L82-L86 (the default accepts every id), changelog L1.
- The Choice (ask the file system, or use a fixed list) is settled under the default: the file-system check fixes the reported Windows symptom and rejects only what each platform rejects, and the fixed list would reject ids that Linux and macOS store fine.

## SOLR-16444: SATISFIED

- Head `8ffee94a5e75b17f5b5f8be9f840862a1a56524e` equals the live tip and the receipt. `TestRestManager` 5 of 5 and `TestManagedStopFilterFactory` 3 of 3, verified 2026-10-07, matching the Proof. Each base class has one failure, the new test, matching the receipt's premise.
- The GitHub run at this head has no recorded conclusion, and the draft claims none.
- Cites checked: head `RestManager.java` L230-L242 (the late-observer notification with the SOLR-16444 comment), `TestRestManager.java` L152-L180 (the test, which also checks that the first observer is not told again), `TestManagedStopFilterFactory.java` L199-L242 (the stop-filter test), changelog L1.
- The Limits say the reporter's setup is not confirmed, which matches the record.

## SOLR-17155: SATISFIED

- Head `1413237f7a75b8b3da946401d12bcd0e29917581` equals the live tip and the receipt. `TopGroupsResultTransformerTest` 1 of 1, matching the Proof. The base result (a server NullPointerException) matches the receipt's premise.
- Cites checked: head `TopGroupsResultTransformer.java` L343-L366 (`retrieveUniqueKey`: the stored value first, then docValues into a new `SolrDocument`, and a SERVER_ERROR when neither exists), changelog L1.
- The receipt says the non-stored unique-key scope question "remains open in the record". Under the decision default the draft's route is adopted: reading docValues makes the setup work, and rejecting it would leave the reported setup broken. The draft's Choice stays as PR text for the owner's review at opening.

## SOLR-17791: SATISFIED

- Head `a39c1c9737667e4ab935a9c498648bdb9f4507c1` equals the live tip and the receipt. `TestManagedFeatureStoreRest` 2 of 2, `TestManagedFeatureStore` 10 of 10 and `TestModelManager` 3 of 3, verified 2026-10-05, matching the Proof.
- The draft's "Four of these tests are new" is correct: four `@Test` methods are added to `TestManagedFeatureStore` between the merge-base `56ec140e3636` and the head.
- Cites checked: head `RestManager.java` L341-L346 (the child id passed to PUT and POST), `ManagedFeatureStore.java` L162-L198 (`withDefaultStore`, which keeps an explicit store), the upgrade note `major-changes-in-solr-11.adoc` L45-L51, the guide `learning-to-rank.adoc` L294, changelog L1.
- The receipt's PR-time owner call (overloads on `ManagedResource` or a child-id getter on the endpoint) is deferred, and the receipt says both options are to be named in the PR text. The draft names both in its Choice, which is what the receipt asks.

## SOLR-6207: SATISFIED

- Head `b099a9f1be5a3fb529767ca625f42e7860e62f13` equals the live tip and the receipt. `SolrQueryRequestBaseTest` 1 of 1, matching the Proof.
- The Proof says the test "does not fail on the base code, and it is not meant to". The receipt says there is no fail-before by design, and that the test also passes with the production edit reverted (javadoc only). The draft matches both.
- Base cites (labelled "the base commit"): `SolrQueryRequestBase.java` L171-L174 and L92-L95, `SolrCore.java` L2927 (the request log). Head: `SolrQueryRequest.java` L128-L134 (the new javadoc), `RequestUtil.java` L182 (the merge that `setParams` performs), `SolrQueryRequestBaseTest.java` L25-L39, changelog L1.
- The Choice (document or deprecate) is settled under the default. Documenting fixes the reported confusion, and deprecation is a wider API change than the ticket's symptom needs.

## SOLR-7520: SATISFIED

- Head `10b6931e1c058d76392c4ba6d53b45bee251e3d2` equals the live tip and the receipt. `AnalyticsMergeStrategyTest` 1, `AnalyticsQueryTest` 1, `TestGroupingSearch` 17, `TestDistributedGrouping` 2, matching the Proof. Base premise (the `analytics section missing` assertion) matches the receipt.
- Cites checked: base `CommandHandler.java` L254-L257 (the post filter set as the collector; the draft's prose cite), head L259-L268 (the `try` with the `complete()` call in the `finally`), `AnalyticsMergeStrategyTest.java` L93-L106 (the new assertion), changelog L1. The base `SolrIndexSearcher.java` L334 and L1200 call `complete()` on the collector chain, which is the non-grouped path the draft describes.
- The Limits name the coordinator gap (merge strategies do not run for grouped responses) with a follow-up offer, as the receipt asks.

## Not checked (sub-batch 4)

- Jira text (the reporter's examples and the ticket's suggested routes). No Jira packet was opened.
- Gate logs (not on disk here); counts are taken as the receipts record them.
- Full test bodies; only the cited blocks were read.
- Uncited sentences: 14381 (the older coordinator casts); 15479 (the `rows=0` path); 14931 (the cluster test's field count). The 10694 Limits about the other response writers were not checked, as the draft says.
- No builds, no tests, no gate runs, and no writes to GitHub.
