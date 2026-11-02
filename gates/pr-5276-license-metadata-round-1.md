# Gate assignment: PR 5276 repaired license metadata, round 1

Status: CLOSED (2026-11-02, owner direction). Superseded by the posted PR comment route; see reports/pr-5274-5276-5278-license-gates-closure-round-1.md. Not GREEN; no receipt will be written.

## Identity and capability

- Public PR: https://github.com/apache/solr/pull/5276; original contributor P: a6d008eb38d508b0179c71bd6a862e2a7b40fb24.
- Fork: https://github.com/nick-boss-tech/solr.git; branch pr-5276-license-metadata.
- Exact required pushed head H: 5f9613e13865b73b992d8321142e61ceddee2b92. Separate remote ref read verified H. Fetch this branch and require the full SHA; use a clean task-specific checkout at H.
- Implementation claim: claims/pr-5276-license-metadata-windows-round-1.md.
- Implementation report and exact outbound files/provenance: reports/pr-5276-license-metadata-implementation-round-1.md.
- Deliverable: receipts/PR-5276-license-metadata.md, owned by the claiming execution host.
- Intended host: any available capable Linux host with gate/review/records tags, JDK 11 and pinned Gradle 8.10, Git and dependency access.



## Claim and preflight

Read live WORKFLOW.md, START-HERE.md, this job and its report from one board snapshot. Check existing claims and the host build lock; one gate/build at a time per host. Add a new immutable execution claim naming this job, host, UTC start, architecture, JDK and wrapper versions, clean checkout, runner, build lock and full log/XML archive paths. Establish capability before claiming; do not claim a Docker-required job on a no-Docker host.

Refresh the fork ref and live PR head/discussion before proof. Require H and parent P with the stated ancestry. If either source moved or an overlapping claim/receipt appeared, inspect and reconcile before execution; never silently substitute another head. A changed head requires an add-only superseding pinned job before positive proof.

Run tidy preflight on the clean head. If it changes files, retain the diff and report the cause. Only scoped license metadata repairs are authorized on this fork branch; no broad source formatting, dependency regeneration or production edits. A scoped repair must be committed/plain-pushed, separately read back and pinned in a superseding gate before further positive proof. Never push to apache/solr or solrbot's fork, force-push or edit this immutable job.

## Required proof

Run required commands separately under the host lock, saving complete logs, exit codes and fresh XML where tests execute. Archive XML before another cleanTest can overwrite it. Do not disable locking/security checks, rewrite dependency locks/checksums, use updateLicenses, omit tasks or exclude anything beyond the expressly named -x test root-check argument. This patch changes license files only and does not add/change dependencies. No runtime fail-before or mutation test is required.

1. ./gradlew --no-daemon tidy; require no source diff and clean git diff --check.
2. ./gradlew --no-daemon :solr:modules:gcs-repository:licenses --info
   Require all four new artifacts to resolve with the intended ASL/ASL/ASL/BSD license suffixes, existing JAR checksums and the four present empty NOTICE companions. Keep the full log.
3. ./gradlew --no-daemon :solr:modules:gcs-repository:cleanTest :solr:modules:gcs-repository:test --tests org.apache.solr.gcs.GCSBackupRepositoryTest -Ptests.seed=5276C0DE
   Require the complete selected suite to execute with zero failures/errors/skips; retain actual method counts and fresh XML. This uses in-memory Storage and does not claim real GCS or gRPC interoperability.
4. ./gradlew --no-daemon check -x test
5. Audit the committed license contents against the exact pinned provenance and hashes in the report; empty companions are intentional, not omitted files. Inspect the distribution license-copy rule at solr/packaging/build.gradle:90-93 (the full distribution inherits the slim contents) and confirm every new file is covered. Record source-copy-rule evidence separately from any produced binary archive; no archive assembly is required by this metadata gate.

Finish with clean git status and git diff --check, and separately read the remote fork head and live PR head again. H must still be current for a current-head result. If the PR moved, preserve H evidence and clearly mark current integration pending; do not propose stale merge commands.

## Failure and receipt

Diagnose and record any task/assertion failure or unexplained skip before retry. An environment refusal before test execution may be relaunched once after a documented environment repair under WORKFLOW.md. An executed test failure needs diagnosis and a recorded repair decision; no blind reruns or weakened assertions.

GREEN requires all named legs at H and no remaining content/provenance/scope concern. Write the host-owned receipt with job/claim links, P/H, commands, UTC times, environment, exit codes, counts and method outcomes where applicable, full logs and fresh XML locations, license hash/classification audit, copy-rule evidence, tidy/root results, source cleanliness and final remote reads. Retain https://github.com/apache/solr/actions/runs/41268286728 as the known original four-license failure; do not relabel it as a runtime regression proof. Include a concise factual follow-up draft with the pinned P-to-H diff and exact proof limits; public communication is handled separately under root authorization rules. Leave NOT GREEN while any required leg or unexplained concern remains.
