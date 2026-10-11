# Pool assignment: Highlighting post-PR review round 1 (SOLR-3704, SOLR-2681, SOLR-4540)

Capability tags: `review`. Staffing: up to 3 (one slice per ticket). Claim path: claims/pool-highlighting-post-pr-review-round-1.md (slice-level claims fine).

Background: Nick picked Highlighting as the next category to submit, following the same pattern as the flaky-fix PRs: the three PRs are open as drafts, and review rounds run until a round reports every slice satisfied; the main side then marks them ready. The drafts are in pr-drafts/highlighting/ and are live as draft PRs on apache/solr (the main side has the numbers; check the live body, the draft is its source). Heads: SOLR-3704 de63d4e5d5d1ddde0da6a100a631e254c078bd55, SOLR-2681 a3b1ea7994d932cdf78667f799542d7d49, SOLR-4540 180b6e8a7d3c23ac308a28d547f61816b5815f04. Receipts: receipts/SOLR-3704.md, receipts/SOLR-2681.md, receipts/SOLR-4540.md.

Per slice, check the live body against the branch head and the receipt:
- Title accuracy against the change.
- Every Proof number traceable to the receipt at the head; no seed values, run identifiers, or internal process vocabulary (gate, receipt, ledger, rc=0, "pre-fix proof" as a label) in the body.
- Every file citation is a blob link at the head SHA with the anchor holding the claimed code; symptom citations point at base or merge-base code, fix citations at the head.
- Each section opens with a bold one-line summary; Limits and Choice statements still true of the code at the head; any Lucene version mention names the other versions too.
- The answers file material/highlighting-round-1-answers.md decisions are reflected faithfully (for SOLR-2681, the nested query(...) form is a Limits line with a follow-up planned, and the changelog title claims only the top-level form).

Deliverable: reports/highlighting-post-pr-review-round-1.md with a verdict per slice (SATISFIED, or the exact remaining items with the fix the main side should apply). Read-only: no PR edits, no branch edits, no builds. Verify any automated or reviewer comment on the PRs against the code before reporting it as real.

Rules: WORKFLOW.md binds (claim before work, subagent cap of 6 to 7 across all hosts, mark the claim DONE in the same push as the deliverable).
