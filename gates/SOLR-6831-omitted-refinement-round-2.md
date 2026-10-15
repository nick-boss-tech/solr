# Gate job: SOLR-6831 refinement stops on omitted partial answers (round 2)

- Branch: solr-6831-submit. Base head: 2ee19dc646329f21ef630df33ae9ebab7e239cbc (the live fork tip when posted; re-verify with ls-remote at claim time).
- Patch: `gates/SOLR-6831-omitted-refinement-round-2.patch`, one commit on the base. Apply it in a fresh worktree at the base head with `git apply`, then commit. Commit identity is Nick Shanin (nick.boss.us@gmail.com), no trailers. Record the resulting head in the run record below.
- Files: `solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java` (refinePivotFacets: a shard refinement answer whose partialResults header is not complete, meaning `true` or `"omitted"`, marks the response partial and returns before queuing more pivot refinement); `solr/core/src/test/org/apache/solr/search/TestQueryLimits.java` (drops a history sentence from one comment; adds `testPivotFacetRefinementStopsWhenOmittedPartialResults`).
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestQueryLimits`. Seed: `6831C0FFEE6831`.
- Assignment: `assignments/pool-solr-6831-omitted-refinement-round-2.md`.
- Claiming host: vm2 (capability tag: gate). The host files its own claim under `claims/`.
- Runner: the host's own route in `hosts/vm2.md`: `./gradlew` with the vendored JDK 21 on PATH and `GRADLE_USER_HOME` under /workspace. Copy `gradle.properties` into the new worktree first.
- Windows does not build. The patch was written on the Windows lane and has not been compiled or run.

## Question this run settles first

Does a shard refinement answer of `"omitted"` make the coordinator queue another pivot refinement round? Before this patch, `refinePivotFacets` did not treat `"omitted"` as partial, so it could. Reading the code says it can; no run has confirmed it. The proof leg below settles it.

## Gate steps

- [ ] claim recorded under `claims/`.
- [ ] step 0: changelog YAML parse with a real parser (the changelog is not in this patch; run it as the standard first step).
- [ ] apply the patch in a fresh worktree at 2ee19dc646; `git diff --stat 2ee19dc646..HEAD` shows exactly the two files above.
- [ ] tidy, with the GATE INVALID guard (restore `gradle/libs.versions.toml` afterward if it changes).
- [ ] spotlessApply on `:solr:core`. If it changes any file, the change is part of the head: record `formatApplied`, commit it, and rerun tidy.
- [ ] Error Prone compile: `:solr:core:compileJava` and `:solr:core:compileTestJava` with `-Pvalidation.errorprone=true`.
- [ ] focused run at the new head: `TestQueryLimits`. Expect 8 tests, 0 failures, 0 errors. Counts from fresh JUnit XML.
- [ ] proof leg, against the pre-fix production: at the new head, `git checkout 2ee19dc646 -- solr/core/src/java/org/apache/solr/handler/component/FacetComponent.java`, keep the new test file, and run `TestQueryLimits`. Expect exactly one failure, `testPivotFacetRefinementStopsWhenOmittedPartialResults`, with the message "refinement should not be requested again after an omitted response: n" where n is greater than 3. Record n and the message. Then restore the patched `FacetComponent.java` (`git checkout HEAD -- <path>` in the gate worktree) and confirm `git diff --stat` is empty against the head.
- [ ] module check: `:solr:core:check -x test`.

## Decision rules

- Proof leg shows n greater than 3 at the pre-fix production, and the focused run at the head is 8 of 8: green. Go to On green.
- Proof leg passes at the pre-fix production (n is 3 or less): the gap is not reproduced in this shape. Do not push. Record it here, mark the claim with the result, and stop. The owner decides whether the contract change is still wanted without a reproduced loop.
- An executed test fails at the head, or the proof leg fails with a different error: record the test, the message, the seed, and the log path here. Do not relaunch. Do not push.

## On green

- Push the new head to the fork as a plain fast-forward from 2ee19dc646 (`git push origin HEAD:solr-6831-submit`). Git refuses anything else. Verify the fork tip with `ls-remote`. Never push to `apache/solr`.
- Write a new top section in `receipts/SOLR-6831.md` (pr-prepare branch) for the new head: the steps, both counts, the proof-leg n and message, and the formatApplied list if any. Push pool files with `research\push-pr-prepare.ps1`.
- Mark this gate job DONE with the head SHA and n. Mark the claim DONE naming the head.
- This gate does not edit PR #5134's body. The Windows review lane re-points the body after the receipt is recorded.

## On red or stop

Record the failing step and detail in this file, mark the claim with the blocker, and stop. Do not relaunch a run that executed tests (WORKFLOW.md).

## Run record

(The host fills this in as the steps finish.)
