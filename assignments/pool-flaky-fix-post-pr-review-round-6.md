# Pool assignment: post-PR review round 6, final confirmation (SOLR-18530, SOLR-18531, SOLR-18532)

Capability tags: `review`. Staffing: up to 3 (one slice per ticket). Claim path: claims/pool-flaky-fix-post-pr-review-round-6.md (slice-level claims fine).

Background: round 5 (reports/flaky-fix-post-pr-review-round-5.md) left one item on SOLR-18530, three on SOLR-18531, two on SOLR-18532. The main side has applied all of them, plus two Limits lines on SOLR-18531 for the failed-start and shared-port-map edges (round 5 non-blocking notes, verified against the head code). This round is the final confirmation: one verdict per slice, SATISFIED or STILL OPEN with the exact item. When all three are SATISFIED the main side marks the three draft PRs ready for review.

Slices:
1. SOLR-18530 at head 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d: the round 5 Limits sentence as now written, plus a final whole-body read against the head and receipts/SOLR-18530.md.
2. SOLR-18531 at head c8a67c7267d00e33ad8323c683c040b539989b0d: the round 5 items as now written ("on Linux" on the bind claim, "still in the cluster" on the summary, the changelog citation as a head-SHA link), the two added Limits lines against the code, gates/SOLR-18531.md status matching the receipt, plus a final whole-body read against the head and receipts/SOLR-18531.md.
3. SOLR-18532 at head 7dfd3d98d0a2f016c520139cbca96ea4a49f6689: the round 5 items as now written (the duplicated paragraph deleted, the install-tests bullet ending without the causal clause), plus a final whole-body read against the head and receipts/SOLR-18532.md.

Deliverable: reports/flaky-fix-post-pr-review-round-6.md with the three verdicts. No edits under this assignment. Rules: WORKFLOW.md binds (claim before work, subagent cap across hosts, mark the claim DONE in the same push as the deliverable). No builds or test runs.
