# Skip record: SOLR-17665

- Ticket: SOLR-17665, "Perf regression: overhead of multiThreaded=false should be nothing". JIRA status Open, resolution none, created 2025-02-11, last updated 2025-02-14, checked 2026-10-19 UTC. Links: SOLR-13350 (closed, introduced multiThreaded), SOLR-17642 (resolved).
- Verdict: keep-open.
- Draft branch: solr-17665-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

A performance report: with `multiThreaded` defaulting to false, the overhead still shows when `indexSearcherExecutorThreads` is not -1, and only setting it to -1 removes the regression. The five comments (all 2025-02-11 and 2025-02-14, dsmiley, kotman12, houston) give the mechanism as they understood it at the time: once `indexSearcherExecutorThreads > 0`, the core creates an executor and passes it to Lucene's `IndexSearcher`, so Lucene runs slices on it even when Solr's `multiThreaded` flag is off. They also propose removing the inheritance of Lucene's `IndexSearcher` from `SolrIndexSearcher`. No test or benchmark is named.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:242-263` (`initCollectorExecutor`): `indexSearcherExecutorThreads == 0` returns null; a positive or negative value creates a bounded `MDCAwareThreadPoolExecutor`.
- `SolrIndexSearcher.java:394` and `:832`: the executor is passed to the `IndexSearcher` super constructor, so it is always set when the node config sets threads.
- `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:379`: `multiThreaded` defaults to false.
- `solr/core/src/java/org/apache/solr/search/MultiThreadedSearcher.java:120-127` (`allowMT`): the Solr multi-threaded collector path runs only when the post filter is null, segment early termination is off, `cmd.getMultiThreaded()` is true, and the executor is non-null.
- The non-threaded paths in `SolrIndexSearcher` (`getDocListNC`, `getDocListAndSetNC`, the filtered count at about `:2164`) call `search(query, collector)` through `buildAndRunCollectorChain` (`:280-310`, the call at `:309`).
- Lucene 10.4.0 bytecode (`org/apache/lucene/search/IndexSearcher.class`, from the local Gradle cache, read with `javap -c -p`): `search(Query, Collector)` loops over the leaves and calls `searchLeaf` on the calling thread. The only `TaskExecutor.invokeAll` is in the private `search(Weight, CollectorManager, C)`, the collector-manager path, which Solr reaches only through `MultiThreadedSearcher`.
- `SolrIndexSearcher.searchLeaf` (`:818-840`): without query limits it calls `super.searchLeaf`; with limits it builds a new `IndexSearcher` per leaf that reuses the executor. This adds cost only when timeouts are enabled.

So at this head, the non-threaded Collector path does not use the executor for slicing. The mechanism the comments describe (Lucene slicing on the node executor when `multiThreaded=false`) is not in the code path that a plain search takes.

## History

- `SOLR-13350` (linked, closed): introduced `multiThreaded`. The commit was not traced on the fork.
- `SOLR-17642` (linked, resolved): not traced on the fork.
- No commit on the fork names SOLR-17665 (empty `git log --grep` at the pinned head).

## Reproduction attempts

Not attempted. The ticket reports a timing difference and names no benchmark, query, index size, or thread count. A reproduction would need a fixed index, a fixed query set, `multiThreaded=false`, and two runs of the same node: `indexSearcherExecutorThreads=0` and `indexSearcherExecutorThreads=-1`, with the median and p99 latency of each. That run is the missing piece; it was not run in this closeout.

## Gap analysis

The code path argument says the executor does not run slices on the non-threaded Collector path. It does not say that there is no overhead: an executor object exists and the `searchLeaf` override and other per-query setup could still cost time. The regression as reported is therefore not explained by the mechanism in the comments, and not disproved either. Only the benchmark separates these.

## Verdict justification

keep-open: the attempts (the code path reads at the pinned head, the Lucene bytecode check, the `searchLeaf` override reading) are named, and the missing piece is a paired benchmark with `indexSearcherExecutorThreads` at 0 and -1 under `multiThreaded=false`. None of the close verdicts fits: the regression is not shown to be fixed, obsolete, or non-reproducible. pr-needed does not fit: the code path does not show the cost the ticket describes. The keep-open minimum is met.

## Limits

This record does not benchmark anything. It does not read the SOLR-13350 or SOLR-17642 commits. It does not check the Lucene behavior of other `IndexSearcher` methods (for example `count` or `searchAfter`) that Solr may call on the same path, beyond the `search(Query, Collector)` path that the non-threaded code uses.
