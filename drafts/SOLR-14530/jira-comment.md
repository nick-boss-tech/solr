We checked this against Solr main at commit 498a09aea97 and found that the shard handler interface is still in use in many places.

What we checked:

- The shard handler interface and its two implementations are present on main, and about fifty source files use the interface.

How we tried to reproduce it:

- Not applicable. This report asks for a refactor, not a fix for a failing behaviour.

What this means:

- The refactor is open. Whether to pursue it, and which uses to change first, is a decision for the Solr developers.

If you still see this:

- If you have a specific place where the interface makes a simple request hard to follow, please point to it and we will look at a change there. Please reopen with that information.
