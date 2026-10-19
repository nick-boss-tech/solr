We checked Solr main at commit 498a09aea9 (October 7, 2026) and believe this is fixed by commit 1177796e32 (SOLR-17118, "Avoid deadlock/hang", from May 2024).

What we checked:

- The request path that waited for the core container to start. Before SOLR-17118, it waited on a latch with no timeout and then returned the error in the report. That wait is gone at the commit we checked.
- The remaining message, "CoreContainer is either not initialized or shutting down", now appears only in the node health endpoint, not in request handling.

How we tried to reproduce it:

- We did not run a reproduction for this check. The conclusion comes from the code history.

What this means:

- The silent wait described in the report comes from the code SOLR-17118 removed, so this should no longer happen on current main.

If you still see this:

- Please send the Solr version, the release you are on, and the logs from a stalled period. We can then check whether the change reached that release.
