# Flaky-fix review round 1, slice 3b: SOLR-18532 test, history, proof wording, changelog

Scope: read-only audit of branch `solr-18532-submit` at `348dd63d85a563c77e0a20b5742b2a0c845dc191` (base `3f5d4c5bf8ac`; two commits, `07a7ead4783` and `348dd63d85a`). The first check, `git rev-parse origin/solr-18532-submit`, returned `348dd63d85a563c77e0a20b5742b2a0c845dc191`, equal to the named head. No builds, tests, Gradle, gate or test-queue runs. No fetch, commit, push, Jira, GitHub or PR writes. Line numbers are at the head unless marked "base".

Ticket text: not in any local file I searched (the worktree, `session-jira.md`, and the Jira CSV export dated 2026-08-18, which predates the ticket). The only ticket text here is the paraphrase in `assignments/pool-flaky-fix-review-round-1.md`, slice 3.

## Verdict: HOLD for the draft

The test and its proof wording are sound by code reading, subject to the wording fixes below. Two owner decisions must be taken before any draft or opening:

1. SOLR-9865 carries the same RestoreCore rollback change and is gate green (`receipts/SOLR-9865.md`). The two branches change the same lines and would duplicate each other.
2. `reports/flaky-tests-root-cause-round-1-t1.md` says the rollback defect is not this flake's fix. A draft cannot present SOLR-18532 as the fix for the CI flake.

Nothing found here is a defect in the test itself.

## Findings

### F1. The new test exercises the target case (Q1, no fault found)

- Non-default case: lines 263-281. The first restore succeeds, so `index.properties` names a `restore.<ts>` directory. Line 281 asserts that prefix, so the test cannot pass vacuously.
- Failure path: lines 283-307. The segments file is deleted, the second restore fails after `modifyIndexProps` (line 214), and the rollback catch (lines 222-226) runs.
- Pointer check: the `assertEquals` at lines 312-315. The last statement is `verifyDocs` at line 318, so the receipt's phrase "final assertion" is inaccurate. Use "the pointer check".
- Base failure, by reasoning (not run): base `RestoreCore.java` line 227 deletes `index.properties`. `readIndexProperty` returns null on FileNotFound or NoSuchFile (lines 337-338). The assertion therefore receives null where it expects the first restore's directory. This matches the receipt.
- The test compiles on base. It uses only base APIs (`IndexFetcher.INDEX_PROPERTIES`, base `IndexFetcher.java` line 142; `DirectoryFactory.DirContext.META_DATA`; `SolrCore.getDataDir`). The base failure is therefore an assertion failure, not a compile error.
- It reads the persisted pointer, not the open core. The SOLR-9865 receipt records that its first test passed on base because the open core kept serving documents. This test avoids that trap.
- Head passes by reasoning: the rollback restores the captured bytes (lines 226, 288-307) before `newIndexWriter(false)` at line 230, so the writer reopens the restored index.
- Inherited timing: the 10 x 50 ms poll at lines 297-307 is copied from `testFailedRestore`. A slow failure makes the test fail (no AssertionError), not pass.
- Coverage gap (for Limits, not a blocker): only the non-null branch is asserted. The delete branch (lines 297-298) runs in the existing `testFailedRestore`, but no test asserts that the file is absent afterwards.

### F2. No existing test was weakened (Q5)

- Base to head, `TestRestoreCore.java` is 118 insertions and 0 deletions. `testSimpleRestore`, `testBackupFailsMissingAllowPaths` (line 176) and `testFailedRestore` are unchanged.
- The added code is the new test (lines 242-320), a private helper (lines 321-348), and imports.
- Test count: `@Test` annotations are at lines 98, 190 and 242. `testBackupFailsMissingAllowPaths` (line 176) has no annotation (base line 168). The receipt's "4 tests" matches the count of `test*` methods. I could not confirm from the branch that the runner executes an un-annotated `test*` method, and the JUnit XML the receipt cites is not on disk. A reader who counts `@Test` sees 3. Confirm the count before a draft says "4 of 4". This is the same open question as the SOLR-9865 `[CONFIRM: count]` placeholder.
- `RestoreCoreOpTest` has one `@Test` (`solr/core/src/test/org/apache/solr/handler/admin/RestoreCoreOpTest.java` line 27), which matches "1 test".

### F3. The history is out of the code, with two traces left (Q2)

- `07a7ead` to head changes only `TestRestoreCore.java` (44 insertions, 11 deletions). Production code and the changelog are identical in both commits.
- The `07a7ead` test read the file from disk with `Files.newInputStream` and called `props.load(in)` on an InputStream. That is the forbidden overload: `gradle/validation/forbidden-apis/defaults.all.txt` line 42 lists `java.util.Properties#load(java.io.InputStream)`.
- Head has no disk read. The pointer is read through the directory factory (lines 326-347). `props.load(new StringReader(...))` at line 344 is the Reader overload, which is not on the forbidden list (lines 42-44 list only InputStream load, save and store). Both defects are gone.
- The harness claim in the head comment is correct. `solr/core/src/test-files/solr/collection1/conf/solrconfig-leader.xml` line 22 sets `${solr.directoryFactory:solr.MockDirectoryFactory}`, and `MockDirectoryFactory` extends `EphemeralDirectoryFactory` (`solr/test-framework/src/java/org/apache/solr/core/MockDirectoryFactory.java` line 30). The test never calls `useFactory`, and I found no override in the Gradle config.
- Trace left in code: the comment at lines 265-266 ("rather than from the filesystem") contrasts with the rejected approach. Suggest cutting that clause. The Javadoc at lines 321-324 is fine.
- Trace left in commit messages: the body of `348dd63` describes the failed first gate and the forbidden overload, and uses the words "premise" and "final assertion". That text shows in the PR's commit list if the branch is opened as is. See owner decision 3.
- The changelog carries no history.

### F4. Changelog (Q4): passes, with one optional wording note

- `changelog/unreleased/SOLR-18532.yml`, 7 lines. The format matches `changelog/unreleased/SOLR-10198.yml`: title, type, authors (name only), links (name and url).
- Author: `Nick Shanin`. No placeholder (`ICLA pending` and `Solr Issues Workspace` are absent). No em dash or en dash appears in any added line of the branch diff (0 found).
- Type `fixed` is correct.
- Title: "A failed restore or install now rolls back to the previous index.properties instead of deleting it, so the pointer to a non-default index directory is no longer lost." "Install" is right: base `InstallCoreData.java` lines 91-92 call `RestoreCore.doRestore`.
- Nit: "instead of deleting it" reads as never deleting. The head code still deletes when the core had no `index.properties` before the restore (lines 297-298). Optional wording: "...rolls back to the previous index.properties, and deletes it only when the core had none before the restore."

### F5. Scope

- Three files, all within the ticket: `RestoreCore.java` (production, audited in slice 3a), `TestRestoreCore.java`, and the changelog. No unrelated edits. Both commits have author and committer `Nick Shanin`, and no Claude or co-author trailers.

## Proof claims in the receipt (Q3)

"Draftable" means a reader can check the claim from the branch or the receipt, so it may appear in the draft in plain words. "Internal" means it must not appear in the draft.

| Receipt claim | Checkable from branch | Status |
|---|---|---|
| GATE GREEN at 348dd63d85a563c77e0a20b5742b2a0c845dc191 | Yes, head SHA | Draftable (head SHA with verification date) |
| base main 3f5d4c5bf8ac | Yes | Draftable as base reference (optional) |
| Verification date 2026-10-10 (from the GATE RUNNER DONE timestamp) | Date is not on the branch | Draftable as date only; drop the time |
| Branch solr-18532-submit | Name only | Internal |
| Gate log g18532-gate.log on vm1 | No | Internal |
| GATE RUNNER DONE 2026-10-10T18:31:33Z | No | Internal (time and runner) |
| Seed 18532C0FFEE18532 | No | Internal (must not appear) |
| Steps 0 to 5 rc=0 | No | Internal (step labels, return codes) |
| Tidy tree clean | No | Internal (lint; pr-formula section 3) |
| Proof (step 3, pre-fix): RestoreCore reverted to the merge-base | Yes, by reverting the production change | Draftable in plain words, without "step 3" |
| TestRestoreCore.testFailedRestoreKeepsNonDefaultIndexPointer FAILS | Name yes; failure reproducible by revert | Draftable |
| "at its final assertion" | Inaccurate: the pointer check is at lines 312-315; verifyDocs (318) is last | Redraft as "at the pointer check" |
| "pointer was expected but null, because the old rollback deletes the file" | Yes: base line 227; head lines 337-338 | Draftable (the exact message appears only on a run) |
| "PREMISE SHAPE OK under the strengthened check ..." | Process vocabulary | Internal |
| Head run (step 4): TestRestoreCore 4 tests, 0 failures | Count per F2; pass result from the run | Draftable as "4 of 4" after the count is confirmed |
| RestoreCoreOpTest 1 test, 0 failures | Count checkable (1); pass from the run | Draftable as "1 of 1" with date and head |
| "(fresh JUnit XML)" | No | Internal |
| First gate at 07a7ead FAILED; forbidden overload; in-memory directory | Commit history | Internal (pr-formula section 3: a caught forbidden-API slip that does not change shipped behavior) |
| Main agent verified both legs before re-gating | No | Internal |

Gate file (`gates/SOLR-18532.md`), for the lead to correct (not edited here): it still shows head `07a7ead`, a QUEUED status line above a DONE line, and the earlier proof wording "failure text naming index.properties", which the receipt superseded. It also has no record of the failed first gate, although WORKFLOW.md (line 43) requires failures to be recorded in the gate file.

## Owner decisions (flagged, not taken)

1. **SOLR-9865 overlap.** Commit `4937608bb181` (branch `solr-9865-submit`, gate green per `receipts/SOLR-9865.md`) changes the same rollback in `RestoreCore.java` and adds a near-identical test (`testFailedRestoreAfterSuccessfulRestoreKeepsCurrentIndex`, `TestRestoreCore.java` line 183 at that commit). The mechanisms differ. SOLR-9865 writes the previous directory name back through `modifyIndexProps` and deletes only when that name is `index`. This branch writes the captured bytes back and deletes only when no file existed. Both touch the same lines, so they cannot land together. Nick decides which branch carries the rollback change, and whether the other is dropped or sequenced. Opening both would duplicate the work.
2. **Framing against the flake.** The root-cause report (`reports/flaky-tests-root-cause-round-1-t1.md`, Part 3) and the main RCA both say the rollback defect is not this flake's fix. Nick decides whether SOLR-18532 goes out as a standalone product fix with no flake claim, or waits. The ticket text is not local, so I could not check how the ticket frames the work.
3. **Commit history.** The branch has two commits. The `348dd63` message narrates the failed gate and uses internal words. Nick decides whether to squash or reword on the fork branch before any PR exists. The lead should first confirm that no PR is open.
4. **Count.** Confirm "4 tests" from the gate JUnit XML (not on disk) before a draft states "4 of 4". This is the same question as the SOLR-9865 `[CONFIRM: count]`.
5. **Gate record.** The lead should correct `gates/SOLR-18532.md` as described above.
6. **Coverage.** Add a no-prior-file assertion to the test, or name the gap in Limits. Nick decides.
7. **Title wording.** Optional nit in F4.

## Notes for slice 3a (production, not audited here)

- `RestoreCore.java` lines 300-307: if `createOutput`, `sync` or `renameWithOverwrite` throws, the `index.properties.<nanoTime>` temp file is left behind. There is no cleanup.
- Line 226 runs inside the catch block. An IOException from `restoreIndexProperties` replaces the SolrException and skips the calls at lines 228-230. Base line 227 had the same exposure, so this is not a regression.

## Checked, not run

Read-only git (`rev-parse`, `log`, `diff`, `show`, `grep`, `cat-file`, `for-each-ref`) and file reads only. Nothing was built, run or fetched.
