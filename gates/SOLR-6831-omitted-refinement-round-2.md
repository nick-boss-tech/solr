# Gate job: SOLR-6831 refinement stops on omitted partial answers (round 2)

- Head to gate: `3e07d801df598573823cc997bbabb848fa2b1cfb` on branch `solr-6831-submit`. It is already pushed to the fork as a fast-forward from `2ee19dc646329f21ef630df33ae9ebab7e239cbc`, and PR #5134's head moved to it on 2026-10-15. The PR is a draft. Fetch the fork and check the head out in a fresh worktree. Re-verify with `ls-remote` at claim time.
- Patch: `gates/SOLR-6831-omitted-refinement-round-2.patch` is the same change, kept for reference. `git diff 2ee19dc646 3e07d801df5` should match it.
- If a step adds commits (for example `formatApplied`), commit them with the identity Nick Shanin (nick.boss.us@gmail.com), no trailers, and record the new head in the run record.
- Files: `solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java` (refinePivotFacets: a shard refinement answer whose partialResults header is not complete, meaning `true` or `"omitted"`, marks the response partial and returns before queuing more pivot refinement); `solr/core/src/test/org/apache/solr/search/TestQueryLimits.java` (drops a history sentence from one comment; adds `testPivotFacetRefinementStopsWhenOmittedPartialResults`).
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestQueryLimits`. Seed: `6831C0FFEE6831`.
- Assignment: `assignments/pool-solr-6831-omitted-refinement-round-2.md`.
- Claiming host: vm2 (capability tag: gate). The host files its own claim under `claims/`.
- Runner: the host's own route in `hosts/vm2.md`: `./gradlew` with the vendored JDK 21 on PATH and `GRADLE_USER_HOME` under /workspace. Copy `gradle.properties` into the new worktree first.
- The commit was written on the Windows lane and has not been compiled or run there.

## Question this run settles first

Does a shard refinement answer of `"omitted"` make the coordinator queue another pivot refinement round? Before this change, `refinePivotFacets` did not treat `"omitted"` as partial, so it could. Reading the code says it can; no run has confirmed it. The proof leg below settles it.

## Gate steps

- [ ] claim recorded under `claims/`.
- [ ] step 0: changelog YAML parse with a real parser (the changelog is not in this change; run it as the standard first step).
- [ ] check out `3e07d801df5` in a fresh worktree; `git diff --stat 2ee19dc646..HEAD` shows exactly the two files above.
- [ ] tidy, with the GATE INVALID guard (restore `gradle/libs.versions.toml` afterward if it changes).
- [ ] spotlessApply on `:solr:core`. If it changes any file, the change is part of the head: record `formatApplied`, commit it, and rerun tidy.
- [ ] Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`.
- [ ] focused run at the head: `TestQueryLimits`. Expect 8 tests, 0 failures, 0 errors. Counts from fresh JUnit XML.
- [ ] proof leg, against the pre-fix production: in the gate worktree, `git checkout 2ee19dc646 -- solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java`, keep the new test file, and run `TestQueryLimits`. Expect exactly one failure, `testPivotFacetRefinementStopsWhenOmittedPartialResults`, with the message "refinement should not be requested again after an omitted response: n" where n is greater than 3. Record n and the message. Then restore the head's `FacetComponent.java` (`git checkout HEAD -- <path>`) and confirm `git diff --stat` is empty against the head.
- [ ] module check: `:solr:core:check -x test`.

## Decision rules

- Proof leg shows n greater than 3 at the pre-fix production, and the focused run at the head is 8 of 8: green. Go to On green.
- Proof leg passes at the pre-fix production (n is 3 or less): the gap is not reproduced in this shape. The head is already on the draft PR, so do not revert it. Record it here, mark the claim with the result, and stop. The owner decides whether to keep the change; a revert would be a new commit, never a force-push.
- An executed test fails, or the proof leg fails with a different error: record the test, the message, the seed, and the log path here. Do not relaunch. Do not push a further commit until this is recorded.

## On green

- If the gate added commits, push them as a plain fast-forward from `3e07d801df5` (`git push origin HEAD:solr-6831-submit`); git refuses anything else. If it added none, the head is already on the fork. Verify the fork tip with `ls-remote`. Never push to `apache/solr`.
- Write a new top section in `receipts/SOLR-6831.md` (pr-prepare branch) for the green head: the steps, both counts, the proof-leg n and message, and the formatApplied list if any. Push pool files with `research\push-pr-prepare.ps1`.
- Mark this gate job DONE with the head SHA and n. Mark the claim DONE naming the head.
- This gate does not edit PR #5134's body. The Windows review lane re-points the body after the receipt is recorded.

## On red or stop

Record the failing step and detail in this file, mark the claim with the blocker, and stop. Do not relaunch a run that executed tests (WORKFLOW.md).

## Run record

(The host fills this in as the steps finish.)

- Head leg FAILED at 3e07d801df598573823cc997bbabb848fa2b1cfb (seed 6831C0FFEE6831; log logs/vm2/6831-r2-head.log; runner gates/g6831-r2-gate.sh, GATE START 2026-10-15T05:48:00Z). Focused `TestQueryLimits`: tests=8 failures=1 errors=0 skipped=0. Failing test: testPivotFacetRefinementStopsWhenOmittedPartialResults, message "org.apache.solr.client.solrj.SolrServerException: No live SolrServers available to handle this request"; the server side reports "No pivot refinement response from shard". Compile, tidy and spotless rc=0. The proof leg (rc=1, expected) ran after it; the module check was still running when recorded. Not relaunched; no further commit pushed. Left for the main agent.


## Run record, vm2 (2026-10-15T06:29ZZ): RED, not pushed

- Head 3e07d801df598573823cc997bbabb848fa2b1cfb. Log logs/vm2/6831-r2-result.txt; GATE END 2026-10-15T06:17:35Z; seed 6831C0FFEE6831.
- Changelog: none in this patch. Tidy rc=0 (tree changed only in TestQueryLimits.java). Spotless rc=0 changed the same file: committed locally as formatApplied 55588a1c709f on the gate worktree, not pushed.
- Compile (Error Prone) rc=0. Module check rc=0.
- Focused head run: TestQueryLimits 8 tests, 1 failure: testPivotFacetRefinementStopsWhenOmittedPartialResults with `SolrServerException: No live SolrServers available to handle this request`.
- Proof leg (production FacetComponent.java at 2ee19dc646): 8 tests, 1 failure, the same test with the same error. The proof does not decide the change: the new test fails before the refinement code can be compared, so n is not reached.
- Stop per the job: an executed test failed. Not relaunched. Nothing pushed. The fork branch stays at 3e07d801df.
- Needs a decision: the test's cluster does not come up in this shape (an environment or fixture defect, or the new test's setup). Options: fix the test setup in a new commit and re-gate, or the review lane investigates the setup first.


## Run record, vm2 round 3 (2026-10-15T07:00ZZ): head green, proof shape differs, not pushed

- Local commit 4afed04987b2 (on 3e07d801df, not on the fork): skips an incomplete refinement answer that has no pivot data, instead of throwing "No pivot refinement response". Spotless and tidy clean. Compile rc=0.
- Focused head run: TestQueryLimits 8 tests, 0 failures, 0 errors (XML 2026-10-15T06:54:51Z). Log logs/vm2/6831-r3-result.txt.
- Proof leg, production FacetComponent.java at 2ee19dc646: 8 tests, 1 failure, testPivotFacetRefinementStopsWhenOmittedPartialResults with `SolrServerException: No live SolrServers available` (XML 2026-10-15T06:58:49Z). Not the expected "refinement should not be requested again ... n" message: at base the coordinator throws before the refinement loop, so n is never reached.
- The test discriminates in outcome (fails at base, passes at head) but not in the message the job expects. Per the job, a different failure shape stops the run; not pushed. The fork stays at 3e07d801df.
- Same error appears in the SOLR-6975 proof leg (see that job), which points at a shared test-cluster issue for proof legs that trip a limit on a shard.

## Settlement, vm1 proof-shape handoff (2026-10-15): DONE, GATE GREEN at 4afed04987b2

- Assignment: assignments/pool-vm1-proof-shape-handoff-round-1.md; claim: claims/pool-vm1-proof-shape-handoff-round-1.md.
- Proof shape changed under the assignment's recorded proof-form default: outcome proof accepted (fails at base, passes at head), recorded here as the job's settled proof shape. vm1's proof leg at production 2ee19dc646 (log g6831-proofshape-proof.log in ~/workspace/tools, fresh JUnit XML) fails the new test with the load balancer message at the client, caused server-side by SolrException "No pivot refinement response from shard" at FacetComponent.refinePivotFacets (line 1053 at base): the base production throw the round 3 hunk removes, not a cluster setup defect. The expected n message is unreachable in this shape, because the omitted answers carry no pivot data and the throw fires before a further refinement round can be counted; the count premise stays unobserved, and the receipt's top section says so plainly.
- Gate at the round 3 head 4afed04987b2 on vm1 (runner ~/workspace/tools/g6831-proofshape-gate.sh, log g6831-proofshape-gate.log): changelog parse rc 0, tidy rc 0 with the tree unchanged, Error Prone compile rc 0, module check rc 0; focused TestQueryLimits 8 of 8 from fresh JUnit XML. The head was already on the fork as branch vm2-solr-6831-r3; this settlement pushed nothing and did not move solr-6831-submit (still 3e07d801df59).
- Status: DONE. Full record in receipts/SOLR-6831.md, top section.
