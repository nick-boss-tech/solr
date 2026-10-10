# Flaky-fix post-PR review round 3, slice 3: SOLR-18532 (PR #5100)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-3.md`, slice 3. Lead: the windows review agent. Lead report: `reports/flaky-fix-post-pr-review-round-3.md`.

Date of live reads: 2026-10-10. Live PR #5100 on apache/solr: draft, OPEN, head `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. The fork branch `solr-18532-submit` was confirmed with `git ls-remote` at the same head before any review, and the read-only fork fetch was run. Read-only throughout: no build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. No claim file was touched.

## Verdict

**STILL OPEN.** Two items in the live body remain. Both are body-only edits. No code, test or changelog change is needed.

1. **R1, Limits plain span.** The Limits bullet on the non-atomic fallback still names `StandardDirectoryFactory` as a plain code span. Link it at head `7dfd3d98d0a2f016c520139cbca96ea4a49f6689` to `solr/core/src/java/org/apache/solr/core/StandardDirectoryFactory.java`, lines 130-148 (the round 2 anchor).
2. **R1, stale SHA on two links.** Two file links still point at the older head `f1e5031fc3a9624b03daf353f26c977891d7d876` instead of the PR head `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`: the first `RestoreCore.java` link in "What this change does" (`#L213-L226`) and the `TestRestoreCore.java` link (`#L242-L319`). The blobs are identical, because the head delta is changelog-only, so the content is the same. The rule in the formula is the PR head SHA, so these are still open.

Everything else is SATISFIED: the receipt now names the four test cases, the count and pass wording match the receipt, the title and changelog no longer say "instead of deleting it", the changelog title commit is tree-identical to the gated tree, the gate file records the failed first gate at 07a7ead, and the live body equals the draft. CI has not run for the head (see CI section). That is a state, not a code finding.

## Item table

| # | Item | Live wording or record | Source check | Verdict |
|---|------|------------------------|--------------|---------|
| 1a | R1: file names as links (Limits) | Limits, bullet 4: "`StandardDirectoryFactory` has a similar non-atomic move when an atomic move is not supported." Plain code span. | At head, `StandardDirectoryFactory.java` 130-148: `renameWithOverwrite`; atomic `Files.move` (136-140); non-atomic fallback on `AtomicMoveNotSupportedException` (142-145); base delegate (148). The claim matches. | STILL OPEN |
| 1b | R1: links at PR head SHA | "What this change does", first link: `.../blob/f1e5031fc3a9624b03daf353f26c977891d7d876/solr/core/src/java/org/apache/solr/handler/RestoreCore.java#L213-L226`. Test link: `.../blob/f1e5031fc3a9624b03daf353f26c977891d7d876/solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java#L242-L319`. | Head is `7dfd`. `git diff --stat f1e5031 7dfd` lists only `changelog/unreleased/SOLR-18532.yml`, so both paths are the same blobs at either SHA. Lines match (see 1c). | STILL OPEN (SHA only) |
| 1c | R1: line anchors and claimed code | "The change is in [RestoreCore.java](`.../7dfd.../RestoreCore.java#L213-L226`)". Proof link to the same range. Changelog link `.../7dfd.../changelog/unreleased/SOLR-18532.yml#L1-L7`. | Head `RestoreCore.java` 213: `byte[] previousIndexProps = readIndexProperties();`; 214: `core.modifyIndexProps(restoreIndexName);`; 226: `restoreIndexProperties(previousIndexProps);`. Head `TestRestoreCore.java` 242: `@Test`; 319: closing brace. Changelog has 7 lines. Anchors cover the claimed code. | SATISFIED |
| 1d | R1: round 2 bare citations (line 15 `RestoreCore.java`, line 49 changelog path) | Both are now links (see 1c). | | SATISFIED |
| 1e | Other repo names in code spans | `RestoreCore` (class, "What happens today", Limits); `TestRestoreCore.testFailedRestoreKeepsNonDefaultIndexPointer` and `testFailedRestore` (test names); `RestoreCoreOpTest` (Proof count line); `index.properties`, `index.properties.<number>` and `index` (runtime file names in the data dir, not repo files). Class and test names are not file citations, so they are not counted. | | Noted, not counted |
| 2 | R2: test count and four names recorded | Body, Proof: "- `TestRestoreCore`: 4 tests, 0 failures at `f1e5031fc3a9624b03daf353f26c977891d7d876`, verified 2026-10-10." Receipt, line 5: "TestRestoreCore 4 tests, 0 failures (fresh JUnit XML). The four TestRestoreCore cases in the re-gate's step 4 run, named from g18532-gate.log: testBackupFailsMissingAllowPaths, testFailedRestore, testFailedRestoreKeepsNonDefaultIndexPointer, testSimpleRestore." | Head source has four `test*` methods in `TestRestoreCore.java`: `testSimpleRestore` (99, `@Test` at 98), `testBackupFailsMissingAllowPaths` (176, no annotation, same as base line 168), `testFailedRestore` (191, `@Test` at 190), `testFailedRestoreKeepsNonDefaultIndexPointer` (243, `@Test` at 242). All four names exist. The receipt now resolves the naming question: the count and the names agree, and the un-annotated method is named in the receipt's step 4 run. | SATISFIED (on the record) |
| 2b | R2 caveat: JUnit XML | The receipt cites `g18532-gate.log` on vm1 and "fresh JUnit XML". | The XML is not on disk in this worktree or on `origin/pr-prepare` (no file matching 18532 except the gates, pr-drafts and receipts). The four names were not checked against the XML. Whether the randomized runner picks up a `test*` method without `@Test` is not verifiable here. | Noted; the record stands as the only source |
| 2c | R3: pass wording | Body: "4 tests, 0 failures" and "1 test, 0 failures" (Proof). Receipt line 5: "TestRestoreCore 4 tests, 0 failures" and "RestoreCoreOpTest 1 test, 0 failures". | Head `RestoreCoreOpTest.java` has one `@Test` (line 27). | SATISFIED |
| 3 | R4: title and changelog no longer read as never deleting | Live title: "SOLR-18532: RestoreCore rollback restores the previous index.properties". Changelog line 1 at head: "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." Neither says the change never deletes. | Head `RestoreCore.java` 297-298: `if (previousIndexProps == null) { dir.deleteFile(IndexFetcher.INDEX_PROPERTIES); }`. The body says "The file is deleted only when the core had no `index.properties` before the restore." That matches the code. | SATISFIED |
| 4 | Changelog title commit and gated tree | Head `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, subject "SOLR-18532: Correct the changelog title", parent `f1e5031fc3a9624b03daf353f26c977891d7d876`. Changelog title at head: "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." Receipt line 11: "Changelog-only delta on top of the gated tree; the gate at f1e5031fc3 carries over the identical code tree." | `git diff --stat f1e5031 7dfd`: only the changelog file (1 insertion, 1 deletion). Tree of `f1e5031` = `40a18b12e637e4c5f11313c4742df3f9685a84ca`; tree of `348dd63` = the same; tree of `7dfd` = `5bbd7cf9df3f8ad48a0da91e7c1d624af4b2cb37` (changelog differs). The receipt's statement holds. | SATISFIED (CI validation of the YAML is still pending, see CI) |
| 5 | Internal record: failed first gate at 07a7ead | `gates/SOLR-18532.md`, line 10: "History: the first gate at 07a7ead4783 (2026-10-10) FAILED. Both defects were in the branch's new test, not the production fix: ... Fixed at 348dd63d85a and re-gated GREEN; receipts/SOLR-18532.md carries the detail." Line 8: "Status: DONE 2026-10-10. GATE GREEN at 348dd63d85a563c77e0a20b5742b2a0c845dc191 (run on vm1; see receipts/SOLR-18532.md)." | The round 2 item is met. The gate file also has a stale bullet at line 6: "Status: QUEUED on vm1 behind the SOLR-18530 gate." It sits above the DONE line. | SATISFIED (see note N4) |
| 6 | Live body equals the draft | `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare`, CR stripped, compared with the live body (CR stripped). | Identical, 5,050 characters each. See the body-vs-draft section. | SATISFIED |
| 7 | Proof numbers match receipt | See the Proof check section. | All numbers match. | SATISFIED |
| 8 | Title accuracy and new comments | Live title as in item 3. | Title matches what the rollback does at head. No reviews, no review comments, no issue comments (see CI). No automated finding to verify. | SATISFIED |
| 9 | CI and reviews | See the CI section. | State only. | State: not run (see CI) |

## Body vs draft

Compared after stripping CR: the live body from `gh pr view 5100 --json body` and the draft on `origin/pr-prepare`. `diff` reports no difference, and both end with the same final newline. No round 2 item is applied in the draft and missing from the live body.

Both copies still carry the two R1 gaps in item 1a and 1b, so a body edit must be made on the live PR and in the draft together.

## Proof check

| Body wording | Receipt wording (line) | Result |
|--------------|------------------------|--------|
| "`TestRestoreCore`: 4 tests, 0 failures at `f1e5031...`, verified 2026-10-10." | Line 5: "TestRestoreCore 4 tests, 0 failures" at the head run; gate done 2026-10-10 (line 1; gate file line 8) | Agrees. The proof head is `f1e5031`, the gated commit. Its tree is identical to the PR head `7dfd` (item 4). Noted in N5. |
| "`RestoreCoreOpTest`: 1 test, 0 failures at the same head, verified 2026-10-10." | Line 5: "RestoreCoreOpTest 1 test, 0 failures" | Agrees. |
| "fails on the base code ... the pointer check fails. The check expects the earlier restored directory, but the pointer is missing (null), because the old rollback deletes the file." | Line 3: "FAILS at the pointer check: ... expected but null, because the old rollback deletes the file" | Agrees. Base `RestoreCore.java` 227 is `dir.deleteFile(IndexFetcher.INDEX_PROPERTIES);`. |
| Test name `TestRestoreCore.testFailedRestoreKeepsNonDefaultIndexPointer` | Line 3: same name | Agrees. |
| Proof says the test passes with this change | Line 1: "GATE GREEN"; line 5: head run 0 failures | Agrees. |

## CI and review state (read 2026-10-10)

- Live PR fields (`gh.ps1 pr view 5100`): `state` OPEN, `isDraft` true, `headRefOid` `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, `mergeStateStatus` UNSTABLE, `reviewDecision` empty.
- `statusCheckRollup` (live): one entry, `labeler`, SUCCESS (run 38084960068, job 114309451502, completed 2026-10-10T20:45:09Z).
- Actions runs for the head (`repos/apache/solr/actions/runs?head_sha=7dfd...`, all `pull_request`, created 2026-10-10T20:45:05Z):
  - Solr Tests via Crave: `action_required` (run 38084962168). Not run.
  - Gradle Precommit: `action_required` (run 38084962170). Not run.
  - Validate Changelog: `action_required` (run 38084962187). Not run. The changelog YAML has not been validated by CI.
  - Pull Request Labeler: success (run 38084960068, `pull_request_target`).
- These three required checks are not in `statusCheckRollup`. They are awaiting approval to run. That is a state, not a code failure.
- Reviews (`repos/apache/solr/pulls/5100/reviews`): `[]`. Review comments (`pulls/5100/comments`): `[]`. Issue comments (`issues/5100/comments`): `[]`. The GraphQL `gh pr view --json reviews,comments` call fails on token scope (read:org, read:discussion), so the REST endpoints were used.
- `reviewRequests`: none recorded in the live fields read.

## Verified and rejected automated findings

None. The only automated check is the labeler, which posted no comment and no review. There was no finding to verify as real or to reject.

## Notes (not verdict items)

- **N1. Repeated claim.** "The rollback path does not run in those tests, so this change does not address the teardown failure" appears in "What this change does" and again as the last Limits bullet. The formula says a claim stated in a summary is not restated in the body. This was round 2 judgment note; it is still present.
- **N2. Length.** The live body is 5,050 characters. The formula guide is about 3,500 characters unless the ticket is complex. This was round 2 note S3-8d, still open.
- **N3. Squashed commit subject.** The head commit `f1e5031` has the subject "SOLR-18532: RestoreCore rollback restores the previous index.properties instead of deleting it". The `7dfd` commit only changes the changelog. If the merge uses the commit message rather than the PR title, the old wording returns. Confirm the merge message with Nick.
- **N4. Gate file.** The header bullet "Status: QUEUED on vm1 ..." (line 6) is stale and sits above "Status: DONE" (line 8). The gate file also names head 348dd63 and does not record the squash to `f1e5031` or the move to `7dfd`. The receipt does record both. Not a round 2 item.
- **N5. Proof head.** The Proof counts say "at `f1e5031`", while the PR head is `7dfd`. The receipt gives the tree identity and the changelog-only delta, so the statement is supported. A reader may want the PR head named.
- **N6. Limits duplication**: see N1.

## Checked and not run

No build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. The only git action with a side effect was the read-only fetch `git fetch origin refs/heads/solr-18532-submit:refs/remotes/origin/solr-18532-submit`, which moved the remote-tracking ref to `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. The live body copy and the draft copy were written to the session scratchpad only, not to the worktree.
