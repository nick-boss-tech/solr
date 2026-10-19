We checked this against Solr main at commit 498a09aea97 and could not confirm the data loss yet.

What we checked:

- The shard split command looks up the parent shard's leader and records it before the split starts. What happens to updates the old leader accepted during the split, when the leader dies, has not been checked yet.
- An earlier comment on this ticket reports that most of the observed failures came from a test bug, which was corrected.

How we tried to reproduce it:

- Not run. The check needs a split with concurrent indexing and a leader kill, on a corrected version of the test.

What this means:

- It is not yet clear whether any documents are lost after the corrected test. This report stays open until that run is done.

If you still see this:

- Please send the Solr version, the split command and timing, the leader kill steps, and the document counts on the sub-shards afterwards. Please reopen with that information.
