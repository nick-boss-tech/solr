# PR 5188 glob field-loading implementation, round 1

Status: Implemented, Proof Pending
Host: windows (Codex desktop)
Date: 2026-10-26
Authority: Nick's direct implementation request and research/glob-field-loading-followup-research-note.md.
Claim: claims/pr-5188-glob-field-loading-windows-round-1.md, posted at 9ca6f8f033ee5267ba20217cd3b8145f29629051.
Board intake snapshot: 1053370a993cea8c6779f0e4bbf79276453daecd; publication ownership recheck: 9ca6f8f033ee5267ba20217cd3b8145f29629051. Only this claim exists for this follow-up.

## Pushed revisions

- Original PR production: e07455a0f953e702da27f677c51ea894b7bafc3a.
- Tests-only pre-fix revision: 4ccd146b904b4574dbd412163f5c88b5f2e0de6b.
- Candidate post-fix revision and verified fork head: b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2.
- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup.
- Both new commits are authored and committed by Nick Shanin <nick.boss.us@gmail.com>, without attribution trailers.
- Incremental comparison: https://github.com/nick-boss-tech/solr/compare/e07455a0f953e702da27f677c51ea894b7bafc3a...b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2
- Production-only comparison: https://github.com/nick-boss-tech/solr/compare/4ccd146b904b4574dbd412163f5c88b5f2e0de6b...b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2

No commit was pushed to apache/solr or the original PR author's branch. PR 5188 is David Smiley's contribution. This branch is a proposed incremental follow-up for review after proof, not an update of that public PR. No public comment, PR body edit, JIRA write or PR creation was performed.

## Source findings and implementation

Rung 1: source and history establish the two integration gaps; runtime discrimination is pending rung 2 on Linux. PR head e07455a split the non-stored docValues accessors but left the stored RTG call and finite-name prefetch guard in place. No old receipt applies to this new candidate.

1. At original RealTimeGetComponent.java:350-352, a partial glob supplies null to docFetcher.doc. SolrDocumentFetcher.doc/document visitor interprets null as all stored fields (and when caching is enabled without lazy loading, intentionally fetches all fields for the shared cache even for finite selections). The normal query optimizer already resolves globs against actual stored field names.
2. At original RealTimeGetComponent.java:908-934, conversion materializes every cached field. Merely narrowing the initial fetch would still force unrelated lazy fields through conversion. The response-path call now passes ReturnFields and skips unneeded index fields before materialization. Existing internal reconstruction/conversion callers keep the unfiltered two-argument method and copy-target handling.
3. The existing stored-field selector has been moved unchanged from RetrieveFieldsOptimizer to public SolrDocumentFetcher.getStoredFieldsToLoad, at candidate line 776. Query optimization, RTG at line 351, and SolrPluginUtils at line 236 use that selector. It resolves actual indexed dynamic field names, preserves transformer inputs, and returns null for all-fields requests. Its returned finite set is mutable; prefetch adds highlight fields and the unique key directly.
4. The finite-only prefetch guard skipped globs even though QueryComponent calls the helper for non-shard requests of at most 50 results. The candidate retains that caller guard and the lazy-loading prerequisite. It also preserves the old no-op for pseudo-only and all-fields requests.

DefaultSolrHighlighter.java:475-495 fetches highlight fields plus the unique key through solrDoc. With skipped prefetch that can populate the document cache before the response fields are fetched, leaving response fields lazy. UnifiedSolrHighlighter.java:223-234 separately retrieves keys and its Lucene highlighter reads text independently. The proposed assertions check eager cached response/highlight/key state and unchanged output. They are not a measurement of disk I/O or a claim that Unified's independent text reads disappear.

## Exact outbound files and tests

Only five files differ from the original PR: three production files above and two existing test suites. No changelog or reference-guide change was added for this exploratory follow-up. Existing PR changelog/docValues changes remain intact; /export and intentional partial-glob term-vector behavior are out of scope.

- BasicFunctionalityTest.java:851, testGlobRTGStoredFieldLoading: glob and explicit fl with a transformer input outside the glob and a synthetic output name colliding with a real stored field. Checks exact XML output, cold and warm cache lazy-field state, fl=* and pseudo-only controls. Unrelated fields must not be materialized by RTG. The warm case observes LazyField.hasBeenLoaded, not physical reads inside Lucene's lazy document.
- BasicFunctionalityTest.java:918, testGlobHighlightPrefetch: finite and equivalent glob requests with one result, highlighting title outside the glob, original and unified highlighters. Checks eager cached response fields/title/key before response writing, unrelated field remains lazy, exact snippet and exact response field count.
- TestPseudoReturnFields.java:508, testGlobRTGUncommittedFields: uses the existing always-enabled transaction-log fixture; compares finite/glob lists with transformer input and synthetic collision both without filters (transaction log) and with fq (forces realtime searcher). /select must see zero documents before and after the RTG matrix. Fresh document is deleted in finally.

The unchanged docValues decoration in RTG retains the parent PR's narrowing to non-stored DVs used as stored. This work does not claim a new explicit-DV RTG fix or data-leak correction. No new endpoint, permission, persistence or trust boundary is introduced; the intended behavior is narrower loading with the same response filtering and transformer inputs.

## Validation and next step

Static diff and identity review completed. git diff --check passes for the full incremental comparison. The worktree is clean after both commits and the fork ref was read back at b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2.

No Gradle, compilation, test execution, tidy, benchmark or profiling ran on Windows. The tests are proposed discriminators until Linux executes them. Gate gates/pr-5188-glob-field-loading-round-1.md names both pinned revisions, the same seed, mandatory assertion failures on pre-fix production, full post-fix suites and compile/format/check requirements. Test/compiler/harness failures require diagnosis and a narrow repair; they are not proof of either hypothesis. Gate receipt and report must distinguish observed failures from this source-based explanation.

## Candidate file SHA-256 (LF source bytes)

- solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java: df63267f1f7a07e22fc25fa9847ce6faf81d276948f4d5fce39fabbad1010363
- solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java: a7d6dab21c280c8a690fcaa9170f4590209f4855d0b96e19cb1222a253839dd0
- solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java: 637a280ec48e774494c165fcda4c3b5af6d671f44f7a9a93c202760ef9f58156
- solr/core/src/test/org/apache/solr/BasicFunctionalityTest.java: 905dfc366c588fdebbc0dd6301d55d8b5518515c1a7dbd891591809761ef4316
- solr/core/src/test/org/apache/solr/search/TestPseudoReturnFields.java: c34728780f9c2f922f63059dc18101a6df6937db6d7771492e9d35510eea9da1
