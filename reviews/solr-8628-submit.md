# solr-8628-submit

- Branch: origin/solr-8628-submit
- Head: ce8211e05e06 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: 3 commits. `solr/core/src/java/org/apache/solr/core/SolrCore.java` (+17/-1: new `containsOnlyWriteLock`, used in `initIndex`), `solr/core/src/test/org/apache/solr/core/TestCoreContainer.java` (+29: `testCreateCoreOverIndexDirWithOnlyWriteLock`), the changelog fragment, and `SOLR-8628-TESTING.md` (kept in place).
- Verdict: Nearly
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08
- Process note: the claim was pushed at 713e201c4ed, after I had read the diff. The claim file and the diff read were in the same step, and the first two claim pushes were rejected. The channel showed no competing claim for this branch at each check, so the review stands, but the protocol order was not followed.

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code, except where marked as hypothesis. Patches: none.

## Premise check (hypothetical-reproduction handoff)

- VERIFIED: `CachingDirectoryFactory.exists` returns true when a directory has any entry (`CachingDirectoryFactory.java:368-377`). A directory holding only `write.lock` therefore reads as an existing index, and `initIndex` takes the open-writer branch (`SolrCore.java:878-881`). That is where the failure would come from.
- VERIFIED: an empty directory already works, because `exists` is false for it.
- VERIFIED: the fix only fires when the directory's sole entry is `IndexWriter.WRITE_LOCK_NAME` (`SolrCore.java:860-868`). A directory with any other file is still treated as an index, so a damaged index is not overwritten.
- VERIFIED: the default lock type is `native` (`SolrIndexConfig.java:107`), an OS-held file lock. A leftover `write.lock` does not block a native lock, so the test fixture reaches `SolrIndexWriter.create` on an unlocked file.
- HYPOTHESIS: a crash leaves only `write.lock` behind. This matches how Lucene's IndexWriter takes the write lock at construction and writes `segments_N` on the first commit. Lucene's source is not in the workspace, so this was not traced.

## Findings (ranked)

LOW (verified): Stale lock files are not covered for the `simple` lock type. The fix helps only when the lock factory is OS-held (`native`, or `single`, which is in-process). Under `simple`, which is existence-based, a leftover `write.lock` still blocks `SolrIndexWriter.create` with a lock error, the same failure as before. The TESTING doc does not mention this. It is a limit of the fix, not a defect in the diff. Owner call if they want `simple` covered.

## Verified correct (by reading; not run)

- Compiles by reading. `IOException`, `Directory`, `IndexWriter`, and `DirContext` are imported in `SolrCore.java` (`:30`, `:80`, `:78`, `:106`). `getDirectoryFactory().get(...)` and `release(...)` are the same calls the snapshot code uses (`SolrCore.java:650`, `:692`).
- The helper releases the directory in a `finally` (`SolrCore.java:865-867`), so the refcount stays balanced.
- `containsOnlyWriteLock` is only evaluated when `exists()` is true (`SolrCore.java:873-874`, short-circuit `&&`), so an empty or missing directory costs nothing extra.
- The fix deletes nothing. With `OpenMode.CREATE`, the new writer adds a fresh index and leaves the lock file alone, so the case the fix covers has no data to lose.
- The test. `init(Path, String)` exists (`TestCoreContainer.java:70`), and `createTempDir()`, `Files`, `Path`, and `Map` are available. `cc.create(String, Map)` is used the same way elsewhere in the file. The branch adds the `SolrIndexSearcher` and `RefCounted` imports it needs. The test writes `write.lock`, creates the core, expects zero docs, and expects a `segments_` file.
- The changelog fragment matches the upstream format (`type: fixed`, ICLA author, JIRA link).
- All commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions

None blocking. Optional: whether the `simple` lock type should be covered (see the LOW finding).

## Not checked

- Nothing was compiled or run. The Linux gate runs the test.
- Lucene's IndexWriter behavior (lock timing and the first-commit write) was not traced. See the hypothesis above.
- The test's data-directory assumption (`<instanceDir>/data/index`, no `index.properties`) was not confirmed by running it.
- Directory factories other than the default `CachingDirectoryFactory` path (HDFS, MMap, NRT) were not checked.
- Upstream conflicts were not checked. The branch is 16 commits behind `upstream/main`.
