# Pool assignment: Highlighting post-PR review round 3, confirmation (SOLR-3704, SOLR-2681, SOLR-4540)

Capability tags: `review`. Staffing: up to 3 (one slice per ticket). Claim path: claims/pool-highlighting-post-pr-review-round-3.md (slice-level claims fine).

Background: round 2 (reports/highlighting-post-pr-review-round-2.md) left three items on SOLR-3704, one on SOLR-2681, one on SOLR-4540. The main side has applied them. The citation tension is settled by a main-side rule decision, recorded in pr-formula.md: citations to code the change produces link the PR head SHA; citations to the pre-change symptom link the merge-base commit and the text says so. This round is the confirmation pass: one verdict per slice, SATISFIED or STILL OPEN with the exact item. When all three are SATISFIED the main side marks the three draft PRs ready for review.

Slices (live draft PRs on apache/solr; drafts in pr-drafts/highlighting/):
1. SOLR-3704 at head de63d4e5d5d1ddde0da6a100a631e254c078bd55: the round 2 items as now written (the corrected fetch condition and the same fix in the answers file, symptom citations at the merge-base cabedd1d968059215188f4e7563fb303241899ed saying so, Extra runs lines carrying the verification date), plus a final whole-body read against the head and receipts/SOLR-3704.md.
2. SOLR-2681 at head a3b1ea7994d932cdf78667f799542d7d49: the changelog line as a link, plus a final whole-body read against the head and receipts/SOLR-2681.md.
3. SOLR-4540 at head 180b6e8a7d3c23ac308a28d547f61816b5815f04: symptom links at the base commit saying so, plus a final whole-body read against the head and receipts/SOLR-4540.md.

Deliverable: reports/highlighting-post-pr-review-round-3.md with the three verdicts. No edits under this assignment. Rules: WORKFLOW.md binds (claim before work, subagent cap across hosts, mark the claim DONE in the same push as the deliverable). No builds or test runs.
