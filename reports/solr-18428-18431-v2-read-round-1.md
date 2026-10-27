# SOLR-18428 to SOLR-18431: v2 read endpoints for shards, replicas and replica properties

Tickets (all Open, Major, reporter Eric Pugh, created 2026-09-08, no comments): SOLR-18428 Fetch Single Shard; SOLR-18429 Fetch Single Replica; SOLR-18430 List Replica Props; SOLR-18431 Fetch Replica Prop. Each proposes a GET path and a response shape. SOLR-18426 and SOLR-18427 (List Shards, List Replicas) had PRs #4911 and #4912, both closed without merge on 2026-10-21; the reason is not on the board.
Pinned main: `e59ce3c97ddb`. Code read only; no build or run.

## What exists at the pinned SHA

Write and delete paths exist. No read path exists under these resources.

| Resource | Existing v2 method and path | Source |
|---|---|---|
| Shard | POST `/collections/{collectionName}/shards` | `solr/api/src/java/org/apache/solr/client/api/endpoint/CreateShardApi.java:27` |
| Shard | DELETE `/collections/{collectionName}/shards/{shardName}` | `DeleteShardApi.java:29` |
| Replica | POST `/collections/{collectionName}/shards/{shardName}/replicas` | `CreateReplicaApi.java:32` |
| Replica | DELETE `/shards/{shardName}/replicas/{replicaName}` | `DeleteReplicaApi.java:45-46` |
| Replica property | PUT `.../replicas/{replicaName}/properties/{propName}` | `AddReplicaPropertyApi.java:29` |
| Replica property | DELETE `.../replicas/{replicaName}/properties/{propName}` | `DeleteReplicaPropertyApi.java:32` |

A GET on `/collections/{collectionName}` (`CollectionStatusApi.java:33`) returns collection metadata, but it takes no shard or replica path segment.

## Data path that already exists

- v1 CLUSTERSTATUS can return one shard. `ClusterStatus.java:232-233` reads the `shard` parameter, and `:307-316` returns the selected shard or "Collection: X shard: Y not found". The v2 single-shard read could wrap this path, which is the ticket's proposed shape.
- v1 CLUSTERSTATUS can include PRS, on the `prs` flag (`ClusterStatus.java:427`), which the replica ticket (SOLR-18429) proposes to mirror.
- Replica properties are stored in the replica's state as `property.<name>` keys (`solrj/src/java/org/apache/solr/common/cloud/Replica.java:357-358`, `:451`). AddReplicaProperty writes them through the Overseer message at `AddReplicaProperty.java:92-95`. No code reads them back in a v2 GET.

## Verdicts, by ticket

- SOLR-18428: gap confirmed. A v2 GET can reuse the existing single-shard status path. The proposed shape matches the list shape from SOLR-18426, which has no merged implementation.
- SOLR-18429: gap confirmed. The read needs the replica entry from cluster state. Whether the PRS entry is included is the open point in the ticket's proposal, and v1 has an existing `prs` flag to match.
- SOLR-18430: gap confirmed. Reads the `property.*` keys of one replica and returns them as a flat map.
- SOLR-18431: gap confirmed. Same source as SOLR-18430, narrowed to one property. Open point: the response for a missing property (404 or empty value). The ticket does not say.

No code change is made here. All four are implementation-size tickets that need an owner decision on shape, not a comment-only fix. The drafts ask Eric the shape question for each.

## Not checked

- Whether `ClusterStatus` already includes `property.*` keys in the replica map it returns. The code path was not traced to the output.
- Whether the closed PRs #4911 and #4912 were closed for a reason that applies to these tickets.
