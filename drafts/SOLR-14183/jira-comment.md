We checked this against Solr main at commit 498a09aea97 and found that the recovery request still returns before the replica is marked as recovering.

What we checked:

- The request handler starts recovery asynchronously and does not set the replica's state itself. The state changes later, when the recovery starts. This matches the report.

How we tried to reproduce it:

- Not run. Reproducing it needs a network partition, a leader restart, and a query to a non-leader replica in the short window before it is marked as recovering.

What this means:

- The window in the report exists in the code. Whether a query in that window misses documents, and where the recovering state should be set, are open questions for the Solr developers.

If you still see this:

- Please send the replica states over time after a recovery request, the query used, and the Solr version. Please reopen with that information.
