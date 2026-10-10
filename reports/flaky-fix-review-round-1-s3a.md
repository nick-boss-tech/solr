# Flaky-fix review round 1, slice 3a: SOLR-18532 production change audit

Scope: read-only audit of the SOLR-18532 production change at head `348dd63d85a563c77e0a20b5742b2a0c845dc191` (origin/solr-18532-submit, checked equal before any work), base `3f5d4c5bf8ac`. The production file is `solr/core/src/java/org/apache/solr/handler/RestoreCore.java`. The test diff and changelog were read for scope. No builds, tests, Gradle, Selenium, gate or test-queue runs. No fetch, commit, push, PR, comment, Jira write or PR description edit.

## Verdict

**Ready for draft, with conditions.**

The change does what the root-cause report names. On rollback it writes back the captured index.properties bytes, and it deletes the file only when the core had no such file before the switch. No rollback failure leaves the pointer in a worse state than the old code did. The draft must describe this as a product defect fix, not a fix for the flaky teardown, because the t1 report and the main RCA both say so. Opening stays held until the owner decisions below are taken.

## Basis and limits

- Head check: `git rev-parse origin/solr-18532-submit` returned `348dd63d85a563c77e0a20b5742b2a0c845dc191`, which matches the named head.
- Ticket text: no Jira packet for SOLR-18532 exists in the local workspace (searched `research/` and the worktree). The ticket text used here is the assignment's summary: rollback restores the captured pointer via temp file and rename, and deletes only when the operation created the file. It was not checked against Jira.
- Numbers: `receipts/SOLR-18532.md` only. The gate log on vm1 was not read.
- Diff: 3 files, 197 insertions, 14 deletions: `changelog/unreleased/SOLR-18532.yml` (new), `RestoreCore.java`, and `solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java`.
- Base check: `RestoreCore.java`, `SolrCore.java` and `DirectoryFactory.java` are unchanged between upstream `8e62c2686882` and base `3f5d4c5bf8ac`, so the t1 line references apply at this base.

## Q1. Does the rollback restore the captured pointer on every path?

Line numbers are at head, `RestoreCore.java` unless noted.

| Path | What happens | Verdict |
|---|---|---|
| Success | Capture at 213 is stored and not used. The new pointer is kept. | Correct |
| Failure before line 209 (download loop, future handling, interrupt) | Throws before the capture. No switch happens and index.properties is untouched. A partial `restore.*` dir stays (pre-existing). | Correct |
| Exception in `readIndexProperties` (256-281) | Throws before the switch at 214. Pointer untouched, restore aborts. | Correct, see F4 |
| Failure in `core.modifyIndexProps` (214) | Sits outside the rollback try (217). An atomic rename failure leaves the old pointer. The non-atomic fallback can leave no pointer, see F3. | Partly covered, see F3 |
| Failure in `newIndexWriter` or `openNewSearcher` (218-219) | Rollback at 226. A null capture leads to `deleteFile` (298), the same as the old code. A non-null capture leads to temp write and rename (300-307). | Correct |
| Failure inside `restoreIndexProperties` (226) | The exception leaves the catch. Lines 228-231 (doneWithDirectory, remove, newIndexWriter, openNewSearcher) do not run. | Old shape, see F1 |
| Restore with no previous pointer | Null capture leads to `deleteFile`, the same as base line 227. | Correct |

Failure states compared with the old code: on each rollback failure the pointer is either still at the restore directory (old and new) or absent (non-atomic fallback, old and new). The change adds no failure state that is worse than the old code's.

## Q2. Is a temp file left behind, and does the rename happen only after the full write?

- Order at 301-307: `createOutput` (301-302), `writeBytes` (303), close at the end of the try-with-resources (304), `sync` (305), `renameWithOverwrite` (306-307). The rename happens only after the bytes are written, closed and synced. Correct.
- Leftover: any failure after `createOutput` (write, close, sync or rename) leaves `index.properties.<nanoTime>` in the data dir. Nothing deletes it. The same pattern exists in `SolrCore.writeNewIndexProps` (`SolrCore.java` about 1525-1535), so it is inherited, not new. See F2.
- Nothing reads `index.properties.*` files, so the leftover does not change pointer behavior.

## Q3. Does the fix preserve the pointer to a non-default index directory?

Yes. `readIndexProperties` (265-274) captures the exact bytes, and `restoreIndexProperties` writes them back unchanged (303). The `index=` line for a non-default directory comes back byte for byte. The new test asserts this: `TestRestoreCore.java` lines 308-315 compare the pointer after the failed restore with the pointer read before it.

Limit: the test harness uses an in-memory directory factory (the test comment says so). At this head, the in-memory factories (RAM, ByteBuffers, Mock) use the base `DirectoryFactory.renameWithOverwrite` (`DirectoryFactory.java` 204-215: delete, then rename), not the atomic move in `StandardDirectoryFactory` (130-150). The proof therefore does not exercise the atomic path that file-system factories use. See F6. The exact in-memory class was not checked.

## Q4. Does the change match the t1 finding, and is anything left out?

Match: t1 (`reports/flaky-tests-root-cause-round-1-t1.md` line 95) names `RestoreCore.java` line 227 at base as deleting index.properties on rollback. It is correct for a default index directory and wrong for a non-default one. The change replaces that delete with restore of the captured pointer, and keeps the delete only for the created-file case. That is the named defect.

Not the flake's fix: t1 (lines 95 and 112) and the main RCA (`reports/flaky-tests-root-cause-round-1.md` line 34, "Separate product defect, not this flake's fix", and line 114, which recommends a separate ticket) say the same. t1 shows the restore path cannot produce the teardown error, and the affected tests use fresh collections, where the captured pointer is null. For those tests the new code takes the old delete path, so the branch does not change the flake's test behavior. The draft must not say the flake is fixed.

Left out: t1 names no other requirement for this ticket. The other t1 items (the `portsToFailOn` reset, the JettySolrRunner reserved-executor warning, the CoreContainer guard, and the GCS stash clear) are not in this branch and are not owed by it. They belong to the flake's own fix. t1 lists a log read (Run 1) as a pending step; I found no record that it was done.

## Q5. Anything outside the ticket's scope?

- Three files, no unrelated changes. Public signatures are unchanged: `doRestore()` and `call()` are untouched, and the two new helpers are private. All added imports are used.
- No debug code, `System.out`, `printStackTrace`, TODO, FIXME, or em dash in the added lines (checked by search of the added lines).
- Identity: both commits (`07a7ead4783`, `348dd63d85a`) are authored and committed as Nick Shanin. No Co-Authored-By or Claude trailer.
- The changelog fragment has the title, type, authors and links shape of the existing fragments. The title is accurate: `InstallCoreData.java` line 91 also calls `RestoreCore.create`, so "install" is correct.
- See F7: the commit message of `348dd63d85a` narrates an internal gate failure.

## Findings

Severity: M = name in Limits or fix before opening; L = note.

- **F1 (M) `RestoreCore.java` 226-234.** The rollback is not guarded. If `restoreIndexProperties` throws, the catch exits before `doneWithDirectory`, `remove`, `newIndexWriter(false)` and `openNewSearcher` run. The writer is not reopened and the restore dir is not removed. The base code has the same shape around `deleteFile` (base line 227), but the new code adds four I/O calls there (createOutput, writeBytes, sync, rename). A fix would wrap the pointer restore so the rest of the rollback still runs. See D2.
- **F2 (L) `RestoreCore.java` 301-307.** No cleanup of `index.properties.<nanoTime>` on failure. Inherited from `SolrCore.writeNewIndexProps`. See D2.
- **F3 (L, Limits) `RestoreCore.java` 214 and `DirectoryFactory.java` 204-215.** The switch (`modifyIndexProps`) sits outside the rollback try. On a factory that uses the base delete-then-rename fallback, a failure between the delete and the rename leaves no pointer, and the rollback never runs. Only the in-memory factories at this head use that fallback; `StandardDirectoryFactory` (130-150) does an atomic move. Not new. Name it in Limits.
- **F4 (L, behavior change) `RestoreCore.java` 268-275.** A read error other than FileNotFound or NoSuchFile now aborts the restore before the switch. The base read path in `SolrCore.writeNewIndexProps` (about 1507-1521) ignores an IOException on open. The new behavior is the safer one, but it is a change the changelog does not mention. See D5.
- **F5 (L to M, test) `TestRestoreCore.java` 297-306.** The failure wait is a loop of 10 status polls with 50 ms sleeps. `TestRestoreCoreUtil.fetchRestoreStatus` (`solr/test-framework/src/java/org/apache/solr/handler/TestRestoreCoreUtil.java` lines 29-53) returns false while the restore is in progress, and throws only on failure or an exception. The loop does not wait for the failed state. If the failure lands after the window, `expectThrows` reports no AssertionError and the test fails. The same pattern is in `testFailedRestore` (`TestRestoreCore.java` 224-233). The receipt records one head run. See D3.
- **F6 (L, proof scope).** The proof runs on an in-memory factory and does not cover the atomic move path. Name it in Limits. See Q3.
- **F7 (L, hygiene).** Commit `348dd63d85a` says "the first gate run failed ... and used a forbidden Properties.load overload". Commit messages appear on the PR, and `pr-formula.md` section 3 keeps internal catches out of PR text. See D4.
- **F8 (L, record hygiene).** `gates/SOLR-18532.md` line 3 still names head `07a7ead4783`. Its status line (line 8) names `348dd63d85a`. The receipt is the source for numbers.

## Owner decisions (flagged, not taken)

- **D1.** Ship this product defect fix in this round, or file it as its own ticket as the main RCA recommended (option a). Nick created SOLR-18532, so shipping looks intended. The framing (product defect, not flake fix) is the owner's call.
- **D2.** Whether to harden the rollback before opening: guard the pointer restore so the writer reopen and restore-dir cleanup still run (F1), and make a best-effort delete of the temp file on failure (F2). Any such change needs a new head and a new gate. Not done here.
- **D3.** Whether to change the new test's failure wait (F5) before opening. Also a new head and gate.
- **D4.** Whether to reword or squash commit `348dd63d85a` (F7) before the branch is opened. Rewording after a public PR needs owner direction.
- **D5.** Whether to keep the stricter read in `readIndexProperties` (F4) or match the base tolerance. Recommendation: keep it, and state it in the draft.

## Draft inputs (for the draft slice; nothing written here)

- Frame as a product defect: rollback of a failed restore or install no longer loses a non-default index pointer. Do not claim the teardown flake is fixed.
- Proof: `TestRestoreCore.testFailedRestoreKeepsNonDefaultIndexPointer`. Per the receipt, it fails on the reverted `RestoreCore` at the final assertion (pointer expected, null). Head run: TestRestoreCore 4 tests, 0 failures; RestoreCoreOpTest 1 test, 0 failures. One run each at head (receipt step 4).
- Limits: in-memory test factory only (F6); atomic move path not exercised; rollback failure handling and temp cleanup unchanged (F1, F2); a failed switch on a non-atomic factory is not covered (F3); read errors now abort the restore (F4); the GCSInstallShardTest teardown flake is not addressed by this change.

## Not checked

- The gate log on vm1 (`g18532-gate.log`).
- Jira ticket text (not local; no Jira call was made).
- Lucene's APPEND and rename exception behavior (library code, not in this repo).
- Runtime behavior of the atomic move on Windows and Linux (read only, not run).
- No builds, tests or gate runs were done. Compile and test status come from the receipt only.
