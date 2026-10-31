# Gate and Linux repair: PR 5268 container platform follow-up, round 1

Status: OPEN. Dependency-metadata repair is required first; the initial source head is not yet gate-ready. Immutable once posted. A repair changes the head and requires a new superseding gate before positive proof.

## Identity and ownership

- Intended host: any available capable pool Linux host with implementation, gate, review and records tags; JDK 21, the pinned Gradle wrapper, Python 3, dependency/nightlies access, working Mockito and Docker with access to apache/kafka:4.3.1 and adobe/s3mock:5.1.0.
- One gate at a time under the claimant's documented host build lock. Use a clean task-specific worktree; never another task's checkout.
- Original PR: https://github.com/apache/solr/pull/5268; no JIRA ticket is assigned.
- Fork: nick-boss-tech/solr; branch: pr-5268-container-platform-fixes.
- Initial implementation head H0: 83a5c0d6e9f8758e26dddc217d7a3eec2339600c.
- Contributor head / implementation parent: 705142f6ff0ef6e61c5db747a7a30c12d2fc4ed0.
- Windows implementation claim: claims/pr-5268-container-platform-fixes-windows-round-1.md.
- Source assessment and remaining work: reports/pr-5268-container-platform-fixes-round-1.md.
- Deliverable receipt: receipts/PR-5268-container-platform-fixes.md, owned by the claiming Linux gate host. Record final tested head and all logs/XML, not H0 by default.
- Linux claim must name host, toolchain, build lock, worktree, runner, log directory and UTC start time. No Linux gate is claimed by Windows.

## Phase A: finish the known dependency repair

H0 deliberately does not guess generated lockfiles. The contributor added implementation libs.testcontainers to the ubiquitous test framework without refreshing dependency locks. Original-head CI failed resolving :solr:test-framework:compileJava and :solr:api:test before any tests ran. This is a known repair task, not an unexplained failing-test rerun.

1. Fetch the fork branch and verify H0 before starting. Read this job, its claim and report from one board snapshot. If the source ref moved, inspect the added commits and reconcile before changing it.
2. Run the repository-prescribed dependency workflow on Linux: `./gradlew updateLicenses resolveAndLockAll --write-locks`. Inspect every generated lock/checksum change. Framework and affected consumer configurations, including API test runtime, must be covered. Preserve pinned versions and normal dependency locking. Do not make unrelated dependency upgrades or silently omit modules.
3. Run `./gradlew tidy`. Commit the generated metadata and any formatting-only repairs with Nick Shanin's ICLA identity, no attribution trailers. Keep commit messages descriptive, without #PR references. Plain-push to the same fork branch; never force or push to the contributor's fork/apache.
4. Separately read back the remote branch SHA. Let H1 be the full resulting SHA. Post an add-only report naming H0, H1 and the exact generated files, plus a new superseding gate pinned to H1 and carrying the proof legs below. Do not edit this job or call any H0 run proof for H1. If another repair changes H1 later, repeat the new-head recording before proof.
5. Phase A completes only with a clean committed tree and normal locked dependency resolution at H1. The current failing CI links in the report establish the original missing-lock problem; a redundant original-head build is not required.

## Phase B: positive proof at the final pinned head

Retain individual command logs and fresh JUnit XML per leg. Use seed 5268C0DE. Fresh XML means cleanTest before each test leg and an archive immediately after it, before another cleanTest removes it. No task exclusion other than the explicitly requested -x test in the root check. No skipped test may satisfy a required leg.

1. Verify `./gradlew tidy` exits zero and leaves the final head clean. Verify `git diff --check`.
2. Focused deterministic helper regression:
   `./gradlew :solr:cross-dc-manager:cleanTest :solr:cross-dc-manager:test --tests org.apache.solr.util.TestcontainersUtilTest -Ptests.seed=5268C0DE`
   Require all nine test methods executed, no failures/errors/skips. This class uses mocked Docker commands and real docker-java callbacks; it needs working Mockito, not a live registry.
3. Normal locked framework compilation and API consumer test:
   `./gradlew :solr:test-framework:compileJava :solr:api:cleanTest :solr:api:test --tests org.apache.solr.client.api.model.SchemaChangeOperationSerializationTest -Ptests.seed=5268C0DE`
   Require successful locked resolution and compilation, all API tests in the class executed (13 methods at H0), no failures/errors/skips. Do not pass --write-locks here.
4. Kafka consumer smoke on working Docker:
   `./gradlew :solr:cross-dc-manager:cleanTest :solr:cross-dc-manager:test --tests org.apache.solr.crossdc.manager.DeleteByQueryToIdTest -Ptests.seed=5268C0DE`
   Require its testExpandDBQ method to execute and pass. Docker-unavailable assumptions are not a pass. This class is used instead of RetryQueueIntegrationTest, which is Nightly and explicitly too slow for normal runs.
5. S3 consumer smoke on working Docker:
   `./gradlew :solr:modules:s3-repository:cleanTest :solr:modules:s3-repository:test --tests org.apache.solr.s3.S3OutputStreamTest -Ptests.seed=5268C0DE`
   Require all five test methods executed, no failures/errors/skips.
6. Root required checks: `./gradlew check -x test`. Include license/checksum/changelog/formatting results; record real failures, setup conditions and any explicit repair. A partial/module-only check is not this leg.
7. Finish with clean git status, git diff --check and a separate remote-head read matching the tested SHA. Validate the added changelog YAML (type other, Nick Shanin, PR 5268 link, no nick field). Required source/test files must be unchanged during proof.

## Phase C: discriminating negative proof

The new deterministic test calls a package-private injection overload absent from the original PR, so overlaying the test on 705142f6 alone is not executable fail-before evidence. Use a separate clean detached negative worktree at the final repaired head H1 (or its explicitly superseding head), keeping the lock/tidy repairs, tests and overload declaration. Record the synthetic tree, helper/test hashes and the exact mutation diff.

In only the injected hasS390xVariant overload, restore the original three decisions while keeping the signature and mocked-client seam:

1. Set imageName from DockerImageName.parse(dockerImage) directly, bypassing imageNameSubstitutor.apply.
2. Return the boolean from awaitCompletion directly, without throwing on a false timeout result.
3. On InterruptedException, restore the interrupt flag and return false. Replace the selective DockerClientException/NotFoundException handler with catch (RuntimeException e) returning false unconditionally.

Remove imports made unused only in the synthetic helper if its compile checks require it; do not change tests, fixture setup, dependency metadata or production callers. These changes recreate the old skip/error policy and missing image substitution within an executable API scaffold. This is a synthetic behavior baseline, not a claim that the original contributor tree contains the injection overload.

Run the same focused helper command from Phase B leg 2 in the negative worktree. Expected result: nine methods execute; exactly these seven fail for intended assertions:

- testRegistryFailuresPropagate
- testOtherPlatformMismatchPropagates
- testCallbackFailurePropagates
- testUnexpectedFailurePropagates
- testTimeoutFailsAndClosesPull
- testInterruptedPullFailsAndPreservesInterrupt
- testImageSubstitution

The first six must fail because the expected exception is not thrown. The mirror test must fail because the substituted registry image was not requested. testSuccessfulPull and testMissingPlatform must still pass. An import/compile error, Mockito attachment refusal, class skip, missing method, unrelated assertion or startup error is not regression proof. Do not commit or push the negative mutation. If observations differ, investigate and record the mismatch before retrying or altering acceptance.

## Completion and limits

GREEN requires Phase A's pushed final metadata repair, a superseding exact-head job, every positive leg above passing, the expected negative failures, a clean tree and retained logs/fresh XML. A gate that executed tests and failed is investigated and repaired under WORKFLOW.md, never blindly rerun. Receipt must name the final source head, host architecture, counts, commands, seed, metadata repair commit, mutation diff and proof limits.

This gate covers the review repair delta and ordinary consumers on the Linux host's architecture. It does not claim native s390x UI source-build/download acceptance, remote-daemon architecture support or emulation. The contributor's UI changes are unchanged from its original head. Any native architecture evidence is a separate clearly named leg; do not pretend the injected unit tests supply it. The receipt's final section should be a factual proposed upstream follow-up with the pinned contributor-to-final-head diff and these limits. Posting a public response is not part of this gate handoff.
