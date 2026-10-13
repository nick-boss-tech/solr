# Claim: strong review of the four new draft PRs, round 1 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: `assignments/pool-four-new-prs-strong-review-round-1.md`. Under the standing coordinator claim, `claims/coordinator-review-rounds.md`.
- Staffing: none, the review was read inline by one agent.
- Scope: read the diffs at the stated heads, audit tests, verify body numbers against receipts, write patches for code findings and post gate jobs for the Linux hosts. No build, no Gradle, no new GitHub comment, no JIRA write. Wording fixes go to the live PR bodies and the matching drafts.
- Checked at claim time: pr-prepare tip `45b1f4c9492`. Live heads: PR #5115 `4eb996121804`.
- Heartbeat: 2026-10-13T15:59Z (claim taken).
- Stand-down note, 2026-10-13T17:00Z: the claim `claims/pool-pr-prepare-tip-39efc73-round-1.md` for the same deliverable was pushed at `46ff7895f10`, before this one, and its report landed first at `4d7c0de40de`. Under first-push-wins this claim stands down for `reports/four-new-prs-strong-review-round-1.md`. It was not seen at claim time because the claims folder was not read first.
- Status: DONE as an independent second review, 2026-10-13. Deliverable: `reports/four-new-prs-strong-review-round-1-windows-addendum.md`, which also lists where it differs from the canonical report. Added work: patches on `review-r1-solr-18539`, `review-r1-solr-18541`, `review-r1-solr-18010`; gate jobs `gates/SOLR-18539-review-r1-patch.md`, `gates/SOLR-18541-review-r1-patch.md`, `gates/SOLR-18010-review-r1-patch.md`; settling runs `gates/queryComponent-real-exit-shape-premise.md` and `gates/SOLR-18010-replace-semantics-premise.md`; the four live PR bodies edited for wording and read back; round 2 posted as `assignments/pool-four-new-prs-strong-review-round-2.md`.
