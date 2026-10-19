We checked Solr main at commit 498a09aea9 (October 7, 2026). The code that raises this error is still there: when a shard response cannot be read and partial results are not allowed, the error is re-thrown with the original exception as its cause. The ticket's log does not include that cause, so we cannot tell from it which read failed.

What we checked:

- The shard response helper that raises the error, and the condition under which it is raised.
- The full text of this ticket, which has no exception cause and no comments.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- The error text alone does not show the failing read. The original exception is needed.

If you still see this:

- Please send the "Caused by" lines from one of the errors in the burst (the full stack trace from the server log), along with the Solr version and whether the request used the shards.tolerant option. With those, we can check which shard response failed.
