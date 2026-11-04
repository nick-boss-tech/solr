# Report: PR 5287 configset repository upload hardening, round 1

Status: IMPLEMENTED, PROOF PENDING.

- Assignment: `assignments/pr-5287-configset-upload-hardening-round-1.md`.
- Claim: `claims/pr-5287-configset-upload-hardening-round-1-windows.md`. The claim record is retrospective because the first contents-API attempt timed out; the live snapshot had no competing claim, gate or receipt.
- PR: [apache/solr#5287](https://github.com/apache/solr/pull/5287), SOLR-18618.
- Contributor parent: `19f7c83812a4c81227601ff1abadca84beb05cb9`.
- Fork branch: `nick-boss-tech/solr:pr-5287-configset-source-validation`.
- Pushed head: `04e570d3426496aa8634ac03e556d45bc159af63`. A separate GitHub ref read returned this exact SHA.
- Pinned comparison: [contributor parent to patch](https://github.com/nick-boss-tech/solr/compare/19f7c83812a4c81227601ff1abadca84beb05cb9...04e570d3426496aa8634ac03e556d45bc159af63).
- Diffstat: 7 files changed, 162 insertions, 9 deletions.
- Gate: `gates/pr-5287-configset-upload-hardening-round-1.md`, OPEN, exact-head Linux proof pending.

## Changes

- Enforce body/repository exclusivity in V1 and V2 using stream reads rather than `InputStream.available()`.
- Reject symlink entries and validate canonical allowed paths only in the local repository-backed configset upload flow. This flow is newly introduced by PR 5287, so the check belongs in the patch; SOLR-12429 addresses a different upload path.
- Preserve dot-prefixed directories during repository imports.
- Add focused V1/V2, local directory symlink, single-file symlink and cross-provider dot-directory tests.

Changed files:

1. `solr/core/src/java/org/apache/solr/handler/admin/ConfigSetsHandler.java`
2. `solr/core/src/java/org/apache/solr/handler/configsets/ConfigSetAPIBase.java`
3. `solr/core/src/java/org/apache/solr/handler/configsets/UploadConfigSet.java`
4. `solr/core/src/test/org/apache/solr/cloud/LocalFSConfigSetUploadFromRepositoryTest.java`
5. `solr/core/src/test/org/apache/solr/handler/configsets/UploadConfigSetAPITest.java`
6. `solr/core/src/test/org/apache/solr/handler/configsets/UploadConfigSetFileAPITest.java`
7. `solr/test-framework/src/java/org/apache/solr/cloud/api/collections/AbstractConfigSetUploadFromRepositoryTest.java`

## Verification and remaining work

Static diff review and whitespace checks passed. Windows ran no Gradle, compilation or tests. The exact-head Linux gate must complete the focused suites, fail-before proof, tidy, Error Prone compilation and `check -x test`. The heap-buffering observation remains outside this patch. Do not treat the branch as proof-complete or offer a public comment until the gate receipt is green and the live PR head and discussion have been refreshed.

No change was made to Houston's PR branch, its body, JIRA or public comments. The gate report will distinguish the security-path fix from public wording; any later comment should describe the implemented safeguards and verified tests without repeating the private exploit analysis.
