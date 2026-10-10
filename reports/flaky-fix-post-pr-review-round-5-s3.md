# Flaky-fix post-PR review round 5, slice 3: SOLR-18532 (PR #5100), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-5.md`, slice 3. Reads dated 2026-10-10.

Scope: fork branch `solr-18532-submit`. `git ls-remote origin refs/heads/solr-18532-submit` returned `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, which equals the PR #5100 `headRefOid`. The slice ran.

Read-only throughout. No build, Gradle, test, Selenium, gate or test-queue run. No PR body edit, comment, review, close, submit-branch edit or Jira write. Git calls were `ls-remote`, `show`, `grep`, `diff` and `cat-file`. The live PR was read through `research/gh.ps1` only.

## Verdict

**STILL OPEN: two items. Both are in the What this change does and Limits text, in the live body and in the main-side draft.**

The round 4 item itself is in place. The last Limits bullet names the install tests and does not contradict the Proof.

Fix both in the live body and in `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare`. The two copies are identical today.

1. **Live line 17, "What this change does", second paragraph (STILL OPEN).**
   - Live text: "This is a product fix. The rollback path does not run in those tests, so this change does not address the teardown failure seen in the install tests."
   - Why: "those tests" follows the sentence that links the new test (`TestRestoreCore.java` lines 242 to 319). Read that way, the sentence says the rollback does not run in the new test. The Proof (line 25) says the old rollback runs there and deletes the file.
   - Fix: delete this paragraph (line 17 and the blank line above it). The Limits bullet on line 47 keeps the install-test claim.

2. **Live line 47, last Limits bullet, causal clause (STILL OPEN, lead to confirm).**
   - Live text: "This change does not address the teardown failure seen in the install tests, because the rollback path does not run there."
   - Why: the round 1 record (`reports/flaky-tests-root-cause-round-1-t1.md` lines 35 and 39) says the rollback catch in `RestoreCore.java` runs for the nonexistent-location install test, where it throws the expected `SolrException`. Only a second open after the rollback (line 236) is the suspected teardown route, and the record labels that an inference. So the flat clause is not supported.
   - Fix: end the bullet at "seen in the install tests." If the lead keeps the clause, this item is SATISFIED on the round 4 wording, which is what round 4 asked for.

## Item table

| # | Item | Live wording | Source check | Verdict |
|---|------|--------------|--------------|---------|
| 1 | Round 4: last Limits bullet names the install tests (line 47) | "This change does not address the teardown failure seen in the install tests, because the rollback path does not run there." | Names the install tests. It does not refer to the new test, so it does not contradict the Proof (line 25). | SATISFIED (wording) |
| 2 | Round 4 bullet's causal clause | "because the rollback path does not run there" | Round 1 record t1 lines 35 and 39: the rollback catch runs for the nonexistent-location install test; the second open is an inference. | STILL OPEN (item 2 above) |
| 3 | Line 17 sentence | "The rollback path does not run in those tests" | "Those tests" reads as the new `TestRestoreCore` test. That contradicts Proof line 25. | STILL OPEN (item 1 above) |
| 4 | Title | "SOLR-18532: RestoreCore rollback restores the previous index.properties" | Head: the rollback writes back the captured bytes (`RestoreCore.java` 213, 226, 288 to 308). It deletes only when no file existed (297 to 298). The title is accurate. | SATISFIED |
| 5 | Changelog title, `changelog/unreleased/SOLR-18532.yml` line 1 | "A failed restore or install now rolls back to the previous index.properties, so the pointer to a non-default index directory is no longer lost." | File exists at head, 7 lines. Title is accurate and does not read as never deleting a file. | SATISFIED |
| 6 | Summary, What happens today | "A failed restore or install can leave the core pointing at the wrong index directory." | Base rollback deletes `index.properties` (base `RestoreCore.java` 216 to 227). Install path: `admin/api/InstallCoreData.java` lines 91 to 92 call `RestoreCore.create` and `doRestore`. | SATISFIED |
| 7 | "No public method changes" | Line 15 | The `RestoreCore.java` diff from base (`3f5d4c5..7dfd`) adds no `public` lines. | SATISFIED |
| 8 | Behavior change: read error stops the restore | Line 19 | `readIndexProperties` (head 256 to 281) rethrows any IOException except file-not-found. It is called at 213, before the try at 217. Base `SolrCore.writeNewIndexProps` logged load errors (1512) and ignored `openInput` IOExceptions (1516 to 1517). | SATISFIED |
| 9 | Proof: the old rollback deletes the file | Line 25 | Base rollback deletes `index.properties` (base line 227). Receipt step 3 says the same. | SATISFIED |
| 10 | Proof: test name | `TestRestoreCore.testFailedRestoreKeepsNonDefaultIndexPointer` | Head line 243. | SATISFIED |
| 11 | Bold one-line summary opens each section | Five sections, all bold | Checked against the live body. | SATISFIED |
| 12 | Choice section ends with a pointed question | "Was saving the exact bytes the right call, or should the rollback set only the previous directory name again?" | Ends with a question. | SATISFIED |
| 13 | Internal vocabulary | None found | Scan of the body: no gate, receipt, ledger, log names, run ids, seeds, claim or takeover. "run" appears only as plain English. "review" is in the required AI footer. | SATISFIED |
| 14 | Body equals draft | Lines 17 and 47 identical in both | See Body vs draft. | SATISFIED (apart from items 1 and 2) |

## Limits check

| Limit (live text) | Head check | Verdict |
|---|---|---|
| In-memory factory: "The proof runs on the in-memory test directory factory. The atomic file move used by the file-system factory is not run by these tests." | `solrconfig-leader.xml` line 22 uses `${solr.directoryFactory:solr.MockDirectoryFactory}`. No Gradle file sets `solr.directoryFactory`. `SolrTestCaseJ4` sets it only inside `useFactory` (lines 376 and 384), which `TestRestoreCore` does not call. `MockDirectoryFactory` extends `EphemeralDirectoryFactory` and creates from `LuceneTestCase.newDirectory` (test-framework `MockDirectoryFactory.java` 46 to 50). It does not use `StandardDirectoryFactory`'s atomic move. | SATISFIED (see note N6) |
| Rollback failure: "If writing the saved bytes back fails, the error stops the rest of the rollback. The writer is not reopened and the restore directory is not removed. The base code has the same shape around its delete." | Head 226 is inside the catch. An exception there skips 228 to 231 (`doneWithDirectory`, `remove`, `newIndexWriter`, `openNewSearcher`). Base delete (base 227) sits inside the same catch and has the same shape. | SATISFIED |
| Temp file: "a temporary `index.properties.<number>` file can be left ... Nothing removes it. The existing code has the same pattern." | Head 300 to 307 create `index.properties.<nanoTime>`. Nothing removes it on failure. `SolrCore.modifyIndexProps` (1474 to 1476) uses the same pattern. | SATISFIED |
| Non-atomic fallback: "On a factory that deletes and then renames ... Only the in-memory factories use that fallback today; StandardDirectoryFactory has a similar non-atomic move when an atomic move is not supported." | `DirectoryFactory.renameWithOverwrite` (204 to 215) deletes, then renames. The Ephemeral family inherits it: `ByteBuffersDirectoryFactory`, `RAMDirectoryFactory`, `MockDirectoryFactory`. `StandardDirectoryFactory` overrides it (130 to 150) with `Files.move`, with a non-atomic fallback at 141 to 145. Its `super` call (148) is only for a non-filesystem base directory, and `create` returns `FSDirectory` (52). No other `DirectoryFactory` subclass exists in the repo at this head. | SATISFIED (see note N7) |
| Read-error change | Item 8 above. | SATISFIED |
| Failure-wait: "polls the restore status 10 times at 50 ms intervals ... copied from the existing `testFailedRestore`. A slower failure would make the test fail rather than pass." | Head 300 to 303 is the same 10 by 50 ms loop as 227 to 230 in `testFailedRestore`. A slower failure leaves `expectThrows` with no assertion, so the test fails. | SATISFIED |
| SOLR-9865 follow-up: "writes back only the previous directory name" | `pr-drafts/replication-backup/SOLR-9865.md` line 13 says this. The head diff (`RestoreCore.java`, `TestRestoreCore.java`, changelog) has no such change. I did not verify the 9865 code itself. | SATISFIED (draft source) |
| Not checked: "It does not check that the file is absent when the core had no earlier file." | Head 297 to 315 check only the pointer. | SATISFIED |

## Citation check

All six links use `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. Each range was read with `git show ... | cat -n | sed -n`.

| Link (body) | Lines at head | Content at those lines | Verdict |
|---|---|---|---|
| `RestoreCore.java` L213-L226 (live lines 15 twice, 25) | 213 `readIndexProperties()`; 226 `restoreIndexProperties(previousIndexProps)` | The capture and the rollback call. The write-back helper (288 to 314) is not in this range. | SATISFIED (note N2) |
| `TestRestoreCore.java` L242-L319 | 242 `@Test`; 243 new method; 319 closing brace | The new test. | SATISFIED |
| `StandardDirectoryFactory.java` L130-L148 | 130 `renameWithOverwrite`; 136 to 140 atomic move; 141 to 145 non-atomic fallback; 148 `super` call | The atomic move and the non-atomic move. | SATISFIED |
| `changelog/unreleased/SOLR-18532.yml` L1-L7 | Whole file, 7 lines | The changelog. | SATISFIED |

Class and method names in code spans are not file citations, so they are not checked as links.

## Proof and count check

| Body (Proof) | Receipt (`receipts/SOLR-18532.md`) | Result |
|---|---|---|
| `TestRestoreCore`: 4 tests, 0 failures at `f1e5031...`, verified 2026-10-10 | Head run, step 4: "TestRestoreCore 4 tests, 0 failures" at `f1e5031fc3a9624b03daf353f26c977891d7d876` | Agrees |
| `RestoreCoreOpTest`: 1 test, 0 failures, same head | "RestoreCoreOpTest 1 test, 0 failures" | Agrees |
| New test fails on base, passes with change | Step 3: FAILS at the pointer check with RestoreCore reverted. Step 4: 0 failures. | Agrees |
| Old rollback deletes the file | "the old rollback deletes the file" | Agrees |
| Four test cases (receipt) | `testSimpleRestore` (99), `testBackupFailsMissingAllowPaths` (176), `testFailedRestore` (191), `testFailedRestoreKeepsNonDefaultIndexPointer` (243) all exist at head. | Agrees |

Head identity: the Proof names `f1e5031`, and the PR head is `7dfd`. `git diff --stat f1e5031 7dfd` changes only the changelog title (1 line). The code is the gated tree, as the receipt says.

## Body vs draft

Live body read with `gh.ps1 pr view 5100 --json body --jq .body`, saved to the session scratchpad, and compared with `pr-drafts/flaky-fixes/SOLR-18532.md` on `origin/pr-prepare`, CR stripped. The live body has no CR characters.

Result: identical. The only difference is one trailing newline added by the export. No wording differs, so items 1 and 2 must be changed in both places.

## CI and review state

- PR #5100: OPEN, draft. `headRefOid` `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. `reviewDecision` empty. `mergeStateStatus` UNSTABLE in this read.
- `statusCheckRollup`: `labeler` (Pull Request Labeler), SUCCESS, completed 2026-10-10T20:45:09Z.
- Actions runs at the head (`pull_request`, created 2026-10-10T20:45:05Z):
  - Validate Changelog: completed, `action_required`.
  - Gradle Precommit: completed, `action_required`.
  - Solr Tests via Crave: completed, `action_required`.
  - These three have not run. `action_required` is a state (awaiting approval), not a code failure. The changelog YAML has not been validated by CI yet.
- Reviews (`pulls/5100/reviews`): `[]`. Review comments (`pulls/5100/comments`): `[]`. Issue comments (`issues/5100/comments`): `[]`.

## Verified and rejected automated findings

None. The only automated check is the labeler, which posted no comment, review or annotation. There was no finding to verify or reject.

## Notes (not verdict items)

- **N1. Proof head.** The Proof names `f1e5031` and the PR head is `7dfd`. The delta is the changelog title only. Naming the PR head in the Proof line would help a reader who checks out the PR.
- **N2. RestoreCore citation.** "The change is in RestoreCore.java" links only lines 213 to 226. The write-back helper at 288 to 314 is not linked. An extra link to that range would cover the change fully. Optional.
- **N3. Length.** The body is about 5,190 characters. The formula guide says about 3,500 unless the ticket is unusually complex. This is round 2 note S3-8d, still open.
- **N4. Public commitment.** The SOLR-9865 bullet says "I will follow up on that branch". This is a public commitment. The lead should confirm it with Nick.
- **N5. Squash subject.** Round 4 N6 (the squash message may reuse the old wording) was not checked here. The title is correct.
- **N6. "In-memory".** `MockDirectoryFactory` creates through `LuceneTestCase.newDirectory`. The randomizer that picks the directory type is not in this checkout, so "in-memory" holds by class family and the receipt's note, not per run. The second Limits sentence does not depend on it.
- **N7. Fallback reach.** `StandardDirectoryFactory`'s `super` call is reached only for a non-filesystem base directory, and its `create` always returns `FSDirectory`. In practice the Limits sentence is accurate.
- **N8. Scope.** The worktree is detached and holds another slice's untracked report (`reports/flaky-fix-post-pr-review-round-5-s1.md`). I did not touch it.
