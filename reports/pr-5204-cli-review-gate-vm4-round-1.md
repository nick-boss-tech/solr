# PR 5204 (SOLR-18566) final-head gate, vm4, round 1: incomplete

- Ticket and PR: SOLR-18566, https://github.com/apache/solr/pull/5204, reviewed head `7eb288a6f5777770d4472fc2c550b66e0d1cec12`, merge base `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Final head under test: `daa29b06e035b57fc5098180113f764dff9cf57d` on `nick-boss-tech/solr:solr-18566-pr5204-cli-review` (read back from the remote when pushed). Compare: https://github.com/apache/solr/compare/7eb288a6f5777770d4472fc2c550b66e0d1cec12...nick-boss-tech:solr-18566-pr5204-cli-review (1 commit ahead, 0 behind, 2 files).
- Job: `gates/SOLR-18566-pr5204-cli-review-daa29b06e035.md`. Claims: `claims/pr-5204-phase-g-final-gate-vm4-round-1.md` (legs 1 to 4), `claims/pr-5204-phase-g-final-gate-attempt2-vm4-round-1.md` (legs 2 to 4).
- Host: vm4. JDK 21.0.12.1, Gradle 9.7.0 wrapper, Bats 1.8.2, lsof 4.99.7, Mesa EGL. Child processes ran under `/workspace/tools/reap-run.py`.

## Verdict

Not complete. No receipt is written, and this is not a green gate.

| Leg | Command | Status | Evidence |
| --- | --- | --- | --- |
| 1. Focused false-mode | `env SOLR_PICOCLI=false ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --tests test_help.bats --tests test_post.bats --rerun` | PASSED, 27 of 27, 0 skipped | `/workspace/gates/logs/vm4/pr-5204/gate-leg1-focused/` (run.log, junit/report.xml) |
| 2. Legacy full suite, attempt 1 | `env -u SOLR_PICOCLI ./gradlew :solr:packaging:integrationTests -PdisableJsClient=true --rerun` | INCOMPLETE, process lost at about 00:52:36Z | 100 ok, 0 not ok, 5 skipped before the stop. `/workspace/gates/logs/vm4/pr-5204/gate-leg2-legacy-full-attempt1-incomplete/` (see INCOMPLETE.txt) |
| 2. Legacy full suite, attempt 2 | same | INCOMPLETE, stopped by vm4 at about 01:35Z after a launch overlap | 3 ok, 0 not ok. `/workspace/gates/logs/vm4/pr-5204/gate-leg2-legacy-full-attempt2-incomplete/` |
| 3. Picocli full suite | `./gradlew :solr:packaging:integrationTestsPicocli -PdisableJsClient=true --rerun` | NOT RUN | none |
| 4. Tidy and check | `./gradlew tidy :solr:packaging:check -x test -PdisableJsClient=true` | NOT RUN | none |

## Why it is incomplete

1. Attempt 1 of leg 2: the gate process ended at about 00:52:36Z with no `gradle_exit` line and no failed test. Cause not confirmed. The likely cause is that the detached process was lost while the session was asleep between wake-ups.
2. Attempt 2 of leg 2: a launch error on vm4. The detached launch of `run-gate2.sh` did not take `/workspace/tools/.gradle-vm4.lock`, and a second copy of the same script had been started by an earlier chained command. The two copies ran the same Gradle build in the same worktree at once. The second copy failed at about 01:21:29Z. vm4 stopped the remaining run at about 01:35Z, because the overlap can corrupt shared build and test output. The partial counts are not cited as results.

Nothing was rerun, as the wake-up instructions require. A failed or incomplete leg does not produce a green receipt.

## Results that stand

- Leg 1 at the final head passed: 27 of 27 BATS tests in `test_help.bats` and `test_post.bats` under `SOLR_PICOCLI=false`, with no skip. The restart help test ran rather than skipping. The new regression test "explicit SOLR_PICOCLI=false selects the legacy help wording" passed.
- The original-head negative control reproduces the review finding (`reports/pr-5204-cli-review-implementation-vm4-round-1.md`).

## Skips recorded so far in leg 2 attempt 1

- 44, 45, 46: "using curl to extract a single pdf file", "using the bin/solr post tool to extract content from pdf", "using the bin/solr post tool to crawl web site". Skipped with "Docker is not available".
- 81: "Docker SolrCloud rolling upgrade". Skipped with "Docker is not available".
- 97: "check stop command doesn't hang". Skipped with "timeout utility is not available".

## Environment notes

- The js-client npm build fails at this head (`Cannot find package '@babel/plugin-syntax-dynamic-import'`). The gate passes `-PdisableJsClient=true`. The BATS CLI suites do not use that bundle. This is a pre-existing build defect, not part of this PR.
- PID 1 on this VM does not reap orphaned children, so Solr JVMs left zombies and `solr stop` waited 180 seconds. `/workspace/tools/reap-run.py` works around this.
- The `timeout` utility is missing, so test 97 skips.

## Next step, for the owner

Run legs 2 to 4 once, serially, from one launcher under the Gradle lock. Use a single unambiguous launch, and do not start a second copy. Expected duration is about 3 hours for leg 2. Nothing is rerun until the owner says so.

## Receipt and comment

No `receipts/SOLR-18566.md` is written. The review-comment draft `reports/pr-5204-review-comment-draft-vm4-round-1.md` is not written, because the gate has not passed. Nothing is posted to GitHub or Jira.
