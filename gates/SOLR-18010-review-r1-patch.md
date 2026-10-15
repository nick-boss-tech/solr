# Gate job: SOLR-18010 review patch, round 1 (strong review of the four new draft PRs)

- Branch: `review-r1-solr-18010` on the fork. Head: `e99be67c9a6fe15f86306104843a30b35196f159`. Parent: `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (the head of draft PR #5118). Not the PR branch.
Status: DONE
- Investigation (18:19Z, vm2 gate backlog coordinator): the three extra failures are PR-level tests whose fix is the PR's own persistConf change (temp file plus atomic move, present at parent 2b5ad801c9 and reverted by the merge-base revert). The merge-base is the wrong baseline for this review patch; the baseline the review patch changes from is its parent 2b5ad801c9, the same pattern step 7 of SOLR-18539 uses. The merge-base failure of the new test is therefore not evidence that the review patch's assertion pins anything beyond the truncating write at merge-base. Classification: gate design (fail-before baseline), not a branch defect and not a production defect. No branch change is needed.
- Next step (18:19Z): corrected fail-before at the parent 2b5ad801c9 (production SecurityConfHandlerLocal.java reverted to the parent, branch tests kept) for SecurityConfHandlerTest and BasicAuthStandaloneTest. Expected: only testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile fails. If it passes at the parent, the new test does not pin the review patch's change, and the review coordinator gets an assignment with that finding. Runner /workspace/gates/g18010-parent-failbefore.sh, log logs/vm2/18010-review-r1-failbefore-parent.log. Receipt and DONE follow the result.
## Investigation, main side first pass (2026-10-13 ~12:22 MDT)

- Failing step: step 6, the fail-before leg. Tests executed: the 11 focused tests ran with production reverted to the merge-base 14c7aac0d151; 4 failed, rc=1. Every other leg passed: delta-only diff scope, tidy, spotless, Error Prone compile, the focused head run at e99be67c9a6f (rc=0), and the module check.
- The new test failed as step 6 designs: testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile, with the assertion `previous security.json was changed`, expected.length=88 actual.length=0, which is the merge-base truncating write (the base persistConf truncates the file before the document is written).
- The three further failures are tests whose passing behavior the parent head 2b5ad801c9e8's own persistConf change provides: testConcurrentEditsToLocalSecurityJson, testConcurrentPersistConfLeavesOneWholeDocument, and BasicAuthStandaloneTest.testBasicAuth (`authentication config should not contain internal metadata`). The step 6 revert to the merge-base removed the parent PR branch's persistConf change (temp file, atomic move, version-marker handling) along with the review patch, so these three failures are the parent PR's own premise showing at the merge-base, not the review patch's doing.
- Classification: gate design, fail-before baseline. The review patch changes from its parent 2b5ad801c9e8, not from the merge-base, so step 6 named the wrong baseline (the SOLR-18539 job's step 7 uses the parent pattern). Not a defect in the patch's new code: the head run passed with the warn-level permission-copy logging and the finally guard in place, and the fail-before leg does not exercise that delta at all. Not tidy or changelog YAML: both checks passed and no changelog file is in this delta. Not environment: every leg executed and no refusal is recorded anywhere in the run's records.
- Record gaps, stated exactly: the fail-before seed is recorded only in vm2's log logs/vm2/18010-review-r1-failbefore.log and is not on this branch; the full failing-test list with assertion messages exists only in logs/vm2/queue-g18010-review-r1-gate-20261013T171513Z.out on vm2, which this host cannot read. The four failing test names and the new test's assertion text are on the branch (this file and reports/vm2-queue-monitor.md), so the classification does not rest on the missing pieces. Copying the vm2 log's failing list onto the branch would close the record gap.
- What settles the remaining question: whether the new test also fails with production at the parent 2b5ad801c9e8 and the branch tests kept. If it passes there, the new test pins only the merge-base truncating write and not the review patch's change, and that finding goes to the review coordinator. The vm2 gate backlog coordinator has already claimed that corrected run (18:19Z, runner /workspace/gates/g18010-parent-failbefore.sh, log logs/vm2/18010-review-r1-failbefore-parent.log). This investigation relaunches nothing. The gate stays NOT GREEN as recorded; before any re-run counts as the gate of record, step 6's baseline is corrected to the parent.

- Capability tags: gate.
- Intended host: Linux only (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Source: `reports/four-new-prs-strong-review-round-1.md`, section SOLR-18010.
- Job type: full focused gate with a fail-before run.

## Changes to check

1. `SecurityConfHandlerLocal.persistConf`:
   - a failed permission copy (`IOException`) is now logged at warn, with the path, instead of debug. `UnsupportedOperationException` (no POSIX view, the Windows case) stays at debug, so Windows does not warn on every edit.
   - the `finally` that deletes the temporary file no longer lets an `IOException` from the delete replace the error from the write; it logs at warn.
2. `SecurityConfHandlerTest`: new `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile`. A credential value whose `CharSequence` methods throw is serialized after the temporary file exists. The test expects a `SolrException` "Failed persisting security.json", the previous bytes unchanged, and no `security.json*` file other than `security.json` in the folder.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/review-r1-solr-18010` equals `e99be67c9a6fe15f86306104843a30b35196f159`.
2. Diff scope against `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`: `SecurityConfHandlerLocal.java` and `SecurityConfHandlerTest.java` only.
3. Tidy first; same rule as the SOLR-18539 job for a formatting-only diff in these two files. Spotless check on `:solr:core`.
4. Error Prone compile.
5. Focused run: `SecurityConfHandlerTest`, `BasicAuthStandaloneTest`, `V2SecurityAPIMappingTest`, fresh JUnit XML. Expect 5, 1 and 5 tests, 0 failures. Record the seed.
6. Fail-before for the new test: revert `SecurityConfHandlerLocal.java` to the merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` and keep the branch tests. The new test should fail because the truncating write changes the previous file. If the merge-base production does not compile against the branch tests, record that and use production at `fadc5ee31e3` instead. Record the failure text.
7. Module check `:solr:core:check`.
8. On green: add a section to `receipts/SOLR-18010.md` for this review branch head. Do not push to `solr-18010-submit` and do not edit PR #5118. Mark this job DONE.

## Checklist

- [ ] head confirmed
- [ ] diff scope
- [ ] tidy and spotless
- [ ] Error Prone compile
- [ ] focused run
- [ ] fail-before recorded
- [ ] module check
- [ ] receipt section; job DONE

## Corrected baseline and classification (18:24Z, vm2)

- Corrected fail-before at the parent `2b5ad801c9` (production `SecurityConfHandlerLocal.java` reverted to the parent, branch tests kept), `SecurityConfHandlerTest` and `BasicAuthStandaloneTest`: BUILD SUCCESSFUL, 6 of 6 passed (log `logs/vm2/18010-review-r1-failbefore-parent.log`, XML 2026-10-13T18:21:51Z). The new test does not fail at its parent.
- Classification: test premise defect. The new test pins the PR's own atomic-move fix and does not exercise the patch's change (the warn log for a failed permission copy, and the `finally` that no longer lets a delete failure replace the write error). Not a production defect.
- Status: FAILED (test-executed run, classified). Next step owned by the review coordinator: `assignments/pool-vm2-18010-review-r1-fail-before-patch.md` (patch the test, then vm2 re-gates at the new head). No receipt written; job not DONE; no push to `solr-18010-submit`; PR #5118 untouched.

## Route 1 pushed (2026-10-13, main side)

- The review coordinator's route 1 patch (`gates/SOLR-18010-fail-before-route1.patch`, test-only: the failed-write test now replaces the temporary file with a non-empty directory of the same name during serialization, so the parent production's `finally` delete throws `DirectoryNotEmptyException` and masks the write error; the test is renamed `testFailedWriteReportsWriteErrorWhenTemporaryFileCannotBeDeleted`) is pushed to `review-r1-solr-18010` as `00cf8148c3218925994c43d3dea6bb8a4099a62e`, a plain fast-forward from `e99be67c9a6f`, with the owner's go-ahead. On vm1 it applied cleanly, matched the patch file exactly after tidy, and `:solr:core:compileTestJava -Pvalidation.errorprone=true` returned BUILD SUCCESSFUL. No test run on vm1.
- Next step: vm2 runs the corrected gate at the new head `00cf8148c3`, with the fail-before leg at the parent baseline `2b5ad801c9` per the classification above (expected at the parent: the renamed test fails with `expected:<SolrException> but was:<DirectoryNotEmptyException>`; at the new head it passes). The gate records a seed and the head SHA with its runs, per the round 1 report's gaps.

## Re-gate at the route 1 head (vm2, 2026-10-13T20:19Z)

- Head: `00cf8148c3218925994c43d3dea6bb8a4099a62e`, confirmed by ls-remote at claim time. Claim: `claims/pool-vm2-gate-backlog-18010-review-r1-g4.md`. Runner: `/workspace/gates/g18010-review-r1-g4.sh`. Logs: `logs/vm2/18010-review-r1-g4-*.log`.
- Gate shape: tidy with the clean-tree guard, Error Prone compile, focused `SecurityConfHandlerTest` at the head (5 expected, including `testFailedWriteReportsWriteErrorWhenTemporaryFileCannotBeDeleted`), fail-before at the parent `2b5ad801c9e8` with the branch test kept (expected: the renamed test fails with `java.nio.file.DirectoryNotEmptyException` at its `assertThrows` line), production restored, module check `:solr:core:check -x test`.
- Status: DONE 2026-10-13T20:40Z by vm2, GATE GREEN at `00cf8148c3218925994c43d3dea6bb8a4099a62e` (GATE END 20:40:22Z). Seed `18010C0FFEE18011`.
- Result: tidy clean (status empty after tidy), spotless rc=0, Error Prone compile rc=0. Focused head run rc=0, fresh JUnit XML: SecurityConfHandlerTest 5 of 5, V2SecurityAPIMappingTest 5 of 5, BasicAuthStandaloneTest 1 of 1; 11 of 11, skipped 0, failures 0, errors 0. Module check `:solr:core:check -x test` rc=0; final tree clean.
- Fail-before at the parent `2b5ad801c9e8` with the branch test kept (production reverted to the parent, restored afterwards): SecurityConfHandlerTest 4 of 5 pass, 1 fails. The failing test is `testFailedWriteReportsWriteErrorWhenTemporaryFileCannotBeDeleted`, line `assertThrows`, `java.lang.AssertionError: unexpected exception type thrown; expected:<org.apache.solr.common.SolrException> but was:<java.nio.file.DirectoryNotEmptyException>`, the premise reason named in the assignment.
- Verdict: the renamed test fails at the parent for the reason the route 1 change targets and passes at the review head, so the premise is settled on Linux. Receipt section added to `receipts/SOLR-18010.md`. No fork push, no PR edit; `solr-18010-submit` untouched.
- Logs: `logs/vm2/18010-review-r1-g4-*.log`, XML in `logs/vm2/18010-review-r1-g4-junit-head/` and `logs/vm2/18010-review-r1-g4-junit-parent/`. Runner `/workspace/gates/g18010-review-r1-g4.sh`.
