We checked Solr main at commit 498a09aea9 (October 7, 2026). The null pointer that the request filter in this ticket hit is guarded at that commit. The error handler that formats the 500 response still assumes the request object is present, though, so a request that reaches it without that object would produce the "exception mapper failure" message.

What we checked:

- The request filter that reads the query request, and its null check.
- The exception handler and the helper it calls, which assumes the request is not null.
- Where the v2 path sets the query request object.

How we tried to reproduce it:

- We did not run a reproduction for this check. Earlier attempts in the comments on this ticket did not reproduce it.

What this means:

- The handler gap is real in the code, but we have not shown that the backup and replica requests in this report reach it. The full stack trace would show which path fails.

If you still see this:

- Please send the full stack trace from the server log for one failed backup or replica request, with the Solr version and whether the request was async.
