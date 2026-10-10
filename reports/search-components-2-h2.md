# Search components round 1, sub-batch 2, part h2 (SOLR-13568 and SOLR-13876)

Result: SOLR-13876 is draftable (draft at `pr-drafts/search-components/SOLR-13876.md`, head 2e110473dbad), held on two FIXes to the branch; SOLR-13568 consistency pass: branch, receipt and live PR #5014 agree on the head and counts, and the live body has one process word and several unmeasured claims, flagged with no PR text drafted.

## Findings

1. FIX. SOLR-13876, test comment. File `solr/core/src/test/org/apache/solr/handler/component/TestExpandComponent.java`, lines 944-945 at 2e110473dbad. The comment says NaN "is serialized as a missing maxScore". That is wrong for the XML response. Evidence from code: `TextResponseWriter.writeVal` sends a `DocList` (line 195) to `writeDocuments`, which passes `res.wantsScores() ? ids.maxScore() : null` (line 250). `ResultContext.wantsScores()` needs `getDocList().hasScores()` (lines 58-63), and `DocSlice.hasScores()` is `scores != null` (line 95-97). A non-empty group with `score` requested therefore has a non-null boxed NaN. `XMLWriter.writeStartDocumentList` writes `maxScore` with `Float.toString`, which gives `"NaN"` (lines 160-161). The base code passes `Float.NaN` at `ExpandComponent.java` line 787 (b5c71bc5573). The base test at line 638 asserts no maxScore only for `expand.rows=0`, where scores are null. Exact replacement for lines 944-945:
   `    // SOLR-13876: an expanded group must report the maximum score of its documents when`
   `    // scores are requested. Before this change the XML response wrote maxScore="NaN".`

2. FIX (owner call; branch is not yet a PR). SOLR-13876, commit messages. Commits `ce19cf78ae9` and `dfde3c56df8` use process words in public text: "from the gated local rebuild", "Port of the review agent version", "identical to the gated local rebuild". The body of `ce19cf78ae9` also says "without the fix the expanded result carries no maxScore at all", which Finding 1 shows is wrong. Evidence: `git log upstream/main..2e110473dbad`. Exact replacement: squash the three commits into one. Subject: `SOLR-13876: report the maximum score of each expanded group's returned documents`. Body: `Expanded groups reported maxScore as NaN when the request asks for scores. Report the highest score among the returned documents of each group. Add a test for a relevance sort and a non-score expand sort.` A squash changes the head, so the draft's head and links must be refreshed after it. I did not rewrite the branch.

3. NOTE. SOLR-13876, production comment. `ExpandComponent.java` lines 782-783 at 2e110473dbad say "Scores are set here, by the collector for a relevance sort or by populateScores for other sorts." The scores are filled in above that block (`populateScores` at line 771, collector scores before). Exact replacement for line 782: `            // The scores were set above: by the collector for a relevance sort, or by populateScores for`. Line 783 stays.

4. NOTE. SOLR-13876, test coverage. `testExpandMaxScore` (lines 942-992 at 2e110473dbad) sets `expand.rows` to 2 at lines 962 and 981 for group "x", which has two expanded documents (ids 2 and 3). Every expanded document is returned, so the test cannot tell slice-local from group-wide. A test that would separate them: a non-score `expand.sort` with `expand.rows` 1, where the highest scoring member (id 2, score 30) is not returned. The draft says so in Limits. Owner call, no test run.

5. NOTE. SOLR-13876, distributed path. `handleResponses` (lines 475-501) adds each shard's `expanded` entries unchanged, and `finishStage` (lines 504-520) passes them on. Nothing in the change tests `maxScore` across shards. The 2026-10-02 review (`research/branch-reviews/SOLR-13876-review.md`, finding 5) is still open at this head. The draft's Limits says so.

6. FIX (owner call; live PR #5014, no text drafted). PR body, Proof paragraph: "(local gate: tidy clean, Error Prone compile clean, `:solr:core:check -x test` green; ...)". "Gate" is internal process vocabulary under the public-text rule. Exact replacement phrase: `Checked locally at head ac5d60c214c on 2026-10-06: tidy, the Error Prone compile, and the core module check all passed.` The receipt names no Gradle task, so the task name is dropped. The body also says "first verified at head 6b89f649071 on 2026-10-04", which the receipt does not record (the receipt names only `e2cf2026a79` as the earlier ledger row). Drop it or keep it as the owner decides.

7. NOTE. SOLR-13568, live PR body, "What happens today". The phrase "fills the filter cache with one-off entries that evict useful ones" is not supported. The receipt and the test show the cache size grows by one entry per page (3 to 5 in the premise run). No eviction was measured. Replacement phrase: `so paging through expanded results adds one filter cache entry per page.`

8. NOTE. SOLR-13568, live PR body, "A choice to check". "the cached entries are rarely hit" is not measured. Replacement phrase: `the cached entries may rarely be hit; no hit rate was measured.`

9. NOTE. SOLR-13568, live PR body, Proof line. "asserts the per-page group queries do not land in the filter cache" overstates the test. `testPerPageGroupQueriesNotCached` (line 1118 at ac5d60c214c) compares the filter cache size after page 1 and after page 3 (lines 1137, 1142, 1145-1147). The 2026-10-02 review (finding 4) called this size check brittle. Replacement phrase: `and checks that the filter cache size does not grow across pages.`

10. NOTE. SOLR-13568, commit history. `84bc1a39323` "SOLR-13568: add testing handoff" and `d8631103f8b` "SOLR-13568: remove TESTING.md handoff file" are in the PR history. The file is added and then removed, and the net diff has 3 files with no TESTING file at head, so the tree is clean. The process word stays in the public commit messages. Rewriting history on a live PR needs the user's direction, so this is left as is.

11. NOTE. SOLR-13568, live PR body format. The body has no bold one-line summary under each section (the 2026-10-08 presentation rule), no blob links on file citations, and a plain-code changelog line. Exact replacement for the changelog line, if the owner edits: `Changelog: [changelog/unreleased/SOLR-13568.yml](https://github.com/nick-boss-tech/solr/blob/ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721/changelog/unreleased/SOLR-13568.yml)`

12. NOTE. SOLR-13568, PR checks. `gh pr checks 5014`: changelog check pass, gradle check pass, Crave.io test run pass, labeler pass, generate skipping. The output does not print head SHAs, so I did not confirm that each run is at ac5d60c214c. The receipt's run 37569706521 is not in the list. `mergeable` and `mergeStateStatus` read UNKNOWN.

## Task results

**SOLR-13876: draftable, after FIX 1 and FIX 2.** The draft is at `pr-drafts/search-components/SOLR-13876.md` and names head 2e110473dbad. Counts come from the receipt: 9 of 9 at head (round 35, 2026-10-07), and 7 of 9 failing on the base code with this test file, every failure a maxScore check. The logs are not on disk. The count of 7 fits the file: six test methods (`testString`, `testStringDv`, `testInt`, `testIntDv`, `testFloat`, `testFloatDv`) call `_testExpand` (lines 62-84 and 85 onward), which holds the new maxScore assertions at lines 762 and 788, and `testExpandMaxScore` makes the seventh. The draft does not name which test fails, because the receipt does not. The code path is sound: the score guard `scoreDocs.length > 0` at line 769 keeps `scores[0]` safe, and the slice-local maximum is computed only when `wantsScore()` is true. The Choice is posed with slice-local implemented. The draft says nothing about filter caching.

**SOLR-13568: consistency pass, no draft (live PR #5014).** The head `ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721` matches the branch, the receipt's gated head, and the PR's `headRefOid`. The receipt and PR agree on 9 of 9, on the premise result (1 failure, `expected:<3> but was:<5>`), on the changelog type `fixed`, and on the benchmark limit. The code is correct: `WrappedQuery` with `setCache(false)` (lines 439-440) routes the group filter to `notCached` in `SolrIndexSearcher.getProcessedFilter` (lines 1244-1252 at ac5d60c214c). The description drift is in Findings 6-12. No PR text was drafted.

**Interactions.** The two heads have no shared hunks in `ExpandComponent.java` (13568 at lines 435-441, 13876 at 780-799) and no shared test hunks (13568 near line 1118, 13876 at 942-992 and 762-788). Trial merges (`git merge-tree --write-tree`, tree objects only, no refs written) are clean in every case: 2e110473dbad onto ac5d60c214c gives tree `0a8c2cb49dd492bbcee514e5e4086fc5ae5e56c3`, the same tree in the reverse order. Each also merges cleanly onto current `upstream/main` (13568 gives `dfd10e9fb447`, 13876 gives `bdb0ad3e4e6e`). Neither file has changed on upstream since either base. The filter caching change cannot change scores: the group filter is a FILTER clause (`QueryUtils.combineQueryAndFilter`, lines 240-262), or a ConstantScoreQuery when the query is `*:*`, so it does not touch scores. Landing order: 13568 first (already live as #5014), then 13876. Both orders produce the same tree.

## Owner decisions

1. SOLR-13876 Choice: keep slice-local (the draft as written), or switch to group-wide. Group-wide needs scores for group members the page does not return, under a non-score sort. The cost was not measured.
2. SOLR-13876: apply FIX 1 (test comment) and FIX 2 (squash and clean commit messages) before opening. Either changes the head, so the draft needs a refresh.
3. SOLR-13876: add the test in Finding 4 (a higher scoring member outside the returned slice), or ship with the Limits line as drafted.
4. Landing order: 13568 (#5014) first, 13876 second. Both merge cleanly in either order.
5. Live PR #5014: whether to edit the body (Findings 6-9 and 11) and whether to rewrite history (Finding 10). I made no edit and posted nothing. Any description change belongs to the owner.

## Not checked

- No builds, Gradle, tests, or spotless or Error Prone runs. Counts come from the receipts only. The gate logs named in the receipts (`g13876r35-tests.log`, `g13876r35-premise.log`, `g13568-rereview-gate.log`, `g13568-rereview-premise.log`) are not on disk. I searched `research/`, `research/test-queue`, and the worktree receipts.
- The Jira ticket text for both tickets was not read. This session has no Jira access, and neither ticket is in the workspace's Jira CSV export. The 2026-10-02 review says the Jira premise does not define slice or group scope for 13876.
- No `git ls-remote` or fetch. The local `origin/solr-13568-submit` (ac5d60c214c) and `origin/solr-13876-submit` (2e110473dba) refs match the claim table. `upstream/main` is the local 8e62c268688, used only for trial merges.
- The XML `maxScore="NaN"` behavior was read from code, not run. The JSON form of a NaN maxScore was not confirmed: no `NaNFloatWriter` subclass was found under `solr/core/src/java`. The drafts make XML claims only.
- The distributed path was read (lines 475-520), not run.
- Lucene 9.x and 10.x were not checked. The draft names no Lucene behavior. The statement that Solr fills in scores only for returned documents under a non-score sort is Solr code (line 771).
- PR #5014 run head SHAs were not confirmed (see Finding 12). The receipt's run 37569706521 was not seen.
- "PR 13568" in the claim is the ticket. The live PR is #5014 per the claim table, so `gh` was run on 5014 only.
- The changelog YAML for both tickets was compared with the upstream fragment format and `changelog/logchange-config.yml` (type `fixed` is valid). It was not run through the changelog checker, which passed for #5014 only.
- The branch diffs contain no `ICLA pending` or `Solr Issues Workspace` placeholders.
