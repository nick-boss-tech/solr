# Claim: scheduled check round for the pr-prepare tip a9a8c85d074 (round 1)

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review, draft
- Trigger: the scheduled check found pr-prepare at a9a8c85d074, recorded tip 4c68e65. The new commits are gate records: vm1's verification of the SOLR-10667 and SOLR-12347 premise retries (both marked DONE, GATE GREEN), and vm2's SOLR-12505 round 3 investigation and fork push to `1a1875d95f` (a test fixture fix; the full hardening gate is not yet recorded). No new assignment or material file.
- Scope: check the drafts those records touch against the named heads and records. `pr-drafts/cli/SOLR-10667.md` and `pr-drafts/cli/SOLR-12347.md` against their gate files and claims. `pr-drafts/streaming/SOLR-12505.md` against the new head `1a1875d95f` and the test it describes; the verdict waits for the full gate. Drafts only.
- Staffing: two subagents. S1 takes the two cli drafts. S2 takes the SOLR-12505 draft.
- Limits: no PR opened, closed, flipped or commented on; no live PR body edited this round; no fork branch changed; no build, test or Gradle on this host; nothing posted.
- Heartbeat: 2026-10-13T16:36Z (claim taken).
- Status: DONE 2026-10-13T17:05Z. Deliverable: `reports/cli-and-streaming-recheck-round-1.md`. Verdicts: SOLR-10667 NOT SATISFIED, SOLR-12347 NOT SATISFIED, SOLR-12505 NOT SATISFIED until the full gate at `1a1875d95f` lands. Draft edits applied to the two cli drafts; the SOLR-12505 live draft is unchanged, and its candidate revision is a separate staging file. No live body edited this round.
