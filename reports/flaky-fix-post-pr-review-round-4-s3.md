# Flaky-fix post-PR review round 4, slice 3: SOLR-18532 (PR #5100), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-4.md`, slice 3. Lead: the windows review agent. Reads dated 2026-10-10.

Scope: PR #5100 on apache/solr, draft, OPEN. Head `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. `git ls-remote origin refs/heads/solr-18532-submit` returned the same SHA before any read. The read-only fork fetch was run, and the commit is present locally.

Read-only throughout. No build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. The only GitHub calls were reads through `research/gh.ps1`.

## Verdict

**STILL OPEN: one item, a body-only wording fix in the last Limits bullet.** R1a and R1b are SATISFIED. The rest of the final read is SATISFIED.

Remaining item:

- **Limits, last bullet (live body line 47; draft line 47).** Live text: "The rollback path does not run in those tests, so this change does not address the teardown failure." The phrase "those tests" has no antecedent in the Limits list. The bullets just above it describe the new test, `testFailedRestoreKeepsNonDefaultIndexPointer`. Read that way the sentence is false. The Proof (live line 25) says the old rollback runs in that test and deletes the file: "because the old rollback deletes the file." The sentence is only meant for the install tests, which "What this change does" (line 17) names. Fix: replace the bullet with "- This change does not address the teardown failure seen in the install tests, because the rollback path does not run there." Keep the same claim. It rests on the round 1 slice 3 record (`reports/flaky-fix-post-pr-review-round-1-s3.md`, S3-2), which calls the install-test cause an inference, so do not make it stronger.

Round 2 marked this Limits line SATISFIED. I do not carry that over, because the Proof makes the nearer reading false.

## Item table

| # | Item | State at head | State in live body | Verdict |
|---|------|---------------|--------------------|---------|
| 1 | R1a: StandardDirectoryFactory link (Limits, bullet 4, line 43) | `StandardDirectoryFactory.java` 130-148 is `renameWithOverwrite`: atomic `Files.move` 136-140, non-atomic fallback 141-145, base delegate 148 | Linked at 7dfd, L130-L148 (quote below) | SATISFIED |
| 2 | R1b: RestoreCore.java links at PR head | 213 `readIndexProperties()` capture, 226 `restoreIndexProperties(...)` rollback call | Three links (line 15 twice, line 25 once), all at 7dfd, L213-L226 | SATISFIED |
| 3 | R1b: TestRestoreCore.java link at PR head | 242 `@Test`, 243 new test method, 319 closing brace | Linked at 7dfd, L242-L319 | SATISFIED |
| 4 | Changelog link (`changelog/unreleased/SOLR-18532.yml`) | File exists, 7 lines | Linked at 7dfd, L1-L7 | SATISFIED |
| 5 | Title accuracy | Rollback writes back the saved bytes, or deletes when no file existed before | "SOLR-18532: RestoreCore rollback restores the previous index.properties" | SATISFIED |
| 6 | Changelog title; no "never deleting" reading | Line 1: "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." | Same wording in the fragment; no "never deletes" reading | SATISFIED |
| 7 | "install" in the title-level claims | `admin/api/InstallCoreData.java` lines 91-92 call `RestoreCore.create(...)` and `doRestore()` | "restore or install" holds | SATISFIED |
| 8 | Proof counts match receipt | Receipt line 5: "TestRestoreCore 4 tests, 0 failures"; "RestoreCoreOpTest 1 test, 0 failures" | Same counts (see Proof check) | SATISFIED |
| 9 | Four test case names in the receipt exist | `testSimpleRestore` 99, `testBackupFailsMissingAllowPaths` 176, `testFailedRestore` 191, `testFailedRestoreKeepsNonDefaultIndexPointer` 243 | n/a (receipt item) | SATISFIED |
| 10 | Limits: in-memory factory | Test factory is `solr.MockDirectoryFactory` by default (`solrconfig-leader.xml` line 22); `TestRestoreCore` never calls `useFactory`. The FS move in StandardDirectoryFactory is not reached. | Line 40 | SATISFIED (see note N5) |
| 11 | Limits: rollback failure | Line 226 runs inside the catch before lines 228-231 (`doneWithDirectory`, `remove`, `newIndexWriter`, `openNewSearcher`). The base delete (base line 227) had the same shape. | Line 41 | SATISFIED |
| 12 | Limits: temp-file | `restoreIndexProperties` creates `index.properties.<nanoTime>` (line 300-302) and has no cleanup. `SolrCore.modifyIndexProps` (line 1474) uses the same tmp-name pattern. | Line 42 | SATISFIED |
| 13 | Limits: non-atomic fallback | `DirectoryFactory.renameWithOverwrite` (lines 204-215) is delete then rename. Only the Ephemeral family (ByteBuffers, RAM, Mock) inherits it. `StandardDirectoryFactory` overrides it. `SolrCore.modifyIndexProps` already uses it on the forward switch (line 1476), so "not new" holds. | Line 43 | SATISFIED |
| 14 | Limits: test checks pointer only | No test covers the no-prior-file path (`previousIndexProps == null`) | Line 44 | SATISFIED |
| 15 | Limits: SOLR-9865 follow-up | The 9865 draft (`pr-drafts/replication-backup/SOLR-9865.md` line 13) says its rollback "writes back the directory that index.properties named before the restore." The head diff has no such change. | Line 45 | SATISFIED |
| 16 | Limits: failure-wait | Lines 300-303 poll 10 times with `Thread.sleep(50)`, the same loop as lines 227-230 in `testFailedRestore` | Line 46 | SATISFIED |
| 17 | Read-error behavior change | `readIndexProperties` rethrows any IOException except file-not-found, before `modifyIndexProps`, so the restore stops before the switch. Base `SolrCore.writeNewIndexProps` ignored the read errors (line 1517 comment, line 1512 "Unable to load" log). | Line 19 | SATISFIED |
| 18 | Limits: last bullet ("those tests") | See the Verdict. Antecedent unclear; under the nearer reading the Proof contradicts it. | Line 47 | STILL OPEN |
| 19 | "No public method changes" | The RestoreCore diff adds only private methods | Line 15 | SATISFIED |
| 20 | Body vs draft | Identical content (see Body vs draft) | n/a | SATISFIED |
| 21 | Internal vocabulary | None (no gate, receipt, ledger, log names, run ids, seeds, claim, takeover) | n/a | SATISFIED |
| 22 | Bold one-line summary opens each section | All five sections (What happens today, What this change does, Proof, A choice to check, Limits) open with a bold line | n/a | SATISFIED |
| 23 | Choice section ends with a pointed question | Ends: "Was saving the exact bytes the right call, or should the rollback set only the previous directory name again?" | n/a | SATISFIED |
| 24 | Automated findings and reviews | No reviews, review comments or issue comments; no bot finding | n/a | No findings to verify |

## Citation check

- **R1a, quoted live sentence (Limits, line 43):** "Only the in-memory factories use that fallback today; [StandardDirectoryFactory](https://github.com/nick-boss-tech/solr/blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689/solr/core/src/java/org/apache/solr/core/StandardDirectoryFactory.java#L130-L148) has a similar non-atomic move when an atomic move is not supported. This is not new."
- **R1b, quoted links (line 15 twice, line 25 once):** `[RestoreCore.java](https://github.com/nick-boss-tech/solr/blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689/solr/core/src/java/org/apache/solr/handler/RestoreCore.java#L213-L226)`
- **R1b, quoted link (line 15):** `[TestRestoreCore.java](https://github.com/nick-boss-tech/solr/blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689/solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java#L242-L319)`
- **Changelog link (line 49):** `[changelog/unreleased/SOLR-18532.yml](https://github.com/nick-boss-tech/solr/blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689/changelog/unreleased/SOLR-18532.yml#L1-L7)`
- The six GitHub links in the body all use the head SHA. The only other 40-character SHA in the body is `f1e5031...`, in a Proof count line (not a link; see N4).
- Each link's lines contain the claim: RestoreCore 213 (read) to 226 (rollback call); TestRestoreCore 242 (`@Test`) to 319 (closing brace) contains the new test; StandardDirectoryFactory 130-148 is the rename with the atomic move and the non-atomic fallback; the changelog is 7 lines.
- Not file citations and not linked: class names in code spans (`RestoreCore`, `TestRestoreCore`, `RestoreCoreOpTest`), test method names, and runtime data-dir names (`index.properties`, `index.properties.<number>`, `index`). `RestoreCoreOpTest` is at `solr/core/src/test/org/apache/solr/handler/admin/RestoreCoreOpTest.java` at head, with one `@Test`.

## Proof and count check

| Body wording (Proof) | Receipt (`receipts/SOLR-18532.md` line 5) | Result |
|----------------------|-------------------------------------------|--------|
| "`TestRestoreCore`: 4 tests, 0 failures at `f1e5031...`, verified 2026-10-10." | "TestRestoreCore 4 tests, 0 failures" (head run, step 4) | Agrees |
| "`RestoreCoreOpTest`: 1 test, 0 failures at the same head, verified 2026-10-10." | "RestoreCoreOpTest 1 test, 0 failures" | Agrees |
| "The new test fails on the base code and passes with this change." | Step 3: FAILS at the pointer check with RestoreCore reverted; step 4: head run 0 failures; "GATE GREEN" | Agrees |
| "because the old rollback deletes the file" | "the old rollback deletes the file" | Agrees |

Head identity: the Proof names `f1e5031`, the PR head is `7dfd`. `git diff --stat f1e5031 7dfd` lists only the changelog (1 insertion, 1 deletion), so the code is the same as the gated tree (receipt, history note). Consistent with the receipt.

## Body vs draft

Compared the live body (read through `gh.ps1 pr view`, JSON) with `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare`. The draft has no CR characters. The live JSON body has no CR characters either. After removing my own BOM from the saved copy, the two are identical. The only difference in the saved copy is one trailing newline added by my `--jq` redirect; the live JSON body ends with a single newline, the same as the draft. Both copies carry the Limits line 47 issue above.

## CI and review state

Read 2026-10-10:

- PR fields: `state` OPEN, `isDraft` true, `headRefOid` `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, `reviewDecision` empty, `mergeStateStatus` UNKNOWN in this read (the round 3 read said UNSTABLE).
- `statusCheckRollup`: one entry, `labeler` (Pull Request Labeler), SUCCESS, completed 2026-10-10T20:45:09Z.
- Actions runs for the head (`repos/apache/solr/actions/runs?head_sha=...`, event `pull_request`, created 2026-10-10T20:45:05Z):
  - Validate Changelog: action_required (run 38084962187). Not run.
  - Gradle Precommit: action_required (run 38084962170). Not run.
  - Solr Tests via Crave: action_required (run 38084962168). Not run.
- These three are awaiting approval. That is a state, not a code failure. CI has not validated the changelog YAML yet.
- Reviews (`repos/apache/solr/pulls/5100/reviews`): `[]`. Review comments (`pulls/5100/comments`): `[]`. Issue comments (`issues/5100/comments`): `[]`.

## Verified and rejected automated findings

None. The only automated check is the labeler, which posted no comment, review or annotation. There was no finding to verify as real or to reject.

## Notes (not verdict items)

- **N1. Repeated sentence.** "The rollback path does not run in those tests" appears at line 17 and at line 47. The formula says a claim is stated once. Once line 47 names the install tests, the two copies say the same thing. Dropping line 17's copy is optional.
- **N2. Length.** The body is about 5,200 characters (draft 5,220 bytes). The formula guide is about 3,500 characters. This is round 2 note S3-8d, still open.
- **N3. Summary of "What this change does".** The bold line says the previous `index.properties` "is written back". When the core had no file before the restore, the file is deleted instead. The next sentence covers that case, and the code matches it (`restoreIndexProperties`, line 297-298). No change needed.
- **N4. Proof head.** The Proof counts cite `f1e5031`, not the PR head `7dfd`. The changelog-only delta is verified. Naming the PR head in the Proof line would help a reader who checks out the PR.
- **N5. "In-memory" wording.** `MockDirectoryFactory` extends `EphemeralDirectoryFactory`. Its `create` calls `LuceneTestCase.newDirectory()` (line 47), and the Lucene test framework source is not in this checkout (dependency in `gradle/libs.versions.toml` line 262). So I could not confirm from code that the Directory is always memory-backed. The Limit's real point holds regardless: the test factory is not `StandardDirectoryFactory`, so its file move is not run.
- **N6. Squash subject.** The head commit `f1e5031` has the subject "...instead of deleting it" (round 3 N3). If a squash merge uses the commit message instead of the PR title, the old wording returns. Confirm the merge message with Nick.
- **N7. SOLR-9865 mention.** The ticket is named without a Jira link. The formula allows ticket links; optional.
- **N8. Commitment.** "Once this change lands, I will follow up on that branch" is a public commitment in the body. The workspace record (`assignments/pool-flaky-fix-post-pr-review-round-1.md` line 11) agrees with it.

## Checked and not run

No build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. The only git action with a side effect was the read-only fork fetch, which moved `refs/remotes/origin/solr-18532-submit` to `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. The saved body copy was written to the session scratchpad only.
