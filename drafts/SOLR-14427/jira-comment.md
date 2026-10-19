We checked this against Solr main at commit 498a09aea97 and could not confirm the error yet.

What we checked:

- The update processor skips a document with a version conflict when the request sets failOnVersionConflicts to false, both for an existing document and for a document that is not found. The leader side of that check is in place.

How we tried to reproduce it:

- Not run. The error needs a cluster with several shards and the two updates in one request.

What this means:

- We have not yet checked whether the flag is passed on to the other shards during a distributed update, which is where the error in the report comes from.

If you still see this:

- Please send the Solr version, the update request, and the full error with its stack trace. Please reopen with that information.
