# Gate continuation: PR 5268 Docker consumer smoke tests at H1

Status: OPEN. Only Phase B legs 4 and 5 remain. The full gate is NOT GREEN until both execute successfully. This job supplements the H1 gate; it does not invalidate or repeat its completed proof.

## Exact source and prior proof

- Fork: nick-boss-tech/solr; branch: pr-5268-container-platform-fixes.
- Required head H1: a18d913f2908f0ddf244b43384bd3caecbe9bc6e. Separately verified against the live fork ref on 2026-10-31.
- Original contributor head: 705142f6ff0ef6e61c5db747a7a30c12d2fc4ed0. Public PR: https://github.com/apache/solr/pull/5268; its head remains unchanged.
- H1 job: gates/pr-5268-container-platform-fixes-linux-gate-round-2.md, inheriting the full acceptance requirements from gates/pr-5268-container-platform-fixes-round-1.md.
- Prior receipt: receipts/PR-5268-container-platform-fixes.md, read at board snapshot e12395ab91daa14fec5b177cb493cfe38a0a9978. It belongs to vm5; other hosts must not edit it.
- vm5 claim: claims/pr-5268-container-platform-fixes-linux-round-1.md. Its stated slice covers metadata repair, non-Docker legs and negative proof; it explicitly hands the Docker legs to a capable host. This continuation is a disjoint unclaimed slice, not a takeover of vm5's work.
- Prior logs: /workspace/gates/logs/vm5/pr-5268-container-platform-fixes-round-1/ on vm5, with fresh XML archives leg2-xml, leg3-xml, leg4-xml, leg5-xml and phaseC-xml.

vm5's receipt records successful metadata regeneration, tidy/clean status, 9/9 helper tests, 13/13 API serialization tests and framework compilation, changelog validation, root check -x test, and the expected seven failures out of nine in the synthetic negative proof. H1 adds 22 dependency lockfile repairs and two formatting-only source reflows. The source diffs were checked on Windows and contain only those reflows. No source change is requested here.

The root check passed in a plain clone of H1. Its earlier linked-worktree attempt failed because Antora requires a .git directory; that failed attempt is not counted. Keep this documented execution condition with the reused proof. Do not rerun the passed legs, metadata generation, tidy or root check merely because another host completes the two Docker legs at the same head.

## Capability and claim

Intended host: any available capable Linux gate host with gate/review/records tags, JDK 21, the pinned Gradle 9.7.0 wrapper, a working Docker-compatible daemon reachable by Testcontainers, and access to apache/kafka:4.3.1 and adobe/s3mock:5.1.0 on its host architecture. vm5 has neither Docker nor Podman and cannot perform this slice. This is not a Windows test assignment.

Before claiming, establish that the daemon is usable from the test runner's account and that required container images support this host. A machine that can only produce Docker-unavailable assumptions is not capable of completing this job. Do not claim and rerun on the same no-Docker setup. Record host architecture, JDK, Docker/runtime version, Gradle, task-specific checkout, runner, logs, UTC start and the host's build lock in a new immutable claim. Follow WORKFLOW.md; one build/gate at a time per host. Check for an existing live claim of this Docker continuation first.

Fetch and verify H1, use a clean checkout at that exact commit, and check that the live source branch still equals H1 before execution. Keep seed 5268C0DE. If the branch moved, reconcile before proceeding; do not count H1 receipts as proof of another commit.

## Required legs

Run separately under the host's build lock, keeping the complete log and fresh JUnit XML for each leg. Archive its XML before any subsequent cleanTest can remove it. No --write-locks, dependency-lock disabling, task exclusions or source/config changes are part of this continuation.

1. Kafka consumer:
   `./gradlew :solr:cross-dc-manager:cleanTest :solr:cross-dc-manager:test --tests org.apache.solr.crossdc.manager.DeleteByQueryToIdTest -Ptests.seed=5268C0DE`
   Require testExpandDBQ to execute and pass, with zero failed, errored or skipped test cases. Confirm KafkaContainerRule starts the real Kafka container; an assumption due to unavailable Docker is not proof.
2. S3 consumer:
   `./gradlew :solr:modules:s3-repository:cleanTest :solr:modules:s3-repository:test --tests org.apache.solr.s3.S3OutputStreamTest -Ptests.seed=5268C0DE`
   Require all selected test cases to execute and pass with zero failures, errors or skips. The five methods declared in this class are testWriteByteByByte, testWriteSmallBuffer, testWriteLargeBuffer, testFlushSmallBuffer and testFlushLargeBuffer. Record the runner's actual case count, including inherited cases if present. vm5's receipt reported six skipped entries; do not copy a skipped-run count as executed proof. Confirm S3MockContainerRule starts the real S3Mock container.

After both legs, retain clean git status and git diff --check output. Separately re-read the fork head and require H1. A runtime/image/registry or assertion failure must be investigated and recorded; do not convert it to a skip, omit the leg or relaunch blindly.

## Completion and record ownership

Write a new host-owned receipt at receipts/PR-5268-container-platform-fixes-docker-h1.md naming H1, the claim and this job, host/runtime details, commands/seed, exact executed/failure/error/skip counts, complete logs and fresh XML paths. Leave vm5's original receipt and gate files untouched.

On success, add reports/pr-5268-container-platform-fixes-h1-closure.md linking both receipts and verifying they cover the same H1. The overall H1 gate may be GREEN from the union of vm5's completed legs and this Docker receipt only when every original required leg is satisfied and no new concern or unexplained failure remains. Report the source SHA, Docker receipt URL and closure report to Nick. If either required smoke test skips or fails, retain NOT GREEN and report the concrete result instead.

If a source or test change becomes necessary, stop treating the earlier H1 proof as final. Make a separately recorded repair commit on the fork, verify the new remote head and create a superseding exact-head job with the required fresh proof. Do not force-push or write to the contributor's branch.

This continuation covers the ordinary Kafka and S3 consumers on the executing host's architecture. It adds no native s390x UI, remote-daemon architecture or emulation claim. The synthetic helper proof remains explicitly synthetic. No public PR/JIRA action is part of this gate slice.
