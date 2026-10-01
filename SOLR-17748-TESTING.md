# SOLR-17748 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

With `shards.info=true` and some shards down, distributed queries threw NPE
in `QueryComponent.returnFields()`: the `SHARDS_INFO` error-recording block
did `shardInfo.get(srsp.getShard())` and dereferenced the result without a
null check. The entry can be missing two ways: (1) the populating loop in
`handleRegularResponses` `continue`s before `shardInfo.add(...)` when a
shard's response has no header/docs; (2) a null/empty shard name is recorded
under a generated `"unknown_shard_N"` key at add time, so the raw-name lookup
misses.

The fix null-checks the lookup: when no entry exists, a fresh
`SimpleOrderedMap` is created, added to `shardInfo` under the shard's name,
and the error is recorded in it as usual — the error stays visible in
`shards.info` (the point of the feature) instead of throwing.

File changed:
- `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`
  (`returnFields`, ~8 lines added)

`NamedList.get(null)`/`add(null, ...)` are null-safe, so a null shard name
cannot reintroduce the NPE through this path.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Drive `returnFields()` (or `handleResponses`) with `shards.info=true`
   and a `ShardResponse` carrying an exception whose shard name was never
   registered in the `shards.info` NamedList → expect no NPE and an `error`
   entry recorded. Check `TestDistributedSearch` / `ShardParams.SHARDS_INFO`
   usages for existing down-shard helpers.
2. Null/empty shard name with exception → no NPE.

## Patch limits and follow-ups

- **Not compiled or tested.**
- The `"unknown_shard_N"` rename asymmetry is only partially addressed: a
  null-named shard that already has a renamed entry will get a second entry
  under its raw (null/empty) name rather than reusing the renamed one. No
  NPE, but slightly untidy `shards.info` output. A reviewer may prefer to
  unify the key computation between the add loop and this lookup.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
