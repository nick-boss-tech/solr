# Remaining dependency PR reviews and JCL metadata correction

Status: review complete; metadata repairs recommended before positive gates. Windows ran no Gradle, formatter, compilation or tests. This report is not a Linux claim or a passing receipt.

## Reviewed identities

- PR 5276: https://github.com/apache/solr/pull/5276; head a6d008eb38d508b0179c71bd6a862e2a7b40fb24.
- PR 5277: https://github.com/apache/solr/pull/5277; head 83cbfa380859ee7844df1be3077952fc478cc958.
- PR 5278: https://github.com/apache/solr/pull/5278; head 82f60b1b7976c187541a6f6808a781bb7f8b9228.
- Shared target/base: branch_9x at daa4b3013503d8ae628b118e9c2a763a58751499. Fetched heads matched live reads; no comments or reviews were present. Board snapshot reviewed: 52770a3a4cf574ed8ea2c0ae7dd840ffa38a33c5. No matching 5276-5278 claims/gates/receipts found.

## Findings

PR 5276 is not ready. [Exact-head precommit 41268286728](https://github.com/apache/solr/actions/runs/41268286728) fails :solr:modules:gcs-repository:validateJarLicenses for dev.cel common/protobuf/runtime 0.13.1 and org.threeten threeten-extra 1.8.0. Their checksums exist; license files do not. Published POMs declare Apache 2.0 for the three CEL artifacts and BSD 3-clause for ThreeTen Extra. grpc-xds introduces the CEL dependencies; CEL runtime introduces ThreeTen. Add correct upstream license/notice metadata before a new positive gate. Do not blindly rerun the known-failing original check. Test CI passed but does not close this packaging failure.

PR 5277 has no blocking finding in the complete metadata diff and inspected JSON/Smile/StAX consumers. All 13 new JAR hashes match Maven Central; Java 11-visible ordinary classes have majors at most 55. Root precommit and both test workflows passed at its head. jackson-annotations stays 2.22 intentionally under the 2.22.4 BOM; woodstox-core is unchanged. No additional targeted gate gap established.

PR 5278 needs a JCL bridge metadata correction despite green CI. Actual jcl-over-slf4j 2.0.20 META-INF/LICENSE.txt is Apache 2.0; Solr's existing JCL license and notice still contain MIT text. The [SLF4J 2.0.19 notes](https://www.slf4j.org/news.html) explain that this corrects upstream JAR packaging, not a new license grant. The [tagged module license](https://github.com/qos-ch/slf4j/blob/v_2.0.20/jcl-over-slf4j/LICENSE.txt) agrees with the JAR. gradle/validation/jar-checks.gradle:229-259 accepts a recognized filename suffix and does not compare upstream license contents. Keep the SLF4J API and JUL bridge's MIT metadata separate from JCL.

## Correction to the earlier PR 5274 review and gate limits

PR 5274 at 3f827a780a292feddeb58077dc531fc3c0086d0c selects exactly the same three SLF4J 2.0.20 checksum files as PR 5278. The initial review missed this JCL metadata concern and has been amended locally. The same correction applies there. The existing gates/pr-5274-extraction-docker-round-1.md remains useful for its Tika/Docker scope at that head, but its receipt cannot settle this license metadata finding or establish overall PR readiness. A later source repair needs a separately verified fork head and superseding pinned proof. Leave the immutable original job untouched.

## Handoff scope

No implementation is authorized by this review-only report. After a scoped metadata repair is authorized, preserve contributor ancestry on separate nick-boss-tech/solr branches, commit/plain-push and separately verify the full remote head. Then post exact-head Linux jobs using Java 11 and Gradle 8.10.

For 5276: tidy, :solr:modules:gcs-repository:licenses, GCSBackupRepositoryTest with seed 5276C0DE and fresh XML, root check -x test, clean status and remote read-back. Inspect license/notice provenance for all four artifacts. For 5278 (and a reconciled 5274 repair as applicable): tidy, server/core licenses, root check -x test, manual inspection of the selected JCL license/notice classification and packaged metadata, clean status and remote read-back. A content audit is required because the existing filename-only check can pass stale MIT metadata. No runtime fail-before/mutation proof is required for these license metadata repairs.

The complete local records are research/branch-reviews/2026-11-01-solrbot-batch/PR-5276-review.md, PR-5277-review.md, PR-5278-review.md and LICENSE-METADATA-FOLLOWUP-PLAN.md on Windows. All 57 added JAR hashes matched Maven Central; their Java 11-visible class headers were compatible. All full logs, POMs and artifact results are retained in each evidence/PR-<n>/ folder. Native/platform loading and all JDKs were not independently tested. No source branch, public PR/JIRA comment, approval or merge was changed.

Prepublication reconciliation: board advanced to 27d6f3d379503d488295e62eae1b70fe1806832b with only unrelated SOLR-16013 records; no overlap with these PR reviews or the PR 5274 gate.
