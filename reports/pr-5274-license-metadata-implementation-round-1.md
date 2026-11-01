# Implementation: dependency license metadata for PR 5274

Status: Implemented, Proof Pending.

## Identity and pushed head

- Direct authorization: Nick requested the necessary fixes on fork branches followed by gate assignments on 2026-11-01.
- Implementation claim: claims/pr-5274-license-metadata-windows-round-1.md.
- Public PR: https://github.com/apache/solr/pull/5274. Contributor head/repair parent P: 3f827a780a292feddeb58077dc531fc3c0086d0c. Live PR head was separately refreshed and remains P.
- Fork: https://github.com/nick-boss-tech/solr.git; branch: pr-5274-jcl-license-metadata.
- Exact pushed repair H: e3bcf3af6fcb544c624efdcb4329ce5ef5a271f3. Separate GitHub ref API read returned H after the ordinary push. One Nick Shanin <nick.boss.us@gmail.com> commit on P; contributor ancestry retained. No attribution trailer.
- Scoped diff: https://github.com/nick-boss-tech/solr/compare/3f827a780a292feddeb58077dc531fc3c0086d0c...e3bcf3af6fcb544c624efdcb4329ce5ef5a271f3.
- Linux job: gates/pr-5274-license-metadata-round-1.md; receipt owned by executing host: receipts/PR-5274-license-metadata.md.

## Patch and provenance

The committed JCL Apache 2.0 license exactly matches META-INF/LICENSE.txt from jcl-over-slf4j 2.0.20 after CRLF-to-LF normalization and the tagged module https://raw.githubusercontent.com/qos-ch/slf4j/v_2.0.20/jcl-over-slf4j/LICENSE.txt. SHA-256: 59899c6091b540582ed617e8eeaac4919dc985ccfc35459ee9752b699be5205b. Deleted the stale MIT license filename and cleared the stale MIT notice contents. The exact artifact and tagged module have no NOTICE, so the empty required companion follows existing Solr conventions. SLF4J API and JUL bridge MIT metadata are unchanged.

Exact outbound files (the MIT path is deleted on JCL branches):

- solr/licenses/jcl-over-slf4j-LICENSE-ASL.txt
- solr/licenses/jcl-over-slf4j-LICENSE-MIT.txt
- solr/licenses/jcl-over-slf4j-NOTICE.txt

No production/test source, build logic, version constraint, dependency lock, checksum, changelog or upstream AGENTS file changed. No new helper/fixture/API is needed for license metadata. The existing license validator and focused consumer suites supply the proof scope. The branch_9x source AGENTS instructions were read; its Gradle tasks belong to the assigned Linux host under the Windows override.

## Static validation and limits

The pushed commit has the exact parent above, exactly the outbound file list, a clean worktree and clean git diff --check. All new text files were read back and compared with their stated upstream/artifact text, with line endings normalized only. The two JCL repair diffs on the PR 5274 and 5278 parents are identical. Windows worktree checkout mode does not become an outbound file. No Gradle, compilation, formatting task or tests ran on Windows.

Original green CI only accepted an existing recognized license filename and did not validate its contents against the upstream artifact. The Linux job requires that manual content/classification audit as well as the ordinary checks.

## Related scope and handoff

This new job supersedes gates/pr-5274-extraction-docker-round-1.md for current repaired-head acceptance and carries forward all five real Docker Tika test requirements. No live Linux claim or receipt for the old job was found at the prepublication board snapshot. Leave the old immutable file untouched; do not count any P result as H proof. The bot has a 2026-11-01 Edited/Blocked notification on the public PR, but the live head remains P; no checkbox or contributor branch was changed.

A job posted to coordination-ui is available to a capable Linux host, not proof that it has been claimed or executed. No public comment, approval, PR opening, contributor push or merge is part of this implementation.
