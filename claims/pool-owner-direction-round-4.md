# Claim: owner directions on the draft PRs, round 4 (pr-prepare)

Status: CLOSED 2026-10-17 (batch close approved by Nick; no heartbeat within 2 hours; listed in reports/claims-stale-batch-close-2026-10-17.md)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Direction: Nick, 2026-10-13:
  1. SOLR-18541 (#5117): push the fix. Leave the PR body alone. The PR head name cannot change; future branches should use the solr-NNNNN naming the wrapper accepts.
  2. SOLR-16630 (#5106): patch it for the maintainer's latches request.
  3. #5065 (the offer): keep as a draft. Update the local draft only, not the posted comment on GitHub.
  4. SOLR-18010 (#5118): check whether a rebase onto 597ed677 is needed. Decide the Windows rename risk and the permission question: probably a test, and keep the existing behavior.
  5. SOLR-17680: keep as is.
  6. SOLR-13369: leave for discussion.
- Staffing: two subagents. A does the SOLR-16630 latch patch. B checks the SOLR-18010 rebase question and prepares the SOLR-18010 test and permission patch.
- Limits: no PR opened, closed, marked ready, or rebased. No new comment on any PR. No force-push. Plain fast-forward pushes only. Gate jobs for each new head. Commit as Nick Shanin, no trailers, no em dashes.
- Heartbeat: 2026-10-13T14:24Z (claim taken).
- Status: ACTIVE.
