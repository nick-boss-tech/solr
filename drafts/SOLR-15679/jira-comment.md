We checked this against Solr main at commit 498a09aea97 and could not identify the cause yet.

What we checked:

- The error means a core was released one more time than it was acquired. The check is in SolrCore's close method, which logs this error when the reference count drops below zero.

How we tried to reproduce it:

- Not attempted. The report does not include a stack trace or the steps that lead to the error.

What this means:

- The error points to a code path that releases a core once too often. The path itself is not identified from the report.

If you still see this:

- Please send the full log around the error, including the stack trace, the Solr version, and what the node was doing (restart, reload, or core creation) at the time. Please reopen with that information.
