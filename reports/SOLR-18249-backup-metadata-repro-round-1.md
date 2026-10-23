# SOLR-18249: backup metadata and async-status findings

## Result

**Mechanism confirmed but production match unknown. The production incident remains unresolved; SOLR-18249 has not been reproduced.** Six focused checks passed, including a temporary fake-clock test showing that a completed, unpolled child request can disappear while an earlier child is still running and being polled. Source inspection separately confirms that the legacy full-snapshot collection-backup path waits for those child statuses before writing collection metadata. A status exception can therefore leave successful shard snapshot data without the later collection metadata.

The missing filename and deployed Solr version are still unknown. In particular, the supplied `incremental=false` parameter points toward the legacy full-snapshot path, which does **not** call `ShardBackupMetadata.store()`. A generic status-expiration result is not proof of that method's non-atomic-write defect.

## Revisions and compatible paths

The executed tests used unchanged Solr main commit `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Source review also covered `f2001e640a464e8e861ee3c202821b6dcc4bd497` and historical commit `a0714c8d01d070d1bf8fea2efb1d3bdece82c280`. The historical revision is the parent of [SOLR-18358 removal commit `da80d79ae253293f7d6b96df1c319fe43404b94e`](https://github.com/apache/solr/commit/da80d79ae253293f7d6b96df1c319fe43404b94e), which removed the non-incremental collection-backup path on September 9, 2026.

At the historical revision, `BackupCmd` reads `incremental`, selects the traditional backup manager for false, and calls `copyIndexFiles` rather than `incrementalCopyIndexFiles`: [BackupCmd lines 86-149](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/cloud/api/collections/BackupCmd.java#L86-L149). Its shard requests explicitly set `BACKUP_INCREMENTAL=false`: [lines 526-546](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/cloud/api/collections/BackupCmd.java#L526-L546).

The current inspected main code always uses the incremental collection manager and sets incremental shard backup requests to true. Merely seeing `incremental=false` in a request does not establish how a newer server interpreted it. The response fields `fileCount` and `snapshotCompletedAt` also do not uniquely identify a release: current `SnapShooter` retains them as deprecated compatibility fields. See [SnapShooter lines 317-359](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/handler/SnapShooter.java#L317-L359).

No exact released Solr version can be inferred from the supplied trace. The local source history establishes compatible implementations, not a production build identification. Older implementations predating the time-expiring tracker may have the same full-backup shape without this timeout mechanism.

## Which metadata artifact should exist?

All locations below are symbolic backup-layout descriptions, not production paths.

| Path | Data and metadata artifacts | Write point |
| --- | --- | --- |
| Legacy full-snapshot collection backup | Root is location / backup name. Each shard copies Lucene files into `snapshot.<shard>`. Collection metadata includes top-level `backup.properties`, `zk_backup/collection_state.json`, optional collection properties, and optional config data under `zk_backup/configs/`. | Shard snapshot files are copied first. Collection ZK/config/state metadata and finally `backup.properties` are written only after `copyIndexFiles` and its child-status wait return. |
| Incremental collection backup | Root additionally contains the collection directory. Shared files live under `index/`; each shard manifest is `shard_backup_metadata/md_<shard>_<backupId>.json`. Collection metadata is `backup_<id>.properties` and `zk_backup_<id>/`. | `IncrementalShardBackup` stores the shard JSON after copying that shard's files. Collection properties are finalized later, after parent response/status processing and state/config backup. |
| Core full snapshot | `snapshot.<snapshotName>` contains the copied committed Lucene index, including its index metadata. | `SnapShooter.createSnapshot` reports success after copying the snapshot. Its response is not a collection-level finalization acknowledgement. |

For traditional names, see historical [BackupManager constants and properties writer](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/core/backup/BackupManager.java#L202-L247), [BackupFilePaths lines 99-117](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/core/backup/BackupFilePaths.java#L99-L117), and [BackupCmd finalization lines 162-184](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/cloud/api/collections/BackupCmd.java#L162-L184). `BackupId.TRADITIONAL_BACKUP` is -1, selecting `backup.properties` and `zk_backup` rather than ID-suffixed names.

For incremental names and write behavior, see [BackupFilePaths lines 63-117](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/core/backup/BackupFilePaths.java#L63-L117), [ShardBackupId lines 25-56](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/core/backup/ShardBackupId.java#L25-L56), and [ShardBackupMetadata.store lines 106-139](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/core/backup/ShardBackupMetadata.java#L106-L139). The latter deletes an existing manifest and creates/writes its replacement directly; that separate non-atomic-write exposure was not exercised by this investigation.

**Best-supported candidate, not an identification:** if the incident used the legacy collection full-backup route, absent `backup.properties` or another later collection metadata artifact is consistent with the observed failure. Missing incremental shard JSON would require a different path or additional evidence. Full-snapshot success alone cannot distinguish these possibilities.

## Why a later child status can disappear

The relevant code coexists at historical revision `a0714c8d01d070d1bf8fea2efb1d3bdece82c280`, not just current main. The time-expiring tracker was introduced by ancestor commit `d3b4c2e1ae39b8ecc5428798531f8b7cf723d787` (SOLR-17160).

`sendShardRequest` assigns and records child async IDs as it submits the requests. `processResponses` drains initial submission responses, then `waitForAsyncCallsToComplete` iterates the recorded IDs, fully awaiting each before moving to the next. This is fanout followed by sequential status polling, not parallel polling: [CollectionHandlingUtils lines 722-820](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java#L722-L820).

The node-local tracker defaults are:

- `solr.admin.async.timeout.minutes`: 60 minutes for running or not-yet-polled terminal entries.
- `solr.admin.async.timeout.completed.minutes`: 5 minutes after completed/failed status is first retrieved.
- `solr.admin.async.max`: 10,000 tracked entries, with capacity eviction another possible cause of absence.

See [CoreAdminHandler lines 364-432](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java#L364-L432) and [expiry policy lines 543-573](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/handler/admin/CoreAdminHandler.java#L543-L573).

These are access-sensitive TTLs, not an absolute limit on operation duration. Polling a running ID refreshes its longer TTL. Task completion merely changes a field; it does not itself refresh the cache entry or apply the short TTL. A later child can therefore finish successfully, remain unpolled for over the longer TTL while the parent waits on an earlier child, and then return `notfound`. Regularly polling the earlier child can keep that earlier entry alive throughout.

The waiter handles `notfound` with `counter++ < 5`: initial request plus five retries, six responses total, with five one-second sleeps before throwing BAD_REQUEST. The message reports `Retried 6 times` because the failing comparison also increments the counter: [CollectionHandlingUtils lines 525-624](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java#L525-L624). That matches the reported retry shape, but does not identify why the ID was absent.

A node or handler restart clears the in-memory tracker independently of TTL. Different per-node timeout/capacity configuration, capacity pressure, wrong or missing submission, and version-specific behavior must also be considered. Child statuses here are node CoreAdmin statuses, not the collection API's separate top-level async tracking. No production configuration, restart history, or submission records were accessed.

## Focused test result

Linux host `phone-agent-2`, JDK `/usr/lib/jvm/java-21-openjdk-amd64`, unchanged source head `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, seed `18249`. The host build lock and disk/inode guard were applied. Command:

```bash
./gradlew --no-daemon --max-workers=2 \
  :solr:core:cleanTest :solr:core:test \
  --tests org.apache.solr.handler.admin.BackupAsyncExpiryProbeTest \
  --tests org.apache.solr.handler.admin.CoreAdminHandlerTest.testTrackedRequestExpiration \
  --tests org.apache.solr.handler.admin.api.GetNodeCommandStatusTest \
  -Dtests.seed=18249 -Ptests.jvms=1
```

**PASS: 6 tests, 0 failures, 0 errors, 0 skipped; Gradle exit 0.** Fresh JUnit XML counted one temporary probe, one existing expiration test, and four existing status-API tests. The initial attempt did not execute tests because the temporary probe referenced an unavailable `assertBusy` helper. The fixture was corrected to use a bounded task-status wait, then the focused run passed. This was a local fixture repair, not a product change or gate.

The temporary probe used the existing package-private ticker constructor with longer/shorter TTLs of 100/10 fake nanoseconds. It submitted a latch-blocked earlier child and an immediately successful later child. At fake time 50 it polled only the earlier running ID; at 110 that earlier ID still existed, the later callable's task object was completed, but the real `GetNodeCommandStatus` API returned `notfound` for the later ID. Releasing the earlier child allowed its completion to be retrieved, and at 121 its now-shortened TTL had expired. Real waits only synchronized executor completion; no real 60-minute timeout was used.

The existing `CoreAdminHandlerTest.testTrackedRequestExpiration` independently demonstrates expiration of a completed unpolled ID and shortening after a terminal poll: [test lines 491-524](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/test/org/apache/solr/handler/admin/CoreAdminHandlerTest.java#L491-L524).

Evidence on this host: `/workspace/phone-agent-2-logs/solr-18249-backup-metadata/focused.log`, `focused.xml/`, and `state.json`; the compile-only first attempt is retained in `attempt-1/`. Portable counts, fixture hashes, and output hashes are in `reports/data/SOLR-18249-backup-metadata-test-results.json`. The temporary Java fixture was removed after each attempt; the source checkout is clean. No fixture source is committed or published.

### What the tests do not prove

The probe exercises the actual tracker and status API, but does not run `CollectionHandlingUtils`' sequential loop, its six-response exception path, `BackupCmd`, a backup repository, or `ShardBackupMetadata.store`. Those call order and finalization findings are source evidence, not an end-to-end backup reproduction.

The missing integration seam is an injectable status-wait clock/sleeper plus a fake shard transport tying `ShardRequestTracker`'s submitted child IDs to the fake-ticker tracker. The tracker already has an injectable ticker, and the shard tracker accepts a `ShardHandlerFactory`; no product implementation change is required to prove the basic expiry premise. A smallest permanent integration test would use two synthetic shard submissions, record their generated IDs, route status requests to the fake-ticker tracker, and advance time while only the first ID is awaited. It would assert later `notfound`, six status responses and the exception. A BackupCmd-level extension with a recording repository would additionally assert that collection properties are never written and successful full snapshots are retained. The private waiter and hard-coded one-second sleeps currently make the complete no-real-delay loop awkward; an injected sleeper/status poller would make that test deterministic. No such permanent change is authorized or made here.

## Failure point and cleanup

On the historical full path, `copyIndexFiles` calls `processResponses` before returning. A child `notfound` exception propagates before collection ZK/config/state backup and `backup.properties` writing. The parent catch-and-clean block covers only the incremental branch, not the full-copy `else`: [BackupCmd lines 121-184](https://github.com/apache/solr/blob/a0714c8d01d070d1bf8fea2efb1d3bdece82c280/solr/core/src/java/org/apache/solr/cloud/api/collections/BackupCmd.java#L121-L184). `SnapShooter` deletes its own snapshot directory when that snapshot creation fails, not when the parent later loses status for an already successful snapshot: [SnapShooter lines 315-370](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/handler/SnapShooter.java#L315-L370). Successful sibling snapshots can remain; no full-backup parent cleanup for this later exception was found.

In contrast, current incremental `BackupCmd` catches `SolrException`, calls `cleanBackup`, and rethrows. That cleanup targets the failed backup ID's shard metadata, unreferenced index files, ZK state and backup properties; cleanup failures are suppressed onto the original exception. See [BackupCmd lines 110-133](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/api/collections/BackupCmd.java#L110-L133) and [DeleteBackupCmd lines 205-227](https://github.com/apache/solr/blob/b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb/solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteBackupCmd.java#L205-L227). That different cleanup contract must not be attributed to the legacy full path.

## Minimum evidence to resolve the production match

The remaining needs are the deployed version/build SHA; exact missing artifact basename and its relative position in the backup layout; confirmation that `incremental=false` was processed by the collection BACKUP route; and sanitized child-submission/poll ordering, timeout settings and node-restart evidence. These are evidence requirements, not authorization to access production.

Until those details distinguish the paths, the classification stays **mechanism confirmed but production match unknown**, with the production match **unresolved**. No production access, live backup, product/source commit, PR, comment, Jira post or email was performed for this assignment.
