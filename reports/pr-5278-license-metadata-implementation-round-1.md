# Implementation: dependency license metadata for PR 5278

Status: Implemented, Proof Pending.

## Identity and pushed head

- Direct authorization: Nick requested the necessary fixes on fork branches followed by gate assignments on 2026-11-01.
- Implementation claim: claims/pr-5278-license-metadata-windows-round-1.md.
- Public PR: https://github.com/apache/solr/pull/5278. Contributor head/repair parent P: 82f60b1b7976c187541a6f6808a781bb7f8b9228. Live PR head was separately refreshed and remains P.
- Fork: https://github.com/nick-boss-tech/solr.git; branch: pr-5278-jcl-license-metadata.
- Exact pushed repair H: cb8c6ab2752b5202b52c7479b9b77ee7806b350e. Separate GitHub ref API read returned H after the ordinary push. One Nick Shanin <nick.boss.us@gmail.com> commit on P; contributor ancestry retained. No attribution trailer.
- Scoped diff: https://github.com/nick-boss-tech/solr/compare/82f60b1b7976c187541a6f6808a781bb7f8b9228...cb8c6ab2752b5202b52c7479b9b77ee7806b350e.
- Linux job: gates/pr-5278-license-metadata-round-1.md; receipt owned by executing host: receipts/PR-5278-license-metadata.md.

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

The same JCL correction also exists separately on pr-5274-jcl-license-metadata. These are sibling PR parents, not a stack. A gate on one head does not prove the other; after merge order is known, reconcile bot metadata and rerun at the resulting head.

A job posted to coordination-ui is available to a capable Linux host, not proof that it has been claimed or executed. No public comment, approval, PR opening, contributor push or merge is part of this implementation.
