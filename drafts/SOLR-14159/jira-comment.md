We checked this against Solr main at commit 498a09aea9. The test still exists. We have not reproduced the failures on current main, so the ticket stays open for now.

What we checked:

- The test-side changes made in January 2020 (the spin-loop removal in TestCloudConsistency and TestTlogReplayVsRecovery) are on main.
- No production change in the history is tied to the connection-refused failure, and the failure texts in the ticket have not been reproduced on main.
- The last report on this ticket is from April 2020.

What this means: the failures may no longer happen, but that has not been checked on current code. A run of the test over several seeds on main would settle it.

If you still see these failures: please send the seed, the Solr version, and the full failure text (the "Server refused connection" or "Timeout waiting for active collection" message with its stack), so we can run the same seed on main.
