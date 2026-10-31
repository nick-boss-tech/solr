# Receipt: PR 5268 container platform fixes, Linux gate on vm5

Status: NOT GREEN on vm5. Legs 1 to 3, 6 and C pass. Legs 4 and 5 ran every test as skipped because vm5 has no Docker or Podman, so they are not passes. The gate needs a Docker-capable host to run those two legs at H1 before it can be GREEN.

- Final source head tested: a18d913f2908f0ddf244b43384bd3caecbe9bc6e (remote read back 2026-10-31, matches).
- Initial implementation head H0: 83a5c0d6e9f8758e26dddc217d7a3eec2339600c. Contributor head: 705142f6ff0ef6e61c5db747a7a30c12d2fc4ed0.
- Metadata repair commit: a18d913f29 (Nick Shanin, no trailers): 22 gradle.lockfile files plus two formatting-only source reflows from tidy. Pinned versions unchanged.
- Host: vm5, Linux 6.1 aarch64, JDK 21.0.12.1+1, Gradle 9.7.0 wrapper, seed 5268C0DE, build lock /workspace/tools/.gradle-vm5.lock.
- Logs: /workspace/gates/logs/vm5/pr-5268-container-platform-fixes-round-1 (Phase A, tidy, legs 2 to 6, Phase C). Fresh XML copies in leg2-xml, leg3-xml, leg4-xml, leg5-xml, phaseC-xml.

## Results

| Leg | Command | Result |
| --- | --- | --- |
| Phase A | `./gradlew updateLicenses resolveAndLockAll --write-locks` | BUILD SUCCESSFUL, 5m 48s |
| B1 | `./gradlew tidy`, `git diff --check`, clean status at H1 | PASS |
| B2 | TestcontainersUtilTest | PASS, 9 of 9 executed, 0 failures, 0 skipped |
| B3 | SchemaChangeOperationSerializationTest, `:solr:test-framework:compileJava` | PASS, 13 of 13 executed, 0 failures, 0 skipped |
| B4 | DeleteByQueryToIdTest | NOT PASS: 1 test, 1 skipped (no Docker) |
| B5 | S3OutputStreamTest | NOT PASS: 6 tests, all 6 skipped (no Docker) |
| B6 | `./gradlew check -x test` | PASS in plain clone of H1 (BUILD SUCCESSFUL, 19m 12s). Attempt 1 in a linked worktree failed in :solr:solr-ref-guide:buildLocalAntoraSite with "Local content source must be a git repository"; the worktree's .git is a file. Recorded in leg6-attempt1-note.txt; not counted. |
| B7 | clean status, `git diff --check`, remote head read, changelog | PASS. Changelog changelog/unreleased/PR-5268-container-platform-probe.yml: type other, author Nick Shanin, PR-5268 link, no nick field. |
| C | Negative mutation, TestcontainersUtilTest | PASS as expected: 9 executed, exactly 7 failed for intended assertions |

## Phase C mutation

Worktree /workspace/worktrees/pool-vm5-5268-neg at H1, not committed or pushed. Mutation diff: phaseC-mutation.diff. Mutated hashes: phaseC-mutated-hashes.txt (TestcontainersUtil.java 0e7b1595..., TestcontainersUtilTest.java unchanged 8493f1a1...).

Failures:
- testInterruptedPullFailsAndPreservesInterrupt, testCallbackFailurePropagates, testUnexpectedFailurePropagates, testOtherPlatformMismatchPropagates, testRegistryFailuresPropagate, testTimeoutFailsAndClosesPull: "Expected exception ... but no exception was thrown".
- testImageSubstitution: Mockito ArgumentsAreDifferent; pullImageCmd was not requested with the substituted registry image.
- testSuccessfulPull and testMissingPlatform passed.

## Environment events

- VM fork at about 00:59 UTC interrupted Phase B legs 2 to 5 before any leg completed. Relaunched once, as the gate allows for runs that executed no tests (logs in relaunch-after-vm-fork-1/).
- Attempt 1 of leg 6 failed on the worktree layout described above, not on the PR.

## Proof limits

- No Docker on vm5: legs 4 and 5 unproven here. The Kafka consumer and S3 consumer smoke tests need a Docker host.
- Negative proof uses the injected synthetic overload only; the original contributor tree has no such overload.
- No native s390x evidence: this gate covers the review repair delta and ordinary consumers on aarch64 only.
- Upstream PR is unchanged; no public PR or JIRA action taken.
