We checked this against Solr main at commit 498a09aea9. We have not reproduced the null-pointer exception yet, so the ticket stays open.

What we checked:

- The CoreAdmin status call for a loaded core reads the core's cloud state and solr config. We did not find a path in the current code where those values are null for a loaded core.
- The stack trace in the ticket comes from an earlier layout of the CoreAdmin code, so it does not map directly to the current code.

What this means: the failure may be gone, or may depend on a state we have not reproduced. We would need steps that reproduce it.

If you still see this on a current release: please send the steps that make the index empty or corrupted, the Solr version, and the full stack trace from the current code (the log line for the failing STATUS call). With those, we can run the same steps on main.
