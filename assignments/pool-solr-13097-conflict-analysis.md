# Pool assignment: SOLR-13097 merge conflict analysis (read-only)

Status: DONE

Capability tags: `review`. Intended hosts: windows or vm3. Staffing: 1. Claim path: claims/pool-solr-13097-conflict-analysis.md.

Background: the SOLR-13097 branch (solr-13097-submit) has a live PR that is open with changes requested by Jan Høydahl and a merge conflict against main. The Core admin round 1 answers record (material/core-admin-round-1-answers.md, the SOLR-13097 entry) is the starting record: the branch was gated green at an older head, the gate's fix is also present in the live history under a different commit, the live tip carries a test class with more test methods than the gate ran, and one Limits claim in the live body (the blockUnknown=false statement) was already marked for softening or removal until it is verified. Whether and when this branch gets rebased and re-gated is an owner call recorded elsewhere. This analysis informs that call; it does not unblock the branch by itself, and no part of it authorizes a rebase.

## Scope

1. Fetch apache/solr main and the solr-13097-submit branch. Compute the merge-base and record it, the live branch tip, and the current main tip, each with dates.
2. Compute the conflict surface without attempting a rebase: use a trial merge or merge-tree computation in a scratch clone or scratch worktree that you discard afterwards, and list every file and hunk that conflicts. For each conflicted hunk, state why it conflicts in terms of what changed on main since the merge-base (the main-side commit or commits that touched those lines, and what they did), not just that both sides edited nearby lines.
3. List what a rebase must preserve: the changes already in the branch that answer the reviewer's requests (per the Core admin answers record and the branch history), the gate fix present in the live history, and the added test methods the gate never ran. For each item, name the file and the branch commit that carries it, so a later rebase can be checked against this list.
4. Flag any claim in the live PR body that a rebase would invalidate or that is already unsupported at the live tip: test counts that exceed what any receipt records, Limits or Proof statements tied to the old merge-base or the old head, and citation links whose targets a rebase would move. Quote each claim and state what a corrected body would need after a rebase; do not write the corrected body.

## Deliverable and limits

Deliverable: `reports/solr-13097-conflict-analysis.md`, with the three tips and merge-base up front, the conflict surface by file and hunk, the preserve list, and the body-claim flags.

This assignment is read-only apart from the claim and the report, plus any scratch clone or worktree used for the trial merge, which must be removed when the work is done. Do not edit the branch, do not attempt the rebase on the real branch, do not push anything to the branch ref, do not edit the live PR body, do not post a comment, and do not run a build, test, or gate.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
