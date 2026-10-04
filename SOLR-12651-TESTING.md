# SOLR-12651 - hypothetical-reproduction handoff

**Read this first: the regression test on this branch was written without being compiled or run.** The research/implement pipeline has
no Gradle access, so the test is a best-guess reproduction. Treat it as a hypothesis to confirm, not as proof.

- JIRA: https://issues.apache.org/jira/browse/SOLR-12651 - "Restore collection should clean up if the operation failed" (Varun Thacker, 2018)
- Branch: `solr-12651-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)
- Research note: `research/pipeline/research-notes/SOLR-12651.md` in the Solr-issues workspace

## The bug, as understood
`RestoreCmd.RestoreOnANewCollection.process` creates a core-less collection, then creates replicas, copies data (`requestShardsToRestore`, `INSTALLSHARDDATA`), adds the
remaining replicas and restores the alias. Main already cleans up when *initial replica creation* reports failures, but any failure (exception) in the later phases left the new,
half-restored collection in cluster state (shards in `construction` state, unreadable index) - exactly the reporter's "restored an 8.0 index on 7.x" case.
`requestShardsToRestore` calls `processResponses(..., abortOnError=true, ...)`, so a failed shard install surfaces as an exception, not as an entry in `results`.

## What the branch changes
- `RestoreCmd.java`: everything after `createCoreLessCollection`/`uploadCollectionProperties` is wrapped in `try { ... } catch (Exception e) { log; cleanupCollection(...); throw e; }`
  (the existing failure-result cleanup stays). Most of the diff is re-indentation; `git diff -w` shows the real change.
- `TestLocalFSCloudBackupRestore.errorRestore` (existing test using `PoisonedRepository`, whose `copyFileTo` throws): after the expected `SolrException`, asserts the restore target
  `<collection>boo` is not listed by `CollectionAdminRequest.listCollections`.

## What was guessed (verify these first)
1. **Where the poisoned repo fails.** If `PoisonedRepository` already throws *before* the collection is created (e.g. while reading `backup.properties` in `RestoreContext`), the new assertion
   passes without the fix and proves nothing. The test only discriminates if the failure happens during `INSTALLSHARDDATA` (`copyFileTo`). Check with fail-before.
2. `cleanupCollection` is called synchronously with a throw-away results list; if it itself throws it would mask the original exception (not guarded).
3. Behavior question raised in the ticket (Tomas Lobbe): some users may prefer a failed restore to be resumable instead of deleted. This branch implements the ticket title (always clean up); making it optional is a follow-up if maintainers want it.
4. Restore into an *existing* collection (`RestoreOnExistingCollection`) is unchanged on purpose.
5. The ticket's second point (INFO log spam "Copying file ... to restore directory") no longer exists in `RestoreCore` on main; nothing done.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.cloud.api.collections.TestLocalFSCloudBackupRestore"
```
Fail-before: revert only `RestoreCmd.java`; `test()` -> `errorRestore` should fail on the new assertion if guess 1 holds.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
