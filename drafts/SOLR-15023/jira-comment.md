We checked this against Solr main at commit 498a09aea97 and could not confirm the cause yet.

What we checked:

- The proxy named in the report (AdminHandlerProxy) is no longer in Solr main.
- The metrics endpoint that the Admin UI calls still exists, and we have not yet timed it on a cluster with about 200 collections.

How we tried to reproduce it:

- Not attempted. The check needs a cluster of that size and a timed call to the metrics endpoint.

What this means:

- It is not yet clear whether the slow call is still present on current Solr, or whether it was specific to the removed proxy.

If you still see this:

- Please send the Solr version, the number of collections and nodes, and the timings of a request to the metrics endpoint with its parameters. Please reopen with that information.
