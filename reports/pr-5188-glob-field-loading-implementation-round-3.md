# PR 5188 current API and missed regressions, implementation round 3

Status: Implemented, Proof Pending at f00caeb5e83ac375672ae27fb43ee1546795e98b.
Host: windows (Codex desktop).
Authority: Nick explicitly authorized the naming changes and review of missed implementation regression coverage.
Claim: claims/pr-5188-api-and-regressions-windows-round-3.md.
Intake snapshot: d31ce26715d398be153dca339a84c0ce4b43c61c; publication recheck: 903d8365a887e0a2f83181176c013789e843f68c. Prior Linux gate claims have terminal result records; no live gate head was moved.

## Pushed history

- Previous verified follow-up: 409183aa9be8c8500942eadf22d036abaf1ead5c. Its round-2 proof remains valid for that exact head.
- Current Apache PR 5188 source: db867d80365510790bb6a04ce33105cd196cf34c (OPEN when read).
- Merge/API adaptation: 2994813ae51847fdc17a1480086c2a9f8cfb059c, with parents 409183a and db867d8.
- Regression additions/final head: f00caeb5e83ac375672ae27fb43ee1546795e98b.
- Fork: nick-boss-tech/solr:pr-5188-glob-field-loading-followup, normal push from 409183a; final full SHA read back through the fork Git Ref API.
- Both new commits: Nick Shanin <nick.boss.us@gmail.com>, no attribution trailers. No history rewrite.
- Incremental compare against the current PR: https://github.com/nick-boss-tech/solr/compare/db867d80365510790bb6a04ce33105cd196cf34c...f00caeb5e83ac375672ae27fb43ee1546795e98b

The comparison contains only the original three production paths plus the two test suites (5 files, 386 insertions, 43 deletions). The merge imports the PR author's own rename commit; its 20-file change is not an additional outbound change against db867d8.

## Naming change

The follow-up's added field-selection/conversion calls now use loadsField, loadsFieldExplicitly and returnsAllFields. The upstream rename commit was merged normally to preserve both the proved history and the current PR ancestry. The only conflict was the inner stored-field selector that this follow-up had already moved to the reusable outer getStoredFieldsToLoad method. Resolution keeps that single shared selector and applies the renamed predicates there. No duplicate inner selector remains. The RTG conversion guard also uses loadsField. Search of core Java sources finds no remaining wantsLuceneField or isExplicitLuceneField calls or conflict markers.

There is no new field-loading policy change in this phase. RTG internal reconstruction, existing DV decoration, /export and term-vector behavior are preserved.

## Coverage audit and additions

1. **Unmatched glob and cache completeness.** Existing coverage checked matching globs and pseudo-only fields but cleared the cache before the all-fields control. It did not prove that a zero-match glob keeps unrelated values lazy or that the same cached document remains complete for later requests.
   New BasicFunctionalityTest.testGlobRTGEmptyAndAliasFieldLoading starts with fl=missing_* and requires an empty response with title/source_s/unrelated_s still lazy. It then requests tit*,unrelated_s:source_s, checks the alias output colliding with a real stored field, and asserts the original unrelated_s is still unmaterialized. A final fl=* on the SAME cached Document must return the original stored source_s and unrelated_s values. Cache object identity is checked across requests; no cache clear masks the transition. Cleanup is in finally.
2. **Prefetch guard boundaries.** Existing coverage exercised one non-shard highlighted result but did not check the <=50 result guard, shard requests, or the no-highlighting prefetch path.
   New BasicFunctionalityTest.testGlobPrefetchBoundaries indexes 51 marked documents. It compares explicit and glob selections across rows=50/51 and isShard=false/true, with hl=false and no response writer. Eligible non-shard 50-result requests must eagerly cache source_s while unrelated_s remains lazy; shard requests and 51-result requests must leave result documents absent from the cache. All 8 combinations are checked. The fixture is deleted by its unique marker in finally.
3. **Already covered, retained.** Existing tests keep explicit/glob and cold/warm RTG, transformer inputs outside the glob, synthetic output collisions, pseudo-only/fl=* controls, uncommitted tlog and forced realtime-searcher visibility, and 24 positive snippet rows across original/unified, implicit/explicit highlight query and both weightMatches settings. TestRetrieveFieldsOptimizer and ReturnFieldsTest cover the reused selector's DV and field-name semantics.
4. **Neighbor coverage requested.** Add existing LargeFieldTest to the fresh Linux suites to exercise the fetcher's special large/lazy-field handling alongside the existing five suites. No new production hook, timing test or duplicate low-level predicate test was added.

These additions are unrun candidate regressions on Windows. Source review found coverage gaps; it did not establish a new product defect beyond the previously proved loading gaps. The new no-match/alias method is predicted to fail before the fix at title should remain lazy. The boundary method is predicted to fail before the fix at Missing prefetched document for fl=source_* rows=50 isShard=false, after finite controls pass. Linux must verify those predictions.

## Validation and next step

Static diff checks pass and the worktree is clean. Current PR db867d8 and old proved head 409183a are both ancestors of the new head (checked by the pinned publisher). No Gradle, compilation, tests, formatting tool or profiling ran on Windows. New exact-head job: gates/pr-5188-glob-field-loading-round-3.md. It uses current PR production db867d8 plus the final two test files for negative/compatibility proof, then tests/checks the exact final branch head. The old receipt does not cover these new commits.

No public comment, PR body edit, new PR or JIRA write was performed. The public PR author's branch remains untouched.

## Final SHA-256 (LF bytes)

- solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java: 42ccfc40043ba7c97d08fe0fcf02c3a98f14d39cccb2aeb2439930f584fcdde6
- solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java: c2953c621dc33a83b9f2e454553f1cb6f65f51ff72d6440a55b200ea176a959e
- solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java: 637a280ec48e774494c165fcda4c3b5af6d671f44f7a9a93c202760ef9f58156
- solr/core/src/test/org/apache/solr/BasicFunctionalityTest.java: e2cdbfd40d5cc03bedbd3658bdbc050113dd35c82f86dffd9ba2f6582fc86a2f
- solr/core/src/test/org/apache/solr/search/TestPseudoReturnFields.java: 577a9b132f5b44820a9c3c8d8f8c1d4a176cd678bc8be8ff5d1bc2ea84b4d393
