# SOLR-9865 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-9865 - "RestoreCore failing can roll an index back in time." (Mark Miller, no comments). If a core has `index.<timestamp>` directories and a restore fails, deleting `index.properties` rolls back to the default `index` directory, which may be older.
- Branch: `solr-9865-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`RestoreCore` switches the core with `core.modifyIndexProps("restore.<ts>")`. If opening the new writer/searcher fails, the rollback does `dir.deleteFile(IndexFetcher.INDEX_PROPERTIES)`. That is only correct when the core was on the default `index` directory before. If it was on `index.<ts>` (after replication) or an earlier `restore.<ts>`, the core falls back to the stale `index` directory. Still the code on main.

## What the branch changes
- `RestoreCore`: remember the name of `core.getIndexDir()` before switching; on rollback, delete `index.properties` only if it was `index`, otherwise rewrite it with `core.modifyIndexProps(previousName)`.
- New `TestRestoreCore.testFailedRestoreAfterSuccessfulRestoreKeepsCurrentIndex`: backup, successful restore (core now on `restore.<ts>`), add one doc, corrupt the backup (delete `segments_N`), restore again (fails), expect `nDocs + 1` docs.

## What was guessed (verify these first)
1. `core.getIndexDir()` before the switch is the directory in use (full path; `Path.of(..).getFileName()`); an `index.properties` that names the default dir explicitly is treated like the default.
2. The test relies on the old `index` directory being empty/stale after the first restore (`deleteNonSnapshotIndexFiles`). If the rollback bug does not show up as a doc-count mismatch, assert on `core.getIndexDir()` instead.
3. Same corruption trick and polling as `testFailedRestore`; `verifyDocs(n, ...)` is assumed to check `*:*` numFound.
4. Not covered: the rollback runs inside a `catch`; a failure of `modifyIndexProps` there would mask the original error (same as the existing delete).

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.handler.TestRestoreCore"
```
Fail-before: revert only `RestoreCore.java`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
