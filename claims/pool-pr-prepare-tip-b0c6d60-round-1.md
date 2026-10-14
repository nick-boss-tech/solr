# Claim: scheduled check round for the pr-prepare tip b0c6d60d7b9 (round 1)

- Claimed by: windows host (scheduled pr-prepare check), Claude Code on this host, working for Nick Shanin. Host file: hosts/windows.md.
- Capability tags: draft, review.
- Trigger: the scheduled check found the tip move from 62392e23e18 to b0c6d60d7b9 (2026-10-14). No new assignment file landed in that range. The new material is the gate results and claims (vm1 gates GREEN for 6831, 8088 and 9864; vm2 gates GREEN for 15906 and 16725; 5394 green at a local head only, fork unchanged; 14919 proof leg returned rc 1; 8628 packaging queued), the 17055 implementation-choice report, the 10305 history rewrite, and receipts refreshed for 17 tickets.
- Taken: a draft fidelity check against receipts and gate files for every ticket whose receipt, gate file or draft changed in that range: 6831, 8088, 9864, 5394; 10305, 10424, 10492, 8051; 6759, 6975, 7390, 7498; 8003, 9396, 9595, 17055; 15906, 16725, 14919.
- Staffing: five subagents, one per group of about four tickets. Under the six-at-once cap.
- Decision rule: each draft ends as SYNC (already matches the receipt at the live fork head), REFRESH (numbers, heads and links brought to the receipt at the live fork head, wording per pr-formula.md), or HOLD (live fork head differs from the receipt head, the gate is not green, or a decision is pending with Nick). Each decision is recorded in reports/pr-prepare-tip-b0c6d60-round-1.md.
- Limits: no PR opened, closed, flipped or commented; no live PR body edits; no fork pushes; no builds, tests or Gradle on this host; no JIRA writes. The owed live PR body re-points for 6831, 8088 and 9864 stay with the main side.
- Heartbeat: 2026-10-14T22:43Z (claim taken).
- Heartbeat: 2026-10-14 (all five subagents reported; main side verified tree identities and wrote the report).
- Status: DONE 2026-10-14. Deliverables: reports/pr-prepare-tip-b0c6d60-round-1.md and the 15 draft files under pr-drafts/ that changed (core-admin/SOLR-16725, solrcloud/SOLR-14919, search-components/SOLR-5394, 6759, 6831, 7390, 8003, 8051, 8088, 9396, 9595, 9864, 10305, 10424, 10492). The other four tickets stay as they were (SYNC or unchanged HOLD). No receipts, gates, live PR bodies or fork branches touched.
