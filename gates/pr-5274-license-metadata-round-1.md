# Gate assignment: PR 5274 repaired license metadata and Docker extraction, round 1

Status: OPEN. Implemented, Proof Pending. Windows does not claim execution.

## Identity and capability

- Public PR: https://github.com/apache/solr/pull/5274; original contributor P: 3f827a780a292feddeb58077dc531fc3c0086d0c.
- Fork: https://github.com/nick-boss-tech/solr.git; branch pr-5274-jcl-license-metadata.
- Exact required pushed head H: e3bcf3af6fcb544c624efdcb4329ce5ef5a271f3. Separate remote ref read verified H. Fetch this branch and require the full SHA; use a clean task-specific checkout at H.
- Implementation claim: claims/pr-5274-license-metadata-windows-round-1.md.
- Implementation report and exact outbound files/provenance: reports/pr-5274-license-metadata-implementation-round-1.md.
- Deliverable: receipts/PR-5274-license-metadata.md, owned by the claiming execution host.
- Intended host: any available capable Linux host with gate/review/records tags, JDK 11 and pinned Gradle 8.10, Git and dependency access, plus a working Docker-compatible daemon reachable by Testcontainers from the runner account and image apache/tika:3.2.3.0-full on the executing architecture. s390x is unsuitable because the test explicitly skips it.

This job supersedes gates/pr-5274-extraction-docker-round-1.md for repaired-head acceptance, retaining its Docker scope and adding the license correction. No live execution claim or receipt was found for that old job at the checked snapshot. Claim this current job, not both. Results from original P or the sibling PR 5278 repair cannot prove H.

## Claim and preflight

Read live WORKFLOW.md, START-HERE.md, this job and its report from one board snapshot. Check existing claims and the host build lock; one gate/build at a time per host. Add a new immutable execution claim naming this job, host, UTC start, architecture, JDK and wrapper versions, clean checkout, runner, build lock and full log/XML archive paths, Docker version/availability and image compatibility. Establish capability before claiming; do not claim a Docker-required job on a no-Docker host.

Refresh the fork ref and live PR head/discussion before proof. Require H and parent P with the stated ancestry. If either source moved or an overlapping claim/receipt appeared, inspect and reconcile before execution; never silently substitute another head. A changed head requires an add-only superseding pinned job before positive proof.

Run tidy preflight on the clean head. If it changes files, retain the diff and report the cause. Only scoped license metadata repairs are authorized on this fork branch; no broad source formatting, dependency regeneration or production edits. A scoped repair must be committed/plain-pushed, separately read back and pinned in a superseding gate before further positive proof. Never push to apache/solr or solrbot's fork, force-push or edit this immutable job.

## Required proof

Run required commands separately under the host lock, saving complete logs, exit codes and fresh XML where tests execute. Archive XML before another cleanTest can overwrite it. Do not disable locking/security checks, rewrite dependency locks/checksums, use updateLicenses, omit tasks or exclude anything beyond the expressly named -x test root-check argument. This patch changes license files only and does not add/change dependencies. No runtime fail-before or mutation test is required.

1. ./gradlew --no-daemon tidy; require no source diff and clean git diff --check.
2. ./gradlew --no-daemon :solr:server:licenses :solr:core:licenses --info
   Require JCL to select ASL, exactly one matching JCL license file and the present empty notice companion. The stale MIT path must be absent. Require API and JUL bridge metadata to remain MIT. Retain the full log; recognized suffix validation alone is insufficient.
3. Manually compare the committed JCL license with the exact 2.0.20 JAR and tag; normalize only line endings and require SHA-256 59899c6091b540582ed617e8eeaac4919dc985ccfc35459ee9752b699be5205b. Confirm no upstream module/artifact NOTICE was omitted. Empty companion files are an established repo convention, not placeholders. Inspect distribution copy coverage at solr/packaging/build.gradle:90-93, inherited by the full distribution. Record that source-copy-rule evidence separately from any actual archive result; archive assembly is not required.
4. ./gradlew --no-daemon :solr:modules:extraction:cleanTest :solr:modules:extraction:check -Ptests.seed=5274C0DE
   Require zero extraction failures/errors. In fresh XML for org.apache.solr.handler.extraction.TikaServerExtractionBackendTest, require these five methods to actually execute and pass with no skips: testExtractTextAndMetadata, testExtractWithSaxHandlerXml, testPdfWithImageRecursive, testMaxCharsLimitEnforced, testMaxCharsLimitEnforcedWithSaxHandler. Record actual total and inherited case counts and explain any other skips. Verify real apache/tika:3.2.3.0-full container startup. The test catches container startup Throwable and assumes unavailable Docker away, so a green task with assumptions does not satisfy this leg. The PR upgrades Tika client libraries, not that server image.
5. ./gradlew --no-daemon check -x test

Finish with clean git status and git diff --check, and separately read the remote fork head and live PR head again. H must still be current for a current-head result. If the PR moved, preserve H evidence and clearly mark current integration pending; do not propose stale merge commands.

## Failure and receipt

Diagnose and record any task/assertion failure or unexplained skip before retry. An environment refusal before test execution may be relaunched once after a documented environment repair under WORKFLOW.md. An executed test failure needs diagnosis and a recorded repair decision; no blind reruns or weakened assertions.

GREEN requires all named legs at H and no remaining content/provenance/scope concern. Write the host-owned receipt with job/claim links, P/H, commands, UTC times, environment, exit codes, counts and method outcomes where applicable, full logs and fresh XML locations, license hash/classification audit, copy-rule evidence, tidy/root results, source cleanliness and final remote reads.  Include a concise factual follow-up draft with the pinned P-to-H diff and exact proof limits; public communication is handled separately under root authorization rules. Leave NOT GREEN while any required leg or unexplained concern remains.
