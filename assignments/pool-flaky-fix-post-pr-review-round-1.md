# Pool assignment: post-PR review round for the flaky-fix PRs (SOLR-18530, SOLR-18531, SOLR-18532)

Capability tags: `review`. Staffing: up to 3 (one slice per ticket). Claim path: claims/pool-flaky-fix-post-pr-review-round-1.md (slice-level claims fine).

Background: the three flaky-fix branches were reviewed pre-opening in the flaky-fix review round 1 (reports/flaky-fix-review-round-1.md). Nick directed that the PRs be posted as they become available and then get another review round against the live PRs. This is that round. Slices activate per ticket as its PR opens; the main side records each PR number in this file as it opens (PR numbers live here and in the report only, per the coordination-content rule they stay out of commit messages).

Slices:

1. SOLR-18530: OPEN as a draft PR (apache/solr, opened 2026-10-10; head 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d, which is the gated head plus the changelog-removal commit). Verify the live PR body equals pr-drafts/flaky-fixes/SOLR-18530.md, the title is accurate, the head matches, and the CI checks' state; re-check the body's claims against the branch at that head.
2. SOLR-18531: not yet open. A fix lane is clearing the round 1 HOLD (F1 close-release, F2 Linux check, F4 disclosure, F10 changelog title) and re-gating. This slice activates when the main side records the opening here.
3. SOLR-18532: OPEN as a draft PR (apache/solr, opened 2026-10-10; head f1e5031fc3a9624b03daf353f26c977891d7d876, the gated tree squashed to one commit). Nick took the overlap call: this branch carries the rollback change, and the SOLR-9865 branch drops its overlapping hunk when it is prepared. This slice is active.

Per-slice work: read the live PR (body, title, head, checks, any reviewer comments including automated ones), compare the body line by line with the pr-drafts/flaky-fixes/ draft, verify every Proof statement against receipts/<TICKET>.md and the branch at the live head, and check that nothing in the round 1 report's open items was silently dropped. Automated reviewer findings are verified against the code before being reported as real. Deliverable: reports/flaky-fix-post-pr-review-round-1.md, one section per activated slice (verdict: CONSISTENT, or the exact drift or finding). Edits to PR bodies are NOT made under this assignment; drift is reported and the main side applies it.

Rules: WORKFLOW.md binds (claim before work, subagent cap across hosts, mark the claim DONE in the same push as the deliverable). No builds or test runs.
