# Jira comment draft: SOLR-18428

Status: DRAFT (not posted). Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-v2-read.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I checked the shard endpoints at apache/solr main {{e59ce3c97d}}. The v2 API has POST {{/collections/\{collectionName\}/shards}} ({{CreateShardApi.java:27}}) and DELETE {{/collections/\{collectionName\}/shards/\{shardName\}}} ({{DeleteShardApi.java:29}}), and no GET on a single shard. A GET is a read path only, so it does not conflict with either.

The data is already there. The v1 CLUSTERSTATUS action takes a {{shard}} parameter and returns that one shard, or "shard not found" ({{ClusterStatus.java:232-233}} and {{:307-316}}). A v2 GET could wrap that path.

Eric, does the response shape in the ticket (the list-view summary plus the replica list) still stand, given that SOLR-18426 (List Shards) had its PR closed without merge?

Possible states: implement the GET on the existing v1 path with the ticket's shape; hold until the list shape is settled under SOLR-18426; or close in favour of a single "shard read" ticket. The recommendation is the first: the read is small and the data path exists.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
