# SOLR-8628 hypothetical reproduction

Status: hypothetical, unrun. Nothing was compiled and Gradle was not used.

## Ticket
When Solr finds an existing empty index directory, it complains about a missing segments file
instead of creating a new index. The attached patch is for the old code path.

## Finding on current `upstream/main`
`CachingDirectoryFactory.exists` returns false for a directory with no entries, so the empty
directory case works: `SolrCore.initIndex` creates the index. The same failure survives for a
directory that is not empty but has no index. The one that Solr itself produces is a directory
holding only `write.lock`: `initIndex` creates the writer with `OpenMode.CREATE` and the first
`segments_N` file only exists after that writer commits on close. A crash (kill -9, OOM kill,
container eviction) between the two leaves `write.lock` alone. On restart `exists` is true, so
`initIndex` calls `solrCoreState.getIndexWriter(this, false)` and the core fails with
"no segments* file found" until someone deletes the directory by hand.

## Change
`SolrCore.initIndex` treats a directory whose only file is `IndexWriter.WRITE_LOCK_NAME` as "no index"
(new private `containsOnlyWriteLock`, opened with lock type `none` like the snapshot code nearby).
The new index is then created by the existing branch. A directory with any other file is untouched,
so a damaged index (segments missing but data files present) is never silently overwritten. If
another process holds the lock, `SolrIndexWriter.create` still fails fast with the lock error.

Test: `TestCoreContainer.testCreateCoreOverIndexDirWithOnlyWriteLock` creates
`<home>/core1/data/index/write.lock` and then `cc.create("core1", configSet=minimal)`; expects an
empty searchable index and a `segments_*` file.

## Expected
Before the change the create fails (`IndexNotFoundException` / "no segments* file found"). After the
change it succeeds.

## Risky guesses
- Default data dir layout `<instanceDir>/data/index` and `index.properties` absent for a fresh core.
- `get(indexDir, DEFAULT, "none")` on an existing directory does not touch the lock file.
- Other directory factories (`ByteBuffersDirectoryFactory`, `MMap`, `NRTCaching`) all answer
  `listAll`; the ephemeral factory's `exists` already returns false for an empty directory.

## Verify later
`:solr:core:test --tests org.apache.solr.core.TestCoreContainer`.
