# JIRA newdev small round 2

Assignment: `assignments/pool-jira-newdev-small-round-2.md`. Base: apache/main c8ab71430f.

## Item 5: SOLR-15462 (vm4), stopped, 2026-10-19T21:04:52Z

- Verified: JIRA status Open, no resolution, on 2026-10-19.
- Stopped, no branch. The class is now `DistribFileStore`, and the dubious code is still there at apache/main c8ab71430f: `solr/core/src/java/org/apache/solr/filestore/DistribFileStore.java`, `refresh` (lines 538 to 555). `myFiles` is a `List<FileDetails>`, so `myFiles.contains(f)` with a `String` is always false, and every ZK child is re-fetched on each refresh.
- The obvious fix matches on `FileDetails.getSimpleName()`. That would stop re-downloading a file that already exists locally. The store keeps `MetaData` (with a hash) for each file, and a name-only match would not notice a changed file under the same name. Whether the check is by name alone or by name plus the stored hash, and what to do with an updated file, is a design decision. The assignment says to stop when a design decision is needed.

