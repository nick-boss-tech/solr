# PR draft: SOLR-14649 (not opened)

- Proposed title: SOLR-14649: Require SHA-512 for package signatures
- Head: nick-boss-tech:solr-14649-submit at 1767e9dce81453233bad30c49b5b19dfff8a017a. Base: main at 79b23df58daac14b92cc35f23b8b81f72098b4ba.
- Gate: green at this head on 2026-10-23 (receipts/SOLR-14649.md on coordination).
- Status: draft only. Nothing opened or posted.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-14649

**Package and filestore signatures are checked with SHA-1.**

Both `CryptoKeys.verify` overloads call `Signature.getInstance("SHA1withRSA")` ([CryptoKeys.java, lines 123 and 136 at the base](https://github.com/nick-boss-tech/solr/blob/79b23df58daac14b92cc35f23b8b81f72098b4ba/solr/core/src/java/org/apache/solr/util/CryptoKeys.java#L123-L136)). Package and filestore signature checks go through these methods.

**This change requires SHA-512 and rejects SHA-1 signatures on both paths.**

Both overloads now use `SHA512withRSA` ([lines 123 and 136 at the head](https://github.com/nick-boss-tech/solr/blob/1767e9dce81453233bad30c49b5b19dfff8a017a/solr/core/src/java/org/apache/solr/util/CryptoKeys.java#L123-L136)). The SHA-256 PKI path is unchanged. The Ref Guide signing commands in [package-manager-internals.adoc](https://github.com/nick-boss-tech/solr/blob/1767e9dce81453233bad30c49b5b19dfff8a017a/solr/solr-ref-guide/modules/configuration-guide/pages/package-manager-internals.adoc) use SHA-512. The changelog entry is [SOLR-14649-package-signatures-sha512.yml](https://github.com/nick-boss-tech/solr/blob/1767e9dce81453233bad30c49b5b19dfff8a017a/changelog/unreleased/SOLR-14649-package-signatures-sha512.yml).

The test repository for the question-answer package used a 512-bit RSA key. That key cannot sign with SHA-512, so the test key pair is now 2048 bits, and both jar signatures in the test repository were regenerated. No production key changed.

**The new tests fail on the base code and pass with the change.**

- Base code, `TestCryptoKeys` (4 tests, seed `014649C0FFEE0001`, run 2026-10-23): with the base `CryptoKeys.java` and the new tests in place, all 4 fail. SHA-512 signatures are rejected and SHA-1 signatures are accepted, for both byte-buffer and stream verification.
- Head `1767e9dce8`, fresh JUnit XML, 15 tests, 0 failures:
  - `TestCryptoKeys` 4 of 4
  - `TestDistribFileStore` 1 of 1
  - `TestPackages` 4 of 4
  - `TestContainerPlugin` 3 of 3
  - `PackageStoreSchemaPluginsTest` 1 of 1
  - `PackageToolTest` 2 of 2
- Error Prone compile and `:solr:core:check -x test` both pass at the head.

**Compatibility**

This change intentionally rejects SHA-1 signatures on the package and filestore verification paths. Published repositories that still contain SHA-1-signed packages will need to re-sign them with SHA-512 before this Solr version can install them. A published-package inventory was not part of this change.
**Limits**

- Only package and filestore verification changed. The SHA-256 PKI path is unchanged.
- The test key rotation touches test resources only.
- The gate ran `:solr:core` checks and the focused tests above, not the full build for every module.
