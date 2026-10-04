# SOLR-14451 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-14451 - "debug=query does not reliably ensure json.facet debug info returned in cloud clusters" (Hoss, 2020; Munendra posted a partial patch, never committed). The old skip note said "uncertain root cause". Reopened in audit round audit-1 (Tier 1 batch 9).
- Branch: `solr-14451-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
`FacetModule.process` (on a shard) creates its `FacetDebugInfo` only when `rb.isDebug()` is true on that shard. In a distributed request the facet work happens in the shard requests with `PURPOSE_GET_JSON_FACETS` (the top-ids request) and `PURPOSE_REFINE_JSON_FACETS`. `DebugComponent.modifyRequest` turns debug *off* for every shard request that is not `PURPOSE_GET_FIELDS` (`debugQuery=false`, `debug=false`) and then re-adds only `debug=timing` and `debug=track`. So:

- `debug=true` (all flavors on) leaves `debug=timing` and `debug=track` on the facet request, shards are in debug mode, and `facet-trace` comes back. That is why Hoss saw it "work".
- `debug=query` (or `debug=results`) leaves the facet request with no debug at all, shards skip `FacetDebugInfo`, and the coordinator merges nothing: no `facet-trace`. It "depends on the randomized index" only because small single-shard-result cases can run as a single pass where the top-ids and get-fields requests are combined.

`TestCloudJSONFacetSKGEquiv` carries the workaround `"debug", "true", // SOLR-14451` in two places, which pins this down.

## What the branch changes
- `DebugComponent.modifyRequest`: for facet shard requests (`PURPOSE_GET_JSON_FACETS` / `PURPOSE_REFINE_JSON_FACETS`) that are not `GET_FIELDS`, when neither timing nor track will be set, add `debug=query` so the shard stays in debug mode. Everything else is unchanged; the cost is the extra `parsedquery`-style debug in those shard responses, which `finishStage` already merges.
- `TestCloudJSONFacetSKGEquiv`: both workarounds changed from `debug=true` to `debug=query`; no new test class.

## Guesses to verify first
1. The shard really parses `debug=false` followed by `debug=query` (multi-valued `debug`) as debug-query on; the existing code already stacks `debug=false` then `debug=timing`, so this should be fine.
2. The merged coordinator `debug` section does not get duplicate `parsedquery` entries that break `TestCloudJSONFacetSKGEquiv`'s "only inspect the first debug NamedList" logic. Look at the merge of equal string values in `DebugComponent.merge`.
3. `debug=results` alone is treated the same (it also adds `debug=query` on facet requests); that is harmless but not asserted.
4. Munendra's note that `debug=timing` returns query debug instead of only timings is a separate issue and is not touched.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.search.facet.TestCloudJSONFacetSKGEquiv"
```
Fail-before: revert the `DebugComponent` hunk; the two tests that now use `debug=query` should lose the `facet-trace` (assertNotNull on the debug section fails).

## Not done
No JIRA comment, no PR.
