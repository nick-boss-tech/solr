# Pool assignment: SOLR-14649 package signature SHA-512 implementation

Status: CLAIMED (claims/pool-solr-14649-sha512-implementation-windows.md)

- Capability tags: implementation.
- Category: 7, permissions and security declarations.
- Intended host: Windows for source implementation; a capable Linux host for the exact-head gate.
- Posted by: Windows host, 2026-10-23, at the owner's direction in this session.
- Store: follow-up to `reports/csv-critblock-03.md`. That report checked the manifest digest and explicitly left the signing path unresolved. This is separate from the Linux-only slice in `assignments/pool-criticals-rest-round-1.md`.
- Evidence: Jira read 2026-10-23, status Open. At upstream/main `79b23df58daac14b92cc35f23b8b81f72098b4ba`, `CryptoKeys.verify` uses `SHA1withRSA` in both overloads, at `solr/core/src/java/org/apache/solr/util/CryptoKeys.java` lines 123 and 136. Package and filestore signature validation calls this helper. PKI uses the separate `verifySha256` helper.
- Category note: no category brief is present under `pool/guides/`; `assignments/pool-category-briefs-round-1.md` still has Slice B open. The work map and current code were read, and this missing brief is carried as a follow-up gap.

## Items

1. **Implement the SHA-512 package signature change.** Start from fresh `upstream/main` at `79b23df58daac14b92cc35f23b8b81f72098b4ba`. Change the shared package and filestore signature verifier to SHA512withRSA, update the regression tests and any test signatures that depend on the old algorithm, and add coverage that accepts SHA-512 and rejects SHA-1 for both byte-buffer and stream verification. Keep the separate SHA-256 PKI signing path unchanged. Output: pushed `solr-14649-submit` branch and `reports/SOLR-14649-sha512-implementation.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| SHA-1 compatibility | decided: reject SHA-1 signatures on the package and filestore verification path | none | Owner, from SOLR-14649 description and comments |
| PKI signatures | decided: leave the separate SHA256withRSA authentication path unchanged | none | Windows implementation assignment |
| Scope | default: keep the patch to shared package/filestore verification and its tests | if a required fixture or caller is outside this path, record the path and stop for the main agent | Windows implementation assignment |
| Proof | decided: request an exact-head Linux hardening gate after the branch is pushed | do not run Gradle or a local test queue on Windows | Workspace workflow |

## Deliverable

`reports/SOLR-14649-sha512-implementation.md`: code changes, branch head, changed paths, and the Linux phase-gate path and status.

## Limits

- Do not edit the Linux-only critical-slate assignment or claim its slice.
- Do not change package upload policy or SOLR-14593 behavior.
- No PR, Jira, or GitHub comments; no force push.
- Commit as Nick Shanin with no trailers. No em dashes.
