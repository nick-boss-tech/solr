# SOLR-17051 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

In the JSON facet API, `mincount` screened regular term buckets but the
`missing` bucket was added unconditionally, so
`{"type":"terms","field":"f","mincount":500,"missing":true}` still returned
`"missing":{"count":497}`. The patch applies the same mincount screening to
the missing bucket in the two places a user-visible terms-facet response is
assembled:

1. `FacetFieldProcessor.createOutput` — after the missing bucket is filled,
   it is removed from the response when its count is below
   `effectiveMincount`. On shards `effectiveMincount` is `min(1, mincount)`,
   so shards still report any nonzero missing count and the coordinator sums
   correctly; standalone requests apply the real mincount.
2. `FacetFieldMerger.getMergedResult` — the merged `missing` bucket is only
   added when the summed count meets `freq.mincount`, mirroring the existing
   `bucket.getCount() < freq.mincount` screening for term buckets just above.

Notes for the reviewer:

- The shard-refine path (`refineFacets`) was deliberately left untouched:
  shard refine results feed the merger, whose gate applies to the summed
  count. Per-shard gating with the full mincount there would be wrong
  (400 + 400 = 800 must survive mincount 500).
- Behavior change beyond the ticket: with the default `mincount=1`, a
  missing bucket with count 0 is now omitted (previously returned as
  `"missing":{"count":0}`), consistent with how zero-count term buckets are
  screened. Flag if any existing test asserts the old shape.
- `numBuckets` counting is unchanged (the missing bucket was never counted
  there).
- Key order in the response is preserved: the bucket keeps its original
  position when retained (`res.remove` only on screen-out).
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).

Files changed:
- `solr/core/src/java/org/apache/solr/search/facet/FacetFieldProcessor.java`
- `solr/core/src/java/org/apache/solr/search/facet/FacetFieldMerger.java`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Standalone JSON-facet terms request with `missing:true` and `mincount`
   above the missing count → no `missing` key in the response; with
   `mincount` below it → `missing` present with the right count.
2. Default `mincount` (1) with zero missing docs → `missing` key absent.
3. Distributed (2+ shards): missing counts split across shards (e.g. 400 +
   400, mincount 500) → merged `missing` present with count 800; (200 + 200,
   mincount 500) → absent. Also exercise the refine path (sub-facets under
   the terms facet) for the same cases.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Range facets were not touched (ticket is terms-specific).
- Remove this file before opening the upstream PR.
