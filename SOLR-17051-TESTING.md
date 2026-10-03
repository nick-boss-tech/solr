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

1. `FacetFieldProcessor.createOutput` — for a non-distributed request, after
   the missing bucket is filled it is removed from the response when its count
   is below the request's `mincount`. Shard sub-requests always report the
   bucket (round-3 review change: the first version also dropped it on shards
   when the shard count was 0, which could leave the merger's `missingBucket`
   null and NPE in `getRefinementSpecial`, and broke old coordinators); the
   coordinator's merger screens the summed count.
2. `FacetFieldMerger.getMergedResult` — the merged `missing` bucket is only
   added when the summed count meets `freq.mincount`, mirroring the existing
   `bucket.getCount() < freq.mincount` screening for term buckets just above.

Notes for the reviewer:

- The shard-refine path (`refineFacets`) was deliberately left untouched:
  shard refine results feed the merger, whose gate applies to the summed
  count. Per-shard gating with the full mincount there would be wrong
  (400 + 400 = 800 must survive mincount 500).
- Behavior change beyond the ticket, **open design decision**: with the
  default `mincount=1`, a missing bucket with count 0 is now omitted
  (previously returned as `"missing":{"count":0}`). An existing test asserts the
  old shape: `TestJsonFacets` (`f3:{ ... missing:{count:0} }` in the
  `excludeTags` terms test) fails with the current patch, in both the
  standalone and distributed runs. `FacetFieldMerger.getRefinementSpecial` also
  documents that the special buckets "will always be included". Either keep the
  contract for the default mincount (only screen when the user passes a
  `mincount` above 1) and leave that test alone, or change the contract and
  update the test and the ref guide. Not decided here.
- `numBuckets` counting is unchanged (the missing bucket was never counted
  there).
- Key order in the response is preserved: the bucket keeps its original
  position when retained (`res.remove` only on screen-out).
- Changelog fragment added: `changelog/unreleased/SOLR-17051.yml` (wording
  assumes the ticket's `mincount` case; revise with the design decision).

Files changed:
- `solr/core/src/java/org/apache/solr/search/facet/FacetFieldProcessor.java`
- `solr/core/src/java/org/apache/solr/search/facet/FacetFieldMerger.java`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Tests added (round-3 patch pass, **not compiled or run**), in `TestJsonFacets`
next to the existing `missing` cases, so they run standalone and distributed:

- `missing:true, mincount:4` on `sparse_s` (4 docs without a value) → `missing`
  present with count 4; `mincount:5` → no `missing` key. These hold under either
  contract choice above (both use a `mincount` above the default).

Queued for the verification run: `org.apache.solr.search.facet.TestJsonFacets`
and `org.apache.solr.search.facet.TestJsonFacetRefinement`, with Spotless.
Expect `TestJsonFacets` to fail on the zero-count `missing:{count:0}` assertion
until the contract decision is made.

Not covered: the refined request with sub-facets and no missing documents (the
NPE guard), and the split-count distributed cases (400 + 400); both need a
dedicated distributed fixture.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Range facets were not touched (ticket is terms-specific).
- Remove this file before opening the upstream PR.
