We checked this against Solr main at commit 498a09aea97 and found that the version check for SolrJ requests still reads the collection state without the cache.

What we checked:

- The check that compares the client's collection state version with the server's (stateVer) reads the collection without using the cache, so a request can reach ZooKeeper each time. This matches the report.
- Another lookup in the same servlet already uses the cache, so the change would apply to the version check only.

How we tried to reproduce it:

- Not attempted. Measuring the number of ZooKeeper reads needs a cluster under request load.

What this means:

- Using the cache here would reduce ZooKeeper reads, but it would also allow the version check to use state up to about two seconds old. Whether that trade-off is acceptable is a decision for the Solr developers.

If you still see this:

- Please send the number of nodes and collections, the request rate with stateVer, and the ZooKeeper read counts, so we can measure the effect of the change.
