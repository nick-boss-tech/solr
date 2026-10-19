We checked Solr main at commit 498a09aea9 (October 7, 2026). We have not reproduced the null pointer exception, so the ticket stays open.

What we checked:

- The CoreAdmin status code for a loaded core. The one value on that path that could be null is the last-published state of the core's cloud descriptor. We have not confirmed whether it can be null on any path.
- The stack trace in the ticket comes from an earlier layout of the CoreAdmin code, so it does not map directly to the current code.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- The failure may be gone, or may depend on a state we have not reproduced. We would need steps that reproduce it on the current code.

If you still see this on a current release:

- Please send the steps that leave the index empty or corrupted, the Solr version, and the full stack trace from the failing status call. With those, we can run the same steps on main.
