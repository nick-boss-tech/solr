# SOLR-13239 — Testing Handoff

> **UNCOMPILED / UNTESTED.** This patch was written against `apache/solr` main
> at `c3e18f1e455` without running a build or any tests. The reviewer (or CI)
> must compile and validate before merge.

## What changed

`solr/solrj-zookeeper/.../cloud/ZkStateReader.java` — `getCurrentCollections()`:
lazy collections are now included in the reported set only when their
`LazyCollectionRef.get(true)` returns non-null, i.e. their `state.json` can
actually be read. Previously every `/collections` child znode name was
reported, so `CloudCollectionsListener.onChange` fired for a collection whose
`state.json` did not exist yet and `getCollection(name)` returned `null`
(indistinguishable from a deletion). Watched collections were already gated
(`activeCollections()` filters on non-null `currentState`); this extends the
same "exists" semantics to lazy collections. Deletion signaling is unchanged:
a removed `/collections` child is dropped from `lazyCollectionStates` by
`refreshCollectionList` and still reported as removed.

Also added: `changelog/unreleased/SOLR-13239.yml`.

## Suggested reviewer validation

```bash
./gradlew :solr:solrj-zookeeper:compileJava -Pvalidation.errorprone=true
./gradlew :solr:solrj-zookeeper:test --tests "org.apache.solr.common.cloud.ZkStateReaderTest"
```

Suggested new coverage (not included): with a ZK test harness, create a
`/collections` child znode without `state.json`, refresh, and assert the name
is absent from `getCurrentCollections()`; then write `state.json`, trigger a
refresh, and assert it appears. Also assert a deleted collection is still
reported as removed.

## Limits / risks

- The gate adds at most one ZK read per lazy collection per ~2s
  (`LAZY_CACHE_TIME` throttle inside `LazyCollectionRef.get(true)`); previously
  `getCurrentCollections()` did no ZK I/O. The same lazy `get()` path is
  already exercised in `constructState` via `notifyStateWatchers`, so no new
  failure mode under ZK outages.
- A newly created collection appears in `onChange` on the next cluster-state
  refresh after `state.json` becomes readable, rather than on the
  `/collections` children watch that fires when its znode is created. There is
  no dedicated re-notify; in practice collection creation is followed by
  further state updates that trigger refreshes.
- No new tests were added in this phase per the contribution workflow.
