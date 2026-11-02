# Draft comment: PR 5276 license metadata (round 1)

Status: DRAFT. Not posted. Awaiting Nick's per-text go.
Pinned identities (verified live 2026-11-02): PR head P a6d008eb38d508b0179c71bd6a862e2a7b40fb24
(branch renovate-9x/grpc-and-netty); repair head H 5f9613e13865b73b992d8321142e61ceddee2b92
(fork branch pr-5276-license-metadata, one commit on P).

## Comment text

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Prepared fix: [compare this PR's head with the repair branch](https://github.com/nick-boss-tech/solr/compare/a6d008eb38d508b0179c71bd6a862e2a7b40fb24...5f9613e13865b73b992d8321142e61ceddee2b92)

License metadata is missing for four artifacts this PR pulls in, and the PR's own precommit run shows it: `:solr:modules:gcs-repository:validateJarLicenses` [fails](https://github.com/apache/solr/actions/runs/41268286728) for `dev.cel:common`, `dev.cel:protobuf` and `dev.cel:runtime` 0.13.1 and for `org.threeten:threeten-extra` 1.8.0. Their checksums are in the repo; the license files are not. The published POMs declare Apache 2.0 for the three CEL artifacts and BSD 3-clause for ThreeTen Extra. The branch above adds exactly those license and notice files, nothing else.

<details>
<summary>Verification and limits</summary>

The license texts come from the exact upstream sources. The CEL license is the LICENSE file from google/cel-java v0.13.1 (SHA-256 cfc7749b96f63bd31c3c42b5c471bf756814053e847c10f3eb003417bc523d30, same file for all three artifacts); the ThreeTen Extra license is META-INF/LICENSE.txt from its Maven Central JAR (SHA-256 decba4464d41e505481403488af67078ce7078eacb164ccf78146c2d12e1206e). Neither artifact supplies a NOTICE, so the notice companions are empty, following the existing api-common, gson and threetenbp convention in solr/licenses. This is a static provenance audit: the texts were compared against the upstream sources and the hashes recorded, but no Gradle run has been executed at the repair head yet. The failing precommit run linked above, at this PR's current head, is the before evidence.

</details>

The repair is one commit on top of this PR's head, so it fast-forwards:

```
git switch renovate-9x/grpc-and-netty
git fetch https://github.com/nick-boss-tech/solr.git pr-5276-license-metadata
git merge --ff-only 5f9613e13865b73b992d8321142e61ceddee2b92
```

You're welcome to take the commits or adapt the changes.
