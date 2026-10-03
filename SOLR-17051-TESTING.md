# SOLR-17051 — Testing handoff

**Status: design decision pending.** The branch has been compiled as part of
the focused verification run. Spotless passed, but `TestJsonFacets` reported
two failures because the current implementation changes the existing default
contract for a zero-count `missing` bucket. The failure is evidence for the
decision below, not a reason to guess at a semantic fix.

The focused run used `TestJsonFacets` and `TestJsonFacetRefinement`. The two
failures were `TestJsonFacets.testStats` and `testStatsDistrib`; both expect
`missing:{count:0}` with `missing:true` and the default `mincount`. The
refinement suite passed and `:solr:core:spotlessJavaCheck` passed.

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

## The contract decision

The question is whether `missing:true` should make the special missing bucket
follow the same `mincount` rule as ordinary term buckets.

Today, Solr's behavior and existing tests treat `missing` as a special bucket:
when it is requested, the response includes it even when its count is zero.
That makes this response shape stable:

```text
missing:true, default mincount -> missing:{count:0}
```

The issue reports a different case:

```text
mincount:500, missing:true, missing count:497
```

The reporter expects the bucket to be omitted because 497 is below 500. The
branch currently implements that rule, but doing so also omits a zero-count
bucket under the default `mincount=1`, which is the compatibility change that
the focused test exposed.

There are three possible decisions:

1. **Preserve the existing contract.** Always return `missing` when requested,
   including `count:0`. This is maximally compatible, but does not apply
   `mincount` to the ticket's `missing:497, mincount:500` case.

2. **Apply `mincount` universally.** Omit `missing` whenever its count is
   below `mincount`, including the default `mincount=1`. This is the most
   literal interpretation of treating `missing` like a term bucket, but it is
   a response-shape change: clients must tolerate an absent `missing` key even
   when they requested `missing:true`.

3. **Use the narrow compatibility interpretation (recommended).** Apply the
   screening only when `mincount` is greater than the default. Thus the
   reported `mincount:500` case is fixed, while the common default case still
   returns `missing:{count:0}`. This preserves the existing test and the
   established response shape for default requests, while giving explicit
   high-mincount requests the filtering behavior the ticket asks for.

The existing assertion that makes this choice visible is in
`TestJsonFacets.java` around line 3279 (`f3 ... missing:{count:0}`). The
branch's new tests cover `count=4` with `mincount=4` (retained) and
`mincount=5` (omitted); those cases are compatible with options 2 and 3.

The reviewer should explicitly choose one option before changing the test,
documentation, or changelog. If option 2 is selected, the JSON Facet API guide
must document that `missing` can be absent despite `missing:true`. If option 3
is selected, the implementation and tests should make the `mincount > 1`
boundary explicit.

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
- This is a compatibility decision, not merely a test expectation. Code that
  currently reads `missing.count` without checking for a missing key may break
  under option 2 (and under option 3 for explicit `mincount > 1` requests).
- The branch does not claim that omitting the bucket saves facet computation:
  the missing bucket and its sub-facets are computed before the response gate.
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

Tests added (round-3 patch pass), in `TestJsonFacets`
next to the existing `missing` cases, so they run standalone and distributed:

- `missing:true, mincount:4` on `sparse_s` (4 docs without a value) → `missing`
  present with count 4; `mincount:5` → no `missing` key. These hold under either
  contract choice above (both use a `mincount` above the default).

Queued for the verification run: `org.apache.solr.search.facet.TestJsonFacets`
and `org.apache.solr.search.facet.TestJsonFacetRefinement`, with Spotless.
The current branch is expected to fail on the zero-count
`missing:{count:0}` assertion until the contract decision is made.

Not covered: the refined request with sub-facets and no missing documents (the
NPE guard), and the split-count distributed cases (400 + 400); both need a
dedicated distributed fixture.

## Patch limits and follow-ups

- The implementation is compiled and Spotless-clean, but the focused proof is
  not green until the contract choice is resolved.
- Range facets were not touched (ticket is terms-specific).
- Keep this handoff document for review until the decision is recorded; remove
  it before opening an upstream PR if the project does not want it included.
