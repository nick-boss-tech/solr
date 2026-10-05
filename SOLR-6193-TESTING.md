# SOLR-6193 - hypothetical reproduction and fix (not run)

Nothing here was compiled or executed. The fix and the test changes were written by reading
`upstream/main`; treat every claim below as a guess to verify first.

## JIRA context
"using facet.* parameters as local params inside of facet.field causes problems in distributed
search" (Hoss: the distributed code predates local params in facet params and is built on
`f.<field>.facet.*` overrides). The `facet.field` half is the same cause as SOLR-11129 (branch
`solr-11129-submit`, `FacetComponent.FieldFacet`). This branch is the pivot half.

## Bug mechanism
On the shards `PivotFacetProcessor` reads the pivot's local params, so each shard applies e.g.
`{!facet.sort=index facet.limit=4}`. The coordinator side, `PivotFacetField`, builds its
limit / offset / sort / mincount from `rb.req.getParams()` only. It merges and trims shard
output with different settings than the shards used (for example count sort and limit 100
instead of index sort and limit 4), so results differ from a non-distributed request.
`DistributedFacetPivotLargeTest` carried five commented-out cases tagged "Broken: SOLR-6193".

## Fix
`PivotFacet` passes its local params to the top level `PivotFacetField`, which layers them over
the request params (`SolrParams.wrapDefaults`, same as the 11129 branch). Nested fields take
the params object of their parent field (`PivotFacetValue.getParentPivot()`), so
`{!facet.limit=4}a,b` applies at both levels, as it does on the shards. The old four-argument
`createFromListOfNamedLists` is kept and delegates.

## What the test pins
The five commented-out `DistributedFacetPivotLargeTest` cases are enabled again:
`{!facet.limit=4 facet.sort=index}`, `{!facet.sort=index}` plus global limit, the `sc`/`si`
two-pivot case, `{!facet.limit=-1}`, `{!f.id.facet.limit=-1}`, `{!f.place_s.facet.limit=-1}`.

## What was guessed / verify first
- That the commented cases were disabled only because of this coordinator-side gap. If one still
  fails with the fix, look at the shard-side `PivotFacetProcessor` handling of that local param
  and at the refinement requests in `FacetComponent` (`shardsRefineRequestPivot`).
- Precedence: a global `f.<field>.facet.*` override still beats a local `facet.*` (wrapDefaults
  reads the `f.` key first); only the `{!f.<field>.facet.*}` local form and plain local
  `facet.*` are addressed.
- The `facet.pivot.mincount` and `facet.offset` local params follow the same path but have no
  new assertion.
- Fail-before: revert only the three `src/java` files; the first re-enabled case should fail.
- Spotless formatting.
