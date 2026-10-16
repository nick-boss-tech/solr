# Claim: pool-solr-11475-proof-fix-round-1

- Claimant host: vm1, matching `hosts/vm1.md`.
- Status: DONE.
- Job or assignment: `assignments/pool-solr-11475-proof-fix-round-1.md`.
- Slice: the whole assignment.
- Capability tags used: implementation, gate.
- Started: 2026-10-16T01:52:00Z (UTC).

Heartbeat: 2026-10-16T01:52:00Z

Scope note: diagnose the OutOfMemoryError fail-before proof on solr-11475-submit at 229947201fd7, make the smallest test-only change that gives an assertion-shaped fail-before, then the full gate at the resulting head. The sign-mismatch readiness blocker recorded in the audit is not decided by this job.

Heartbeat: 2026-10-16T02:09:00Z (vm1 lane started; branch tip verified at 229947201fd7 by ls-remote; diagnosis under way, harness confirmation run queued behind the test-queue flock)

Heartbeat: 2026-10-16T02:52:00Z

## Diagnosis (vm1, 2026-10-16)

Why the fail-before run exhausts memory, by candidate cause:

1. **Reverted production's behavior (root cause).** At the merge-base `14c7aac0d15`, `PeerSync.MissedUpdatesFinderBase.handleVersionsWithRanges` (PeerSync.java lines 805-837) reaches its else branch at the opposite-sign pair: the inner while only moves while `|other| < |our|`, and the absolute values are equal (42 vs -42), so neither index ever changes. The outer loop re-enters the same branch forever, appending the identical range string (`-42...10`) to `rangesToRequest` on every pass. Evidence: code reading; the recorded 2026-10-08 base run's OutOfMemoryError stack inside that method (PeerSync.java:835, `Long.toString` in the range construction); a standalone probe of the verbatim merge-base method on the staged input, which never returns.
2. **The staged test's shape (why the OOM, not the assertion, ends the run).** The staged test does detect the loop: its 30-second watchdog assertion fires (`AssertionError: handleVersionsWithRanges did not terminate`, PeerSyncTest.java:477). But the test never stops the worker thread, so the daemon worker keeps appending through suite teardown and the forked JVM dies with OutOfMemoryError in the same run. Evidence: the 2026-10-08 recorded run (gate log `g11475-fix-prefix.log`) shows the assertion followed by an `UncaughtExceptionError` wrapping `OutOfMemoryError: GC overhead limit exceeded` from the worker thread; vm1's confirmation run at seed 11475C0FFEE11475 on 2026-10-16 reproduced the compound exactly (JUnit XML: 1 test, 1 failure, 0 errors; the failure carries both the assertion and the worker's OutOfMemoryError; log `g11475-pf-diag.log`).
3. **Heap against append rate (aggravating, not causal).** The forked test JVM's heap is 512m (`tests.heapsize` default, gradle/testing/defaults-tests.gradle). The verbatim-method probe at -Xmx512m shows retained heap growing about 65-70 MB per second, reaching OutOfMemoryError about 11 seconds into the worker's spin. Any heap only moves where the loop dies; it cannot turn the outcome into an assertion. (`Thread.stop()` is not an option on this JDK: a probe on JDK 21.0.12.1 shows it throws UnsupportedOperationException.)

## Repair and repaired proof

Smallest test-only change, in `PeerSyncTest.testHandleVersionsWithRangesSameVersionDifferentSign` only: the two input lists are wrapped in a read-counting list; after 100,000 reads (the fixed walk needs about a dozen) a read throws a signal the worker catches, ending the thread, and the check then fails with the same designed assertion. Same input, same production code path, no production change.

Repaired proof run on vm1 (base production, repaired test, seed 11475C0FFEE11475, log `g11475-pf-proof.log`): **PASS as an assertion-shaped fail-before.** JUnit XML: 1 test, 1 failure, 0 errors; the only failure text is `java.lang.AssertionError: handleVersionsWithRanges did not terminate`; no OutOfMemoryError in the XML or the log; the run took 112s against 251s for the contaminated run. Next: the full hardening gate at the resulting head.

## Outcome: DONE, gate GREEN (vm1, 2026-10-16)

- Test-only commit `7cab082b1a7a` on top of 229947201fd7 (PeerSyncTest.java only), pushed plainly to fork `solr-11475-submit` after the gate; the fork tip is verified at 7cab082b1a7a7a238e046186d5376ca05345e220 by ls-remote.
- Full hardening gate at 7cab082b1a7a (log `g11475-pf-gate.log`, seed 11475C0FFEE11475): changelog YAML parse rc=0; tidy rc=0, tree unchanged; Error Prone compile (main and test) rc=0; repaired fail-before proof PASS (PeerSyncTest against merge-base production fails by the designed assertion `handleVersionsWithRanges did not terminate`, fresh JUnit XML 1 test / 1 failure / 0 errors, no OutOfMemoryError in the XML or the log); focused tests at the head, from fresh JUnit XML: PeerSyncTest 1/1, PeerSyncWithLeaderTest 1/1, PeerSyncWithBufferUpdatesTest 1/1, PeerSyncWithIndexFingerprintCachingTest 1/1, PeerSyncWithLeaderAndIndexFingerprintCachingTest 1/1; module check `:solr:core:check -x test` rc=0. GATE RESULT: GREEN.
- Records: `receipts/SOLR-11475.md` refreshed at the new head (the 2026-10-08 INCONCLUSIVE line kept as history, scoped to the pre-repair test code); outcome appended to the takeover log; this claim marked DONE; the assignment's Status set to DONE.
- Not touched, per the assignment: the sign-mismatch behavior call in the audit stays a PR-readiness blocker; no PR body or draft was edited.
