# Pool assignment: post-PR review round 2 for the flaky-fix PRs (SOLR-18530, SOLR-18531, SOLR-18532)

Capability tags: `review`. Staffing: up to 3 (one slice per ticket). Claim path: claims/pool-flaky-fix-post-pr-review-round-2.md (slice-level claims fine).

Background: round 1 of the post-PR review (reports/flaky-fix-post-pr-review-round-1.md) found drifts on all three PRs; the main side applied the fixes to the live bodies, drafts and receipts. Nick directed a second round, and once this round is satisfied the main side marks the three draft PRs ready for review. This round is the satisfaction check: verify the round 1 findings are actually cleared in the live PRs, and re-check the bodies against the heads and receipts one more time.

Slices:
1. SOLR-18530 (draft PR, head 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d): verify round 1 items 1 to 4 in the live body (links at the head SHA, routing wording and title no longer claim leaders-only, the "forward" sentence reworded, changelog exemption line present), and that receipts/SOLR-18530.md names the head the gate runs used.
2. SOLR-18531 (draft PR): new head 351914f52c99180f0582d45c5bea1bd800194d29 (the F2 traffic-before-stop variant committed as a real test in TestJettySolrRunner). Its re-gate is running on vm1 (log g18531-regate2.log; proof leg already OK: both reservation tests fail on the reverted framework). This slice activates when receipts/SOLR-18531.md records GATE GREEN at 351914f52c; the main side updates the PR body Proof at the same time. Verify the round 1 slice 2 items D1 to D4 and R1 to R2 in the live body and receipt, that the Proof text cites the committed test with the re-gate's counts, and the F2 Limits line matches the code.
3. SOLR-18532 (draft PR, head f1e5031fc3a9624b03daf353f26c977891d7d876): verify the round 1 slice 3 items in the live body (causal sentence, temp-file Limit, non-atomic fallback naming, failure-wait Limit, head-SHA file links, Choice question, Limits summary line).

Per-slice deliverable: a section in reports/flaky-fix-post-pr-review-round-2.md with verdict SATISFIED or the exact remaining item. Also re-check, per slice: title accuracy, every Proof number against the receipt, and any new reviewer or automated comments on the PR (verified against the code before being reported as real). Edits are NOT made under this assignment; anything remaining goes in the report for the main side.

Rules: WORKFLOW.md binds (claim before work, subagent cap across hosts, mark the claim DONE in the same push as the deliverable). No builds or test runs.
