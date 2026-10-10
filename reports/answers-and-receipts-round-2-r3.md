# Answers and receipts round 2, part r3: SOLR-15003 and SOLR-18010

Result: SOLR-18010 is draftable (draft at `pr-drafts/core-admin/SOLR-18010.md`, Proof names head `c3685bb37d9d`); SOLR-15003 is held with no draft, because its receipt's count and its base proof do not hold against the branch.

## Findings

1. **FIX, `receipts/SOLR-15003.md` line 6 (counts).**
   Evidence: at `1004abee39ab`, `TestReplicationHandler.java` has 28 `@Test` methods (`git show ... | grep -c '@Test'`). It has 27 at `8f5b6f8360a` and 24 at the merge-base `b5c71bc5573`. The four new tests are `testFullCopyRemovesOldIndexDirWhenNoSnapshot` (line 221), `testFullCopyKeepsSnapshotFilesInOldIndexDir` (278), `testFullCopyWithReloadRemovesOldIndexDir` (384) and `testDeleteNamedSnapshotWithMissingIndexDir` (473). The class is `@LuceneTestCase.Nightly` (line 97). The receipt's "5 tests" is not the class count, and the receipt names neither the filter nor the tests. A maintainer who runs the class sees 28 tests.
   Replacement for line 6: `Counts: focused run at 1004abee39ab, filter <exact --tests filter from the log>: <N> run, <F> failures, from g15003r36fix-gate.log. Tests run: <names from the log>. The class has 28 test methods at this head.` The values come from the log, which is not on disk here.

2. **FIX, `receipts/SOLR-15003.md` line 7 (proof baseline).**
   Evidence: the only recorded failure is step 3a at `8f5b6f8360a`. The fail-before procedure in the workspace AGENTS.md uses the merge-base `b5c71bc5573` with the branch's `src/test` files overlaid. The receipt records steps 3b and 3c as passing on base, and no base failure at all. By reading, base `IndexFetcher.java` lines 744 to 750 remove the old index directory unconditionally after a full copy. Commit `a97a03a340d` removed that call, and `afeab98ea82` added the post-reload cleanup. So the reload test probably passes on base too. The likeliest base failure is `testFullCopyKeepsSnapshotFilesInOldIndexDir` (line 278), which expects snapshot files to survive a full copy. Nothing here ran, so this is by reading.
   Replacement for line 7: `Fail-before at the merge-base b5c71bc5573, head src/test files overlaid: <test names and failing assertions from the log, or NOT_PROVEN>. Intermediate check at 8f5b6f8360a, head TestReplicationHandler.java overlaid: testFullCopyWithReloadRemovesOldIndexDir fails at its removal assertion (5c6e8753a39 added that test). Steps 3b, 3c and 4: <list from the log>.`

3. **NOTE, `receipts/SOLR-15003.md` line 7 (step labels and overlay).**
   Evidence: "3a", "3b", "3c" and "4" appear only in the receipt and the gate log, not in the code. The reload test does not exist at `8f5b6f8360a` (`git show 8f5b6f8360a:...` has no match, and `git log -S` names `5c6e8753a39`). So step 3a must have run the head's test file against that production. The receipt does not say so. FIX 2's replacement covers it.

4. **NOTE, `receipts/SOLR-15003.md` lines 5 and 8 (logs).**
   Evidence: `g15003r36fix-gate.log` is not on disk. An exact-name search of `research/` and the worktree found nothing, and a depth-7 find of the workspace (excluding `source/` and `wt/`) found nothing either. The GitHub run `37764360597` is not checked, since this part makes no `gh` reads. No replacement.

5. **NOTE, `receipts/SOLR-15003.md` line 5 (time of day).**
   Evidence: commit `1004abee39a` has author and committer time `2026-10-08T10:33:48Z`. The gate line says `GATE RUNNER DONE 2026-10-08` with no time. The gate names this exact head, so it ran after the commit. Optional replacement: `GATE RUNNER DONE 2026-10-08 hh:mm UTC`.

6. **NOTE, `changelog/unreleased/SOLR-15003.yml` line 1 (title).**
   Evidence: the title matches the code. The skip for a missing index directory is in `SolrCore.java`, the post-reload cleanup is in `IndexFetcher.java` (`afeab98ea82`), and files pinned by named snapshots are kept. The YAML was not parsed: no YAML parser and no Python on this machine. By inspection, the value is one plain scalar with no `: ` or ` #` and no dash characters. No replacement.

7. **FIX, `receipts/SOLR-18010.md` line 8 (settling run "on disk").**
   Evidence: the receipt says the settling run is "found and on disk: g18010-settle.log". No file with that name exists. An exact-name search of `research/` and the worktree found nothing, and the depth-7 find found no `g18010` file. The receipt's figures (forced interleave 5 of 5, unforced barrier 4 of 50) were measured at base `14c7aac0d15`. The forced interleave 5 of 5 was also measured at `fadc5ee31e3`. They do not measure the live tip.
   Replacement for line 8, first sentence: `The settling run is recorded in the main side's takeover log (2026-10-04). It is not on disk in the review workspace. Its figures were measured at base 14c7aac0d15 (forced interleave 5 of 5, unforced barrier 4 of 50) and at fadc5ee31e3 (forced interleave 5 of 5). They do not measure the tip.`

8. **FIX, `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml` line 1 (title overclaims).**
   Evidence: the title says concurrent edits "can no longer corrupt the file or overwrite each other." The lock covers POST edits inside one `SecurityConfHandlerLocal` instance (lines 44 to 66). The atomic write covers torn writes (lines 103 to 147). The two new tests cover two overlapping edits and two overlapping writes in one process. Two Solr processes that share one SOLR_HOME are not covered, and cloud mode is outside the change (`CoreContainer.java` line 851 uses `SecurityConfHandlerZk`). This is the k2 FIX 12 concern in narrower form.
   Replacement for line 1 (exact): `title: Standalone security.json edits are serialized on each node and written atomically, so overlapping edits no longer lose each other's changes or leave a partly written file; the file also no longer stores the internal version marker`
   Effect: a new commit moves the head. The draft names `c3685bb37d9d` and must be re-headed to the new commit before posting.

9. **NOTE, `receipts/SOLR-18010.md` line 7 (premise runs).**
   Evidence: `g18010-premise.log` is not on disk. By reading, both new tests fail on base. Base `SecurityConfHandlerLocal.java` lines 86 and 87 open the file, which empties it, and then write. The base edit path (`SecurityConfHandler.java` lines 124 to 151) takes no lock. A held first write or a held first edit reproduces the hybrid document and the lost edit. `testEdit` is unchanged. The draft states the base failures as the receipt's record, not as a run here.

10. **NOTE, `SecurityConfHandlerTest.java` (persist test byte comparison).**
    Evidence: `Utils.getDeepCopy` keeps `LinkedHashMap` order (`solrj/.../Utils.java` lines 122 to 141), and the test data uses `LinkedHashMap`. So the sanitized write equals `shortBytes` or `longBytes` as the test expects. No change.

11. **NOTE, 18010 counts and base (checked against the code).**
    Evidence: `SecurityConfHandlerTest` has three test methods (`testEdit` line 55, `testConcurrentEditsToLocalSecurityJson` line 203, `testConcurrentPersistConfLeavesOneWholeDocument` line 271). `BasicAuthStandaloneTest` has one (`testBasicAuth` line 68). `V2SecurityAPIMappingTest` has five `@Test` methods and the branch does not change it. The merge-base with `upstream/main` (`8e62c2686882`) is `14c7aac0d15`, so the receipt's base is the right one. The receipt's gate finished on 2026-10-05, and the commit is dated `2026-10-05T06:51:48Z`. The gate names this head, so the ordering is fine. No change.

12. **NOTE, Admin UI example in the changelog and draft.**
    Evidence: base `security.js` lines 1248 to 1283 loop over the permissions and post `update-permission` (line 1265) and `set-permission` (line 1275) with no wait. The hydrated JIRA text (`research/jira-context/SOLR-18010.json`) does not mention the Admin UI. The draft cites the UI code, not the ticket. No change.

13. **NOTE, no Choice section for 18010.**
    Evidence: the record holds no design decision. The in-process lock versus a cross-process file lock is the alternative a maintainer might raise. The draft's Limits names the gap and offers the follow-up. No change.

14. **NOTE, dash and process-word scan.**
    Evidence: no em or en dash bytes in either receipt, either changelog, or the 18010 draft. The 18010 draft has no process words (gate, receipt, round, takeover, premise, settle, claim, harden, fail-before).

## Task results

**SOLR-15003: held, no draft.** The gated head `1004abee39ab` matches the live tip. The production change `afeab98ea82` matches the receipt's description and the changelog. The receipt's count of 5 does not match the class, which has 28 tests with four new ones, and it names no tests (FIX 1). The only recorded failure is at `8f5b6f8360a` with the head's test file overlaid, not at the merge-base, and no base failure is recorded (FIX 2). By the fail-before procedure, the proof is NOT_PROVEN as recorded. What changed since k6: k6 held the tip with a gate owed. The refreshed receipt now records a green gate at the tip, which closes that item on paper. The counts and the baseline are new and are not supported by the branch as recorded. k6's "three commits past the gated head" wording is not in the receipt. Draftable once FIX 1 and FIX 2 are filled from the log.

**SOLR-18010: draftable.** Draft written at `pr-drafts/core-admin/SOLR-18010.md`, with the Proof naming head `c3685bb37d9d74e4ed538d6d1b65bb1b60970677`. The live tip matches the gated head. The counts 3, 1 and 5 match the code (NOTE 11). Both concurrent-edit tests read as failing on the base code, and the lock and atomic write address both (NOTE 9). What changed since k2: k2 held the ticket because the tip had no gate and the settling record was unread. The receipt now records a gate at the tip, and the concurrency claim can be checked against the tests. Still open before posting: the settling record is not on disk, though the receipt says it is (FIX 7), and the title overclaim remains in narrower form (FIX 8). The draft does not use the settle figures. If FIX 8 lands, the head moves and the draft must be re-headed.

## Not checked

- No builds, Gradle, tests, or `gh` calls. Counts are the receipts', checked against the code by `@Test` counts and method names. Pass and fail claims at base and `fadc5ee31e3` are read, not run.
- Not on disk: `g15003r36fix-gate.log`, `g18010-gate.log`, `g18010-premise.log`, `g18010-settle.log`, the takeover log, and GitHub run `37764360597`. Exact-name searches of `research/` and the worktree, and a depth-7 find of the workspace excluding `source/` and `wt/`, found none.
- The changelog YAML was not parsed (no YAML parser and no Python here). Checked by inspection only.
- The receipts' tidy, Error Prone and module-check results are not verifiable here.
- The 18010 draft's "testEdit passes on the base code" comes from the receipt only.
- No trial merges, and no check for drift against upstream main.
- The 15003 base removal (IndexFetcher lines 744 to 750) and the likely base failing test are from reading, not from a run.
- The Admin UI behavior is from reading `security.js`, not from a browser run.
- Live PR state for 15003 and 18010 was not checked (no `gh` reads in this part).
- Parts p1 and r2 were not touched. The only file created is `pr-drafts/core-admin/SOLR-18010.md`, plus this report.
