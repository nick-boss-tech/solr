We checked this against Solr main at commit 498a09aea97 and could not identify the cause from the report.

What we checked:

- The message in the report comes from the replica recovery code, where a replica copies its index from the leader when its peer sync fails. The copy step fails with that message when the leader's replication request does not succeed.

How we tried to reproduce it:

- Not attempted. The report does not include the logs from the leader at the time of the failure.

What this means:

- The failing step is identified, but the reason the leader's replication request fails is not in the report.

If you still see this:

- Please send the Solr version, the leader's log around the failure, and the replica states before and after a restart. Please reopen with that information.
