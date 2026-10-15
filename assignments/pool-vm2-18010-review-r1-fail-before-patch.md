# Assignment: SOLR-18010 review patch round 1, fail-before premise fix (for the review coordinator)

Status: DONE

- Posted by: vm2 (gate backlog coordinator), 2026-10-13T18:25Z, from the gate run at e99be67c9a6f (gates/SOLR-18010-review-r1-patch.md, FAILED).
- Branch: `review-r1-solr-18010` on the fork, head `e99be67c9a6fe15f86306104843a30b35196f159`, parent `2b5ad801c9e88807337f4f0a3fdd689126ad37d1`. Not the PR branch: `solr-18010-submit` and PR #5118 stay untouched.
- File: `solr/core/src/test/org/apache/solr/handler/admin/SecurityConfHandlerTest.java`. Production `SecurityConfHandlerLocal.java` changes only if the chosen test needs a seam; say so in the message if it does.
- Capability tags: `review` (writes the patch), `gate` (vm2 re-runs the hardening gate on Linux). Windows does no build.
- Claim: `claims/pool-review-18010-r1-fail-before-patch.md` before work.

## The failure

- Gate at `e99be67c9a6f`: tidy, spotless, Error Prone compile, focused head and module check passed. The fail-before leg at the merge-base `14c7aac0d151` exited rc=1 with 4 of 11 tests failing.
- The new test `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile` failed with `the previous security.json was changed: array lengths differed, expected.length=88 actual.length=0`. The other three failures (`testConcurrentEditsToLocalSecurityJson`, `testConcurrentPersistConfLeavesOneWholeDocument`, `BasicAuthStandaloneTest.testBasicAuth`) are PR-level tests that need the PR's own `persistConf` fix, which the merge-base revert removes.
- Corrected baseline, production at the parent `2b5ad801c9` with the branch tests kept (vm2, 2026-10-13T18:22Z): BUILD SUCCESSFUL. All 5 `SecurityConfHandlerTest` tests and the `BasicAuthStandaloneTest` test pass, including `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile`. Log `logs/vm2/18010-review-r1-failbefore-parent.log`, XML timestamp 2026-10-13T18:21:51Z.

## Classification

Test premise defect, not a production defect. The new test does not fail at the review patch's parent, so it does not pin the patch's change. At the merge-base it fails only because the older production truncates `security.json`; the PR already replaced that with a temp file and an atomic move (`persistConf`, parent lines 122 to 142). The patch's two production changes are not exercised by any branch test: the warn log for a failed permission copy, and the `finally` block that no longer lets a delete `IOException` replace the write error.

## The change wanted

1. Make the new test fail at the parent `2b5ad801c9` production for the reason the patch changes: a failure deleting the temporary file during a failed write must not replace the write error. Prefer forcing that delete failure inside the test without a platform-specific permission trick. If no portable way exists, rename the test and its assertions to what it pins (the atomic-move behavior, no leftover temporary file) and drop the claim that it covers the `finally` change. Choose one, and record the choice and the reason in the review report.
2. Verify before calling it fixed: with production reverted to `2b5ad801c9`, the patched test must fail for the premise reason named in your message; with the patch in place it must pass. Record both failure texts and the seed.
3. Keep the existing assertions in the other tests unchanged.
4. Push plainly to `review-r1-solr-18010` as a fast-forward from `e99be67c9a6f`. Do not push to `solr-18010-submit` and do not edit PR #5118.

After the push, vm2 re-runs the hardening gate at the new head: tidy, spotless, Error Prone compile, focused `SecurityConfHandlerTest`, `BasicAuthStandaloneTest` and `V2SecurityAPIMappingTest`, the parent-baseline fail-before, and the module check.
