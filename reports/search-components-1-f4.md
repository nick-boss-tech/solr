# Search components round 1, part f4 (SOLR-6831 and SOLR-12556)

Result: SOLR-6831 is draftable after two small fixes (changelog wording, test control comment). SOLR-12556 is held: the code matches the design, but the changelog title overstates the change and two Proof items are unconfirmed.

Scope: read only. Claim `claims/search-components-1.md`, assignment `assignments/search-components-1.md`, `pr-formula.md`. Heads checked with `git ls-remote origin` on 2026-10-09: `solr-6831-submit` at `96b33ba5f6f834038e570d0b93f8684dc887fbe8` and `solr-12556-submit` at `033ec65a0e1bc12fd16d65b77a5b19458c837040`. Both match the claim table and the receipts. No build, no test run, no gh write call, nothing posted, nothing committed.

Drafts written (not posted):
- `pr-drafts/search-components/SOLR-6831.md` (head `96b33ba5f6f8`)
- `pr-drafts/search-components/SOLR-12556.md` (head `033ec65a0e1b`)

## Findings

1. FIX (SOLR-12556). Changelog title, first sentence is true only for buckets known before refinement.
   - File: `changelog/unreleased/SOLR-12556-processempty-refinement-completeness.yml`, lines 2-3 at head `033ec65a0e1b`.
   - Evidence: `FacetRequestSortedMerger.java` L189-L191 (`firstSeenDuringRefinement`) and L178 (old rule for those buckets). A bucket numbered at or above `phase1BucketLimit` skips the new rule. The new test `testProcessEmptySubFacetBucketsFirstSeenDuringRefinement` (`TestJsonFacetRefinement.java` L1199-L1260) expects child buckets under `pY` and `pX` with `debug:1`, so they are returned with stats from one shard. Round 15 review (`research/branch-reviews/round-15/SOLR-12556-review.md`, "Changelog and hygiene") says the same. The receipt says "the PR text should say so", but the changelog is part of the PR, so the wording must change there too.
   - Replacement for the whole title block (lines 2-5, with finding 2 applied):
     ```
       JSON Facet field refinement with processEmpty:true now drops buckets known before refinement that were not refined against every shard that returned the facet, instead of returning them with partial stats. Buckets first seen during refinement keep their previous behavior.
     ```

2. FIX (SOLR-12556). Changelog title, second sentence describes no change from base.
   - File and lines: same file, lines 4-5.
   - Sentence: "Shards that never return the facet (for example because their domain is empty) no longer cause all of its buckets to be dropped."
   - Evidence: base (`14c7aac0d15`) `FacetModule.java` L300-L306: a shard response with no `facets` key is skipped before `merge` runs. Base `FacetRequestSortedMerger.java` L51-L57: only a shard that reports `more:true` sets its bit in `shardHasMoreBuckets`, and L151-L163 uses only those bits. So base never dropped buckets because a shard did not return the facet. The example is also wrong for `processEmpty`: base `FacetProcessor.java` L473 does not skip sub-facets for an empty domain when the parent has `processEmpty`, so an empty-domain shard does return the facet and counts under the new rule. The head comment at `FacetRequestSortedMerger.java` L41-L43 notes this case only for a parent without `processEmpty`.
   - Replacement: delete the sentence. Finding 1's replacement already does this.

3. FIX (SOLR-12556). Test count in Proof does not match the file.
   - Receipt `receipts/SOLR-12556.md` L6: "TestJsonFacetRefinement 13 tests, 0 failures, 0 errors, 1 skipped".
   - File: `solr/core/src/test/org/apache/solr/search/facet/TestJsonFacetRefinement.java` at head. It has 12 `@Test` lines. One of the 13 `public void test` methods, `testIndexAscRefineConsistency` (L2241), has no `@Test`; it has none in base either. The skipped one is the `@AwaitsFix` test at L1112-L1113. So the file gives at most 12 annotated tests, and the receipt's 13 is not explained by the file.
   - Replacement: do not quote 13 until the JUnit results for the final gate are read. If they show 12 tests, 0 failures, 0 errors, 1 skipped, write exactly that. The draft has a CONFIRM marker in this place.

4. FIX (SOLR-12556). The fail-before test is not named in any record that can be checked.
   - Receipt `receipts/SOLR-12556.md` L7 names the symptom only ("a bucket value mismatch in the merged top buckets"), and the decide log `g12556-decide.log` is not on disk.
   - The round 15 review names `testProcessEmptyRefinement` as the fail-before test, but that is a reading at the older head `156117f4bb7`, not a run. In base, `testProcessEmptyRefinement` carried `@AwaitsFix` (base `TestJsonFacetRefinement.java` L938), which fits it, but that is not proof.
   - Replacement for the Proof sentence, once the base run log confirms the test: "On the base production code with this test file, `<test name>` fails with the ticket symptom." Until then, keep the CONFIRM marker. Do not name a test from memory.

5. NOTE (SOLR-12556). Local ref and pipeline queue entry are stale.
   - Local `solr-12556-submit` is at `ebaaebd708a9`, an ancestor of origin `033ec65a0e1b` with 7 commits behind. Its tip also adds `SOLR-12556-TESTING.md`, which origin removed at `b5267a0a7da`.
   - `research/pipeline/queue.json` (around L4983-L4992, in the Solr-issues root) names commit `ebaaebd708a9` and "test unrun" for SOLR-12556. It is older than the receipt.
   - Replacement: none in the draft. The owner should refresh the local ref and the queue entry. Do not push from the local ref.

6. FIX (SOLR-6831). Changelog parenthetical leaves out `memAllowed`.
   - File: `changelog/unreleased/SOLR-6831-pivot-facet-limits.yml`, line 2.
   - Evidence: `PivotFacetProcessor.java` L331 calls the shared `QueryLimits.maybeExitWithPartialResults`. `QueryLimits.java` (unchanged on this branch; base L78-L79) adds a `MemAllowedLimit` when `memAllowed` is set. So the new check covers `memAllowed` too, which is a wider behavior than the changelog names.
   - Replacement for line 2:
     ```
       Pivot faceting now checks the request's query limits (timeAllowed, cpuAllowed, memAllowed) between pivot values, returning partial results instead of continuing to build the whole pivot tree after the limit is exceeded.
     ```

7. FIX (SOLR-6831). Test comment and control assertion assume a whole-collection count.
   - File: `solr/core/src/test/org/apache/solr/search/TestQueryLimits.java`. Comment L137-L138 says `distrib=false` keeps the request on one shard replica. Comment L146-L147 says "every value of val_i (5 values across the 100 indexed docs)". Assertion L152: `assertEquals("expected one top level pivot per val_i value", 5, controlPivots.size())`.
   - Evidence: the collection is created with 3 shards and 2 replicas (L47, `createCollection(COLLECTION, "conf", 3, 2)`). With `distrib=false` the answer comes from one shard, about a third of the 100 docs. The branch does not show that every shard holds all 5 values. If the answering shard lacks a value, L152 fails. The receipt has one passing run.
   - Replacement for L146-L147: `// control: with no limit installed, the top level pivot list for val_i is not empty`. Replacement for L152: `assertFalse("expected top level pivots in the control", controlPivots.isEmpty());`
   - This is a test change, so the focused proof must be run again before the draft is posted. Owner decides.

8. NOTE (SOLR-6831). Proof must not count the first new test as a fail-before proof.
   - `testPivotFacetRespectsLimits` (TestQueryLimits.java L118-L132) checks only the partial header and the trip detail. The receipt shows exactly one failure in the four-test run (`testPivotFacetTruncatesWhenLimitsTrip`), so the first test passes on base too.
   - The draft says so. Replacement wording if the owner wants it shorter: "testPivotFacetRespectsLimits passes with and without this change."

9. NOTE (SOLR-6831). Local branch is not the gated head.
   - Local `solr-6831-submit` is at `88444d51ab8`: 3 commits not on origin, origin has 2 commits not on local. Local adds `SOLR-6831-TESTING.md` (a handoff note, 28 lines) and a different `TestQueryLimits.java` (8 insertions, 59 deletions against origin).
   - The reviewed and gated head is origin `96b33ba5f6f8`. Do not push or reset from the local ref. The push would be rejected as non-fast-forward, so do not force it.

10. NOTE (interactions). No file overlap in this pair or with the nearby branches.
   - SOLR-6831 touches `PivotFacetProcessor.java`, `TestQueryLimits.java` and its changelog. SOLR-12556 touches `FacetMerger.java`, `FacetModule.java`, `FacetRequestSortedMerger.java`, `TestJsonFacetRefinement.java` and its changelog. No shared file, so no landing-order constraint between them.
   - SOLR-6193 (part f3) touches `PivotFacet.java`, `PivotFacetField.java`, `PivotFacetValue.java` and a large test. It does not touch `PivotFacetProcessor.java`, so no hunk overlap with 6831. SOLR-11129 (f2) touches `FacetComponent.java` and one test file, not the pivot processor.
   - SOLR-17051 (f5) touches `FacetFieldMerger.java` (missing bucket output, L139-L140 at head). SOLR-12556 does not edit that file, but its code path runs through it: `FacetFieldMerger.java` L122 is the only caller of `isBucketComplete` in base. The 12556 Limits sentence "only field facets with refine:true use this check" depends on that. If 17051 or another branch adds a caller, that sentence changes.

11. NOTE (SOLR-12556). Round 15 findings are resolved at head by design.
   - Round 15 finding 1 (late buckets always complete): at head, late buckets use the base rule (`FacetRequestSortedMerger.java` L178).
   - Round 15 finding 2 (mergers created during refinement use the strict rule): at head the boundary is on the merge context, set in `FacetModule.java` L200 before the refinement loop, and read in `firstSeenDuringRefinement` (L189-L191). The fix matches round 15's "simplest cover".
   - The three new tests (L1199, L1339, L1391) check outcomes. By reading, their expectations equal base's rule for late buckets, so they may pass on base as well. The receipt does not say. The draft does not claim they fail on base.

## Task results

SOLR-6831: draftable, with fixes 6 and 7 before posting. Head `96b33ba5f6f8`. `TestQueryLimits` 4 of 4 (receipt). The new `testPivotFacetTruncatesWhenLimitsTrip` fails on base production code with the new test file, 1 failure in the four-test run (receipt). The premise holds by reading: the pivot value loop had no limit check at base (`PivotFacetProcessor.java` L328-L345 at base), while other components check limits. The change is small (88 insertions in 3 files). The draft at `pr-drafts/search-components/SOLR-6831.md` uses the head, states the memAllowed behavior, and puts the searcher-level route in Limits, not in a choice section (narrow scope on a small patch is not a choice under `pr-formula.md`). Verdict: draftable; hold posting until fixes 6 and 7 are decided.

SOLR-12556: held, not postable yet. Head `033ec65a0e1b`, 5 files. The code matches the design the owner ruled (option 1 from round 15): the phase 1 boundary is recorded once on the merge context, buckets known before refinement use the processEmpty completeness rule, buckets first seen during refinement keep the base rule, and round 15's two findings are fixed at head (finding 11). The changelog is wrong in two places (findings 1 and 2), and the Proof has an unexplained count (finding 3) and an unnamed fail-before test (finding 4). The draft at `pr-drafts/search-components/SOLR-12556.md` has CONFIRM markers for findings 3 and 4 and a "choice to check" question about keeping late buckets with partial stats. Verdict: draftable once findings 1 to 4 are resolved; the choice section is optional and the owner decides whether to keep it.

## Owner decisions

- SOLR-12556 changelog: approve the title replacement (finding 1) and the deletion (finding 2) as a changelog-only branch commit. The reviewer did not commit anything.
- SOLR-12556 Proof: someone with the gate logs must confirm the test count (finding 3) and the fail-before test name (finding 4) before the draft is posted. The logs are not on disk.
- SOLR-12556 choice question: keep it (asks maintainers whether late buckets should keep partial stats, as base does) or drop it (the owner already ruled option 1).
- SOLR-6831 changelog: approve adding memAllowed (finding 6).
- SOLR-6831 test: decide on the control comment and assertion (finding 7). That is a test change, so the focused proof must be run again.
- Local and queue state: decide whether to reset local `solr-6831-submit` and `solr-12556-submit` to origin and refresh the SOLR-12556 queue entry (findings 5 and 9).
- Posting: nothing is authorized. No PR exists for either branch as far as the inventory and the round 28 review show. Drafts stay local until the owner says otherwise.

## Not checked

- No build and no test run, so compilation of the new `maybeExitWithPartialResults` call in `doPivots` was not verified. `QueryLimitsExceededException` extends Lucene's `ExitableDirectoryReader.ExitingReaderException`; the Lucene class was not read from source, so whether it is unchecked was not confirmed. `doPivots` declares only `IOException`.
- Gate logs are not on disk: `g6831-gate.log`, `g6831-premise4.log`, `g12556-gate3.log`, `g12556-decide.log`, `g12556-opt1b.log`. All proof counts come from the two receipts only.
- The 13 versus 12 test count for SOLR-12556 is not reconciled (finding 3).
- Whether the new 12556 tests and the other 6831 test pass or fail on base was not run.
- The searcher path the 6831 ticket names was not traced at head. At head, `SolrIndexSearcher.getDocSetNC` (L1447-L1448) delegates to `DocSetUtil.createDocSet`; the place where the timeout exception is now caught was not followed.
- The coordinator merge of partial pivot lists (`PivotFacet.java`, part f3 territory) was not traced.
- No `gh` call. Whether a public PR exists for either branch was not searched. The inventory (2026-10-08) and round 28 review (2026-10-07) found none. The GitHub run cited in the 12556 receipt (37269903553) was not checked.
- Lucene version claims: neither draft names Lucene behavior, so the 9.x and 10.x check was not needed. Nothing was checked against either line.
- Changelog YAML was read by eye, not parsed. The 6831 receipt says it parses; the 12556 receipt does not say so.
- SOLR-16290 (exclusion recompute, held pin) was not read. Its relation to 12556's refinement area is for part f5.
