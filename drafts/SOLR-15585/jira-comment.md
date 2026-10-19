We checked this against Solr main at commit 498a09aea97 and could not confirm the data loss yet.

What we checked:

- A PULL replica polls its leader for new segments, and the interval comes from the commit settings (three seconds by default). Until the next poll, a PULL replica can lag behind a leader that has committed.
- The comments on this ticket describe a case where all TLOG replicas go offline, so that a PULL replica has nothing to fetch from.

How we tried to reproduce it:

- Not attempted. The loss needs a cluster with TLOG and PULL replicas, a clean shutdown of the leader, and a count of the documents afterwards.

What this means:

- Whether a clean shutdown should wait for PULL replicas to catch up is a design question. We have not yet checked what the shutdown path does.

If you still see this:

- Please send the replica types and counts, the commit and poll settings, the order of the shutdowns, and the document counts on each replica afterwards. Please reopen with that information.
