# PR 5301 reconnect liveness gate, round 2: execution report

Job: gates/pr-5301-reconnect-liveness-round-2.md (superseding round 1 for the tidy-repaired head)
Executor: phone-agent (Linux, this VM), claim claims/pr-5301-reconnect-liveness-round-2-phone-agent.md
Candidate H2: 27a9cf105b25f1bf3d604534cf8bdd8674c470fc on fork branch solr-18623-reconnect-liveness (H1 19579b2ed239 plus one formatting-only tidy commit; repair report reports/pr-5301-reconnect-repair-round-1.md). Contributor head H0: 01e820f0539cddc4697d26176d216614a776a99a, base branch_9x.
Fixed seed: 5301C0DE. Builds via solr-gradle8.sh (Gradle 8.10, vendored JDK 21), serialized under the host build lock.
Legs ran 2026-11-05T22:16:01Z to 22:33:40Z; diagnostics 22:52:12Z to 22:57:45Z.

## Passing legs

- Ordering leg, rc 0: ConnectionManagerOrderingTest 5 of 5 pass. ConnectionManagerTest 3 tests, 0 failures, 1 skip; the skip is the pre-existing ignored testConnectionManager, and testLikelyExpired and testReconnectWhenZkDisappeared both executed and passed.
- SolrZkClientTest: 7 of 7 pass, rc 0.
- Negative leg A (ConnectionManager.java restored from the exact H0 blob in an H2 checkout): testExpiredReplacementDoesNotBlockRecovery failed at the specified bounded assertion, "An expired replacement must not block the next reconnect". rc 1 as required.
- Negative leg B (acceptStateEvent mutated to return true unconditionally): all three stale-state tests failed with AssertionError at their connected-state assertions. rc 1 as required.
- Changelog fragment: validateChangelogs.py cannot complete its global checks on this host (its four errors are cross-branch bookkeeping: released-version folders missing on other branches and files listed in both unreleased and released on branch_10x; none names this branch's fragment). As the positive substitute, logchangeGenerate at H2 exited rc 0 and generated both SOLR-18623 entries into CHANGELOG.md.

## Finding 1: root check fails on a source-pattern violation in the new test

Root check (check -x test) at H2, rc 1, fails at :solr:core:validateSourcePatterns: "File uses Mockito but has no assumeWorkingMockito() call: solr/core/src/test/org/apache/solr/common/cloud/ConnectionManagerOrderingTest.java". This is a defect in the change as offered. The standard repair is the static import of assumeWorkingMockito from SolrTestCaseJ4, per the project's test-writing rules. The gate did not apply it; a repair changes the candidate and needs its own round.

## Finding 2: the nightly stress test fails reproducibly at the pinned seed

TestConnectionManagerReconnectStress (with -Ptests.nightly=true) failed in the leg run (22:25:46Z) and again in an independent same-seed diagnostic rerun (22:55:03Z), both times with the identical sub-seed 5301C0DE:AEBABCFD74B0F4C6 and the identical assertion: "ConnectionManager did not re-establish the session after it expired", thrown from expireSession (test line 183, called from the flap loop at line 147) after an injected session expiration; the reconnect listener was not released within the drain window. Two identical failures at the pinned seed are a reproduction, not a single sighting. The deterministic ordering tests for the same logic all pass, so the gap is specific to the live expiration path or to the test's drain timing on this host; distinguishing those is implementation work and was not improvised inside the gate.

## Classified non-finding: Error Prone compile

The Error Prone compile leg at H2 fails (rc 1) on one warning: FloatRangeFieldTest.java:328, [FloatingPointLiteralPrecision], in a file the change does not touch. The diagnostic run repeated the identical compile at H0 and it failed identically (rc 1, same file, line and warning). The failure is pre-existing at the base head on this host and is not attributed to the branch.

## Verdict

GATE FAILED at H2 27a9cf105b25f1bf3d604534cf8bdd8674c470fc, on Finding 1 (source-pattern violation in the new ordering test) and Finding 2 (stress test reproducibly failing at the pinned seed). The core ordering proof, both negative legs, the neighboring suites and the changelog generation all pass. Raw evidence with a SHA-256 manifest is archived at reports/pr-5301-reconnect-liveness-round-1-evidence/.
