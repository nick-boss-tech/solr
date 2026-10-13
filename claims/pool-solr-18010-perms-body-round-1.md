# Claim: SOLR-18010 description update for the permission fix, round 1 (pr-prepare)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Trigger: `receipts/SOLR-18010.md` at `2a5c815c191` records the permission re-gate GREEN at `2b5ad801c9e88807337f4f0a3fdd689126ad37d1` (4 of 4 SecurityConfHandlerTest, fail-before recorded). The PR description still describes the earlier head and lists owner-only permissions as a limit.
- Direction: Nick, 2026-10-13: the test and keeping the existing behavior were the decision, and the PR description may be updated.
- Staffing: two subagents. S1 checks every citation of the new head and drafts the replacement text from the receipt. S2 checks the formula, the voice, and the length of the result.
- Scope: `pr-drafts/core-admin/SOLR-18010.md` and the live PR description of the SOLR-18010 PR, read back and compared after the edit.
- Limits: no PR opened, closed, flipped, or commented on; no Jira text; no build, test, or Gradle on this host. Commit as Nick Shanin, no trailers, no em dashes.
- Heartbeat: 2026-10-13T15:06Z (claim taken).
- Status: DONE, 2026-10-13. Deliverable: `reports/solr-18010-perms-body-round-1.md`. The live body and the draft match, at head 2b5ad801c9e.
