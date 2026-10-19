# JIRA newdev small improvements, round 2 (vm3)

Assignment: `assignments/pool-jira-newdev-small-round-2.md`. One section per ticket claimed by vm3.

## SOLR-15462: dubious code in DistribPackageStore::refresh (vm3, 2026-10-19)

- Status: not applicable on apache/main at `c8ab71430f`. No branch. Claim: `claims/pool-vm3-jira-newdev-small-round-2-solr-15462.md`.
- `DistribPackageStore` is not in the source tree at base. A search of `solr/` for the class finds no source file, and the only references are changelog entries (`CHANGELOG.md` line 2372, `changelog/archive.md` line 1631), so the code the ticket describes has been removed.
- Gate: none, since there is nothing to change.

## SOLR-18182: Deprecate ByteBuffersDirectoryFactory (vm3, 2026-10-19)

- Status: fixed. Branch `solr-18182-deprecate-bytebuffers`, commit `0abcca2f4b` (tree `be235de6ba`). Claim: `claims/pool-vm3-jira-newdev-small-round-2-solr-18182.md`.
- Change: `ByteBuffersDirectoryFactory` is `@Deprecated(since = "11.0")` with a javadoc pointer to `RAMDirectoryFactory`. Code reading confirms the ticket's point: `RAMDirectoryFactory.create` builds the same `ByteBuffersDirectory`, and the lock handling matches. Changelog entry `changelog/unreleased/SOLR-18182-deprecate-bytebuffers-directory-factory.yml` (type `deprecated`).
- Test: `ByteBuffersDirectoryFactoryTest.testDeprecated` is a pin: it checks the annotation is present, and it does not show a behavior change. The behavior is unchanged, and the existing tests in the class cover it.
- Gate, on `0abcca2f4b`'s tree: changelog validation 0, tidy 0 (no changes), Error Prone compile 0, focused `ByteBuffersDirectoryFactoryTest` 5 of 5 with 0 failures (seed 18182E50), `:solr:core:check -x test` 0. Logs: `/workspace/gates/logs/vm3/solr-18182-gate/`.
- Not done: no reference-guide note. Maintainers may want one on the deprecation page.
