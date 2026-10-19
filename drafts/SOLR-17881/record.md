# Skip record: SOLR-17881

- Ticket: SOLR-17881, "Is SolrColr::getIndexFingerprint leaking file handles". JIRA status Open, resolution none, last updated 2025-08-26, checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-17881-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream cited: apache/solr PR #3477 (SOLR-17863, merged 2025-08-21, merge commit 4659f9b1f0 per the PR page).
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The ticket asks whether the index fingerprint leaks file handles. It has no description beyond a link to a comment on PR #3477, no JIRA comments, no linked issues, and no named test class. The method name in the title (SolrColr) does not exist in the code; the reading here takes it to mean the index fingerprint path (SolrIndexSearcher, SolrCore, IndexFingerprint).

The linked PR comment (kotman12, 2025-08-18 and 2025-08-25) raises the question for a 500-segment test in SolrIndexFingerprintTest. A reply on 2025-08-25 reports a "Too many open files" failure from that test under a constrained JVM (ActiveProcessorCount=1 and a handle-limit file system). The commenter notes that the fingerprint should need only about one handle per parallel task, and calls the result suspicious without confirming a leak.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:2311-2324`: `getIndexFingerprint(long maxVersion)` builds one Callable per leaf of the top reader context and submits them to `CoreContainer.getIndexFingerprintExecutor()`, then reduces the results.
- `solr/core/src/java/org/apache/solr/core/CoreContainer.java:450-454`: the executor is `ExecutorUtil.newMDCAwareCachedThreadPool(EXECUTOR_MAX_CPU_THREADS, Integer.MAX_VALUE, ...)`. The thread count is not capped by the number of leaves, so concurrency equals the leaf count at most.
- `solr/core/src/java/org/apache/solr/core/SolrCore.java:2141-2178`: per-segment fingerprints are read from and put into `perSegmentFingerprintCache`; a miss calls `IndexFingerprint.getFingerprint(searcher, ctx, maxVersion)`.
- `solr/core/src/java/org/apache/solr/update/IndexFingerprint.java:111-124`: `getFingerprint(searcher, ctx, maxVersion)` reads `ctx.reader()` (maxDoc, numDocs, liveDocs, and the leaf's indexed values). The file has no `Directory` or `IndexInput` use and opens no file itself (grep of `Directory`, `IndexInput`, `open` in that file returns no open call in this method).

## History

- `2041d1d0e6` (2025-05-21), SOLR-17756: parallelize index fingerprint across segments (#3352). Ancestor of the pinned head: yes. This is the change that introduced the per-leaf fan-out above.
- SOLR-17863 / PR #3477 (merged 2025-08-21, merge commit 4659f9b1f0 per the PR page): fixes a race in the per-segment cache. It does not change file handling. Ancestor of the pinned head: not checked on the fork; the ticket does not depend on it.
- `git log` of the fork names SOLR-17881 in no commit (grep of `origin/main` log, empty).

## Reproduction attempts

Not attempted. The ticket gives no reproduction input. The only known setup is the 500-segment test in SolrIndexFingerprintTest, and its failure is a handle-limit error from the test environment, not a measured count of handles per call. A reproduction that could close or reopen the ticket would count the process's open file descriptors (for example from `/proc/<pid>/fd`) before and after repeated `getIndexFingerprint` calls on an index with about 500 segments, at the pinned head, with a fixed seed and the same JVM options. That run is the missing piece; it was not run in this closeout.

## Gap analysis

At this head the fingerprint code does not open files. The handles the reporter saw are the segment files that the searcher's leaf readers hold open, and their count scales with the number of segments, not with the number of fingerprint calls. The "Too many open files" failure in the linked comment is consistent with that count under a low limit. What is not established is whether Lucene's readers open additional handles per access in this path (for example for doc values or stored fields used by the fingerprint). Only the measurement above settles that.

## Verdict justification

Verdict keep-open. Evidence is inconclusive: the code path is read with file and line at the pinned head, and it shows no file open in the fingerprint itself, but no measurement exists for handles per call. The keep-open minimum is met: the attempts (code reading, history, linked comment) and the missing piece (the fd measurement on a 500-segment index) are named here. A later run could move this to close-cannot-reproduce (no growth in fd count) or pr-needed (growth tied to fingerprint calls).

## Limits

This record does not establish whether the reporter's environment had a leak, and it does not establish behavior on any release other than the pinned head. It does not run tests. It does not check the upstream PR branch beyond the linked comment.
