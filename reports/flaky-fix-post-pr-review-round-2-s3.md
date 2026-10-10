# Flaky-fix post-PR review round 2, slice 3: SOLR-18532 (PR #5100)

Date of live reads: 2026-10-10. Scope: read-only check of draft PR #5100 on apache/solr against the fork branch solr-18532-submit at f1e5031fc3a9624b03daf353f26c977891d7d876. No build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. The only git action with a side effect was the read-only fetch of the fork branch refspec, which moved the local remote-tracking ref origin/solr-18532-submit to f1e5031. The live body was saved to the scratchpad only. No claim file was touched.

Line numbers below: "body line N" is the live body (LF copy, saved to scratchpad). "head" means the file at f1e5031 read with git show.

## Verdict

REMAINING. Items 1 to 4, 6, 7, 10 and 12 are satisfied. Items 5, 8, 9, 11 and 14 remain, and item 13 remains only through items 9 and 11.

Remaining items for the main side:

- **R1 (item 5, file links).** Three file citations are still plain code spans: "The change is in `RestoreCore.java`" (body line 15), "`StandardDirectoryFactory`" (body line 43) and "`changelog/unreleased/SOLR-18532.yml`" (body line 49). Each needs a link at f1e5031 with line anchors. The StandardDirectoryFactory anchor is head `solr/core/src/java/org/apache/solr/core/StandardDirectoryFactory.java` lines 130-148.
- **R2 (item 8, test count).** The body says "4 of 4" and the receipt says "4 tests". The head source has three `@Test` methods (TestRestoreCore.java lines 98, 190, 242), and the un-annotated `testBackupFailsMissingAllowPaths` (line 176) predates the branch. No record names the four testcases. The JUnit XML is not on disk, so only the gate host can name them. Add the four testcase names from the gate XML to the receipt.
- **R3 (item 9, "0 failures").** Body lines 27 and 28 say "4 of 4 pass" and "1 of 1 passes". The receipt says "4 tests, 0 failures" and "1 test, 0 failures" (receipt line 5), and records no error or skip counts. Use the receipt wording.
- **R4 (item 11, optional).** "instead of deleting it" remains in the live title and in changelog line 1 at head. The body does not repeat it. Changing the title is a live edit. Changing the changelog needs a new head and a gate decision. This was round 1 item s3b-D7, still open.
- **R5 (item 14, CI).** Three required workflows at f1e5031 are `action_required` and have not run: Solr Tests via Crave, Gradle Precommit and Validate Changelog. The changelog YAML has not been validated by CI. These are not failures, but the validation is incomplete.
- **Internal only (item 12).** `gates/SOLR-18532.md` has no record of the failed first gate at 07a7ead4783, which `receipts/SOLR-18532.md` line 7 records. WORKFLOW.md line 43 requires the gate file to record it. This is not in the public body.

Notes for the main side's judgment (not among the 14 items):

- The rollback-path sentence appears twice in the body (line 17 in "What this change does" and line 47 in Limits). The formula says a claim is not restated.
- The replacement sentence states as fact that the rollback path does not run in those tests. The round 1 record calls the same premise an inference (`reports/flaky-tests-root-cause-round-1-t1.md` line 39). The body is only as strong as the record.
- The live body is 4,602 characters, against the roughly 3,500-character guide in pr-formula.md. This is round 1 item S3-8d, still open.

## Item table

| # | Item | State at head | State in live body | Verdict |
|---|------|---------------|--------------------|---------|
| 1 | Causal sentence (S3-2) | Not a code claim. The record calls the premise an inference (t1 line 39). | Line 17: "This is a product fix. The rollback path does not run in those tests, so this change does not address the teardown failure seen in the install tests." Line 47 (Limits) repeats it without "seen in the install tests". The phrase "comes from another path" is absent (0 matches). | SATISFIED |
| 2 | Temp-file Limit (S3-3) | `RestoreCore.java` 300-307: temp name (300), `createOutput` (301-302), `writeBytes` (303), try-with-resources close (304), `sync` (305), `renameWithOverwrite` (306-307). No cleanup on any path. | Line 42: "If any step after the file is created fails, a temporary `index.properties.<number>` file can be left in the data directory. Nothing removes it." The phrase "If the write fails" is absent. | SATISFIED |
| 3 | Non-atomic fallback named (S3-4) | `StandardDirectoryFactory.java` 130-148: atomic `Files.move` with ATOMIC_MOVE and REPLACE_EXISTING (136-140); on `AtomicMoveNotSupportedException`, a non-atomic `Files.move` with REPLACE_EXISTING (142-145); non-FS base directory delegates to the base (148). `DirectoryFactory.java` 204-215: `deleteFile(toName)` (207) then `rename` (214). | Line 43: "On a factory that deletes and then renames, a failure between those two steps can leave no pointer. Only the in-memory factories use that fallback today; `StandardDirectoryFactory` has a similar non-atomic move when an atomic move is not supported. This is not new." Both fallbacks are named. The text does not say "Files.move" and does not give line anchors. | SATISFIED |
| 4 | Failure-wait Limit (S3-5, s3a-F5, s3a-D3) | `TestRestoreCore.java` 297-306: `expectThrows(AssertionError)` around a 10-iteration loop of `fetchRestoreStatus` then `Thread.sleep(50)` (300-304). The same loop is in `testFailedRestore` at 224-232. | Line 46: "The new test polls the restore status 10 times at 50 ms intervals while waiting for the failure, copied from the existing `testFailedRestore`. A slower failure would make the test fail rather than pass." | SATISFIED |
| 5 | File links at head SHA (S3-8a) | Links checked: `RestoreCore.java#L213-L226` (213 capture, 214 switch, 217-226 try/catch with rollback call at 226) and `TestRestoreCore.java#L242-L319` (242 `@Test`, 243 method, 319 closing brace). Both use f1e5031 and the right paths. | Line 15 has both links with the right SHA and lines. Bare file citations remain at line 15 ("The change is in `RestoreCore.java`"), line 43 (`StandardDirectoryFactory`) and line 49 (changelog path). | REMAINING (R1) |
| 6 | Choice question (S3-8b) | Not a code claim. | Line 34 ends: "Was saving the exact bytes the right call, or should the rollback set only the previous directory name again?" No "I can switch" offer. | SATISFIED |
| 7 | Limits summary line (S3-8c) | Not a code claim. | Line 38: "**The fix is proven on the in-memory test factory; a few rollback edges keep the old code's shape.**" Lines 7, 13, 23 and 32 also open with bold lines. | SATISFIED |
| 8 | Test count "4 of 4" (S3-6, s3b-D4) | `TestRestoreCore.java` has three `@Test` methods (98, 190, 242). `testBackupFailsMissingAllowPaths` (176) has no annotation. Base has the same un-annotated method (base line 168). | Line 27: "TestRestoreCore: 4 of 4 pass at f1e5031..., verified 2026-10-10." Receipt line 5: "TestRestoreCore 4 tests, 0 failures (fresh JUnit XML)." Body and receipt agree on 4. The receipt names no testcases, the XML is not on disk, and no record reconciles 3 annotated methods with 4 tests. | REMAINING (R2). Agreement: yes. Confirmation recorded: a claim of "fresh JUnit XML" only, not checkable here. |
| 9 | "0 failures" wording (S3-10) | Receipt line 5: "0 failures" for both classes. No error or skip counts recorded. | Lines 27-28: "4 of 4 pass" and "1 of 1 passes". "Pass" for every test goes one step past the record. | REMAINING (R3, minor) |
| 10 | SOLR-9865 wording (S3-9) | Title search on apache/solr for "SOLR-9865 in:title", all states, returned no PRs. | Line 45: "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name. Once this change lands, I will follow up on that branch by dropping its rollback edit, so the two do not conflict." The ticket is named. No "open change". No claim that a public PR exists. "That branch" means the fork branch and reads a little loosely. | SATISFIED |
| 11 | "Instead of deleting it" (S3-7, s3b-D7) | Changelog line 1 at head: "A failed restore or install now rolls back to the previous index.properties instead of deleting it, so the pointer to a non-default index directory is no longer lost." Code deletes when there was no prior file (`RestoreCore.java` 297-298). | Title (live): "SOLR-18532: RestoreCore rollback restores the previous index.properties instead of deleting it". The body does not repeat the phrase. | REMAINING (R4, optional) |
| 12 | Gate and receipt head record (s3b-D5, S3-11) | Trees: f1e5031 and 348dd63 are both `40a18b12e637e4c5f11313c4742df3f9685a84ca`. Match. | Gate file line 3 names head 348dd63 and line 8 says "GATE GREEN at 348dd63". Receipt line 1 says "GATE GREEN at f1e5031" and line 9 gives the gated commit 348dd63 with identical tree. Each record says which commit the run used. | SATISFIED for head naming. Internal note: the failed 07a7ead gate is not in the gate file (see Remaining items). |
| 13 | Title accuracy and Proof numbers | Title matches the rollback behavior (`RestoreCore.java` 297-314). The phrase "instead of deleting it" overstates it, as item 11 says. | Proof numbers match the receipt except wording (see the Title and Proof check table). | REMAINING only through R3 and R4 |
| 14 | CI and reviews | See the CI and review section. | Not a code claim. | REMAINING (R5) |

Round 1 item mapping: S3-1 is item 14; S3-2 is item 1; S3-3 is item 2; S3-4 is item 3; S3-5 is item 4; S3-6 is item 8; S3-7 is item 11; S3-8a is item 5; S3-8b is item 6; S3-8c is item 7; S3-8d is the length note; S3-9 is item 10; S3-10 is item 9; S3-11 is item 12.

## Title and Proof check

Live title: "SOLR-18532: RestoreCore rollback restores the previous index.properties instead of deleting it". Accurate for the rollback change at this head. It restores the saved bytes when a file existed and deletes only when none existed. The generalization is item 11.

| Body says | Receipt says | Result |
|-----------|--------------|--------|
| Line 27: "TestRestoreCore: 4 of 4 pass at f1e5031, verified 2026-10-10" | Line 5: "TestRestoreCore 4 tests, 0 failures"; gate file line 8 and receipt line 1 give the gate done date 2026-10-10 | Count agrees. Wording is item 9. Count confirmation is item 8. |
| Line 28: "RestoreCoreOpTest: 1 of 1 passes at the same head, verified 2026-10-10" | Line 5: "RestoreCoreOpTest 1 test, 0 failures" | Count agrees. The head has one `@Test` (`solr/core/src/test/org/apache/solr/handler/admin/RestoreCoreOpTest.java` line 27). Wording is item 9. |
| Lines 25-26: fails on base at the pointer check; "expected but null" | Line 3: "FAILS at the pointer check: ... expected but null, because the old rollback deletes the file" | Agrees. Base rollback deletes at base `RestoreCore.java` line 227. Failing assertion is at head `TestRestoreCore.java` 312-315. |
| Line 27: "at f1e5031" | Receipt header names f1e5031; line 9 gives the tree identity with 348dd63 | Agrees by tree identity. |
| No other numbers | Receipt has steps 0 to 5 rc=0 and seed; none are in the body | Nothing to compare. |

## CI and review state (read 2026-10-10)

- `gh pr view 5100` (fields as requested): state OPEN, isDraft true, headRefOid f1e5031fc3a9624b03daf353f26c977891d7d876, mergeStateStatus UNSTABLE, reviewDecision empty.
- statusCheckRollup: one entry, labeler, SUCCESS (run 38079478965).
- `gh pr checks 5100`: labeler pass only.
- Actions runs for head f1e5031 (REST, all event pull_request or pull_request_target, all completed, created 2026-10-10T19:21:31Z):
  - Pull Request Labeler: success (run 38079478965).
  - Solr Tests via Crave: action_required (run 38079479193). Not run.
  - Gradle Precommit: action_required (run 38079479167). Not run.
  - Validate Changelog: action_required (run 38079479268). Not run.
- Reviews (REST `repos/apache/solr/pulls/5100/reviews`): none.
- Inline review comments (REST `pulls/5100/comments`): none.
- Issue comments (REST `issues/5100/comments`): none. The GraphQL `gh pr view --comments` call failed on token scope (read:org, read:discussion), so the REST fallbacks were used.
- Requested reviewers: none recorded in the fields read.

## Automated findings

None. The labeler posted no review and no comment. There are no automated findings to verify as real or to reject. The only CI concern is R5, which is incomplete validation, not a code finding.

## Checked and not run

No build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. No claim, gate or receipt file was edited. The JUnit XML for the gate was not available and was not read. The scratchpad copy of the live body is a temporary file and is not part of the worktree.
