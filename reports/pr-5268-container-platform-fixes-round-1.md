# PR 5268: container platform probe repair, round 1

Status: Implemented, Proof Pending. F2 source repair is pushed; F1 generated dependency metadata remains a required Linux repair leg before validation.

## Revision and scope

- User direction: build a patch for the review findings and hand it to the gate agent.
- Fork: nick-boss-tech/solr.
- Branch: pr-5268-container-platform-fixes.
- Pushed and separately verified head: 83a5c0d6e9f8758e26dddc217d7a3eec2339600c.
- Parent / original contributor head: 705142f6ff0ef6e61c5db747a7a30c12d2fc4ed0. PR 5268 was unchanged at the implementation refresh.
- Delta: https://github.com/nick-boss-tech/solr/compare/705142f6ff0ef6e61c5db747a7a30c12d2fc4ed0...83a5c0d6e9f8758e26dddc217d7a3eec2339600c
- Claim: claims/pr-5268-container-platform-fixes-windows-round-1.md. Its started time says 00:39 UTC in error; the claim was posted at approximately 2026-10-31 00:33 UTC, with the post operation starting at 00:33:24 UTC. This report corrects the timestamp without editing the immutable claim.
- Windows tree: C:/Users/shaninna/dev/Solr-issues/wt/PR-5268-container-platform-fixes.

Three outbound files above the contributor head:

1. solr/test-framework/src/java/org/apache/solr/util/TestcontainersUtil.java
2. solr/cross-dc-manager/src/test/org/apache/solr/util/TestcontainersUtilTest.java
3. changelog/unreleased/PR-5268-container-platform-probe.yml

No UI source or contributor-branch changes were made. The public PR head did not move. The commit author is Nick Shanin and there are no attribution trailers.

## Behavior and source review

The helper returns false only for a DockerClientException or NotFoundException containing Docker's explicit `no matching manifest for linux/s390x in the manifest list entries` diagnostic. Registry failures and unrelated runtime exceptions propagate. A timed-out pull throws IllegalStateException; interruption restores the interrupt flag and throws with the InterruptedException as its cause. The image name passes through Testcontainers' ImageNameSubstitutor before the pull, with repository and tag passed separately.

The error text is necessarily a narrow daemon diagnostic, not a structured manifest-platform field. Moby emits it in daemon/internal/distribution/pull_v2.go and normalizes containerd platform errors to it in daemon/containerd/image_pull.go:

- https://github.com/moby/moby/blob/master/daemon/internal/distribution/pull_v2.go
- https://github.com/moby/moby/blob/master/daemon/containerd/image_pull.go

Unknown platform-error wording fails conservatively rather than skipping. The existing JVM-architecture check and the forced pull policy are retained; Docker-server architecture, emulation and offline cached-image behavior are not broadened or claimed as fixed.

The only new method is package-private and injects the Docker client, image substitutor and timeout for deterministic tests. The test sits in cross-dc-manager because that module already has Mockito, Docker/Testcontainers and its test security-manager setup; it adds no dependency declaration, schema or configset. It extends SolrTestCase, reuses the existing Mockito availability helper, uses the real docker-java callback for completion/error/timeout behavior, and makes no Docker or registry connections. The tests cover success, missing platform, registry failures, a mismatch for another platform, callback errors, unexpected runtime failures, timeout cleanup, interrupted-pull cleanup and image substitution. Nine test methods exist; none has executed on this Windows host.

Both KafkaContainerRule and S3MockContainerRule still call the same public helper before starting their containers. Their cleanup paths tolerate a pre-allocation skip. No new production API or security boundary is introduced.

## F1 is explicitly pending Linux repair

The original `implementation libs.testcontainers` declaration is retained. Its missing generated lock state is not fixed by the source commit. Current original-head CI already demonstrates failures in test-framework compileClasspath and api testRuntimeClasspath:

- https://github.com/apache/solr/actions/runs/41055530574/job/124064998848
- https://github.com/apache/solr/actions/runs/41055531281/job/124065013959

Do not attempt positive proof at the pushed source head before resolving this known condition. The Linux handoff must run `./gradlew updateLicenses resolveAndLockAll --write-locks`, review the generated changes across all affected modules, commit required lock/checksum changes and any tidy repair, push, verify the remote final head and post a superseding exact-head gate. Generated metadata is not guessed or hand-edited on Windows. F1 closes only when normal locked builds pass at that final head, without --write-locks or disabled locking.

The PR-specific changelog fragment is type other, links the assigned PR and names Nick Shanin only. It supplies an entry for the current changelog-check failure without requiring a public label/comment action; Linux must validate its YAML and the normal checks.

## Handoff and verification limits

Gate: gates/pr-5268-container-platform-fixes-round-1.md. It names dependency-metadata repair, exact-head focused and neighboring tests, Docker integration smoke tests, tidy, root check, and a synthetic fail-before leg for the behavior changes.

Windows checks: inspected the full outbound source/test/changelog content; git diff --check and staged diff --check passed; the publish helper rejected any file outside the three-file allowlist; plain fork push succeeded and a separate GitHub ref read returned 83a5c0d6e9f8758e26dddc217d7a3eec2339600c. No Gradle, compilation, formatting task or local test queue was used. The source commit remains uncompiled and untested. No public GitHub or JIRA action was taken.

This handoff validates the review repair delta. Native s390x UI source-build/download behavior from the contributor's original changes remains outside that delta and needs separate architecture-specific evidence before representing the whole PR as validated.
