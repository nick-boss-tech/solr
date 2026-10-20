# Surface dossier: the PeerSync fingerprint cache

Pinned revision: `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main). Callers found by `git grep` at that revision in this pass. Written by vm2, 2026-10-20T10:56:36Z (UTC). Confirmation review by a second host is pending.

## Naming note

The assignment's seed reads "PeerSync fingerprint cache". At the pinned revision the cache is not in PeerSync. It is the per-segment cache `perSegmentFingerprintCache` on `SolrCore` (`solr/core/src/java/org/apache/solr/core/SolrCore.java`, field at line 235), with the computation in `SolrCore.getIndexFingerprint` (line 2141). PeerSync is a consumer of it. The surface is covered as both.

## 1. What the surface is and what depends on it

`IndexFingerprint` (`solr/core/src/java/org/apache/solr/update/IndexFingerprint.java`, class at line 55) is a summary of a core's documents up to a max version: count, max version, and a hash over the versions. Peer sync and recovery compare a leader's and a replica's fingerprints to decide whether a replica can catch up from the update log or must do a full sync. `SolrCore.perSegmentFingerprintCache` (line 235, a `Cache<IndexReader.CacheKey, IndexFingerprint>`) keeps one fingerprint per segment, so that the whole-core fingerprint can be assembled from cached segments.

## 2. Callers and consumers (grep at 498a09aea97aac86c2cc7ac114113b946d249942)

- `solr/core/src/java/org/apache/solr/core/SolrCore.java`: owns the cache (line 235) and `getIndexFingerprint` (line 2141). It caches only segments whose versions are all included in the fingerprint (comment at lines 2130-2132 and the check around 2174-2175). Cache hits and misses are logged (lines 2167, 2188).
- `solr/core/src/java/org/apache/solr/update/PeerSync.java`: calls `IndexFingerprint.getFingerprint(core, Long.MAX_VALUE)` (line 307) for its own fingerprint and compares it with the other side's (`IndexFingerprint.fromObject` at line 328, `compare` at line 329). Its request object carries the fingerprint (line 479).
- `solr/core/src/java/org/apache/solr/update/PeerSyncWithLeader.java`: the leader-side peer sync; listed by grep (not reread here).
- `solr/core/src/java/org/apache/solr/update/UpdateLog.java`: documents that buffered updates come from PeerSync and fingerprint (comment at line 939).
- `solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java`: builds a fingerprint for the `fingerprint` request (line 1213), with test injections at lines 1204 and 1210.
- `solr/core/src/java/org/apache/solr/core/CoreContainer.java`: owns the `IndexFingerprintPool` executor (lines 188 and 454) used for parallel fingerprint work.
- `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java`: listed by grep (the searcher-level fingerprint entry point; lines not reread here).
- `solr/core/src/java/org/apache/solr/update/IndexFingerprint.java` itself, and `RecoveryStrategy`, `SyncStrategy`, `ShardLeaderElectionContext` (these call the peer-sync path rather than the fingerprint directly; listed by grep in the PeerSync search).
- Tests: `TestInjection` names fingerprint failure hooks (`injectFailIndexFingerprintRequests`, `injectWrongIndexFingerprint`). `solr/core/src/test/org/apache/solr/update/SolrIndexFingerprintTest.java` exists at this revision. The SOLR-18507 test classes `SolrCoreFingerprintCacheTest` and `SolrCoreFingerprintMergeTest` are not found at this revision by `git grep`; they are named in the phase-3 gate job.

## 3. Invariants and contracts

- Only complete segments are cached. A segment's fingerprint is cached only when every version in the segment is at or below the requested max version (`SolrCore.getIndexFingerprint`, around lines 2156-2180). Otherwise the segment is recomputed.
- Max version. `PeerSync` asks for `Long.MAX_VALUE` (line 307), so the whole core is covered; a smaller max version forces the uncached path.
- Equality is the contract. `PeerSync` treats equal fingerprints as "in sync" (line 329). Any change to what a fingerprint covers changes that decision.
- Parallel computation. Segments can be computed in parallel on the `IndexFingerprintPool` (CoreContainer lines 188 and 454; commit `2041d1d0e6`, SOLR-17756).

## 4. Live work

Phase branches of SOLR-18507 change this surface and are the live work: `solr-18507-phase-3-submit` (head `35f6e044dd`, gate-green at `ad706640ff`, `gates/SOLR-18507-phase-3.md`), `solr-18507-phase-4-submit` (head moved to `d797b9b310`, not gated; see `gates/SOLR-18507-phase-4.md`). Phase 3 adds persisted gross summaries in SegmentInfo; Phase 4 adds a per-segment live delta cache. The benchmark report `reports/SOLR-18507-fingerprint-benchmark-round-1.md` measures Phase 2 (`137e77a4c8`) against Phase 3 (`ad706640ff`) with partial results.

## 5. Recent history (path-scoped, at 498a09aea97aac86c2cc7ac114113b946d249942)

- `55e3b8838b` (2026-08-28) SOLR-18373: removed `NamedList.asShallowMap` and related helpers that fingerprint code used.
- `22c37b3c7f` (2026-06-11) SOLR-17600: migrated core config classes to MapSerializable.
- `2041d1d0e6` (2025-05-21) SOLR-17756: parallel index fingerprint across segments.
- `c99af207c7` (2022-03-04) formatting only (SOLR-14920).
- `201d56b350` (2021-07-07) RawTypes cleanup.

Review requests for these changes were not located in this pass.

## 6. Danger spots

- Caching an incomplete segment. A segment cached while some of its versions are above the max version gives a wrong fingerprint for later requests. The guard at `SolrCore.getIndexFingerprint` is the only protection; a refactor that moves the cache write must keep it.
- Changing what a fingerprint covers. Because `PeerSync` compares equality (line 329), a change to the hash inputs makes old and new nodes disagree and triggers full syncs during a rolling upgrade.
- Parallel segment work. The `IndexFingerprintPool` (CoreContainer) is shared; a task that blocks on another fingerprint task in the same pool can deadlock. Not tested in this pass.
- Test injection hooks in `RealTimeGetComponent` (lines 1204, 1210) change responses on purpose. Production code that stops calling them removes the failure tests' coverage.

## Limits

- Callers are grep-verified at the pinned revision. Phase-branch behavior is not verified here; it is described from the gate records.
- The cache-key and hit-rate behavior is not measured here; the benchmark report is the source for measured numbers.
