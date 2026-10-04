# SOLR-17287 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-17287 - "RESTORECORE should reset/clear the UpdateLog" (David Smiley, 2024). Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-17287-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
After a core restore replaces the index, the UpdateLog still holds the pre-restore updates. Realtime-get can serve a doc added after the backup, and a restart after a crash would replay old tlog entries on top of the restored index. SOLR-16924 added `applyBufferedUpdates()` to the SolrCloud restore API, but that is a no-op unless the log is BUFFERING (always in SolrCloud, never standalone).

## What the branch changes
- `UpdateLog.clearAndActivate()` (new): under `blockUpdates`, drops buffer/current/previous/old tlogs (deleting their files), clears the lookup maps, oldDeletes and DBQs, sets state ACTIVE, then opens a new realtime searcher.
- `org.apache.solr.handler.RestoreCore.doRestore()` calls it after a successful index switch, **only when the core container is not ZooKeeper-aware**. In SolrCloud the log is BUFFERING during restore and must keep the updates that arrive meanwhile; `handler/admin/api/RestoreCore` still calls `applyBufferedUpdates()` for that.
- Test: `TestRestoreCore.testRestoreClearsUpdateLog` with a new config `solrconfig-leader-ulog.xml` (copy of `solrconfig-leader.xml` plus `<updateLog/>`) and the default `schema.xml` (needs `_version_`). Adds a doc after the backup without committing, restores, then RTG must return `null` for it.

## Guesses to verify first
1. Whether the standalone-only condition is right. The ticket's text says core-level "not SolrCloud" API; if cloud should also clear when not BUFFERING, extend the condition.
2. `discardLog` (deleteOnClose=true, decref, forceClose) semantics for a log still referenced by RecentUpdates/peer sync; `logs` entries own one ref each per `addOldLog`.
3. After deleting tlog files, `id` continues to increase, so `ensureLog` creates a fresh file; check there is no assumption of at least one old log elsewhere (e.g. `getLastLogId`).
4. The test's `getById` through SolrJ hits `/get` (implicit RTG handler) and `before-backup` is returned from the restored index; `ReplicationHandler` restore requires the data dir allowed (default location is fine).
5. `schema.xml` in test-files has no required fields beyond `id`.
6. Crash-restart replay (the ticket's strongest argument) is not tested.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.TestRestoreCore"
```
Fail-before: revert the `RestoreCore.java` hook; `testRestoreClearsUpdateLog` should fail on the `assertNull`.

## Not done
No JIRA comment, no PR.
