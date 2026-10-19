We checked this against Solr main at commit 498a09aea97 and could not close it from here.

What we checked:

- The leader election code still uses separate election context classes, and the shard leader node that a 2022 comment called redundant is still written and read by the code.
- No commit on main addresses the proposal to consolidate the election code.

How we tried to reproduce it:

- Not attempted. The ticket lists design properties, and checking each one needs a multi-node election test.

What this means:

- This is an open design question. Whether to consolidate the election code, and whether to keep the shard leader node, is a decision for the Solr developers.

If you still see this:

- If you see a specific leader-election failure (two leaders for one shard, or a replica that cannot rejoin), please send the logs around it and the Solr version, and we will check it against the property the ticket lists.
