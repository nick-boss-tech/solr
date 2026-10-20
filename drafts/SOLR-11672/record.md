# Skip record: SOLR-11672

- Ticket: SOLR-11672, NPE in FacetComponent countFacets(). JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; the link field shows "Reference SOLR-2803 (Closed)"; updated 2019-06-08.
- Verdict: keep-open.
- Draft branch: solr-11672-draft-open (unchanged status; this record replaces the bulk audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: with two shards and a facet.query under facet.mincount, the first shard's response causes removeQueryFacetsUnderLimits to remove the query facet from FacetInfo.queryFacets; when the second shard's response is counted, get() returns null and qf.count() throws a NullPointerException. The reporter proposes moving the removal to finishStage. The link to SOLR-2803 (closed) is the earlier report.

## Code reading

- solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java:716-745 (countFacets): for each shard response in the request, facet query counts are added with `QueryFacet qf = fi.queryFacets.get(returnedKey); qf.count += count;` (lines 732-736). There is no null check on qf, so a key already removed from fi.queryFacets gives the NullPointerException the ticket describes.
- FacetComponent.java:844 (end of countFacets): removeQueryFacetsUnderLimits(rb) runs at the end of each countFacets call, which removes query facets below facet.mincount from fi.queryFacets. The removal is guarded only by rb.getStage() == STAGE_EXECUTE_QUERY (FacetComponent.java:848-850).
- FacetComponent.java:847-870 (removeQueryFacetsUnderLimits): builds newQueryFacets from entries whose count is at least the minimum; the others are dropped.
- FacetComponent.java:1090-1092 (finishStage): the facet output is assembled only at STAGE_GET_FIELDS, so the removal is not in finishStage at the pin.

So the unguarded lookup and the removal inside countFacets are both present. The NPE needs a second countFacets call that sees the key removed by the first; whether the shard responses reach countFacets in separate calls (so that the second call runs after the removal) depends on how the sub-requests are batched, which was not traced in this pass.

## History

- No commit at the pin names SOLR-11672 (git log --grep at the pin: none).
- SOLR-2803 is linked as the earlier report; its closing commit was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a two-shard collection at the pin with a facet.query and facet.mincount such that the first shard's count is below the minimum, recording whether the second shard's response throws the NullPointerException at FacetComponent.java:734. Not built or run in this pass.

## Gap analysis

The unguarded lookup at FacetComponent.java:734 and the removal at :844 are both present, so the ticket's mechanism is in the code. Whether the ordering the ticket describes happens in practice (a second countFacets call after the removal) is not established by reading. The reporter's proposed fix (move the removal to finishStage) is not what the code does at the pin.

## Verdict justification

keep-open: the NPE site and the removal are present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a two-shard run at the pin with facet.query and facet.mincount, recording whether the NullPointerException occurs at FacetComponent.java:734; if it does not, close-cannot-reproduce on that run.

## Limits

This record does not run the two-shard query, does not trace the sub-request batching, and does not post anything to JIRA or GitHub.
