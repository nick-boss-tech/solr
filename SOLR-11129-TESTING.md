# SOLR-11129 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-11129 - "Distributed facet search with localparm facet.mincount doesn't work in a multi shard cloud env." (Gregory Loscombe). Reported on 4.10/5.5/6.6; `facet.mincount` as a local param is ignored and 0-count terms come back. Works when passed as a plain request param.
- Branch: `solr-11129-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`SimpleFacets` (shard side) builds its params with `SolrParams.wrapDefaults(localParams, global)`, so a local `facet.mincount` is honored there. `FacetComponent.FieldFacet` (coordinator side) read only `rb.req.getParams()`, so the coordinator believed `minCount == 0`.
The final `removeFieldFacetsUnderLimits` pass also looked the value up with `getFieldInt(<response key>, ...)` on the global params, so it never saw the local value either (and used the key, not the field name, when `key=` was set).
Result: nothing is removed at merge time, and 0-count terms added during merging/refinement stay in the response.

## What the branch changes
- `FieldFacet` constructor: `fillParams` now receives `SolrParams.wrapDefaults(localParams, rb.req.getParams())` (null-safe).
- `removeFieldFacetsUnderLimits`: uses `DistribFieldFacet.minCount` (already computed in the constructor) instead of re-reading global params.
- New `DistributedFacetLocalParamsMinCountTest` (`BaseDistributedSearchTestCase`, 3 shards): `facet.field={!key=k facet.mincount=1}t_s` with an `fq` that leaves one term; and a `facet.mincount=2` local param.

## What was guessed (verify these first)
1. **Root cause**: only reasoned from code. Shards still receive the original `facet.field` value including the local `facet.mincount`, which overrides the `f.<field>.facet.mincount` the coordinator sets (`initialMincount`). For `mincount=1` that is harmless; for larger local values each shard applies the full value per shard, which can drop terms that only reach the threshold in total. Not fixed here (would need rewriting the local params sent to shards).
2. **Test expectations**: `query(params)` compares control vs distributed; the extra `assertEquals` values assume the sample data above. `QueryResponse.getFacetField("k")` is assumed to find the field by response key.
3. The second scenario (`mincount=2`) may expose the shard-side override in guess 1 and fail even with the fix; if so, treat that as a separate finding.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.component.DistributedFacetLocalParamsMinCountTest"
```
Fail-before: revert only `FacetComponent.java`; the first query should list zero-count terms (or differ from control).

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
