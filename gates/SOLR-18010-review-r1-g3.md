# Gate job: SOLR-18010 review round 1, leg G3 (focused set at the patched review head)

- DEPENDENCY, read first: this job runs only after vm2's corrected parent-baseline fail-before for the SOLR-18010 review patch has recorded its result in `gates/SOLR-18010-review-r1-patch.md`. That corrected run reverts production to the parent `2b5ad801c9e8` (not the merge-base) with the review branch's tests kept; until its result is recorded there, this job is not claimable. If that result finds the new test does not pin the review patch's change, stop and report instead of running: the finding goes to the review coordinator first.
Status: DONE (GATE GREEN at e99be67c9a6f)
- Source job: `gates/SOLR-18010-permissions-fail-before-and-multiauth.md`, leg G3 (the legs G1 and G2 of that file are DONE at `2b5ad801c9e8`). This file carries G3 as its own job so the gate buffer holds it as claimable work once the dependency clears.
- Branch: `review-r1-solr-18010` on the fork (the strong review's patch branch, not the PR branch). Head: `e99be67c9a6fe15f86306104843a30b35196f159`; confirm by ls-remote before the run and stop if the tip has moved.
- Capability tags: gate. Intended host: vm1 or vm2 (Linux only). Windows never claims build work.
- What the review patch changes, for the run's context: in `SecurityConfHandlerLocal.persistConf`, a failed permission copy (`IOException`) is logged at warn with the path instead of debug (`UnsupportedOperationException` stays at debug), and the `finally` delete of the temporary file no longer lets a delete `IOException` replace the write error; plus the new test `testFailedWriteKeepsPreviousFileAndLeavesNoTemporaryFile` in `SecurityConfHandlerTest`.

## Run

Focused set at the head, counted from fresh JUnit XML, seed `18010C0FFEE18010`:

- `org.apache.solr.handler.admin.SecurityConfHandlerTest` (expect 5 tests, including the new failed-write test)
- `org.apache.solr.security.BasicAuthStandaloneTest` (expect 1 test)
- `org.apache.solr.handler.admin.V2SecurityAPIMappingTest` (expect 5 tests)
- `org.apache.solr.security.MultiAuthPluginTest` (expect 6 tests; this class sends its standalone HTTP POSTs through the changed write path and the new edit lock, and leg G2 of the source job already ran it at the unpatched head)

Module: `:solr:core`. Gate shape: changelog YAML parse for any fragment in the delta, tidy with the GATE INVALID post-tidy guard (restore `gradle/libs.versions.toml`), Error Prone compile (`-Pvalidation.errorprone=true`), the focused run above, then `:solr:core:check -x test`.

## On completion

- Record the counts from the fresh JUnit XML in this file and mark it DONE; mark the claim DONE in the same push.
- Refresh `receipts/SOLR-18010.md` with the G3 result at `e99be67c9a6f`.
- A test-executing failure is not relaunched by the claiming host; record the failing test, seed and counts here and report, per WORKFLOW.md.
- No fork push and no PR edit under this job; moving the patch onto `solr-18010-submit` is a main-side step after the review loop accepts the gate.

## Result, 2026-10-13 (vm2, GATE GREEN at e99be67c9a6f)

- Head confirmed unchanged before the run (ls-remote and local `rev-parse` both `e99be67c9a6f`). Seed `18010C0FFEE18010`.
- Tidy rc=0 with a clean tree after restoring `gradle/libs.versions.toml`; Error Prone `:solr:core:compileTestJava -Pvalidation.errorprone=true` rc=0.
- Focused run rc=0, fresh JUnit XML (`logs/vm2/18010-review-r1-g3-junit/`): SecurityConfHandlerTest 5 of 5, V2SecurityAPIMappingTest 5 of 5, BasicAuthStandaloneTest 1 of 1, MultiAuthPluginTest 6 of 6; skipped 0, failures 0, errors 0. Total 17 of 17.
- Module check `:solr:core:check -x test` rc=0 (log `logs/vm2/18010-review-r1-g3-check.log`). Final tree clean.
- Logs: `logs/vm2/18010-review-r1-g3-tidy.log`, `18010-review-r1-g3-compile.log`, `18010-review-r1-g3-focused.log`, `18010-review-r1-g3-check.log`; runner `gates/g18010-review-r1-g3.sh` on the coordinator side.
- This is the review branch `review-r1-solr-18010`, not `solr-18010-submit`. No fork push, no PR touched.
