# Gate: distributed command admission and rejected async ID reuse

Status: OPEN. Tags: gate, review, records. Intended host: capable Linux CPU
gate host with JDK 21 and this branch's pinned Gradle/dependencies.

- Source claim: `claims/ref-fork-porting-windows-source-round-1.md`.
- Umbrella context: SOLR-17293; this is coverage-only, not a new bug ticket.
- Fork: `nick-boss-tech/solr`, branch `solr-17293-distributed-admission-tests`.
- Exact head: `b85f3fe3476dc3bd7f4ac3dad5f92c1b27395d26`.
- Original production/base: `c0c219e374611e0edc475c0f793c9bd688b13292`.
- Fixed seed: `17293C0FFEE`.
- Receipt: `receipts/ref-fork-solr-17293-admission-round-1.md`.

The new test holds a real collection write lock and fills the command pool
with existing mock commands waiting for read locks. A bounded probe reaches
rejection without a sleep barrier. It checks 503, rejected-ID absence, accepted-
ID duplicate rejection, successful drain and reuse of the rejected ID. There
is no production change, scheduler prototype or claim of original failure.

Claim separately, serialize under the host lock, verify the live head, and
use full clean checkouts. Preserve commands, checkout paths, toolchain,
exit codes, fresh logs/XML and all counts. Missing/skipped tests do not pass.

## Required legs

1. Original base: run the existing suites:
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.DistributedApiAsyncTrackerTest --tests org.apache.solr.cloud.api.collections.CollectionApiLockingTest -Ptests.seed=17293C0FFEE`.
   Expected pass. This is the original-suite baseline, not fail-before.
2. Head: `./gradlew tidy`, require a clean tree; then
   `./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
3. Head:
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.api.collections.DistributedCommandAdmissionTest --tests org.apache.solr.cloud.DistributedApiAsyncTrackerTest --tests org.apache.solr.cloud.api.collections.CollectionApiLockingTest -Ptests.seed=17293C0FFEE`.
   Expected pass, including the new test. The new suite must use distributed
   mode as configured and reach the rejection/assertion path.
4. Isolated mutation at the head: leave tests unchanged. Remove only
   `asyncTaskTracker.cancelAsyncId(adminCmdContext.getAsyncId());` from the
   RejectedExecutionException catch in `runCollectionCommand` of
   `DistributedCollectionConfigSetCommandRunner.java`. Retain the rest of the
   API/scaffold and do not mutate ConfigSet execution. Run
   `./gradlew :solr:core:test --tests org.apache.solr.cloud.api.collections.DistributedCommandAdmissionTest -Ptests.seed=17293C0FFEE`.
   Require runtime failure at the new rejected-ID tracking assertion, retain
   mutation diff and fresh logs/XML, then discard only the isolated mutation
   checkout after recording it. Do not commit the mutation or change the
   offered branch. A compile/startup failure is not mutation proof.
5. Unmutated head:
   `./gradlew :solr:core:check -x test -Pvalidation.errorprone=true`, then
   `./gradlew check -x test -Pvalidation.errorprone=true`.

The mutation demonstrates rejected-ID cleanup coverage only. Duplicate-ID,
drain and response-marker assertions are coverage pins unless separately
proved; do not call the entire test mutation-proven. Shutdown, session loss,
cross-node saturation and performance remain outside this suite.

Investigate executed failures before retrying and record environment failures
separately. Source/formatting changes require a pushed verified head and a
superseding gate. No PR/Jira posting is authorized. Finish with the named
receipt and a factual statement that baseline and added coverage both pass.
