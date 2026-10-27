# Receipt: LB attempt limits, PR 1 (vm2, round 2)

Status: GREEN at local head 28dbd2edfce1520c354dbc71b555db4ced3fbebe. Not yet on the fork: the live fork tip is still 7a226ef031f8b82527a1209ce760fcf37e611601, and the two repair commits have not been published.

- Gating host: vm2. Claim: claims/lb-attempt-limits-pr1-vm2-round-1.md (board 7857a0a060f). Job: gates/lb-attempt-limits-pr1-round-1.md.
- Fork branch: nick-boss-tech/solr:solr-lb-attempt-limits-pr1-submit.
- Parent and proof base: apache/solr main 0d6b2e32f598d772aed094038022a6a588fdfc5a.
- Toolchain: JAVA_HOME /workspace/jdk/jdk-21.0.12.1+1, GRADLE_USER_HOME /workspace/gradle-home, JDK 21.0.12.1, `--no-daemon`. Build lock: /workspace/tools/.gradle-vm2.lock via flock on every gradle invocation.
- Seed: 5B407A2D9C1310E7 (both proof legs).

## Heads and repairs

| Head | Role | Result |
| --- | --- | --- |
| 7a226ef031f8b82527a1209ce760fcf37e611601 | Fork head, Nick's implementation | Preflight tidy rc 0, but tidy rewrote three files. Tidy changes are formatting only. |
| bb3876aa83ec26370c261293a8a88bd8ca6ea575 | Tidy repair, Nick Shanin, no trailers | Tidy clean. Focused 47/0/0/0. Root check failed at `:solr:solrj:ecjLintMain`. |
| 28dbd2edfce1520c354dbc71b555db4ced3fbebe | Javadoc repair, Nick Shanin, no trailers | Tidy clean. Focused 47/0/0/0. Root check rc 0 in a full clone. |

- Tidy repair (bb3876a): one javadoc rewrap in `LBSolrClient.java`, three line joins in the two test suites. Formatting only.
- Javadoc repair (28dbd2e): in `LBAsyncSolrClient.java` lines 60-62, `{@link Req#getNumServersToTry()}` and `{@link AttemptLimitExceededException}` became `LBSolrClient.Req` and `LBSolrClient.AttemptLimitExceededException`. ECJ reported "Invalid member type qualification" for both. These are javadoc-only, and the failing task was lint, not tests.
- Replayed patch series: `drafts/lb-attempt-limits-pr1-repair-series.mbox` (board) applies cleanly with `git am --3way` to 7a226ef. The replayed tree equals the 28dbd2edfce tree (4d09e238ca0a7890e125b5f36d2dd78ff90f7110).

## Commands and evidence

1. Tidy preflight at 7a226ef: rc 0; tree changed (see repair above). Log: `preflight-tidy.log`.
2. Fixed head bb3876a, under lock: tidy rc 0 and clean tree. Focused run: `:solr:solrj:cleanTest :solr:solrj:test` with the seven suites from the job, seed 5B407A2D9C1310E7. Result: 47 tests, 0 failures, 0 errors, 0 skipped. Logs: `head-focused.log`, XML `head-focused-xml/`. Root check in a worktree failed at `:solr:solrj:ecjLintMain` with 2 invalid-qualification errors. Log: `root-check.log`.
3. Fixed head 28dbd2edfce (round 2), under lock: tidy rc 0 and clean tree. Focused run: 47 tests, 0 failures, 0 errors, 0 skipped. Logs: `r2-focused.log`, XML `r2-focused-xml/`.
4. Root check at 28dbd2edfce in a worktree: failed in `:solr:solr-ref-guide:buildLocalAntoraSite` because Antora requires a `.git` directory (`Local content source must be a git repository`). ECJ lint and solrj `check` passed. Log: `r2-root-check.log`.
5. Root check at 28dbd2edfce in a full clone, `check -x test`: rc 0, tree clean, BUILD SUCCESSFUL in 13m 5s. Antora site build passed. Log: `root-check-full.log`. Status: `full-root-status.txt`.
6. Synthetic-base discrimination on parent 0d6b2e3 with the two new test files from 7a226ef and the inert `AttemptLimitExceededException` declaration copied from 7a226ef `LBSolrClient.java`. No counter, loop, or iterator change. Four named methods executed, all failed: `base-discrim.log`, XML `base-discrim-xml/`.
   - `LBSolrClientAttemptLimitTest.testStandaloneLimitStopsBeforeZombieFallback`: no exception thrown; zombie fallback went past the limit of two.
   - `LBSolrClientAttemptLimitTest.testStandaloneLimitWithAllZombies`: `ArithmeticException: / by zero`, from an empty live array reaching server selection.
   - `LBSolrClientAttemptLimitTest.testStandaloneDoesNotRetryExhaustedLiveSnapshot`: five endpoint dispatches instead of three.
   - `LBAsyncSolrClientAttemptLimitTest.testAsyncPreservesAttemptLimitFailure`: the cause was not `AttemptLimitExceededException` (assertion at line 245).

## Counts

- Focused 47/0/0/0 at both fixed heads (seven suites: 12 + 12 new methods, plus LBSolrClientTest 8, LBAsyncSolrClientTest 4, LBSolrClientRetryUnsentTest 3, LB2SolrClientTest 7, LBHttpSolrClientBadInputTest 1).
- Discrimination 4 of 4 failed for the stated behavioral reasons. No compile errors.
- Skips: 0.

## Proof limits

- The discrimination uses the 7a226ef test files. The 28dbd2e test files differ only by tidy formatting from bb3876a, so the behavior under test is the same.
- Fail-before proof covers only the four named methods. The other 20 new methods are compatibility pins, not discriminated.
- The root check at 28dbd2e ran in a clone whose tree matches the replayed patch series, not in the fork.
- The receipt does not cover the fork tip. The fork still points at 7a226ef, so no proof exists for the live fork head.

## Remote head confirmation

- The fork push of bb3876a was rejected twice by GitHub: `Password authentication is not supported for Git operations`. The same token from /workspace/.env reads the fork and writes the board. No other credential was tried. The 28dbd2e commit was not pushed.
- Live fork head at receipt time: 7a226ef031f8b82527a1209ce760fcf37e611601.

## Failures and repairs

- Round 1 preflight tidy rewrote three files (formatting). Repaired as bb3876a.
- Round 1 root check failed at ECJ lint, two invalid javadoc qualifications. Repaired as 28dbd2e.
- Worktree root checks failed at Antora (`.git` file, not directory). The full-clone root check passed. Round 2 focused was rerun at 28dbd2e.
- Earlier full clones were corrupted or stalled; the gate used an existing clean full clone of the same repository (`/workspace/worktrees/pr5235-r3-Fclone`, detached at 28dbd2edfce) after its own finished gate. Its evidence lives under gates/logs.

## Proposed PR description (observed results only)

Title: SolrJ: consistently enforce LB request attempt limits

The standalone LB request path could continue into zombie fallback after spending numServersToTry, and could revisit failed live endpoints when the configured limit exceeded the live snapshot. Async requests also hid attempt-limit exhaustion behind the preceding transport failure.

This change counts live and zombie attempts within one execution and exposes `LBSolrClient.AttemptLimitExceededException` with the attempt count, configured maximum, and last cause. Unconfigured behavior and retry eligibility are unchanged. The limit covers LB dispatches, including the initial attempt, not HTTP-client-internal retries.

Validation: at head 28dbd2edfce, tidy is clean; the seven focused LB suites run 47 tests with 0 failures and 0 skips; `check -x test` passes in a full clone (rc 0). On the parent with only the new tests and the exception declaration, the four named discriminating methods fail for the stated reasons.
