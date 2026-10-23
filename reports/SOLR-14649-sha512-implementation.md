# SOLR-14649 SHA-512 package signature implementation

Status: Implemented, proof pending

## Finding

Jira was read on 2026-10-23. SOLR-14649 is Open and requests that package signing move from SHA-1 to SHA-512. The accepted decision is to reject SHA-1 signatures on package and filestore verification. The independent SHA-256 PKI path remains unchanged.

## Changes

- Changed both package and filestore overloads in `CryptoKeys.verify` to use `SHA512withRSA`.
- Added four direct regression cases in `TestCryptoKeys`: SHA-512 acceptance and SHA-1 rejection for both `ByteBuffer` and `InputStream` verification.
- Updated package, container plugin, schema plugin, and repository test signatures for SHA-512 verification.
- Updated the package signing commands in the Ref Guide and added the SOLR-14649 changelog fragment.

## Branch and files

- Fork branch: `solr-14649-submit`.
- Base: `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Pushed head: `800e91188e0d9f975b9096c9413915ea18713b23`, confirmed by `git ls-remote` on 2026-10-23.
- Commits: `9c3bf304a5279dae3cdbc4ef3f30170d72aafd4d` and `800e91188e0d9f975b9096c9413915ea18713b23`.
- Changed paths: `changelog/unreleased/SOLR-14649-package-signatures-sha512.yml`; `solr/core/src/java/org/apache/solr/util/CryptoKeys.java`; `solr/core/src/test-files/runtimecode/sig.txt`; `solr/core/src/test-files/solr/question-answer-repository/repository.json`; `solr/core/src/test/org/apache/solr/cli/PackageToolTest.java`; `solr/core/src/test/org/apache/solr/filestore/TestDistribFileStore.java`; `solr/core/src/test/org/apache/solr/handler/TestContainerPlugin.java`; `solr/core/src/test/org/apache/solr/pkg/PackageStoreSchemaPluginsTest.java`; `solr/core/src/test/org/apache/solr/pkg/TestPackages.java`; `solr/core/src/test/org/apache/solr/util/TestCryptoKeys.java`; `solr/solr-ref-guide/modules/configuration-guide/pages/package-manager-internals.adoc`.

## Proof status

No Gradle tasks or local test queue were run on Windows. Exact-head Linux gate `SOLR-14649-phase-sha512-signatures` is OPEN at `gates/SOLR-14649-phase-sha512-signatures.md`. It requests the direct verifier tests and all five changed integration test classes, plus the core hardening steps. Status remains Implemented, proof pending until the gate reports green.
