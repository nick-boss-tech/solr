# SOLR-15478 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

Schema changes were not visible after a configset was deleted and re-created
under the same name (backup/restore, or `zk downconfig` → edit schema → `zk
upconfig`): the schema API kept serving the OLD schema until server restart.

Root cause: `ConfigSetService.createIndexSchema` caches `IndexSchema` in a
Caffeine `schemaCache` keyed by `configSet / guessSchemaName / modVersion /
luceneMatchVersion`. For ZK-backed configsets, `modVersion` came from
`ZkConfigSetService.getCurrentSchemaModificationVersion`, which returned
`stat.getVersion()` — the znode data version that resets to 0 when the znode
is deleted and recreated. A re-uploaded configset whose schema was never
edited in ZK (version 0 both before and after) produced an identical cache key,
so `schemaCache.get` returned the stale schema from the previous generation.
The cache is never invalidated on configset delete.

Fix (`solr/core/src/java/org/apache/solr/cloud/ZkConfigSetService.java`,
only file changed): return `stat.getMzxid()` instead of
`(long) stat.getVersion()`. `mzxid` increases monotonically per ZooKeeper
ensemble and is never reset by znode delete/re-create, so a re-uploaded
configset always gets a new cache key. Every data-version bump is also an
mzxid bump, so no change-detection power is lost. The returned value is used
only as an opaque cache-key component in `ConfigSetService.createIndexSchema`
(verified by grep — the only other override is a test stub returning null),
so the semantic change is safe.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- SolrCloud integration test mirroring the reporter's repro: create configset
  + collection → `zk downconfig` → add a `<field>` to the local
  managed-schema → delete collection + configset from ZK → `zk upconfig` →
  recreate the same-named collection → assert `/schema/fields` shows the new
  field without a restart (fails before the patch, passes after).
- Unit-level: stub `zkClient.exists` to return a `Stat` with a reset data
  version but a newer mzxid across a simulated delete/re-create, and assert
  `getCurrentSchemaModificationVersion` differs between the two generations.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Related but distinct from the SOLR-15674 fix (that one addressed
  `ZkSolrResourceLoader`/`IndexSchemaFactory` resource caching keyed on znode
  version; this one addresses the `ConfigSetService.schemaCache` keyed on
  `getCurrentSchemaModificationVersion`).
- Remove this file before opening the upstream PR.
