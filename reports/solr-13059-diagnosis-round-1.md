# SOLR-13059 diagnosis, round 1

Status: DONE (assignments/pool-solr-13059-diagnosis-round-1.md)

Diagnosis only: no code changes, no fix branch, no Jira activity. Executed by vm1 under claims/pool-solr-13059-diagnosis-round-1.md. Runs finished 2026-10-16T02:40Z.

## Base

- Branch: solr-13696-submit on the fork.
- Head used as base: da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9.
- Verified at claim time two ways: `git ls-remote fork` returned da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9 for refs/heads/solr-13696-submit, and the GitHub API reported the same SHA as the head of the SOLR-13696 submit branch's live PR (open, not a draft). The runner re-checked the worktree HEAD against this SHA before the first run and would have aborted on a mismatch.
- No merge to main was needed or waited on.

## Grounding facts, re-verified at claim time

From the shaped brief (solr-13059-diagnosis-brief-2026-10-09.md in the goal records), checked against the tree at the base head:

- The Time subclass carries its own annotation: TimeRoutedAliasUpdateProcessorTest.java line 75 reads `@LuceneTestCase.AwaitsFix(bugUrl = "https://issues.apache.org/jira/browse/SOLR-13059")`. Confirmed.
- The base class RoutedAliasUpdateProcessorTest carries no AwaitsFix annotation at this head; the SOLR-13696 repair removed the base class's `@AwaitsFix(SOLR-13696)`. Confirmed by grep.
- The test file lives at solr/core/src/test/org/apache/solr/update/processor/TimeRoutedAliasUpdateProcessorTest.java, package org.apache.solr.update.processor, module :solr:core. The run commands were composed from this grep result, per the assignment.
- The 2026-10-09 historical items (SOLR-13943 stacked gate GREEN at cc155cf68e1; SOLR-13696 gate r7 GREEN at 1d0b8a0a73cd) are record citations from the brief, not re-run here; the 13696 head has since moved to the head verified above.

## Run matrix

All runs: `-Dtests.awaitsfix=true`, via ~/workspace/tools/solr-gradle.sh, serially behind the blocking test-queue flock, in a scratch worktree at the base head. Counts are from the fresh JUnit XML of each run, archived with the run log on vm1 at ~/workspace/tools/diag-13059-round1/ (log diag-13059-round1.log; one TEST XML copy per run). The same six seeds per method as the 2026-10-09 diagnosis run, so the two matrices compare directly.

testPreemptiveCreation, one run per seed:

| Seed | Result |
| --- | --- |
| 8879E35521A4B9EA | FAIL: AssertionError, expected:<8> but was:<9>, at TimeRoutedAliasUpdateProcessorTest.java:599 |
| 13059C0FFEE00001 | pass |
| 13059C0FFEE00002 | pass |
| 13059DEADBEEF0001 | pass |
| 13059F00D0000001 | pass |
| 13059ABCDEF00001 | pass |

testSliceRouting, one run per seed: all six passed (seeds 8879E35521A4B9EA, 13059C0FFEE00011, 13059C0FFEE00012, 13059DEADBEEF0011, 13059F00D0000011, 13059ABCDEF00011).

Full class, one run (seed 13059C0FFEE00021): 7 tests, 0 failures, 0 errors; both target methods passed inside it.

## Failure rate for testPreemptiveCreation

Round 1: 1 failure in 6 runs (about 17 percent).

The 2026-10-09 diagnosis run at this same head da4fa6df117, same seeds, failed 2 of 6: seed 8879E35521A4B9EA with a bare AssertionError at line 390 (the threadFinished confidence assert), and seed 13059F00D0000001 with expected:<8> but was:<9> at line 599. Combined across both matrices: 3 failures in 12 runs (25 percent).

The failures do not follow the seed deterministically. Seed 8879E35521A4B9EA failed in both matrices but at different assertions (line 390 in 2026-10-09, line 599 in round 1); seed 13059F00D0000001 failed in 2026-10-09 and passed in round 1. The failure tracks wall-clock timing, not the seed's random choices alone.

## testSliceRouting verdict

Does not reproduce in these runs. 0 failures in 6 dedicated round 1 runs, plus a pass inside the full-class run. Combined with 2026-10-09: 0 failures in 12 dedicated runs and 2 full-class passes. The string "no core retrieved" appears nowhere in the round 1 run log or evidence files. The original SOLR-13059 symptom (testSliceRouting getting "no core retrieved" for a collection the test just created) does not reproduce at the current head in this matrix.

## Classification of the failure seen

The one round 1 failure (testPreemptiveCreation, seed 8879E35521A4B9EA) is classified as: **test polling or timing race in the test itself.**

Evidence:

- The failing assertion is line 599, `assertEquals(8, cols.size())`, over a ListAliases snapshot. The method's design races asynchronous preemptive collection creation against exactly these snapshots; the comment block directly above the assertion (lines 588 to 596 of the test) names the hazard itself: the async case arising in the middle of synchronous creation.
- The failing run's own test output shows the ninth collection is the next time slice, myalias__TRA__2017-10-31, created asynchronously by preemptive creation. The collection existed and was visible in the alias listing; the snapshot simply caught its arrival. Nothing in the failure shows a created collection being invisible, so this is not a production state-propagation (collection creation visibility) failure.
- Nothing in the failure is environmental: the other 12 round 1 runs on the same host, head and toolchain passed, and the failure is an assertion mismatch inside the test, not an infrastructure error.
- The second half of the 2026-10-09 diagnosis also still holds in the source: the two worker lambdas in testPreemptiveCreation catch only Exception, so an AssertionError raised inside concurrentUpdates escapes the threadExceptions capture and surfaces elsewhere; in the 2026-10-09 run that is exactly how the failure presented (the threadFinished assertTrue at line 390, after the worker died on an in-worker assertion).

Net: the 2026-10-09 picture still holds at the current head. The reproducible defect is in testPreemptiveCreation's exact-count snapshots taken while preemptive creation is still landing, with the escaped-AssertionError reporting flaw on top. No production defect was observed in any run.

## Fix-shape estimate

Small test fix; no production work indicated. Recorded as an estimate only, per the assignment; nothing here is implemented.

- Make the exact-count assertions in testPreemptiveCreation (the collection counts in concurrentUpdates and checkPreemptiveCase1, and the late count at line 599) settled-state assertions: wait for the alias contents to settle, or assert after the preemptive creation window has closed, instead of snapshotting while creation is in flight.
- Record Throwable, not only Exception, from the worker lambdas, so an in-worker assertion failure is reported as itself instead of as a downstream confidence-assert failure.
- With those in place, re-evaluate removing the class-level `@AwaitsFix(SOLR-13059)` from TimeRoutedAliasUpdateProcessorTest; testSliceRouting needs no change on this evidence.

## Evidence on vm1

- Runner: ~/workspace/tools/diag-13059-round1.sh (blocking flock, HEAD guard against da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9).
- Log and per-run JUnit XML: ~/workspace/tools/diag-13059-round1/.
- 2026-10-09 baseline for comparison: ~/workspace/tools/diag-13059/.
