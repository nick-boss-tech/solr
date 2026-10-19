We checked this against Solr main at commit 498a09aea9. We did not reproduce a timing difference, and the mechanism described in the comments does not apply to the non-threaded search path, so the ticket stays open pending a benchmark.

What we checked:

- The node's search executor is created when indexSearcherExecutorThreads is greater than zero, and it is passed to Lucene's IndexSearcher.
- The non-threaded search path calls the plain collector search in Lucene. That call runs each segment on the calling thread. The executor is used only by the collector-manager path, which Solr reaches only when multiThreaded is true and the other conditions are met.
- The per-segment wrapper that Solr adds to Lucene's search creates an extra searcher only when query timeouts are enabled.

What this means: at this commit, the non-threaded path does not use the executor for slicing, so the mechanism in the comments does not explain the regression. The regression itself is not confirmed or ruled out. A benchmark with indexSearcherExecutorThreads set to 0 and to -1, with multiThreaded=false, would settle it.

If you still see this: please send the index size, the query set, the Solr version, and the median and p99 latency with each setting of indexSearcherExecutorThreads. That would let us run the same comparison on main.
