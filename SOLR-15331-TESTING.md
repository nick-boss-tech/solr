# SOLR-15331 — Testing Handoff (external reviewer)

Ticket: https://issues.apache.org/jira/browse/SOLR-15331
("'Missing' count lost when convert facetResponse", reporter: Robin Li)
Branch: `solr-15331-submit` (based on origin/main @ 56ec140e363)
Fix commit: 7dcb1bdee97 — "SOLR-15331: expose missing bucket count in BucketBasedJsonFacet"

## What the patch does
For JSON terms facets with the `missing` option, the server returns a top-level
`"missing": {"count": N}` entry, but SolrJ's `BucketBasedJsonFacet` had no branch
for the `"missing"` key — it fell into the silently-ignore `else`. Added a
`missingBucketCount` field (default `UNSET_FLAG`), a constructor branch parsing
`((NamedList) value).get("count")`, and a `getMissingCount()` getter, mirroring
the existing `allBuckets`/`before`/`after`/`between` handling exactly.

## What to verify
1. Compiles: `:solr:solrj:compileJava -Pvalidation.errorprone=true` (solrj targets
   Java 17 language level per repo AGENTS.md).
2. Unit test: construct `BucketBasedJsonFacet` from a `NamedList` containing a
   `"missing"` entry (`{"count": 42}`) plus regular buckets; assert
   `getMissingCount() == 42` and other getters unaffected.
3. Absent-key case: response without `"missing"` leaves `getMissingCount()` at
   `UNSET_FLAG` (-1).
4. Existing suites: solrj JSON facet response tests, if present.

## AI disclosure
This change was drafted with AI assistance (Muse) and has not been compiled or
tested by the author. Human review, compilation, and test validation required
before any upstream PR.
