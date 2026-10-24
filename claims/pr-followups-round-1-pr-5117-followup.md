# Claim: pr-followups-round-1-pr-5117-followup

- Claimant host: windows, matching `hosts/windows.md`.
- Status: DONE. Deliverables: `reports/pr-5117-followup-plan.md` and updated `assignments/pr-followups-round-1.md`.
- Job or assignment: `assignments/pr-followups-round-1.md`, the PR #5117 slice, revised at the owner's request.
- Slice: Replace the already-addressed draft-only correction with an evidence-based plan for the follow-up PR explicitly offered in PR #5117: research the `QueryComponent.mergeIds` partial-shard totals defect, its existing SOLR-18539 scope, adjacent combined-query behavior, and the remaining short-per-query-list failure. Deliver a report and update the assignment. No code change or public post.
- Capability tags used: review, records, draft.
- Started: 2026-10-24T07:16:17Z (UTC).
- Links: Parent ticket SOLR-18541; follow-up ticket SOLR-18539; related count ticket SOLR-18550; PR #5117. Supersedes: none.
- Constraints: read-only code/Jira/GitHub research; no Gradle or local test queue; no GitHub or Jira comments; no new ticket; board update only. Commit as Nick Shanin with no trailers.
- Decision ladder: rungs 1–3. The PR body names the offered change, SOLR-18539 already tracks the `QueryComponent.mergeIds` leak, and SOLR-18550 separately tracks exact union counts. Record any remaining choice as a scope boundary, not an owner question unless public intent is still ambiguous.

Result: SOLR-18539 is the matching follow-up scope. The report separates the `QueryComponent` totals leak from SOLR-18550's exact union count, the #5117 short-list timeout failure, and other adjacent `mergeIds` issues. The old rebase-reply draft is superseded by Nick's posted response. No code or public write was made.

DONE: 2026-10-24T07:39:52Z. Deliverables: `reports/pr-5117-followup-plan.md`, `assignments/pr-followups-round-1.md`.
