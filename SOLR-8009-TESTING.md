# SOLR-8009 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-8009 - "RealTimeGet NPE with implicit router-based collection" (Crawdaddy). Related duplicates in the queue: SOLR-8954 (same symptom; workaround is passing `shards=`; later reports of duplicates when naming all shards).
- Branch: `solr-8009-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`RealTimeGetComponent.createSubRequests` asks the collection's router for the target slice of each id. `ImplicitDocRouter.getTargetSlice` returns `null` when no `_route_` is given ("no shard specified"). The NPE from the 5.3 report is gone, but on main the loop just does `if (slice == null) continue;`, so these ids are silently dropped and `/get` returns nothing for implicit-router collections unless the caller adds `_route_` or `shards`.

## What the branch changes
- `RealTimeGetComponent.createSubRequests`: ids without a target slice are collected and sent in one extra sub request to all shards (the same shape the `shards=` branch uses: `sreq.shards = null`).
- New `FullSolrCloudDistribCmdsTest.testRealTimeGetImplicitRouterWithoutRoute`: implicit collection with `shard1,shard2`, one doc on each; `getById` for each id and for both returns the docs.

## What was guessed (verify these first)
1. That the missing results come from the dropped `slice == null` case and not from a different layer (e.g. the `/get` handler's own routing in `HttpSolrCall`/`SearchHandler`).
2. The `shards = null` ("ALL") request merges responses for these ids correctly; with several replicas per shard the same doc might come back more than once (test uses 1 replica per shard).
3. `CloudSolrClient.getById(String, String)` and `getById(String, Collection)` hit `/get` on a random node without extra params.
4. Alternative worth discussing: reject the request with a clear 400 asking for `_route_`/`shards` instead of querying all shards.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.cloud.FullSolrCloudDistribCmdsTest"
```
Fail-before: revert only `RealTimeGetComponent.java`; `getById` returns null.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
