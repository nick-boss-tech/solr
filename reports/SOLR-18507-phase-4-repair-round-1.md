# SOLR-18507 Phase 4 repair, round 1

Status: implementation pushed; Linux gate proof pending.

## Heads and records

- Starting Phase 4 head: `807f9d42db448ee229a00d25eda9c9b63e3ed2f9` on `solr-18507-phase-4-submit`.
- Phase 3 green head: `ad706640ffd62907961749dc9319ca8bff35bbb0`. It is an ancestor of the Phase 4 branch; the merge-base check returned this exact head.
- Repair commit and pushed Phase 4 head: `81a7e5d78fa2be005a81e29d3dfc47a742272928`.
- New gate request: `gates/SOLR-18507-phase-4-r4.md`, gate ID `SOLR-18507-phase-4-r4`, at head `81a7e5d78fa2be005a81e29d3dfc47a742272928`. Status is OPEN.
- The Phase 3 benchmark follow-up remains OPEN and unclaimed. This implementation claim did not take the `sweep` assignment.

## Diagnosis

Attempt 3 at `807f9d42db` reported five assertion failures:

- `testMalformedPersistedGrossFingerprintFallsBackToLiveScan`: expected 0 gross-cache entries, found 1.
- `testIncrementalFingerprintFallsBackWhenDeletedSnapshotExceedsLimit`: the class-level incremental-update count was 1 before the check and 3 after it.
- `testStalePersistedGrossFingerprintIsRejectedAfterDocValuesUpdate`: the class-level incremental-update count was 8, not the expected 6.
- `testGrossFingerprintCacheHitAfterDelete`: the class-level incremental-update count was 15, not the expected 12.
- `testPersistedGrossFingerprintSurvivesCoreReloadAndDeletes`: expected one leaf, found seven.

`SolrCoreFingerprintCacheTest` initialized its core once with `@BeforeClass`, then each test method added and flushed its own segments without clearing the prior methods' data. Its helpers fingerprint every leaf, while the cache size and update count are core-wide. Earlier fixtures therefore affected later count assertions and the single-leaf assumption. In the four failures that reached the cached-versus-uncached comparison before the reported assertion, the fingerprints matched. The reload test failed on its leaf-count assertion before reaching that comparison. The stale-DocValues test's later post-update comparison was not reached in the failed attempt.

## Change

The repair adds a `@Before` setup that calls `IndexWriter.deleteAll()`, commits with a new searcher, and reloads the core before each method. This gives each method a fresh index and fresh per-core counters and caches. The existing whole-core cached-versus-uncached comparison remains in place. No production code or expected values changed.

Only `SolrCoreFingerprintCacheTest.java` changed in the repair commit. `git diff --check` passed. No Gradle task or test ran on Windows. Formatting was reviewed manually; the Linux gate owns tidy and the focused run.

## Focused result and next step

The focused test result is pending. Gate `SOLR-18507-phase-4-r4` runs `SolrCoreFingerprintCacheTest` with seed `18507A004`, plus the standard changelog, tidy, Error Prone, and core module checks on Linux. Treat Phase 4 as proof-pending until that gate reports against the exact pushed head.
