# solr-12007-submit

- Branch: origin/solr-12007-submit (the PR branch; ci/12007-cleanupclose has the same solr/ code plus a fork-only workflow commit)
- Head: 500a61012db
- Base: upstream/main at 9d7cc2884e8 (merge-base 14c7aac0d15, 45 commits behind, 6 ahead)
- Scope: SolrCore.java, SolrCoreCleanupOnCloseTest.java (new), changelog fragment. No fork-only files.
- Verdict: Needs work
- Reviewer: spot-check, 2026-10-07

## Findings (ranked)

- **MEDIUM (verified, design decision):** On the close path, old index directory cleanup now runs on the closing thread instead of a daemon thread. Core close and unload therefore wait for directory deletion, which can be slow on large or network-backed indexes. The changelog does not mention this. The owner needs to decide whether that is acceptable.
- **MEDIUM (verified):** SolrCoreCleanupOnCloseTest asserts only the name of the thread that called cleanupOldIndexDirectories. It does not assert that old directories are removed before the factory closes, which is the race the changelog describes. The test would pass whether or not the race exists, and no fail-before run is shown.
- **MEDIUM (hypothesis):** The code comment says the DirectoryFactory is closed "further down in this method". That ordering claim makes the synchronous call necessary, so confirm it in SolrCore.close() before accepting the change.
- **Housekeeping:** 45 commits behind upstream/main. Rebase before PR.

## Not checked

Nothing was compiled or run. Did not confirm the close ordering against the code.
