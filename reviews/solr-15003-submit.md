# solr-15003-submit

- Branch: origin/solr-15003-submit
- Head: afeab98ea82c
- Base: upstream/main at 9d7cc2884e8a (merge-base b5c71bc5573c, 46 commits behind)
- Scope: 6 commits. `SolrCore.java` (+27/-16: `exists` guard in the named-snapshot deletion path; `deleteNonSnapshotIndexFiles` unchanged), `IndexFetcher.java` (+22/-6: removes the old in-fetch cleanup and `remove(indexDir)`, adds a post-install cleanup that re-resolves the core via `CoreContainer.getCore`), `TestReplicationHandler.java` (+165: three tests), changelog `SOLR-15003.yml` (+7)
- Verdict: Needs work (the fix logic is sound; the tests have a platform-dependent path comparison, a timing race, and no coverage of the reload path the fix targets)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Handoff: fixed after a CI root-cause analysis at this head (a regression in the branch, fixed and re-gated). Head unchanged: `origin/solr-15003-submit` is at `afeab98ea82c`, as listed.
- The CI fix is the final commit, `afeab98ea82c`, which resolves the core again after a reload. This review checks that fix.

## Verified code facts

- Control flow in `IndexFetcher`: the old `remove(indexDir)` in the base ran only inside `if (isFullCopyNeeded)`. A non-full-copy install never removed the old directory, so removing it now does not leak on that path.
- Post-install block: runs after `openNewSearcherAndUpdateCommitPoint()` and after `reloadCore()` (if the conf changed). `indexDirPath` is captured as `solrCore.getIndexDir()` before the fetch (`IndexFetcher.java:606`), so it is the old directory. The reload case is handled by `coreContainer.getCore(...)`, which returns the current core instance. The fetcher's own `solrCore` is the closed pre-reload instance.
- API resolution: `DirectoryFactory.exists(String)` (`throws IOException`), `SolrCore.deleteNonSnapshotIndexFiles(String)` (`throws IOException`), `CoreContainer.isShutDown()`, `CoreContainer.getCore(String)`, and `SolrCore.getCoreContainer()` all exist. `SolrCore` implements `Closeable`, so try-with-resources is valid.
- Named-snapshot path (`SolrCore.java` hunk): `exists` guard before `DirectoryFactory.get`. `get` creates the directory, so without the guard a delete for a vanished directory would recreate it before removing it. The guard prevents that.
- `SolrSnapshotMetaDataManager.listSnapshotsInIndexDir(String)` compares with `equals` (`SolrSnapshotMetaDataManager.java:280-284`), and `snapshot(...)` stores the path string as given (`:168-185`).

## Findings (ranked)

1. **HIGH, mechanism verified by reading; whether it fails depends on `dataDir` format (hypothesis). Test 2 compares a `Path`-normalised string against a raw stored string.** `testFullCopyKeepsSnapshotFilesInOldIndexDir` stores the snapshot with `core.getIndexDir()` (raw), then looks it up with `listSnapshotsInIndexDir(oldIndexDir.toString())`, where `oldIndexDir = Path.of(core.getIndexDir())`. `Path.toString()` drops a trailing slash and, on Windows, converts `/` to `\`. `getIndexDir()` returns `dataDir + "index/"` (with slash) or `dataDir + s.trim()` from `index.properties` (`SolrCore.java`, around lines 440-449). If the strings differ, the lookup returns nothing, and `assertEquals(1, …size())` fails. This is platform- and config-dependent, and the repo workspace is Windows. Suggested fix (proposed, not applied): keep the raw `core.getIndexDir()` string in a variable and use it for the snapshot, the lookup, and the `oldIndexDir` comparison. The same issue weakens test 1's `assertFalse(oldIndexDir.toString().equals(core.getIndexDir()))`: when the two strings differ in format, that assertion passes whether or not the directory switched. Test 1's real check (old directory gone) is the `Files.exists` assertion.

2. **MEDIUM, verified ordering. Test 1 has a race and no poll.** `testFullCopyRemovesOldIndexDirWhenNoSnapshot` asserts `!Files.exists(oldIndexDir)` immediately after `rQuery(4, ...)`. The cleanup block runs after `openNewSearcherAndUpdateCommitPoint()` (`IndexFetcher.java`, post-install block). `fetchindex` without `wait=true` starts a non-waiting thread (`ReplicationHandler.java:445-455`), and `rQuery` returns as soon as the document count matches, which happens before cleanup. Test 2 polls for exactly this and says so in its comment; test 1 should do the same, or send `wait=true`.

3. **MEDIUM, verified coverage gap. The reload path the fix targets is not tested.** The CI regression was in the post-reload case. None of the three tests changes the configuration, so `modifiedConfFiles` is empty and no reload happens. The `coreContainer.getCore(...)` branch is therefore not exercised by any test in this diff. A test with a changed config file (so the install reloads the core) is needed to pin the fix.

4. **MEDIUM, hypothesis. The cleanup can be skipped entirely.** If `coreContainer.getCore(name)` returns `null` (the core is being unloaded or reloaded at that moment), the block does nothing, and the old index directory is never removed. Base removed it unconditionally inside `if (isFullCopyNeeded)`. This is a leak in a race window. Not run; frequency unknown.

5. **LOW, verified. Cleanup depends on `openNewSearcherAndUpdateCommitPoint()` succeeding.** If that call throws, the post-install block is not reached, and the old directory is not removed in this call. Base removed it earlier, before the searcher was reopened. Minor, but a behaviour change.

6. **LOW, hypothesis. `testDeleteNamedSnapshotWithMissingIndexDir` may not be fail-before.** On base, `DirectoryFactory.get` creates the missing directory and `remove` deletes it again, so no error is raised and the test may pass on base as well. The Javadoc says it "must not fail", which implies base fails. Not verified; if it passes on base, the gate's fail-before check returns `NOT_PROVEN`. Worth checking on the Linux side.

7. **LOW, verified. Changelog title has two clauses.** "Keep snapshot metadata consistent across replication recovery; clean up the old index after a full copy without deleting files pinned by named snapshots." The first clause is not clearly tied to a separate hunk. The owner should confirm the title matches the code.

8. **LOW, verified. Formatting artifact.** `SolrCore.java` contains `if (snapshots\n .isEmpty()) { // …` with a trailing comment, which is a spotless-style wrap. Spotless will normalise it; no behavioural effect.

## Owner calls

None. The fix design (post-install cleanup, core re-resolution) follows the approach already used by `openNewSearcherAndUpdateCommitPoint`.

## Proposed changes (not applied; for the owner)

- Use the raw `core.getIndexDir()` string everywhere in test 2 (finding 1), and compare normalised paths only where a comparison is intended.
- Add a poll to test 1 (finding 2).
- Add a test with a config change so the install reloads the core (finding 3).
- Decide whether the `null` from `getCore` should fall back to removing the directory directly (finding 4).

## Not checked

- Not compiled, formatted (spotless), or run. Pass and fail on base and head is a hypothesis.
- Whether `openNewSearcherAndUpdateCommitPoint` can throw in practice (finding 5).
- Existing reload-path tests elsewhere in the replication suite (finding 3 only covers the tests in this diff).
- Error Prone was not run.
- Nothing pushed or posted to GitHub or JIRA.
