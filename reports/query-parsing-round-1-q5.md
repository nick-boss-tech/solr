# q5 report: childfield pair (SOLR-12871, 17311) and SOLR-15615

Result: 17311 draftable; 15615 draftable after one comment FIX; 12871 held for a changelog FIX (clients already get the same 400 on main); the three heads merge cleanly with each other.

Heads checked (origin/* refs, matching the claim): 12871 `c79a49320cd`, 17311 `9f7524582f9`, 15615 `be77267bf55`. Line references are to those heads, or to `upstream/main` (`8e62c268688`) where marked. Ticket text came from `research/jira-context/SOLR-<n>.json` on disk, not a live Jira call.

## Findings

1. **FIX.** `changelog/unreleased/SOLR-12871-childfield-sort-rewriteable.yml`, line 2 (at c79a493). The title says the sort "is now rejected with a clear 400 error instead of a Lucene UnsupportedOperationException." Evidence: in `SortSpecParsing.java` (unchanged on the branch) the `vs.getSortField(top)` call sits inside the try at lines 113-126. The `catch (Exception e)` at line 123 stores the error as `qParserException`. Lines 157-164 then throw a 400 with the generic message "sort param could not be parsed as a query, and is not a field that exists in the index". Both base and branch take that path, so the message and status are the same. For a 400, `ResponseUtils.java` line 84 skips the stack trace, and lines 113-115 stop before the cause chain. `SolrException.getResponseMessage()` returns only `getMessage()` (`SolrException.java` lines 132-134). `TestPointFields.java` line 4645 already asserts that generic message reaches clients for a function sort. The only client-visible change is `metadata.root-error-class` (`ResponseUtils.java` lines 77-79). Replacement for line 2: `  Sorting by childfield() on a field type whose sort needs rewriting (such as CurrencyFieldType) is now rejected before the unsupported sort is built, and the server log names the field and its type.`

2. **FIX.** `solr/core/src/test/org/apache/solr/search/mlt/CloudMLTQParserTest.java`, line 208 (at be77267). The comment says "similar docs from the *other* collection (13, 14, ...)". Docs 13 and 14 are indexed into COLLECTION in `indexDocs` (lines 83-84). `mlt-collection-2` holds only doc 100 (lines 170-177). The assertion itself is sound: without the fallback the query returns a 400 and the test fails. Replacement for line 208: `    // similar docs from COLLECTION (13, 14, ...) are found; the source doc 100 is not`. This is the branch's own new code, so the fix goes on the branch.

3. **NOTE.** 12871 test coverage. `TestNestedDocsSort.java` lines 93-102 (at c79a493) assert only the cause chain: the 400 code, then the field name, type name, and message text. No test covers the client response. The assertions are correct for what they check. The owner decides whether the client path needs more (Owner decision 1).

4. **NOTE.** 12871 scope. The ticket asks why sorts cannot be made to work by rewriting underneath the sort (`research/jira-context/SOLR-12871.json`, Description). `research/pipeline/skips.md` line 917 says the real fix needs a rewrite-API design decision. The branch rejects these sorts and does not support them. The draft puts this in Limits, as `pr-formula.md` section 4 says for scope questions.

5. **NOTE.** 12871 `ChildFieldValueSourceParser.java` line 205 (at c79a493). A rejected sort now logs at ERROR with a stack trace, through the existing `log.error("can't parse {}", ...)` catch. The other childfield errors already do this. Nothing to change; flagged for awareness.

6. **NOTE.** Commit subjects that name an internal handoff document. 12871: `2eee236a8af` "add hypothetical-reproduction handoff doc" and `dee3ac18e38` "remove the hypothetical-reproduction handoff doc". 15615: `d3d62728819` "add hypothetical-reproduction handoff doc" and `51bbb48cbe8` "remove the handoff doc". The final trees have no such file (`git diff --stat` shows 3 files on each head), but the PR commit list shows these subjects. Replacement: none on the branch. Owner decides whether to rewrite the fork branches before any PR opens (Owner decision 6).

7. **NOTE.** Local branch names are behind the live tips. `solr-12871-submit` is at `2eee236a8af`, not `c79a493`. `solr-15615-submit` is at `d3d62728819`, not `be77267`. `wt-solr-17311-submit` is at `4a7dc40f09a`, not `9f7524582f9`. The `origin/solr-*-submit` refs match the claim. Use the origin refs for any proof. Replacement: none.

8. **NOTE.** 12871 against 17311 (direct check). `git merge-tree --write-tree origin/solr-17311-submit origin/solr-12871-submit` exits 0, and both files auto-merge. Merge base is `14c7aac0d15`. Hunks do not overlap. 12871 adds the guard at `ChildFieldValueSourceParser.java` lines 195-203, inside `parse`. 17311 changes `setTopValue` at lines 63-67. In `TestNestedDocsSort.java`, 12871 adds one `@Test` at lines 89-103. 17311 adds imports, a method at lines 107-145, and a helper after it. The merged class has 13 test methods (11 on base, plus one from each branch). Not built. No landing order is needed (Owner decision 8).

9. **NOTE.** 15615 against 12871 and 17311. Both `merge-tree` checks exit 0. 15615 touches only `CloudMLTQParser.java`, `CloudMLTQParserTest.java`, and its changelog, so there is no shared file.

10. **NOTE.** 17311 fail-before evidence is not at the live head. The only on-disk fail-before run is `research/test-queue/results/SOLR-17311.failbefore.json`. Its verdict is PASS, and its failure line is `testCursorMarkPagingOverMissingChildValue ... NullPointerException ... "text" is null`. That run was at head `bb964d8356`, which is not an ancestor of the live tip. Its test differs: ids `p1`/`c1` there, `1`/`101` at `9f7524582f9` (`git diff bb964d8 origin/solr-17311-submit -- .../TestNestedDocsSort.java`). `ChildFieldValueSourceParser.java` is identical in both. The receipt for the live head records only that the pre-fix check passed. Replacement: none on the branch. The draft's NPE sentence is safe once the owner confirms it (Owner decision 7).

11. **NOTE.** 17311 Lucene claim (code comment, lines 64-66, and the draft). Checked in the jar bytecode with `javap -c -p`. In lucene-core 10.4.0 and 9.12.3, `TermOrdValComparator`'s leaf comparator sets `topOrd = missingOrd` when `topValue` is null (the `ifnull` branch at offset 39, then the `missingOrd` store at offsets 91-96). In lucene-join 10.4.0, `ToParentBlockJoinSortField$1` extends `TermOrdValComparator`, so the delegate chain holds. The lucene-join 9.x jar is not in the local Gradle cache, so the join layer is checked on 10.4.0 only. No change needed. The draft limits its Lucene sentence to `TermOrdValComparator`, which both lines cover.

12. **NOTE.** 17311 `TestNestedDocsSort.java` line 107 has no `@Test`. The method still runs, because the randomized runner runs test-prefixed methods. The fail-before log shows it running, and `testEquality` and `testCacheHits` in the same file are unannotated too. Optional replacement: add `  @Test` on its own line above line 107.

13. **NOTE.** 17311 limits, both in the draft. The wrapper applies only to `Type.STRING` (`ChildFieldValueSourceParser.java` lines 132-134 at 9f75). `getSortField` builds the sort from the child field type alone (lines 124-127), so `sortMissingFirst` and `sortMissingLast` are not applied. The draft names both.

14. **NOTE.** 15615 fan-out, not measured. `CloudMLTQParser.java` lines 138-151 (at be77267) make one `getById` call per other collection, one at a time. A document that exists nowhere costs one lookup per other collection before the 400. The round-28 review's fan-out finding still stands. The draft's Choice section and Limits cover it.

15. **NOTE.** 15615 authorization of the fallback lookup, not traced. `HttpSolrCall.getAuthorizationCollectionsList` (upstream/main lines 225-236) uses the core's own collection when a local core serves the request. The fallback reads other collections through `coreContainer.getZkController().getSolrClient()` (`CloudMLTQParser.java` line 144). `PKIAuthenticationPlugin.java` lines 376-378 (upstream/main) forward the request's user principal on internode requests. I did not trace whether this `getById` call carries that principal. The draft says nothing about authorization. Owner decision 5.

16. **NOTE.** 15615 round-28 test-path finding is resolved at be77267. The test pins one replica of COLLECTION (lines 180-199), so the local lookup misses on every run. Commit `96710a4ed82` "make the alias regression test deterministic" made this change.

17. **NOTE.** 15615 single-collection requests are unchanged. `HttpSolrCall.addCollectionParamIfNeeded` (upstream/main, around lines 783-800) adds no `collection` parameter when the request names the core's own collection. The parser returns null when the parameter is absent (`CloudMLTQParser.java` line 134). So those requests make no extra lookups.

18. **NOTE.** Round-28 reviews are at older heads. 12871: findings 2 (assertion strength) and 3 (root `SOLR-12871-TESTING.md`) are resolved at c79a493. Finding 1 (scope) stands. 15615: finding 1 is resolved (item 16). Finding 2 (fan-out) stands (item 14). The owner and security questions stand (item 15). 17311: the "Close" verdict stands. The round-3 items are resolved: the test and changelog exist, the handoff file is gone, and the code does not claim `sortMissingLast` behavior.

19. **NOTE.** Receipts and disk. The gate logs `g12871r35-gate.log`, `g15615r35-gate-fix.log`, and `g17311-harden.log` are not on disk. There is no test-queue result for 12871 or 15615. `research/test-queue/results/SOLR-17311.json` is at superseded head `bb964d8` and lists `TestCloudNestedDocsSort` 1 of 1. That is not live-head evidence, and no draft cites it. The 15615 earlier GATE INVALID is superseded, as the receipt says. The drafts do not mention it, because it is internal.

## Task results

**SOLR-12871 (c79a49320cd): held for FIX 1.** The gate is green by receipt. The test is sound for what it asserts (Finding 3), and the guard fires before the Lucene sort is built. Lucene 10.4.0 bytecode confirms that the `ToParentBlockJoinSortField` constructor throws for REWRITEABLE (the switch maps REWRITEABLE to a throwing case). The 9.x check is not done. The changelog overstates the change, because the client already gets the same 400 on main (Finding 1). The draft is at `pr-drafts/query-parsing/SOLR-12871.md`. It is written to the corrected framing, with the rewrite question in Limits. Do not post until FIX 1 lands on the branch and the owner accepts the framing (Owner decision 1).

**SOLR-17311 (9f7524582f9): draftable.** The fix is one null check in the string comparator. The paging test covers the reported path. The changelog matches the scope. The Lucene claim is checked (Finding 11). The hunks do not clash with 12871. The draft is at `pr-drafts/query-parsing/SOLR-17311.md`. Before posting, confirm the live-head failure line (Finding 10, Owner decision 7).

**SOLR-15615 (be77267bf55): draftable after FIX 2.** The final gate is green by receipt, with 15 of 15 in `CloudMLTQParserTest` (14 annotated tests plus `testInvalidSourceDocument`). The round-28 test-path concern is resolved at this head. Open items are the fan-out cost and the fallback authorization (Findings 14 and 15). The draft is at `pr-drafts/query-parsing/SOLR-15615.md` and includes a Choice section (Owner decision 3).

## Owner decisions

1. 12871: ship the narrow change with the corrected wording (draft), or also extend `SortSpecParsing` so the real reason reaches clients. The second is broader and touches a shared path. Recommendation: narrow now, and a follow-up for the message.
2. 12871: the rewrite-support question sits in Limits, per the scope rule in `pr-formula.md`. Say if you want a Choice section instead.
3. 15615: keep the Choice section in the draft, or drop it.
4. 15615: accept the fan-out with the Limits line (as drafted), or measure it first.
5. 15615: confirm the fallback lookup carries the caller's identity before posting. Then add one Limits sentence.
6. 12871 and 15615: commit subjects name an internal handoff document. Rewrite the fork branches before any PR opens? This needs your go-ahead.
7. 17311: confirm the live-head NPE line before posting, or accept the draft's sentence.
8. Landing order: none required. Suggested: 17311 first, 12871 after FIX 1, and 15615 independent.

## Not checked

- No builds, tests, `gh` calls, or live Jira calls. The Jira text came from the on-disk packets, which may be stale.
- Gate logs and GitHub run IDs named in the receipts (`37666081934`, `37708319561`) are not on disk and were not checked.
- Live-head failure output for 12871 and 15615 is not on disk.
- Lucene 9.x for `ToParentBlockJoinSortField`: the lucene-join 9.x jar is not in the local cache. The 12871 draft therefore names the Lucene exception only through the ticket.
- Client responses were checked by reading `SortSpecParsing.java`, `ResponseUtils.java`, and `SolrException.java`, not by a live request.
- The fallback's authorization path (`CloudSolrClient.getById` to the receiving node) was not traced end to end.
- Tidy, Spotless, Error Prone, and the changelog YAML parse were not checked here. The receipts say they pass.
- No branch was changed, nothing was committed, and nothing was posted or pushed. No stash was touched.
