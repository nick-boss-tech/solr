# SOLR-15674 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

Repro: create a collection, delete it, recreate it with the same name but a
different initial schema. The schema API / Solr UI then returned the OLD
schema; editing the phantom fields 500'd. Root cause: the node-level
`ObjectCache` in `IndexSchemaFactory.getFromCache` validated cached schema
configs only against the ZK znode's *data version* — but a
deleted-then-recreated znode legitimately has data version 0 again, so the
stale entry "matched" and the old schema was served.

Fix (delete/recreate-aware freshness, per the ticket's suggested shape):

- `solr/core/src/java/org/apache/solr/cloud/ZkSolrResourceLoader.java` —
  `getZkResourceInfo` now returns `Pair<String, Stat>` (the full ZK `Stat`,
  not just the data version) so callers can also read `czxid`. Sole caller
  is `IndexSchemaFactory.getFromCache`; no other usages in the repo.
- `solr/core/src/java/org/apache/solr/schema/IndexSchemaFactory.java` —
  `VersionedConfig` gains a `czxid` field (new 3-arg constructor; the old
  2-arg constructor is kept and records `czxid = -1`). `loadConfig` captures
  `czxid` from the ZK stat at load time. `getFromCache` now requires both
  `version` and `czxid` to match for a cache hit; czxid always increases
  across delete/recreate, so a stale entry can never match the new znode. A
  cached `czxid` of -1 (entries predating czxid tracking) falls back to the
  old version-only check — no behavior change for those.
- `solr/core/src/java/org/apache/solr/core/SolrConfig.java` — its
  `ResourceProvider` captures `zkCzxid` and `readXml` passes it into
  `VersionedConfig`, so solrconfig caching (which shares `getFromCache`)
  gets the same fix. `znodeVersion` semantics are unchanged (still the data
  version).

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- Unit test on `IndexSchemaFactory.getFromCache` with a stubbed
  `ZkSolrResourceLoader` whose `getZkResourceInfo` returns the same path and
  same data version but a *different* czxid (simulating delete+recreate) →
  expect a cache miss (loader re-invoked), not the stale cached config.
- Regression: same path, same version, same czxid → cache hit preserved
  (normal path unchanged).
- Existing `IndexSchemaFactory` / configset test suites for regressions.

## Patch limits and follow-ups

- **Not compiled or tested.**
- `getZkResourceInfo`'s signature changed (`Pair<String, Integer>` →
  `Pair<String, Stat>`); it had exactly one in-repo caller, but external
  plugins calling it would need updating.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
