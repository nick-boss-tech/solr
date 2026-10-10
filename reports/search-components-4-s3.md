# Search components round 1, sub-batch 4, part s3: rerank pair (SOLR-11310, 15479)

Result: both draftable, neither ready to open. SOLR-11310 is held for the owner's Choice and a changelog title fix. SOLR-15479 needs a history cleanup (a Claude trailer and two "handoff" commit subjects) and three text fixes first.

Scope: read only. No builds, tests, or gh calls. Nothing posted, committed, pushed, or written to any ref. The only writes are this report and two drafts. The heads match the claim table and both receipts: `origin/solr-11310-submit` at `e38ddec5279c465f65a2fa2de22ee9c92e0f5e31`, and `origin/solr-15479-submit` at `57bd53ce4d07a4a392456db5318d52d64f5680a2`. Bases match the receipts: `14c7aac0d151` for 11310 and `b5c71bc5573` for 15479. No tip moved. Drafts: `pr-drafts/search-components/SOLR-11310.md` and `pr-drafts/search-components/SOLR-15479.md`.

## Findings

1. FIX (11310). `changelog/unreleased/SOLR-11310-ltr-elevated-docs.yml` line 1, at `e38ddec5279`. The title names only LTR and says "kept at the top". The code change is in `BoostedComp`, which every rerank uses. `AbstractReRankQuery.java` lines 105-110 read the boost set from the request context for any rerank. `QueryElevationComponent.java` line 572 puts that set in the context on any elevation match, whatever `forceElevation` is. Replacement title: "Documents elevated with elevateIds now move to the top of the rerank window for every rq rerank, including LTR, not only the leading ones in the window". Changing the title moves the head, so the draft links need the new SHA.

2. NOTE (11310, owner Choice). The reach of the new boost is wider than the ticket. At base, `QueryElevationComponent.java` lines 773 and 780-781 add the elevation sort only when `forceElevation` is on (default false, line 131) or the sort is by score. The rerank boost does not check either rule. At head, `ReRankCollector.java` lines 181-182 sort the window with `BoostedComp` before the page is cut at lines 187-221. So an elevated document from anywhere in the window (up to `reRankDocs`) can reach the first page. The draft poses this as the Choice. No branch change.

3. NOTE (11310, local refs). Do not open or push from the local branches. Local `solr-11310-submit` is at `8920648ddeb`, 362 behind and 1 ahead of origin, on a different lineage. Local `wt-solr-11310-submit` (`5b19fe4e1ef`, the worktree at `wt/SOLR-11310`) has one extra commit, "add open-questions note for the owner (remove before the PR)", which adds `OPEN-QUESTIONS-SOLR-11310.md`. That file is not on origin. Open only from `origin/solr-11310-submit` at `e38ddec5279`. Local `wt-solr-15479-submit` (`86a026aa5bd`) is also a different lineage (17 behind, 3 ahead) and carries its own "testing handoff" commit. Same rule: open only from `origin/solr-15479-submit`.

4. NOTE (both). Gate logs named in the receipts are not on disk. Those are `g11310-harden.log`, `g11310-harden2.log`, `g11310-harden3.log`, `g11310r35-ab.log`, `g15479r35-gate-fix.log`, and the 15479 premise-step log. I checked `research/test-queue/logs` by name and found none for either ticket. The queue results folder has no file for either ticket. The counts and the fail-before results rest on the receipts alone. The drafts say so only by naming the check dates the receipts give.

5. FIX (15479, history). Commit `4a6ffa87358` ("SOLR-15479: report the max of the returned rescored scores for RankQuery in all four paths; tests and changelog") has `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>` in its message body (line 3 of the body). Your standing rule says no Claude trailers on commits pushed to your repo, and this branch is on the fork. Replacement: reword `4a6ffa87358` to delete that trailer line, or squash it. Either rewrite moves the head SHA. The receipt names `57bd53ce4d07`, so the Proof line in the draft must name the new SHA. Whether a new gate run is needed is the owner's call. No trailer appears on the 11310 branch.

6. FIX (15479, history). Two commit subjects are process vocabulary on the PR commit list: `b21e1b940fa` ("SOLR-15479: add testing handoff") and `81602df492f` ("SOLR-15479: drop the TESTING.md handoff note"). The first adds `SOLR-15479-TESTING.md` (+54 lines, stat checked) and the second removes it (-54 lines, stat checked). Dropping both leaves the file content unchanged. Replacement: drop both commits. Also squash the two "apply tidy formatting" commits (`59997ea9101`, `57bd53ce4d0`) into the feature commits, since they record formatting catches. This rewrite also moves the head (see finding 5). Optional for 11310: `e38ddec5279` ("tidy formatting for TestLTRReRankingPipeline") can be squashed the same way.

7. FIX (15479, changelog). `changelog/unreleased/SOLR-15479.yml` line 1, at `57bd53ce4d0`. The title says maxScore "reflects the rescored documents". Two of the new tests show otherwise. In `testRerankMaxScoreOutsideWindowMatch` (`TestReRankQParserPlugin.java` lines 252-319, assertions at 286-287), doc 1 keeps its untouched score of 5.0 and sets maxScore. It is outside the rerank window. In `testRerankMaxScoreBeyondReturnedPage` (lines 322-362, assertions at 357-358), doc 128 is not on the page and sets maxScore at 28.0. Replacement title: "maxScore of a response that uses a rerank query (rq) now uses the final scores after reranking, not the scores seen before reranking".

8. FIX (15479, javadoc). `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java` lines 1829-1830, at `57bd53ce4d07`. The javadoc says the max is taken "among the returned docs". The code takes it from `topDocs`, which is the collected window (`topCollector.topDocs(0, len)` at lines 1930 and 2055, where `len` is `supersetMaxDoc`), not the returned page. Replace lines 1829-1830 with:
   `   * during collection can be stale. Returns the highest score among the docs in topDocs, which is`
   `   * the collected result window and can reach past the returned page. Falls back to the collected`
   `   * maximum when no doc in topDocs carries a score.`

9. FIX (15479, test comment). `TestReRankQParserPlugin.java` lines 263-265, at `57bd53ce4d07`. The word "window" means the rerank window in one comment and the collected result window in another. Line 264-265 says maxScore "follows the final scores of the returned documents", which contradicts lines 290-294 and 332-336. Replace lines 263-265 with:
   `      // match outside the rerank window (doc 1, final score 5.0) is returned with its`
   `      // untouched score, and it supplies the reported maxScore. maxScore covers the`
   `      // collected result window, so a document outside the rerank window can set it.`

10. NOTE (15479, uncovered path). The zero-collection branches still report the pre-rerank maximum. In `SolrIndexSearcher.java` at `57bd53ce4d07`, `getDocListNC` takes the `lastDocRequested <= 0` branch at line 1865 and sets maxScore at line 1905. `getDocListAndSetNC` does the same at lines 1986 and 2024. Neither calls `rescoredMaxScore`. `supersetMaxDoc` is 0 when `rows=0` and the query result cache is not used for the request (lines 1567-1568 and 1611-1614, and the cache-off flag at 1560-1563). None of the three new tests uses `rows=0` (their row counts are 10, 10, 1 and 2). Replacement: the Limits line in the draft names it. Alternatively, fix it before opening. That would be a new change and needs a new gate.

11. NOTE (15479, other rank queries). The change applies to every `RankQuery`, not only rq. Subclasses include `AbstractReRankQuery` (`ReRankQuery` in `ReRankQParserPlugin.java`, and `LTRQuery` in the LTR module), `ExportQuery` (`ExportQParserPlugin.java` line 67), and test rank queries. The LTR module has no test that mentions maxScore. Core tests that mention both a rank query and maxScore: `RankQueryTestPlugin.java` (12 mentions, plugin code), `SolrIndexSearcherTest.java` (2), `MergeStrategyTest.java`, `QueryEqualityTest.java`, `TestQueryTypes.java`. None of these was run or read in full. Replacement: none. Listed under Not checked.

12. NOTE (15479, count). `TestReRankQParserPlugin.java` line 75, `testIntrospection`, has no `@Test` annotation. The same holds at base. The file has 13 `@Test` annotations at head (10 at base) but 14 methods named `test*` (11 at base). The receipts' counts (14 for 15479, and 11 for the base file in the 11310 receipt) match the named-method count. I did not see the JUnit XML. The draft states the receipt's 14 as recorded. Replacement: none, unless the owner wants the XML checked.

13. NOTE (interaction, 11310 with 15479). The two diffs do not overlap in any file. 11310 touches `ReRankCollector.java`, `TestLTRReRankingPipeline.java` and its changelog. 15479 touches `SolrIndexSearcher.java`, `TestReRankQParserPlugin.java` and its changelog. The 11310 change only reorders documents: `BoostedComp` is used only as a comparator (head lines 321-349). The 15479 change only reads score values. So maxScore is the same whichever lands first. A trial merge, `git merge-tree --write-tree origin/solr-11310-submit origin/solr-15479-submit`, exits 0 with no conflicts. The result is tree `2d6b77378f59185c8132d25fd7fd0d2e309a2f0f`, written as an object with no ref. Against the 11310 head, its only differences are the three 15479 files (199 insertions, 8 deletions). The claim says both share `TestReRankQParserPlugin`. That is a shared run, not a shared file: 11310 does not change that file, and its receipt runs it as a regression check. The combined tree has not been gated, and neither receipt covers it.

14. NOTE (interaction, consequence). After 11310, the first result can have a lower score than maxScore. This is normal for any sort and does not conflict with 15479.

## Task results

**SOLR-11310: draftable, held.** The live head `e38ddec5279` and base `14c7aac0d151` match the receipt. The change is one removed `break` in `BoostedComp` (`ReRankCollector.java` base lines 331-332), plus a new test in the LTR module and a changelog. The receipt records 4 of 4 for `TestLTRReRankingPipeline` (I count 4 `@Test` methods at head, which agrees) and 11 of 11 for `TestReRankQParserPlugin`, both at head, checked 2026-10-03. It also records the new test failing at base with `expected:<[1]> but was:<[0]>`, in the round 35 check dated 2026-10-07. The receipt dates the 4 of 4 and 11 of 11 counts to the gate run on 2026-10-03; the draft uses those dates. Verdict: draftable. Hold the opening until the owner confirms the field-sort position (finding 2) and the title fix (finding 1) is applied. The draft is at `pr-drafts/search-components/SOLR-11310.md`. It is 4,135 characters with links and 3,518 without, a little over the guide.

**SOLR-15479: draftable, not ready to open.** The live head `57bd53ce4d07` and base `b5c71bc5573` match the receipt. The three new tests are the only additions to `TestReRankQParserPlugin.java`, which fits the receipt's 14 and the 3 failures on base. Blockers: findings 5 and 6 (history), 7 (changelog title), and 8 and 9 (javadoc and comment). The owner must also settle the window scope (Choice in the draft) and whether finding 10 is listed in Limits or fixed. The draft is at `pr-drafts/search-components/SOLR-15479.md`, at 3,201 characters. The GitHub run cited in the receipt (37688766357) is left out of the draft because I could not check it.

## Owner decisions

1. SOLR-11310: confirm a field-sorted rerank window boosts elevated documents when `forceElevation` is off (option 1), or choose option 2 in the draft. The changelog title (finding 1) follows the answer. Do not open before this.
2. SOLR-15479: confirm maxScore covers the collected result window (option 1), or the returned page only (option 2).
3. SOLR-15479: keep the rows=0 path in Limits, or fix it first (finding 10).
4. SOLR-15479: approve the history rewrite (findings 5 and 6). It moves the head. Say whether to re-run the gate on the new head.
5. Landing order: there is no code dependency, and the trial merge is clean. Open 15479 first, once findings 5 to 9 and decision 2 are done. Open 11310 after decision 1. The second PR rebases on the first, with no conflict expected.
6. Open both PRs only from `origin/solr-11310-submit` and `origin/solr-15479-submit`, never from the local worktree branches (finding 3).

## Not checked

- No builds, tests, or gate runs. The proof counts and fail-before results are from receipts only. I could not confirm them from the logs.
- The round 35 goal files named in the receipts (`goal files/reviews-2026-10-07-round35-bulk/11310.md` and `15479.md`) and the takeover log entries. A full-tree search for the gate log names and the round 35 file names under the Solr-issues root returned no matches. The takeover log entries were not searched for.
- The GitHub run 37688766357 cited in the 15479 receipt. No gh calls were made in this part.
- Live PR state for 11310 and 15479. No gh calls were made. The claim lists no live PR for either.
- Whether any caller wraps a rank query so that `cmd.getQuery()` is not a `RankQuery`. The head check is on `cmd.getQuery()` only.
- The `/export` path (`ExportQuery`) and whether it reaches `rescoredMaxScore`.
- The multi-threaded collection code (`MultiThreadedSearcher`), which I did not read.
- The core tests in finding 11, which mention rank queries and maxScore. I did not read or run them.
- The shard (distributed) rerank path and whether it uses `BoostedComp` differently.
- The JUnit XML for 15479 (finding 12).
- Lucene. The drafts name no Lucene behavior, so the 9.x and 10.x check was not triggered. The repo pins Lucene 10.4.0 at base (`gradle/libs.versions.toml` line 39). The 9.x line was not checked. I did not read Lucene's `QueryRescorer` ordering, and the drafts do not depend on it.
- Jira live state. I read the local hydrated packets `research/jira-context/SOLR-11310.json` and `SOLR-15479.json` only.
- Checked and found no problem: the 11310 test helpers (`makeFieldValueFeatures`, `TestLinearModel.createLinearModel`, `makeFeatureWeights`, `LTRScoringQuery.setRequest`, `QueryCommand.setLen`) exist at base with the signatures used. `getBoostDocs` accepts a null request context. Elevation tests that combine rerank with `elevateIds` exist only in `TestReRankQParserPlugin` (base lines 475-600), and the LTR module has none.
