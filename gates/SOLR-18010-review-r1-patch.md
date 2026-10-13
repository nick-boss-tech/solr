# Gate job: SOLR-18010 review patch, round 1 (strong review of the four new draft PRs)

- Branch: `review-r1-solr-18010` on the fork. Head: `e99be67c9a6fe15f86306104843a30b35196f159`. Parent: `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (the head of draft PR #5118). Not the PR branch.
- Status: GATE RUN NOT GREEN at e99be67c9a6f (run 17:15:13Z to 17:38:49Z, log logs/vm2/queue-g18010-review-r1-gate-20261013T171513Z.out). Delta-only, tidy, spotless, Error Prone compile, focused head (rc=0), and module check all passed. Fail-before at the merge-base 14c7aac0d151 exited rc=1 with 4 of 11 focused tests failing (logs/vm2/18010-review-r1-failbefore.log, seed in that log): testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile (the new test; failed as step 6 expects, `previous security.json was changed`, expected.length=88 actual.length=0); testConcurrentEditsToLocalSecurityJson and testConcurrentPersistConfLeavesOneWholeDocument (pre-existing PR tests); BasicAuthStandaloneTest.testBasicAuth (`authentication config should not contain internal metadata`).
- Investigation (18:19Z, vm2 gate backlog coordinator): the three extra failures are PR-level tests whose fix is the PR's own persistConf change (temp file plus atomic move, present at parent 2b5ad801c9 and reverted by the merge-base revert). The merge-base is the wrong baseline for this review patch; the baseline the review patch changes from is its parent 2b5ad801c9, the same pattern step 7 of SOLR-18539 uses. The merge-base failure of the new test is therefore not evidence that the review patch's assertion pins anything beyond the truncating write at merge-base. Classification: gate design (fail-before baseline), not a branch defect and not a production defect. No branch change is needed.
- Next step (18:19Z): corrected fail-before at the parent 2b5ad801c9 (production SecurityConfHandlerLocal.java reverted to the parent, branch tests kept) for SecurityConfHandlerTest and BasicAuthStandaloneTest. Expected: only testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile fails. If it passes at the parent, the new test does not pin the review patch's change, and the review coordinator gets an assignment with that finding. Runner /workspace/gates/g18010-parent-failbefore.sh, log logs/vm2/18010-review-r1-failbefore-parent.log. Receipt and DONE follow the result.
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
