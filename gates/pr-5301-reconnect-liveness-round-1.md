# Gate: SOLR-18623 replacement-session recovery and ordering coverage, round 1

Status: OPEN. Implemented, Proof Pending; execution is unclaimed at posting.

- Intended host: any capable Linux CPU gate host with gate, implementation, review and records tags, JDK 21, this branch's Gradle 8.10 wrapper and pinned dependencies. Source remains Java 11 compatible. Serialize under the host build lock.
- Repository: nick-boss-tech/solr. Branch: `solr-18623-reconnect-liveness`.
- Exact offered H1: `19579b2ed2397929c0cc903a6c682331cc865586`, independently confirmed through the fork refs API after the plain push.
- Contributor H0: `01e820f0539cddc4697d26176d216614a776a99a`, apache/solr PR 5301, branch_9x. Preserve H0 ancestry; the contributor's branch is not writable by this job.
- Claim: `claims/pr-5301-reconnect-windows-round-1.md`.
- Implementation report: `reports/pr-5301-reconnect-implementation-round-1.md`.
- Reserved execution receipt: `receipts/PR-5301-reconnect-liveness-round-1.md`, written only by the gate host.
- Seed: `5301C0DE`. Changed modules: solrj-zookeeper production and core tests; one new changelog fragment.

## Scope and preflight

The PR filters obsolete state callbacks, but its reconnect thread waits indefinitely for the replacement's connected flag. If a later Disconnected has invalidated that callback and the replacement expires, its Expired callback is queued behind the waiting reconnect thread. H1 checks the candidate's actual state and exits the wait when it is terminal, allowing the existing retry path to create a healthy session. State-event ordering remains guarded. A new ordinary test suite forces the ordering and uses future completion barriers. The existing nightly integration stress test now counts the post-state processing logs rather than only sleeping-appender exits.

Refresh the live board, fork ref and PR discussion, check for an existing gate claim/receipt, and require H1 before execution. Claim with host, toolchain, exact checkout, build lock, runner, command logs and fresh XML paths. Run the detached tidy preflight. No Windows Gradle, compilation, formatting or tests have run. Original upstream CI covers H0's merge result, not H1, and does not establish execution of the nightly class.

## Positive proof at pristine H1

Retain separate logs/exits and fresh XML for each test leg; do not reuse XML from earlier invocations.

1. Run `./gradlew tidy` followed by `git diff --check`; require no tracked changes. Validate the added changelog fragment through the repository's parser/check. A formatting change requires the repair route below.
2. Compile changed source and tests with Error Prone enabled:

   `./gradlew :solr:solrj-zookeeper:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`

3. Run the ordinary deterministic suite and existing connection-manager neighbor:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest --tests org.apache.solr.cloud.ConnectionManagerTest -Ptests.seed=5301C0DE --rerun-tasks`

   All five new methods must execute and pass with no skips. Record existing suite counts and its pre-existing ignored `testConnectionManager` separately. Require `testLikelyExpired` and `testReconnectWhenZkDisappeared` to execute successfully. The new liveness test must finish both the first expiration future and the queued replacement-expiration future, install only the healthy keeper, close the failed keeper and make exactly two replacement attempts.
4. Run the real-client module neighbor:

   `./gradlew :solr:solrj-zookeeper:cleanTest :solr:solrj-zookeeper:test --tests org.apache.solr.common.cloud.SolrZkClientTest -Ptests.seed=5301C0DE --rerun-tasks`

   Require the selected suite to execute and pass. Record any pre-existing assumptions individually; an entirely skipped suite is incomplete proof.
5. Explicitly enable and execute the modified real-ZooKeeper stress class:

   `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.cloud.TestConnectionManagerReconnectStress -Ptests.nightly=true -Ptests.seed=5301C0DE --rerun-tasks`

   Require its method to execute, zero failures/errors/skips, 25 flaps/expirations, and matching delayed/processed callback counts. A default run with nightly disabled does not satisfy this leg. Do not describe the stress run as deterministic fail-before proof.
6. Run `./gradlew check -x test`; require success and record material environment conditions alongside it.

## Negative leg A: exact original PR production code

Use a separate disposable H1 checkout. Leave all H1 tests unchanged. Replace only `solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/ConnectionManager.java` with the exact blob from H0 (`01e820f0539cddc4697d26176d216614a776a99a`). Keep all other production files at H1. Save the baseline diff and source/test hashes. The new tests use package-private APIs already present at H0, so no scaffold or test adaptation is needed.

Run:

`./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testExpiredReplacementDoesNotBlockRecovery -Ptests.seed=5301C0DE --rerun-tasks`

Require one executed runtime failure at the bounded assertion `An expired replacement must not block the next reconnect`. The new test closes the manager and interrupts/joins its executor in finally, so the intended result is a recorded assertion failure, not a whole-build timeout or leaked-thread failure. Compilation errors, setup failures, missing APIs, skip, or a different assertion do not count. Preserve the fresh failing log/XML. At pristine H1 the same unchanged test must pass.

## Negative leg B: discriminate stale-state assertions

Use another disposable H1 checkout. Retain tests unchanged and all API signatures. Change only `ConnectionManager.acceptStateEvent(long)` to return true unconditionally, modeling the old unconditional state-application behavior. Save the exact mutation diff and source/test hashes. This is a synthetic mutation, not a claim that unmodified pre-PR production code compiled with the new test APIs.

Run:

`./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testStaleDisconnectedDoesNotUndoConnectedState --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testStaleConnectedDoesNotUndoDisconnectedState --tests org.apache.solr.common.cloud.ConnectionManagerOrderingTest.testStaleConnectedDoesNotClearExpiredState -Ptests.seed=5301C0DE --rerun-tasks`

Require all three methods to execute and fail at their first intended connected-state assertion after the out-of-order callback. Record each failure from fresh XML, distinguish it from the exact H0 liveness failure in leg A, and show the positive three at pristine H1. Secondary assertions and the ordinary in-order test remain coverage pins unless separately discriminated. Do not delete methods or induce compile/startup failures as a mutation.

## Repair and retry rules

- Preserve and investigate an executed failure before retrying. Do not weaken assertions, lengthen waits to conceal a deadlock, remove nightly coverage, or silently relaunch a failed test.
- If setup prevented every selected test from running, record the cause and exact environment repair; one relaunch is allowed under WORKFLOW.md. Setup refusal is neither regression proof nor a pass.
- Any source, test or tidy repair changes the head: make a scoped ordinary commit using Nick Shanin's identity, no trailers; plain-push only this fork branch; read back its full SHA; post a superseding add-only gate. No force-push. Proof at H1 does not certify a repaired head.
- Keep mutation/baseline work isolated. Never push it to the offered branch, and restore a pristine positive checkout before final verification.

## Finish

Read back the exact fork head separately and require the tested head. Write the reserved receipt with host/JDK/Gradle, exact SHA, UTC times, commands and exits, fresh log/XML paths/counts for every positive and negative leg, tidy/changelog/root results, baseline/mutation diffs and limits. End with a concise factual PR description/comment evidence draft. Add a completion report; do not edit the implementation claim or this job. Missing proof stays pending.

This gate authorizes no public PR/JIRA comment, contributor-branch write, PR body edit, merge or upstream push. The review owner will reconcile the live PR and prepared comment after the receipt is complete.
