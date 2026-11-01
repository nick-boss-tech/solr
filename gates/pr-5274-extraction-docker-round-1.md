# Gate assignment: dependency upgrade extraction checks with Docker, round 1

Status: OPEN. Verification pending; no Linux execution has been claimed by the posting Windows host.

## Identity and capability

- PR: https://github.com/apache/solr/pull/5274, targeting branch_9x. No JIRA ticket or implementation patch.
- Contributor repository: solrbot/apache-_-solr; branch: renovate/all-non-major-dependencies.
- Exact required head H: 3f827a780a292feddeb58077dc531fc3c0086d0c.
- PR base at preparation: daa4b3013503d8ae628b118e9c2a763a58751499.
- Source retrieval: fetch refs/pull/5274/head from https://github.com/apache/solr.git and require the fetched full SHA to equal H. There is no separate Nick fork source branch; the fork's coordination-ui branch carries this assignment only.
- Intended host: any available capable Linux gate host with gate, review and records tags, JDK 11, pinned Gradle 8.10, and a working Docker-compatible daemon reachable by Testcontainers from the runner account. Required image: apache/tika:3.2.3.0-full. Exclude s390x because the test explicitly assumes it away.
- Receipt: receipts/PR-5274-extraction-docker-round-1.md, owned by the executing host.
- Claim: add a new immutable claims/pr-5274-extraction-docker-round-1-<host>.md before proof; record host, UTC start, architecture, JDK, Docker/runtime and wrapper versions, task-specific checkout, runner, log/XML directories and host build lock. Check live claims first. One gate/build at a time per host.

## Reason and scope

This checks the existing dependency upgrade, particularly Tika 3.3.2 to 3.3.3, without changing production code or tests. Windows reviewed the complete diff, matching lock entries/checksums, Java 11 class compatibility and the extraction consumers. At H, upstream Crave tests, SolrJ tests and root precommit succeeded. Crave reported extraction 30 tests, 19 skipped; its log does not identify each skipped method. The Docker extraction workflow did not run: .github/workflows/gradle-extraction-check.yml watches only solr/modules/extraction/**, while this PR changes root dependency metadata and license checksums.

TikaServerExtractionBackendTest.startTikaServer catches any container startup Throwable and converts it to an assumption. A green task alone therefore does not prove execution. The external server image stays at 3.2.3.0-full; this PR upgrades client libraries, not that image. This assignment requires fresh XML showing all five declared methods actually execute.

## Preflight and exact-head rules

Read WORKFLOW.md, START-HERE.md and this job from one live board snapshot. Confirm the live PR is still open at H, and inspect current discussion for superseding evidence or requests. If its head moved, report the delta and request/post a superseding pinned job before proof; do not substitute a new head silently. Use a clean task-specific checkout of H. Establish that Docker works and the image supports the host architecture before claiming execution. A no-Docker host cannot complete this slice.

Run the detached tidy preflight under the host build lock. Record the command, exit status and diff. If tidy or compilation requires source repairs, retain the diff and report the issue as an add-only report. This verification-only assignment does not authorize a repair or a push to the bot/contributor branch. Stop positive proof until a separately authorized fork repair and superseding job exist. Do not treat a modified tree as H.

## Required proof

Use seed 5274C0DE and retain a complete separate log plus fresh XML for the command. No dependency lock rewriting, dependency-lock disabling, security-manager disabling, task exclusions or source/config changes. No fail-before or mutation proof is needed for an unchanged dependency-update PR.

1. Tidy must succeed and leave H clean; retain git diff --check and git status.
2. Run: ./gradlew --no-daemon :solr:modules:extraction:cleanTest :solr:modules:extraction:check -Ptests.seed=5274C0DE
3. Require zero failures/errors in the extraction suite. Inspect the fresh XML for org.apache.solr.handler.extraction.TikaServerExtractionBackendTest and require each of these methods to execute and pass with zero skips: testExtractTextAndMetadata, testExtractWithSaxHandlerXml, testPdfWithImageRecursive, testMaxCharsLimitEnforced, testMaxCharsLimitEnforcedWithSaxHandler. Record actual total executed and skipped counts, including inherited methods separately. Explain any other skipped cases; the five required methods cannot be skipped. Confirm real container startup and retain relevant Docker/Testcontainers diagnostics.
4. Run ./gradlew --no-daemon check -x test. This named root-check leg is required for this gate; retain its separate log and any setup conditions. Do not count earlier CI as this leg.
5. Archive XML before any later cleanTest; finish with a clean checkout and git diff --check. Separately refresh the live PR head and require H. If it moved during proof, retain the results as historical H evidence and leave current-head verification pending.

## Failure and completion

Investigate and record any assertion failure, image/registry/runtime failure, assumption or unexplained skip before retrying. A run that never executes tests may be relaunched once after a documented environment repair under WORKFLOW.md. An executed test failure requires diagnosis and a recorded repair decision; no blind reruns, weakened assertions or converted skips.

Write the host-owned receipt with H, job/claim links, UTC times, environment, exact commands/seed, exit codes, all five method outcomes, total test/failure/error/skip counts, complete logs and fresh XML locations, tidy/root-check results, final source cleanliness and live PR-head read-back. Retain NOT GREEN if any required leg remains incomplete. A successful receipt establishes this focused verification scope only. No production change, new PR, public review/comment, merge or JIRA write is authorized by this assignment. The posting host ran no Gradle or tests on Windows.
