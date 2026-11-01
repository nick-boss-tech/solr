# Implementation: dependency license metadata for PR 5276

Status: Implemented, Proof Pending.

## Identity and pushed head

- Direct authorization: Nick requested the necessary fixes on fork branches followed by gate assignments on 2026-11-01.
- Implementation claim: claims/pr-5276-license-metadata-windows-round-1.md.
- Public PR: https://github.com/apache/solr/pull/5276. Contributor head/repair parent P: a6d008eb38d508b0179c71bd6a862e2a7b40fb24. Live PR head was separately refreshed and remains P.
- Fork: https://github.com/nick-boss-tech/solr.git; branch: pr-5276-license-metadata.
- Exact pushed repair H: 5f9613e13865b73b992d8321142e61ceddee2b92. Separate GitHub ref API read returned H after the ordinary push. One Nick Shanin <nick.boss.us@gmail.com> commit on P; contributor ancestry retained. No attribution trailer.
- Scoped diff: https://github.com/nick-boss-tech/solr/compare/a6d008eb38d508b0179c71bd6a862e2a7b40fb24...5f9613e13865b73b992d8321142e61ceddee2b92.
- Linux job: gates/pr-5276-license-metadata-round-1.md; receipt owned by executing host: receipts/PR-5276-license-metadata.md.

## Patch and provenance

CEL common/protobuf/runtime 0.13.1: exact https://raw.githubusercontent.com/google/cel-java/v0.13.1/LICENSE normalized to LF. Each committed license SHA-256: cfc7749b96f63bd31c3c42b5c471bf756814053e847c10f3eb003417bc523d30. ThreeTen Extra 1.8.0: exact META-INF/LICENSE.txt from its Maven Central JAR normalized to LF, retaining its copyright and BSD terms; committed SHA-256 decba4464d41e505481403488af67078ce7078eacb164ccf78146c2d12e1206e. Neither the CEL JARs/pinned root nor the ThreeTen JAR supplies a NOTICE; the empty companion files follow existing api-common, gson and threetenbp conventions. Existing four JAR checksums remain unchanged.

Exact outbound files (the MIT path is deleted on JCL branches):

- solr/licenses/common-LICENSE-ASL.txt
- solr/licenses/common-NOTICE.txt
- solr/licenses/protobuf-LICENSE-ASL.txt
- solr/licenses/protobuf-NOTICE.txt
- solr/licenses/runtime-LICENSE-ASL.txt
- solr/licenses/runtime-NOTICE.txt
- solr/licenses/threeten-extra-LICENSE-BSD.txt
- solr/licenses/threeten-extra-NOTICE.txt

No production/test source, build logic, version constraint, dependency lock, checksum, changelog or upstream AGENTS file changed. No new helper/fixture/API is needed for license metadata. The existing license validator and focused consumer suites supply the proof scope. The branch_9x source AGENTS instructions were read; its Gradle tasks belong to the assigned Linux host under the Windows override.

## Static validation and limits

The pushed commit has the exact parent above, exactly the outbound file list, a clean worktree and clean git diff --check. All new text files were read back and compared with their stated upstream/artifact text, with line endings normalized only. The two JCL repair diffs on the PR 5274 and 5278 parents are identical. Windows worktree checkout mode does not become an outbound file. No Gradle, compilation, formatting task or tests ran on Windows.

Original precommit run https://github.com/apache/solr/actions/runs/41268286728 at P documents the four missing licenses. That is existing build failure evidence, not a runtime fail-before test. The new head has no executed Linux receipt yet.

## Related scope and handoff

Keep this repair separate from the JCL sibling branches; it preserves the gRPC/Netty PR parent and its dependency choices.

A job posted to coordination-ui is available to a capable Linux host, not proof that it has been claimed or executed. No public comment, approval, PR opening, contributor push or merge is part of this implementation.
