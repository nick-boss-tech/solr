# JIRA draft: cross-shard order of a leading _docid_ sort depends on the replica list (not filed)

Status: DRAFT. Not filed. Ticket number is a placeholder (SOLR-TBD) until Nick files it.

Summary: Distributed sort on _docid_ orders documents from different shards by replica list, so paging with start and rows can repeat or skip documents

Relates to: SOLR-6975 (PR #5135). The fix stacks on that branch, so this ticket should follow #5135.

Description:

With a leading `_docid_` sort, the merge ranks documents from different shards by `ShardDoc.shard`. For a shard, that value is the list of its replica URLs joined with `|`, in the order the request built it. The replica list is shuffled for each request, so the first URL decides the order between shards. The clusters start with random ports, so the first URLs of the two shards can sort either way. When the shard addresses interleave as strings, the shard order flips between requests, and paging repeats some documents and skips others.

Steps to reproduce (SolrCloud, collection with 2 shards and 2 replicas each, 40 documents with unique id):
1. Query `q=*:*&fl=id&sort=_docid_ asc&rows=7` with start 0, 7, 14, 21, 28 and 35.
2. Collect the ids and count the distinct ones. Expected 40.

Observed (vm4, probe on SOLR-6975 head 6c033ba6ea): in clusters where the shard addresses interleave, 8 of 9 page sets returned fewer than 40 distinct ids (26 to 38). In clusters where they do not interleave, all 9 page sets returned 40. Replicas agreed on every _docid_ in every cluster checked, so divergent replicas are not the cause.

Pinning each shard to its leader with shards.preference=replica.leader:true returned 40 distinct ids in every page set.

Proposed fix (PR, to be opened): rank cross-shard documents by slice name, taken from rb.slices, instead of the replica list. Routing still uses ShardDoc.shard.

Expected result after the fix: the order between shards no longer depends on which replica answers. Stable paging also needs each replica to keep the same _docid_ values, which the change does not check.

Logs: /workspace/gates/logs/vm4/solr-5135-paging-probe/ (pre-fix clusters), /workspace/gates/logs/vm4/solr-slice-order/ (fix).
