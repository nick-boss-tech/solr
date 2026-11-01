# PR 5107 security setup review fixes, round 2

Status: Implemented, Proof Pending

Nick requested implementation of the November 1 review findings on Eric's current branch and a handoff to a Linux gate agent. Windows implemented and plain-pushed one scoped commit. No public comment or contributor-branch write was made.

## Revisions and delivery

- Public PR: https://github.com/apache/solr/pull/5107 (Eric Pugh, draft).
- Ticket: SOLR-8442.
- Contributor parent C: `25c88fb1c432120b98f7af668cf58be0052d13d0`, still the live PR head when checked before committing.
- Fork branch: `nick-boss-tech/solr:solr-8442-security-review-r2-submit`.
- Implementation head H: `795b821a36e7d395b80ee3473bea4de82875e397`, one ordinary child commit above C.
- Commit: `SOLR-8442: preserve TLS policy and validate setup credentials`; author and committer Nick Shanin, no trailers.
- Plain push and separate GitHub ref API read both returned H.
- Pinned diff: https://github.com/nick-boss-tech/solr/compare/25c88fb1c432120b98f7af668cf58be0052d13d0...795b821a36e7d395b80ee3473bea4de82875e397
- Claim: `claims/pr-5107-security-review-fixes-windows-round-2.md`, posted at `22d2db9fb0c78f7bd6900fcfccd9ceb6e4cda8fb`.
- Gate: `gates/pr-5107-security-review-fixes-round-2.md`.
- Required receipt: `receipts/PR-5107-security-review-fixes-round-2.md`.

## Changes

1. `SSLTool` leaves existing `SOLR_SSL_NEED_CLIENT_AUTH` and `SOLR_SSL_WANT_CLIENT_AUTH` assignments intact. Fresh configurations use Solr's existing disabled default; command invocation no longer resets a required or optional client-certificate policy.
2. `SecureExampleCredentials.setAdministrator` uses the shared password policy before mutating the saved credentials. An equal username/password is rejected before TLS generation, credential persistence or node startup, with the established compatibility override retained.
3. PEM input-password validation accepts a nonempty single line, independently of the new output-keystore password's six-character minimum. Both paths still reject embedded newline/CR/NUL.
4. Regression tests cover required and optional client-authentication settings; successful encrypted-PEM import with a four-character existing password; wrong PEM and short new-keystore passwords leaving configuration untouched; rejected credentials without in-memory mutation or persistence; and the password-policy override.
5. CLI/BATS coverage verifies invalid supplied credentials create no TLS/ZK/HTTP security state, and an actual certificate replacement followed by restart retains mTLS: the certificate-bearing client succeeds, while a client without a certificate cannot complete the connection. Its negative curl intentionally omits `--fail`, so HTTP denials cannot masquerade as TLS rejection.
6. The SSL guide describes the retained client-authentication policy and separate password rules. It also makes the existing generated-truststore limit explicit: private trust anchors beyond JDK roots and the supplied chain must be restored before restart. The patch does not add a general truststore-merging mechanism or change that existing behavior.

## Exact outbound files

Eight files; 198 insertions and 10 deletions relative to C:

- `changelog/unreleased/SOLR-8442-security-setup-validation.yml`
- `solr/core/src/java/org/apache/solr/cli/SSLTool.java`
- `solr/core/src/java/org/apache/solr/cli/SecureExampleCredentials.java`
- `solr/core/src/test/org/apache/solr/cli/SSLToolTest.java`
- `solr/core/src/test/org/apache/solr/cli/SecureExampleCredentialsTest.java`
- `solr/packaging/test/test_secure_example.bats`
- `solr/packaging/test/test_ssl.bats`
- `solr/solr-ref-guide/modules/deployment-guide/pages/enabling-ssl.adoc`

## Verification and limits

Windows inspected the exact diff, reused the contributor's test fixtures and BATS helpers, hand-formatted the edits, verified the outbound file list and ICLA identity, and ran `git diff --check` successfully. No Gradle, compilation, tests, OpenSSL, keytool, server startup, HTTP probe or retired Windows queue operation ran. All new tests are pending Linux execution.

The earlier `solr-8442-pr-5107-submit` branch at `5ff8f324ef3` remains separate and is not merged here. Its receipt explicitly lacked the root-only ownership proof. Eric replaced that old writer with in-place writes on the current branch; this patch does not touch writer ownership or claim that the old gate is green. Do not transplant the old patch or reuse its receipt for H.

The new gate requires positive tests, default and security-manager-off unit variants, both CLI parsers in packaging, actual TLS behavior, original-production fail-before proof with unchanged new tests, tidy and the root check. A gate posting is a handoff, not a Linux execution claim or green result. No FedRAMP/FIPS or broader production-readiness claim is made.
