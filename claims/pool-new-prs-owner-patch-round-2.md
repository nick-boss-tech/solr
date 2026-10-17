# Claim: patches and live edits for the four draft PRs, round 2 (pr-prepare)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job none: none linked)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Direction: Nick, 2026-10-13: "You should do these patches, you can edit those PR descriptions and/or comments." Recorded in `memory/pr-review-patch-authority.md`.
- Findings applied: those in `reports/new-prs-review-round-1.md`.
- Staffing: three subagents, one per branch group. A: #5115 and #5116, text only. B: #5117, a code patch with its test and a new head for a gate. C: #5118, text plus a changelog title change on its branch.
- Work done here: PR body edits through the API, read back and compared; branch commits as Nick Shanin, no trailers, no em dashes, plain fast-forward pushes only; gate jobs under `gates/` for each new head; the drafts under `pr-drafts/` updated to match.
- Not done here: PR opening, closing, marking ready, rebasing #5118 onto `597ed677` (a force-push that needs a lease and a new gate, so it waits for Nick), and new comments on other people's PRs.
- Deliverable: `reports/new-prs-owner-patch-round-2.md`.
- Heartbeat: 2026-10-13T06:16Z (claim taken).
- Status: ACTIVE.
