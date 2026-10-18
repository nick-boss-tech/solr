# PR 5134 (SOLR-6831): body draft, round 2 item 6

- Head used: 4afed04987b2343af58261b83c4b49fda0af45c0 (the assignment's head and the receipt's round 3 head; live body read 2026-10-18T04:33Z).
- LIVE PR HEAD MOVED: the PR head is now 5c1124bdef95f4fa1d5bbbb6cf2dfd495111f528 (commit 2026-10-17 06:39:46 UTC, "SOLR-6831: withhold pivot data when a shard refinement answer is omitted", one commit on 4afed049, touching FacetComponent.java and TestQueryLimits.java). No receipt or gate file covers 5c1124bd. This draft does not describe it. Escalate to the main agent to decide which head the body should name once 5c1124bd is gated.
- Visible characters: 3427 (limit 3,500).
- Proof numbers come only from receipts/SOLR-6831.md: TestQueryLimits 8 of 8 at 4afed049 (gate 2026-10-15, fresh JUnit XML, seed 6831C0FFEE6831); fail-before with production FacetComponent.java at 2ee19dc646 and the head test file: 8 tests, 1 failure, testPivotFacetRefinementStopsWhenOmittedPartialResults; testPivotFacetTruncatesWhenLimitsTrip at the earlier head 24bd15ebb84 (4 of 4) and the merge base 97d973814336.
- Corrections from the stored body:
  - The Limits sentence "so the early stop does not apply there" is wrong at 4afed049. `refinePivotFacets` (FacetComponent.java, lines 1033 to 1067 at 4afed049) treats an omitted shard answer as incomplete, the same as a partial one.
  - The stored body's "9 of 9 at 5c1124bd" and "7 tests with 1 failure at ae76f048005" have no receipt at the head they name. Removed.
  - The stored body claimed distributed partialResults=false coverage as settled. The assignment says to state the gap until the review check shows it. The draft states one test and no second run.
  - The count bound (at most three refinement requests) is not reached in the omitted shape, per the receipt. The draft says so.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-6831

## What happens today

**Pivot faceting keeps building pivot values after the request's query limits are exceeded, and distributed pivot refinement keeps requesting refinements after a shard has answered with partial results.**

Other components check `timeAllowed`, `cpuAllowed` and `memAllowed` between units of work, for example [QueryComponent](https://github.com/nick-boss-tech/solr/blob/4afed04987b2343af58261b83c4b49fda0af45c0/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java) and [FacetComponent](https://github.com/nick-boss-tech/solr/blob/4afed04987b2343af58261b83c4b49fda0af45c0/solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java). Pivot faceting did not check limits inside its value loop, so after `timeAllowed` trips it keeps running lookups that fail. At the merge-base commit `97d973814336`, the value loop in [`doPivots`](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/handler/component/PivotFacetProcessor.java#L328-L345) runs a subset search for every value at every level.

In a distributed request, when a shard answers a pivot refinement request with partial results, the coordinator still queues the next level of refinement, even though its counts are incomplete.

## What this change does

**The pivot loop checks the query limits before each value and stops at the first limit that has tripped. The coordinator stops pivot refinement once a shard refinement answer is incomplete.**

[`doPivots`](https://github.com/nick-boss-tech/solr/blob/4afed04987b2343af58261b83c4b49fda0af45c0/solr/core/src/java/org/apache/solr/handler/component/PivotFacetProcessor.java#L328-L333) checks the limits before each value. When one has tripped, the loop stops and returns the values built so far, and the response is marked partial with the detail "Faceting pivots". The check runs at every level.

In [`refinePivotFacets`](https://github.com/nick-boss-tech/solr/blob/4afed04987b2343af58261b83c4b49fda0af45c0/solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java#L1023-L1078), when any shard's refinement answer is incomplete (its partialResults header is `true` or `omitted`), the coordinator marks its response partial and queues no further refinement. An incomplete answer with no pivot data is skipped rather than failing the request.

Behavior change, stated openly: the check covers every limit the shared code knows, so `memAllowed` is included. With `partialResults=false`, a tripped limit is reported as "omitted" and no pivot data is returned.

## Proof

**`TestQueryLimits` passes 8 of 8 at head `4afed04987b2343af58261b83c4b49fda0af45c0` (gate, 2026-10-15, fresh JUnit XML).**

- Fail-before: with production `FacetComponent.java` at `2ee19dc646` and the head test file, one test fails, `testPivotFacetRefinementStopsWhenOmittedPartialResults`. The base code throws "No pivot refinement response from shard" on the omitted answer. The head skips that answer, and the test passes.
- That test's bound of at most three refinement requests is not reached in this shape, because the throw comes first. The test shows the outcome, not the count.
- `testPivotFacetTruncatesWhenLimitsTrip` fails on the merge base `97d973814336` and passes at the earlier head `24bd15ebb84` (gate, 2026-10-14, 4 of 4 at that head).

## Limits

**The check runs between values. The distributed `partialResults=false` path has one test, and no second run has confirmed it.**

- A limit that trips inside one value's subset search ends that search as before.
- With `partialResults=false`, an omitted shard answer stops refinement the same way a partial answer does.
- That path is covered by one test, `testPivotFacetRefinementStopsWhenOmittedPartialResults`. Its count bound is not reached (see Proof), and no second run has confirmed it. No wider coverage is claimed.
- The searcher's exception handling is not changed here.
- No test measures how many lookups are saved.

Changelog: [changelog/unreleased/SOLR-6831-pivot-facet-limits.yml](https://github.com/nick-boss-tech/solr/blob/4afed04987b2343af58261b83c4b49fda0af45c0/changelog/unreleased/SOLR-6831-pivot-facet-limits.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
