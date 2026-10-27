# Gate: SOLR-18564 / PR 5192 typed executor timeout follow-up, round 1

Status: OPEN
Tags: gate, premise-run, review, records.
Intended host: any capable Linux gate host with JDK 21 and this branch's build dependencies, one gate at a time under its host lock.

## Pinned inputs and scope

- Fork: `nick-boss-tech/solr:corecontainer-shutdown-closes`, apache/solr PR 5192.
- Fixed head F: `bd42a1d58b9fba4656a200536a007d9d79dc45b2`.
- Original PR head O / parent: `dd65b21b41dc1d008c51031b71f61964ee2ca169`. F is a normal follow-up commit; no rebase or force-push.
- Direct owner authorization: Nick asked the Windows agent to implement a specific unchecked timeout exception, narrow the CoreContainer catch, and hand it to the gate, after sigram's comment https://github.com/apache/solr/pull/5192#issuecomment-6439747314.
- Implementation claim: `claims/solr-18564-timeout-type-round-1-windows.md`. Implementation report: `reports/solr-18564-timeout-type-round-1-windows.md`.
- Delta O..F: three files only: `solr/core/src/java/org/apache/solr/core/CoreContainer.java`, `solr/solrj/src/java/org/apache/solr/common/util/ExecutorUtil.java`, and `solr/solrj/src/test/org/apache/solr/common/util/ExecutorUtilTest.java`.
- `ExecutorUtil.ExecutorTerminationTimeoutException` extends RuntimeException, with construction confined to ExecutorUtil. The timeout throw retains its exact message and executor details. CoreContainer catches this type only. Timing, forced shutdown, interrupt restoration, and the later closes are unchanged.
- The existing fast utility timeout test now requires the specific type and asserts the message prefix and pool name. The existing two-minute CoreContainerShutdownTest is unchanged and must continue to pass through the real production timeout path.
- Supersedes `gates/SOLR-18564-pr5192-exact-head.md` for verification of F only. Older receipts remain evidence for O, not proof for F. Windows ran no Gradle or tests.

## Required proof and commands

Use clean detached worktrees and the supported Linux runner, JDK 21, an absolute test work directory, fresh XML, and one serialized build at a time. Record the full commands, environment, head and test blob hashes, exit codes, log paths, and XML counts. Unset NODE_ENV for the successful check environment and record that condition. Do not count UP-TO-DATE or cached test tasks as fresh runs. Use `--no-daemon --no-build-cache` on the commands below.

1. Verify the live fork and PR head still equal F. Claim this new job after its detached preflight, checking existing claims and receipts so this job runs on one host only.
2. Tidy at F, tree must stay clean. Parse the existing `changelog/unreleased/SOLR-18564.yml` (Nick Shanin, name only).
3. Error Prone production/test compilation for both touched modules:
   `./gradlew --no-daemon --no-build-cache :solr:solrj:compileJava :solr:solrj:compileTestJava :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`
4. Focused F tests, seed `18564ABC0001`, after the indicated cleanTest tasks:
   - `./gradlew --no-daemon --no-build-cache :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.util.ExecutorUtilTest -Ptests.seed=18564ABC0001 -Ptests.workDir=/tmp/solr18564-timeout-type-tests`
   - `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.core.CoreContainerShutdownTest --tests org.apache.solr.core.TestCoreContainer -Ptests.seed=18564ABC0001 -Ptests.workDir=/tmp/solr18564-timeout-type-tests`
5. Repeat both focused commands with seed `18564ABC0002`, using cleanTest and capturing each run's XML separately. All executed tests must pass. Record skipped tests separately; the three Windows-only TestCoreContainer tests are expected to skip on Linux. CoreContainerShutdownTest intentionally takes about two minutes per run.
6. Matched mutation proof for the strengthened timeout-type assertion, seed `18564ABC0001`:
   - In a separate F worktree, replace only the timeout throw's `new ExecutorTerminationTimeoutException(` with `new RuntimeException(` in ExecutorUtil.java. Keep the new nested exception declaration and F test file unchanged, so the test compiles. Record the one-line mutation diff and test blob SHA. This is a synthetic mutation of F, not an unmodified O run.
   - Run `./gradlew --no-daemon --no-build-cache :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.util.ExecutorUtilTest.testExecutorUtilAwaitsTerminationWhenTaskIgnoresInterupt -Ptests.seed=18564ABC0001 -Ptests.workDir=/tmp/solr18564-timeout-type-mutation-tests`.
   - Required failure: the expected ExecutorTerminationTimeoutException is replaced by the old plain RuntimeException, with the timeout message. An unrelated failure or a compilation error does not count. Compare with the same method's fresh passing XML from F at the same seed. Do not publish the mutation.
   - The existing original cleanup fail-before proof is recorded in `reports/rc3-test-failure-causes-round-2.md` and older receipts. This new mutation proves the narrower type contract; it is not a replay of the original natural teardown incident.
7. At pristine F, final tidy must leave the tree clean, then run the root check: `env -u NODE_ENV ./gradlew --no-daemon --no-build-cache check -x test`. Use the root check because the public utility in solrj is consumed across modules.

## Failure handling and deliverable

- If tidy changes the tree, the gate is INVALID for F. Commit only the formatting repair with Nick's identity, push a normal fast-forward to the same fork branch, verify the fork and PR head, and post a superseding gate pinned to the repaired head before treating any subsequent proof as final. Never rewrite history or silently count proof at another head.
- If any required test executes and fails, investigate it as a repair under this claim; record the failure and diagnosis before retrying. An environment failure before tests execute can be relaunched once. Do not broaden exclusions or replace the named targets without documenting the reason.
- Do not modify shared board job or claim files or another host's receipts. Add your own claim and a fresh receipt at `receipts/SOLR-18564-timeout-type-round-1.md`, linking this job and the exact final tested head. Include actual counts, required mutation failure details, tidy/compile/root-check results, and log/XML paths. Mark GREEN only when every required leg is satisfied.
- No public comment, PR body edit, JIRA write, approval, or merge is part of this gate. Return the receipt for review of the response to sigram. The patch remains Implemented, Proof Pending until the exact-head receipt is green.
