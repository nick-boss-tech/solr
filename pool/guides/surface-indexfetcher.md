# Surface dossier: IndexFetcher and the replication handler

Pinned revision: `498a09aea97aac86c2cc7ac114113b946d249942` (fork origin/main). Every caller below was found by `git grep -l IndexFetcher 498a09aea97aac86c2cc7ac114113b946d249942 -- solr` in this pass. Written by vm2, 2026-10-20T10:43:37Z (UTC). Confirmation review by a second host is pending (the assignment requires it before DONE).

## 1. What the surface is and what depends on it

`IndexFetcher` (`solr/core/src/java/org/apache/solr/handler/IndexFetcher.java`) is the follower side of index replication: it asks a leader's replication handler for the file list of the leader's latest commit, downloads the files that differ, moves them into the index directory, reloads or reopens the searcher, and updates `index.properties` (`INDEX_PROPERTIES`, line 141). Entry points: `IndexFetcher(...)` constructor (line 261), `fetchLatestIndexOnce` (line 418, the main path), `getLatestVersion` (line 352, used by health checks).

## 2. Callers and consumers (grep at 498a09aea97aac86c2cc7ac114113b946d249942)

Production:
- `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java`: owns the polling `pollingIndexFetcher` (line 218), uses `IndexFetchResult` (line 99), and documents the file-fetch protocol that `IndexFetcher.FileFetcher` reads (line 336-338).
- `solr/core/src/java/org/apache/solr/cloud/ReplicateFromLeader.java`: checks `IndexFetcher.IndexFetchResult.INDEX_FETCH_SUCCESS` (line 102) to decide whether a recovering replica can go active.
- `solr/core/src/java/org/apache/solr/core/SolrCore.java`: reads `INDEX_PROPERTIES` (line 444) and renames a temporary properties file over it (lines 1474-1476). This is the other writer of `index.properties`.
- `solr/core/src/java/org/apache/solr/handler/RestoreCore.java`: uses `IndexFetcher.CompareResult`, `compareFile` and `filesToAlwaysDownloadIfNoChecksums` (lines 157-167) to decide what a restore downloads.
- `solr/core/src/java/org/apache/solr/handler/admin/api/NodeHealth.java`: builds an `IndexFetcher` directly (line 202) to read the leader's latest version.
- `solr/core/src/java/org/apache/solr/update/SolrIndexSplitter.java`: deletes `index.properties` (line 455) during a split.
- `solr/core/src/test/...`: `TestPullReplica`, `IndexFetcherPacketProtocolTest`, `ReplicationHandlerRecoveryAuthIntegrationTest`, `TestReplicationHandler` (named by path, not reread here).
- `solr/core/src/java/org/apache/solr/util/TestInjection.java`: a test-injection hook name that the fetcher checks.
- Outside Java: `solr/server/etc/security.policy` names the package for the policy.

## 3. Invariants and contracts

- Full copy versus differential copy. `fetchLatestIndexOnce` sets `isFullCopyNeeded` when `isIndexStale(indexDir)` finds a same-named file with a different size or checksum (`IndexFetcher.java:617-618`, `isIndexStale` at line 1306). A differential copy (`doDifferentialCopy`, lines 1132-1164) then reuses matching files and downloads only the rest. Pinning evidence: the SOLR-11920 commits `fbdf7fa736` and `72ee0f64d4` are ancestors of this revision.
- Two directories. The fetch writes to a temporary index directory (`tmpIndexDir`, lines around 596-605) and moves files into the real index directory (`moveIndexFiles`, line 1350) once the copy is complete. The temporary directory uses the same directory factory and lock type as the index.
- Stop and abort. A volatile `stop` flag (line 173) and `abortFetch()` (line 1483) stop a running fetch; `fsyncService` is torn down by `terminateAndWaitFsyncService` (line 888).
- Searcher and commit point. `openNewSearcherAndUpdateCommitPoint` (line 1014) and `reloadCore` (line 1052) run after the files are in place. `skipCommitOnLeaderVersionZero` (line 185) controls one leader-version edge case.
- Shutdown. A fetch must not open a new searcher while the `CoreContainer` is shutting down: commit `9e15873bb5` (2026-08-25) adds that guard. Any change to searcher open must keep it.

## 4. Live work

Fork branches whose names point at replication, backup or restore and whose history touches `IndexFetcher` (names from `git ls-remote`, not verified commit by commit here): `ci/11650-replication-auth`, `ci/11650-fetcher-redact-r35`, `ci/15003-replication`, `ci/15863-backup`, `ci/15863-backupcmd-r36`, `ci/15863-backupcoreapi-r36`, `ci/15863-incbackup-tc`, `ci/16725-localfsbackup`, `ci/16725-localfsbackup-r35`, `ci/17287-restorecore`, `ci/17287-restorecore-fix`, `ci/9598-restoreapi`, `ci/9865-restore`, `solr-11815-draft-close` and `solr-11815-draft-open` (the latter two are skip-record drafts; no code change). Receipts that name `IndexFetcher`: `receipts/SOLR-11650.md`, `receipts/SOLR-12085.md`, `receipts/SOLR-12246.md`, `receipts/SOLR-15003.md`, `receipts/SOLR-6711.md`.

## 5. Recent history

- `0cc72b8e32` (2026-09-07) SOLR-18406: restart replication after the index generation expires. Its test is `TestReplicationHandler.java` (commit `0cc72b8e32` is the newest change to that file in the path log).
- `9e15873bb5` (2026-08-25): skip opening a searcher when the `CoreContainer` is shutting down.
- `640428af23` (2026-01-29) SOLR-18083: fix replication and other operations in readOnly mode (an earlier revert, `b314f8f5d3`, is in history).
- `6b21201fcc` (2026-04-10) SOLR-18121: remove the rm-in-advance fetcher codepath.
- `a364ac71b8` (2026-03-24) SOLR-18080: leader election for shard terms, which changes the leader side that the fetcher talks to.
- `fbdf7fa736` (2018-03-15) SOLR-11920: differential file copy; `72ee0f64d4` (2018-04-17) its follow-up file-exists check.

Review requests on these changes are not located in this pass; the receipts above are where they would be recorded.

## 6. Danger spots

- `isIndexStale` (line 1306) decides whether a full copy is needed. Removing the check looks safe because the differential copy already reuses files, but the flag also controls the reload path and the log text; the SOLR-11815 record (`solr-11815-draft-close`) keeps the flag as a stated limit for this reason.
- Searcher open during shutdown. `9e15873bb5` is a guard that is easy to lose when a refactor moves the searcher call. Any change that opens a searcher from inside the fetch must keep that guard.
- Temporary directory and index directory. The temporary directory is the same factory and lock type. Changing the lock type for one of them alone can break the move step, because the move assumes the same underlying directory layer.
- `index.properties` has two writers: `IndexFetcher` and `SolrCore` (rename at `SolrCore.java:1474-1476`). A change to the file name or format must update both, and `SolrIndexSplitter` deletes it during a split (line 455).

## Limits

- Callers are grep-verified at the pinned revision. Consumers reached only through reflection or configuration are not.
- The live-branch list is by name. A branch can touch the fetcher without its name saying so.
