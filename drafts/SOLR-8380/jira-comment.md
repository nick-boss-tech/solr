We checked this against Solr main at commit 498a09aea9. The test still exists. We have not reproduced the 500 responses on current main, so the ticket stays open for now.

What we checked:

- The only commit on main that names this ticket changes an assert message in the cluster controller. It does not change behavior.
- The request buffer size change suggested in the comments is not on main.
- The generic "Server Error" message is the subject of SOLR-8469, which is also open.

What this means: the failures may no longer happen, but that has not been checked on current code. A run of the test over several seeds on main would settle it.

If you still see these failures: please send the seed, the Solr version, and the server log for the request that returned the 500, so we can run the same seed on main.
