We checked this against Solr main at commit 498a09aea9 and have not found a file handle leak in the index fingerprint path. We have not measured it yet, so the ticket stays open.

What we checked:

- The fingerprint runs one task per index segment on the core container's fingerprint thread pool (SolrIndexSearcher.getIndexFingerprint, CoreContainer).
- Each per-segment fingerprint reads a segment reader that the searcher already has open. The fingerprint code does not open files itself.
- The "Too many open files" failure in SolrIndexFingerprintTest comes from a test with about 500 segments under a low file handle limit. The count reflects the segment files the searcher holds open.

What this means: the number of open files should follow the number of segments, not the number of fingerprint calls. A measurement is still needed to confirm it.

If you still see this: please send the segment count of the core, the number of open files before and after repeated fingerprint calls (for example from /proc/<pid>/fd), and the Solr version. That would confirm or rule out a leak.
