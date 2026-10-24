# Assignment: assess and implement released cuvs-lucene adoption in Solr, round 4

Status: HANDED (waiting for a compatible published artifact and Nick's Solr activation)

- Intended host: implementation host for source work, Linux gate host for CPU build/formatting proof.
- Capability tags: implementation, review, records. Actual test execution requires the gate tag and a concrete job.
- Claim: `claims/cuvs-lucene-08-solr-adoption-round-4-<host>.md` after the prerequisites below are evidenced.
- Dependencies: step 6's released-artifact assessment, current SOLR-18556 ownership, and Nick's explicit activation of the selected Solr scope. This planning assignment does not select a release, authorize a speculative pin, or approve an upstream Solr PR.

## Readiness and assessment

1. Resolve the current ticket receipt/claims and selected Solr target branch first. Verify the release coordinates, digests, exact Lucene/JDK/native requirements, source tag and available GPU evidence from step 6. Recheck live primary artifacts because release state can change. Do not choose an artifact by version string alone.
2. Read current CuVSCodec, integration tests, module dependencies, versions catalog, lock/verification metadata, relevant docs and changelog conventions at a fixed Solr revision. Compare all used cuvs-lucene APIs and codec/vector-format SPI names with the published artifact. The multi-version packaging/API may require more than a pin; enumerate each required change with file/line and behavior.
3. If the required scope is larger than Nick activated, finish the assessment with the concrete proposed migration and its proof needs before asking to widen it. Do not hide an API migration inside a version bump. Confirm the target Lucene version actually has a corresponding supported module/artifact.

## Implementation after activation

4. Use the exact ticket fork branch in an isolated worktree, preserving unrelated edits. Make only the selected dependency/API integration changes, necessary dependency metadata, focused tests and accurate docs/changelog. Changelog author is Nick Shanin by name only. No compatibility guard or swallowing linkage errors unless separately selected by Nick.
5. Review and hand-format the diff on Windows. Do not run Gradle or the retired local queue. Commit and push the actual Solr patch to the ticket fork branch in the same session with the ICLA identity and no trailers; verify the remote SHA and any public PR head. No push to apache/solr, force-push to an open PR, public PR/comment or live-body edit without the relevant direction.
6. Publish concrete CPU and GPU gate inputs on coordination-ui for that exact pushed SHA: target branch, artifact/digests, source/JDK/native requirements, discovered module/test tasks, formatting tasks, focused classes, seed, log/receipt paths and non-skipping acceptance. The CPU gate may establish compilation/formatting; it cannot establish GPU behavior. Do not leave placeholder heads or guessed Gradle task paths.
7. If a gate is already claimed, preserve its input. A code/test/setup change is a new input with a replacement job after reconciling that claim. A formatting repair must be committed, pushed and covered under the final identity. Label the patch Implemented, Proof Pending until the required legs pass.

## Deliverables and acceptance

Add `reports/solr-18556-adoption-round-4.md` with assessment, activated scope, artifact matrix, outbound files, exact fork SHA and proof status. Publish concrete gate job files and testing instructions once source exists. A valid implementation result is a verified pushed fork head and executable exact-input jobs; a version-only suggestion or a GPU-skipped test is insufficient.

Step 9 consumes the final pushed head and matching CPU result. It must be possible for a GPU host to reproduce the original indexing scenario using the released artifacts. Do not call the ticket fixed or draft a stronger public claim until that leg completes.

## Limits

No unpublished NVIDIA snapshot substituted for the release, no GPU proof on a CPU host, no Windows Gradle, no speculative release selection, and no public submission from this assignment alone. If no GPU host exists, keep the implementation proof pending with the exact missing capability identified.
