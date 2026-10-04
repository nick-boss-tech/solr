# SOLR-12085 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12085 - "IndexFetcher does not honor SolrDeletionPolicy" (Karishma Agrawal, 5.4.1/7.1/7.2). No comments; three fix options listed in the description.
- Branch: `solr-12085-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
In `IndexFetcher.fetchLatestIndex`, before an incremental copy it loops `while (hasUnusedFiles(indexDir, commit)) { deleteUnusedFiles(); sleep 1s }`, up to 30 times, then forces a full copy.
`hasUnusedFiles` considered a file "used" only if the *latest* commit refers to it. With `SolrDeletionPolicy` `maxCommitsToKeep > 1`, files of the retained older commits are legitimately kept by the
deletion policy, so the loop never finishes and every replication degrades to a 30s wait plus a full copy. Still true on main (`IndexFetcher.java` ~L639, L867).

## What the branch changes
- `IndexFetcher.hasUnusedFiles`: also treats the files of every commit returned by `DirectoryReader.listCommits(indexDir)` as in use (this is option 2/3's intent without a Lucene change). Made package-private `static` so it can be unit-tested.
- New `IndexFetcherUnusedFilesTest`: writes two commits with `NoDeletionPolicy` + `NoMergePolicy`, asserts no unused files, then adds `_orphan.cfs` and asserts it is reported.

## What was guessed (verify these first)
1. **Compile**: `hasUnusedFiles` uses only existing members (`log` is a static field); `DirectoryReader` import added in sorted position. `new IndexWriterConfig()` with the no-arg constructor and `newDirectory()` (MockDirectoryWrapper) are assumed fine in `SolrTestCaseJ4`.
2. **MockDirectoryWrapper**: may reject the extra `_orphan.cfs` file or flag it at close (it checks for leaked/unreferenced files in some modes). If so, create the orphan through `dir.createOutput` on a plain `ByteBuffersDirectory` instead.
3. **Semantics**: listing all commits means a stale commit the deletion policy *wants* to delete but cannot yet (e.g. open reader on Windows) no longer blocks the wait loop. That is a narrower guarantee than before; confirm it is acceptable (alternative: only count commits the `SolrDeletionPolicy` still reports).
4. No end-to-end replication test was written (needs `maxCommitsToKeep>1` replication harness, e.g. `TestReplicationHandler`).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.IndexFetcherUnusedFilesTest"
```
Fail-before: revert only `IndexFetcher.java` (the test then also needs the method to be non-private; expect `assertFalse` to fail).

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
