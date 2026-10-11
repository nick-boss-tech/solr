# Flaky-fix post-PR review round 8, slice 2: SOLR-18532 (PR #5100), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-8.md`, slice 2. Prior round: `reports/flaky-fix-post-pr-review-round-7-s2.md`. Head checked: `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. Reads dated 2026-10-10.

Scope: read-only. `git ls-remote origin refs/heads/solr-18532-submit` returned `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, equal to the PR head. The read-only fork fetch ran into `refs/remotes/origin/solr-18532-submit`. The live PR was read through `research/gh.ps1` only: `pr view`, `pr checks`, `run list`, and GET calls to the reviews, review comments and issue comments endpoints. No body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle, test, Selenium or gate run. The only file written is this report.

## Verdict

**STILL OPEN: one item.** The round 7 item is satisfied in the live body. "in-memory" is gone from the summary (line 36) and from the test-directory bullet (line 38). One "in-memory" claim remains, at line 41. The code does not establish it for the test directory factory.

Remaining item. Apply the same edit in the live body and in `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare` (line 41 in both):

- Line 41, current: "Only the in-memory factories use that fallback today;"
- Fact: the factories that reach the delete-then-rename fallback at head are the three that extend `EphemeralDirectoryFactory`: `ByteBuffersDirectoryFactory`, `RAMDirectoryFactory` and `MockDirectoryFactory`. The first two are in-memory. `MockDirectoryFactory` opens `LuceneTestCase.newDirectory()`, and the build sets `tests.directory=random`, so its directory is not always in-memory. The same reasoning as round 6 and round 7 applies: the Ephemeral family name does not fix the directory type.
- Fix: replace the words "the in-memory factories" with "the factories that extend `EphemeralDirectoryFactory`", so the sentence reads "Only the factories that extend `EphemeralDirectoryFactory` use that fallback today;". The rest of the bullet stays.
- Why "only" still holds: `StandardDirectoryFactory.renameWithOverwrite` calls the default only when the base directory is not an `FSDirectory` (line 148). Its `create()` (line 52) returns `FSDirectory.open(...)`, so no factory at head reaches that call.

## Item table

| # | Item | Live wording (line) | Source check at 7dfd3d98 | Verdict |
|---|---|---|---|---|
| 1 | Round 7: "in-memory" dropped from the summary | Line 36: "**The fix is proven on the test directory factory; a few rollback edges keep the old code's shape.**" | "in-memory" is gone. The factory is `MockDirectoryFactory` (item 2). | SATISFIED |
| 2 | Round 7: test directory factory named, "in-memory" dropped from the Limits bullet | Line 38: "The proof runs on the test directory factory (MockDirectoryFactory), which opens Lucene's randomized test directory." | `solr/test-framework/src/java/org/apache/solr/core/MockDirectoryFactory.java` lines 29-30 (javadoc "Opens a directory with LuceneTestCase#newDirectory()"; class extends `EphemeralDirectoryFactory`) and 47-50 (`create()` calls `newMockDirectory()` or `newDirectory()`). `gradle/testing/randomization.gradle` line 100: `tests.directory` value "random". `solrconfig-leader.xml` line 22 sets the default factory to `solr.MockDirectoryFactory`, and `TestRestoreCore.java` lines 72-76 load that config. The wording is accurate. | SATISFIED |
| 3 | Round 7: no other "in-memory" claim in the body | Line 41: "Only the in-memory factories use that fallback today;" | See the verdict. The only remaining "in-memory" in the body. | STILL OPEN |
| 4 | Atomic move not run by these tests (line 38, second sentence) | "The atomic file move used by the file-system factory is not run by these tests." | `MockDirectoryFactory` extends `EphemeralDirectoryFactory`, so `DirectoryFactory.renameWithOverwrite` (lines 204-215) applies. `StandardDirectoryFactory.renameWithOverwrite` (lines 130-150) is not reached. | SATISFIED |
| 5 | Title | "SOLR-18532: RestoreCore rollback restores the previous index.properties" | The rollback writes back the saved bytes (`RestoreCore.java` lines 226 and 288-314), or deletes the file when none existed (lines 297-298). The title says that. | SATISFIED |
| 6 | Bold one-line summary on every section | Bold lines 7, 13, 21, 30, 36 | Five sections, each opening with one bold line. The "AI assistance" footer has none; it is the approved template text. | SATISFIED |
| 7 | Choice section ends with a pointed question | Line 32 ends: "Was saving the exact bytes the right call, or should the rollback set only the previous directory name again?" | Ends with a question. See note N2. | SATISFIED |
| 8 | Proof numbers come from the receipt | Lines 25 and 26 | See the Proof and count check. | SATISFIED |
| 9 | Test count and the four test names | Line 25: "4 tests". Line 23 names the new test. | The receipt's step 4 lists four names. All four exist in `solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java` at head: `testSimpleRestore` (line 99), `testBackupFailsMissingAllowPaths` (176), `testFailedRestore` (191), `testFailedRestoreKeepsNonDefaultIndexPointer` (243). | SATISFIED |
| 10 | No internal vocabulary | Whole body | No gate, receipt, ledger, log name, run identifier, seed, claim, takeover, round or pool wording. Hits on "check", "step", "path" and "runs" are ordinary English. | SATISFIED |
| 11 | No em dashes | Whole body | Zero. | SATISFIED |
| 12 | "No public method changes" (line 15) | Line 15 | The base-to-head diff of `RestoreCore.java` adds or removes no line containing "public". | SATISFIED |
| 13 | Read-error behavior change | Line 17: "a read error other than "file not found" now stops the restore before the switch. Before, that error was ignored." | Head `readIndexProperties()` (`RestoreCore.java` 256-281) returns null only for `FileNotFoundException` and `NoSuchFileException`. Other errors propagate. It runs at line 213, before `modifyIndexProps` at line 214. Base `SolrCore.writeNewIndexProps` (lines 1516-1518) ignores an `IOException` from `openInput`, and lines 1511-1512 log load errors. The claim holds. | SATISFIED |
| 14 | Changelog file and title | Line 47 links `changelog/unreleased/SOLR-18532.yml` L1-L7 | The file exists at head and has 7 lines. Title: "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." The title is accurate and does not say the file is never deleted. | SATISFIED |
| 15 | Limits bullets | Lines 38-45 | See the Limits check. Line 41 has the open wording item; its facts hold. | STILL OPEN (line 41 wording only) |

## Limits check

| Live line | Limit | Source check at 7dfd3d98 | Verdict |
|---|---|---|---|
| 38 | Test directory factory (MockDirectoryFactory) | Item 2. | SATISFIED |
| 38 | Atomic file move not run | Item 4. | SATISFIED |
| 39 | Rollback failure | `RestoreCore.java` line 226 is the first statement in the catch. If `restoreIndexProperties` throws, lines 228-231 (`doneWithDirectory`, `remove`, `newIndexWriter`, `openNewSearcher`) are skipped. The base code's `dir.deleteFile` (base line 227, inside try/finally) has the same skip of base lines 234-237. The claim "the writer is not reopened and the restore directory is not removed" holds. | SATISFIED |
| 40 | Temporary file left behind | Lines 300-307: the temp name is `index.properties.` plus `System.nanoTime()`; the code creates it, writes, syncs, then renames. Nothing deletes it on failure. `SolrCore.modifyIndexProps` (lines 1474-1476) uses the same pattern. | SATISFIED |
| 41 | Non-atomic fallback (facts) | `DirectoryFactory.renameWithOverwrite` (lines 204-215) deletes, then renames. Only `StandardDirectoryFactory` (lines 130-150) overrides it: atomic move at 136-140, non-atomic fallback at 141-145, super call at 148 (see the verdict for why no factory reaches 148). The three Ephemeral subclasses do not override it. "This is not new": the default is in the base code, and `SolrCore` line 1476 uses it. | Facts SATISFIED; wording STILL OPEN (see verdict) |
| 42 | Pointer checked, file absence not checked | Lines 297-315 assert the pointer only. Lines 317-318 check the documents. | SATISFIED |
| 43 | SOLR-9865 follow-up | The `solr-9865-submit` tip on `origin` (`ls-remote`) is `4937608bb181efae104c0d6f0257f445af50bf52`, the commit checked. Its rollback (lines 231-236) deletes only when the previous directory is "index", and otherwise calls `core.modifyIndexProps(previousIndexDirName)`. The claim "writes back only the previous directory name" holds. The sentence "I will follow up" is note N4. | SATISFIED (facts) |
| 44 | Failure-wait limit | Lines 300-304: a loop of 10 iterations with `Thread.sleep(50)` inside `expectThrows(AssertionError.class, ...)`. `testFailedRestore` (lines 227-230) has the same loop. If no assertion arrives, `expectThrows` fails the test. | SATISFIED |
| 45 | Install-tests bullet | "This change does not address the teardown failure seen in the install tests." Ends at "install tests." with no causal clause. | SATISFIED |
| 17 | Read-error behavior change | Item 13. | SATISFIED |

Install path used by line 7: `solr/core/src/java/org/apache/solr/handler/admin/api/InstallCoreData.java` lines 91-92 call `RestoreCore.create` and `doRestore`.

## Citation check

All six links use the full head SHA `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. Each range was read with `git show <sha>:<path>` and `cat -n`.

| Link (live line) | Range | Lines at head | Verdict |
|---|---|---|---|
| `RestoreCore.java` L213-L226 (line 15, "reads the file's bytes") | 213-226 | 213 `readIndexProperties()` capture; 226 `restoreIndexProperties(previousIndexProps)` in the catch | SATISFIED |
| `RestoreCore.java` L213-L314 (line 15, "The change is in") | 213-314 | Read call, switch, catch and rollback call (213-226); read helper (256-281); write-back helper (288-314) with the delete branch (297-298) and the temp rename (300-307) | SATISFIED. The range reaches the write-back and delete code. |
| `RestoreCore.java` L213-L226 (line 23, Proof) | 213-226 | The capture and rollback call sites that the change alters | SATISFIED |
| `TestRestoreCore.java` L242-L319 (line 15) | 242-319 | 242 `@Test`; 243 the new method; 319 its closing brace | SATISFIED |
| `StandardDirectoryFactory.java` L130-L148 (line 41) | 130-148 | 130 method start; 141-145 the non-atomic move the claim names; 148 super call | SATISFIED |
| `changelog/unreleased/SOLR-18532.yml` L1-L7 (line 47) | 1-7 | The whole fragment (7 lines) | SATISFIED |

## Proof and count check

| Body line | Body text | Receipt (`receipts/SOLR-18532.md` on origin/pr-prepare) | Head check | Verdict |
|---|---|---|---|---|
| 25 | TestRestoreCore: 4 tests, 0 failures at `f1e5031...`, verified 2026-10-10 | Step 4: "TestRestoreCore 4 tests, 0 failures"; gate done 2026-10-10T18:31:33Z | Four test methods at head (see item 9) | SATISFIED (see N1) |
| 26 | RestoreCoreOpTest: 1 test, 0 failures, same head | Step 4: "RestoreCoreOpTest 1 test, 0 failures" | `solr/core/src/test/org/apache/solr/handler/admin/RestoreCoreOpTest.java` has one test method (line 28) | SATISFIED |
| 23 | With the base RestoreCore, the new test fails at the pointer check, pointer null | Step 3: fails at the pointer check, pointer null, because the old rollback deletes the file | Pointer assertion at lines 312-315; base rollback deletes at base line 227 | SATISFIED |

No test or build was run. The counts come from the receipt, as the formula requires.

## Body vs draft

Live body (`gh.ps1 pr view 5100 --json body`) against `pr-drafts/flaky-fixes/SOLR-18532.md` on origin/pr-prepare, with CR characters stripped. The wording is identical: 51 lines each, and no word, link or number differs. The only difference in the diff is one blank line at the end of the captured live file, which the capture added. The JSON body ends with one newline, the same as the draft. Line 41 carries the same "in-memory" phrase in both copies, so the item 3 fix goes into both.

## CI and review state

- PR #5100: OPEN, draft, head branch `solr-18532-submit`, `headRefOid` `7dfd3d98d0a2f016c520139cbca96ea4a49f6689` (equal to the fork tip). `mergeStateStatus` UNSTABLE. `reviewDecision` empty.
- `statusCheckRollup` (one entry): `labeler` (Pull Request Labeler), COMPLETED, SUCCESS, completed 2026-10-10T20:45:09Z. `pr checks` shows the same single entry as pass.
- Actions runs on head 7dfd3d98 (`run list`, branch solr-18532-submit):
  - Validate Changelog (run 38084962187, pull_request): action_required.
  - Gradle Precommit (run 38084962170, pull_request): action_required.
  - Solr Tests via Crave (run 38084962168, pull_request): action_required.
  - Pull Request Labeler (run 38084960068, pull_request_target): success.
- `action_required` is a held run, not a failed one. None of the three pull_request runs has executed for this head, so the changelog YAML, precommit and tests have no CI result yet on this head.
- Reviews (`repos/apache/solr/pulls/5100/reviews`): none. Review comments (`pulls/5100/comments`): none. Issue comments (`issues/5100/comments`): none.

## Verified and rejected automated findings

None. PR #5100 has no automated comment, review, review comment or check annotation at this head. The labeler check is a labeling step and reports no findings. Nothing to verify or reject.

## Notes (not verdict items)

- N1. The Proof names `f1e5031fc3a9624b03daf353f26c977891d7d876` (the gate head), while the PR head is `7dfd3d98`. `git diff --stat f1e5031 7dfd3d98` changes only `changelog/unreleased/SOLR-18532.yml` (one line). The code tree is the same. The formula asks for "verified <date> at this head". Optional wording: "(same code as this head; only the changelog title differs)". Raised in rounds 5, 6 and 7; unchanged.
- N2. The formula's choice template asks for "the alternative's cost". The "A choice to check" section (lines 30-32) says both routes fix the tested case but names no cost of the other route. This is not part of the round 7 item. If the lead wants it, the cost must be taken from the code, not assumed.
- N3. The body is 5,107 characters against the formula's guide of about 3,500. Carried from earlier rounds.
- N4. Line 43: "Once this change lands, I will follow up on that branch by dropping its rollback edit". This is a public commitment by Nick. No file on origin/pr-prepare that I read records that Nick confirmed it. Rounds 5, 6 and 7 asked for that confirmation. It is the lead's call; the facts in the sentence hold (item 43).
- N5. The test code comments at `TestRestoreCore.java` lines 265-266 and 323-324 at head say "in-memory". They are code comments, not body text. Changing them would be a code change and a new head. They share the cause of item 3.
- N6. `testBackupFailsMissingAllowPaths` (line 176) has no `@Test` annotation at head; base line 168 is the same. It predates this diff. The receipt counts it among the four cases, and the body's count of 4 matches the receipt.
- N7. Line 43 names SOLR-9865 without a link. Optional: `https://issues.apache.org/jira/browse/SOLR-9865`.

## Not done

No PR body edit, comment, review, close, submit-branch edit or Jira write. No build, Gradle, test, Selenium or gate run. The only fetch was the read-only fork fetch. The report is not committed; the lead commits it with the roll-up.
