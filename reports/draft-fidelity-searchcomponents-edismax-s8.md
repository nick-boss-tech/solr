# Search components draft fidelity, slice s8 (edismax)

Assignment: draft fidelity review under the brief at the scratchpad path `brief-draft-fidelity.md`. Slice: `pr-drafts/search-components/` drafts SOLR-6831, SOLR-6975, SOLR-7390, SOLR-7498 and SOLR-7520. Worktree HEAD is `d627304e96b` ("Claim the queue buffer review, draft and RCA assignments"), as the lead instructed; the brief names `e84522fa5bc`, which is an earlier commit. Checks run 2026-10-11. Read only: no commit, push, PR call, build, Gradle run or test.

Sources used:
- Receipts: `receipts/SOLR-<n>.md` (all five present).
- Category round reports, by the section that covers each ticket: SOLR-6831 in `reports/search-components-1-f4.md` (roll-up `search-components-1.md`); SOLR-6975 in `search-components-2-h4.md` (roll-up `search-components-2.md`); SOLR-7390 in `search-components-3-w1.md` (roll-up `search-components-3.md`); SOLR-7498 in `search-components-3-w5.md` (roll-up `search-components-3.md`); SOLR-7520 in `search-components-4-s2.md` (roll-up `search-components-4.md`).
- Answers material: `material/` has no hits for any of the five ticket numbers, and there is no search-components answers file.
- Code: `git cat-file`, `git show <sha>:<path>` and `git diff` in the worktree. The worktree shares the object store of the Solr source checkout (`source/.git`), so no fetch was needed. Upstream is now `upstream/main` = `3f5d4c5bf8a`; the round reports used `8e62c2686882`.
- JIRA packets `research/jira-context/SOLR-<n>.json`, read for the symptom claims only (summary, description, comments).

## Verdicts

| Draft | Head checked (ls-remote tip, same as draft) | Verdict |
|---|---|---|
| SOLR-6831 | `solr-6831-submit` = `96b33ba5f6f834038e570d0b93f8684dc887fbe8` | DRIFT (2 items) |
| SOLR-6975 | `solr-6975-submit` = `761aa629bb83e241df2aefc114513797b7339522` | CONSISTENT (notes) |
| SOLR-7390 | `solr-7390-submit` = `7463dd006a7825db46f00b8a4790650c5bb51e61` | DRIFT (1 item) |
| SOLR-7498 | `solr-7498-submit` = `2050d8e447a733966d5f6e4742a1a8ea931a2b00` | DRIFT (2 items) |
| SOLR-7520 | `solr-7520-submit` = `10b6931e1c058d76392c4ba6d53b45bee251e3d2` | CONSISTENT (notes) |

All five live heads match the heads named in the drafts, so no head drift.

## SOLR-6831

Verdict: DRIFT (2 items).

1. Draft says: "The value loop in [`doPivots`](https://github.com/nick-boss-tech/solr/blob/96b33ba5f6f834038e570d0b93f8684dc887fbe8/solr/core/src/java/org/apache/solr/handler/component/PivotFacetProcessor.java#L330-L350) runs a subset search for every value at every level."
   - Evidence: The symptom is pre-change code, so it must link the merge-base and say so (pr-formula.md). The draft links head lines L330-L350, which include the check this change adds (head L331-L333, `maybeExitWithPartialResults("Faceting pivots")`). The merge-base of `96b33ba` with upstream/main is `97d973814336101e12475558d7419321c743de79`. At that commit the loop is `PivotFacetProcessor.java` L328-L345, and the subset search is L345 (`getSubset`). Round report f4 "Task results" cites the same base lines.
   - Replacement: "At the merge-base commit `97d973814336`, the value loop in [`doPivots`](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/handler/component/PivotFacetProcessor.java#L328-L345) runs a subset search for every value at every level."

2. Draft says: "Behavior change, stated openly: the check covers every limit the shared code knows, so `memAllowed` is included, not only `timeAllowed` and `cpuAllowed`."
   - Evidence: The draft is right on the code. `QueryLimits.java` at head adds `MemAllowedLimit` when `memAllowed` is set, and `PivotFacetProcessor` calls the shared `maybeExitWithPartialResults`. The branch changelog does not match. `changelog/unreleased/SOLR-6831-pivot-facet-limits.yml` at `96b33ba`, title line 2, reads "(timeAllowed, cpuAllowed)" and omits memAllowed. Round report f4 finding 6 is a FIX, and the owner must approve it. The draft's own reviewer block (lines 45-47) already flags it.
   - Replacement (changelog title, a branch file: change it only in a changelog-only commit after owner approval; the draft text stays as written): "Pivot faceting now checks the request's query limits (timeAllowed, cpuAllowed, memAllowed) between pivot values, returning partial results instead of continuing to build the whole pivot tree after the limit is exceeded."

Checked and consistent: the receipt counts (TestQueryLimits 4 of 4; one failure on base in `testPivotFacetTruncatesWhenLimitsTrip`) and the verification date 2026-10-06 (receipt L3-L7). The new tests are at head L118-L132 (`testPivotFacetRespectsLimits`) and L135-L187 (`testPivotFacetTruncatesWhenLimitsTrip`, cited as L135-L188). Both use `CallerSpecificQueryLimit`. The branch diff is three files (PivotFacetProcessor.java, TestQueryLimits.java, changelog), so the draft's statement that the searcher is untouched holds. The "What this change does" links (L328-L333 at head) show the read and the check. The draft has no Choice section; that matches f4 (a narrow-scope call on a small patch is not a choice under pr-formula.md section 4), and the searcher route sits in Limits with a follow-up offer.

Optional notes, not blocking the draft text:
- f4 finding 7 (owner decision; blocking before posting): the control comment (TestQueryLimits.java L146-L147) and the assertion (L152, expects 5 values) assume a whole-collection count, while `distrib=false` answers from one shard. A test change means the proof must be run again. The draft does not quote either line.
- The block after the `---` line ("NOT FOR POSTING") uses process words (gate, receipt, round 28 review, report finding F6 and F7). Delete the whole block before posting.

## SOLR-6975

Verdict: CONSISTENT (notes).

Checked and consistent:
- `ShardFieldSortedHitQueue.java` at head: same-shard branch keeps shard order (L85-L91); the multi-key loop continues while `c == 0` (L97-L102); the shard-name tie-break is L106-L108; the DOC comparator returns 0 (L123-L126). These match "What this change does", including the claim that the direction does not change the shard grouping.
- The file is unchanged between base `e432df19c4a` and upstream/main (empty diff), as the draft says.
- Test `DistributedQueryComponentOptimizationTest.java` L348-L362 at head: ascending and descending `_docid_` requests, `rows=20`, asserts size equals numFound and size above zero. The draft describes this correctly.
- Counts 10 of 10 and verification date 2026-10-05 match `receipts/SOLR-6975.md` L4-L6.
- Changelog `SOLR-6975-distributed-docid-sort.yml` exists at head and its title matches the draft.
- Choice: "reject distributed `_docid_` sorts with a clear error" is a live route with a real cost, as h4 owner decision 4 says. The question is pointed.
- Limits (multi-key sorts untested; payload fetch failure on both trees) match h4 items and receipt L7.
- The JIRA packet has the same request and the `IndexOutOfBoundsException: Index: 0, Size: 0` stack text.

Optional notes:
- h4 finding 13: SOLR-17976 changes the tie-break in the same file. Re-read this draft after 17976 lands; the "shard name" wording holds for either key.
- h4 finding 10 (round 28 LOW): the test does not assert the same-shard order. The draft does not claim it does; keep it that way.
- The test compares the returned count (capped at `rows=20`) with numFound. It holds only while numFound is 20 or less. The fixture size was not checked; the draft's wording is literal and accurate.

## SOLR-7390

Verdict: DRIFT (1 item).

1. Draft says: "Only the three test classes above were run for this change."
   - Evidence: The Proof names four classes: `ReturnFieldsTest` 15 of 15, `TestPseudoReturnFields` 31 of 31, `TestExplainDocTransformer` 1 of 1 and `TestCustomDocTransformer` 1 of 1 (`receipts/SOLR-7390.md` L6). Round report w1 "Not checked" lists the same four classes as the run set.
   - Replacement: "Only the four test classes above were run for this change. Other classes that send unregistered names were not run."

Checked and consistent:
- Head ls-remote matches. Base `SolrReturnFields.java` at `0cc328310f8` L386-L389 is the empty `else` with a commented-out throw (the draft's "base lines" link). Head `7463dd006a7` L386-L389 is the throw with `"Unknown DocTransformer: " + augmenterName`.
- `ReturnFieldsTest.java` at head L293-L300 has the `expectThrows` for `[xxxxx]` and for the alias form. Base L292-L297 had the ignore assertion (w1 finding 5).
- The default transformers list in Limits (explain, value, docid, shard, child, subquery, json, xml, geo, core) matches `TransformerFactory.java` L111-L120 at head.
- Changelog `SOLR-7390-unknown-doc-transformer.yml` exists at head (L1-L8) and its title matches the draft.
- Proof: the base run fails `testTransformers` with "Expected exception SolrException but no exception was thrown" and 15 tests, 1 failure (receipt L6-L7). Matches.
- Choice: keeping the silent behavior is a live route with a cost (requests that name a missing transformer would keep succeeding). It matches w1 owner decision 6. The JIRA second comment (14495672) raises the same silent-or-throw question.
- Verification date 2026-10-05 is present.

Optional notes:
- "With this change, at head `7463dd006a7`, `ReturnFieldsTest` passes 15 of 15": the gate ran at `72caa363e29`, which the draft discloses in the next sentence. w1 finding 6 confirms the code is identical. If the owner wants the count tied to the head exactly, write "at `7463dd006a7`, whose source and test files match `72caa363e29`".
- The `[elevated]` link (`QueryElevationComponent.java` L232-L239) points to upstream commit `8e62c2686882` and is labeled "upstream main". That commit is an ancestor of upstream/main, but current upstream/main is `3f5d4c5bf8a`. Relabel it "upstream commit" or link the current main. The lines hold the editorial marker factory (default name "elevated", L135).
- pr-formula.md presentation rule: the Limits section opens with a bullet, not a bold one-line summary.
- The changelog file name has a slug after the ticket number. Upstream main names its fragments that way, so this is not drift.

## SOLR-7498

Verdict: DRIFT (2 items).

1. Draft says: "`ExtractionBackendMetadataTest` 2 of 2 at `2050d8e447a733966d5f6e4742a1a8ea931a2b00`, verified [run date to confirm]."
   - Evidence: The placeholder is not a verification date (brief check 2). `receipts/SOLR-7498.md` L4 records the gated head with the date 2026-10-05 ("branch solr-7498-submit, 2026-10-05"), and L9 records the takeover log entry dated 2026-10-05. The receipt gives no separate run date. Round report w5 owner decision 1 asks the owner to confirm run dates; the draft's "Verified" date comes from the gate record.
   - Replacement: "`ExtractionBackendMetadataTest` 2 of 2 at `2050d8e447a733966d5f6e4742a1a8ea931a2b00`, verified 2026-10-05 at this head."
   - Owner to confirm that 2026-10-05 is the run date (outside the replacement text).

2. Draft says: "The loader passes that null on as the request's stream size ([ExtractingDocumentLoader.java L139](https://github.com/nick-boss-tech/solr/blob/2050d8e447a733966d5f6e4742a1a8ea931a2b00/solr/modules/extraction/src/java/org/apache/solr/handler/extraction/ExtractingDocumentLoader.java#L139))."
   - Evidence: This is pre-change symptom code. The branch diff from base `14c7aac0d151` to head changes three files, none of them `ExtractingDocumentLoader.java`. L139 (`.streamSize(stream.getSize())`) is identical at base and head. pr-formula.md says symptom links use the merge-base and the text says so; the draft does this for `ContentStream.java` (base) and for `ExtractionBackend.java` L55 ("at base"), but not here.
   - Replacement: "The loader passes that null on as the request's stream size ([ExtractingDocumentLoader.java L139 at base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/modules/extraction/src/java/org/apache/solr/handler/extraction/ExtractingDocumentLoader.java#L139))."

Checked and consistent: `ExtractionBackend.java` base L55 (`String.valueOf(request.streamSize)`) and head L55-L58 (`if (request.streamSize != null)`); `ContentStream.java` base L39 (`Long getSize(); // size if we know it, otherwise null`); test names `testUnknownStreamSizeIsNotAdded` (head L50) and `testKnownStreamSizeIsAdded` (L58); counts 2 of 2, 2 of 2 and 6 of 6 match receipt L6; changelog `SOLR-7498-extract-null-stream-size.yml` exists at head with type `fixed`, and its title matches the behavior; the JIRA packet has the "null" error text and the post-tool remark. Limits match w5 (the metadata-level test, existing "null" values need reindexing).

Optional notes:
- No Choice section. w5 could not find a live alternative without tracing the SolrJ path, so none is drafted. Consistent.
- w5 finding 7: a missing Content-Length (`SolrRequestParsers.java` base L465-L467) may be the root cause, so a client-side fix could be the real fix. Limits says the cause "was not traced" and offers no route. Name the client-side route in Limits if the owner wants it there (w5 owner decision 2).

## SOLR-7520

Verdict: CONSISTENT (notes).

Checked and consistent:
- `CommandHandler.java` at head L259-L268: the `try` wraps `searcher.search`, and the `finally` (L262-L267) calls `complete()` when a post filter is set. This matches the draft's citation and "What this change does".
- Base `14c7aac0d151` L254-L257 sets the post filter as the collector, and base `CommandHandler.java` has no `complete()` call (grep empty). So "nothing calls complete() afterwards" holds.
- `DelegatingCollector` has `complete()` (base L111) and no `finish()`. The JIRA packet names `finish()` and `searchWithTimeLimiter`, so "The ticket names the method finish; the current name is complete()" holds.
- The non-grouped path calls `complete()` in `SolrIndexSearcher` (base L331-L334, L1200), as the draft says.
- The branch diff from base touches only `CommandHandler.java`, `AnalyticsMergeStrategyTest.java` and the changelog, so the coordinator is untouched.
- `AnalyticsMergeStrategyTest.java` L93-L106 at head holds the one new `assertNotNull` on a first-phase shard response, matching the draft.
- Counts 1, 1, 17 and 2, and the date 2026-10-05, match `receipts/SOLR-7520.md` L4-L7.
- Changelog `SOLR-7520-grouping-postfilter-complete.yml` exists at head; its title matches the behavior.
- Limits matches s2 (coordinator merge strategies not run for grouped responses, with a follow-up offer; the new check covers one shard). s2 names no choice owed; the draft has none.

Optional notes:
- The head history contains commit subjects with process words: `3abc1f0bacd` "SOLR-7520: add hypothetical-reproduction handoff doc" is an ancestor of `10b6931e1c05`. These are not in the draft text. s2 finding 5 says to squash or reword before opening, which changes the head. If that happens, every `10b6931e1c05` link in this draft must be re-pointed (s2 owner decision 8).
- pr-formula.md presentation rule: the Proof and Limits sections open without a bold one-line summary.

## Not done

- No build, test, gate run, test-queue command, gh call or Jira write. Live Jira status, live PR state and CI were not checked. JIRA packets were read for the symptom wording only.
- Citation lines were read with `git show <sha>:<path>` from the shared object store. Nothing was fetched, and the GitHub links were not opened.
- The changelog YAML files were read by eye, not parsed.
- Not checked: the SOLR-6975 fixture size behind the numFound assertion; the upstream content of `QueryComponent.java` and `FacetComponent.java` beyond the existence of the maybeExitWithPartialResults calls; whether the `[elevated]` lines still match current upstream/main (they do, at `8e62c2686882` and current main).
- No answers material exists for these tickets in `material/`.
- Other drafts and reports in `pr-drafts/search-components/` were not checked.
