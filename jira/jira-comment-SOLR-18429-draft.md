# Jira comment draft: SOLR-18429

Status: DRAFT (not posted). Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-v2-read.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I checked the replica endpoints at apache/solr main {{e59ce3c97d}}. There is a POST to create a replica ([CreateReplicaApi.java:32|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/CreateReplicaApi.java#L32]) and a DELETE on {{/shards/\{shardName\}/replicas/\{replicaName\}}} ([DeleteReplicaApi.java:45-46|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/DeleteReplicaApi.java#L45-L46]), and no GET on a single replica.

The v1 CLUSTERSTATUS action can already return PRS per-replica state on its {{prs}} flag ([ClusterStatus.java:427|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/core/src/java/org/apache/solr/handler/admin/ClusterStatus.java#L427]), which is the piece the ticket proposes to include.

Eric, should the single-replica GET include the PRS entry only when the collection has PRS enabled, as the ticket proposes, or should it always show it?

Possible states: implement the GET with PRS included when enabled; implement it without PRS and add PRS later; or hold for SOLR-18426, since the replica list shape depends on it. The recommendation is the first, matching the v1 {{prs}} behaviour.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
