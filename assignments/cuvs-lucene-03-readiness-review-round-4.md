# Assignment: review the cuvs-lucene contribution for submission, round 4

Status: HANDED (waiting for steps 1 and 2)

- Intended host: a review-capable host; use a reviewer who did not prepare the consolidated patch/draft when available. A prior Linux proof is not replaced by this review.
- Capability tags: review, records.
- Claim: `claims/cuvs-lucene-03-readiness-review-round-4-<host>.md`.
- Dependencies: the exact published outputs of assignments 01 and 02.
- Context: `material/cuvs-lucene-next-steps-round-4.md`.

## Objective

Give Nick a concrete READY or CHANGES NEEDED verdict on the exact proposed patch and first-contact text. This is a source/evidence/public-text review, not a fresh test gate or approval to publish.

## Work

1. Record the reviewed board snapshot, upstream base/head observed, combined patch digest, four resulting test blobs, submission-manifest identity, draft digests and both receipt commits. Read actual source and controls; do not rely on the authors' summaries alone. Apply the patch in an isolated directory if needed to confirm reconstruction.
2. Check the tests against behavior: direct approximate-query forwarding must remain exercised so exact-search fallback cannot conceal a dropped mask. Filtering, deletion and intersection assertions must retain their required outcomes on 10.2 and 10.4 controls. In archived-index coverage, verify live-document handling, sparse vector ordinals, exact ID/value association, the fixed fixture data contract, and before/after merge assertions. Identify unmutated behavior pins honestly.
3. Verify that each changed file is byte-identical to the version covered by its receipt, or that a new matching receipt exists for every changed input. Inspect the underlying compile, linkage and JUnit evidence enough to verify counts and failure shapes. Confirm the round-3 stdout-derived matrix agrees with its source log; note its reduced metadata rather than inventing another run.
4. Check scope and attribution: only four test files, no duplicate production fix, no native/API stubs presented as official artifacts, no build/runtime installation or proof material in the outbound patch. Verify source history/current PR discussion does not already make the offer obsolete.
5. Review the proposed public text under comment-formula.md: correct author/recipient, evidence proportional to claims, clear CPU limits, proper AI disclosure, no premature Nick-review claim, no date promise and no implicit demand for review work. Verify any proposed URL/attachment exists and exposes only reviewed material.
6. Produce actionable findings with file/line or draft passage, priority and remedy. Fixing typos may be handled in a new draft revision without a code gate. Source edits or a changed base require a new package identity and affected CPU proof, not a quiet amendment to the reviewed input.

## Deliverable and acceptance

Add `reports/cuvs-lucene-readiness-review-round-4.md` with an identity table, code assessment, proof mapping, description findings and disposition. READY means no unresolved source/proof/text issue remains within the CPU scope. List GPU/native/release/Solr limits as limits, not as invented CPU failures. If CHANGES NEEDED, name a bounded repair and its owner/next record; do not repeatedly rerun unchanged tests.

Finish by presenting the exact first-contact text and package to Nick for review. Only then is step 4 actionable. Nick's approval must bind the target and actual content; this agent's READY verdict is not approval to post.

## Limits

No Windows Gradle or local queue, no build/test sweep, no upstream review approval/merge, no PR/comment/body/Jira write, no source push. Publish the review as a new board file and verify it; do not rewrite old evidence or shared indexes.
