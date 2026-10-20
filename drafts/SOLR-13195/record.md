# Skip record: SOLR-13195

- Ticket: SOLR-13195, NPE caused by distributed queries when no shards are configured.
- Verdict: keep-open.
- Draft branch: solr-13195-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 9 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-01-31, updated 2021-02-01, no fix versions, no links. The report: `select?distrib=true&facet=on&facet.field=genre` returns HTTP 500 with a `NullPointerException` in `FacetComponent`. A second request (`select?distrib=true`) fails with an NPE in `SearchHandler.handleRequestBody`. One comment (2019-01-31, jkloos) gives the second stack trace.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java:312-315`: a request is distributed when the `shards` parameter contains a URL (`indexOf('/') > 0`); `distrib=true` alone does not set it.
- `SearchHandler.java:326-334` (`getAndPrepShardHandler`): when `rb.isDistrib` is true, `shardHandler.prepDistributed(rb)` runs, and if the request is not distributed after all, the shard handler is set to null.
- `solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java:555`: `rb.shards = new String[rb.slices.length]` is set in the distributed preparation path. The audit note says `prepDistributed` now populates `rb.shards` on all distributed paths; this pass read the assignment on the cloud path and did not trace the standalone path.
- `solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java:373` and following loop over `rb.shards.length`: the NPE in the report is at this kind of access, so the NPE needs `rb.shards` null, which the gate above prevents when `isDistrib` is false.
- No commit at the pin names SOLR-13195 (the evidence tool lists none).

## History

No commit names the ticket. The history of the `isDistrib` gate and `rb.shards` assignment was not traced in this pass.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run at the pin: a standalone core with the ticket's `films` collection shape, and a cloud core, each with `distrib=true&facet=on&facet.field=genre` and `distrib=true`, checking for the NPE and the HTTP status.

## Gap analysis

- The reading suggests the NPE needs `rb.shards` to be null on a path where `isDistrib` is true; the gate and the assignment make that unlikely at the pin, but the reading does not show that every path is covered.
- The ticket's second request has no facet and still NPEs in `SearchHandler`; that path is not traced.
- The ticket is from 2019 and was updated in 2021 without a resolution.

## Verdict justification

keep-open: the gate that the ticket's NPE would need is present at the pin, but no run shows the two requests succeed or fail, and the second stack trace is not traced. close-fixed would need a fix commit named and checked, and none is found; close-obsolete does not fit (the request path is present).

Missing for this verdict: a run of the two requests against a standalone and a cloud core at the pin, with the outcome recorded.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
