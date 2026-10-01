# SOLR-17733 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`DELETE /cluster/filestore/files/{file}` 500ed in cloud mode whenever the
file existed in ZooKeeper — the normal case for a cluster file. Root cause
chain: `ClusterFileStore.doClusterDelete` → `DistribFileStore.delete(path)` →
`deleteLocal(path)` → `checkInZk(path)`, whose guard ("The path exist ZK,
delete and retry") is meant for local-only deletes but fired on the cluster
path too — on the coordinator AND on every node in the `DeleteFile` fan-out —
and `doClusterDelete` re-wrapped the BAD_REQUEST as SERVER_ERROR.

Secondary defect: `doClusterDelete` never removed the ZK `/packageStore/<path>`
entry, so even a successful delete would let the file linger in ZK and
reappear via `refresh`/fetch.

Fix (in `DistribFileStore.delete`): remove the ZK entry via the existing
`deleteZKFileEntry` helper BEFORE `deleteLocal`. Since ZK is shared, the
guard then passes naturally on the coordinator and on every fanned-out node —
no bypass flags, no API changes, and the guard still protects genuine
local-only deletes. `DistribFileStore.delete` has exactly one caller
(`ClusterFileStore.doClusterDelete`), so no other behavior changes.

The ticket's second complaint (`sync` 500ing when the ZK node already exists)
needs no change: `distribute()` uses `SolrZkClient.makePath(..., failOnExists=false)`,
which overwrites instead of failing on existing nodes (verified in
`solrj-zookeeper` on this checkout).

File changed:
- `solr/core/src/java/org/apache/solr/filestore/DistribFileStore.java`
  (`delete`, ~4 lines added)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Cloud-mode fixture: `DistribFileStore.delete(path)` with the path present
   in ZK → local file deleted, ZK entry removed, fan-out issued, no
   BAD_REQUEST/SERVER_ERROR.
2. `deleteLocal(path)` on a ZK-resident file → guard still fires
   (no regression of the intended local-only protection).
3. `syncToAllNodes` with the ZK node already existing → no 500 (confirm the
   second half of the ticket is indeed already fixed on main).

## Patch limits and follow-ups

- **Not compiled or tested.**
- `deleteZKFileEntry` swallows KeeperException/InterruptedException with only
  a log line: if the ZK delete silently fails, `deleteLocal` still trips the
  guard and the 500 persists. Same as today's failure mode, not a regression.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
