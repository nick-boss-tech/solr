# Gate job: SOLR-15003 proof recount and fail-before at the merge base

- Status: FAILED 2026-10-14, handed to the main agent (see the gate run record at the end). Not DONE, receipt not changed.
- Branch: solr-15003-submit. Live head: 1004abee39ab07d5eae1a5ea6327572997ff50d2 (verified by `git ls-remote origin refs/heads/solr-15003-submit` on 2026-10-14; matches the gated head in `receipts/SOLR-15003.md`).
- Merge base for the fail-before leg: b5c71bc5573c4e31b4cee5a7965d73587fc0ae58 (`git merge-base upstream/main 1004abee39ab`).
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.TestReplicationHandler` (nightly; run with nightly tests enabled, as the original gate did).
- Intended host: any capable Linux host with the Gradle toolchain, under the WORKFLOW.md claim rules. Windows never claims build work.
- Capability tags: gate.
- Job type: record repair on a gated branch. No production or test code change is expected. No push is expected. The job refreshes `receipts/SOLR-15003.md` lines 6 and 7 only.

## Why this job exists

The receipt's count line reads "TestReplicationHandler 5 tests, 0 failures at the head". The class has 28 `@Test` methods at the head, so the number cannot be read against the class, and the receipt names neither the filter nor the tests. The four new tests are `testFullCopyRemovesOldIndexDirWhenNoSnapshot`, `testFullCopyKeepsSnapshotFilesInOldIndexDir`, `testFullCopyWithReloadRemovesOldIndexDir` and `testDeleteNamedSnapshotWithMissingIndexDir`.

The receipt's fail-before line records one failure, at step 3a, on the pre-fix production code `8f5b6f8360a` with the head's test file overlaid. It records no failure at the merge base. The steps are labelled 3a, 3b, 3c and 4 only in the receipt and the log, and the log is not on disk in the review workspace.

Sources: `reports/answers-and-receipts-round-2-r3.md` (FIX 1 and FIX 2, with replacement wording), `reports/answers-and-receipts-round-2.md` (SOLR-15003 details), and the SOLR-15003 entry in `material/core-admin-round-1-answers.md`. The draft `pr-drafts/core-admin/SOLR-15003.md` is not written until this job is DONE.

## Steps

1. **Focused run at the head.** Run `TestReplicationHandler` with an exact `--tests` filter that names the four new tests, with nightly tests enabled. Count the tests from fresh JUnit XML. Record: the exact filter text, the number of tests run, the number of failures and errors, and each test name that ran. Also record the class total from the same run's XML or from `grep -c '@Test'` at the head (expected 28).
2. **Fail-before at the merge base.** Build a throwaway tree at `b5c71bc5573` and overlay only `solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java` from the head. Run the same filter. For each of the four new tests, record PASS or FAIL and the failing assertion text. Verdict per the workspace procedure: PASS (failed without the fix), NOT_PROVEN (passed without the fix), or INCONCLUSIVE (did not compile, or log shows cluster or timeout noise). A test that does not compile on the base is recorded as INCONCLUSIVE for that test, with the compile error line.
3. **Intermediate check at `8f5b6f8360a`.** Overlay the head's `TestReplicationHandler.java` on the production tree at `8f5b6f8360a` and run `testFullCopyWithReloadRemovesOldIndexDir` alone. Record the failing assertion text (the removal assertion, if that is what fails). This confirms the receipt's step 3a with its test name.
4. **Changelog parse.** Parse `changelog/unreleased/SOLR-15003.yml` with a real YAML parser at the head. Record the result.

The tidy, Error Prone compile and module check results already in the receipt stand for the head. This job does not owe them again unless step 1 changes a file.

## Gate failure rule

If step 1 records a failing test at the head, do not relaunch. Record the failing test, the seed, the reproduce line and a classification in this file first (the coordinator handoff rule). The main agent decides whether the failure blocks the ticket.

## On green (every step recorded)

- Replace `receipts/SOLR-15003.md` line 6 with: "Counts: focused run at 1004abee39ab, filter `<exact filter>`: <N> run, <F> failures, <date>. Tests run: <names>. The class has 28 test methods at this head."
- Replace `receipts/SOLR-15003.md` line 7 with: "Fail-before at the merge base b5c71bc5573, head test file overlaid: <per-test outcomes and assertion text, verdict>. Intermediate check at 8f5b6f8360a: testFullCopyWithReloadRemovesOldIndexDir fails at <assertion text>."
- Mark this job DONE here, with the date and the receipt lines changed. Mark the claim DONE in the same push.
- Push: none. This job edits only the receipt and this file.

## Step checklist

- [ ] step 1: focused run at the head, filter, counts, test names (from fresh JUnit XML)
- [ ] step 2: fail-before at `b5c71bc5573` with the head test file overlaid, per-test verdicts
- [ ] step 3: intermediate check at `8f5b6f8360a`, reload test assertion text
- [ ] step 4: changelog YAML parse at the head
- [ ] receipt lines 6 and 7 replaced; this job DONE; claim DONE

## Gate run record (coordinator, 2026-10-14T09:25Z)

Run: gates/g15003-proof-gate.sh at head 1004abee39ab, log logs/vm2/queue-g15003-proof-gate-20261014T090951Z.out, finished 2026-10-14T09:24Z.

- Step 1, head focused run: rc=0, BUILD SUCCESSFUL (15003-head.log). The filter, test counts and names were not extracted from the XML in this record.
- Step 2, fail-before at b5c71bc5573 with the head test file overlaid: rc=1 (15003-mb.log). "4 tests completed, 1 failed." FAILED: testFullCopyKeepsSnapshotFilesInOldIndexDir, AssertionError "old index directory must be retained while a snapshot references it". The other three new tests passed at the merge base, so their verdict is NOT_PROVEN.
- Step 3, intermediate at 8f5b6f8360a, testFullCopyWithReloadRemovesOldIndexDir alone: rc=0, BUILD SUCCESSFUL (15003-pre.log). The receipt's step 3a says this test fails there. It passed.
- Step 4, changelog YAML parse at the head: rc=0, YAML OK.

Classification: not GREEN. Step 2 gives a partial fail-before, and step 3 contradicts the receipt. Not relaunched. The main agent decides whether receipts/SOLR-15003.md line 7 can stand and whether the other three new tests need a fail-before proof.
