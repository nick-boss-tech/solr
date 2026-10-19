# JIRA newdev small round 2

Assignment: `assignments/pool-jira-newdev-small-round-2.md`. Base: apache/main c8ab71430f.

## Item 5: SOLR-15462 (vm4), stopped, 2026-10-19T21:04:52Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The class is now `DistribFileStore`, and the dubious code is still there at apache/main c8ab71430f: `solr/core/src/java/org/apache/solr/filestore/DistribFileStore.java`, `refresh` (lines 538 to 555). `myFiles` is a `List<FileDetails>`, so `myFiles.contains(f)` with a `String` is always false, and every ZK child is re-fetched on each refresh.
- The obvious fix matches on `FileDetails.getSimpleName()`. That would stop re-downloading a file that already exists locally. The store keeps `MetaData` (with a hash) for each file, and a name-only match would not notice a changed file under the same name. Whether the check is by name alone or by name plus the stored hash, and what to do with an updated file, is a design decision. The assignment says to stop when a design decision is needed.

## Item 9: SOLR-18173 (vm4), stopped, 2026-10-19T22:03:48Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The ticket asks for the deprecation and for anonymous child documents to be stored under a reserved `_childDocuments_` key. Moving the storage changes how child documents are kept and read, and the ticket points to SOLR-12298 for the open design. The assignment says to stop when the change needs a design decision.
## Item 6: SOLR-17719 (vm4), fixed branch, 2026-10-19T22:27:04Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Status: fixed branch. `solr-17719-semaphore-newsearcher` on the fork: code commit bd878b38f8 (test only: `TestManagedSchemaWithMultipleAdd` waits for a newSearcher event through a `TestInjection` hook and a semaphore instead of sleeping), draft commit 732d3fbcc4 (`drafts/SOLR-17719/pr-description.md`).
- Gate: `TestManagedSchemaWithMultipleAdd` 1 of 1 pass, 0 failures, 0 skipped; spotless passes. Log `/workspace/gates/logs/vm4/solr-17719-gate.log`.
- No fail-before run: the failure is sporadic and was not reproduced on this host.
