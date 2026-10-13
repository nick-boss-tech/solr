# Claim: strong review of the four new draft PRs, round 1 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-four-new-prs-strong-review-round-1.md`. Under the standing coordinator claim, `claims/coordinator-review-rounds.md`.
- Staffing: none, the review was read inline by one agent.
- Scope: read the diffs at the stated heads, audit tests, verify body numbers against receipts, write patches for code findings and post gate jobs for the Linux hosts. No build, no Gradle, no new GitHub comment, no JIRA write. Wording fixes go to the live PR bodies and the matching drafts.
- Checked at claim time: pr-prepare tip `45b1f4c9492`. Live heads: PR #5115 `4eb996121804`.
- Deliverable: `reports/four-new-prs-strong-review-round-1.md`.
- Heartbeat: 2026-10-13T15:59Z (claim taken).
- Status: DONE, round 1, 2026-10-13. Report: `reports/four-new-prs-strong-review-round-1.md`. Verdicts: #5116 SATISFIED; #5115, #5117, #5118 NOT SATISFIED with the blocking items named in the report. Patches on `review-r1-solr-18539`, `review-r1-solr-18541`, `review-r1-solr-18010`, gate jobs `gates/SOLR-18539-review-r1-patch.md`, `gates/SOLR-18541-review-r1-patch.md`, `gates/SOLR-18010-review-r1-patch.md`, settling runs `gates/queryComponent-real-exit-shape-premise.md` and `gates/SOLR-18010-replace-semantics-premise.md`. Live bodies of the four PRs and the four drafts in `pr-drafts/` were edited for wording and read back. The loop continues in `assignments/pool-four-new-prs-strong-review-round-2.md`.
