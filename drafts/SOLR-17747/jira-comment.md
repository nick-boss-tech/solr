We checked this against Solr main at commit 498a09aea9. We could not reproduce the problem yet, so the ticket stays open.

What we checked:

- An optimize commit calls the index writer's forced merge with the requested segment count. Without a maxSegments parameter, it uses the merge policy's default and respects maxMergedSegmentMB.
- A small test with Lucene 10.4.0 and TieredMergePolicy merged ten one-document segments down to one, with both a 5000 MB and a 100000 MB segment limit. That test did not use vector fields and was not run through Solr.

What this means: the reported behavior is not explained by the default path for small segments. It may depend on the request parameters, on segments at or above the size limit, or on the HNSW vector fields, and the report does not say which.

If you still see this: please send the optimize request you used (including any maxSegments value), the document count, the segment count and sizes before and after the optimize, and the Solr log for the commit. With those, we can reproduce the case on main.
