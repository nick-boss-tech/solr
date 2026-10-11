# Flaky-fix post-PR review round 9, slice 2: SOLR-18532 (PR #5100), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-9.md`, slice 2. Round 8 item: `reports/flaky-fix-post-pr-review-round-8.md` and `reports/flaky-fix-post-pr-review-round-8-s2.md`. Head checked: `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. Reads dated 2026-10-10.

Scope: read-only. `git ls-remote origin refs/heads/solr-18532-submit` returned `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, equal to the PR head. The read-only fork fetch ran into `refs/remotes/origin/solr-18532-submit`, and the head commit is present locally. The live PR was read through `research/gh.ps1` only: `pr view`, `pr checks`, `run list`, and GET calls to the reviews, review comments and issue comments endpoints. No body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle, test, Selenium or gate run.

## Verdict

**SATISFIED.** The round 8 item is fixed in the live body. Line 41 now names the factories that extend `EphemeralDirectoryFactory`, and no "in-memory" or "in memory" wording remains. The live body is identical to `pr-drafts/flaky-fixes/SOLR-18532.md` on origin/pr-prepare. Every citation, limit, Proof number and test name checks out against the head and the receipt.

Two facts for the lead, neither a blocker under this slice:

- CI has not run on this head. The three `pull_request` workflows show `action_required` and the only completed check is the labeler, which labels and reports no findings. That is a state, not a code finding, but the PR has no CI result yet.
- Notes N1 to N5 below are optional wording points that earlier rounds also raised. None changes the verdict.

## Item table

| # | Item | Live wording (line) | Source check at 7dfd3d98 | Verdict |
|---|---|---|---|---|
| 1 | Round 8: line 41 names the Ephemeral family, not "the in-memory factories" | Line 41: "Only the factories that extend `EphemeralDirectoryFactory` use that fallback today;" | `MockDirectoryFactory.java` line 30 extends `EphemeralDirectoryFactory`. `ByteBuffersDirectoryFactory.java` line 29 and `RAMDirectoryFactory.java` line 29 extend it too. `EphemeralDirectoryFactory` (line 26, extends `CachingDirectoryFactory`) and `CachingDirectoryFactory` do not override `renameWithOverwrite`; only `DirectoryFactory.java` (lines 204-215, delete then rename) and `StandardDirectoryFactory.java` (lines 130-150) define it. So the three Ephemeral factories inherit the base delete-then-rename. Wording matches the round 8 request exactly. | SATISFIED |
| 2 | No "in-memory" or "in memory" wording anywhere in the body | Case-insensitive search of the whole live body for "memory": zero hits. Zero hits in the draft too. | None needed. | SATISFIED |
| 3 | Line 38: test directory factory named, "in-memory" dropped | Line 38, first sentence: "The proof runs on the test directory factory (MockDirectoryFactory), which opens Lucene's randomized test directory." | `MockDirectoryFactory.java` javadoc line 29 ("Opens a directory with LuceneTestCase#newDirectory()"), `create()` lines 47-50 (`newMockDirectory()` or `newDirectory()`). `solr/core/src/test-files/solr/collection1/conf/solrconfig-leader.xml` line 22 sets `${solr.directoryFactory:solr.MockDirectoryFactory}`, and `TestRestoreCore.java` lines 72-76 copy that config. Wording is accurate. | SATISFIED |
| 4 | Line 38, second sentence: the atomic move is not run by these tests | "The atomic file move used by the file-system factory is not run by these tests." | Tests use `MockDirectoryFactory` (item 3), so `StandardDirectoryFactory.renameWithOverwrite`'s atomic move (lines 136-140) is not reached. | SATISFIED |
| 5 | Title | "SOLR-18532: RestoreCore rollback restores the previous index.properties" | Head rollback (`RestoreCore.java` line 226) writes back the saved bytes (helper lines 288-308), or deletes the file when none existed (lines 297-298). The title says that. | SATISFIED |
| 6 | Each section opens with a bold one-line summary | Bold lines 7, 13, 21, 30, 36 (five sections) | The "AI assistance" footer (line 49) has no bold line; it is the approved template text, not a section with a claim. | SATISFIED |
| 7 | Choice section ends with a pointed question | Line 32: "Was saving the exact bytes the right call, or should the rollback set only the previous directory name again?" | Ends with a question mark. See N2 for the optional cost point. | SATISFIED |
| 8 | Proof numbers come from the receipt | Lines 23, 25, 26 | See the Proof and count check. | SATISFIED |
| 9 | Test count and the four test names exist at head | Line 25: "4 tests". Line 23 names the new test. | Receipt step 4 lists four names. All four exist in `solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java` at head: `testSimpleRestore` (line 99), `testBackupFailsMissingAllowPaths` (line 176), `testFailedRestore` (line 191), `testFailedRestoreKeepsNonDefaultIndexPointer` (line 243). See N6 for the annotation note on the second. | SATISFIED |
| 10 | No internal vocabulary | Whole body | No gate, receipt, ledger, run identifier, seed, claim, takeover, round, pool, lead or worktree wording. Hits on "check", "run", "log" (in "Changelog") are ordinary English or the template label. | SATISFIED |
| 11 | No em dashes | Whole body | Zero em dashes in the live body and the draft. | SATISFIED |
| 12 | "No public method changes" (line 15) | Line 15 | The base-to-head diff of `RestoreCore.java` adds or removes no line containing "public". The new helpers are private. | SATISFIED |
| 13 | Read-error behavior change (line 17) | "a read error other than "file not found" now stops the restore before the switch. Before, that error was ignored." | Head `readIndexProperties()` (lines 256-281) is called at line 213, before `core.modifyIndexProps` at line 214. It returns null only for `FileNotFoundException` and `NoSuchFileException` (lines 268-270); other `IOException`s propagate. Base `SolrCore.writeNewIndexProps` (lines 1505-1518) catches every `IOException` from `openInput` and ignores it ("ignore; file does not exist"). The claim holds. | SATISFIED |
| 14 | Changelog file exists and its title is accurate | Line 47 links `changelog/unreleased/SOLR-18532.yml` L1-L7 | File exists at head, 7 lines. Title: "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." Accurate. It does not say the file is never deleted. | SATISFIED |
| 15 | Limits bullets (lines 38-45) | See the Limits check | All facts hold. Line 41 wording is the item 1 fix. | SATISFIED |

## Limits check

| Live line | Limit | Head check | Verdict |
|---|---|---|---|
| 38 | Test directory (MockDirectoryFactory, Lucene's randomized test directory) | Item 3. | SATISFIED |
| 38 | Atomic file move not run by these tests | Item 4. | SATISFIED |
| 39 | Rollback failure: writer not reopened, restore directory not removed, rest of rollback stops; base has the same shape | Head `RestoreCore.java` line 226 is the first statement in the catch. If `restoreIndexProperties` throws, lines 228-231 (`doneWithDirectory`, `remove`, `newIndexWriter`, `openNewSearcher`) are skipped. Base code: `dir.deleteFile` (base line 227) inside try/finally, and base lines 234-237 are skipped the same way. | SATISFIED |
| 40 | Temporary file `index.properties.<number>` can be left behind; nothing removes it; the existing code has the same pattern | Head lines 300-307: temp name is `INDEX_PROPERTIES + "." + System.nanoTime()`, then create, write, sync, rename, with no cleanup on failure. `SolrCore.modifyIndexProps` (lines 1474-1476 at head, same at base) uses the same name pattern and the same rename. | SATISFIED |
| 41 | Non-atomic fallback: deletes then renames on a factory that has no atomic move; StandardDirectoryFactory has a similar non-atomic move; not new | `DirectoryFactory.renameWithOverwrite` (lines 204-215): `deleteFile` at 207, `rename` at 214. `StandardDirectoryFactory.renameWithOverwrite` (lines 130-150): atomic move at 136-140, non-atomic move at 141-145 when the atomic move is not supported, super call at 148 when the base directory is not an `FSDirectory`. No Ephemeral factory overrides the method. No factory at head reaches line 148: every `StandardDirectoryFactory` subclass returns an FS-based directory (`MMapDirectory` at `MMapDirectoryFactory.java` line 65, `NIOFSDirectory` at line 30, `NRTCachingDirectory` wrapping `FSDirectory` at `NRTCachingDirectoryFactory.java` lines 51-52, and `newFSDirectory` in the test-side `MockFSDirectoryFactory`), and `getBaseDir` unwraps filters. `DirectoryFactory.java` and `StandardDirectoryFactory.java` are unchanged from base, so "not new" holds. | SATISFIED |
| 42 | Pointer checked; file absence not checked when the core had no earlier file | Test lines 308-315 assert the pointer only; lines 317-318 check the documents. No absence check. | SATISFIED |
| 43 | SOLR-9865 follow-up: that change writes back only the previous directory name | `ls-remote origin refs/heads/solr-9865-submit` returns `4937608bb181efae104c0d6f0257f445af50bf52` (the object is local). Its rollback (lines 231-236) deletes only when the previous directory is "index", and otherwise calls `core.modifyIndexProps(previousIndexDirName)`. The fact holds. The commitment "I will follow up" is Nick's call (N4). | SATISFIED (facts) |
| 44 | Failure-wait: 10 polls at 50 ms, copied from the existing test | Test lines 300-304: loop of 10, `Thread.sleep(50)`, inside `expectThrows(AssertionError.class, ...)`. Same loop in `testFailedRestore` (lines 227-231). `TestRestoreCoreUtil.fetchRestoreStatus` (`solr/test-framework/src/java/org/apache/solr/handler/TestRestoreCoreUtil.java` lines 29-51) throws `AssertionError` on a failed status. If no failure shows within the loop, `expectThrows` fails the test, so a slower failure makes the test fail, as the body says. | SATISFIED |
| 45 | Install-tests bullet ends at "seen in the install tests" with no causal clause | Line 45: "This change does not address the teardown failure seen in the install tests." No causal clause. | SATISFIED |
| 3 (line 7) | "restore or install" path | `InstallCoreData.java` lines 91-92 call `RestoreCore.create` and `doRestore`, so the install path reaches the same rollback. | SATISFIED |

## Citation check

All six links use the full head SHA `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. No other GitHub URL appears in the body. Each range was read at the head.

| Link (live line) | Range | Lines at head | Verdict |
|---|---|---|---|
| `RestoreCore.java` L213-L226 (line 15, "reads the file's bytes") | 213-226 | 213 `readIndexProperties()` capture; 226 `restoreIndexProperties(previousIndexProps)` in the catch | SATISFIED |
| `RestoreCore.java` L213-L314 (line 15, "The change is in") | 213-314 | Capture and rollback call (213-226); read helper (256-281); write-back helper (288-314) with the delete branch (297-298) and the temp write and rename (300-307) | SATISFIED. The range reaches the write-back and delete code. |
| `RestoreCore.java` L213-L226 (line 23, Proof) | 213-226 | The capture and rollback call sites the change alters | SATISFIED |
| `TestRestoreCore.java` L242-L319 (line 15) | 242-319 | 242 `@Test`; 243 the new method; 319 its closing brace | SATISFIED |
| `StandardDirectoryFactory.java` L130-L148 (line 41) | 130-148 | 130 method start; 141-145 the non-atomic move the claim names; 148 super call | SATISFIED |
| `changelog/unreleased/SOLR-18532.yml` L1-L7 (line 47) | 1-7 | Whole fragment, 7 lines | SATISFIED |

The Jira link on line 3 is `https://issues.apache.org/jira/browse/SOLR-18532`, the ticket itself.

## Proof and count check

| Body line | Body text | Receipt (`receipts/SOLR-18532.md` on origin/pr-prepare) | Head check | Verdict |
|---|---|---|---|---|
| 23 | Pre-fix: new test fails at the pointer check, pointer null, because the old rollback deletes the file | Step 3: fails at the pointer check, pointer expected but null, because the old rollback deletes the file | Pointer assertion at test lines 312-315; base rollback deletes at base line 227 | SATISFIED |
| 25 | TestRestoreCore: 4 tests, 0 failures at `f1e5031...`, verified 2026-10-10 | Step 4: "TestRestoreCore 4 tests, 0 failures" | Four runnable test methods at head (item 9) | SATISFIED (see N1) |
| 26 | RestoreCoreOpTest: 1 test, 0 failures at the same head, verified 2026-10-10 | Step 4: "RestoreCoreOpTest 1 test, 0 failures" | `RestoreCoreOpTest.java` lines 27-28: one `@Test` method | SATISFIED (see N1) |

Every Proof number in the body is in the receipt. No count is taken from memory, and no test or build was run for this check.

## Body vs draft

The live body (`gh.ps1 pr view 5100 --json body`) was compared with `pr-drafts/flaky-fixes/SOLR-18532.md` on origin/pr-prepare, with CR characters stripped. The two are identical: 51 lines each, zero differences. The draft already carries the line 41 fix. The live body and the draft agree on every word, link and number. (A byte-order mark appeared in one local capture; it came from the capture file, not the body, and was removed before the comparison.)

## CI and review state

- PR #5100: OPEN, draft. `headRefName` `solr-18532-submit`, `headRefOid` `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, equal to the fork tip. `mergeStateStatus` UNSTABLE. `reviewDecision` empty.
- `statusCheckRollup` (one entry): `labeler` (Pull Request Labeler), COMPLETED, SUCCESS, completed 2026-10-10T20:45:09Z. `pr checks` shows the same entry as pass.
- Actions runs on head 7dfd3d98 (`run list`):
  - Validate Changelog, run 38084962187, `pull_request`: action_required.
  - Gradle Precommit, run 38084962170, `pull_request`: action_required.
  - Solr Tests via Crave, run 38084962168, `pull_request`: action_required.
  - Pull Request Labeler, run 38084960068, `pull_request_target`: success.
- `action_required` is a held run that has not executed, not a failure. No `pull_request` run has executed for this head, so the changelog check, precommit and tests have no CI result on this head.
- Reviews (`repos/apache/solr/pulls/5100/reviews`): none. Review comments (`pulls/5100/comments`): none. Issue comments (`issues/5100/comments`): none.

## Verified and rejected automated findings

None. PR #5100 has no review, review comment, issue comment, or annotation at this head. The labeler check is a labeling step and reports no findings. Nothing to verify or reject.

## Notes (not verdict items)

- N1. The Proof names `f1e5031fc3a9624b03daf353f26c977891d7d876` (the gate head), while the PR head is `7dfd3d98`. `git diff --stat f1e5031 7dfd3d98` changes only `changelog/unreleased/SOLR-18532.yml` (one line). The code tree is identical. The formula asks for "verified <date> at this head". Optional wording: "(same code tree as this head; only the changelog title differs)". Raised in rounds 5 to 8; unchanged.
- N2. The formula's choice template asks for "the alternative's cost". The "A choice to check" section (lines 28-32) names the other route but no cost of it. Raised in round 8; unchanged. If the lead wants it, the cost must come from the code, not an assumption.
- N3. The body is 5,136 bytes against the formula's guide of about 3,500 characters. Raised in earlier rounds; unchanged.
- N4. Line 43: "Once this change lands, I will follow up on that branch by dropping its rollback edit". This is a public commitment by Nick. No file on origin/pr-prepare that I read records that Nick confirmed it. The facts in the sentence hold (item 43). This is the lead's call.
- N5. Line 43 names SOLR-9865 without a link. Optional: `https://issues.apache.org/jira/browse/SOLR-9865`. Raised in round 8 (N7).
- N6. `testBackupFailsMissingAllowPaths` (line 176) has no `@Test` annotation at head; base line 168 is the same. The method predates this diff. The receipt's run log names it among the four cases, so the randomized test runner collected it. The body's count of 4 matches the receipt, so this is not a body item.
- N7. The test code comments at `TestRestoreCore.java` lines 265-266 and 323-324 say "in-memory". They are code comments, not body text, and changing them would be a code change and a new head. They are outside this verdict.

## Not done

No PR body edit, comment, review, close, submit-branch edit or Jira write. No build, Gradle, test, Selenium or gate run. The only fetch was the read-only fork fetch. Scratch copies of the body and draft were written to the session scratch folder, not the repository. This report is not committed; the lead commits it with the roll-up.
