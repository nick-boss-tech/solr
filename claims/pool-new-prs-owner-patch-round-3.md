# Claim: branch patches for the draft PRs, round 3 (pr-prepare)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job none: none linked)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Direction: Nick, 2026-10-13: do the patches; git actions are allowed.
- Work in this round:
  - SOLR-18541 (#5117): finding C1, a code patch on `combinedquerycomponent-merge-totals` at `d5ed34125db9582b584bbfdd149a4de4b40650d0`. A skipped shard clears the exactness flag. A focused test, a changelog title, a plain push, and a gate job for vm2 at the new head.
  - SOLR-18010 (#5118): finding W5, the changelog title on `solr-18010-submit` at `c3685bb37d9d74e4ed538d6d1b65bb1b60970677`. A plain push and a gate job for the YAML parse at the new head.
- Staffing: two subagents, one per branch. Each works in its own detached worktree, commits as Nick Shanin with no trailers and no em dashes, and pushes with `research\push-fork-ff.ps1`, which never forces.
- Not in this round: rebasing #5118 onto `597ed677`, opening or closing PRs, marking PRs ready, and new comments.
- Deliverable: `reports/new-prs-owner-patch-round-3.md`.
- Heartbeat: 2026-10-13T14:00Z (claim taken).
- Status: ACTIVE.
