# Receipt: SOLR-18613 standalone selection gate, round 2

Status: **GATE GREEN** at H `fe557633a169c34dcb835696470cc6e69ba00be4`.

- Job: `gates/SOLR-18613-selection-round-2.md`. Claim: `claims/SOLR-18613-selection-gate-phone-agent.md`. Round 1 FAILED at `e88dc392bc` (forbidden executor API in the new test; untidy Javadoc wrap); receipt `receipts/SOLR-18613-selection-round-1.md`.
- Executor: phone-agent (Linux, JDK 21, Gradle 9.7.0 via solr-gradle.sh), host test-queue flock, one build at a time.
- Run window (UTC): 2026-11-05T19:54:08Z to 20:10:47Z.
- H: `fe557633a169c34dcb835696470cc6e69ba00be4`, tree `6a0c8c7bfe43420ba03284f727c420af3996ebf4`, verified in a clean detached full clone (`~/workspace/solr-clones/18613-sel2-h`). P: `2399d0312fa320563773ea87884ee4f534594974`, second clean detached full clone. Seed `5B407A2D9C1310E7` throughout.
- Logs and XML: `~/workspace/tools/gate-18613-sel2/` (leg logs, `xml-h/`, `xml-p/`, `results.txt`).

## Positive legs at H

1. Tidy: rc 0, clean tracked tree afterwards.
2. Error Prone compile (`:solr:solrj:compileJava -Pvalidation.errorprone=true`): rc 0.
3. Focused suites: rc 0. Fresh JUnit XML totals 49 tests, 0 failures, 0 errors, 0 skipped: LBSolrClientAttemptLimitTest 14 (both new selection methods executed), LBAsyncSolrClientAttemptLimitTest 12, LBSolrClientTest 8, LBAsyncSolrClientTest 4, LBSolrClientRetryUnsentTest 3, LB2SolrClientTest 7, LBHttpSolrClientBadInputTest 1.
4. Root `check -x test` in the full clone: rc 0 (BUILD SUCCESSFUL; this includes the forbidden-apis checks that failed round 1). `git diff --check` rc 0; final tree clean, head and tree unchanged.

## Negative leg at P

Second clone at P with only H's `LBSolrClientAttemptLimitTest.java` copied in (synthetic diff: exactly that one file, +93/-1 against P's version; sha256 `43b2209e1ba087673fab8164e033b8e3a07fa62e0ad8f7d71707aba6c7645c37`, identical before and after the run). Both new methods executed and failed for the intended reason: `testStandaloneInterleavedRequestDoesNotHideHealthyEndpoint` with "Primary request failed after [http://one.invalid/solr, http://one.invalid/solr]" and `testStandaloneRetryUsesCustomPickerWithUntriedEndpoints` with "Request failed after [http://one.invalid/solr, http://one.invalid/solr]", the [A, A] dispatch trace the job specifies.

## Carried-forward historical proof

The four original-production failures recorded in `receipts/SOLR-18613.md` were not rerun, as the job allows; the four methods are present in H's test files with their assertions (name-verified in round 1). This remains historical evidence, not exact-head proof.

## Read-backs

Fork branch ref and live PR #5297 head both equal H after the run (fork `fe557633a169`, PR open, draft).

## Description draft for owner review (not posted)

Add to the PR body, after the existing change description:

"A follow-up commit (e88dc392bc) fixes a standalone-selection regression the first version introduced: the standalone path caps live selections at the captured live-array size, but the round-robin counter is shared across requests, so an interleaved request could advance the counter past a healthy endpoint the primary request had not tried, and the primary then failed with no live servers even though an attempt remained. The standalone selection now filters endpoints already failed by this request out of the captured array and picks again from the untried candidates; filtered selections are not dispatches and do not spend the attempt allowance. Two new tests cover the interleaving (with limits 3, 2, and unset) and a custom first-candidate picker. Two further commits are repairs only: a Javadoc rewrap from the formatter and an MDC-aware executor in the new test, which the project's forbidden-apis check requires."

Replace the verification line with: "Verified 2026-11-05 at `fe557633a1`: tidy clean, Error Prone compile passes, the attempt-limit suites pass 14 of 14 and 12 of 12, the pre-existing LB suites pass unchanged (8, 4, 3, 7, and 1), and root `check -x test` passes. The two new selection tests fail against the pre-fix head with the primary request dispatching to the same failed endpoint twice."
