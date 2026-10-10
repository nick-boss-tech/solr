# Flaky-fix post-PR review round 7, slice 2: SOLR-18532 (PR #5100), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-7.md`, slice 2. Prior round: `reports/flaky-fix-post-pr-review-round-6-s3.md`. Reads dated 2026-10-10.

Scope: the fork branch `solr-18532-submit`. `git ls-remote origin refs/heads/solr-18532-submit` returned `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, which equals the PR head (`headRefOid`). The commit objects were already in the worktree, so no fetch was run. The live PR was read through `research/gh.ps1` only (`pr view`, `pr checks`, `run list`, and GET calls to `api`). `pr view --comments` failed with a token scope error (`read:org`), so the API fallback was used for reviews and comments. No body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle, test, Selenium or gate run. The only file written is this report.

## Verdict

**STILL OPEN: one item.** The round 6 citation item is satisfied in the live body and in the draft. The final read finds one accuracy problem in Limits: the word "in-memory" is not established by the code at the head. It appears on live lines 36 and 38.

Remaining item. Apply the same wording change in the live body and in `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare`:

- Line 36 (bold summary). Current: "The fix is proven on the in-memory test factory; a few rollback edges keep the old code's shape." Fix: replace "in-memory test factory" with "test directory factory".
- Line 38 (bullet). Current: "The proof runs on the in-memory test directory factory. The atomic file move used by the file-system factory is not run by these tests." Fix: replace the first sentence with "The proof runs on the test directory factory (MockDirectoryFactory), which opens Lucene's randomized test directory." Keep the second sentence.

Why. `MockDirectoryFactory.create()` (`solr/test-framework/src/java/org/apache/solr/core/MockDirectoryFactory.java`, lines 47 to 50 at the head) opens `LuceneTestCase.newDirectory()` or `newMockDirectory()`. `gradle/testing/randomization.gradle` line 100 sets `tests.directory` to `random`. The checkout does not fix the directory type, and Lucene's choice is outside the checkout (round 6 s3 N5 made the same point). Round 6 accepted "in-memory" because the factory is in the Ephemeral family. That family name does not pin the type of the directory this test receives. The second sentence holds regardless: `MockDirectoryFactory` extends `EphemeralDirectoryFactory` and never reaches `StandardDirectoryFactory.renameWithOverwrite`.

The test code has the same word at `solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java` lines 265 and 324. A body-only fix leaves those comments as they are. Changing them is a code change and a new head, which is the lead's call.

Everything else in the slice is SATISFIED.

## Item table

| # | Item | Live wording | Source check | Verdict |
|---|---|---|---|---|
| 1 | Round 6 item: range on the "The change is in" link (live line 15) | `[RestoreCore.java](https://github.com/nick-boss-tech/solr/blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689/solr/core/src/java/org/apache/solr/handler/RestoreCore.java#L213-L314)`. At round 6 this was `#L213-L226`. | Head lines 213 (read call), 226 (rollback call), 252 to 281 (read helper), 288 to 314 (write-back helper), 297 to 298 (delete branch) and 306 to 307 (rename) all fall in 213 to 314. The sentence "The file is deleted only when..." has its line (297 to 298) in range. | SATISFIED |
| 2 | Draft carries the same link | `pr-drafts/flaky-fixes/SOLR-18532.md` line 15, second link: `#L213-L314` | The draft and the live body are identical after CR strip (Body vs draft). | SATISFIED |
| 3 | Every file citation is a link at the head SHA | Six links, all `blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689` | See Citation check. | SATISFIED |
| 4 | Title | "SOLR-18532: RestoreCore rollback restores the previous index.properties" | At head the rollback writes back the saved bytes, or deletes the file when none existed (297 to 308). The title says that. | SATISFIED |
| 5 | Bold one-line summary on every section | Bold lines 7, 13, 21, 30, 36 | Five sections, each opens with one bold line. The "AI assistance" footer has none. It is approved template text (N1). | SATISFIED |
| 6 | Choice section ends with a pointed question | Line 32 ends "...or should the rollback set only the previous directory name again?" | Ends with a question. The alternative route is the one in SOLR-9865 (see Limits line 43). | SATISFIED |
| 7 | Every Proof number is in the receipt | Line 25: "4 tests, 0 failures at `f1e5031...`, verified 2026-10-10". Line 26: "1 test, 0 failures at the same head". | `receipts/SOLR-18532.md` step 4: "TestRestoreCore 4 tests, 0 failures" and "RestoreCoreOpTest 1 test, 0 failures"; gate done 2026-10-10T18:31:33Z. | SATISFIED (N2) |
| 8 | Test count sentence and four test names | Count "4 tests" (line 25). The new test is named at line 23. | Head `TestRestoreCore.java` has four test methods: `testSimpleRestore` (99), `testBackupFailsMissingAllowPaths` (176), `testFailedRestore` (191), `testFailedRestoreKeepsNonDefaultIndexPointer` (243). The four names match the receipt. `RestoreCoreOpTest.java` has one test (line 28). | SATISFIED (N6) |
| 9 | No internal vocabulary | Scan of the body | No gate, receipt, ledger, log name, run identifier, seed, claim or takeover. Hits on "path", "pattern", "runs" and "agents" are ordinary English. | SATISFIED |
| 10 | No em dashes | Scan of the body | None. | SATISFIED |
| 11 | Changelog file and title | `changelog/unreleased/SOLR-18532.yml` (7 lines). Title: "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." | File exists at head. Title is accurate. It does not read as never deleting a file. Author is "Nick Shanin", no placeholder. | SATISFIED |
| 12 | "No public method changes" (line 15) | Line 15 | The base-to-head diff of `RestoreCore.java` has no added or removed line containing `public`. The three matches are hunk headers. | SATISFIED |
| 13 | Limits: in-memory factory (lines 36 and 38) | "in-memory test factory" (36); "in-memory test directory factory" (38) | See Limits check. The code does not fix the directory as in-memory. | STILL OPEN |
| 14 | Limits: rollback-failure limit (line 39) | "If writing the saved bytes back fails, the error stops the rest of the rollback..." | Head line 226 is inside the catch. An IOException there skips 228 to 231. Base line 227 has the same shape. | SATISFIED |
| 15 | Limits: temp-file limit (line 40) | "...a temporary `index.properties.<number>` file can be left..." | Head 300 to 307 writes the temp file, then syncs and renames. Nothing removes it on failure. `SolrCore.modifyIndexProps` (1474 to 1476) has the same pattern. | SATISFIED |
| 16 | Limits: non-atomic fallback (line 41) | "On a factory that deletes and then renames..." | `DirectoryFactory.renameWithOverwrite` (204 to 215) deletes, then renames. The Ephemeral family uses it. `StandardDirectoryFactory` (141 to 145) has the non-atomic fallback. `SolrCore` line 1476 already uses this rename, so "not new" holds. | SATISFIED |
| 17 | Read-error behavior change (line 17) | "a read error other than 'file not found' now stops the restore before the switch. Before, that error was ignored." | Head `readIndexProperties` (256 to 281) rethrows any IOException except the not-found pair. It runs at 213, before `modifyIndexProps` at 214. Base `SolrCore.writeNewIndexProps` (1505 to 1518) swallows the openInput IOException (1516) and logs load errors (1511). | SATISFIED |
| 18 | Limits: failure-wait limit (line 44) | "polls the restore status 10 times at 50 ms intervals..." | Head 300 to 303 is the same 10 by 50 ms loop as 227 to 230 in `testFailedRestore`. If no AssertionError arrives, `expectThrows` fails the test. | SATISFIED |
| 19 | Limits: install-tests bullet (line 45) | "This change does not address the teardown failure seen in the install tests." | Ends at "install tests." with no causal clause. | SATISFIED |
| 20 | Limits: SOLR-9865 follow-up (line 43) | "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name." | Checked against the SOLR-9865 commit `4937608bb181efae104c0d6f0257f445af50bf52` (the ls-remote tip of `solr-9865-submit`, present locally). Its rollback (lines 231 to 236) deletes only when the directory is "index", otherwise calls `core.modifyIndexProps(previousIndexDirName)`. The claim holds. The "I will follow up" sentence is N4. | SATISFIED |
| 21 | Limits: not checked (line 42) | "The test checks the pointer after the rollback. It does not check that the file is absent..." | Head 297 to 315 assert only the pointer. Head 317 to 318 check the docs. | SATISFIED |
| 22 | "What happens today" facts (lines 7 and 9) | "...the core then opens the default `index` directory instead." | Head `SolrCore.getIndexPropertyFromPropFile` (441 to 464) returns `dataDir + "index/"` when the file is absent. `admin/api/InstallCoreData.java` lines 91 to 92 call `RestoreCore.create` and `doRestore`, so install shares the path. | SATISFIED |

## Limits check

| Live line | Limit | Head check | Verdict |
|---|---|---|---|
| 38 | In-memory factory: "The proof runs on the in-memory test directory factory." | `solrconfig-leader.xml` line 22 sets `${solr.directoryFactory:solr.MockDirectoryFactory}`. `TestRestoreCore` loads that config (lines 72 to 76). `MockDirectoryFactory` extends `EphemeralDirectoryFactory`. Its `create()` uses `LuceneTestCase.newDirectory()`, and the build sets `tests.directory=random` (`gradle/testing/randomization.gradle` line 100). The head does not make the directory in-memory. | STILL OPEN (item 13) |
| 38 | Atomic move: "The atomic file move used by the file-system factory is not run by these tests." | `MockDirectoryFactory` never calls `StandardDirectoryFactory.renameWithOverwrite`, whatever the directory type. | SATISFIED |
| 39 | Rollback failure | Head 226 in the catch; 228 to 231 skipped. Base 227 has the same shape. | SATISFIED |
| 40 | Temp file | Head 300 to 307; no cleanup on failure. | SATISFIED |
| 41 | Non-atomic fallback | `DirectoryFactory` 204 to 215; `StandardDirectoryFactory` 130 to 150 (atomic move 136 to 140, fallback 141 to 145, super call 148 for non-filesystem base directories). Ephemeral subclasses are the in-memory factories. The head has no other DirectoryFactory class. | SATISFIED |
| 17 | Read-error change | See item 17. | SATISFIED |
| 44 | Failure wait | See item 18. | SATISFIED |
| 45 | Install-tests bullet | See item 19. | SATISFIED |
| 43 | SOLR-9865 follow-up | See item 20. | SATISFIED |

## Citation check

All six links use the full head SHA `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. Each range was read with `git show <sha>:<path> | cat -n | sed -n`.

| Link (live line) | Lines at head | Content at those lines | Verdict |
|---|---|---|---|
| `RestoreCore.java` L213-L226 (15, "reads the file's bytes") | 213 `readIndexProperties()` call; 226 `restoreIndexProperties(previousIndexProps)` in the catch | Both lines named by the claim (read, and write back). | SATISFIED |
| `RestoreCore.java` L213-L314 (15, "The change is in") | 213 to 314 | Read call, rollback call, read helper (252 to 281), write-back helper (288 to 314), delete branch (297 to 298). The imports at 19, 24 and 42 are outside the range; they are not the claim. | SATISFIED |
| `RestoreCore.java` L213-L226 (23, Proof, "reverted to the base commit") | 213 to 226 | The capture and rollback call sites, the lines the change alters in the rollback. | SATISFIED |
| `TestRestoreCore.java` L242-L319 (15, "a new test") | 242 `@Test`; 243 method; 319 closing brace | The new test. The test-only helper (321 on) is not described in the body. | SATISFIED |
| `StandardDirectoryFactory.java` L130-L148 (41) | 130 `renameWithOverwrite`; 136 to 140 atomic move; 141 to 145 non-atomic fallback; 148 super call | The claim, "a similar non-atomic move when an atomic move is not supported", is at 141 to 145. | SATISFIED |
| `changelog/unreleased/SOLR-18532.yml` L1-L7 (47) | File is 7 lines | The whole changelog fragment. | SATISFIED |

## Proof and count check

| Body (Proof, lines 23 to 26) | Receipt (`receipts/SOLR-18532.md` on `origin/pr-prepare`) | Head check | Result |
|---|---|---|---|
| `TestRestoreCore`: 4 tests, 0 failures at `f1e5031...`, verified 2026-10-10 | Step 4: "TestRestoreCore 4 tests, 0 failures"; gate done 2026-10-10T18:31:33Z | `git diff --stat f1e5031 7dfd` changes only `changelog/unreleased/SOLR-18532.yml` (1 line). The code tree is the same. | SATISFIED (N2) |
| `RestoreCoreOpTest`: 1 test, 0 failures, same head | Step 4: "RestoreCoreOpTest 1 test, 0 failures" | One test at head (line 28). | SATISFIED |
| New test fails on base at the pointer check | Step 3: with RestoreCore reverted, fails at the pointer check, pointer null | Head assertion at 312 to 315 (`assertEquals`) on the pointer. | SATISFIED |
| Test count and four names | Receipt names four: testSimpleRestore, testBackupFailsMissingAllowPaths, testFailedRestore, testFailedRestoreKeepsNonDefaultIndexPointer | Four test methods at head (99, 176, 191, 243). | SATISFIED |
| Old rollback deletes the file | Receipt step 3 | Base line 227 `dir.deleteFile(IndexFetcher.INDEX_PROPERTIES)`. | SATISFIED |

No test or build was run. The counts are taken from the receipt, as the formula requires.

## Body vs draft

- Live body: `gh.ps1 pr view 5100 --json body --jq .body`, saved to the scratchpad. The JSON body has no CR.
- Draft: `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare` (51 lines, no CR).
- My file capture added a byte-order mark and CRLF endings, and one trailing blank line. I stripped the BOM and CRs. The only remaining difference is that trailing blank line, which is from the capture (the live body ends with one newline, like the draft).
- Result: identical wording, including line 1 (the emoji), the Jira line, the link on line 15 (`#L213-L314` in both copies) and the changelog link. The item 13 fix must go into both copies.

## CI and review state

- PR #5100: OPEN, draft (`isDraft: true`). `headRefOid` `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, equal to the fork tip. `mergeStateStatus` UNSTABLE. `reviewDecision` empty.
- `statusCheckRollup` (one entry): `labeler` (Pull Request Labeler), SUCCESS, completed 2026-10-10T20:45:09Z.
- Actions runs for head 7dfd (`gh run list --branch solr-18532-submit`, event `pull_request`):
  - Validate Changelog (run 38084962187): action_required.
  - Gradle Precommit (run 38084962170): action_required.
  - Solr Tests via Crave (run 38084962168): action_required.
  - Pull Request Labeler (run 38084960068, `pull_request_target`): success.
- `action_required` means the runs await maintainer approval. It is a state, not a code failure. CI has not validated the changelog YAML.
- Reviews (`repos/apache/solr/pulls/5100/reviews`): none. Review comments (`pulls/5100/comments`): none. Issue comments (`issues/5100/comments`): none.

## Verified and rejected automated findings

None. PR #5100 has no automated comment, review, review comment or annotation. The labeler check only applies labels, so there was no finding to verify or reject.

## Notes (not verdict items)

- N1. The "AI assistance" footer has no bold summary line. It is the approved template text in `pr-formula.md`. Same as round 6 s3 N1.
- N2. The Proof names `f1e5031fc3a9624b03daf353f26c977891d7d876`, while the PR head is `7dfd3d98`. The only delta is the changelog title. The formula asks for "verified <date> at this head". An optional wording: "(same code as this head; only the changelog title differs)". Round 5 N1 and round 6 N2 raised this; it is unchanged.
- N3. The body is 5,046 characters, against the formula's guide of about 3,500. Carried from round 2 S3-8d.
- N4. The sentence "Once this change lands, I will follow up on that branch by dropping its rollback edit" is a public commitment by Nick. No file I read records that he confirmed it. Round 5 N4 and round 6 N4 asked for that confirmation, and it is still open. The SOLR-9865 facts were checked against the commit, not only the draft.
- N5. The test comments at `TestRestoreCore.java` lines 265 to 266 and 323 to 324 say "in-memory". The same caveat as item 13 applies.
- N6. `testBackupFailsMissingAllowPaths` (line 176) has no `@Test` annotation at head, and the base has the same at line 168. That is pre-existing and not part of this diff. The receipt counts it among the four tests run.
- N7. "SOLR-9865" is named without a link. An optional link is `https://issues.apache.org/jira/browse/SOLR-9865`.

## Not done

No PR body edit, comment, review, close, submit-branch edit or Jira write. No build, Gradle, test, Selenium or gate run. No fetch. The report is not committed; the lead commits it with the roll-up.
