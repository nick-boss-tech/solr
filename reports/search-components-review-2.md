# Search components review, sub-batch 2: SOLR-11470, 13876, 14451, 15018, 17748, 17976, 18109, 6975, 7550, 8009, 8020, 8939

Reviewer: Windows review host (claim `claims/search-components-review.md`), 2026-10-13.
Scope: the twelve drafts under `pr-drafts/search-components/` in the second earlier sub-batch.

Method: the same as sub-batch 1. Each receipt head was compared with the live fork tip; all twelve agree. Proof counts and dates were compared with the receipts. Citations were read at the SHA they name (cite-check). Merge-bases were checked with `git merge-base`. Upstream claims were checked against `upstream/main` (SOLR-18109, SOLR-6975). PR state was read with the read-only `gh pr view` (PR 4779 for SOLR-18109; PR 5014 for SOLR-13876). Public-text lint: no "we", no em or en dashes, and no process words in the twelve drafts. Nothing was built, no test was run, and nothing was written to GitHub.

## Summary

| Ticket | Verdict | Edits in this tree |
|---|---|---|
| SOLR-11470 | SATISFIED | none |
| SOLR-13876 | SATISFIED | 2 (Limits: group-wide maximum as a named follow-up) |
| SOLR-14451 | SATISFIED | 1 (Limits: other debug modes as a named follow-up) |
| SOLR-15018 | SATISFIED | none |
| SOLR-17748 | SATISFIED | none |
| SOLR-17976 | SATISFIED | none |
| SOLR-18109 | SATISFIED | none (upstream outcome recorded below) |
| SOLR-6975 | SATISFIED | 3 (shard name to shard address) |
| SOLR-7550 | SATISFIED | 1 (Limits: narrower rule as a named follow-up) |
| SOLR-8009 | SATISFIED | 1 (unrecorded run claim removed) |
| SOLR-8020 | SATISFIED | none |
| SOLR-8939 | SATISFIED | none |

## SOLR-11470: SATISFIED

- Head `f44c294da37452462541771c48b2d85132cf5a16` equals the live tip and the receipt. `TestReRankQParserPlugin` 13 of 13 and `QueryEqualityTest` 113 of 113, matching the Proof. The receipt's two-round premise record matches the Proof: the lucene-parser test passes on base, and the `{!bool}` test is the one that fails.
- Base `14c7aac0d151`. Cites checked: `SolrQueryParser.java` L31-L35 (the auto-fix), `QParser.java` L113-L118 (the `luceneMatchVersion` check against 10.2.0). Head: `ResponseBuilder.java` L480-L488 (the `wrap` change), `TestReRankQParserPlugin.java` L303-L335 (the new test), changelog L1.
- The wrapper claim is supported by code: base `QueryUtils.makeQueryable` (L185-L188) unwraps a `WrappedQuery` before it checks for a negative query, so a `cache` or `cost` wrapper is lost under a rank query. The Limits say no test covers it, which matches the receipt.
- The "9.x branch has no such check" claim matches the cross-line check recorded for SOLR-8977 in `reports/query-parsing-review-q1.md` on `assignment-query-parsing-review` (`upstream/branch_9x` and `upstream/branch_10_0` have no `autoFixPureNegative`). Not re-run here.
- Not checked: the ticket's example (52 documents alone, none with `rq`). Jira was not opened.

## SOLR-13876: SATISFIED

- Head `2e110473dbada4f6a0e23a903a4e002da2b78143` equals the live tip and the receipt. The Proof's counts come from the round 35 re-run at the same head, dated 2026-10-07, which the receipt names with logs (`g13876r35-tests.log`, `g13876r35-premise.log`). The receipt's row has no gate log or explicit gate date (a record gap, listed below); the draft cites the dated logs, not the row.
- Premise: base production with this branch's test file gives 7 of 9 failures, all maxScore checks, matching the receipt.
- Base `b5c71bc5573c`. Cites checked: base `ExpandComponent.java` L787 (the fixed `Float.NaN`), head L780-L790 (the slice maximum), head L475-L520 (the coordinator passes groups through), changelog L1.
- Edit (Limits): the group-wide maximum is the alternative in "A choice to check". It is now a named follow-up in Limits, with the reason (it needs scores for documents the page does not return; the cost is not measured).
- Not checked: "For the relevance sort it equals the group maximum, because the top scored documents come back first" (uncited; NOTE for the main side). PR 5014 (SOLR-13568) is OPEN, and its title says it changes the filter cache. The draft's "does not change scores or the documents returned" was not checked against the diff.
- Record gap (main side): the receipt row for SOLR-13876 names no gate log and no explicit gate date. Adding the gate log name and date to the receipt would close it.

## SOLR-14451: SATISFIED

- Head `e60891d8716d89538109407aadc629987456017a` equals the live tip and the receipt. `DebugComponentTest` 7 of 7 and `TestCloudJSONFacetSKGEquiv` 7 of 7, verified 2026-10-07, matching the receipt.
- Premise: `DebugComponentTest` runs 7 with exactly 1 failure (`testModifyRequestFacetPurposeDebugModes`), matching the receipt.
- Base cites (labelled "at the base commit"): `FacetModule.java` L150, `DebugComponent.java` L175-L177. Head: `DebugComponent.java` L179-L189, `DebugComponentTest.java` L197-L243, `TestCloudJSONFacetSKGEquiv.java` L269. Changelog L1.
- Edit (Limits): the choice in "A choice to check" (forward only query debugging, or every debug mode) is settled under the ladder. The other debug modes are not part of the ticket, so the alternative is optional and is now a named follow-up in Limits, rather than an open question for the owner.
- The draft says the cloud test was not run on the base code, which matches the receipt.

## SOLR-15018: SATISFIED

- Head `d0f29b4630c03f14db715f5f23864694ec9e8327` equals the live tip and the receipt. `NestedAtomicUpdateIgnoredFieldTest` 1 of 1, `NestedAtomicUpdateTest` 15 of 15, `TestRealTimeGet` 4 of 4, verified 2026-10-07, matching the Proof.
- Premise: the strengthened test fails on base `b5c71bc5573` with one failure, matching the Proof.
- Cites checked: base `RealTimeGetComponent.java` L878-L880 (the skip), head L873-L913 (the per-element keep), test L58-L115 (the 120 filler documents over 12 commits, which the draft describes correctly), changelog L1.
- The Limits claim that the tlog limits "default to 100 records in 10 files" is correct: `solrconfig-tlog.xml` sets `numRecordsToKeep` to `${solr.ulog.numRecordsToKeep:100}` and `maxNumLogsToKeep` to `${solr.ulog.maxNumLogsToKeep:10}`.
- The copy-field statement holds: head L880 keeps the children of a copy-field target, and base L879 skipped the whole field.

## SOLR-17748: SATISFIED

- Head `dfacaf347668e43a433baef650268b281649c6fc` equals the live tip and the receipt. `TestShardsInfoErrorRecording` 4 of 4 and `TestShardsInfoResponse` 1 test pass, matching the receipt.
- Premise: base `QueryComponent.java` (`14c7aac0d15`) gives 3 of 4 NullPointerException failures in `TestShardsInfoErrorRecording`, and the new single-pass case fails there, matching the Proof.
- Cites checked: head `QueryComponent.java` L1094-L1098 (the `unknown_shard_N` record), L1043 and L1496-L1498 (the error with no cause), L1487-L1492 (the entry creation), changelog L1.
- The receipt's PR notes are reflected: the behavior change (single-pass tolerant queries now return 200 with partial results) is stated, and the entry-creation choice is posed with its reason ("the id merge records every failed response under its name").
- Not checked: "Without `shards.tolerant=true`, a shard with no live replica still fails the request with a 503 error before this step" (uncited; NOTE for the main side).

## SOLR-17976: SATISFIED

- Head `56ea43c448ed4fea75131d34c390d11233bab5cb` equals the live tip and the receipt. The counts match the receipt: `TestShardTieBreakCluster` 1 of 1, `TestShardTieBreak` 2 of 2, `CombinedQueryComponentTest` 6, `CombinedQuerySolrCloudTest` 9, `DistributedCombinedQueryComponentTest` 3.
- Premise: the cluster test fails on base with reversed document order, matching the Proof. `TestShardTieBreak` sets the new field, so it has no failing-before result, which the draft says.
- Cites checked: head `QueryComponent.java` L971-L989 (the name map) and L1232 (the name assignment), `CombinedQueryComponent.java` L323 and L424, `ShardFieldSortedHitQueue.java` L104-L118 (the tie-break), `ShardDoc.java` L30-L33 (the public field), test L150-L159, changelog L1.
- The PR notes in the receipt (the order changes once for every SolrCloud user, and code that builds a `ShardDoc` without `shardName` keeps the old order) are in the draft's Change and Limits.
- Interaction (main side): SOLR-6975 (below) breaks cross-shard ties by the shard address. This ticket says that address varies per request. The two branches share the tie-break; the 6975 draft's "order across shards is not defined" sentence covers it.

## SOLR-18109: SATISFIED (upstream outcome)

Outcome of the assignment's check: the upstream change under this key has merged, and the draft still has something of its own to submit.

- Upstream: PR 4779 (title "SOLR-18109: move SolrPluginUtils debug methods to DebugComponent") is MERGED, `mergedAt` 2026-08-24T17:25:40Z (read-only `gh pr view`). `upstream/main` has `doStandardQueryDebug`, `doStandardResultsDebug` and `doSimpleQuery` as static methods in `DebugComponent`. The draft's statement matches.
- Own content: the branch adds `testExplainOther` to `DebugComponentTest` (+10 lines) and `testStandardDebugOutput` to `MoreLikeThisHandlerTest` (+34 lines). Neither test exists on `upstream/main` (checked by grep). So the two regression tests are the branch's own content.
- Explain-other path: `upstream/main` `DebugComponent.java` L467 calls `doSimpleQuery`. That matches the draft's claim that the explainOther test reaches it.
- Main-side note: the branch's merge-base with `upstream/main` is `14c7aac0d151`, and `upstream/main` is now `cf4a66669d`. The branch predates PR 4779. The tests do not call the moved methods directly, so they should compile on main, but the head has not been re-run on a rebased tree. Rebasing before the PR opens is a main-side call.
- The Proof is "skipped by construction" (test-only change), as the receipt records.

## SOLR-6975: SATISFIED

- Head `761aa629bb83e241df2aefc114513797b7339522` equals the live tip and the receipt. `DistributedQueryComponentOptimizationTest` 10 of 10, matching the Proof.
- Base `e432df19c4a` is the merge-base with `upstream/main`. `ShardFieldSortedHitQueue.java` has no change between `e432df19c4a` and `upstream/main`, so "the comparator file has not changed on main since this branch was cut" holds.
- Premise: base with only the new test fails with the `IndexOutOfBoundsException` from `NamedList.getName`, matching the receipt.
- Cites checked: head `ShardFieldSortedHitQueue.java` L123-L126 (the `DOC` comparator returns a tie), L82-L111 (`lessThan`), test L348-L362 (the ascending and descending check, which matches the draft's description), changelog L1.
- Edit (three places): the draft said documents from different shards are grouped by shard "name". The code at this head breaks ties by `docA.shard`, which is the shard address (replica URLs). The draft now says shard address in the Change, the Limits sentence and the first option of the Choice.
- The receipt's two owed items are both present: the Choice (group by shard versus a clear error) and the Limits line on the payload fetch failure.

## SOLR-7550: SATISFIED

- Head `687165651f9210e333a03c566a27f59f40527174` equals the live tip and the receipt. `PeerSyncTest` 1 of 1 and `PeerSyncHandleResponseTest` 2 of 2, matching the Proof. The receipt's proof is "inconclusive by construction", and the draft says the same.
- Base `14c7aac0d151`. Cites checked: base `PeerSync.java` L398-L420 (the 503 and 404 rules), head L423-L433 (the 500 rule), head `ShardResponse.java` L81 and L100 (the public setters), head `PeerSync.java` L367-L368 (the package-visible `handleResponse`), test L50-L74, changelog L1.
- The receipt says the generic-500 trade-off is an owner-level decision to be made explicit before submission. The draft makes it explicit (the Choice section names the reporter's broad rule and the narrower alternative, and the cost of each).
- Edit (Limits): the narrower rule is now a named follow-up with its reason. Under the ladder the choice is settled at rung 3: the narrower rule is not required by the ticket, and the only way to reach it is error-text matching or a server marker, both fragile. The owner can reopen it when the PR is opened; this is recorded here so the decision is not silent.

## SOLR-8009: SATISFIED

- Head `8795661ddc98c8bffdb8db49a7dd5f293216e929` equals the live tip and the receipt. `FullSolrCloudDistribCmdsTest` 10 of 10, `TestRealTimeGet` 4, `TestRandomFlRTGCloud` 2, `TestAddFieldRealTimeGet` 1, verified 2026-10-05, matching the Proof. The 2026-10-12 reconciliation run in the receipt reports the same 10 tests (fresh JUnit XML).
- Premise: base `14c7aac0d15` with the branch test fails at the first `assertNotNull`, matching the Proof. The guard `testDeleteByIdImplicitRouter` passes on base, as the draft says.
- Cites checked: base `RealTimeGetComponent.java` L1065-L1067 (the skip) and L1088-L1093 (the `shards` request), head L1067-L1076 (the implicit-router collection) and L1097-L1103 (the all-shards request), test L157-L184, changelog L1.
- Edit: the sentence "In test runs it also trips the existing assert `docList.size() <= 1` in `addDocListToResponse`" made a run claim that no record supports. It is removed; the Limits line now says only that a get by one id whose document sits on two shards is not tested. The assert itself exists at head (`RealTimeGetComponent.java` L1197); the record does not show that it trips.
- The receipt's PR notes are reflected: the fan-out (the 400 alternative is posed in the Choice) and the duplicate-result risk inherited from `shards` (SOLR-8954).

## SOLR-8020: SATISFIED

- Head `79f790523d2fcbc91670e5dab0d4b5940dc11c41` equals the live tip and the receipt. `ComponentStageLimitsTest` 5 of 5, verified 2026-10-05, matching the Proof.
- Premise: with only `SearchHandler.java` at base, one of five fails (`testLimitAfterDebugKeepsSingleDebugSection`, "expected:<1> but was:<2>"), matching the receipt's round 2 log.
- Cites checked: base `SearchHandler.java` L794-L798 (the unconditional debug add), head L794-L800 (the added condition), head L784 (the response-list check), test L207-L240, changelog L1.
- The receipt says no Choices section is owed, and the draft has none. The Limits say the numFound and duplicate complaints are already handled on main.

## SOLR-8939: SATISFIED

- Head `a855a2d8965c8eae5df171ad9b5148b7e95f7425` equals the live tip and the receipt. `StoredFieldsShardRequestFactoryTest` 2 of 2 and `QueryComponentIdFormatTest` 2 of 2, matching the Proof. The receipt's premise (1 failure of 2) matches the draft's statement that the date test fails on base and the string test passes on both.
- Cites checked: head `QueryComponent.java` L1453-L1462 (the helper) and L1443-L1447 (the plain path), `StoredFieldsShardRequestFactory.java` L79 (the grouping path), changelog L1.
- The receipt's coverage limit (unit level only, no two-shard run) is stated in the draft's Proof and Limits.
- The zero-millisecond note is correct: `Instant.toString()` omits the fraction when the nanoseconds are zero.

## Record gaps and notes for the main side

- SOLR-13876: the receipt row names no gate log and no explicit gate date (see above).
- SOLR-18109: the branch base predates PR 4779; rebase before opening is a main-side call.
- SOLR-6975 and SOLR-17976: the cross-shard tie-break interaction (see SOLR-17976).
- Uncited sentences, not run: 13876 "for the relevance sort it equals the group maximum"; 17748 "Without `shards.tolerant=true`... 503"; 11470 "the 9.x branch has no such check" (consistent with the cross-line record, not re-run here).

## Not checked

- Jira text for every ticket (no Jira packet was opened).
- Gate logs (not on disk here); counts are taken as the receipts record them.
- Full test bodies; only the cited blocks were read.
- No builds, no tests, no gate runs, and no writes to GitHub. The `gh` calls were read-only (`pr view`).
