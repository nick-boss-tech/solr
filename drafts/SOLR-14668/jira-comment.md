We checked this against Solr main at commit 498a09aea97 and could not close it from here.

What we checked:

- Solr routes a document by the prefix of its composite ID. When the prefix changes, the new version goes to a different shard, and nothing removes the old copy from the original shard.
- No cleanup for changed composite IDs exists in the current code, and no commit names this ticket.

How we tried to reproduce it:

- Not attempted. The duplicate follows from the routing design, and a test would only confirm that.

What this means:

- Whether Solr should provide a cleanup for this case, or the documentation should describe an approach such as a copy field with delete-by-query, is a decision for the Solr developers.

If you still see this:

- If you have a concrete setup where a changed composite ID leaves a duplicate, please send the collection's router settings, the two IDs, and the shard each copy is on. Please reopen with that information.
