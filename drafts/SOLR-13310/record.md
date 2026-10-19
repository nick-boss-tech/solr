# Skip record: SOLR-13310

- Ticket: SOLR-13310, facet.threads is using the updateExecutor.
- Verdict: keep-open.
- Draft branch: solr-13310-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at the pin in this pass.
- Source: the queue audit (round audit-1, 2026-10-04, confirmed-skip) is replaced by this pass.

## Ticket reading

JIRA read 2026-10-19 (this pass): Open, resolution None, created 2019-03-08, updated 2019-06-08, no fix versions. Link: problem/incident SOLR-12801 (Open). The report: `SimpleFacets` takes its facet executor from the update shard handler's update executor, which hossman (2019-03-08) says was introduced with SOLR-12801 and looks like a bug. The queue audit's note says the bug claim was later walked back and the ticket reads as an executor-lifecycle proposal; that later comment is not re-read in this pass.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/request/SimpleFacets.java:170`: `this.facetExecutor = req.getCoreContainer().getUpdateShardHandler().getUpdateExecutor();`. The line the ticket names is still there.
- `SimpleFacets.java:864`: `private final Executor facetExecutor;`.
- `SimpleFacets.java:593`: `Executor executor = threads == 0 ? directExecutor : facetExecutor;` (per-field facet threading).
- `SimpleFacets.java:885-886`: `int maxThreads = req.getParams().getInt(FacetParams.FACET_THREADS, 0);` and `Executor executor = maxThreads == 0 ? directExecutor : facetExecutor;`. So `facet.threads` non-zero runs on the update executor.
- `solr/core/src/java/org/apache/solr/cloud/SyncStrategy.java:74` and `solr/core/src/java/org/apache/solr/update/StreamingSolrClients.java:56` also take the update executor, so the update executor is shared by update and sync work as well as facets.

So the facet path uses the update executor at the pin, as the ticket says. Whether that causes a problem (starvation of update work, or shutdown issues) is not shown by reading.

## History

- No commit at the pin names SOLR-13310 (evidence tool: none).
- The line at `SimpleFacets.java:170` was not traced to its introducing commit in this pass; the ticket links it to SOLR-12801.

## Reproduction attempts

Not run. No build or test run in this pass.

## Gap analysis

- The code path is present. The ticket's concern (facets share the update executor) is a design question about executor sharing, not a crash or wrong result the reading shows.
- The reporter-side bug claim was later walked back per the audit note, so there is no reproduction target. No fix spec is in the ticket.
- Missing piece: a maintainer decision on whether facet threads should have their own executor, or a load test that shows update starvation under `facet.threads` at the pin.

## Verdict justification

keep-open: the executor sharing the ticket names is present at the pin, the ticket has no reproduction and no fix spec, and the earlier bug claim was walked back. A close verdict would need a fix or an obsolescence check (neither applies); pr-needed needs a reproduced problem or an agreed design.

Missing for a closing or PR verdict: the maintainer decision on executor ownership, or a load run that shows update starvation.

## Limits

This record does not run facet queries under load. It does not re-read the later comments that walk back the bug claim. It does not post anything to JIRA or GitHub.
