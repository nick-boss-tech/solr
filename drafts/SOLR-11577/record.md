# Skip record: SOLR-11577

- Ticket: SOLR-11577, HttpShardHandler does not retry on stale clusterstate. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments; updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the bulk audit-derived record on this branch).
- Draft branch: solr-11577-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: with two shards and one replica each, restarting a node and then indexing and committing through CloudSolrClient can fail the next query, because CloudSolrClient retries on a stale cluster state (SOLR-11484) but HttpShardHandler does not; when no live replica is found for a shard the shard handler throws "no servers hosting shard". The reporter describes the steps in the ticket (restart node2, index, commit, assert). No comments.

## Code reading

- solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java:194-201 (getURLs): the URL list for a shard is looked up in shardToURLs and built once with httpShardHandlerFactory.buildURLList(shard) when first needed; the map is per handler (per request), so a list computed before a replica comes back is not refreshed during the request.
- HttpShardHandler.java:245-257 (submit): if urls is empty, the shard response is recorded with SolrException SERVICE_UNAVAILABLE "no servers hosting shard: <shard>" and the method returns. No refresh of the cluster state and no retry is attempted on this path.
- The grep at the pin for a forced refresh or retry in HttpShardHandler.java (forceUpdate, retry, refresh) returns no hits.

So the behavior the ticket describes is present at the pin: a shard with no URLs at submit time fails the request without a cluster-state refresh or a retry in the shard handler.

## History

- No commit at the pin names SOLR-11577 (git log --grep at the pin: none).
- SOLR-11484 (the CloudSolrClient retry) was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a two-shard, two-node collection at the pin, a node restart, indexing and commit through CloudSolrClient, and a query immediately after, recording whether the query fails with "no servers hosting shard". Not built or run in this pass.

## Gap analysis

The code matches the ticket's mechanism: no refresh or retry on an empty URL list. Whether the failure window is hit in practice depends on the cluster-state timing during restart, which a run would show.

## Verdict justification

keep-open: the mechanism is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: a restart-and-query run at the pin recording whether "no servers hosting shard" occurs; zero occurrences over repeated runs would support close-cannot-reproduce.

## Limits

This record does not run the restart scenario, does not trace SOLR-11484, and does not post anything to JIRA or GitHub.
