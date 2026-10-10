# Search components round 1, sub-batch 2, part h4 (SOLR-6975, 7550, 8020, 18109)

Result: all four origin heads match the claim table; 6975 and 8020 are draftable, 7550 is draftable only after the owner settles the 500 rule and fixes its changelog title, and 18109 is draftable as a test-only PR whose framing must say the fix is already on main.

## Findings

1. FIX (7550, changelog title). File: `changelog/unreleased/SOLR-7550-peersync-500.yml` line 1, head `687165651f9`. Evidence: the title says PeerSync "treats a 500 ... as unreachable, like a 503". The code counts it as success: `solr/core/src/java/org/apache/solr/update/PeerSync.java` L426-L432 logs "counting as success" and returns true. Exact replacement: `PeerSync during leader election now counts a 500 response to the versions request from a replica as success, like a 503, so a replica whose core failed to load no longer blocks the election`. A changelog-only commit does not change the tested code, but confirm the YAML still parses. I did not edit the branch.

2. FIX (7550, owner decision). File: `solr/core/src/java/org/apache/solr/update/PeerSync.java` L423-L433, head `687165651f9210e333a03c566a27f59f40527174`. Evidence: the rule checks only the request purpose (`SHARD_REQUEST_PURPOSE_GET_VERSIONS`) and `code() == 500`, so every 500 for a versions request counts, not only the failed-core case in the Jira packet (`research/jira-context/SOLR-7550.json`). Round 28 review finding 1 (MEDIUM) says the same, and the receipt calls the trade-off an owner decision. Exact replacement: use the draft's "A choice to check" section, which poses the broad rule as implemented against the narrow rule. The owner must decide before submission.

3. FIX (18109, framing). Files: the Jira title is "Fix deprecated SolrPluginUtils.doStandardDebug"; the branch is test only. Evidence: `git merge-base --is-ancestor 0006fe3c14c origin/solr-18109-submit` succeeds, so the move (apache/solr PR 4779, merged 2026-08-24) is in the branch base. `git diff 14c7aac0d151 origin/solr-18109-submit` touches only `MoreLikeThisHandlerTest.java` and `DebugComponentTest.java`. On `upstream/main`, `doStandardDebug` exists only in `solr/core/src/java/org/apache/solr/handler/component/DebugComponent.java` L413 and its caller `MoreLikeThisHandler.java` L253. Exact replacement, PR title: `SOLR-18109: add tests for the debug helpers moved to DebugComponent`. The body must not use "fix". The draft's first section already says the move landed.

4. FIX (6975, behavior statement). File: `solr/core/src/java/org/apache/solr/handler/component/ShardFieldSortedHitQueue.java` L97-L102 (the loop continues while `c == 0`) and L123-L126 (the DOC comparator returns 0). Evidence: with `_docid_` as one of several keys, the DOC comparator returns 0, so the next key decides, and only single-key `_docid_` sorts are tested. Exact replacement, for "What this change does": `If _docid_ is one of several sort keys, the merge ignores that key. The other keys decide the order, and the shard name breaks what is left.` The draft already has this sentence.

5. NOTE (8020, wording). The Jira says the old code "overwrites" the debug section. The code appends a second entry: `solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java` L796-L799 at head, and the unguarded `if (rb.isDebug())` block at L794-L798 on base `22a8cfebbbdb`. `NamedList.add` appends. Replacement: none needed. The draft says "adds a second debug entry". Do not repeat "overwrites" in the PR.

6. NOTE (6975, local ref). Local `solr-6975-submit` (2a83d30f760) is not the live tip `761aa629bb83`. Against origin it adds an internal file `SOLR-6975-TESTING.md` that says "not run", and its test at `DistributedQueryComponentOptimizationTest.java` L357 uses `fl` `id,payload` where origin uses `id,test_sS`. The production code and changelog match origin. Replacement: none. Build from `origin/solr-6975-submit` and do not push the local ref.

7. NOTE (7550, 8020, 18109, local refs). The local refs also diverge from origin. `solr-7550-submit` (598620cc566) sits on an older line, and its diff against origin touches `ui/` files. `solr-8020-submit` (85556fc9084) has an internal `SOLR-8020-TESTING.md` and an older `ComponentStageLimitsTest` with no `/exiting` handler and no `ExitingReaderSearchComponent.java`. `solr-18109-submit` (8d644a2974a) carries a production commit that duplicates the move already on main through PR 4779. Replacement: none. Do not push any local ref for these four tickets.

8. NOTE (7550, public visibility). Files: `solr/core/src/java/org/apache/solr/handler/component/ShardResponse.java` L81 and L100 (setters now public); `solr/core/src/java/org/apache/solr/update/PeerSync.java` L367-L368 (`handleResponse` now package visible with `@VisibleForTesting`). Evidence: the test in `org.apache.solr.update` needs these to build a response. Round 28 review finding 2 (LOW) asks whether the test can avoid them. Replacement: the draft's sentence "Two smaller changes support the test." The owner decides; restructuring the test changes tested code and needs a new gate.

9. NOTE (7550, test reach). `PeerSyncHandleResponseTest` calls `handleResponse` with a synthetic `SolrException`. `solr/solrj/src/java/org/apache/solr/client/solrj/RemoteSolrException.java` L30 (upstream/main) extends `SolrException`, so a remote 500 can match the rule's `instanceof`. I did not trace how the shard handler hands that exception to PeerSync. Replacement: the draft's Limits bullet "The path from a real HTTP 500 to this rule is not tested end to end."

10. NOTE (6975, test strength). Round 28 finding 1 (LOW): the test checks completion and count, not same shard order. A same shard order assertion would be clearer. Changing the test needs a new gate, so I do not recommend it for this round. Owner decision.

11. NOTE (6975, payload failure). The receipt records a payload fetch failure on both trees, but the trigger is not in the record. The local ref's test used `fl=id,payload`, which may be that trigger. Not confirmed. The draft names no field. Keep it that way until confirmed.

12. NOTE (8020, test scope). The test asserts `numFound > 0` (`ComponentStageLimitsTest.java` L207-L240) but does not count response sections. The draft's Limits says so. No change.

13. NOTE (landing order, pairs in this batch). Two file overlaps, both trial merges clean in both orders (`git merge-tree --write-tree`, exit 0, no refs written).
   - 6975 and 17976 both change `ShardFieldSortedHitQueue.java`. 17976 changes the tie-break key on its branch (L104-L112, `tieBreakShard`, using `shardName` when set). 6975 adds the DOC branch at L123-L126 and leaves the tie-break alone. The 6975 draft says "shard name", which holds for either key. Either order works. Re-read the 6975 draft after 17976 lands.
   - 14451 and 18109 both change `DebugComponentTest.java`. 14451 adds an import and `testModifyRequestFacetPurposeDebugModes`; 18109 adds `testExplainOther`. Once both land the file has 8 tests, so the 18109 count "7 of 7" applies to 18109 alone and must be stated again at the combined head.

14. NOTE (cross batch). The research issue log says SOLR-7351 shares `MoreLikeThisHandler.java` with SOLR-18109 (`research/issue-log.md` around line 560). Keep the two PRs separate, as that log says.

## Task results

**SOLR-6975: draftable.** Head `761aa629bb83e241df2aefc114513797b7339522` (origin), matching the claim table. The receipt counts 10 of 10 match the test class, which has 10 `@Test` methods at head. The proof is a real failure on the base `e432df19c4a` with only the new test added. The Jira packet stack (`research/jira-context/SOLR-6975.json`) matches the changelog wording. The three files have not changed on main since the base, and the trial merge onto `upstream/main` is clean. Draft: `pr-drafts/search-components/SOLR-6975.md`. Choice drafted: grouping by shard, as implemented. Limits: multi key sorts, payload failure.

**SOLR-7550: draftable after an owner decision.** Head `687165651f9210e333a03c566a27f59f40527174`, matching the claim table. The counts (`PeerSyncTest` 1 of 1, `PeerSyncHandleResponseTest` 2 of 2) match the receipt, and the test class has 2 tests. The proof is inconclusive by construction, and the draft says so: the test does not compile on the base, because `handleResponse` is private there and the setters are package private. The 500 rule is broader than the Jira case, so the owner must accept it or narrow it. The changelog title needs Finding 1. The trial merge onto main is clean. Draft: `pr-drafts/search-components/SOLR-7550.md`.

**SOLR-8020: draftable.** Head `79f790523d2fcbc91670e5dab0d4b5940dc11c41`, matching the claim table. `ComponentStageLimitsTest` 5 of 5 matches the receipt and the file has 5 tests. The receipt's base run (only `SearchHandler.java` reverted) fails 1 of 5 with `expected:<1> but was:<2>`, which matches the new test's assertion. The Jira's numFound and duplicate response points are already handled on main (`SearchHandler.java` L784 guard, and `shortCircuitedResults` does not change `numFound`), so the draft covers only the debug entry. No Choice is owed. The trial merge is clean. Draft: `pr-drafts/search-components/SOLR-8020.md`.

**SOLR-18109: draftable as a test-only PR with corrected framing.** Head `b19395e1f60acddb17cbd4ae3e3d0e75e0844c5f`, matching the claim table. `DebugComponentTest` 7 of 7 and `MoreLikeThisHandlerTest` 3 of 3 match the receipt, and the files have 7 and 3 `@Test` methods. The proof is skipped by construction, as recorded. The production move is already on main through PR 4779, so the PR cannot present itself as the fix (Finding 3). The local packet still shows the Jira as Open, and I did not check the live status. The owner decides whether a coverage-only PR is wanted at all. Draft: `pr-drafts/search-components/SOLR-18109.md`.

## Owner decisions

1. SOLR-7550: keep the broad 500 rule as implemented (the reporter's own proposal), or narrow it to the failed-core case. The draft is written for the broad rule and needs a new Choice section if the rule narrows.
2. SOLR-7550: keep the two public `ShardResponse` setters, or restructure the test to avoid them (restructuring needs a new gate).
3. SOLR-7550: approve the changelog title change in Finding 1.
4. SOLR-6975: group by shard (as implemented), or reject distributed `_docid_` sorts with a clear error. The draft poses this as the Choice.
5. SOLR-6975: whether to add a same shard order assertion (round 28 LOW). That changes the test and needs a new gate.
6. SOLR-18109: whether to submit a coverage-only PR for a ticket whose production fix is already on main, and under what title. Nothing has been posted, and the Jira status is not changed.
7. Landing order: 6975 and 17976 in either order, and 14451 and 18109 in either order, with the DebugComponentTest counts restated after both land.
8. Local refs for the four tickets diverge from origin. Confirm origin is canonical, and decide whether to reset or rename the local refs before anyone pushes. I changed nothing.

## Not checked

- No builds, tests, Gradle runs, or `gh` calls, per the shared rules. The counts come only from the receipts, and the gate logs are not in this worktree.
- Live heads: I did not run `git ls-remote`. The origin remote-tracking refs in this repo match the claim table for all four tickets.
- Live Jira status and comments. I read only the local JSON packets, which show "Open".
- The trigger of the payload fetch failure on 6975.
- How a remote 500 reaches PeerSync. Only the SolrJ class hierarchy was checked.
- Lucene behavior. No draft names Lucene behavior, so the 9.x and 10.x check did not apply. `upstream/branch_9x` exists locally (8adbca1c68d) if a Lucene claim is added later.
- How JSON clients handle duplicate keys. The 8020 draft says a client "may" keep only one entry, which is a hedge, not a checked fact.
- Whether the 8020 config replacement in the test matches the base test resource. The receipt covers the run.
- Logchange or other changelog tooling. I checked the schema against a current main fragment and `changelog/logchange-config.yml` (type `fixed` exists).
- Compile of the combined files (DebugComponentTest for 14451 with 18109, and ShardFieldSortedHitQueue for 6975 with 17976). Only text trial merges were run.
- The round 28 reviews were used as context only, and I did not re-derive them.
- The local `SOLR-6975-TESTING.md` was read only to understand the local ref. It is not evidence.
