# Pool assignment: Highlighting post-PR review round 2, confirmation (SOLR-3704, SOLR-2681, SOLR-4540)

Capability tags: `review`. Staffing: up to 3 (one slice per ticket). Claim path: claims/pool-highlighting-post-pr-review-round-2.md (slice-level claims fine).

Background: round 1 (reports/highlighting-post-pr-review-round-1.md) left three items on SOLR-3704 (docValues wording, a missing verification date, an Extra runs formatting fix), one on SOLR-2681 (the verification date on the Proof line), and two on SOLR-4540 (a missing pass count, and a CI-run sentence that overstated the receipt). The main side has applied the fixes; where a date or count depends on the receipt, the receipt was updated first from the run record. This round is the confirmation pass: one verdict per slice, SATISFIED or STILL OPEN with the exact item. When all three are SATISFIED the main side marks the three draft PRs ready for review.

Slices (live draft PRs on apache/solr; drafts in pr-drafts/highlighting/):
1. SOLR-3704 at head de63d4e5d5d1ddde0da6a100a631e254c078bd55: the round 1 items as now written, plus a final whole-body read against the head and receipts/SOLR-3704.md.
2. SOLR-2681 at head a3b1ea7994d932cdf78667f799542d7d49: the round 1 item as now written, plus a final whole-body read against the head and receipts/SOLR-2681.md.
3. SOLR-4540 at head 180b6e8a7d3c23ac308a28d547f61816b5815f04: the round 1 items as now written, plus a final whole-body read against the head and receipts/SOLR-4540.md.

Deliverable: reports/highlighting-post-pr-review-round-2.md with the three verdicts. No edits under this assignment. Rules: WORKFLOW.md binds (claim before work, subagent cap across hosts, mark the claim DONE in the same push as the deliverable). No builds or test runs.
