# Flaky-fix post-PR review round 6, slice 3: SOLR-18532 (PR #5100), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-6.md`, slice 3. Prior round: `reports/flaky-fix-post-pr-review-round-5-s3.md`. Reads dated 2026-10-10.

Scope: fork branch `solr-18532-submit`. `git ls-remote` returned `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, which equals the `headRefOid` of PR #5100. A read-only fetch of that branch went to `refs/remotes/origin/solr-18532-submit`. The live PR was read through `research/gh.ps1` only (`pr view`, `pr checks`, `run list`, and GET calls to `api`). No body edit, comment, review, close, branch edit, Jira write, build, Gradle, test, Selenium or gate run. The live body was saved to the session scratchpad for the diff. This report is the only file written in the repo.

## Verdict

**STILL OPEN: one item.** Both round 5 items are applied in the live body and in the main-side draft. The final read finds one citation whose line range does not contain the claim it supports.

Remaining item. The fix goes into the live body and into `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare` (the two copies are identical today):

- Live line 15, "What this change does", second link, "The change is in [RestoreCore.java](...#L213-L226)". At head 7dfd, lines 213 to 226 hold the read call (213) and the rollback call (226). The write-back helper (283 to 314, body 300 to 307) and the delete branch (297 to 298, `if (previousIndexProps == null) dir.deleteFile(...)`) are outside that range. The sentence on the same line, "The file is deleted only when the core had no `index.properties` before the restore", has no line in its cited range.
- Fix: change this link's range to `#L213-L314`. That range covers the read helper (252 to 281), the write-back helper (283 to 314), and the delete branch. Round 5 s3 treated this as optional (its N2). This check asks for lines that contain the claim, so it stays open.

## Item table

| # | Item | Live wording | Source check | Verdict |
|---|---|---|---|---|
| 1 | Round 5 (a): duplicated paragraph gone | Live lines 15 to 19. Line 15 ends "...No public method changes." Line 16 blank. Line 17: "Behavior change: a read error other than "file not found" now stops the restore before the switch. Before, that error was ignored." Line 18 blank. Line 19: "## Proof". The paragraph that began "The rollback path does not run in those tests" is absent. | One blank line separates each block. No doubled blank line. The draft matches. | SATISFIED |
| 2 | Round 5 (b): install-tests bullet ends with no cause | Live line 45: "- This change does not address the teardown failure seen in the install tests." | The bullet states no cause. The t1 record (Q3) says the rollback catch runs for the nonexistent-location install test and throws the expected SolrException, and labels the second open after the rollback an inference. | SATISFIED |
| 3 | Final read: citations at the head | Live line 15, second link, `RestoreCore.java#L213-L226` | See Citation check. The cited range does not contain the delete or write-back logic that the sentence describes. | STILL OPEN |
| 4 | Title | "SOLR-18532: RestoreCore rollback restores the previous index.properties" | At head, lines 213 and 226 capture and roll back. The write-back is at 288 to 308. The delete happens only when no file existed (297 to 298). The title describes that. | SATISFIED |
| 5 | Summary, "What happens today" | Line 7: "A failed restore or install can leave the core pointing at the wrong index directory." | Base `RestoreCore.java` line 227 deletes `index.properties` in the rollback. `SolrCore.java` (head 437 to 462) falls back to the default `index` directory when the file is absent. `admin/api/InstallCoreData.java` lines 91 to 92 call `RestoreCore.create` and `doRestore`, so install shares the path. | SATISFIED |
| 6 | "No public method changes" | Line 15 | `git diff 3f5d4c5bf8ac 7dfd3d98...` on `RestoreCore.java` adds and removes no line containing `public`. | SATISFIED |
| 7 | Behavior change: read error stops the restore | Line 17 | Head `readIndexProperties` (256 to 281) rethrows any IOException except FileNotFoundException and NoSuchFileException, which return null. It runs at line 213, before `modifyIndexProps` at 214, so the restore stops before the switch. Base `SolrCore.writeNewIndexProps` (1516 to 1517) catches an IOException from `openInput` and ignores it. | SATISFIED |
| 8 | Proof: old rollback deletes the file | Line 23 | Base `RestoreCore.java` line 227. Receipt step 3 says the same. | SATISFIED |
| 9 | Proof: test name | Line 23: `TestRestoreCore.testFailedRestoreKeepsNonDefaultIndexPointer` | Head `TestRestoreCore.java` line 243. | SATISFIED |
| 10 | Bold one-line summary on every section | Bold lines 7, 13, 21, 30, 36 | Five sections, each opening with one bold line. The "AI assistance" footer has no summary line. It is the approved template text in `pr-formula.md`. See note N1. | SATISFIED |
| 11 | Choice section ends with a pointed question | Line 32: "...or should the rollback set only the previous directory name again?" | Ends with a question. | SATISFIED |
| 12 | No internal vocabulary | Scan of the live body | No gate, receipt, ledger, log name, run identifier, seed, claim or takeover. "Changelog" is the required line. "review" appears only in the required AI footer. | SATISFIED |
| 13 | Body equals draft | Whole body | See Body vs draft. | SATISFIED |
| 14 | Changelog file and title | `changelog/unreleased/SOLR-18532.yml` line 1: "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." | File exists at 7dfd, 7 lines. The title says what the rollback now does. It does not read as never deleting a file. | SATISFIED |

## Limits check

| # | Live Limits text (line) | Head check | Verdict |
|---|---|---|---|
| 1 | In-memory factory (38): "The proof runs on the in-memory test directory factory. The atomic file move used by the file-system factory is not run by these tests." | `solr/core/src/test-files/solr/collection1/conf/solrconfig-leader.xml` line 22, the config this test loads (line 72), uses `${solr.directoryFactory:solr.MockDirectoryFactory}`. No Gradle or properties file sets `solr.directoryFactory` for tests; the only match is an env-var mapping in solrj resources. `SolrTestCaseJ4` sets the property only inside `useFactory` (lines 376, 384), which this test does not call. `MockDirectoryFactory` extends `EphemeralDirectoryFactory`, so it never runs `StandardDirectoryFactory`'s `Files.move` (130 to 145). | SATISFIED (note N5) |
| 2 | Rollback failure (39): "If writing the saved bytes back fails, the error stops the rest of the rollback. The writer is not reopened and the restore directory is not removed. The base code has the same shape around its delete." | Head line 226 is inside the catch. An IOException there skips 228 to 231 (`doneWithDirectory`, `remove`, `newIndexWriter`, `openNewSearcher`). Base line 227 (`deleteFile`) sits in the same catch and has the same effect (base 218 to 237). | SATISFIED |
| 3 | Temp file (40): "a temporary `index.properties.<number>` file can be left ... Nothing removes it. The existing code has the same pattern." | Head 300 to 307 writes `index.properties.<nanoTime>` and renames it. Nothing removes it on failure. `SolrCore.modifyIndexProps` (1474 to 1476) uses the same pattern. | SATISFIED |
| 4 | Non-atomic fallback (41): "On a factory that deletes and then renames ... Only the in-memory factories use that fallback today; StandardDirectoryFactory has a similar non-atomic move when an atomic move is not supported. This is not new." | `DirectoryFactory.renameWithOverwrite` (204 to 215) deletes, then renames. The Ephemeral family (`ByteBuffersDirectoryFactory`, `RAMDirectoryFactory`, `MockDirectoryFactory`) inherits it. `StandardDirectoryFactory` (130 to 150) uses `Files.move` with a non-atomic fallback at 141 to 145; its super call (148) runs only for a non-filesystem base directory. No other DirectoryFactory class exists in the tree at head. "Not new": `SolrCore.modifyIndexProps` (1476) already uses this rename on the same file. | SATISFIED |
| 5 | Read-error change (17) | See item 7. | SATISFIED |
| 6 | Failure wait (44): "polls the restore status 10 times at 50 ms intervals ... copied from the existing `testFailedRestore`. A slower failure would make the test fail rather than pass." | Head 300 to 303 is the same 10 by 50 ms loop as 227 to 230 in `testFailedRestore`. If no AssertionError arrives, `expectThrows` fails the test. | SATISFIED |
| 7 | SOLR-9865 follow-up (43): "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name." | `pr-drafts/replication-backup/SOLR-9865.md` on `origin/pr-prepare` (line 15) says it writes back the previous directory name. The 9865 branch itself was not checked. The base-to-head diff touches only `RestoreCore.java`, `TestRestoreCore.java` and the changelog, so the 9865 change is not in this PR. | SATISFIED against the draft (see N4) |
| 8 | Not checked (42): "The test checks the pointer after the rollback. It does not check that the file is absent when the core had no earlier file." | Head 297 to 315 assert only the pointer. | SATISFIED |

## Citation check

All six links use the full head SHA `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. Each range was read with `git show <sha>:<path> | cat -n | sed -n`.

| Link (live line) | Lines at head | Content at those lines | Verdict |
|---|---|---|---|
| `RestoreCore.java` L213-L226 (15, first link, "reads the file's bytes") | 213 `readIndexProperties()`; 226 `restoreIndexProperties(previousIndexProps)` | Covers the read and the rollback call. | SATISFIED |
| `RestoreCore.java` L213-L226 (15, second link, "The change is in") | Same range | Does not cover the read helper (252 to 281), the write-back helper (283 to 314), or the delete branch (297 to 298). The claim "The file is deleted only when..." has no line in range. | STILL OPEN. Fix: L213-L314 |
| `RestoreCore.java` L213-L226 (23, Proof) | Same range | The reverted code is the capture and the rollback call. | SATISFIED |
| `TestRestoreCore.java` L242-L319 (15) | 242 `@Test`; 243 method; 319 closing brace | The new test. | SATISFIED |
| `StandardDirectoryFactory.java` L130-L148 (41) | 130 `renameWithOverwrite`; 136 to 140 atomic move; 141 to 145 non-atomic fallback; 148 super call | The atomic and non-atomic moves. | SATISFIED |
| `changelog/unreleased/SOLR-18532.yml` L1-L7 (47) | File is 7 lines | The whole changelog. | SATISFIED |

## Proof and count check

| Body (Proof) | Receipt (`receipts/SOLR-18532.md` on `origin/pr-prepare`) | Head check | Result |
|---|---|---|---|
| `TestRestoreCore`: 4 tests, 0 failures at `f1e5031...`, verified 2026-10-10 | Step 4: "TestRestoreCore 4 tests, 0 failures" at `f1e5031fc3a9624b03daf353f26c977891d7d876`; gate done 2026-10-10T18:31:33Z | `git diff --stat f1e5031 7dfd` changes only the changelog title line (1 insertion, 1 deletion). | SATISFIED |
| `RestoreCoreOpTest`: 1 test, 0 failures, same head | Step 4: "RestoreCoreOpTest 1 test, 0 failures" | File exists at head (`solr/core/src/test/org/apache/solr/handler/admin/RestoreCoreOpTest.java`) with one test (line 28). | SATISFIED |
| New test fails on base at the pointer check | Step 3, with RestoreCore reverted: fails at the pointer check, pointer null | Head test asserts the pointer at 312 to 315. | SATISFIED |
| Four test names: testSimpleRestore, testBackupFailsMissingAllowPaths, testFailedRestore, testFailedRestoreKeepsNonDefaultIndexPointer | Receipt names all four | Head lines 99, 176, 191 and 243 each define one. `testBackupFailsMissingAllowPaths` has no `@Test` annotation at head (line 175 is blank); the receipt's run counted it, so the count matches. | SATISFIED |
| Old rollback deletes the file | Receipt step 3 | Base line 227. | SATISFIED |

Every Proof number (4, 0, 1, 0, 2026-10-10, `f1e5031`) appears in the receipt. The Proof names `f1e5031` while the PR head is `7dfd`; that is accurate, because the code tree is identical (note N2).

## Body vs draft

- Live body from `gh pr view 5100 --json body`, saved to the scratchpad (5,055 characters, no CR). Draft `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare` (51 lines, no CR).
- After CR stripping, `diff` reports one differing line: line 1, the robot emoji. My console capture turned the emoji bytes into mojibake. The live JSON and the draft both carry the emoji. No other line differs, including the final newline.
- Result: identical wording. Items 1 and 2 are already applied in both copies. The item 3 fix must go into both copies.

## CI and review state

- PR #5100: OPEN, draft. `headRefOid` `7dfd3d98d0a2f016c520139cbca96ea4a49f6689` (matches the fork tip). `reviewDecision` empty. `mergeStateStatus` UNSTABLE.
- `statusCheckRollup`, the only entry: `labeler` (Pull Request Labeler), SUCCESS, completed 2026-10-10T20:45:09Z.
- Actions runs at 7dfd (`pull_request`), from `gh run list`: Validate Changelog, `action_required`; Gradle Precommit, `action_required`; Solr Tests via Crave, `action_required`. The state means the runs await approval. It is not a code failure. CI has not validated the changelog YAML.
- Reviews (`pulls/5100/reviews`): none. Review comments (`pulls/5100/comments`): none. Issue comments (`issues/5100/comments`): none.

## Verified and rejected automated findings

None. PR #5100 has no automated comment, review, review comment or annotation. The labeler check adds labels and posts nothing, so there was no finding to verify or reject.

## Notes (not verdict items)

- N1. The "AI assistance" footer has no bold one-line summary. It is the approved template text in `pr-formula.md`, so I treated it as the footer, not as a claim section. The lead should decide whether the presentation rule covers it.
- N2. The Proof names `f1e5031` while the PR head is `7dfd`. The only delta is the changelog title. Naming the PR head in the Proof would help a reader who checks out the PR. Round 5 N1 made the same point.
- N3. The body is 5,055 characters. The formula's guide is about 3,500 unless the ticket is unusually complex. Round 2 note S3-8d is still open.
- N4. The sentence "Once this change lands, I will follow up on that branch by dropping its rollback edit" is a public commitment. Round 5 N4 asked the lead to confirm it with Nick, and that is still unconfirmed. The SOLR-9865 claim was checked against its draft only.
- N5. "In-memory" holds by class family. `MockDirectoryFactory` calls `LuceneTestCase.newDirectory()`, and Lucene's test randomizer picks the underlying directory, which is outside this checkout. The Limits sentence still holds, because the mock factory never runs `StandardDirectoryFactory`'s atomic move.
- N6. Round 5 s3 treated the citation range as optional and marked the citation item satisfied. This check marks it open, because the delete and write-back lines are outside the cited range.
