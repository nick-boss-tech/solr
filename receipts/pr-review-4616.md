# Receipt: PR review apache/solr #4616 (vm4)

- PR: https://github.com/apache/solr/pull/4616, open, not draft, head branch `multi-threaded-search-performance` on `arup-chauhan/solr`, 1 file, +20/-3 (GitHub files API), updated 2026-09-08, `mergeable_state` unknown at the snapshot.
- File: `solr/benchmark/src/java/org/apache/solr/bench/search/ExitableDirectoryReaderSearch.java`. Adds `@Param({"false","true"}) boolean multiThreaded = false`, and sets `CommonParams.MULTI_THREADED` in `createInitialParams(BenchState)`.
- Base check at `c8ab71430f`: `CommonParams.MULTI_THREADED = "multiThreaded"` exists (line 160). `createInitialParams` is defined in the same file at line 154.
- Formatting: the new `CommonParams` import sits between `client.solrj.request` imports, so the order is wrong for spotless.
- Gate: none run.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/4616.md`.
