# Gate job: SOLR-10305 live-head gate at 52e32f892c

Status: CLAIMED (claims/pool-vm1-gate-10305-live-head.md)

- Branch: solr-10305-submit. Head: `52e32f892c5e8ccdb474c8fc222ce8203955efd3` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at run time, stop and record the new tip here.
- Prior gated head: `86ddbd433dba9852f30880b184b3ec2bc14180e4` (full gate GREEN, receipts/SOLR-10305.md). That evidence does not cover the one commit on top of it, 52e32f892c, which this job gates. Merge-base with upstream main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Intended host: vm1 (Nick directed this run on 2026-10-15 evening).
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.MinimalSchemaTest` (package copied from the file on the branch). Seed: `10305C0FFEE10305`.
- Diff scope of 52e32f892c against 86ddbd433dba: 2 files. `solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java`: in `prepare`, the distributed uniqueKey check moves above the `query=false` early return (`params.getBool(COMPONENT_NAME, true)`), so a shards request with query=false on a schema without a uniqueKey is rejected with the 400 instead of slipping past the check. `solr/core/src/test/org/apache/solr/MinimalSchemaTest.java`: one new test, `testDistributedQueryWithoutUniqueKeyAndQueryFalseIsBadRequest`. Anything else in the tree is out of scope.
- Why: the head commit is ungated; the receipt's GATE GREEN covers only the tree at 86ddbd433dba. Competition check: at pr-prepare tip da2dbfa8cb (2026-10-16), no file in claims/, assignments/ or gates/ names 52e32f892c; the only 10305 claim (claims/pool-vm2-gate-backlog-10305.md, the text gate at c43de86d4c13) is DONE, and gates/SOLR-10305-text.md and gates/SOLR-10305-review-history.md are DONE. No claim or named-host assignment covers gating this head. This claim is the first.
- Runner and logs on vm1: ~/workspace/tools/g10305-live-gate.sh, ~/workspace/tools/g10305-live-gate.log (head worktree ~/workspace/solr-worktrees/solr-10305-gate); proof leg ~/workspace/tools/g10305-live-proof.sh, ~/workspace/tools/g10305-live-proof.log (proof worktree ~/workspace/solr-worktrees/solr-10305-proof).

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the worktree out at the head first.

- [ ] Step 0: changelog YAML parse of `changelog/unreleased/SOLR-10305-distributed-query-requires-unique-key.yml`.
- [ ] Step 1: tidy (`:solr:core:tidy`; every changed Java file on the branch is in :solr:core). If tidy changes any file (post-tidy status, with gradle/libs.versions.toml restored), the run is GATE INVALID and stops.
- [ ] Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- [ ] Step 3, proof leg: scratch worktree at the merge-base `cabedd1d9680` (base production) with the branch's test file `MinimalSchemaTest.java` at 52e32f892c copied in; run the focused class at the seed; count and read failures from the fresh JUnit XML. Expected, by content, stated from the code before the run: base production has no uniqueKey check in `QueryComponent.prepare` at all, so both uniqueKey tests fail there: `testDistributedQueryWithoutUniqueKeyIsBadRequest` (the established premise; the request proceeds into the distributed path instead of answering the branch's 400) and the new `testDistributedQueryWithoutUniqueKeyAndQueryFalseIsBadRequest` (no check exists to fire before or after the query=false early return). Expected shape: 5 tests run, exactly 2 failures, both uniqueKey tests; the other 3 tests pass. Any other failure shape stops the job and follows the failure route.
- [ ] Step 4, focused head run: cleanTest first, then the focused class at the seed in the head worktree. Count from the fresh JUnit XML. Expected: 5 of 5. Nothing else runs on the worktree between the test run and the XML read.
- [ ] Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure route.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof expectation | decided: both uniqueKey tests fail on base production by content (5 run, exactly 2 failures); the premise verdict rests on those two failures | none | this job, from the receipt and the head diff |
| Branch push | decided: none. The head is already the fork tip; do not edit the branch | none | main side brief, 2026-10-15 |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

## On green

- Refresh receipts/SOLR-10305.md with a new top section at 52e32f892c (earlier sections stay as history).
- Mark this job DONE and the claim DONE in the same push sequence.
- Append one line to the takeover log on the main host.

## Result

(pending)
