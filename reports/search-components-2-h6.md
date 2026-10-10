# Search components round 1, sub-batch 2, part h6: audit only

Result: none of the five tickets is draftable now; SOLR-9124, 10305 and 17055 moved heads (only a handoff doc was removed, code unchanged), SOLR-3044 is test-only and its code is already on main, and four changelog lines need FIX.

## Scope and method

- Base: `upstream/main` 8e62c2686882 (2026-10-09). Worktree at claim commit e4a5dafa8f40.
- Live heads: the local `origin/solr-<n>-submit` refs. They match the claim table. I did not run `git ls-remote` or fetch.
- Commands: `git show`, `git log`, `git diff`, `git grep`, and `git merge-tree --write-tree` trial merges (no ref written). No builds, tests, `gh` calls, or posts.
- Jira: local packets `research/jira-context/SOLR-<n>.json` (the apache-jira MCP was not used).
- Changelog authors on all four changelog files: Nick Shanin (matches the ICLA name).

## Findings

1. FIX. SOLR-10305 changelog, `changelog/unreleased/SOLR-10305-distributed-query-requires-unique-key.yml` (title, live head 0cf26e5f331b).
   Evidence: on `upstream/main`, the first dereference on the distributed path is `createMainQuery`, `QueryComponent.java` L793 (`getUniqueKeyField().getName()`). `regularDistributedProcess` calls it at L646 (STAGE_EXECUTE_QUERY), while the shard request is built, before any shard response exists. The text "while merging shard responses" is wrong for this path.
   Replacement title: `A distributed query (shards parameter) against a schema without a uniqueKey now fails with a 400 "Distributed search requires a uniqueKey field in the schema" instead of a NullPointerException.`

2. FIX. SOLR-17055 changelog, `changelog/unreleased/SOLR-17055-knn-topk-distributed.yml` (live head 925a130e2621).
   Evidence: `QueryComponent.java` L1018-1021 trims the merge queue only when `sort == null`. L1238-1239 caps `numFound` for every request. With an explicit sort (for example `sort=id asc`), all shard hits stay. A two-shard query with topK=3 can return 6 docs with numFound 3. The changelog says topK hits overall, with no sort exception.
   Replacement, if the sort gap stays: `A distributed {!knn} query now returns at most topK hits overall for score-ordered results, instead of topK per shard. An explicit sort is not trimmed yet.` If the owner trims the sort path too, keep the original wording only after a test covers the sort case. See owner decision 4.

3. FIX. SOLR-6759 changelog, `changelog/unreleased/SOLR-6759-expand-complete-postfilter.yml` (live head 62974ef8d18c).
   Evidence: `TestExpandComponent.testExpandCompletesPostFilterCollectors` asserts 2 expanded groups and CREATED equals COMPLETED. It does not check that the last block-collapse group is present. `SOLR-6759-TESTING.md` says that user-visible symptom (`expand.fq` with `{!collapse hint=block}`) is "not covered".
   Replacement: `ExpandComponent now calls complete() on the post filter collector chain it builds, so post filters that finish their work in complete() also run for expand searches.`

4. FIX. SOLR-9124 changelog, `changelog/unreleased/SOLR-9124-grouped-exact-stats.yml` (live head dfc2518214fb).
   Evidence: `TestBaseStatsCache.checkGroupedScores` compares a grouped query on the control client with the same grouped query on shards. It does not compare grouped with ungrouped scores.
   Replacement: `Distributed grouped queries now send the merged global term statistics (ExactStatsCache, LRUStatsCache) to the shards in the group stages, so grouped scores on a sharded query match the same grouped query on one node.`

5. NOTE. SOLR-6759 head still carries `SOLR-6759-TESTING.md` (24 lines, added at 62974ef8d18c). The other three moved heads removed theirs. Remove it before any PR, as the AGENTS rules require for handoff docs. No replacement text.

6. NOTE. Moved heads, SOLR-9124, 10305 and 17055. Each live tip deletes only `<KEY>-TESTING.md` compared with the named head (`git diff --stat`: 30, 29 and 26 lines). Code is identical, so the receipts' code facts still hold. The flagged moves: 101e12d2085f to dfc2518214fb (9124), c21ca8c0e75 to 0cf26e5f331b (10305), f0c401a290b to 925a130e2621 (17055). Not adopted silently. The receipts still name the old heads as live tips: `receipts/SOLR-9124.md` L4, `receipts/SOLR-10305.md` L4, `receipts/SOLR-17055.md` L4. Each needs the new head and a note that the handoff doc was removed there.

7. NOTE. Receipt disagreement, SOLR-10305. `receipts/SOLR-10305.md` L5 says no record supplies the ticket topic. The local packet `research/jira-context/SOLR-10305.json` does (comment 17199302 by Jan Hacker, 2020, and David Smiley's reply 17199665). The removed TESTING doc says the same. Replacement sentence for the receipt: `Topic: distributed query with no uniqueKey in the schema raises an NPE (JIRA comment 17199302).` The blank topic also appears in `branch-focus-inventory-2026-10-08.md` L218 and `research/branch-reviews/round-28/PARKED.md` L46.

8. NOTE. Skip list. `research/pipeline/skips.md` L144 (9124, "fixed in 6.2/7.0, fix version set"), L260 (6759, "fixed in 4.10.5") and L542 (10305, "obsolete: fixed on master per reporter") rest on fix versions or a reporter comment. On `upstream/main` the fixes are not there: `QueryComponent.java` L600-633 has no `sendGlobalStats` in the group stages (the only callers are L896 and `DebugComponent.java` L163); `ExpandComponent.java` L447 has no `complete()` call; the no-uniqueKey dereference is at L793. For 10305 the store=false part is handled per David Smiley's 2020 comment, but the no-uniqueKey case is not. Replacement for each row: `not fixed on main; see the branch`.

9. NOTE. SOLR-3044 premise. The receipt is right. The production hunks are on main in equivalent form: `upstream/main` `QueryComponent.java` L1239-1243 and `CombinedQueryComponent.java` L457-461 call `getResponseHeader().put(...)`, and `SimpleOrderedMap.put` (L138) does the same `indexOf` then add or `setVal` as the branch. `PivotListEntry.java` L83 on main already uses `indexOf`. A trial merge of `04b877e9de18` onto `upstream/main` conflicts in `CombinedQueryComponent.java`, `QueryComponent.java` and `PivotListEntry.java` (merge-tree exit 1). The branch base `86bc6f292245` (2026-06-18) is 425 commits behind main. The local ref `solr-3044-submit` is stale at `abd4e761f7fe`; use `origin/solr-3044-submit` (04b877e9de18).

10. FIX, only if retargeted. SOLR-3044 `CombinedQueryComponentPartialResultsTest.java` (live 04b877e9de18) L109-110 calls `withShardResponse(header, docs, List.of(docs))` with three arguments. `upstream/main` `MockShardRequest.java` L33 has only the two-argument form, and the branch does not change that file. Replacement: decide whether to use the two-argument form or add the overload when retargeting. The third argument's meaning is not clear from the code, so do not guess it.

11. NOTE. SOLR-3044 `TestPivotHelperCode.testPivotListEntryOptionalLookupSkipsEarlierMatches` (live 04b877e9de18) checks `PivotListEntry.STATS.extract`. Main already has the `indexOf` form, so the test is a behavior check that probably passes on main too. It is not a fail-before proof. Expected, not run.

12. NOTE. SOLR-6759 partial-results path. `ExpandComponent.java` L448-451 (live 62974ef8d18c) calls `complete()` after `searcher.search` with no `try` or `finally`. On `upstream/main`, `SolrIndexSearcher.java` L330-336 runs `complete()` in a `finally`, because `complete()` can use the collectors. `ExpandComponent` returns early on query limits (`maybeExitWithPartialResults`, main L448-449). If the search throws, the new call is skipped. Read only, not run. The owner can accept this or copy the `SolrIndexSearcher` pattern. Name it in Limits either way.

13. NOTE. SOLR-6759 wording. The JIRA names `finish()`. On `upstream/main`, `DelegatingCollector.finish()` is final (`DelegatingCollector.java` L103) and `complete()` is the hook (L111). A future Proof should say that the JIRA's `finish()` is now `complete()`.

14. NOTE. Interaction, SOLR-6759 and SOLR-13568 (h2). 6759 edits `ExpandComponent.java` L448-451 on its head. The 13568 hunk is at L434-444 on `ac5d60c214cf`. No text overlap. Both trial-merge cleanly onto `upstream/main` (exit 0). They were not trial-merged together.

15. NOTE. SOLR-17055 wrapped knn. `getKnnTopK` (`QueryComponent.java` L1296) trims only a top-level `SolrKnnFloatVectorQuery` or `SolrKnnByteVectorQuery`. Wrapped forms (filter, block join, rerank, `{!bool}`) are not trimmed. `getTopK()` is new; `git grep` finds no `getTopK` on `upstream/main`. Limits line: `Knn queries wrapped in another query (filters, block join, rerank, {!bool}) are not trimmed to topK overall.`

16. NOTE. SOLR-17055 test scope. `DistributedKnnTopKTest.java` (live 925a130e2621) checks score order only. It has no sort case, so finding 2 is untested. It uses `schema-vector-catchall.xml` with the default solrconfig. The TESTING doc says that compatibility is not verified.

17. NOTE. SOLR-10305 handoff claim. The removed TESTING doc says IndexSchema logs "no uniqueKey specified in schema". `git grep` finds no such IndexSchema message on `upstream/main`. The nearest text is `JsonLoader.java` L307, for update requests. No effect on the code. Minor.

18. NOTE. Lucene claim, SOLR-6759 TESTING doc. "Since Lucene 8, LeafCollector.finish() is called by the searcher itself" is not checked against Lucene sources. `DelegatingCollector.java` L98-100 cites Lucene 9.8 for the `finish` clash only (9.x evidence). No 10.x check. No draft exists yet, so this does not block.

## Task results

**SOLR-3044 (parked, audit only): not draftable.** Head 04b877e9de18 matches live. The production work is already on main in equivalent form (finding 9), so the branch is test-only. The new test does not compile (finding 10), and the branch conflicts with main in three production files. A premise run would need a behavior that main lacks. Nothing in this branch shows one, and a fail-before run on `upstream/main` cannot discriminate code that is already there. A first gate would need a compiling test that fails on `upstream/main`. The retarget is the owner's call.

**SOLR-6759 (no gate): audit only, premise holds, not draftable yet.** Head 62974ef8d18c matches live; no move. `ExpandComponent.java` L447 on main has no `complete()` call, and `complete()` is the current hook (finding 13). The JIRA symptom (an ACL-style filter with an empty expanded section) is not reproduced. The test uses a counting filter. A premise run needs: the new test fails on `upstream/main` with COMPLETED below CREATED, as the TESTING doc expects; and a block-collapse case through `expand.fq` that loses the last group on main and keeps it with the change. A first gate: focused `TestExpandComponent` with `-WithFailBefore` and `-WithSpotless`. Changelog needs FIX (finding 3). The handoff doc must come out (finding 5).

**SOLR-9124 (no gate): audit only, premise holds, not draftable yet.** The premise holds on main: `sendGlobalStats` is called only at `QueryComponent.java` L896 and `DebugComponent.java` L163, and not in the group stages. Head moved: receipt names 101e12d2085f, live is dfc2518214fb (handoff doc removed, code identical). Trial merge onto `upstream/main` is clean. The branch base is 37 commits behind main. A premise run needs: `checkGroupedScores` fails on `upstream/main` for `TestExactStatsCache`, `TestLRUStatsCache` and `TestExactSharedStatsCache`, and passes with the change; and `shard_i` is a valid group field in the test schema (the TESTING doc marks it as a guess). A first gate: focused run of those three classes with `-WithFailBefore`. Changelog needs FIX (finding 4).

**SOLR-10305 (no gate): audit only, partial fit, not draftable yet.** The no-uniqueKey NPE premise holds on main (finding 1, `QueryComponent.java` L793). The ticket title is the store=false case. This branch does not touch it, and main still warns that a non-stored uniqueKey breaks distributed search (`IndexSchema.java` L607-609). Head moved: receipt names c21ca8c0e75, live is 0cf26e5f331b (handoff doc removed, code identical). Trial merge is clean. A premise run needs: `MinimalSchemaTest.testDistributedQueryWithoutUniqueKeyIsBadRequest` fails on `upstream/main` with an NPE (HTTP 500) and passes with the change. It also needs a check that the `shards=127.0.0.1:1/solr/collection1` request reaches `prepare` (the TESTING doc marks this as a guess). A first gate: `-WithFailBefore` on `MinimalSchemaTest`. The linking decision is owner decision 3. Changelog needs FIX (finding 1).

**SOLR-17055 (no gate): audit only, premise holds, design call pending.** The premise holds: `upstream/main` has no knn trim and no `getTopK`. The skip list calls the per-shard behavior "by design" (`research/pipeline/skips.md` L27; `research/pipeline/HANDOFF-task3-skiplist.md` L39). This branch makes the other call for score order only. Head moved: receipt names f0c401a290b, live is 925a130e2621 (handoff doc removed, code identical). Trial merge is clean. A premise run needs: `DistributedKnnTopKTest` fails on `upstream/main` (the TESTING doc expects 9 docs, not 3, not run) and passes with the change. It also needs a recorded result for the explicit sort case, which the test does not cover. A first gate: `-WithFailBefore` on `DistributedKnnTopKTest`. The design and sort calls are owner decisions 4. Changelog needs FIX (finding 2).

## Owner decisions

1. SOLR-3044: retarget to a concrete ticket with a test-only remainder that compiles and fails on main; or close the branch as superseded by SOLR-18373. Production work is already on main.
2. SOLR-6759: accept `complete()` without `try` or `finally`, or copy the `SolrIndexSearcher` pattern (finding 12). Decide whether the block-collapse case needs a test before the first gate.
3. SOLR-10305: link the branch to SOLR-10305 as a partial fix, or treat the no-uniqueKey case as a separate ticket (only on request). Pick the 2017 option: throw up front (this branch) or support a non-stored uniqueKey (David Smiley's other option).
4. SOLR-17055: (a) trim the explicit sort path too, or (b) keep sort uncapped and state it in Limits. Also confirm the design: cap at topK overall (this branch) or keep per-shard topK by design (`skips.md` L27).
5. Skip-list rows for SOLR-9124, 6759 and 10305 (`research/pipeline/skips.md` L144, L260, L542): correct them to "not fixed on main" or leave with a note.

## Not checked

- Builds, tests, Gradle, `gh`, and live Jira. Jira facts come from the local packets only.
- A fresh `git ls-remote` or fetch. Heads come from local `origin/` refs.
- Gate logs. None are on disk for these five; the receipts record none, so there are no proof numbers.
- Lucene 9.x and 10.x claims (finding 18). No Lucene source in the checkout.
- Whether `uniqueKey` with `stored=false` and `docValues=false` still throws an NPE on main (the original report).
- Whether the no-uniqueKey check changes any distributed path that works today (`SearchHandler` and `ShardHandler.prepDistributed` ordering).
- Shard-side handling of `PURPOSE_SET_TERM_STATS` for group requests (`QueryComponent.prepare` L366, read only, not traced).
- The 17055 `schema-vector-catchall.xml` and default solrconfig compatibility, and wrapped knn forms in a run.
- Cross-part hunk overlap in `QueryComponent.java`: 17055's `mergeIds` and queue region against h3 (8939, 17748, 17976), and 9124's `groupedDistributedProcess` against h3 or h5. The lead should run that overlap pass.
- The 6759 ACL-style symptom and the block-collapse case in a run.
