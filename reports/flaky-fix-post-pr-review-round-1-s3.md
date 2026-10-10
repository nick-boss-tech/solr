# Flaky-fix post-PR review round 1, slice 3: SOLR-18532 (PR #5100)

Date of live reads: 2026-10-10. Scope: read-only review of the live draft PR #5100 on apache/solr against fork branch solr-18532-submit at f1e5031fc3a9624b03daf353f26c977891d7d876. No build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. The only git action with a side effect was a read-only fetch of the fork branch refspec (`git fetch origin refs/heads/solr-18532-submit`), which moved the local remote-tracking ref origin/solr-18532-submit from 348dd63 to f1e5031. No remote state changed. The live body was saved to the scratchpad only, not to the worktree.

## Verdict

FINDINGS. Not CONSISTENT in full.

- The live body is byte-identical to `pr-drafts/flaky-fixes/SOLR-18532.md` on the worktree tip (cmp: identical, 3,754 bytes). There is no drift between the PR and the reference draft.
- Head, tree, title, proof numbers, flake framing and internal wording check out.
- The findings below are in the draft itself, in the live PR state, or in round 1 items that are not settled. Nothing here is a blocker for the code; several need a decision by the main side before the PR leaves draft.

Findings (numbered for the main side):

- **S3-1 (CI, live PR).** Three required workflows are `action_required` at the head and have not run: Solr Tests via Crave, Gradle Precommit, Validate Changelog. Only the labeler check ran. The changelog YAML has not been validated by CI. Per AGENTS.md, action_required is incomplete validation.
- **S3-2 (causal claim, body and draft).** The Limits sentence "The intermittent teardown failure in the install tests comes from another path" states a cause that the record does not confirm. t1 calls the "restore path cannot fire here" point an inference (`reports/flaky-tests-root-cause-round-1-t1.md` line 39), and the root-cause report marks the install-test causes unconfirmed (`reports/flaky-tests-root-cause-round-1.md` line 133). Suggested wording: "The rollback path does not run in those tests, so this change does not address the teardown failure."
- **S3-3 (Limit, temp file, body and draft).** The body says "If the write fails, a temporary index.properties.<number> file can be left". The code leaves the file after any failure once it is created: write, close, sync or rename (`RestoreCore.java` 301-307). Suggested: "If any step after the file is created fails".
- **S3-4 (Limit, non-atomic fallback, body and draft).** The limit covers only the base delete-then-rename default. `StandardDirectoryFactory.java` lines 141-145 also have a non-atomic `Files.move` with REPLACE_EXISTING when ATOMIC_MOVE is not supported. The body does not name it. The main side decides whether to add it.
- **S3-5 (round 1 item dropped silently).** s3a-F5 (the new test's failure wait is 10 polls of 50 ms, `TestRestoreCore.java` 297-306; a slow failure makes the test fail, not pass) is not in the body and not in Limits. Owner decision D3 was not taken. It needs a Limit line, or a test change with a new head and a new gate.
- **S3-6 (count, unsettled).** The body says "TestRestoreCore: 4 of 4 pass". The receipt is the only source ("4 tests, 0 failures"). The source at head has three `@Test` annotations (lines 98, 190, 242) and one un-annotated `test*` method (line 176, `testBackupFailsMissingAllowPaths`, which predates this branch). Whether the runner executes it was not verified here, and the JUnit XML the receipt cites is not on disk. Round 1 s3b-D4 said to confirm before "4 of 4"; no confirmation is recorded. Confirm from the gate JUnit XML, or reword to the receipt wording.
- **S3-7 (wording, optional, title and changelog).** "instead of deleting it" (title and `changelog/unreleased/SOLR-18532.yml` line 1) reads as never deleting. The code still deletes when the core had no index.properties (`RestoreCore.java` 297-298). Round 1 s3b-D7, still open, optional.
- **S3-8 (pr-formula gaps, body and draft alike).** (a) No file citation is a link at the head SHA; the body names `RestoreCore.java`, the test and the changelog path as plain code spans. The presentation rule requires links; anchors are in section 7. (b) "A choice to check" ends with "if the name route reads better, I can switch"; the template ends with the pointed question ("Was this the right call?"). (c) "Limits" has no bold one-line summary; every section should open with one. (d) 3,754 bytes against the roughly 3,500-character guide. None of these is a blocker.
- **S3-9 (SOLR-9865 wording, body and draft).** "A separate open change for SOLR-9865" points at a fork branch, not a public change. A search of apache/solr for open PRs with SOLR-9865 in the title returned none. The fork branch solr-9865-submit (tip 4937608bb181) still carries commit cd46e4a3521, so the hunk drop Nick decided has not happened yet (outside this slice). Suggest naming the ticket ("the SOLR-9865 change") rather than "open change".
- **S3-10 (proof wording, minor).** The receipt records "0 failures". The body says "4 of 4 pass" and "1 of 1 passes". The receipt does not state 0 errors or 0 skipped. Acceptable as a translation; strictly, use "0 failures".
- **S3-11 (internal record, not the body).** `gates/SOLR-18532.md` line 3 now names 348dd63, so the round 1 s3b-D5 mismatch on 07a7ead is fixed. The gate file still has no record of the failed first gate at 07a7ead, which WORKFLOW.md line 43 requires. Optional: the code comment at `TestRestoreCore.java` 265-266 describes the rejected approach ("rather than from the filesystem"); s3b F3 suggested cutting that clause.

## 1. Head and tree

- Live `headRefOid`: f1e5031fc3a9624b03daf353f26c977891d7d876.
- `git ls-remote origin refs/heads/solr-18532-submit`: f1e5031fc3a9624b03daf353f26c977891d7d876. **Match.**
- The commit is a single commit above base 3f5d4c5bf8ac (rev-list count 1; the parent is the merge-base). Author and committer: Nick Shanin. No trailers.
- Before my read-only fetch, the object f1e5031 was not present in the local store (cat-file failed). After the fetch it is present.
- Tree of f1e5031: 40a18b12e637e4c5f11313c4742df3f9685a84ca. Tree of the gated commit 348dd63d85a: 40a18b12e637e4c5f11313c4742df3f9685a84ca. **Match.**
- Receipt (`receipts/SOLR-18532.md`): "tree 40a18b12e637, identical to the gated tree at 348dd63d85a". Its header names f1e5031 as the gated head. The gate file (`gates/SOLR-18532.md` line 3) names 348dd63. The two names are reconciled by tree identity. **Match.**
- Live file list: changelog (7 additions), RestoreCore.java (72 additions, 14 deletions), TestRestoreCore.java (118 additions). Same as `git diff --stat 3f5d4c5bf8a f1e5031`. **Match.**

## 2. Title

Live title: "SOLR-18532: RestoreCore rollback restores the previous index.properties instead of deleting it".

Accurate for the change at this head. The rollback writes back the captured bytes, and it deletes only when the core had no file. The "instead of deleting it" phrase is the same generalization as S3-7. The title makes no flake claim.

## 3. Body vs draft

- Method: `gh pr view 5100 --json body --jq .body`, saved raw; compared with `git show 80ed2a7882c:pr-drafts/flaky-fixes/SOLR-18532.md` (the file is unchanged in the worktree against that commit).
- Result: byte-identical. No differences in wording, numbers, links, headings, the AI header and footer, the Jira link, or the Changelog line.

## 4. Framing

- The body says "This is a product fix. It does not address the intermittent teardown failure seen in the install tests." It does not say the flake is fixed. The changelog and the title make no flake claim.
- Root-cause report line 34 says "Separate product defect, not this flake's fix". t1 line 95 says "Separate product defect, flagged and not this flake's fix". Both are reflected.
- The only framing issue is S3-2 (the "comes from another path" cause sentence).

## 5. Proof

| Body says | Receipt says | Result |
|---|---|---|
| TestRestoreCore 4 of 4 pass at f1e5031, verified 2026-10-10 | Head run (step 4): "TestRestoreCore 4 tests, 0 failures"; gate done 2026-10-10 | Match. Count still open (S3-6). |
| RestoreCoreOpTest 1 of 1 passes, same head, 2026-10-10 | "RestoreCoreOpTest 1 test, 0 failures" | Match. Source has one `@Test` (RestoreCoreOpTest.java line 27). |
| Fails on base at the pointer check; pointer expected, null | "FAILS at the pointer check: ... expected but null, because the old rollback deletes the file" | Match. The failing assertion is `assertEquals` at TestRestoreCore.java 312-315; the base rollback's removed `deleteFile` is the cause (diff of 3f5d4c5 to f1e5031). |
| "pass" at f1e5031 | Receipt header names f1e5031; tree identical to the gated 348dd63 | Match by tree identity. |
| Numbers in the Proof | Only 4, 1, the date 2026-10-10 (date only, template-allowed) and the SHA appear | No number outside the receipt. |
| "final assertion" wording | Not in the body. The receipt now says "pointer check" (line 3), not "final assertion". The last statement of the test is verifyDocs at line 318, after the pointer check at 312-315. | Absent from body and receipt. Resolved. |

## 6. Internal wording

- Scanned the live body for: gate, receipt, step labels, seed, runner, log, timestamps, vm, JUnit, forbidden-API, Properties, squash, history, commit, premise, final assertion, tidy, lint, Claude, co-author. No hits. The words "pass", "verified 2026-10-10" and "Changelog:" are sanctioned outcome, date or template wording.
- No commit-history narration. The squashed commit message is clean and has no trailers.
- No em dashes or en dashes in the body or in the added diff lines.
- The optional code-comment note (S3-11) is the only internal-style text in the public diff.

## 7. Citations

The body has no file links. Only the Jira link is a link. The presentation rule (S3-8a) asks for file citations as links at the head SHA. Anchors checked at f1e5031 (`git show <head>:<path>`, line numbers verified):

- `solr/core/src/java/org/apache/solr/handler/RestoreCore.java`: capture 213 (`readIndexProperties()`), switch 214 (`modifyIndexProps`), rollback catch 222-226 (`restoreIndexProperties` call at 226), `readIndexProperties` 256-281, `restoreIndexProperties` 288-314 (delete at 297-298; temp write 301-304; sync 305; rename 306-307).
- `solr/core/src/test/org/apache/solr/handler/TestRestoreCore.java`: new test 242-319 (pointer `assertEquals` 312-315; verifyDocs 318); helper `readIndexProperty` 326-350; failure wait 297-306.
- `changelog/unreleased/SOLR-18532.yml`: lines 1-7.
- URL pattern for the links: `https://github.com/nick-boss-tech/solr/blob/f1e5031fc3a9624b03daf353f26c977891d7d876/<path>#L<a>-L<b>`.
- Base and framework anchors used in the limits: `DirectoryFactory.java` 204-215 (delete then rename); `StandardDirectoryFactory.java` 130-148 (atomic move 136-140, non-atomic fallback 141-145, base delegation 148); `solr/test-framework/src/java/org/apache/solr/core/MockDirectoryFactory.java` line 30 (extends EphemeralDirectoryFactory); `solr/core/src/test-files/solr/collection1/conf/solrconfig-leader.xml` line 22 (default `solr.MockDirectoryFactory`).
- `InstallCoreData.java` (solr/core/src/java/org/apache/solr/handler/admin/api/) line 91 calls `RestoreCore.create`, so "install" in the body is correct.

## 8. Limits

| Limit in body | Code at head | Result |
|---|---|---|
| Proof runs on the in-memory test factory; the atomic file move is not run | Test config defaults to MockDirectoryFactory, which extends EphemeralDirectoryFactory and uses the base delete-then-rename. The atomic move is in StandardDirectoryFactory 136-140. | Accurate. |
| If writing the bytes back fails, the error stops the rest of the rollback; writer not reopened; restore dir not removed | Line 226 is inside the catch; lines 228-231 are skipped if it throws; the finally releases directories. | Accurate. |
| A temp `index.properties.<number>` can be left; nothing removes it | Created at 301-302; left on any later failure. | Accurate, but "if the write fails" is narrower (S3-3). |
| Non-atomic fallback: delete then rename; only in-memory factories use it | Base default at DirectoryFactory 204-215 is delete then rename; in-memory factories use it. StandardDirectoryFactory 141-145 has a separate non-atomic Files.move fallback the body does not name. | Partly accurate (S3-4). |
| Read error other than file-not-found now stops the restore before the switch; before, the error was ignored | Read at 213 is before the switch at 214; `readIndexProperties` catches only FileNotFoundException and NoSuchFileException. The base never read the file, so the error could not surface. | Accurate. |
| Test checks the pointer after rollback; not that the file is absent when no earlier file existed | The test covers only the non-null case (242-319). | Accurate. |
| SOLR-9865 follow-up: the other change writes back only the previous directory name | Commit cd46e4a3521 (fork branch solr-9865-submit) calls `modifyIndexProps` with the previous directory name and deletes only when that name is "index". Branch tip 4937608bb181 still carries it. | Accurate description; wording (S3-9). |
| "The intermittent teardown failure ... comes from another path" | See S3-2. The record does not confirm the cause. | Overstated. |

The "A choice to check" statement that the other route "sets it again through the existing modify path" matches cd46e4a3521.

## 9. Changelog

- File at head: `changelog/unreleased/SOLR-18532.yml`, 7 lines: title, type `fixed`, author Nick Shanin, link to SOLR-18532. No ICLA placeholder.
- Title matches the body's behavior statement. It does not mention the read-error behavior change (the body does). Not a conflict; the body is the place for it.
- The "instead of deleting it" wording is S3-7.
- The CI changelog validator has not run (S3-1), so YAML validity is not confirmed by CI. The file's shape matches the other fragments (per s3b F4).

## 10. CI and review state (live, 2026-10-10)

| Check | Event | State at f1e5031 |
|---|---|---|
| labeler (Pull Request Labeler) | pull_request_target | SUCCESS (run 38079478965) |
| Solr Tests via Crave | pull_request | action_required, not run |
| Gradle Precommit | pull_request | action_required, not run |
| Validate Changelog | pull_request | action_required, not run |

- `statusCheckRollup`: one check (labeler, success). `mergeStateStatus`: UNSTABLE. `reviewDecision`: empty. `isDraft`: true. Labels: `tests` (added by the labeler).
- Reviews: 0. Issue comments: 0. Inline review comments: 0. Requested reviewers: 0.
- Files: 3, as in section 1.

## 11. Automated findings

None. The only automated item is the labeler, which posted no review and no comment. No automated review finding exists to verify, so none is verified as real and none is rejected.

## 12. Round 1 item status (SOLR-18532 section, `reports/flaky-fix-review-round-1.md` lines 53-78, and parts s3a and s3b)

| Item | Status in live body or still listed |
|---|---|
| D-a overlap with SOLR-9865 | Settled in body (Limits names the follow-up). The SOLR-9865 branch hunk drop is still pending on that branch (S3-9). |
| D-b framing (standalone product fix, no flake claim) | Settled in body. Nick's sign-off on the framing is not in the files I read. |
| "final assertion" wording in receipt | Resolved. The body and the receipt say "pointer check". |
| s3a-F1 guard the restore (writer and restore-dir cleanup skipped on rollback failure) | Settled as a Limit (fix not taken). |
| s3a-F2 temp file leftover | Settled as a Limit, wording narrower (S3-3). |
| s3a-F3 non-atomic fallback | Settled as a Limit, partly (S3-4). |
| s3a-F4 and s3a-D5 read errors now abort | Settled in body (behavior change stated). The changelog does not mention it; acceptable. |
| s3a-F5 and s3a-D3 failure wait | **Dropped silently.** Not in body, not in Limits (S3-5). |
| s3a-F6 in-memory factory only | Settled as a Limit. |
| s3a-D2 hardening decision (F1 and F2 fixes) | Not taken; the Limits cover the gap. |
| s3b-D3 and s3a-D4 commit history | Settled: the branch is one squashed commit with a clean message. |
| s3b-D4 count "4 of 4" | **Open** (S3-6). |
| s3b-D5 gate record | Partly settled: line 3 now names 348dd63. The failed first gate is still not recorded (S3-11, internal). |
| s3b-D6 coverage (no absent-file assertion) | Settled as a Limit. |
| s3b-D7 "instead of deleting it" | **Open, optional** (S3-7). |
| Receipt "final assertion" (s3b F1) | Resolved (see above). |

Nothing else in the section is missing from the body or from this list.

## Checked and not run

No build, test, Gradle, Selenium, gate or test-queue run. No Jira read or write. No change to claims, WORKFLOW.md, gates, receipts, drafts or the PR. Numbers come from the receipt only. The gate log on vm1 was not read.
