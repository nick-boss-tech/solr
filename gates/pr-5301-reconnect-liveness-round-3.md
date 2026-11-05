# Gate: SOLR-18623 failed-gate test repairs, round 3

Status: OPEN. Implemented, Proof Pending. Repair of an executed failure, not an unexplained retry.

- Intended host: any capable Linux CPU gate host with gate, implementation, review and records tags; preferably phone-agent while its failure evidence and three clones remain available. Serialize under the host build lock. Use JDK 21 and branch_9x's Gradle 8.10 wrapper/runner, Java 11 source compatibility.
- Repository/branch: `nick-boss-tech/solr:solr-18623-reconnect-liveness`.
- Exact H3: `dfde9bde0a348ad44e6f6ceec197d478ef11bfd0`, independently confirmed from the fork refs API after the plain push.
- Parent H2: `27a9cf105b25f1bf3d604534cf8bdd8674c470fc`. Contributor H0: `01e820f0539cddc4697d26176d216614a776a99a`, PR 5301, base branch_9x.
- Supersedes rounds 1/2 for acceptance at H3. Preserve `receipts/PR-5301-reconnect-liveness-round-1.md` as the failed H2 record.
- Repair claim: `claims/pr-5301-reconnect-repair-windows-round-3.md`. Report: `reports/pr-5301-reconnect-repair-round-3.md`.
- New receipt: `receipts/PR-5301-reconnect-liveness-round-3.md`, owned by the executing gate host.
- Fixed seed: `5301C0DE`, including stress and every negative leg. Stress failure sub-seed at H2: `5301C0DE:AEBABCFD74B0F4C6`.

## Failure investigation completed before this repair

H2 passed the five deterministic tests, existing manager and client suites, exact-H0 liveness negative, and three accept-all mutation negatives. It failed the root source-pattern rule for missing assumeWorkingMockito(), and failed the nightly stress twice at injected-expiration recovery. The error-prone FloatRangeFieldTest warning was separately reproduced at H0. Raw evidence is in `reports/pr-5301-reconnect-liveness-round-1-evidence/`, archive SHA-256 `80a8d934a581dfb6979c22943793795cfb7717044fd2691057582637f4c29e33`.

The stress logs show the proxy no longer accepting connections after close/reopen; the replacement repeatedly receives connection-refused replies. SocketProxy.reopen hides setup failures at debug level, so the exact hidden exception is not established. H3 keeps the listener open and calls the existing halfClose() to drop client connections; all original observations, seeds, timeouts and expiration injections remain. The new suite also has the required static Mockito guard. Only the two test files change; production and changelogs are identical to H2. This diagnosis must be confirmed by the runtime comparison below.

## Claim and pristine preflight

Refresh live PR, fork ref, board claims and receipts. Require H3, or reconcile a newer head before running. Claim with exact SHA, host/toolchain, isolated checkout, build lock, runner and fresh log/XML paths. Run tidy preflight. A tidy/source/test repair needs a scoped ordinary fork commit, remote read-back and a superseding job before any proof is attributed to the new head. No force-push or contributor-branch write.

## Required positive proof at H3

1. `./gradlew tidy` and `git diff --check`: success and no tracked change. Generate changelog with `./gradlew logchangeGenerate`, require both SOLR-18623 entries and retain the generated output. Do not commit generated output. Existing cross-branch validateChangelogs.py bookkeeping errors must be identified separately; none may name an invalid current fragment.
2. Run the source-pattern task before expensive tests:

   `./gradlew :solr:core:validateSourcePatterns`

   Require success, including the Mockito rule.
3. Keep Error Prone enabled and complete affected compilation using the build's documented warning-tolerance option:

   `./gradlew :solr:solrj-zookeeper:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true -Pjavac.failOnWarnings=false`

   Require success and inspect the full diagnostic log. The only accepted warning is the already archived H0-matched FloatRangeFieldTest.java:328 FloatingPointLiteralPrecision warning, outside the diff. Any new diagnostic requires investigation and remains a concern; warning tolerance is not permission to overlook it. Record this execution condition and do not call the result a strict warning-free compile. Do not edit unrelated source or disable Error Prone.
4. Fresh ordinary suites:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest --tests org.apache.solr.cloud.ConnectionManagerTest -Ptests.seed=5301C0DE --rerun-tasks`

   Require all five new methods to execute and pass with no assumption skips despite the added Mockito guard. Existing ConnectionManagerTest must execute testLikelyExpired and testReconnectWhenZkDisappeared; record only its pre-existing ignored test separately.
5. Fresh client neighbor:

   `./gradlew :solr:solrj-zookeeper:cleanTest :solr:solrj-zookeeper:test --tests org.apache.solr.common.cloud.SolrZkClientTest -Ptests.seed=5301C0DE --rerun-tasks`

   Require the selected suite to execute and pass; retain counts and explain any pre-existing assumptions individually.
6. Run the repaired nightly stress **twice in independent invocations** at the failed seed, preserving fresh XML/logs separately before the next cleanTest:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.TestConnectionManagerReconnectStress -Ptests.nightly=true -Ptests.seed=5301C0DE --rerun-tasks`

   Both runs must execute the method with zero failures/errors/skips, all 25 iterations/five expirations, actual Disconnected observations, and matching processed/delayed counts. Inspect logs for accepted/re-established connections and an available listener through expiration recovery; do not infer recovery from a successful test process alone. No longer waits, changed seeds, skipped expiration or weakened assertions. A failure must be diagnosed before any further run.
7. Root check:

   `./gradlew check -x test`

   Require success at the pristine offered head. Record environment conditions. Root check is not waived because the earlier Error Prone warning is pre-existing.

## Controlled stress-fixture comparison

Use a separate disposable H3-based checkout and retain the H3 assertions/counters/seed/deadlines. Change only flap() back to H2's `proxy.close(); proxy.reopen();`, removing the H3 halfClose line/comment. Save exact diff and hashes. Run the same explicitly enabled stress command once, keeping fresh evidence distinct from H3's two positives.

Expected: reproduce the expiration-recovery assertion failure and capture the proxy connection-refused/no-acceptance condition seen twice at H2. The exact swallowed reopen exception may still be unavailable; do not invent it. If it does not reproduce, report the non-reproduction and classify this as an intermittent listener-lifecycle avoidance rather than claiming a deterministic fixture fail-before. The two H3 positives are still required, but do not declare complete causal proof or publish an unqualified explanation while an execution concern remains. Do not induce failures with sleeps or exceptions. Never push this comparison to the offered branch.

## Original liveness and stale-state negatives at H3

Keep the new tests unchanged, including their Mockito guard. Use two further disposable H3 checkouts or reuse the isolated comparison checkout only after restoring a pristine H3 tree and proving the next exact diff.

A. Replace only ConnectionManager.java with its exact H0 blob. Run:

`./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testExpiredReplacementDoesNotBlockRecovery -Ptests.seed=5301C0DE --rerun-tasks`

Require one executed bounded runtime failure at `An expired replacement must not block the next reconnect`, not a build timeout, leaked thread, compilation/startup failure or skip. Save baseline diff, hashes and fresh XML/logs.

B. At pristine H3, change only acceptStateEvent(long) to return true unconditionally, preserving APIs. Run:

`./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testStaleDisconnectedDoesNotUndoConnectedState --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testStaleConnectedDoesNotUndoDisconnectedState --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testStaleConnectedDoesNotClearExpiredState -Ptests.seed=5301C0DE --rerun-tasks`

Require three executed failures at the intended connected-state assertions. Save exact mutation and hashes and fresh logs/XML. Label B synthetic. Four methods are discriminated across A and B; the fifth in-order test and secondary assertions remain coverage pins. Never claim all five are fail-before proven.

## Finish and retry constraints

Keep the failed H2 runs and diagnoses. No silent retry of executed failures. A setup failure that prevents all tests from running may be repaired and relaunched once under WORKFLOW.md, with the cause recorded; it is neither negative proof nor a pass. Any source/test/tidy change creates a new head and needs a new job.

Restore and verify the pristine positive checkout; separately read the remote offered ref and require the tested head. Write the new reserved receipt with all commands, exits, UTC times, host/toolchain/flags, logs, fresh XML counts, two H3 stress positives, controlled comparison outcome and limits, both original negative legs, tidy/changelog/root results and the explicit Error Prone baseline-warning condition. If any required proof or unresolved diagnosis remains, record a non-green disposition and the exact next step. A posted job is not executed proof.

No public PR/JIRA comment, PR body edit, merge, upstream push or contributor-branch change is authorized by this job. The review owner retains the pending draft and reconciles complete proof with live PR state before publication.
