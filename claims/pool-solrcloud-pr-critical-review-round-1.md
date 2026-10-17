# Claim: SolrCloud PR drafts, critical review with JIRA context, round 1 (pr-prepare)

Status: STALE 2026-10-17 (no heartbeat within 2 hours, WORKFLOW.md rule 4; linked job none: none linked)

- Claimed by: windows (review agent), Claude Code on this host, working for Nick Shanin
- Capability tags: review
- Assignment: none posted. Direction from Nick, 2026-10-13: the SolrCloud PRs are to be submitted, so run another review round, be critical, and read the JIRA context for each ticket.
- Staffing: four read-only subagents, about five drafts each. Each subagent checks its drafts against the JIRA text (fetched read-only into the scratchpad), the receipt, the fork tip, and the code at the fork tip and at the merge-base.
- Scope: the nineteen drafts in `pr-drafts/solrcloud/`. For each draft: the JIRA problem and request against the draft's symptom; the draft's claims against its receipt; whether the head is current; the formula's rules (length guide, simple language, header and footer, named Limits, a Choice only for a live alternative); and a critical verdict, READY, HOLD (the item that holds it), or NOT READY, with what a maintainer would push back on.
- Limits: no PR opened, no comment, no JIRA write, no email, nothing posted. No build, test, or gate. The vm2 gate records are read as records, not re-run. Commit as Nick Shanin, no trailers, no em dashes.
- Deliverable: `reports/solrcloud-pr-critical-review-round-1.md`, verdict first, one row per ticket.
- Heartbeat: 2026-10-13T02:23Z (claim taken).
- Status: CLAIMED.
